package com.postgrestoelasticsearch.api.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.postgrestoelasticsearch.api.models.ResearchBoost;
import com.postgrestoelasticsearch.api.repositories.ResearchBoostRepository;

@Service
public class QueryBoostService {
    
    @Autowired 
    private ResearchBoostRepository researchBoostRepository;

    public List<ResearchBoost> getAll() {
        List<ResearchBoost> boosts = new ArrayList<>();
        researchBoostRepository.findAll()
            .forEach(boosts::add);
        
        return boosts;
    }
}
