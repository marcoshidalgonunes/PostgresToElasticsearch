package com.postgrestoelasticsearch.agregator.services;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.postgrestoelasticsearch.agregator.domain.models.Admission;
import com.postgrestoelasticsearch.agregator.domain.models.Research;
import com.postgrestoelasticsearch.agregator.domain.models.ResearchBoost;
import com.postgrestoelasticsearch.agregator.domain.serdes.AdmissionSerde;
import com.postgrestoelasticsearch.agregator.domain.serdes.ResearchSerde;

@Service
public class AgregatorService {

    @Autowired
    private KStream<String, Admission> streamAdmission;

    @Autowired
    private KStream<String, Research> streamResearch;

    public void agregate() {

        final AdmissionSerde admissionSerde = new AdmissionSerde();
        final ResearchSerde researchSerde = new ResearchSerde();

        final KTable<Integer, Admission> admissions = streamAdmission.selectKey((k, v) -> v.getStudent_id())
                .toTable(Materialized.<Integer, Admission, KeyValueStore<Bytes, byte[]>>as("Admissions")
                        .withKeySerde(Serdes.Integer())
                        .withValueSerde(admissionSerde)
                        .withCachingDisabled());

        final KTable<Integer, Research> researchs = streamResearch.map((k, v) -> new KeyValue<>(v.getResearch(), v))
                .toTable(Materialized.<Integer, Research, KeyValueStore<Bytes, byte[]>>as("Researchs")
                        .withKeySerde(Serdes.Integer())
                        .withValueSerde(researchSerde)
                        .withCachingDisabled());

        final KTable<Integer, ResearchBoost> boost = admissions.join(researchs, Admission::getStudent_id, (l, r) ->
                new ResearchBoost(l.getStudent_id(), r.getResearch(), l.getAdmit_chance())
        );

        System.out.println("boost");
        boost.toStream().foreach((x, y) -> System.out.println(y));    
    }
}
