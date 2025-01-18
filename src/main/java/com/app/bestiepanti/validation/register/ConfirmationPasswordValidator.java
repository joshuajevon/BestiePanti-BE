package com.app.bestiepanti.validation.register;

import com.app.bestiepanti.dto.request.RegisterRequest;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ConfirmationPasswordValidator implements ConstraintValidator<ConfirmationPassword, RegisterRequest>{

    @Override
    public boolean isValid(RegisterRequest request, ConstraintValidatorContext context) {
        if (request == null) return true;
        boolean isValid = request.getPassword().equals(request.getConfirmationPassword());
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Confirmation password do not match").addPropertyNode("confirmation_password").addConstraintViolation();
        }
        return isValid;
    }
}

