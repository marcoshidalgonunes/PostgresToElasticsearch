package com.postgrestoelasticsearch.agregator.domain.serdes;

import org.springframework.kafka.support.serializer.JsonSerde;

import com.postgrestoelasticsearch.agregator.domain.models.StudentId;

public class StudentIdSerde extends JsonSerde<StudentId> { }
