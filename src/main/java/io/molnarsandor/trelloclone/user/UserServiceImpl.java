package io.molnarsandor.trelloclone.user;

import io.molnarsandor.trelloclone.user.exceptions.ActivationKeyNotFoundException;
import io.molnarsandor.trelloclone.user.exceptions.UsernameAlreadyExistsException;
import io.molnarsandor.trelloclone.user.model.UserActivationDTO;
import io.molnarsandor.trelloclone.user.model.UserEntity;
import io.molnarsandor.trelloclone.user.model.UserRegisterDTO;
import io.molnarsandor.trelloclone.user.model.UserRegistrationResponseDTO;
import io.molnarsandor.trelloclone.util.EmailService;
import io.molnarsandor.trelloclone.util.ModelConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final String USER_NOT_FOUND = "User not found";

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final EmailService emailService;

    private final ModelConverter modelConverter;

    private final UserValidator userValidator;

    // == PUBLIC METHODS ==
    @Transactional
    @Override
    public UserDetails loadUserById(Long id) {

        UserEntity userEntity = userRepository.getById(id);

        if (userEntity == null) {
            throw new UsernameNotFoundException(USER_NOT_FOUND);
        }

        return new UserDetailsImpl(userEntity);
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
    public UserRegistrationResponseDTO registerUser(UserRegisterDTO userRegisterDTO, BindingResult result) {

        UserEntity userEntity = modelConverter.userRegisterDtoToEntity(userRegisterDTO);

        userValidator.validate(userEntity, result);

        checkIsUserExists(userEntity.getEmail());

        String uuid = generateKey();
        userEntity.setEmail(userEntity.getEmail());
        userEntity.setPassword(bCryptPasswordEncoder.encode(userEntity.getPassword()));
        userEntity.setConfirmPassword("");
        userEntity.setEnabled(false);
        userEntity.setActivation(uuid);
        userRepository.save(userEntity);

        emailService.sendMessage(userEntity.getEmail(), "Activation email", "You can activate your account following this link: https://trello-clone-ms.herokuapp.com/api/users/activation/" + uuid);
        return modelConverter.userEntityToDto(userEntity);
    }

    public UserActivationDTO userActivation(String key) {

        UserEntity userEntity = userRepository.findByActivation(key);

        if (userEntity == null) {
            throw new ActivationKeyNotFoundException("Activation key not found");
        }

        userEntity.setEnabled(true);
        userEntity.setActivation("");
        userRepository.save(userEntity);

        return new UserActivationDTO("User activated");
    }

    // == PRIVATE METHODS ==
    private String generateKey() {
        return UUID.randomUUID().toString();
    }

    private void checkIsUserExists(String email) {

        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity != null) {
            throw new UsernameAlreadyExistsException("Username '" + email + "' already exists");
        }
    }
}
