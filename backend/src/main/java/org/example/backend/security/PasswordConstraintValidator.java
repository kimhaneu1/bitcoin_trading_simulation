package org.example.backend.security;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (!containsNumber(password)) {
            setMessage(context, "비밀번호에 숫자가 포함되어야 합니다.");
            return false;
        }
        if (!containsSpecialCharacter(password)) {
            setMessage(context, "비밀번호에 특수문자가 최소 1개 이상 포함되어야 합니다.");
            return false;
        }
        return true;
    }

    private void setMessage(ConstraintValidatorContext context, String message) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
    }

    private boolean containsNumber(String password) {
        return password.matches(".*\\d.*");
    }

    private boolean containsSpecialCharacter(String password) {
        return password.matches(".*[!@#$%^&*()_+\\-={}:\";'<>?,./].*");
    }
}

