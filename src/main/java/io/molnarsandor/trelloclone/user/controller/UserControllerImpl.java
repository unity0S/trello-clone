package io.molnarsandor.trelloclone.user.controller;

import io.molnarsandor.trelloclone.security.JwtTokenProvider;
import io.molnarsandor.trelloclone.user.UserServiceImpl;
import io.molnarsandor.trelloclone.user.model.*;
import io.molnarsandor.trelloclone.util.MapValidationErrorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static io.molnarsandor.trelloclone.security.SecurityConstants.TOKEN_PREFIX;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserControllerImpl implements UserController {

    private final MapValidationErrorService mapValidationErrorService;

    private final UserServiceImpl userServiceImpl;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider tokenProvider;

    @Override
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> authenticateUser(@Valid
                                                                 @RequestBody
                                                                 UserLoginDTO userLoginDTO,
                                                                 BindingResult result) {

        mapValidationErrorService.mapValidationService(result);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginDTO.getUsername(),
                        userLoginDTO.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = TOKEN_PREFIX + tokenProvider.generateToken(authentication);

        return new ResponseEntity<>(new UserLoginResponseDTO(true, jwt), HttpStatus.OK);
    }

    @Override
    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponseDTO> registerUser(@Valid
                                                                    @RequestBody
                                                                    UserRegisterDTO userRegisterDTO,
                                                                    BindingResult result) {

        mapValidationErrorService.mapValidationService(result);
        UserRegistrationResponseDTO newUser = userServiceImpl.registerUser(userRegisterDTO, result);

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @Override
    @GetMapping("/activation/{key}")
    public ResponseEntity<UserActivationDTO> activateUser(@PathVariable String key) {

        UserActivationDTO result = userServiceImpl.userActivation(key);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
