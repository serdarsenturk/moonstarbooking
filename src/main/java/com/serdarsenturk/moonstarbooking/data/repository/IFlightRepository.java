package com.serdarsenturk.moonstarbooking.data.repository;

import com.serdarsenturk.moonstarbooking.data.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IFlightRepository extends JpaRepository<Flight, Integer> {
    
}
