package com.postgrestoelasticsearch.agregator.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.postgrestoelasticsearch.agregator.services.AgregatorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
public class AgregatorController {

    @Autowired
    private AgregatorService service;
    
    @GetMapping("/process")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> process() {
        service.process();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
