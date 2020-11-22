package io.molnarsandor.trelloclone.collaborator.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.molnarsandor.trelloclone.project.model.ProjectEntity;
import io.molnarsandor.trelloclone.util.EntitySuperClass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Entity
@Table(name = "collaborator")
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class CollaboratorEntity extends EntitySuperClass {

    @Email(message = "Needs to be a valid email")
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String projectIdentifier;
    @Column(nullable = false)
    private String collaboratorSequence;
    @ManyToOne
    @JoinColumn(name = "project_id", updatable = false, nullable = false)
    @JsonBackReference
    private ProjectEntity project;

}
