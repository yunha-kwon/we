package com.akdong.we.member.entity;

import com.akdong.we.member.request.UpdatedMemberInfoRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(value = {AuditingEntityListener.class})
@ToString
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@NotBlank(message = "이메일을 잘 입력해주세요")
    @Email
    private String email;

    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "패스워드를 잘 입력해주세요")
    private String password;

    @NotBlank(message = "닉네임을 잘 입력해주세요")
    private String nickname;

    @CreatedDate
    private LocalDateTime regDate;

    @ColumnDefault("0")
    private boolean isLeaved;

    private LocalDateTime leavedDate;

    @Column(name="userKey", nullable = false)
    private String userKey;

    @Column(name="coupleJoined", nullable = false)
    @ColumnDefault("0")
    private boolean coupleJoined;

    @Column(name="pin")
    private String pin;

    @Column(name="deviceToken")
    private String deviceToken;

    @Column(name="priorAccount")
    private String priorAccount;

//    @ManyToOne
//    @JoinColumn(name = "coupleId", nullable = true)
//    private Couple couple;

    @Builder
    public Member(String email, String password, String nickname, LocalDateTime regDate, LocalDateTime leavedDate, boolean isLeaved,
                  String userKey, boolean coupleJoined, String pin, String deviceToken){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.regDate = regDate;
        this.leavedDate = leavedDate;
        this.isLeaved = isLeaved;
        this.userKey = userKey;
        this.coupleJoined = coupleJoined;
        this.pin = pin;
        this.deviceToken = deviceToken;
    }


    public void update(String password){
        this.password = password;
    }

    public void updateMemberInfo(UpdatedMemberInfoRequest updatedMemberInfoRequest){
        this.password = updatedMemberInfoRequest.getPassword();
        this.nickname = updatedMemberInfoRequest.getNickname();
    }

    public void delete(){
        this.isLeaved = true;
        this.leavedDate = LocalDateTime.now();
    }

}

