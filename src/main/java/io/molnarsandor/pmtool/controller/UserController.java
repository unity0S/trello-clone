package io.molnarsandor.pmtool.controller;

import io.molnarsandor.pmtool.domain.User;
import io.molnarsandor.pmtool.dto.ActivationDTO;
import io.molnarsandor.pmtool.exceptions.ActivationKeyNotFoundExceptionResponse;
import io.molnarsandor.pmtool.exceptions.CustomInternalServerErrorExceptionResponse;
import io.molnarsandor.pmtool.exceptions.UserNotLoggedInExceptionResponse;
import io.molnarsandor.pmtool.exceptions.ValidationErrorExceptionResponse;
import io.molnarsandor.pmtool.payload.JWTLoginSuccessResponse;
import io.molnarsandor.pmtool.payload.LoginRequest;
import io.molnarsandor.pmtool.security.JwtTokenProvider;
import io.molnarsandor.pmtool.service.EmailService;
import io.molnarsandor.pmtool.service.MapValidationErrorService;
import io.molnarsandor.pmtool.service.UserServiceImpl;
import io.molnarsandor.pmtool.validator.UserValidator;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static io.molnarsandor.pmtool.security.SecurityConstans.TOKEN_PREFIX;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmailService emailService;

    @PostMapping("/login")
    @ApiOperation(value = "Login", notes = "Login existing User", response = JWTLoginSuccessResponse.class)
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success", response = JWTLoginSuccessResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ValidationErrorExceptionResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = UserNotLoggedInExceptionResponse.class),
        @ApiResponse(code = 500, message = "Internal server Error", response = CustomInternalServerErrorExceptionResponse.class)
    })
    public ResponseEntity<JWTLoginSuccessResponse> authenticateUser(
            @Valid
            @RequestBody
            @ApiParam(required = true, name = "loginRequest", value = "Email and password")
            LoginRequest loginRequest,
            BindingResult result) {
        mapValidationErrorService.mapValidationService(result);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = TOKEN_PREFIX + tokenProvider.generateToken(authentication);

        return new ResponseEntity<>(new JWTLoginSuccessResponse(true, jwt), HttpStatus.OK);
    }

    @PostMapping("/register")
    @ApiOperation(value = "Register", notes = "Register New User", response = User.class)
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success", response = User.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ValidationErrorExceptionResponse.class),
        @ApiResponse(code = 500, message = "Internal server Error", response = CustomInternalServerErrorExceptionResponse.class)
    })
    public ResponseEntity<User> registerUser(
            @Valid
            @RequestBody
            @ApiParam(required = true, name = "user", value = "New User")
            User user,
            BindingResult result) {

        userValidator.validate(user, result);

        mapValidationErrorService.mapValidationService(result);

        User newUser = userServiceImpl.registerUser(user);

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @GetMapping("/activation/{key}")
    @ApiOperation(value = "Activation", notes = "Registered User activation endpoint", response = String.class)
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success", response = String.class),
        @ApiResponse(code = 404, message = "Not Found", response = ActivationKeyNotFoundExceptionResponse.class),
        @ApiResponse(code = 500, message = "Internal server Error", response = CustomInternalServerErrorExceptionResponse.class)
    })
    public ResponseEntity<ActivationDTO> activateUser(
            @PathVariable
            @ApiParam(required = true, name = "key", value = "Activation key received in User Email")
            String key) {

        ActivationDTO result = userServiceImpl.userActivation(key);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
