package com.neo.bank.controller;

import com.neo.bank.dto.SimpleResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class RootController {

    @GetMapping
    public ResponseEntity<SimpleResponse> index(){
        return ResponseEntity.status(HttpStatus.OK).body(new SimpleResponse(true, "Welcome to Neo Bank."));
    }


}
