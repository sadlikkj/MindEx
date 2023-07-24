package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure read(String employeeId) {
        LOG.debug("Reading reporting structure for employee with id [{}]", employeeId);

        Employee employee = employeeRepository.findByEmployeeId(employeeId);
        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + employeeId);
        }

        employee = populateDirectReports(employee);

        int numberOfReports = getNumberOfReports(employeeId);

        ReportingStructure reportingStructure = new ReportingStructure();
        reportingStructure.setEmployee(employee);
        reportingStructure.setNumberOfReports(numberOfReports);

        return reportingStructure;
    }
    
    private Employee populateDirectReports(Employee employee) {
        List<Employee> directReports = employee.getDirectReports();
        if (directReports == null) {
            return employee;
        }

        List<Employee> fullDirectReports = new ArrayList<Employee>();
        for (Employee directReport : directReports) {
            directReport = employeeRepository.findByEmployeeId(directReport.getEmployeeId());
            directReport = populateDirectReports(directReport);
            fullDirectReports.add(directReport);
        }

        employee.setDirectReports(fullDirectReports);
        return employee;
    }

    private int getNumberOfReports(String id) {
        Employee employee = employeeRepository.findByEmployeeId(id);
        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        List<Employee> directReports = employee.getDirectReports();
        if (directReports == null) {
            return 0;
        }

        int numberOfReports = directReports.size();
        for (Employee directReport : directReports) {
            numberOfReports += getNumberOfReports(directReport.getEmployeeId());
        }

        return numberOfReports;
    }
}
