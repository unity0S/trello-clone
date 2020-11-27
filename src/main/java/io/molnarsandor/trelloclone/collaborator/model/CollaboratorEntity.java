package io.molnarsandor.trelloclone.collaborator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", updatable = false, nullable = false)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private ProjectEntity project;

}
