package com.wby.Community;

import com.wby.Community.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class )
public class MailTests {

    @Autowired
    private MailClient mailClient ;
    @Test
    public void test() throws MessagingException {
        mailClient .sendMail("593847567@qq.com,", "你好","dddd");

    }
}

