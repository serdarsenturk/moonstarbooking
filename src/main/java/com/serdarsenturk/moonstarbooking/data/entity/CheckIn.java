package com.serdarsenturk.moonstarbooking.data.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class CheckIn{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer cost;

    @ManyToOne(fetch = FetchType.LAZY)
    private Flight flight;

    @ManyToOne(fetch = FetchType.LAZY)
    private Passenger passenger;

    private LocalDate createdAt;

    private String flightCode;

    public CheckIn(){};

    public CheckIn(Integer cost, Flight flight, Passenger passenger, LocalDate createdAt){
        this.cost = cost;
        this.flight = flight;
        this.passenger = passenger;
        this.createdAt = createdAt;
        this.flightCode = flight.getFlightCode();
    }

    public Integer getId() {
        return id;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getFlightCode() {
        return flightCode;
    }

    public void setFlightCode(String flightCode) {
        this.flightCode = flightCode;
    }
}
