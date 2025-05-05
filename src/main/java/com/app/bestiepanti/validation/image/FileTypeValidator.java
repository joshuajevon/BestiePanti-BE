package com.app.bestiepanti.validation.image;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class FileTypeValidator implements ConstraintValidator<FileType, Object> {
    private String[] allowedTypes;

    @Override
    public void initialize(FileType constraintAnnotation) {
        this.allowedTypes = constraintAnnotation.allowedTypes();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        if (value instanceof MultipartFile) {
            return isValidFileType((MultipartFile) value);
        } else if (value instanceof List) {
            for (Object item : (List<?>) value) {
                if (item instanceof MultipartFile && !isValidFileType((MultipartFile) item)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private boolean isValidFileType(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && Arrays.asList(allowedTypes).contains(contentType);
    }
}



