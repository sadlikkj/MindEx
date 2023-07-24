package com.mindex.challenge.data;

import java.time.LocalDate;

public class CompensationStorage {
    private String employeeId;
    private double salary;
    private LocalDate effectivDate;
    
    public CompensationStorage() {
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public LocalDate getEffectivDate() {
        return effectivDate;
    }

    public void setEffectiveDate(LocalDate effectivDate) {
        this.effectivDate = effectivDate;
    }
}
