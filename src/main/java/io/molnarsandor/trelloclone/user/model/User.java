package io.molnarsandor.trelloclone.user.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@MappedSuperclass
@Data
public class User {

    @Email(message = "Needs to be a valid email")
    @NotBlank(message = "Email is required")
    @ApiModelProperty(value = "Existing email where the activation link will be sent", required = true, example = "test@test.com")
    private String username;
    @NotBlank(message = "Password field is required")
    @Size(min = 8, max = 100, message = "Please use 8 to 30 characters")
    @ApiModelProperty(value = "Password, 8 to 30 chars", required = true, position = 1, example = "password")
    private String password;
}
