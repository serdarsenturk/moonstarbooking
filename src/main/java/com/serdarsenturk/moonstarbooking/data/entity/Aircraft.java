package com.serdarsenturk.moonstarbooking.data.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Aircraft{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    private Integer capacity;

    private String companyName;

    @OneToMany(mappedBy = "aircraft", cascade = CascadeType.ALL)
    private List<Flight> flights;

    public Aircraft(){}

    public Aircraft(Company company, Integer capacity) {
        this.company = company;
        this.capacity = capacity;
        this.companyName = company.getName();
    }

    public Integer getId() {
        return id;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getCompanyName() {
        return companyName;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }

    @Override
    public String toString(){
        return String.format("Aircraft[id=%d, capacity=%d]", id, capacity);
    }

}
