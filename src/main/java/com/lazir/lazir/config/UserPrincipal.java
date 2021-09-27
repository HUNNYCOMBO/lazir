// package com.lazir.lazir.config;

// import java.time.LocalDateTime;
// import java.util.List;

// import com.lazir.lazir.domain.Account;

// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.userdetails.User;

// import lombok.Getter;

// //principal 그자체가 될 user를 상속받은 객체
// @Getter
// public class UserPrincipal extends User{

//     private Account account;
//     private String email;
//     private LocalDateTime createTokenTime;

//     public UserPrincipal(Account account) {
//         super(account.getNickname(), account.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_"+account.getRole())));
//         this.account = account;
//         this.email = account.getEmail();
//         this.createTokenTime = account.getCreateTokenTime();
//     }
    
// }
