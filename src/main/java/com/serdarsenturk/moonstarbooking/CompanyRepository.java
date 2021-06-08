package com.serdarsenturk.moonstarbooking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, String> {
        List<Company> findAll();

        List<Company> findCompanyByName(String name);
}
