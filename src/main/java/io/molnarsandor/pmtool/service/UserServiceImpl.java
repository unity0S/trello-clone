package io.molnarsandor.pmtool.service;

import io.molnarsandor.pmtool.domain.User;
import io.molnarsandor.pmtool.dto.ActivationDTO;
import io.molnarsandor.pmtool.exceptions.ActivationKeyNotFoundException;
import io.molnarsandor.pmtool.exceptions.CustomInternalServerErrorException;
import io.molnarsandor.pmtool.exceptions.UsernameAlreadyExistsException;
import io.molnarsandor.pmtool.repositories.UserRepository;
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
    public User registerUser(User newUser) {

        User user = userRepository.findByEmail(newUser.getEmail());

        if (user != null) throw new UsernameAlreadyExistsException("Username '" + newUser.getEmail() + "' already exists");

        try {
            String uuid = generateKey();
            newUser.setEmail(newUser.getEmail());
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
            newUser.setConfirmPassword("");
            newUser.setEnabled(false);
            newUser.setActivation(uuid);
            userRepository.save(newUser);

            emailService.sendMessage(newUser.getEmail(), "Activation email", "You can activate your account following this link: https://trello-clone-ms.herokuapp.com/api/users/activation/" + uuid);
            return newUser;
        } catch (DataAccessException io) {
            throw new CustomInternalServerErrorException("Internal Server Error", io);
        }
    }


    @Transactional
    @Override
    public User loadUserById(Long id) {
        User user = userRepository.getById(id);

        if(user == null) throw new UsernameNotFoundException(USER_NOT_FOUND);

        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByEmail(username);

        if (user == null) throw new UsernameNotFoundException(USER_NOT_FOUND);

        return new UserDetailsImpl(user);
    }

    @Override
    public ActivationDTO userActivation(String key) {
        User user = userRepository.findByActivation(key);

        if(user == null) throw new ActivationKeyNotFoundException("Activation key not found");

        try {
            user.setEnabled(true);
            user.setActivation("");
            userRepository.save(user);
        } catch (DataAccessException io) {
            throw new CustomInternalServerErrorException("Internal Server Error", io);
        }
        return new ActivationDTO("User activated");
    }


    private String generateKey() {
        return UUID.randomUUID().toString();
    }

}
