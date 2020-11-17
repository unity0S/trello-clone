package io.molnarsandor.trelloclone.user;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return UserEntity.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        UserEntity userEntity = (UserEntity) o;

        if(userEntity.getPassword().length() < 8) {
            errors.rejectValue("password", "Length", "Password must be at least 8 characters");
        }

        if(!userEntity.getPassword().equals(userEntity.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "Length", "Passwords must match");
        }
    }
}
