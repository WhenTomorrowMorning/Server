package com.y2sg.wtm.domain.user.domain;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.y2sg.wtm.domain.common.BaseEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Email
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    private Integer age;

    private String gender;

    @JsonIgnore
    private String password;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Embedded
    private Address address;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Provider provider;

    private String providerId;


    @Builder
    public User(String name, String email, String nickname, Integer age, String gender, String password, Role role, Provider provider, String providerId, String imageUrl, Address address){
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.age = age;
        this.gender = gender;
        this.password = password;
        this.provider = provider;
        this.providerId = providerId;
        this.role = role;
        this.imageUrl = imageUrl;
        this.address = address;
    }

    public void updateNickName(String nickname){
        this.nickname = nickname;
    }

    public void updateImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

}
