package com.serdarsenturk.moonstarbooking.data.service;

import com.serdarsenturk.moonstarbooking.data.entity.Flight;
import com.serdarsenturk.moonstarbooking.data.repository.IFlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

@Service
public class FlightService extends CrudService<Flight, Integer> {
    private final IFlightRepository repository;

    public FlightService(@Autowired IFlightRepository repository){
        this.repository = repository;
    }

    @Override
    protected IFlightRepository getRepository(){
        return repository;
    }
}
