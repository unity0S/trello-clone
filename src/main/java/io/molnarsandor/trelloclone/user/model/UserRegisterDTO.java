package io.molnarsandor.trelloclone.user.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@ApiModel
public class UserRegisterDTO {

    @Email(message = "Needs to be a valid email")
    @NotBlank(message = "Email is required")
    @ApiModelProperty(value = "Existing email where the activation link will be sent", required = true)
    private String email;
    @NotBlank(message = "Please enter your full name")
    @Size(min = 5, max = 50, message = "Please use 5 to 50 characters")
    @Pattern(regexp = "^([A-Za-záéúőóüö.]+\\s?){5,}$", message = "Special characters not allowed!")
    @ApiModelProperty(value = "Full name of the User, 5 to 50 chars, no special characters", required = true, position = 1)
    private String fullName;
    @NotBlank(message = "Password field is required")
    @Size(min = 8, max = 100, message = "Please use 8 to 30 characters")
    @ApiModelProperty(value = "Password, 8 to 30 chars", required = true, position = 2)
    private String password;
    @NotBlank(message = "Confirm password field is required")
    @Size(min = 8, max = 100, message = "Please use 8 to 30 characters")
    @ApiModelProperty(value = "Password again", required = true, position = 3)
    private String confirmPassword;

}
