package io.molnarsandor.trelloclone.project.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.molnarsandor.trelloclone.collaborator.model.CollaboratorEntity;
import io.molnarsandor.trelloclone.project_task.model.BacklogEntity;
import io.molnarsandor.trelloclone.user.model.UserEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@SuppressFBWarnings({"EI_EXPOSE_REP2", "EI_EXPOSE_REP"})
@Entity
@Table(name = "project")
@Getter
@Setter
@NoArgsConstructor
@ApiModel(value = "Project", description = "A project")
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    @NotBlank(message = "Project name is required")
    @Size(min = 5,max = 30, message = "Please use 5 to 30 characters")
    @Pattern(regexp = "^([A-Z0-9a-záéúőóüö.]+\\s?){2,}$", message = "Special characters not allowed!")
    @ApiModelProperty(value = "The Project name", required = true)
    private String projectName;
    @NotBlank(message = "Project Identifier is required")
    @Size(min = 4, max = 30, message = "Please use 4 to 30 characters")
    @Column(updatable = false, unique = true)
    @Pattern(regexp = "^([A-Z0-9a-záéúőóüö.]+\\s?){2,}$", message = "Special characters not allowed!")
    @ApiModelProperty(value = "The Project Identifier", required = true)
    private String projectIdentifier;
    @NotBlank(message = "Project description is required")
    @Size(min = 5, max = 200, message = "Please use 5 to 200 characters")
    @Pattern(regexp = "^([A-Z0-9a-záéúőóüö.]+\\s?){2,}$", message = "Special characters not allowed!")
    @ApiModelProperty(value = "Project description", required = true)
    private String description;
    @JsonFormat(pattern = "yyyy-mm-dd")
    @ApiModelProperty(value = "The Date when the Project starts")
    private Date startDate;
    @SuppressFBWarnings("EI_EXPOSE_REP")
    @JsonFormat(pattern = "yyyy-mm-dd")
    @ApiModelProperty(value = "The Date when the Project finishes")
    private Date endDate;
    @SuppressFBWarnings("EI_EXPOSE_REP")
    @JsonFormat(pattern = "yyyy-mm-dd")
    @Column(updatable = false)
    @ApiModelProperty(value = "Date when the Project was created")
    private Date createdAt;
    @SuppressFBWarnings("EI_EXPOSE_REP")
    @JsonFormat(pattern = "yyyy-mm-dd")
    @ApiModelProperty(value = "Date when the Project was last updated, excluding the Project Task update dates")
    private Date updatedAt;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "project")
    @JsonIgnore
    private BacklogEntity backlog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserEntity user;

    @ApiModelProperty(value = "The User email who created/owns the Project")
    private String projectLeader;

    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "project", orphanRemoval = true)
    @JsonManagedReference
    @ApiModelProperty(value = "Invited users to collaborate with the Project (Collaborator)")
    private Set<CollaboratorEntity> collaborators;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }
}
