package com.y2sg.wtm.domain.pet.domain;

import com.y2sg.wtm.domain.common.BaseEntity;
import com.y2sg.wtm.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String imageUrl;

    @Enumerated(value = EnumType.STRING)
    private Type type;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private int experience = 0;

    @Builder
    public Pet(String name, String imageUrl, User user, Type type, int experience) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.type = type;
        this.user = user;
        this.experience = experience;
    }

}
