package com.postgrestoelasticsearch.agregator.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.postgrestoelasticsearch.agregator.domain.models.Summary;
import com.postgrestoelasticsearch.agregator.services.AgregatorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequestMapping("/admin")
public class AgregatorController {

    @Autowired
    private AgregatorService service;
    
    @GetMapping("/create")
    public Summary create() {
        return service.create();
    }

    @GetMapping("/delete")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete() {
        service.delete();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }    
}
