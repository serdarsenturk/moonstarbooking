package com.serdarsenturk.moonstarbooking.data.repository;

import com.serdarsenturk.moonstarbooking.data.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ICompanyRepository extends JpaRepository<Company, Integer> {
        List<Company> findByNameStartsWithIgnoreCase(String name);
}