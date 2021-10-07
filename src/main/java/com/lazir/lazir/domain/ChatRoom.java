// package com.lazir.lazir.domain;

// import java.util.HashSet;
// import java.util.Set;
// import java.util.UUID;

// import org.springframework.web.socket.WebSocketSession;

// import lombok.Data;

// @Data
// public class ChatRoom {
    
//     private String roomId;
//     private String name;
//     private Set<WebSocketSession> sessions = new HashSet<>();

//     public static ChatRoom create(String name){
//         ChatRoom room = new ChatRoom();

//         room.roomId = UUID.randomUUID().toString();
//         room.name = name;
//         return room;
//     }
// }
