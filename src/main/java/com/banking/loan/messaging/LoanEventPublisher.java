package com.banking.loan.messaging;

import com.banking.loan.config.RabbitMQConfig;
import com.banking.loan.model.LoanApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class LoanEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(LoanEventPublisher.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Async
    public void publishApplicationSubmitted(LoanApplication app) {
        LoanEvent event = buildEvent("APPLICATION_SUBMITTED", app,
            "Your loan application " + app.getApplicationNumber() + " has been submitted successfully.");
        publish(RabbitMQConfig.ROUTING_APPLICATION, event);
    }

    @Async
    public void publishCreditCheckCompleted(LoanApplication app, int creditScore) {
        LoanEvent event = buildEvent("CREDIT_CHECK_COMPLETED", app,
            "Credit check completed. Credit score: " + creditScore);
        publish(RabbitMQConfig.ROUTING_CREDIT, event);
    }

    @Async
    public void publishApprovalDecision(LoanApplication app, String decision) {
        LoanEvent event = buildEvent("APPROVAL_DECISION", app,
            "Loan application " + app.getApplicationNumber() + " has been " + decision + ".");
        publish(RabbitMQConfig.ROUTING_APPROVAL, event);
    }

    @Async
    public void publishDisbursementCompleted(LoanApplication app, String accountNumber) {
        LoanEvent event = buildEvent("DISBURSEMENT_COMPLETED", app,
            "Loan disbursed. Account number: " + accountNumber);
        publish(RabbitMQConfig.ROUTING_DISBURSEMENT, event);
    }

    @Async
    public void publishNotification(LoanApplication app, String message) {
        LoanEvent event = buildEvent("NOTIFICATION", app, message);
        publish(RabbitMQConfig.ROUTING_NOTIFICATION, event);
    }

    private LoanEvent buildEvent(String type, LoanApplication app, String message) {
        return new LoanEvent(
            type,
            app.getApplicationNumber(),
            app.getApplicationId(),
            app.getApplicant().getEmail(),
            app.getApplicant().getFullName(),
            app.getStatus().name(),
            message
        );
    }

    private void publish(String routingKey, LoanEvent event) {
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, routingKey, event);
            log.info("Published event [{}] for application: {}", event.getEventType(), event.getApplicationNumber());
        } catch (Exception e) {
            log.error("Failed to publish event [{}]: {}", event.getEventType(), e.getMessage());
        }
    }
}
