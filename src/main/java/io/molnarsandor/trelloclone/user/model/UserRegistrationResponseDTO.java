package io.molnarsandor.trelloclone.user.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class UserRegistrationResponseDTO {

    @ApiModelProperty(value = "User email")
    private String email;
    @ApiModelProperty(value = "Full name", position = 1)
    private String fullName;
}
