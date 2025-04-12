package com.app.bestiepanti.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.bestiepanti.dto.request.panti.CreatePantiRequest;
import com.app.bestiepanti.dto.request.panti.DeleteImagePantiRequest;
import com.app.bestiepanti.dto.request.panti.UpdateIsUrgentPantiRequest;
import com.app.bestiepanti.dto.response.GeneralResponse;
import com.app.bestiepanti.dto.response.panti.PantiResponses;
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
    public static final String UPDATE_IS_URGENT_PANTI_ENDPOINT = "/update/{id}";
    public static final String DELETE_PANTI_ENDPOINT = "/delete/{id}";
    public static final String VIEW_ALL_PANTI_ENDPOINT = "/view";
    public static final String VIEW_PANTI_BY_ID_ENDPOINT = "/view/{id}";
    public static final String VIEW_URGENT_PANTI_ENDPOINT = "/view/urgent";
    public static final String DELETE_IMAGE_PANTI_ENDPOINT = "/delete-image/{id}";
 
    private final PantiService pantiService;

    @RequestMapping(value = CREATE_PANTI_ENDPOINT,method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PantiResponse> createPanti(@Valid @ModelAttribute CreatePantiRequest request){
        PantiResponse pantiResponse = pantiService.createPanti(request);
        log.info("Panti " + pantiResponse.getId() + " is created!");
        return new ResponseEntity<>(pantiResponse, HttpStatus.CREATED);
    }

    @RequestMapping(value = UPDATE_IS_URGENT_PANTI_ENDPOINT, method=RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<PantiResponse> updateIsUrgentPanti(@PathVariable BigInteger id, @Valid @RequestBody UpdateIsUrgentPantiRequest request) throws UserNotFoundException {
        log.info("Request Body: " + request.toString());
        PantiResponse pantiResponse= pantiService.updateIsUrgentPanti(id, request);
        log.info("Panti " + id + " is updated!");
        return new ResponseEntity<>(pantiResponse, HttpStatus.OK);
    }

    @RequestMapping(value = DELETE_PANTI_ENDPOINT, method=RequestMethod.DELETE)
    public ResponseEntity<GeneralResponse> deletePanti(@PathVariable BigInteger id) throws IOException, UserNotFoundException {
        pantiService.deletePanti(id);
        GeneralResponse generalResponse = new GeneralResponse("Panti with ID " + id + " has been successfully deleted");
        log.info("Panti " + id + " is deleted!");
        return new ResponseEntity<>(generalResponse,HttpStatus.OK);
    }

    @RequestMapping(value = VIEW_ALL_PANTI_ENDPOINT, method=RequestMethod.GET)
    public ResponseEntity<PantiResponses> viewAllPanti() {
        PantiResponses pantiReponses = new PantiResponses();
        List<PantiResponse> pantiResponseList = pantiService.viewAllPanti();
        pantiReponses.setPantiResponses(pantiResponseList);
        log.info("Response Body: " + pantiReponses);
        return new ResponseEntity<>(pantiReponses, HttpStatus.OK);
    }

    @RequestMapping(value = VIEW_PANTI_BY_ID_ENDPOINT, method=RequestMethod.GET)
    public ResponseEntity<PantiResponse> viewPantiById(@PathVariable BigInteger id) throws UserNotFoundException {
        PantiResponse pantiResponse = pantiService.viewPantiById(id);
        log.info("Response Body: " + pantiResponse);
        return new ResponseEntity<>(pantiResponse, HttpStatus.OK);
    }
    
    @RequestMapping(value = VIEW_URGENT_PANTI_ENDPOINT, method=RequestMethod.GET)
    public ResponseEntity<PantiResponses> viewUrgentPanti() {
        PantiResponses pantiReponses = new PantiResponses();
        List<PantiResponse> pantiResponseList = pantiService.viewUrgentPanti();
        pantiReponses.setPantiResponses(pantiResponseList);
        log.info("Response Body: " + pantiReponses);
        return new ResponseEntity<>(pantiReponses, HttpStatus.OK);
    }
    
    @RequestMapping(value = DELETE_IMAGE_PANTI_ENDPOINT, method=RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<GeneralResponse> deleteImagePanti(@PathVariable BigInteger id, @RequestBody DeleteImagePantiRequest request) throws IOException {
        pantiService.deleteImagePanti(id, request);
        GeneralResponse generalResponse = new GeneralResponse("Image panti " + request.getImageList() +" with user id: " + id + " have been successfully deleted!");
        log.info("Response Body: " + generalResponse);
        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }
    
}
