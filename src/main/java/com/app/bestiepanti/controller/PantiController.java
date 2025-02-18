package com.app.bestiepanti.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

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
import com.app.bestiepanti.dto.response.GeneralResponse;
import com.app.bestiepanti.dto.response.panti.PantiReponses;
import com.app.bestiepanti.dto.response.panti.PantiResponse;
import com.app.bestiepanti.exception.UserNotFoundException;
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
    public static final String DELETE_PANTI_ENDPOINT = "/delete/{id}";
    public static final String VIEW_ALL_PANTI_ENDPOINT = "/view";
    public static final String VIEW_PANTI_BY_ID_ENDPOINT = "/view/{id}";
 
    private final PantiService pantiService;

    @RequestMapping(value = CREATE_PANTI_ENDPOINT,method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PantiResponse> createPanti(@Valid @ModelAttribute CreatePantiRequest request){
        PantiResponse pantiResponse = pantiService.createPanti(request);
        return new ResponseEntity<>(pantiResponse, HttpStatus.CREATED);
    }

    @RequestMapping(value = UPDATE_PANTI_ENDPOINT, method=RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PantiResponse> updatePanti(@PathVariable BigInteger id, @Valid @ModelAttribute UpdatePantiRequest request) throws UserNotFoundException {
        PantiResponse pantiResponse= pantiService.updatePanti(id, request);
        return new ResponseEntity<>(pantiResponse, HttpStatus.OK);
    }

    @RequestMapping(value = DELETE_PANTI_ENDPOINT, method=RequestMethod.DELETE)
    public ResponseEntity<GeneralResponse> deletePanti(@PathVariable BigInteger id) throws IOException, UserNotFoundException {
        pantiService.deletePanti(id);
        GeneralResponse generalResponse = new GeneralResponse("Panti with ID " + id + " has been successfully deleted");
        return new ResponseEntity<>(generalResponse,HttpStatus.OK);
    }

    @RequestMapping(value = VIEW_ALL_PANTI_ENDPOINT, method=RequestMethod.GET)
    public ResponseEntity<PantiReponses> viewAllPanti() {
        PantiReponses pantiReponses = new PantiReponses();
        List<PantiResponse> pantiResponseList = pantiService.viewAllPanti();
        pantiReponses.setPantiResponses(pantiResponseList);
        return new ResponseEntity<>(pantiReponses, HttpStatus.OK);
    }

    @RequestMapping(value = VIEW_PANTI_BY_ID_ENDPOINT, method=RequestMethod.GET)
    public ResponseEntity<PantiResponse> viewPantiById(@PathVariable BigInteger id) throws UserNotFoundException {
        PantiResponse pantiResponse = pantiService.viewPantiById(id);
        return new ResponseEntity<>(pantiResponse, HttpStatus.OK);
    }
    
    
    
}
