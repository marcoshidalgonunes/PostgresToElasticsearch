package com.postgrestoelasticsearch.agregator.services;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Service;

import com.postgrestoelasticsearch.agregator.domain.models.ResearchBoost;

@Service
//@Component
public class AgregatorService {

    @Autowired
    private StreamsBuilderFactoryBean streamsBuilderFactoryBean;

    @SuppressWarnings("null")
    public void process() {
        KafkaStreams kafkaStreams = streamsBuilderFactoryBean.getKafkaStreams();
        ReadOnlyKeyValueStore<Integer, ResearchBoost> keyValueStore = kafkaStreams.store(
            StoreQueryParameters.fromNameAndType("researchs-boost-view", QueryableStoreTypes.keyValueStore())
        );
        keyValueStore.all().forEachRemaining(entry -> {
            Integer key = entry.key;
            ResearchBoost value = entry.value;
            System.out.println(key + "={\"student_id\":" + value.getStudentId() + ",\"research\":" + value.getResearch() + ",\"admit_chance\":" + value.getAdmitChance() + "}");
        });
    }
}

