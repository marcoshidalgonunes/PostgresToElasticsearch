package com.postgrestoelasticsearch.api.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.postgrestoelasticsearch.api.models.ResearchBoost;
import com.postgrestoelasticsearch.api.services.QueryBoostService;

@RestController
public class QueryController {

    private final QueryBoostService queryService;

    public QueryController(QueryBoostService boostService) {
        queryService = boostService;
    }
    
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ResearchBoost> getAll() {
        return queryService.getAll();
    }
}
