package com.lazir.lazir.domain;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
public class Board {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    private String content;

    private Long count;

    @CreationTimestamp
    private Timestamp createDate;

    private Timestamp updateDate;

    @ManyToOne
    @JoinColumn(name = "accountId") //연관관계의 주인 (FK)
    private Account account;        //조인테이블. 어카운트 1 : 게시판 N

    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER)  //Reply.java의 Board 변수명. 연관관계의 주인이 아님.
    private Set<Reply> reply = new HashSet<>();       //set과 list의 차이. equalsandhash 어노테이션과 관련있는데, equals부담을 줄이기 위함.
}
