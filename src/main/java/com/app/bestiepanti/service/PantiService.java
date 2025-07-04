package com.app.bestiepanti.service;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.app.bestiepanti.configuration.ApplicationConfig;
import com.app.bestiepanti.dto.request.panti.CreatePantiRequest;
import com.app.bestiepanti.dto.request.panti.DeleteImagePantiRequest;
import com.app.bestiepanti.dto.request.panti.ImagePantiRequest;
import com.app.bestiepanti.dto.request.panti.UpdateIsUrgentPantiRequest;
import com.app.bestiepanti.dto.request.panti.UpdatePantiRequest;
import com.app.bestiepanti.dto.response.panti.PantiResponse;
import com.app.bestiepanti.exception.UserNotFoundException;
import com.app.bestiepanti.model.Donation;
import com.app.bestiepanti.model.Message;
import com.app.bestiepanti.model.Panti;
import com.app.bestiepanti.model.Payment;
import com.app.bestiepanti.model.Role;
import com.app.bestiepanti.model.UserApp;
import com.app.bestiepanti.repository.DonationRepository;
import com.app.bestiepanti.repository.MessageRepository;
import com.app.bestiepanti.repository.PantiRepository;
import com.app.bestiepanti.repository.PaymentRespository;
import com.app.bestiepanti.repository.RoleRepository;
import com.app.bestiepanti.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PantiService {
 
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final PantiRepository pantiRepository;
     private final FundDonationService fundDonationService;
    private final NonFundDonationService nonFundDonationService;
    private final DonationRepository donationRepository;
    private final MessageService messageService;
    private final MessageRepository messageRepository;
    private final JwtService jwtService;
    private final ApplicationConfig applicationConfig;
    private final PaymentRespository paymentRespository;
    private final UserService userService;

    public PantiResponse createPanti(CreatePantiRequest request){
        UserApp user = new UserApp();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
 
        Role role = roleRepository.findByName(UserApp.ROLE_PANTI);
        user.setRole(role);
 
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setIsGoogle(0);
        userRepository.save(user);
 
        Panti panti = saveToPanti(request, user);
        Payment payment = saveToPayment(request, panti);
        String jwtToken = jwtService.generateToken(user);
        return createPantiResponse(user, panti, payment, jwtToken);
    }
         
    private Payment saveToPayment(CreatePantiRequest request, Panti panti) {
        Payment payment = new Payment();
        payment.setPantiId(panti);
        processQris(request, payment);
        payment.setBankAccountName(request.getBankAccountName());
        payment.setBankAccountNumber(request.getBankAccountNumber());
        payment.setBankName(request.getBankName());
        paymentRespository.save(payment);
        return payment;
    }
        
    public Panti saveToPanti(CreatePantiRequest request, UserApp user){
        Panti panti = new Panti();
        processImage(request, panti);
        panti.setDescription(request.getDescription());
        panti.setPhone("+62"+request.getPhone());
        panti.setDonationTypes(request.getDonationTypes());
        panti.setIsUrgent(Integer.parseInt(request.getIsUrgent()));
        panti.setAddress(request.getAddress());
        panti.setRegion(request.getRegion());
        panti.setMaps(request.getMaps());
        panti.setUser(user);
        pantiRepository.save(panti);
        return panti;
    }

    public PantiResponse updatePanti(BigInteger id, UpdatePantiRequest request) throws UserNotFoundException {
        UserApp user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " Not Found"));
    
        user.setName(request.getName());

        Panti panti = pantiRepository.findByUserId(id);
        if(panti != null){
            processImage(request, panti);
            panti.setDescription(request.getDescription());
            panti.setPhone("+62"+request.getPhone());
            panti.setDonationTypes(request.getDonationTypes());
            panti.setAddress(request.getAddress());
            panti.setMaps(request.getMaps());
            panti.setRegion(request.getRegion());
            pantiRepository.save(panti);
        }

        Payment payment = paymentRespository.findByPantiId(panti.getId());
        if(payment != null){
            processQris(request, payment);
            payment.setBankAccountName(request.getBankAccountName());
            payment.setBankAccountNumber(request.getBankAccountNumber());
            payment.setBankName(request.getBankName());
            paymentRespository.save(payment);
        }
        return createPantiResponse(user, panti, payment, null);
    }
    
    @Transactional
    public void deletePanti(BigInteger id) throws IOException, UserNotFoundException {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User with id " + id + " Not Found");
        }

        try {
            Panti panti = pantiRepository.findByUserId(id);
            if (panti != null) {
                List<String> fileNames = panti.getImage();
                for (String fileName : fileNames) {
                    Path filePath = Paths.get(applicationConfig.getImageUploadDir(), fileName);
                    if (Files.exists(filePath)) {
                        Files.delete(filePath);
                    }
                }
            }

            Payment payment = paymentRespository.findByPantiId(panti.getId());
            if(payment != null){
                if (payment.getQris() != null) {
                    String fileQris = payment.getQris();
                    Path filePath = Paths.get(applicationConfig.getQrisUploadDir(), fileQris);
                    if (Files.exists(filePath)) {
                        Files.delete(filePath);
                    }
                }
            }

            List<Donation> donations = donationRepository.findAllByPantiId(id);
            if(!donations.isEmpty()){
                for (Donation donation : donations) {
                    if(donation.getDonationTypes().contains("Dana"))
                        fundDonationService.deleteFundDonation(donation.getId());
                    else
                        nonFundDonationService.deleteNonFundDonation(donation.getId());
                }
            }

            List<Message> messages = messageRepository.findAllByPantiId(id);
            if(!messages.isEmpty()){
                for (Message message : messages) {
                    messageService.deleteMessage(message.getId());
                }
            }

            paymentRespository.delete(payment);
            pantiRepository.deleteByUserId(id);
            userRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete Panti with id " + id, e);
        }
    }


    private void processQris(ImagePantiRequest request, Payment payment) {
        try {
            if (request.getQris() != null && !request.getQris().isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + request.getName() + "_" + request.getQris().getOriginalFilename();
                Path filePath = Paths.get(applicationConfig.getQrisUploadDir(), fileName);
                try {
                    if(payment.getQris() != null){
                        String prevFileName = payment.getQris();
                        Path prevFilePath = Paths.get(applicationConfig.getQrisUploadDir(), prevFileName);
                        if (Files.exists(prevFilePath)) {
                            Files.delete(prevFilePath);
                        }
                    }
                    Files.write(filePath, request.getQris().getBytes());
                } catch (IOException e) {
                    throw new RuntimeException("Failed to save image", e);
                }
                payment.setQris(fileName);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }

    private void processImage(ImagePantiRequest request, Panti panti) {
        try {
            if (request.getImage() != null && !request.getImage().isEmpty()) {
                List<String> imagePaths = panti.getImage() != null ? new ArrayList<>(panti.getImage()) : new ArrayList<>();
    
                for (MultipartFile image : request.getImage()) {
                    String fileName = System.currentTimeMillis() + "_" + request.getName() + "_" + image.getOriginalFilename();
                    Path filePath = Paths.get(applicationConfig.getImageUploadDir(), fileName);
    
                    try {
                        Files.write(filePath, image.getBytes());
                        imagePaths.add(fileName);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to save image", e);
                    }
                }
                panti.setImage(imagePaths);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to save images", e);
        }
    }


    public PantiResponse createPantiResponse(UserApp userApp, Panti panti, Payment payment, String jwtToken) {
        return PantiResponse.builder()
                .id(userApp.getId())
                .name(userApp.getName())
                .email(userApp.getEmail())
                .role(userApp.getRole().getName())
                .image(panti.getImage())
                .description(panti.getDescription())
                .phone(panti.getPhone())
                .donationTypes(panti.getDonationTypes())
                .isUrgent(panti.getIsUrgent())
                .address(panti.getAddress())
                .region(panti.getRegion())
                .maps(panti.getMaps())
                .bankAccountName(payment != null ? payment.getBankAccountName() : null)
                .bankAccountNumber(payment != null ? payment.getBankAccountNumber() : null)
                .bankName(payment != null ? payment.getBankName() : null)
                .qris(payment != null ? payment.getQris() : null)
                .token(jwtToken)
                .build();
    }

    public List<PantiResponse> viewAllPanti() {
        List<Panti> pantiList = pantiRepository.findAllByOrderByIdDesc();
        List<PantiResponse> pantiResponseList = new ArrayList<>();
        for (Panti panti : pantiList) {
            Payment payment = paymentRespository.findByPantiId(panti.getId());
            PantiResponse pantiResponse = createPantiResponse(panti.getUser(), panti, payment, null);
            pantiResponseList.add(pantiResponse);
        }
        return pantiResponseList;
    }

    public PantiResponse viewPantiById(BigInteger id) throws UserNotFoundException {
        PantiResponse pantiResponse = new PantiResponse();
        Panti panti = pantiRepository.findByUserId(id);
        if (panti == null) {
            throw new UserNotFoundException("User with id " + id + " Not Found");
        } else {
            Payment payment = paymentRespository.findByPantiId(panti.getId());
            pantiResponse = createPantiResponse(panti.getUser(), panti, payment, null);
        }
        return pantiResponse;
    }

    public List<PantiResponse> viewUrgentPanti() {
        List<Panti> pantiList = pantiRepository.findAllByIsUrgent(1);
        List<PantiResponse> pantiResponseList = new ArrayList<>();

        for (Panti panti : pantiList) {
            Payment payment = paymentRespository.findByPantiId(panti.getId());
            PantiResponse pantiResponse = createPantiResponse(panti.getUser(), panti, payment, null);
            pantiResponseList.add(pantiResponse);
        }
        return pantiResponseList;
    }

    public PantiResponse updateIsUrgentPanti(BigInteger id, UpdateIsUrgentPantiRequest request) throws UserNotFoundException {
        UserApp user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " Not Found"));
        Panti panti = pantiRepository.findByUserId(id);
        if(panti != null){
            panti.setIsUrgent(Integer.parseInt(request.getIsUrgent()));
            pantiRepository.save(panti);
        }
        return createPantiResponse(user, panti, null, null);
    }

    public void deleteImagePanti(BigInteger id, DeleteImagePantiRequest request) throws IOException, UserNotFoundException {
        Panti panti = pantiRepository.findByUserId(id);
        UserApp userDonatur = userService.getAuthenticate();
        if(userDonatur.getId() != panti.getUser().getId()){
            throw new UserNotFoundException("User is not permitted to delete this image panti");
        }
        if (panti != null) {
            List<String> allFileNames = new ArrayList<>(panti.getImage());
            List<String> requestFilesDeleted = request.getImageList();
    
            for (String fileDeleted : requestFilesDeleted) {
                if (allFileNames.contains(fileDeleted)) {
                    Path filePath = Paths.get(applicationConfig.getImageUploadDir(), fileDeleted);
                    if (Files.exists(filePath)) {
                        Files.delete(filePath);
                    }
                    allFileNames.remove(fileDeleted);
                }
            }
            panti.setImage(allFileNames);
            pantiRepository.save(panti);
        }
    }

    public void deleteQrisPanti(BigInteger id) throws IOException, UserNotFoundException {
        Panti panti = pantiRepository.findByUserId(id);
        UserApp userDonatur = userService.getAuthenticate();
        if(userDonatur.getId() != panti.getUser().getId()){
            throw new UserNotFoundException("User is not permitted to delete this qris panti");
        }
        if(panti != null){
            Payment payment = paymentRespository.findByPantiId(panti.getId());
            Path filePath = Paths.get(applicationConfig.getQrisUploadDir(), payment.getQris());
            if (Files.exists(filePath)){
                Files.delete(filePath);
            }
            payment.setQris(null);
            paymentRespository.save(payment);
        }
    }

}
