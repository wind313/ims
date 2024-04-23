package com.yjc.platform.listener;

import com.yjc.client.Sender;
import com.yjc.common.model.PrivateMessageInfo;
import com.yjc.platform.constants.AiConstant;
import com.yjc.platform.pojo.PrivateMessage;
import com.yjc.platform.service.AiService;
import com.yjc.platform.util.BeanUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AiListener {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Autowired
    private AiService aiService;

    @Autowired
    private Sender sender;

    @PostConstruct
    public void init() {
        Queue queue = new org.springframework.amqp.core.Queue(AiConstant.queueName, true);
        DirectExchange exchange = new DirectExchange(AiConstant.exchangeName);
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(AiConstant.key);
        amqpAdmin.declareQueue(queue);
        amqpAdmin.declareExchange(exchange);
        amqpAdmin.declareBinding(binding);

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(rabbitTemplate.getConnectionFactory());
        container.setQueueNames(AiConstant.queueName);
        container.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                PrivateMessageInfo privateMessageInfo = (PrivateMessageInfo) messageConverter.fromMessage(message);
                PrivateMessage response = aiService.response(privateMessageInfo.getSendId(), privateMessageInfo.getReceiveId(), privateMessageInfo.getContent());
                PrivateMessageInfo responseInfo = BeanUtil.copyProperties(response, PrivateMessageInfo.class);
                sender.sendPrivateMessage(responseInfo);
            }
        });
        container.start();
    }
}
