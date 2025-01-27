package com.postgrestoelasticsearch.agregator.services;

import org.apache.kafka.streams.state.KeyValueIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.postgrestoelasticsearch.agregator.components.BoostKafkaComponent;
import com.postgrestoelasticsearch.agregator.domain.models.ResearchBoost;
import com.postgrestoelasticsearch.agregator.domain.models.Summary;
import com.postgrestoelasticsearch.agregator.repositories.ResearchBoostRepository;

@Service
public class AgregatorService {

    @Autowired
    private BoostKafkaComponent boostKafkaComponent;

    @Autowired 
    private ResearchBoostRepository researchBoostRepository;

    public Summary create() {
        Summary summary = new Summary(0, 0);

        boostKafkaComponent.start();

        try (KeyValueIterator<Integer, ResearchBoost> topics = boostKafkaComponent.getTopics()) {
            topics.forEachRemaining(entry -> {
                summary.setReadedTopics(summary.getReadedTopics() + 1);
                if (!researchBoostRepository.findById(entry.key).isPresent()) {
                    researchBoostRepository.save(entry.value);
                    summary.setWrittenTopics(summary.getWrittenTopics() + 1);
                }
            });
        }

        boostKafkaComponent.stop();   

        return summary;
    }

    public void delete() {
        researchBoostRepository.deleteAll();
    }
}

