package io.molnarsandor.pmtool.service;

import io.molnarsandor.pmtool.domain.User;

public interface UserService {

    User registerUser(User newUser);
    User loadUserById(Long id);
    String userActivation(String key);
}
