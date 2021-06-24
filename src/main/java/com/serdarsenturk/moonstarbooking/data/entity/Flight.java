package com.serdarsenturk.moonstarbooking.data.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "aircraft_id")
    private Aircraft aircraft;

    private String flight_code;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "from_airport_id")
    private Airport from_airport;

    private String from_airport_name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "to_airport_id")
    private Airport to_airport;

    private String to_airport_name;

    private LocalDate departure_date;

    private LocalDate arrival_date;

    private Integer cost;

    public Flight(){}

    public Flight(String flight_code, LocalDate departure_date, LocalDate arrival_date, Integer cost, Aircraft aircraft, Airport from_airport, Airport to_airport){
        this.flight_code = flight_code;
        this.departure_date = departure_date;
        this.arrival_date = arrival_date;
        this.cost = cost;
        this.aircraft = aircraft;
        this.from_airport_name = from_airport.getName();
        this.to_airport_name = to_airport.getName();
    }

    public Integer getId() {
        return id;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    public String getFlight_code() {
        return flight_code;
    }

    public void setFlight_code(String flight_code) {
        this.flight_code = flight_code;
    }

    public LocalDate getDeparture_date() {
        return departure_date;
    }

    public void setDeparture_date(LocalDate departure_date) {
        this.departure_date = departure_date;
    }

    public LocalDate getArrival_date() {
        return arrival_date;
    }

    public void setArrival_date(LocalDate arrival_date) {
        this.arrival_date = arrival_date;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Airport getFrom_airport() {
        return from_airport;
    }

    public void setFrom_airport(Airport from_airport) {
        this.from_airport = from_airport;
    }

    public Airport getTo_airport() {
        return to_airport;
    }

    public void setTo_airport(Airport to_airport) {
        this.to_airport = to_airport;
    }

    public String getFrom_airport_name() {
        return from_airport_name;
    }

    public String getTo_airport_name() {
        return to_airport_name;
    }

    public void setTo_airport_name(String to_airport_name) {
        this.to_airport_name = to_airport_name;
    }
}
