package com.postgrestoelasticsearch.agregator.services;

import org.apache.kafka.streams.kstream.KTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.postgrestoelasticsearch.agregator.domain.models.ResearchBoost;

@Service
public class AgregatorService {
    
    @Autowired
    private KTable<Integer, ResearchBoost> boostTable;

    public void process() {
        boostTable.toStream().foreach((key, value) -> {
            // Process each record
            System.out.println("Value: " + value);
        });          
    }
}

