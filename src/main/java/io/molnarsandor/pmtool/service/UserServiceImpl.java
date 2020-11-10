package io.molnarsandor.pmtool.service;

import io.molnarsandor.pmtool.domain.User;
import io.molnarsandor.pmtool.exceptions.UsernameAlreadyExistsException;
import io.molnarsandor.pmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    public static final String USER_NOT_FOUND = "User not found";
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private EmailService emailService;

    @Override
    public User registerUser(User newUser) {

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
        } catch (Exception e) {
            throw new UsernameAlreadyExistsException("Username '" + newUser.getEmail() + "' already exists");
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
    public String userActivation(String key) {
        User user = userRepository.findByActivation(key);

        if(user == null) throw new UsernameNotFoundException(USER_NOT_FOUND);

        user.setEnabled(true);
        user.setActivation("");
        userRepository.save(user);

        return "User activated";
    }


    private String generateKey() {
        return UUID.randomUUID().toString();
    }

}
