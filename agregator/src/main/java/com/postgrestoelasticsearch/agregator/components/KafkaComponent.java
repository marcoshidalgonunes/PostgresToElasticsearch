package com.postgrestoelasticsearch.agregator.components;

import org.apache.kafka.streams.state.KeyValueIterator;

public interface KafkaComponent<K, V> {

    KeyValueIterator<K, V> getTopics();

    void start();
    
    void stop();
}
