package com.app.bestiepanti.service;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.app.bestiepanti.configuration.ApplicationConfig;
import com.app.bestiepanti.configuration.JwtConfig;
import com.app.bestiepanti.dto.request.auth.LoginRequest;
import com.app.bestiepanti.dto.request.auth.MailRequest;
import com.app.bestiepanti.dto.request.auth.changecredential.ChangePasswordRequest;
import com.app.bestiepanti.dto.request.auth.forgotpassword.ResetPasswordRequest;
import com.app.bestiepanti.dto.request.auth.forgotpassword.VerifyOtpRequest;
import com.app.bestiepanti.dto.request.auth.register.RegisterRequest;
import com.app.bestiepanti.dto.request.auth.register.VerifyOtpRegisterRequest;
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
import com.app.bestiepanti.model.TwoStepVerification;
import com.app.bestiepanti.model.UserApp;
import com.app.bestiepanti.repository.DonaturRepository;
import com.app.bestiepanti.repository.ForgotPasswordRepository;
import com.app.bestiepanti.repository.PantiRepository;
import com.app.bestiepanti.repository.PaymentRespository;
import com.app.bestiepanti.repository.RoleRepository;
import com.app.bestiepanti.repository.TwoStepVerificationRepository;
import com.app.bestiepanti.repository.UserRepository;

