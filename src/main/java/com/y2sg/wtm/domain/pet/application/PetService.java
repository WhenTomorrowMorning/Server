package com.y2sg.wtm.domain.pet.application;

import com.y2sg.wtm.domain.pet.domain.Pet;
import com.y2sg.wtm.domain.pet.domain.Type;
import com.y2sg.wtm.domain.pet.domain.repository.PetRepository;
import com.y2sg.wtm.domain.pet.dto.CreatePetReq;
import com.y2sg.wtm.domain.user.domain.User;
import com.y2sg.wtm.domain.user.domain.repository.UserRepository;
import com.y2sg.wtm.global.DefaultAssert;
import com.y2sg.wtm.global.config.security.token.UserPrincipal;
import com.y2sg.wtm.global.payload.ApiResponse;
import com.y2sg.wtm.global.payload.ErrorCode;
import com.y2sg.wtm.global.payload.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;

    public ResponseEntity<?> createPet(final UserPrincipal userPrincipal, final CreatePetReq createPetReq) {
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isOptionalPresent(user);

        Pet pet = Pet.builder()
                .name(createPetReq.getName())
                .imageUrl(createPetReq.getImageUrl())
                .type(Type.valueOf(createPetReq.getType()))
                .user(user.get())
                .build();

        petRepository.save(pet);

        return ResponseEntity.ok().body(ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("펫이 생성되었습니다").build())
                .build());

    }
}
