package com.serdarsenturk.moonstarbooking.data.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String flightCode;

    @ManyToOne(fetch = FetchType.LAZY)
    private Aircraft aircraft;

    @ManyToOne(fetch = FetchType.LAZY)
    private Airport fromAirport;

    @ManyToOne(fetch = FetchType.LAZY)
    private Airport toAirport;

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL)
    private List<CheckIn> checkIns;

    private String fromAirportName;

    private String toAirportName;

    private LocalDate departureDate;

    private LocalTime departureTime;

    private LocalDate arrivalDate;

    private LocalTime arrivalTime;

    private String aircraftCode;

    private Integer cost;

    public Flight(){}

    public Flight(Integer cost, String flightCode, LocalDate departureDate, LocalDate arrivalDate, Aircraft aircraft, Airport fromAirport, Airport toAirport, LocalTime departureTime, LocalTime arrivalTime){
        this.flightCode = flightCode;
        this.cost = cost;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.arrivalDate = arrivalDate;
        this.arrivalTime = arrivalTime;
        this.aircraft = aircraft;
        this.fromAirport = fromAirport;
        this.toAirport = toAirport;
        this.fromAirportName = fromAirport.getName();
        this.toAirportName = toAirport.getName();
        this.aircraftCode = aircraft.getAircraftCode();
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

    public String getFlightCode() {
        return flightCode;
    }

    public void setFlightCode(String flightCode) {
        this.flightCode = flightCode;
    }

    public Airport getFromAirport() {
        return fromAirport;
    }

    public void setFromAirport(Airport fromAirport) {
        this.fromAirport = fromAirport;
    }

    public Airport getToAirport() {
        return toAirport;
    }

    public void setToAirport(Airport toAirport) {
        this.toAirport = toAirport;
    }

    public String getFromAirportName() {
        return fromAirportName;
    }

    public void setFromAirportName(String fromAirportName) {
        this.fromAirportName = fromAirportName;
    }

    public String getToAirportName() {
        return toAirportName;
    }

    public void setToAirportName(String toAirportName) {
        this.toAirportName = toAirportName;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public String getAircraftCode() {
        return aircraftCode;
    }

    public void setAircraftCode(String aircraftCode) {
        this.aircraftCode = aircraftCode;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public List<CheckIn> getCheckIns() {
        return checkIns;
    }

    public void setCheckIns(List<CheckIn> checkIns) {
        this.checkIns = checkIns;
    }
}
