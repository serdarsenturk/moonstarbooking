package com.serdarsenturk.moonstarbooking.data.repository;

import com.serdarsenturk.moonstarbooking.data.entity.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IAirportRepository extends JpaRepository<Airport, Integer> {
    List<Airport> findByNameStartsWithIgnoreCase(String name);
}
