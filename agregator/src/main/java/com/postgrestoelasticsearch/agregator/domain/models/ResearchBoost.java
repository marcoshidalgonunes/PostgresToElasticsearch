package com.postgrestoelasticsearch.agregator.domain.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResearchBoost {
    @JsonProperty("student_id")
    int studentId;

    @JsonProperty("research")
    int research;

    @JsonProperty("admit_chance")
    double admitChance;
}
