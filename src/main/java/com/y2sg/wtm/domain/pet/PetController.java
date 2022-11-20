package com.y2sg.wtm.domain.pet;

import com.y2sg.wtm.domain.pet.application.PetService;
import com.y2sg.wtm.domain.pet.dto.CreatePetReq;
import com.y2sg.wtm.global.config.security.token.CurrentUser;
import com.y2sg.wtm.global.config.security.token.UserPrincipal;
import com.y2sg.wtm.global.payload.ErrorResponse;
import com.y2sg.wtm.global.payload.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "Pet", description = "Pet API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pets")
public class PetController {

    private final PetService petService;

    @Operation(summary = "펫 생성", description = "펫을 생성합니다,")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "펫 생성 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "펫 생성 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping("")
    public ResponseEntity<?> createPet(
            @Parameter(description = "Accesstoken", required = true) @CurrentUser final UserPrincipal userPrincipal,
            @Parameter(description = "CreatePetReq", required = true) @Valid @RequestBody final CreatePetReq createPetReq
    ) {
        return petService.createPet(userPrincipal, createPetReq);
    }
}
