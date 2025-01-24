package com.postgrestoelasticsearch.agregator.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.postgrestoelasticsearch.agregator.domain.models.ResearchBoost;

@Repository
public interface ResearchBoostRepository extends ElasticsearchRepository<ResearchBoost, Integer> {
    
}
