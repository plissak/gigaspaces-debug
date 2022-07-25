package gs.debug.hnpe.server.processor;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.openspaces.core.GigaSpace;
import org.openspaces.events.EventExceptionHandler;
import org.openspaces.events.ListenerExecutionFailedException;
import org.openspaces.events.SpaceDataEventListener;
import org.openspaces.events.notify.SimpleNotifyEventListenerContainer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.TransactionStatus;

import com.gigaspaces.client.ReadModifiers;
import com.gigaspaces.client.iterator.SpaceIteratorConfiguration;
import com.gigaspaces.client.iterator.SpaceIteratorType;
import com.gigaspaces.events.NotifyActionType;
import com.gigaspaces.events.batching.BatchRemoteEvent;
import com.j_spaces.core.client.EntryArrivedRemoteEvent;

import gs.debug.hnpe.common.domain.Widget;
import gs.debug.hnpe.common.service.DebugWriteAccess;
import gs.debug.hnpe.common.util.WidgetUtil;
import net.jini.core.event.RemoteEvent;
import net.jini.lease.LeaseListener;
import net.jini.lease.LeaseRenewalEvent;

@SuppressWarnings("deprecation")
public class WidgetProcessor implements InitializingBean, DisposableBean {
	public static final int DEFAULT_BATCH_SIZE = 1000;
    public static final int DEFAULT_BATCH_TIMEOUT_MS = 200;

	private GigaSpace space;
	private DebugWriteAccess writeAccess;

	private SimpleNotifyEventListenerContainer container;
	private Logger logger = Logger.getLogger(getClass());

	@Required
	public void setSpace(GigaSpace space) {
		this.space = space;
	}

	@Autowired
	public void setWriteAccess(DebugWriteAccess writeAccess) {
		this.writeAccess = writeAccess;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		// create space objects
		for (int i = 0; i < 100; i++) {
			Widget widget = WidgetUtil.randomWidget();
			Widget saved = writeAccess.write(widget);

			if (saved == null || saved.getId() == null) {
				throw new Exception("Missing identifier");
			}
		}

		// read space objects
		for (Iterator<Widget> iterator = space.iterator(new Widget(), getIteratorConfiguration()); iterator.hasNext();) {
			process(iterator.next(), false);
		}

		// listen for space objects
		createContainer();
	}

	@Override
	public synchronized void destroy() throws Exception {
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

	private SpaceIteratorConfiguration getIteratorConfiguration() {
		SpaceIteratorConfiguration config = new SpaceIteratorConfiguration();
		config.setBatchSize(DEFAULT_BATCH_SIZE);
		config.setIteratorType(SpaceIteratorType.PREFETCH_UIDS); // w/o this the ISpaceFilter process method won't have a security context
		config.setReadModifiers(ReadModifiers.READ_COMMITTED); // this is how GSIterator behaved by default
		return config;
	}

	protected synchronized void createContainer() {
		container = new SimpleNotifyEventListenerContainer();
		container.setGigaSpace(space);
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

		if (! space.getSpace().isEmbedded()) {
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
		container.setBatchSize(DEFAULT_BATCH_SIZE);
		container.setBatchTime(DEFAULT_BATCH_TIMEOUT_MS);

		container.afterPropertiesSet();
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
