package org.rf.rfserver.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.rf.rfserver.chat.dto.ChatRes;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatSubscriber implements MessageListener {
    private final ObjectMapper objectMapper;
    private final RedisTemplate redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
        ChatRes chatRes = new ChatRes();
        try {
            chatRes = objectMapper.readValue(publishMessage, ChatRes.class);
        } catch(Exception e) {

        }
        messagingTemplate.convertAndSend("/sub/channel/"+chatRes.getPartyId(), chatRes);
    }
}