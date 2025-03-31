package com.app.bestiepanti.service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.bestiepanti.configuration.JwtConfig;
import com.app.bestiepanti.dto.request.auth.LoginRequest;
import com.app.bestiepanti.dto.request.auth.MailRequest;
import com.app.bestiepanti.dto.request.auth.RegisterRequest;
import com.app.bestiepanti.dto.request.auth.forgotpassword.ResetPasswordRequest;
import com.app.bestiepanti.dto.request.auth.forgotpassword.VerifyOtpRequest;
import com.app.bestiepanti.dto.response.AdminResponse;
import com.app.bestiepanti.dto.response.donatur.DonaturResponse;
import com.app.bestiepanti.dto.response.panti.PantiResponse;
import com.app.bestiepanti.exception.UserNotFoundException;
import com.app.bestiepanti.exception.ValidationException;
import com.app.bestiepanti.model.Donatur;
import com.app.bestiepanti.model.ForgotPassword;
import com.app.bestiepanti.model.Panti;
import com.app.bestiepanti.model.Payment;
import com.app.bestiepanti.model.Role;
import com.app.bestiepanti.model.UserApp;
import com.app.bestiepanti.repository.DonaturRepository;
import com.app.bestiepanti.repository.ForgotPasswordRepository;
import com.app.bestiepanti.repository.PantiRepository;
import com.app.bestiepanti.repository.PaymentRespository;
import com.app.bestiepanti.repository.RoleRepository;
import com.app.bestiepanti.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DonaturRepository donaturRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PantiRepository pantiRepository;
    private final JwtConfig jwtConfig;
    private final PaymentRespository paymentRespository;
    private final EmailService emailService;
    private final ForgotPasswordRepository forgotPasswordRepository;

    public DonaturResponse register(RegisterRequest registerRequest) {
        UserApp user = new UserApp();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());

        Role role = roleRepository.findByName(UserApp.ROLE_DONATUR);
        user.setRole(role);

        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userRepository.save(user);

        Donatur donatur = saveToDonatur(registerRequest, user);

        String jwtToken = jwtService.generateToken(user);
        return createDonaturResponse(user, donatur, jwtToken);
    }

    public Object login(LoginRequest loginRequest) throws UserNotFoundException {
        UserApp user = findUserByEmail(loginRequest.getEmail());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (AuthenticationException e) {
            throw new UserNotFoundException("Email atau Kata Sandi tidak valid. Silakan coba lagi.");
        }
        String jwtToken = jwtService.generateToken(user);
        String existingToken = jwtConfig.getActiveToken(loginRequest.getEmail());
        if (existingToken != null)
            jwtConfig.blacklistToken(existingToken);
        jwtConfig.storeActiveToken(loginRequest.getEmail(), jwtToken);

        if (user.getRole().getName().equals(UserApp.ROLE_DONATUR)) {
            Donatur donatur = donaturRepository.findByUserId(user.getId());
            log.info("Donatur " + user.getId() + " is logged in!");
            return createDonaturResponse(user, donatur, jwtToken);
        } else if (user.getRole().getName().equals(UserApp.ROLE_PANTI)) {
            Panti panti = pantiRepository.findByUserId(user.getId());
            Payment payment = paymentRespository.findByPantiId(panti.getId());
            log.info("Panti " + user.getId() + " is logged in!");
            return createPantiResponse(user, panti, payment, jwtToken);
        } else if (user.getRole().getName().equals(UserApp.ROLE_ADMIN)) {
            log.info("Admin " + user.getId() + " is logged in!");
            return createAdminResponse(user, jwtToken);
        }
        throw new UserNotFoundException("Role tidak ditemukan untuk pengguna. Silakan hubungi dukungan.");
    }

    public void verifyEmail(String email) throws Exception{
        UserApp user = findUserByEmail(email);
        Integer otp = otpGenerator();
        MailRequest mailBody = MailRequest.builder()
                            .to(email)
                            .subject("[No Reply] OTP Forgot Password Bestie Panti Account")
                            .build();
        
        ForgotPassword fp = ForgotPassword.builder()
                    .otp(otp)
                    .expirationTime(new Date(System.currentTimeMillis() + 90 * 1000)) // expire after 90s
                    .isUsed(0)
                    .user(user)
                    .build();

        Map<String, Object> variables = new HashMap<>();
        variables.put("name", user.getName());
        variables.put("otp", otp.toString());

        emailService.sendSimpleMessage(mailBody, variables);
        forgotPasswordRepository.save(fp);
    }

    public void verifyOtp(VerifyOtpRequest request) throws UserNotFoundException {
        UserApp user = findUserByEmail(request.getEmail());
        ForgotPassword fp = forgotPasswordRepository.findByOtpAndUserId(request.getOtp(), user.getId()).orElseThrow(() -> new ValidationException("Kode OTP tidak valid untuk " + request.getEmail()));
        
        if(fp.getIsUsed() == 0){
            if(fp.getExpirationTime().before(Date.from(Instant.now()))) {
                forgotPasswordRepository.deleteById(fp.getId());
                throw new ValidationException("Kode OTP sudah kadaluarsa untuk " + request.getEmail());
            } 
            fp.setIsUsed(1);
            forgotPasswordRepository.save(fp);
        } else {
            throw new ValidationException("Kode OTP sudah digunakan!");
        }
        
    }

    public void resetPassword(ResetPasswordRequest resetPassword) {
        ForgotPassword fp = forgotPasswordRepository.findByUserEmail(resetPassword.getEmail()).orElseThrow(() -> new ValidationException("Kode OTP tidak valid untuk " + resetPassword.getEmail()));

        if (fp.getIsUsed() == 0) 
            throw new ValidationException("Kode OTP belum terverifikasi. Silahkan memverifikasi terlebih dahulu!");

        String encodedPassword = passwordEncoder.encode(resetPassword.getPassword());
        userRepository.updatePassword(resetPassword.getEmail(), encodedPassword);

        forgotPasswordRepository.deleteById(fp.getId());
    }

    public Integer otpGenerator(){
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }

    public UserApp findUserByEmail(String email) throws UserNotFoundException {
        UserApp user = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new UserNotFoundException(
                                "Tidak ditemukan akun dengan alamat email ini. Silakan mendaftar."));
        return user;
    }

    public Donatur saveToDonatur(RegisterRequest userRequest, UserApp user) {
        Donatur donatur = new Donatur();
        donatur.setUser(user);
        donatur.setAddress(userRequest.getAddress());
        donatur.setGender(userRequest.getGender());
        donatur.setPhone(userRequest.getPhone());
        LocalDate dob = LocalDate.parse(userRequest.getDob());
        donatur.setDob(dob);
        if(userRequest.getGender().equals("L")) donatur.setProfile("defaultProfileMale.png");
        else if(userRequest.getGender().equals("P")) donatur.setProfile("defaultProfileFemale.png");
        donaturRepository.save(donatur);
        return donatur;
    }

    public Object getUser() throws UserNotFoundException {
        UserApp user = getAuthenticate();

        if (user.getRole().getName().equals(UserApp.ROLE_DONATUR)) {
            Donatur donatur = donaturRepository.findByUserId(user.getId());
            return createDonaturResponse(user, donatur, null);
        } else if (user.getRole().getName().equals(UserApp.ROLE_PANTI)) {
            Panti panti = pantiRepository.findByUserId(user.getId());
            Payment payment = paymentRespository.findByPantiId(panti.getId());
            return createPantiResponse(user, panti, payment, null);
        }
        return createAdminResponse(user, null);
    }

    public UserApp getAuthenticate() throws UserNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserApp user = findUserByEmail(email);
        return user;
    }

    public AdminResponse createAdminResponse(UserApp userApp, String token) {
        return AdminResponse.builder()
                .id(userApp.getId())
                .name(userApp.getName())
                .email(userApp.getEmail())
                .role(userApp.getRole().getName())
                .token(token)
                .build();
    }

    public DonaturResponse createDonaturResponse(UserApp userApp, Donatur donatur, String token) {
        return DonaturResponse.builder()
                .id(userApp.getId())
                .name(userApp.getName())
                .email(userApp.getEmail())
                .role(userApp.getRole().getName())
                .phone(donatur.getPhone())
                .dob(donatur.getDob().toString())
                .gender(donatur.getGender())
                .address(donatur.getAddress())
                .profile(donatur.getProfile())
                .token(token)
                .build();
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
                .bankAccountName(payment.getBankAccountName())
                .bankAccountNumber(payment.getBankAccountNumber())
                .bankName(payment.getBankName())
                .qris(payment.getQris())
                .token(jwtToken)
                .build();
    }
}
