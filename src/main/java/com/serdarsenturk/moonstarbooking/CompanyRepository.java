package com.serdarsenturk.moonstarbooking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
        List<Company> findByNameStartsWithIgnoreCase(String name);
}
