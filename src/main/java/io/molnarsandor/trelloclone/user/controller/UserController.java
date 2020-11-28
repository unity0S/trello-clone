package io.molnarsandor.trelloclone.user.controller;

import io.molnarsandor.trelloclone.globalExceptions.CustomGeneralExceptionResponse;
import io.molnarsandor.trelloclone.user.model.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import springfox.documentation.annotations.ApiIgnore;

public interface UserController {

    @ApiOperation(value = "Login", notes = "Login existing User", response = UserLoginResponseDTO.class)
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success", response = UserLoginResponseDTO.class),
        @ApiResponse(code = 400, message = "Bad Request", response = CustomGeneralExceptionResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = CustomGeneralExceptionResponse.class),
        @ApiResponse(code = 500, message = "Internal server Error", response = CustomGeneralExceptionResponse.class)
    })
    ResponseEntity<UserLoginResponseDTO> authenticateUser(@ApiParam(required = true, name = "loginRequest", value = "Email and password")
                                                                  UserLoginDTO userLoginDTO,
                                                          @ApiIgnore
                                                          BindingResult result);

    @ApiOperation(value = "Register", notes = "Register New User", response = UserRegisterDTO.class)
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success", response = UserRegistrationResponseDTO.class),
        @ApiResponse(code = 400, message = "Bad Request", response = CustomGeneralExceptionResponse.class),
        @ApiResponse(code = 500, message = "Internal server Error", response = CustomGeneralExceptionResponse.class)
    })
    ResponseEntity<UserRegistrationResponseDTO> registerUser(@ApiParam(required = true, name = "user", value = "New User")
                                         UserRegisterDTO userRegisterDTO,
                                                             @ApiIgnore
                                         BindingResult result);

    @ApiOperation(value = "Activation", notes = "Registered User activation endpoint", response = UserActivationDTO.class)
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success", response = UserActivationDTO.class),
        @ApiResponse(code = 404, message = "Not Found", response = CustomGeneralExceptionResponse.class),
        @ApiResponse(code = 500, message = "Internal server Error", response = CustomGeneralExceptionResponse.class)
    })
    ResponseEntity<UserActivationDTO> activateUser(@ApiParam(required = true, name = "key", value = "Activation key received in User Email")
                                                   String key);
}
