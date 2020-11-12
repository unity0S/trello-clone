package io.molnarsandor.pmtool.domain.dto;

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
