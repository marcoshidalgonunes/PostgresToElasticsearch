package com.postgrestoelasticsearch.agregator.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResearchBoost {
    int student_id;

    int research;

    double admit_chance;
}
