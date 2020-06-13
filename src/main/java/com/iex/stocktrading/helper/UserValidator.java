package com.iex.stocktrading.helper;


import com.iex.stocktrading.model.User;
import com.iex.stocktrading.model.dto.NewUserDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {

        NewUserDTO user = (NewUserDTO) object;

        if (!user.getPassword().equals(user.getConfirm_password())) {
            errors.rejectValue("confirm_password", "Match", "Passwords must match");
        }

    }
}