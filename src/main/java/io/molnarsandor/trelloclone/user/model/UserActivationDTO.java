package io.molnarsandor.trelloclone.user.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

@Data
@ApiModel
public class UserActivationDTO {

    @NonNull
    @ApiModelProperty(value = "Activation message", example = "User activated")
    private String activation;
}
