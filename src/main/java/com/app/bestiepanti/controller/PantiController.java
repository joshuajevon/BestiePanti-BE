package com.app.bestiepanti.controller;

import java.math.BigInteger;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.bestiepanti.dto.request.panti.CreatePantiRequest;
import com.app.bestiepanti.dto.request.panti.UpdatePantiRequest;
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
    public static final String UPDATE_PANTI_ENDPOINT = "/update/{id}";
 
    private final PantiService pantiService;

    @RequestMapping(value = CREATE_PANTI_ENDPOINT,method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PantiResponse> createPanti(@Valid @ModelAttribute CreatePantiRequest request){
        PantiResponse pantiResponse = pantiService.createPanti(request);
        return new ResponseEntity<>(pantiResponse, HttpStatus.CREATED);
    }

    @RequestMapping(value = UPDATE_PANTI_ENDPOINT, method=RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PantiResponse> updatePanti(@PathVariable BigInteger id, @Valid @ModelAttribute UpdatePantiRequest request) {
        PantiResponse pantiResponse= pantiService.updatePanti(id, request);
        return new ResponseEntity<>(pantiResponse, HttpStatus.OK);
    }
    
}
