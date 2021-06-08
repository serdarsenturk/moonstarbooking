package com.serdarsenturk.moonstarbooking;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Company {
    @Id
    @GeneratedValue
    private int id;

    private String name;
    private String owner;

    protected Company(){}

    public Company(String name, String owner){
        this.name = name;
        this.owner = owner;
    }

    public int getId(){
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

    @Override
    public String toString(){
        return String.format("Company[id=%d, name='%s', owner='%s']", id, name, owner);
    }
}
