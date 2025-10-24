package africa.enumverse.lrs.listener;

import africa.enumverse.lrs.service.EventPublisherService.StatementCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Listens for statement created events and can trigger analytics updates,
 * notifications, or other side effects
 */
@Component
@Slf4j
public class StatementEventListener {

    @EventListener
    @Async
    public void handleStatementCreated(StatementCreatedEvent event) {
        log.info("Statement created event received: {}", event.getStatement().getId());

        // Here you can:
        // 1. Update real-time analytics dashboards
        // 2. Send notifications
        // 3. Trigger other business processes
        // 4. Update caches
        // 5. Send to external systems (e.g., webhooks, message queues)

        log.debug("Actor: {}, Verb: {}, Activity: {}",
                event.getStatement().getActor() != null ? event.getStatement().getActor().getName() : "unknown",
                event.getStatement().getVerb() != null ? event.getStatement().getVerb().getId() : "unknown",
                event.getStatement().getObject() != null ? event.getStatement().getObject().getId() : "unknown");
    }
}

