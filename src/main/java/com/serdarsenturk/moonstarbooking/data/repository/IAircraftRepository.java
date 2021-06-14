package com.serdarsenturk.moonstarbooking.data.repository;

import com.serdarsenturk.moonstarbooking.data.entity.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAircraftRepository extends JpaRepository<Aircraft, Integer> {

}
