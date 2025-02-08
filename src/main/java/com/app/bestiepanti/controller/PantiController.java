package com.app.bestiepanti.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.bestiepanti.dto.request.PantiRequest;
import com.app.bestiepanti.dto.response.PantiResponse;
import com.app.bestiepanti.service.PantiService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/panti")
public class PantiController {
   
    public static final String CREATE_PANTI_ENDPOINT = "/create";
 
    private final PantiService pantiService;
 
    @RequestMapping(value = CREATE_PANTI_ENDPOINT, method=RequestMethod.POST, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<PantiResponse> createPanti(@Valid @RequestBody PantiRequest request){
        PantiResponse pantiResponse = pantiService.createPanti(request);
        return new ResponseEntity<>(pantiResponse, HttpStatus.CREATED);
    }
 
}
