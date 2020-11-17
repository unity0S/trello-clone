package io.molnarsandor.trelloclone.user;

import io.molnarsandor.trelloclone.global_exceptions.CustomInternalServerErrorException;
import io.molnarsandor.trelloclone.user.exceptions.ActivationKeyNotFoundException;
import io.molnarsandor.trelloclone.user.exceptions.UsernameAlreadyExistsException;
import io.molnarsandor.trelloclone.util.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final String USER_NOT_FOUND = "User not found";

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final EmailService emailService;



    // == PUBLIC METHODS ==
    @Transactional
    @Override
    public UserEntity loadUserById(Long id) {

        UserEntity userEntity;

        try {
            userEntity = userRepository.getById(id);
        } catch (DataAccessException io) {
            throw new CustomInternalServerErrorException(io);
        }

        if (userEntity == null) {
            throw new UsernameNotFoundException(USER_NOT_FOUND);
        }

        return userEntity;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        UserEntity userEntity;

        try {
            userEntity = userRepository.findByEmail(username);
        } catch (DataAccessException io) {
            throw new CustomInternalServerErrorException(io);
        }

        if (userEntity == null) {
            throw new UsernameNotFoundException(USER_NOT_FOUND);
        }

        return new UserDetailsImpl(userEntity);
    }

    // == PROTECTED METHODS ==
    protected UserEntity registerUser(UserEntity newUserEntity) {

        checkIsUserExists(newUserEntity.getEmail());

        try {
            String uuid = generateKey();
            newUserEntity.setEmail(newUserEntity.getEmail());
            newUserEntity.setPassword(bCryptPasswordEncoder.encode(newUserEntity.getPassword()));
            newUserEntity.setConfirmPassword("");
            newUserEntity.setEnabled(false);
            newUserEntity.setActivation(uuid);
            userRepository.save(newUserEntity);

            emailService.sendMessage(newUserEntity.getEmail(), "Activation email", "You can activate your account following this link: https://trello-clone-ms.herokuapp.com/api/users/activation/" + uuid);
            return newUserEntity;
        } catch (DataAccessException io) {
            throw new CustomInternalServerErrorException(io);
        }
    }

    protected UserActivationDTO userActivation(String key) {

        UserEntity userEntity;

        try {
            userEntity = userRepository.findByActivation(key);
        } catch (DataAccessException io) {
            throw new CustomInternalServerErrorException(io);
        }

        if (userEntity == null) {
            throw new ActivationKeyNotFoundException("Activation key not found");
        }

        try {
            userEntity.setEnabled(true);
            userEntity.setActivation("");
            userRepository.save(userEntity);
        } catch (DataAccessException io) {
            throw new CustomInternalServerErrorException(io);
        }
        return new UserActivationDTO("User activated");
    }

    // == PRIVATE METHODS ==
    private String generateKey() {
        return UUID.randomUUID().toString();
    }

    private void checkIsUserExists(String email) {

        UserEntity userEntity;

        try {
            userEntity = userRepository.findByEmail(email);
        } catch (DataAccessException io) {
            throw new CustomInternalServerErrorException(io);
        }

        if (userEntity != null) {
            throw new UsernameAlreadyExistsException("Username '" + email + "' already exists");
        }
    }
}
