package com.postgrestoelasticsearch.agregator.services;

import org.apache.kafka.streams.state.KeyValueIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.postgrestoelasticsearch.agregator.components.BoostKafkaComponent;
import com.postgrestoelasticsearch.agregator.domain.models.ResearchBoost;

@Service
//@Component
public class AgregatorService {

    @Autowired
    private BoostKafkaComponent boostKafkaComponent;

    public void process() {
        boostKafkaComponent.start();

        KeyValueIterator<Integer, ResearchBoost> topics = boostKafkaComponent.getTopics();
        topics.forEachRemaining(entry -> {
            Integer key = entry.key;
            ResearchBoost value = entry.value;
            System.out.println(key + "={\"student_id\":" + value.getStudentId() + ",\"research\":" + value.getResearch() + ",\"admit_chance\":" + value.getAdmitChance() + "}");
        });

        boostKafkaComponent.stop();
    }
}

