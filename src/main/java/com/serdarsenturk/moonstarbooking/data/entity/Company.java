package com.serdarsenturk.moonstarbooking.data.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;
    private String owner;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Aircraft> aircrafts;

    protected Company(){}

    public Company(String name, String owner){
        this.name = name;
        this.owner = owner;
    }

    public Integer getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getOwner(){
        return owner;
    }

    public void setOwner(String owner){
        this.owner = owner;
    }

    public List<Aircraft> getAircrafts() {
        return aircrafts;
    }

    public void setAircrafts(List<Aircraft> aircrafts) {
        this.aircrafts = aircrafts;
    }

    @Override
    public String toString(){
        return String.format("Company[id=%d, name='%s', owner='%s']", id, name, owner);
    }

}
