package io.molnarsandor.pmtool.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ApiModel(value = "Collaborator", description = "A simplified User used to handle Project collaboration")
public class Collaborator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    @Email(message = "Needs to be a valid email")
    @NotBlank
    @Column(updatable = false)
    @ApiModelProperty(value = "Unique email of an existing or a non-existing User", required = true)
    private String email;
    @Column(updatable = false)
    @ApiModelProperty(value = "Unique Project Identifier", required = true)
    private String projectIdentifier;
    @Column(updatable = false, unique = true)
    @ApiModelProperty(value = "Unique Collaborator identifier")
    private String collaboratorSequence;

    @ManyToOne
    @JoinColumn(name = "project_id", updatable = false, nullable = false)
    @JsonBackReference
    @ApiModelProperty(hidden = true)
    private Project project;
}
