package io.molnarsandor.pmtool.controller;

import io.molnarsandor.pmtool.domain.dto.*;
import io.molnarsandor.pmtool.domain.entity.User;
import io.molnarsandor.pmtool.exceptions.ActivationKeyNotFoundExceptionResponse;
import io.molnarsandor.pmtool.exceptions.CustomInternalServerErrorExceptionResponse;
import io.molnarsandor.pmtool.exceptions.UserNotLoggedInExceptionResponse;
import io.molnarsandor.pmtool.exceptions.ValidationErrorExceptionResponse;
import io.molnarsandor.pmtool.security.JwtTokenProvider;
import io.molnarsandor.pmtool.service.MapValidationErrorService;
import io.molnarsandor.pmtool.service.UserServiceImpl;
import io.molnarsandor.pmtool.util.ModelConverter;
import io.molnarsandor.pmtool.validator.UserValidator;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

import static io.molnarsandor.pmtool.security.SecurityConstans.TOKEN_PREFIX;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    private final MapValidationErrorService mapValidationErrorService;

    private final UserServiceImpl userServiceImpl;

    private final UserValidator userValidator;

    private final JwtTokenProvider tokenProvider;

    private final AuthenticationManager authenticationManager;

    private final ModelConverter modelConverter;

    @PostMapping("/login")
    @ApiOperation(value = "Login", notes = "Login existing User", response = UserLoginResponseDTO.class)
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success", response = UserLoginResponseDTO.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ValidationErrorExceptionResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = UserNotLoggedInExceptionResponse.class),
        @ApiResponse(code = 500, message = "Internal server Error", response = CustomInternalServerErrorExceptionResponse.class)
    })
    public ResponseEntity<UserLoginResponseDTO> authenticateUser(
            @Valid
            @RequestBody
            @ApiParam(required = true, name = "loginRequest", value = "Email and password")
            UserLoginDTO userLoginDTO,
            @ApiIgnore
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

    @PostMapping("/register")
    @ApiOperation(value = "Register", notes = "Register New User", response = UserRegisterDTO.class)
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success", response = UserDTO.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ValidationErrorExceptionResponse.class),
        @ApiResponse(code = 500, message = "Internal server Error", response = CustomInternalServerErrorExceptionResponse.class)
    })
    public ResponseEntity<UserDTO> registerUser(
            @Valid
            @RequestBody
            @ApiParam(required = true, name = "user", value = "New User")
            UserRegisterDTO userRegisterDTO,
            @ApiIgnore
            BindingResult result) {

        User user = modelConverter.userRegisterDtoToEntity(userRegisterDTO);

        userValidator.validate(user, result);

        mapValidationErrorService.mapValidationService(result);

        UserDTO newUser = modelConverter.userEntityToDto(
                userServiceImpl.registerUser(user));

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @GetMapping("/activation/{key}")
    @ApiOperation(value = "Activation", notes = "Registered User activation endpoint", response = UserActivationDTO.class)
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success", response = UserActivationDTO.class),
        @ApiResponse(code = 404, message = "Not Found", response = ActivationKeyNotFoundExceptionResponse.class),
        @ApiResponse(code = 500, message = "Internal server Error", response = CustomInternalServerErrorExceptionResponse.class)
    })
    public ResponseEntity<UserActivationDTO> activateUser(
            @PathVariable
            @ApiParam(required = true, name = "key", value = "Activation key received in User Email")
            String key) {

        UserActivationDTO result = userServiceImpl.userActivation(key);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
