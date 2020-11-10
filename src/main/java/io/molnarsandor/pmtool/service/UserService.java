package io.molnarsandor.pmtool.service;

import io.molnarsandor.pmtool.domain.User;
import io.molnarsandor.pmtool.dto.ActivationDTO;

public interface UserService {

    User registerUser(User newUser);
    User loadUserById(Long id);
    ActivationDTO userActivation(String key);
}