import jakarta.servlet.http.HttpServletResponse;
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
    private final TwoStepVerificationRepository twoStepVerificationRepository;
    private final ApplicationConfig applicationConfig;

    public void authenticateWithGoogle(OAuth2User principal, HttpServletResponse response) throws UserNotFoundException, IOException {
        String name = null;
        String email = null;
    
        if (principal != null) {
            name = principal.getAttribute("name");
            email = principal.getAttribute("email");
        } else {
            throw new ValidationException("Pengguna tidak terautentikasi!");
        }
    
        Optional<UserApp> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            UserApp user = existingUser.get();
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail(email);
            loginRequest.setPassword(UUID.randomUUID().toString());
    
            String jwtToken = jwtService.generateToken(user);
            String redirectUrl = applicationConfig.getUrlFrontEnd() + "/login/callback?token=" + jwtToken;
            response.sendRedirect(redirectUrl);
            return;
        }
    
        UserApp newUser = new UserApp();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setIsGoogle(1);
        log.info("Logged in to Google Account Email: " + email);
    
        Role role = roleRepository.findByName(UserApp.ROLE_DONATUR);
        newUser.setRole(role);
    
        newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        userRepository.save(newUser);

        saveToDonaturWithGoogle(newUser);
    
        String jwtToken = jwtService.generateToken(newUser);
        String redirectUrl = applicationConfig.getUrlFrontEnd() + "/login/callback?token=" + jwtToken;
        response.sendRedirect(redirectUrl);
    }

    public void sendOtpTwoStepRegistration(RegisterRequest request) throws Exception {
        MailRequest mailBody = MailRequest.builder()
                                .to(request.getEmail())
                                .subject("[No Reply] OTP Two Step Verification Bestie Panti Account")
                                .build();

        Integer otp = otpGenerator();
        TwoStepVerification twoStepVerification = new TwoStepVerification();
        twoStepVerification.setExpirationTime(new Date(System.currentTimeMillis() + 90 * 1000));
        twoStepVerification.setEmail(request.getEmail());
        twoStepVerification.setOtp(passwordEncoder.encode(otp.toString()));
        twoStepVerificationRepository.save(twoStepVerification);

        Map<String, Object> variables = new HashMap<>();
        variables.put("name", request.getName());
        variables.put("otp", otp.toString());

        emailService.sendEmailOtp(mailBody, variables);
    }

    public DonaturResponse verifyOtpRegistration(VerifyOtpRegisterRequest request) throws UserNotFoundException {
        UserApp user = new UserApp();
        TwoStepVerification twoStepVerification = twoStepVerificationRepository.findTopByEmailOrderByIdDesc(request.getEmail());

        if (twoStepVerification != null && passwordEncoder.matches(request.getOtp(), twoStepVerification.getOtp())) {
            if(twoStepVerification.getVerifiedTimestamp() == null){
                if(twoStepVerification.getExpirationTime().before(Date.from(Instant.now()))) {
                    twoStepVerificationRepository.deleteById(twoStepVerification.getId());
                    throw new ValidationException("Kode OTP sudah kadaluarsa untuk " + request.getEmail() + ". Silahkan mendaftar ulang!");
                }
                
                user.setName(request.getName());
                user.setEmail(request.getEmail());
                user.setPassword(passwordEncoder.encode(request.getPassword()));

                Role role = roleRepository.findByName(UserApp.ROLE_DONATUR);
                user.setRole(role);

                user.setIsGoogle(0);
                userRepository.save(user);
                
                saveToDonatur(request, user);   
                        
                twoStepVerification.setVerifiedTimestamp(LocalDateTime.now());
                twoStepVerificationRepository.save(twoStepVerification);
            } else {
                throw new ValidationException("Kode OTP sudah digunakan!");
            }
        } else {
            throw new ValidationException("Kode OTP tidak valid untuk " + request.getEmail());
        }

        Donatur donatur = donaturRepository.findByUserId(user.getId());
        String jwtToken = jwtService.generateToken(user);
        return createDonaturResponse(user, donatur, jwtToken);
    }

    public Object login(LoginRequest loginRequest, Boolean isGoogle) throws UserNotFoundException {
        UserApp user = findUserByEmail(loginRequest.getEmail());
        if (!isGoogle) {
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            } catch (AuthenticationException e) {
                throw new UserNotFoundException("Email atau kata sandi tidak valid. Silakan coba lagi.");
            }
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
                    .otp(passwordEncoder.encode(otp.toString()))
                    .expirationTime(new Date(System.currentTimeMillis() + 90 * 1000)) // expire after 90s
                    .isUsed(0)
                    .user(user)
                    .build();

        Map<String, Object> variables = new HashMap<>();
        variables.put("name", user.getName());
        variables.put("otp", otp.toString());

        emailService.sendEmailOtp(mailBody, variables);
        forgotPasswordRepository.save(fp);
    }

    public void verifyOtpForgotPassword(VerifyOtpRequest request) throws UserNotFoundException {
        UserApp user = findUserByEmail(request.getEmail());
        ForgotPassword fp = forgotPasswordRepository.findTopByUserIdOrderByIdDesc(user.getId()).orElseThrow(() -> new ValidationException("Tidak ditemukan permintaan ubah password pada akun " + user.getId()));
        if (fp != null && passwordEncoder.matches(request.getOtp(), fp.getOtp())) {
            if(fp.getIsUsed() == 0){
                if(fp.getExpirationTime().before(Date.from(Instant.now()))) {
                    forgotPasswordRepository.deleteById(fp.getId());
                    throw new ValidationException("Kode OTP sudah kadaluarsa untuk " + request.getEmail() + ". Silahkan verifikasi ulang!");
                } 
                fp.setIsUsed(1);
                forgotPasswordRepository.save(fp);
            } else {
                throw new ValidationException("Kode OTP sudah digunakan!");
            }
        } else {
            throw new ValidationException("Kode OTP tidak valid untuk " + request.getEmail());
        }
    }

    public void resetPassword(ResetPasswordRequest resetPassword) {
        ForgotPassword fp = forgotPasswordRepository.findTopByUserEmail(resetPassword.getEmail()).orElseThrow(() -> new ValidationException("Kode OTP tidak valid untuk " + resetPassword.getEmail()));

        if (fp.getIsUsed() == 0) 
            throw new ValidationException("Kode OTP belum terverifikasi. Silahkan memverifikasi terlebih dahulu!");

        String encodedPassword = passwordEncoder.encode(resetPassword.getPassword());
        userRepository.updatePassword(resetPassword.getEmail(), encodedPassword);

        forgotPasswordRepository.deleteById(fp.getId());
    }

    public String changePassword(ChangePasswordRequest request) throws UserNotFoundException {
        UserApp user = getAuthenticate();

        if(!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword()))
            throw new ValidationException("Kata sandi lama salah!");

        if(!request.getNewPassword().equals(request.getConfirmationPassword()))
            throw new ValidationException("Konfirmasi kata sandi baru tidak sama!");
        
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return user.getEmail();
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

    public Donatur saveToDonatur(VerifyOtpRegisterRequest userRequest, UserApp user) {
        Donatur donatur = new Donatur();
        donatur.setUser(user);
        donatur.setAddress(userRequest.getAddress());
        donatur.setGender(userRequest.getGender());
        donatur.setPhone("+62"+userRequest.getPhone());
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

    public Donatur saveToDonaturWithGoogle(UserApp user) {
        Donatur donatur = new Donatur();
        donatur.setUser(user);
        donatur.setAddress(null);
        donatur.setGender(null);
        donatur.setPhone(null);
        donatur.setDob(null);
        donatur.setProfile("defaultProfileGeneral.png");
        donaturRepository.save(donatur);
        return donatur;
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
                .isGoogle(userApp.getIsGoogle().toString())
                .phone(donatur != null ? donatur.getPhone(): null)
                .dob(donatur != null ? Optional.ofNullable(donatur.getDob()).map(LocalDate::toString).orElse(null) : null)
                .gender(donatur != null ? donatur.getGender() : null)
                .address(donatur != null ? donatur.getAddress() : null)
                .profile(donatur != null ? donatur.getProfile() : null)
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
                .maps(panti.getMaps())
                .bankAccountName(payment.getBankAccountName())
                .bankAccountNumber(payment.getBankAccountNumber())
                .bankName(payment.getBankName())
                .qris(payment.getQris())
                .token(jwtToken)
                .build();
    }

}
