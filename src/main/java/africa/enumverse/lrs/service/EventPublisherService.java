package africa.enumverse.lrs.service;

import africa.enumverse.lrs.dto.StatementResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * Publishes learning events to other parts of the system
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EventPublisherService {

    private final ApplicationEventPublisher eventPublisher;

    /**
     * Publish statement creation event to other services
     */
    public void publishStatementCreated(StatementResponse statement) {
        log.info("Publishing statement created event: {}", statement.getId());

        // Create and publish event
        StatementCreatedEvent event = new StatementCreatedEvent(this, statement);
        eventPublisher.publishEvent(event);

        log.debug("Statement created event published successfully");
    }

    /**
     * Custom event class for statement creation
     */
    public static class StatementCreatedEvent extends org.springframework.context.ApplicationEvent {
        private final StatementResponse statement;

        public StatementCreatedEvent(Object source, StatementResponse statement) {
            super(source);
            this.statement = statement;
        }

        public StatementResponse getStatement() {
            return statement;
        }
    }
}

