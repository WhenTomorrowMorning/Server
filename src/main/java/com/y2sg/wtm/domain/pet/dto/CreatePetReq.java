package com.y2sg.wtm.domain.pet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreatePetReq {

    @Schema(type = "String", description = "펫 이름")
    @NotNull
    private String name;

    @Schema(type = "String", description = "펫 사진")
    private String imageUrl;

    @Schema(type = "String", description = "펫 타입")
    @NotNull
    private String type;

}
