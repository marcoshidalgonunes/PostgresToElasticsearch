package com.postgrestoelasticsearch.agregator.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;

import com.postgrestoelasticsearch.agregator.domain.models.Admission;
import com.postgrestoelasticsearch.agregator.domain.models.Research;
import com.postgrestoelasticsearch.agregator.domain.models.ResearchBoost;
import com.postgrestoelasticsearch.agregator.domain.serdes.AdmissionSerde;
import com.postgrestoelasticsearch.agregator.domain.serdes.ResearchSerde;

@Configuration
@EnableKafka
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

        return new KafkaStreamsConfiguration(props);
    }

    @Bean
    public StreamsBuilderFactoryBean streamBuilderFactoryBeam(KafkaStreamsConfiguration streamsConfig) {
        StreamsBuilderFactoryBean factory = new StreamsBuilderFactoryBean(streamsConfig);
        factory.setAutoStartup(false);

        return factory;
    }

    @Bean
    KTable<Integer, ResearchBoost> boostTable(StreamsBuilder streamsBuilder) {
        final AdmissionSerde admissionSerde = new AdmissionSerde();
        final ResearchSerde researchSerde = new ResearchSerde();

        final KTable<Integer, Admission> admissions = streamsBuilder.stream("dbserver1.public.admission", Consumed.with(Serdes.String(), new AdmissionSerde()))
            .selectKey((k, v) -> v.getStudent_id())
            .toTable(Materialized.<Integer, Admission, KeyValueStore<Bytes, byte[]>>as("Admissions")
                .withKeySerde(Serdes.Integer())
                .withValueSerde(admissionSerde)
                .withCachingDisabled());

        final KTable<Integer, Research> researchs = streamsBuilder.stream("dbserver1.public.research", Consumed.with(Serdes.String(), new ResearchSerde()))
            .map((k, v) -> new KeyValue<>(v.getResearch(), v))
            .toTable(Materialized.<Integer, Research, KeyValueStore<Bytes, byte[]>>as("Researchs")
                .withKeySerde(Serdes.Integer())
                .withValueSerde(researchSerde)
                .withCachingDisabled());

        return admissions.join(researchs, Admission::getStudent_id, (l, r) ->
                new ResearchBoost(l.getStudent_id(), r.getResearch(), l.getAdmit_chance())
        );
    }
}
