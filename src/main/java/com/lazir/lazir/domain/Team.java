package com.lazir.lazir.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
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

import com.lazir.lazir.config.PrincipalDetail;

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

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    private String context;

    @Column(unique = true, nullable = false)
    private String URL;

    @ManyToOne
    @JoinColumn
    private Account manager;    //방장 account객체

    @ManyToMany   //조인테이블에 추가적인 데이터 변경은 일어나지 않을것 같다.
    private Set<Account> members = new HashSet<>();   //참가자 객체

    @ManyToMany
    private Set<Account> waitting = new HashSet<>();

    // @ManyToMany
    // private Set<Tag> tags = new HashSet<>();

    private String image;

    private Integer memberLimit;

    private boolean recruiting; //인원 모집 여부

    private boolean published; //공개 여부

    private boolean closed; //팀종료 여부

    private LocalDateTime recruitUpdateTime;

    private LocalDateTime createTime;    //생성일

    private LocalDateTime closedTime;

    public boolean isMember(PrincipalDetail principalDetail){
        return this.members.contains(principalDetail.getAccount());
    }

    public boolean isManager(PrincipalDetail principalDetail){
        return this.manager.equals(principalDetail.getAccount());
    }

    public boolean isJoinable(PrincipalDetail principalDetail){
        Account account = principalDetail.getAccount();
        return this.isPublished() && this.isRecruiting()
        && !this.members.contains(account) && !this.manager.equals(account) && !this.waitting.contains(account)
        && (this.members.size()+1) < this.memberLimit;
    }

    public boolean isWaitting(PrincipalDetail principalDetail){
        return this.waitting.contains(principalDetail.getAccount());
    }

    public boolean isAcceptable(){
        return (this.members.size()+1) < this.memberLimit;
    }

    public boolean canUpdateRecruiting() {
        return this.published && this.recruitUpdateTime == null || this.recruitUpdateTime.isBefore(LocalDateTime.now().minusMinutes(1));
    }
}
