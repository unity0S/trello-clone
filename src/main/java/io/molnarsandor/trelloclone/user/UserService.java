package io.molnarsandor.trelloclone.user;

import io.molnarsandor.trelloclone.user.model.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;

public interface UserService {

    UserDetails loadUserById(Long id);
    UserRegistrationResponseDTO registerUser(UserRegisterDTO userRegisterDTO, BindingResult result);
    UserActivationDTO userActivation(String key);
}
