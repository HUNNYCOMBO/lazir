// package com.lazir.lazir.controller;


// import com.lazir.lazir.domain.Chat;

// import org.springframework.messaging.handler.annotation.MessageMapping;
// import org.springframework.messaging.simp.SimpMessagingTemplate;
// import org.springframework.stereotype.Controller;

// import lombok.RequiredArgsConstructor;

// @Controller
// @RequiredArgsConstructor
// public class ChatController {
    
//     private final SimpMessagingTemplate template;

//     @MessageMapping("/chat/enter")
//     public void enter(Chat chat){
//         chat.setMessage(chat.getWriter() + "님이 참여하였습니다.");
//         template.convertAndSend("/sub/chat/room/" + chat.getRoomId(), chat);
//     }

//     @MessageMapping("/chat/message")
//     public void message(Chat message){
//         template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
//     }
// }
