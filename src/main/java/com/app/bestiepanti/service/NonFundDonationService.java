package com.app.bestiepanti.service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.app.bestiepanti.configuration.ApplicationConfig;
import com.app.bestiepanti.dto.request.auth.MailRequest;
import com.app.bestiepanti.dto.request.donation.nonfund.CreateNonFundDonationRequest;
import com.app.bestiepanti.dto.request.donation.nonfund.UpdateNonFundDonationRequest;
import com.app.bestiepanti.dto.response.donation.nonfund.NonFundDonationResponse;
import com.app.bestiepanti.exception.UserNotFoundException;
import com.app.bestiepanti.model.Donation;
import com.app.bestiepanti.model.NonFund;
import com.app.bestiepanti.model.Panti;
import com.app.bestiepanti.model.UserApp;
import com.app.bestiepanti.repository.DonationRepository;
import com.app.bestiepanti.repository.NonFundDonationRepository;
import com.app.bestiepanti.repository.PantiRepository;
import com.app.bestiepanti.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NonFundDonationService {

    private final UserRepository userRepository;
    private final PantiRepository pantiRepository;
    private final UserService userService;
    private final DonationRepository donationRepository;
    private final NonFundDonationRepository nonFundDonationRepository;
    private final EmailService emailService;
    private final ApplicationConfig applicationConfig;
    
    public NonFundDonationResponse createNonFundDonation(CreateNonFundDonationRequest request, BigInteger pantiId) throws Exception{
        UserApp userDonatur = userService.getAuthenticate();
        UserApp userPanti = userRepository.findById(pantiId).orElseThrow(() -> new UserNotFoundException("User with id " + pantiId + " Not Found"));
        Panti panti = pantiRepository.findByUserId(pantiId);
        Donation donation = new Donation();
        LocalDate fundDonationDate = LocalDate.parse(request.getDonationDate());
        donation.setDonationDate(fundDonationDate);
        donation.setDonationTypes(request.getDonationTypes());
        donation.setDonaturId(userDonatur);
        donation.setPantiId(userPanti);
        donation.setIsOnsite(Integer.parseInt(request.getIsOnsite()));
        donation.setStatus(Donation.STATUS_PENDING);
        donation.setInsertedTimestamp(LocalDateTime.now());
        donationRepository.save(donation);

        NonFund nonFund = new NonFund();
        nonFund.setPic(request.getPic());
        nonFund.setNotes(request.getNotes());
        nonFund.setActivePhone("+62" + request.getActivePhone());
        nonFund.setDonationId(donation);
        nonFundDonationRepository.save(nonFund);
        
        sendEmailNotificationToDonatur(userDonatur, nonFund, donation);
        sendEmailNotificationToPanti(userDonatur, nonFund, donation, userPanti);
        return createNonFundDonationResponse(donation, nonFund, panti);
    }

    private void sendEmailNotificationToDonatur(UserApp userDonatur, NonFund nonFund, Donation donation) throws Exception {
         MailRequest mailBody = MailRequest.builder()
                                .to(userDonatur.getEmail())
                                .subject("[No Reply] Donation Details Bestie Panti")
                                .build();
        Map<String, Object> variables = new HashMap<>();
        variables.put("name", userDonatur.getName());
        variables.put("date", donation.getDonationDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("id", "ID"))));
        variables.put("type", String.join(", ", donation.getDonationTypes()));
        String isOnsite = null;
        if(donation.getIsOnsite() == 0) isOnsite = "Online";
        else if(donation.getIsOnsite() == 1) isOnsite = "Onsite";
        variables.put("isOnsite", isOnsite);
        variables.put("pic", nonFund.getPic());
        variables.put("phone", nonFund.getActivePhone());
        variables.put("notes", nonFund.getNotes());
        emailService.sendSuccessNonFundDonationDetails(mailBody, variables, true);
    }

    private void sendEmailNotificationToPanti(UserApp userDonatur, NonFund nonFund, Donation donation, UserApp userPanti) throws Exception {
        MailRequest mailBodyPanti = MailRequest.builder()
                                .to(userPanti.getEmail())
                                .subject("[No Reply] Donation Details Bestie Panti")
                                .build();         

        Map<String, Object> variables = new HashMap<>();
        variables.put("nameDonatur", userDonatur.getName());
        variables.put("namePanti", userPanti.getName());
        variables.put("date", donation.getDonationDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("id", "ID"))));
        variables.put("type", String.join(", ", donation.getDonationTypes()));
        String isOnsite = null;
        if(donation.getIsOnsite() == 0) isOnsite = "Online";
        else if(donation.getIsOnsite() == 1) isOnsite = "Onsite";
        variables.put("isOnsite", isOnsite);
        variables.put("pic", nonFund.getPic());
        variables.put("phone", nonFund.getActivePhone());
        variables.put("notes", nonFund.getNotes());
        String verificationLink = applicationConfig.getUrlFrontEnd() + "/dashboard-panti";
        variables.put("verificationButton", verificationLink);

        emailService.sendSuccessNonFundDonationDetails(mailBodyPanti, variables, false);
    }

    public List<NonFundDonationResponse> viewAllNonFundDonation() {
        List<Donation> donationList = donationRepository.findAllByNonFundTypes();
        List<NonFundDonationResponse> nonFundDonationResponseList = new ArrayList<>();
        for (Donation donation : donationList) {
            Panti panti = pantiRepository.findByUserId(donation.getPantiId().getId());
            NonFund nonFund = nonFundDonationRepository.findByDonationId(donation.getId());
            NonFundDonationResponse nonFundDonationResponse = createNonFundDonationResponse(donation, nonFund, panti);
            nonFundDonationResponseList.add(nonFundDonationResponse);
        }
        return nonFundDonationResponseList;
    }

    public void deleteNonFundDonation(BigInteger id){

        try {
            Donation donation = donationRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Non Fund Donation with id " + id + " Not Found"));
            NonFund nonFund = nonFundDonationRepository.findByDonationId(id);
            if(nonFund == null) throw new NoSuchElementException("Non Fund Donation with id " + id + " Not Found");
            nonFundDonationRepository.delete(nonFund);
            donationRepository.delete(donation);
        } catch (NoSuchElementException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete Non Fund Donation with id " + id, e);
        }
    }

    public List<NonFundDonationResponse> viewNonFundDonationByUserId(BigInteger userId) throws UserNotFoundException{
        UserApp user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id " + userId + " Not Found"));
        List<NonFundDonationResponse> nonFundDonationResponses = new ArrayList<>();
        List<Donation> donations = new ArrayList<>();
        if(user.getRole().getName().equals(UserApp.ROLE_DONATUR)){
            donations = donationRepository.findAllByDonaturIdAndNonFundTypes(userId);
        } else if (user.getRole().getName().equals(UserApp.ROLE_PANTI)){
            donations = donationRepository.findAllByPantiIdAndNonFundTypes(userId);
        }

        if(!donations.isEmpty()){
            for (Donation donation : donations) {
                Panti panti = pantiRepository.findByUserId(donation.getPantiId().getId());
                NonFund nonFund = nonFundDonationRepository.findByDonationId(donation.getId());
                NonFundDonationResponse response = createNonFundDonationResponse(donation, nonFund, panti);
                nonFundDonationResponses.add(response);
            }
        }
        return nonFundDonationResponses;
    }

    public NonFundDonationResponse verifyNonFundDonation(BigInteger id, UpdateNonFundDonationRequest request) throws Exception{
        try {
            Donation donation = donationRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Non Fund Donation with id " + id + " Not Found"));
            Panti panti = pantiRepository.findByUserId(donation.getPantiId().getId());
            UserApp userPanti = userService.getAuthenticate();
            UserApp userDonatur = userRepository.findById(donation.getDonaturId().getId()).orElseThrow(() -> new UserNotFoundException("User with id " + donation.getDonaturId().getId() + " Not Found"));
            if(userPanti.getId() != donation.getPantiId().getId()){
                throw new UserNotFoundException("User is not permitted to verify this non fund donation");
            }
            
            donation.setStatus(request.getStatus());
            donation.setVerifiedTimestamp(LocalDateTime.now());
            LocalDate fundDonationDate = LocalDate.parse(request.getDonationDate());
            donation.setDonationDate(fundDonationDate);
            donation.setDonationTypes(request.getDonationTypes());
            donation.setIsOnsite(Integer.parseInt(request.getIsOnsite()));
            donationRepository.save(donation);
            
            NonFund nonFund = nonFundDonationRepository.findByDonationId(id);
            if(nonFund != null){
                nonFund.setPic(request.getPic());
                nonFund.setNotes(request.getNotes());
                nonFund.setActivePhone("+62"+request.getActivePhone());
                nonFundDonationRepository.save(nonFund);
            }
            if(!request.getStatus().equals(Donation.STATUS_PENDING)) sendEmailNotificationVerifyNonFundDonationToDonatur(userDonatur, nonFund, donation);
            return createNonFundDonationResponse(donation, nonFund, panti);
        } catch (NumberFormatException e) {
            throw e;
        } catch (NoSuchElementException e) {
            throw e;
        }
    }

    private void sendEmailNotificationVerifyNonFundDonationToDonatur(UserApp userDonatur, NonFund nonFund, Donation donation) throws Exception {
        MailRequest mailBodyDonatur = MailRequest.builder()
                                .to(userDonatur.getEmail())
                                .subject("[No Reply] Status Donation Bestie Panti")
                                .build();      

        Map<String, Object> variables = new HashMap<>();
        variables.put("name", userDonatur.getName());
        variables.put("date", donation.getDonationDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("id", "ID"))));
        variables.put("type", String.join(", ", donation.getDonationTypes()));
        String isOnsite = null;
        if(donation.getIsOnsite() == 0) isOnsite = "Online";
        else if(donation.getIsOnsite() == 1) isOnsite = "Onsite";
        variables.put("isOnsite", isOnsite);
        variables.put("pic", nonFund.getPic());
        variables.put("phone", nonFund.getActivePhone());
        variables.put("notes", nonFund.getNotes());
        variables.put("status", donation.getStatus());

        emailService.sendVerifyNonFundDonationDetails(mailBodyDonatur, variables);
    }

    public NonFundDonationResponse viewNonFundDonationByDonationId(BigInteger id) throws UserNotFoundException{
        try {
            Donation donation = donationRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Non Fund Donation with id " + id + " Not Found"));
            Panti panti = pantiRepository.findByUserId(donation.getPantiId().getId());
            UserApp userPanti = userService.getAuthenticate();
            if(userPanti.getId() != donation.getPantiId().getId()){
                throw new UserNotFoundException("User is not permitted to access this non fund donation");
            }
            NonFund nonFund = nonFundDonationRepository.findByDonationId(id);

          return createNonFundDonationResponse(donation, nonFund, panti);
        } catch (NumberFormatException | NoSuchElementException e) {
            throw e;
        }
    }

    public NonFundDonationResponse createNonFundDonationResponse(Donation donation, NonFund nonFund, Panti panti) {
        return NonFundDonationResponse.builder()
                .id(donation.getId())
                .donaturId(donation.getDonaturId().getId())
                .donaturName(donation.getDonaturId().getName())
                .pantiId(donation.getPantiId().getId())
                .pantiName(donation.getPantiId().getName())
                .donationDate(donation.getDonationDate().toString())
                .isOnsite(donation.getIsOnsite())
                .donationTypes(donation.getDonationTypes())
                .activePhone(nonFund.getActivePhone())
                .pic(nonFund.getPic())
                .notes(nonFund.getNotes())
                .status(donation.getStatus())
                .profile(panti.getImage().getFirst())
                .insertedTimestamp(donation.getInsertedTimestamp())
                .verifiedTimestamp(donation.getVerifiedTimestamp())
                .build();
    }
}
