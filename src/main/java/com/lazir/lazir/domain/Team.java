package com.lazir.lazir.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@Entity
public class Team {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    private String context;
    
    @ManyToOne
    @JoinColumn(name = "manager")
    private Account manager;    //방장 account객체

    @ManyToMany     //조인테이블에 추가적인 데이터 변경은 일어나지 않을것 같다.
    private Set<Account> members;   //참가자 객체

    @ManyToMany
    private Set<Tag> tags;

    @OneToMany(mappedBy = "team")
    private Set<Reply> replys;

}
