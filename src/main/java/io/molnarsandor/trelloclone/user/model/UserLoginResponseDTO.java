package io.molnarsandor.trelloclone.user.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@AllArgsConstructor
@Data
@ApiModel
public class UserLoginResponseDTO {

    @ApiModelProperty(value = "Boolean")
    private boolean success;
    @ApiModelProperty(value = "Bearer token", example = "Bearer {token}", position = 1)
    private String token;
}
