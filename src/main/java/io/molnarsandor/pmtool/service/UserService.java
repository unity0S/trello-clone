package io.molnarsandor.pmtool.service;

import io.molnarsandor.pmtool.domain.entity.User;
import io.molnarsandor.pmtool.domain.dto.UserActivationDTO;

public interface UserService {

    User registerUser(User newUser);
    User loadUserById(Long id);
    UserActivationDTO userActivation(String key);
}
