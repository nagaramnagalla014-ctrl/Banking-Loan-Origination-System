package com.banking.loan.messaging;

import com.banking.loan.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class LoanEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(LoanEventConsumer.class);

    @RabbitListener(queues = RabbitMQConfig.QUEUE_APPLICATION)
    public void handleApplicationSubmitted(LoanEvent event) {
        log.info("[APPLICATION_SUBMITTED] {} | Applicant: {} | Status: {}",
            event.getApplicationNumber(), event.getApplicantName(), event.getCurrentStatus());
        sendEmail(event.getApplicantEmail(), "Loan Application Received", event.getMessage());
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_CREDIT)
    public void handleCreditCheckCompleted(LoanEvent event) {
        log.info("[CREDIT_CHECK_COMPLETED] {} | {}",
            event.getApplicationNumber(), event.getMessage());
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_APPROVAL)
    public void handleApprovalDecision(LoanEvent event) {
        log.info("[APPROVAL_DECISION] {} | Applicant: {} | {}",
            event.getApplicationNumber(), event.getApplicantName(), event.getMessage());
        sendEmail(event.getApplicantEmail(), "Loan Application Update", event.getMessage());
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_DISBURSEMENT)
    public void handleDisbursementCompleted(LoanEvent event) {
        log.info("[DISBURSEMENT_COMPLETED] {} | {}",
            event.getApplicationNumber(), event.getMessage());
        sendEmail(event.getApplicantEmail(), "Loan Disbursed Successfully", event.getMessage());
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NOTIFICATION)
    public void handleNotification(LoanEvent event) {
        log.info("[NOTIFICATION] {} | {}",
            event.getApplicationNumber(), event.getMessage());
        sendEmail(event.getApplicantEmail(), "Loan Update", event.getMessage());
    }

    private void sendEmail(String to, String subject, String body) {
        // Email integration point — logs for demo purposes
        log.info("EMAIL -> To: {} | Subject: {} | Body: {}", to, subject, body);
    }
}
