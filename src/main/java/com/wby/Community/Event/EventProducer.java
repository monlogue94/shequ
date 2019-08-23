package com.wby.Community.Event;

import com.alibaba.fastjson.JSONObject;
import com.wby.Community.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventProducer {
    @Autowired
    private KafkaTemplate kafkaTemplate ;
//处理事件也就是发送消息
    public void  fireEvent(Event event ){
        //将事件发布到指定topic,还要包含event的所有数据也就是发送一条数据xxx点赞了你的帖子string类型.把event转化为json
        //本来要发字符串，包含even的所有数据，把event转为json，消费者再转为string
         kafkaTemplate .send(event .getTopic(), JSONObject.toJSONString(event ));

    }


}
