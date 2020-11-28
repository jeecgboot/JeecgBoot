package org.jeecg.boot.starter.rabbitmq.core;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

public class MapMessageConverter implements MessageConverter {
    @Override
    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        return new Message(object.toString().getBytes(), messageProperties);
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        String contentType = message.getMessageProperties().getContentType();
        if (null != contentType && contentType.contains("text")) {
            return new String(message.getBody());
        } else {
            ObjectInputStream objInt = null;
            try {
                ByteArrayInputStream byteInt = new ByteArrayInputStream(message.getBody());
                objInt = new ObjectInputStream(byteInt);
                //byte[]转map
                Map map = (HashMap) objInt.readObject();
                return map;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;

    }
}
