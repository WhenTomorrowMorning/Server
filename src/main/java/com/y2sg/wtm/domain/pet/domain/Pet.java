package com.y2sg.wtm.domain.pet.domain;

import com.y2sg.wtm.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String imageUrl;

    @Enumerated(value = EnumType.STRING)
    private Type type;

    private int experience = 0;

    @Builder
    public Pet(String name, String imageUrl, Type type, int experience) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.type = type;
        this.experience = experience;
    }

}
