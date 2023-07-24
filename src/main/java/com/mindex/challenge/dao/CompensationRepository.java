package com.mindex.challenge.dao;

import com.mindex.challenge.data.CompensationStorage;

import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface CompensationRepository extends MongoRepository<CompensationStorage, String> {
    CompensationStorage findByEmployeeId(String employeeId);
}
