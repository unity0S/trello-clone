package io.molnarsandor.trelloclone.user.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel
@AllArgsConstructor
public class UserActivationDTO {

    @NotBlank
    @ApiModelProperty(value = "Activation message", example = "User activated")
    private String activation;
}
