package com.serdarsenturk.moonstarbooking.data.repository;

import com.serdarsenturk.moonstarbooking.data.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPassengerRepository extends JpaRepository<Passenger, Integer> {
    List<Passenger> findByFirstNameStartsWithIgnoreCase(String firstName);
}
