package com.yjc.server.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yjc.common.enums.CommandType;
import com.yjc.common.model.PrivateMessageInfo;
import com.yjc.common.model.ReceiveInfo;
import com.yjc.server.processors.AbstractProcessor;
import com.yjc.server.processors.ProcessorFactory;
import com.yjc.server.websocket.ServerGroup;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@Data
public class PullPrivateMessageTask extends AbstractPullMessageTask {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    MessageConverter messageConverter;

    @Autowired
    private AmqpAdmin amqpAdmin;

    public String queueName = "private";

    public String exchangeName = "yjc-im";

    public String key = "private";

    public void init(){
        Queue queue = new org.springframework.amqp.core.Queue(queueName+ServerGroup.serverId, true);
        DirectExchange exchange = new DirectExchange(exchangeName+ServerGroup.serverId);
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(key+ServerGroup.serverId);
        amqpAdmin.declareQueue(queue);
        amqpAdmin.declareExchange(exchange);
        amqpAdmin.declareBinding(binding);

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(rabbitTemplate.getConnectionFactory());
        container.setQueueNames(queueName+ServerGroup.serverId);
        container.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {

                try {
                    ObjectMapper mapper = new ObjectMapper();
                    Object o = messageConverter.fromMessage(message);
                    String s = mapper.writeValueAsString(o);
                    JavaType javaType = mapper.getTypeFactory().constructParametricType(ReceiveInfo.class, PrivateMessageInfo.class);
                    ReceiveInfo<PrivateMessageInfo> receiveInfo = (ReceiveInfo<PrivateMessageInfo>)mapper.readValue(s, javaType);

                    AbstractProcessor processor = ProcessorFactory.createProcessor(CommandType.PRIVATE_MESSAGE);
                    processor.process(receiveInfo);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

            }
        });
        container.start();
    }
}
