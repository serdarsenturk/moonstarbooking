package com.serdarsenturk.moonstarbooking.data.repository;

import com.serdarsenturk.moonstarbooking.data.entity.Airport;
import com.serdarsenturk.moonstarbooking.data.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface IFlightRepository extends JpaRepository<Flight, Integer> {
    List<Flight> findFlightByFromAirportAndToAirportAndDepartureDate(@Param("from") Airport from , @Param("to") Airport to, @Param("date") LocalDate date);
}
