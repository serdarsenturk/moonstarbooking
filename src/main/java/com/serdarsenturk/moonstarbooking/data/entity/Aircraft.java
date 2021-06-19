package com.serdarsenturk.moonstarbooking.data.entity;

import javax.persistence.*;

@Entity
public class Aircraft{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "companyId")
    private Company company;

    private Integer capacity;

    public Aircraft(){}

    public Aircraft(Company company, Integer capacity) {
        this.company = company;
        this.capacity = capacity;
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

    @Override
    public String toString(){
        return String.format("Aircraft[id=%d, capacity=%d, companyId=%s]", id, capacity);
    }

}
