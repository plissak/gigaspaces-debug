package gs.debug.hnpe.server.processor;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.space.mode.AfterSpaceModeChangeEvent;
import org.openspaces.core.space.mode.PostPrimary;
import org.openspaces.events.EventExceptionHandler;
import org.openspaces.events.ListenerExecutionFailedException;
import org.openspaces.events.SpaceDataEventListener;
import org.openspaces.events.notify.SimpleNotifyEventListenerContainer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.TransactionStatus;

import com.gigaspaces.events.NotifyActionType;
import com.gigaspaces.events.batching.BatchRemoteEvent;
import com.j_spaces.core.client.EntryArrivedRemoteEvent;

import gs.debug.core.common.util.SpaceUtil;
import gs.debug.hnpe.common.domain.Widget;
import gs.debug.hnpe.common.service.DebugReadAccess;
import net.jini.core.event.RemoteEvent;
import net.jini.lease.LeaseListener;
import net.jini.lease.LeaseRenewalEvent;

@SuppressWarnings("deprecation")
public class WidgetSecondaryProcessor implements InitializingBean, DisposableBean {
    private DebugReadAccess readAccess;
	private GigaSpace primarySpace;

	private SimpleNotifyEventListenerContainer container;
	private Logger logger = Logger.getLogger(getClass());
	private boolean isPropertiesSet;

	@Autowired
	public void setReadAccess(DebugReadAccess readAccess) {
		this.readAccess = readAccess;
	}

	@Required
	public void setPrimarySpace(GigaSpace primarySpace) {
		this.primarySpace = primarySpace;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("After properties set...");
		try {
			readAll();
		}
		catch (Throwable t) {
			logger.error("Read exception: " + t.getMessage(), t);
		}
		isPropertiesSet = true;
	}

	@PostPrimary
	public void startPostPrimary(AfterSpaceModeChangeEvent event) {
		if (isPropertiesSet || event.getSpace().isEmbedded()) {
			logger.info("Entering post-primary...");

			try {
				readAll();
			}
			catch (Throwable t) {
				logger.error("Read exception: " + t.getMessage(), t);
			}
			try {
				iterateAndStartListening();
			}
			catch (Throwable t) {
				logger.error("Iterate exception: " + t.getMessage(), t);
			}
		}
		else {
			logger.info("Ignoring post-primary");
		}
	}

	public void readAll() {
		logger.info("Begin read all");
		for (Widget widget : readAccess.getWidgets()) {
			process(widget, false);
		}
		logger.info("End read all");
	}

	public void iterateAndStartListening() {

		// read space objects
		logger.info("Begin iterate all");
		for (Iterator<Widget> iterator = primarySpace.iterator(new Widget(), SpaceUtil.getIteratorConfiguration()); iterator.hasNext();) {
			process(iterator.next(), false);
		}
		logger.info("End iterate all");

		// listen for space objects
		createContainer();
	}

	@Override
	public synchronized void destroy() throws Exception {
		logger.info("Destroying bean...");
		SimpleNotifyEventListenerContainer oldContainer = container;
		if (oldContainer != null) {
			container = null;
			oldContainer.destroy();
		}
	}

	private void process(Widget widget, boolean isDelete) {
		logger.info("Widget " + (isDelete ? "DELETE " : "") + "\"" + widget.getName() + "\"; parts=" + widget.getParts() + "; codes="
				+ widget.getCodes());
	}

	private void processBatch(BatchRemoteEvent batchEvent) {
		for (RemoteEvent event : batchEvent.getEvents()) {
			EntryArrivedRemoteEvent entryEvent = (EntryArrivedRemoteEvent)event;

			boolean isDelete = entryEvent.getNotifyActionType() == NotifyActionType.NOTIFY_TAKE
					|| entryEvent.getNotifyActionType() == NotifyActionType.NOTIFY_LEASE_EXPIRATION;

			try {
				process((Widget)entryEvent.getObject(), isDelete);
			}
			catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	private void processException(ListenerExecutionFailedException exception, Widget data, Object source) {
		logger.error("GigaSpace notification exception [" + this + "]: " + exception.getMessage(), exception);
	}

	protected synchronized void createContainer() {
		logger.info("Begin create container");

		container = new SimpleNotifyEventListenerContainer();
		container.setGigaSpace(primarySpace);
		container.setTemplate(new Widget());
		container.setFifo(true);
		container.setNotifyAll(true);

		container.setExceptionHandler(new EventExceptionHandler<Widget>() {
			@Override
			public void onException(ListenerExecutionFailedException exception, Widget data, GigaSpace space, TransactionStatus txStatus,
					Object source) throws RuntimeException {
				processException(exception, data, source);
			}

			@Override
			public void onSuccess(Widget data, GigaSpace space, TransactionStatus txStatus, Object source) throws RuntimeException {}
		});

		if (! primarySpace.getSpace().isEmbedded()) {
			container.setAutoRenew(true);
			container.setLeaseListener(new LeaseListener() {
				@Override
				public void notify(LeaseRenewalEvent e) {
					handleLeaseRenewalEvent(e);
				}
			});
		}

		container.setEventListener(new SpaceDataEventListener<Object[]>() {
			@Override
			public void onEvent(Object[] objects, GigaSpace gs, TransactionStatus txStatus, Object obj) {
				processBatch((BatchRemoteEvent)obj);
			}
		});
		container.setPassArrayAsIs(true);
		container.setBatchSize(SpaceUtil.DEFAULT_BATCH_SIZE);
		container.setBatchTime(SpaceUtil.DEFAULT_BATCH_TIMEOUT_MS);

		container.afterPropertiesSet();

		logger.info("End create container");
	}

	protected void handleLeaseRenewalEvent(LeaseRenewalEvent event) {
		SimpleNotifyEventListenerContainer theContainer;
		synchronized (this) {
			theContainer = container;
		}
		if (theContainer != null) {
			logger.error("[Lease Renewal Exception] " + this, event.getException());

			try {
				theContainer.stop();
				theContainer.start();
			}
			catch (Throwable t) {
				logger.error("Failed to restart: " + t.getMessage(), t);
			}
		}
	}

}
