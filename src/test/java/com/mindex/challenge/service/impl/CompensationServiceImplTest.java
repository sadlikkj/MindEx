package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String compensationUrl;
    private String compensationIdUrl;

    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private CompensationService compensationService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        compensationUrl = "http://localhost:" + port + "/employee/{id}/compensation";
    }

    @Test
    public void testCreateReadUpdate() {
        String id = "16a596ae-edd3-4847-99fe-c4518e82c86f";

        Compensation testCompensation = new Compensation();
        testCompensation.setSalary(12345.67);
        
        // Create checks
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Compensation createdCompensation =
                restTemplate.exchange(compensationUrl,
                        HttpMethod.PUT,
                        new HttpEntity<Compensation>(testCompensation, headers),
                        Compensation.class,
                        id).getBody();
        
        assertNotNull(createdCompensation.getEmployee());
        assertEquals(createdCompensation.getEmployee().getEmployeeId(), testCompensation.getEmployee().getEmployeeId());
        assertEquals(createdCompensation.getSalary(), testCompensation.getSalary(), .0001);
        assertNotNull(createdCompensation.getEffectiveDate());

        // Read checks
        Compensation readCompensation = restTemplate.getForEntity(compensationIdUrl, Compensation.class, createdCompensation.getEmployee().getEmployeeId()).getBody();
        assertNotNull(readCompensation.getEmployee());
        assertEquals(readCompensation.getEmployee().getEmployeeId(), testCompensation.getEmployee().getEmployeeId());
        assertEquals(readCompensation.getSalary(), testCompensation.getSalary(), .0001);
        assertNotNull(readCompensation.getEffectiveDate());
    }
}
