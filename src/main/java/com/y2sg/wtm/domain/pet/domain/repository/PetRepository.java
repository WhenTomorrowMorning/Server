package com.y2sg.wtm.domain.pet.domain.repository;

import com.y2sg.wtm.domain.pet.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
}
