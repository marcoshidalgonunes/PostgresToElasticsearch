package com.postgrestoelasticsearch.agregator.services;

import org.apache.kafka.streams.kstream.KTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Service;

import com.postgrestoelasticsearch.agregator.domain.models.ResearchBoost;

@Service
public class AgregatorService {

    @Autowired
    private StreamsBuilderFactoryBean factory;

    @Autowired
    private KTable<Integer, ResearchBoost> boostTable;

    public void process() {
        factory.start();
        try {
            System.out.println("boost");
            boostTable.toStream().foreach((x, y) -> System.out.println(y));
        }
        finally{
            factory.stop();
        }              
    }
}

