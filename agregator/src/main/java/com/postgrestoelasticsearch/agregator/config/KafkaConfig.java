package com.postgrestoelasticsearch.agregator.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;


import com.postgrestoelasticsearch.agregator.domain.models.Admission;
import com.postgrestoelasticsearch.agregator.domain.models.Research;
import com.postgrestoelasticsearch.agregator.domain.models.ResearchBoost;
import com.postgrestoelasticsearch.agregator.domain.serdes.AdmissionSerde;
import com.postgrestoelasticsearch.agregator.domain.serdes.ResearchSerde;
import com.postgrestoelasticsearch.agregator.domain.serdes.StudentIdSerde;

@Configuration
@EnableKafka
@EnableKafkaStreams
public class KafkaConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value("${spring.application.name}")
    private String appplicationId;

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    KafkaStreamsConfiguration kafkaStreamsConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, appplicationId);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 2000);
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_DOC, 10000000);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, "true");

        return new KafkaStreamsConfiguration(props);
    }

    @Bean
    KTable<Integer, ResearchBoost> boostTable(StreamsBuilder streamsBuilder) {
        final AdmissionSerde admissionSerde = new AdmissionSerde();
        final ResearchSerde researchSerde = new ResearchSerde();
        final StudentIdSerde studentIdSerde = new StudentIdSerde();

        final KTable<Integer, Admission> admissions = streamsBuilder.stream("dbserver1.public.admission", Consumed.with(studentIdSerde, admissionSerde))
            .selectKey((k, v) -> v.getStudentId())
            .toTable(Materialized.with(Serdes.Integer(), admissionSerde));

        final KTable<Integer, Research> researchs = streamsBuilder.stream("dbserver1.public.research", Consumed.with(studentIdSerde, researchSerde))
            .selectKey((k, v) -> v.getStudentId())
            .toTable(Materialized.with(Serdes.Integer(), researchSerde));

        return admissions.join(researchs, Admission::getStudentId, (l, r) ->
            new ResearchBoost(l.getStudentId(), r.getResearch(), l.getAdmitChance())
        );
    }    
}
