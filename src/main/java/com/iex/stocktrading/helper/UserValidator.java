package com.iex.stocktrading.helper;


import com.iex.stocktrading.model.User;
import com.iex.stocktrading.model.dto.NewUserDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.iex.stocktrading.config.Constants.PASSWORD_MIN_LENGTH;

@Component
public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {

        NewUserDTO user = (NewUserDTO) object;

        if (user.getPassword().length() < PASSWORD_MIN_LENGTH) {
            errors.rejectValue("password", "Length", "Password must be at least " + PASSWORD_MIN_LENGTH + " characters");
        }

        if (!user.getPassword().equals(user.getConfirm_password())) {
            errors.rejectValue("confirmPassword", "Match", "Passwords must match");
        }

    }
}