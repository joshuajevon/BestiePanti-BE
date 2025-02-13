package com.app.bestiepanti.validation.general;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class ConfirmationPasswordValidator implements ConstraintValidator<ConfirmationPassword, Object> {

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        try {
            Field passwordField = obj.getClass().getDeclaredField("password");
            Field confirmationPasswordField = obj.getClass().getDeclaredField("confirmationPassword");
            passwordField.setAccessible(true);
            confirmationPasswordField.setAccessible(true);

            String password = (String) passwordField.get(obj);
            String confirmationPassword = (String) confirmationPasswordField.get(obj);

            boolean isValid = password != null && password.equals(confirmationPassword);
            if (!isValid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Confirmation password do not match")
                       .addPropertyNode("confirmationPassword").addConstraintViolation();
            }
            return isValid;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Error validating confirmation password", e);
        }
    }
}


