package com.serdarsenturk.moonstarbooking.data.repository;

import com.serdarsenturk.moonstarbooking.data.entity.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICheckInRepository extends JpaRepository<CheckIn, Integer> {
}
