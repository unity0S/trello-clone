package io.molnarsandor.trelloclone.user;

public interface UserService {

    UserEntity registerUser(UserEntity newUserEntity);
    UserEntity loadUserById(Long id);
    UserActivationDTO userActivation(String key);
}
