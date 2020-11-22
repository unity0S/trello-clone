package io.molnarsandor.trelloclone.collaborator.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "Collaborator Entity", description = "A simplified User used to handle Project collaboration")
public class CollaboratorDTO {

    @Email(message = "Needs to be a valid email")
    @NotBlank
    @ApiModelProperty(value = "Unique email of an existing or a non-existing User", required = true)
    private String email;
    @NotBlank
    @ApiModelProperty(value = "Unique Project Identifier", required = true, position = 1)
    private String projectIdentifier;
    @ApiModelProperty(value = "Unique Collaborator identifier", position = 2)
    private String collaboratorSequence;

}
