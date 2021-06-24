package com.serdarsenturk.moonstarbooking.data.service;

import com.serdarsenturk.moonstarbooking.data.entity.Aircraft;
import com.serdarsenturk.moonstarbooking.data.repository.IAircraftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

@Service
public class AircraftService extends CrudService<Aircraft, Integer> {
    private final IAircraftRepository repository;

    public AircraftService(@Autowired IAircraftRepository repository){
        this.repository = repository;
    }

    @Override
    protected IAircraftRepository getRepository(){
        return repository;
    }
}
