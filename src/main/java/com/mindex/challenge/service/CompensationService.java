package com.mindex.challenge.service;

import com.mindex.challenge.data.Compensation;

public interface CompensationService {
    Compensation create(String id, Compensation compensation);
    Compensation read(String id);
}
