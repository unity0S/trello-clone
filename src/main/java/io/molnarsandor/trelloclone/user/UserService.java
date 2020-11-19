package io.molnarsandor.trelloclone.user;

import io.molnarsandor.trelloclone.user.model.UserEntity;

public interface UserService {

    UserEntity loadUserById(Long id);
}
