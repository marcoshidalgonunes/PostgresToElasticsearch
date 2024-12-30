package com.postgrestoelasticsearch.agregator.components;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.stereotype.Component;

import com.postgrestoelasticsearch.agregator.domain.models.Admission;
import com.postgrestoelasticsearch.agregator.domain.models.Research;
import com.postgrestoelasticsearch.agregator.domain.models.ResearchBoost;
import com.postgrestoelasticsearch.agregator.domain.serdes.AdmissionSerde;
import com.postgrestoelasticsearch.agregator.domain.serdes.ResearchSerde;

@Component
public class ViewBuilder {
    
    public void buildBoostView(StreamsBuilder sb) {
        final AdmissionSerde admissionSerde = new AdmissionSerde();
        final ResearchSerde researchSerde = new ResearchSerde();

        final KTable<Integer, Admission> Admissions = sb.stream("dbserver1.public.admission", Consumed.with(Serdes.Integer(), admissionSerde)).selectKey((k, v) -> v.getStudent_id())
                .toTable(Materialized.<Integer, Admission, KeyValueStore<Bytes, byte[]>>as("Admissions")
                        .withKeySerde(Serdes.Integer())
                        .withValueSerde(admissionSerde)
                        .withCachingDisabled());

        final KTable<Integer, Research> Researchs = sb.stream("dbserver1.public.research", Consumed.with(Serdes.Integer(), researchSerde))
                .map((k, v) -> new KeyValue<>(v.getResearch(), v))
                .toTable(Materialized.<Integer, Research, KeyValueStore<Bytes, byte[]>>as("Researchs")
                        .withKeySerde(Serdes.Integer())
                        .withValueSerde(researchSerde)
                        .withCachingDisabled());

        final KTable<Integer, ResearchBoost> join = Admissions.join(Researchs, Admission::getStudent_id, (l, r) ->
                new ResearchBoost(l.getStudent_id(), r.getResearch(), l.getAdmit_chance())
        );

        join.toStream().foreach((x, y) -> System.out.println(y));         
    }
}
