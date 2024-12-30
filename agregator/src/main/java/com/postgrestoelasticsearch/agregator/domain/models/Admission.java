package com.postgrestoelasticsearch.agregator.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admission {
    int student_id;
    
    int gre;

    int toefl;

    double cpga;

    double admit_chance;
}
