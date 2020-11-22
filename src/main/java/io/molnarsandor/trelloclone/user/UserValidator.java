package io.molnarsandor.trelloclone.user;

import io.molnarsandor.trelloclone.user.model.UserEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    @Override
    public boolean supports(@NonNull  Class<?> aClass) {
        return UserEntity.class.equals(aClass);
    }

    @Override
    public void validate(@NonNull Object o, @NonNull Errors errors) {

        UserEntity userEntity = (UserEntity) o;

        if(!userEntity.getPassword().equals(userEntity.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "Length", "Passwords must match");
        }
    }
}
