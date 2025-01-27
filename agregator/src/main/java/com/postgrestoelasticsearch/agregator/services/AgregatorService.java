package com.postgrestoelasticsearch.agregator.services;

import org.apache.kafka.streams.state.KeyValueIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.postgrestoelasticsearch.agregator.components.BoostKafkaComponent;
import com.postgrestoelasticsearch.agregator.domain.models.ResearchBoost;
import com.postgrestoelasticsearch.agregator.repositories.ResearchBoostRepository;

@Service
public class AgregatorService {

    @Autowired
    private BoostKafkaComponent boostKafkaComponent;

    @Autowired 
    private ResearchBoostRepository researchBoostRepository;

    public void create() {
        boostKafkaComponent.start();

        KeyValueIterator<Integer, ResearchBoost> topics = boostKafkaComponent.getTopics();
        topics.forEachRemaining(entry -> {
            if (!researchBoostRepository.findById(entry.key).isPresent()) {
                ResearchBoost boost = researchBoostRepository.save(entry.value);
                System.out.println("Saved {\"studentId\":" + boost.getStudentId() + ",\"research\":" + boost.getResearch() + ",\"admitChance\":" + boost.getAdmitChance() + "}");
            }
        });

        boostKafkaComponent.stop();   
    }

    public void delete() {
        researchBoostRepository.deleteAll();
    }
}

