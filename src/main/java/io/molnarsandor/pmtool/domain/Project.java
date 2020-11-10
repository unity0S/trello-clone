package io.molnarsandor.pmtool.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

@Entity
@Getter
@Setter
@NoArgsConstructor
@ApiModel(value = "Project", description = "A project")
public class Project {

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
    @JsonFormat(pattern = "yyyy-mm-dd")
    @ApiModelProperty(value = "The Date when the Project finishes")
    private Date endDate;
    @JsonFormat(pattern = "yyyy-mm-dd")
    @Column(updatable = false)
    @ApiModelProperty(value = "Date when the Project was created")
    private Date createdAt;
    @JsonFormat(pattern = "yyyy-mm-dd")
    @ApiModelProperty(value = "Date when the Project was last updated, excluding the Project Task update dates")
    private Date updatedAt;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "project")
    @JsonIgnore
    private Backlog backlog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    @ApiModelProperty(value = "The User email who created/owns the Project")
    private String projectLeader;

    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "project", orphanRemoval = true)
    @JsonManagedReference
    @ApiModelProperty(value = "Invited users to collaborate with the Project (Collaborator)")
    private Set<Collaborator> collaborators;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }
}
