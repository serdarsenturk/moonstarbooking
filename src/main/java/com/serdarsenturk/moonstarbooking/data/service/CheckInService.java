package com.serdarsenturk.moonstarbooking.data.service;

import com.serdarsenturk.moonstarbooking.data.entity.CheckIn;
import com.serdarsenturk.moonstarbooking.data.repository.ICheckInRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

@Service
public class CheckInService extends CrudService<CheckIn, Integer> {
    private final ICheckInRepository repository;

    public CheckInService(@Autowired ICheckInRepository repository){
        this.repository = repository;
    }

    @Override
    protected ICheckInRepository getRepository(){
        return repository;
    }
}
