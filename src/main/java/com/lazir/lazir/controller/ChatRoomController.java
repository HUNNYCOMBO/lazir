// package com.lazir.lazir.controller;

// import com.lazir.lazir.repository.ChatRoomRepository;

// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.servlet.ModelAndView;
// import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// import lombok.RequiredArgsConstructor;

// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;


// @Controller
// @RequiredArgsConstructor
// @RequestMapping("/chat")
// public class ChatRoomController {
    
//     private final ChatRoomRepository repository;

//     @PostMapping(value="/room")
//     public String create(@RequestBody String name, RedirectAttributes attributes) {
        
//         attributes.addFlashAttribute("roomName", repository.createChatRoom(name));
//         return "redirect:/chat/rooms";
//     }

//     @GetMapping("/room")
//     public void getRoom(String roomId, Model model){
//         model.addAttribute("room", repository.findRoomById(roomId));
//     }

//     @GetMapping("/rooms")
//     public ModelAndView rooms(){
//         ModelAndView mv = new ModelAndView("chat/rooms");

//         mv.addObject("list", repository.findAllRooms());

//         return mv;
//     }
    
// }
