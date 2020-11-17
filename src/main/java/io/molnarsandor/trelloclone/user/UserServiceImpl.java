package io.molnarsandor.trelloclone.user;

import io.molnarsandor.trelloclone.util.EmailService;
import io.molnarsandor.trelloclone.user.exceptions.ActivationKeyNotFoundException;
import io.molnarsandor.trelloclone.exceptions.CustomInternalServerErrorException;
import io.molnarsandor.trelloclone.user.exceptions.UsernameAlreadyExistsException;
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

    public static final String USER_NOT_FOUND = "User not found";

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final EmailService emailService;

    @Override
    public UserEntity registerUser(UserEntity newUserEntity) {

        UserEntity userEntity = userRepository.findByEmail(newUserEntity.getEmail());

        if (userEntity != null) {
            throw new UsernameAlreadyExistsException("Username '" + newUserEntity.getEmail() + "' already exists");
        }

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
            throw new CustomInternalServerErrorException("Internal Server Error", io);
        }
    }


    @Transactional
    @Override
    public UserEntity loadUserById(Long id) {
        UserEntity userEntity = userRepository.getById(id);

        if (userEntity == null) {
            throw new UsernameNotFoundException(USER_NOT_FOUND);
        }

        return userEntity;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserEntity userEntity = userRepository.findByEmail(username);

        if (userEntity == null) {
            throw new UsernameNotFoundException(USER_NOT_FOUND);
        }

        return new UserDetailsImpl(userEntity);
    }

    @Override
    public UserActivationDTO userActivation(String key) {
        UserEntity userEntity = userRepository.findByActivation(key);

        if (userEntity == null) {
            throw new ActivationKeyNotFoundException("Activation key not found");
        }

        try {
            userEntity.setEnabled(true);
            userEntity.setActivation("");
            userRepository.save(userEntity);
        } catch (DataAccessException io) {
            throw new CustomInternalServerErrorException("Internal Server Error", io);
        }
        return new UserActivationDTO("User activated");
    }


    private String generateKey() {
        return UUID.randomUUID().toString();
    }

}
