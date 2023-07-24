package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.CompensationStorage;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Compensation create(Compensation compensation) {
        LOG.debug("Creating compensation [{}]", compensation);

        String employeeId = compensation.getEmployee().getEmployeeId();

        CompensationStorage compensationStorage = compensationRepository.findByEmployeeId(employeeId);

        if (compensationStorage != null) {
            throw new RuntimeException("Compensation exists for employeeId: " + employeeId);
        }

        Employee employee = employeeRepository.findByEmployeeId(employeeId);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + employeeId);
        }

        LocalDate effectiveDate = LocalDate.now();

        compensationStorage = new CompensationStorage();
        compensationStorage.setEmployeeId(employeeId);
        compensationStorage.setSalary(compensation.getSalary());
        compensationStorage.setEffectiveDate(effectiveDate);

        compensationRepository.insert(compensationStorage);

        compensation.setEmployee(employee);
        compensation.setEffectiveDate(effectiveDate);

        return compensation;
    }

    @Override
    public Compensation read(String id) {
        LOG.debug("Creating employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        CompensationStorage compensationStorage = compensationRepository.findByEmployeeId(id);

        if (compensationStorage == null) {
            throw new RuntimeException("Invalid employeeId for compensation: " + id);
        }

        Compensation compensation = new Compensation();
        compensation.setEmployee(employee);
        compensation.setSalary(compensationStorage.getSalary());
        compensation.setEffectiveDate(compensationStorage.getEffectivDate());

        return compensation;
    }
}
