package com.banking.loan.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "loan.events.exchange";
    public static final String QUEUE_APPLICATION   = "loan.application.submitted";
    public static final String QUEUE_CREDIT        = "loan.credit.check.completed";
    public static final String QUEUE_APPROVAL      = "loan.approval.decision";
    public static final String QUEUE_DISBURSEMENT  = "loan.disbursement.completed";
    public static final String QUEUE_NOTIFICATION  = "loan.notification";

    public static final String ROUTING_APPLICATION  = "loan.application";
    public static final String ROUTING_CREDIT       = "loan.credit";
    public static final String ROUTING_APPROVAL     = "loan.approval";
    public static final String ROUTING_DISBURSEMENT = "loan.disbursement";
    public static final String ROUTING_NOTIFICATION = "loan.notify";

    @Bean
    public TopicExchange loanExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean public Queue applicationQueue()  { return new Queue(QUEUE_APPLICATION, true); }
    @Bean public Queue creditQueue()       { return new Queue(QUEUE_CREDIT, true); }
    @Bean public Queue approvalQueue()     { return new Queue(QUEUE_APPROVAL, true); }
    @Bean public Queue disbursementQueue() { return new Queue(QUEUE_DISBURSEMENT, true); }
    @Bean public Queue notificationQueue() { return new Queue(QUEUE_NOTIFICATION, true); }

    @Bean public Binding applicationBinding()  { return BindingBuilder.bind(applicationQueue()).to(loanExchange()).with(ROUTING_APPLICATION); }
    @Bean public Binding creditBinding()       { return BindingBuilder.bind(creditQueue()).to(loanExchange()).with(ROUTING_CREDIT); }
    @Bean public Binding approvalBinding()     { return BindingBuilder.bind(approvalQueue()).to(loanExchange()).with(ROUTING_APPROVAL); }
    @Bean public Binding disbursementBinding() { return BindingBuilder.bind(disbursementQueue()).to(loanExchange()).with(ROUTING_DISBURSEMENT); }
    @Bean public Binding notificationBinding() { return BindingBuilder.bind(notificationQueue()).to(loanExchange()).with(ROUTING_NOTIFICATION); }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
