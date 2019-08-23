package com.wby.Community.Event;

import com.alibaba.fastjson.JSONObject;
import com.wby.Community.entity.Event;
import com.wby.Community.entity.Message;
import com.wby.Community.service.MessageService;
import com.wby.Community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class EventConsumer implements CommunityConstant {
   //发送消息就是向某人发一条消息就是message表里插入数据
    @Autowired
    MessageService messageService ;

    @KafkaListener(topics={TOPIC_COMMENT,TOPIC_FOLLOW,TOPIC_LIKE})

    public void handleCommentMessage(ConsumerRecord record ){

    Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {

        return;
    }

    // 发送站内通知
    Message message = new Message();
        message.setFromId(SYSTEM_USER_ID);
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic());
        message.setCreateTime(new Date());

    Map<String, Object> content = new HashMap<>();
        content.put("userId", event.getUserId());
        content.put("entityType", event.getEntityType());
        content.put("entityId", event.getEntityId());

        if (!event.getData().isEmpty()) {
        for (Map.Entry<String, Object> entry : event.getData().entrySet()) {
            content.put(entry.getKey(), entry.getValue());
        }
    }

        message.setContent(JSONObject.toJSONString(content));
        messageService.addMessage(message);
}



    }










