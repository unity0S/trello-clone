package io.molnarsandor.trelloclone.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.molnarsandor.trelloclone.collaborator.model.CollaboratorEntity;
import io.molnarsandor.trelloclone.project_task.model.BacklogEntity;
import io.molnarsandor.trelloclone.user.model.UserEntity;
import io.molnarsandor.trelloclone.util.EntitySuperClass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@SuppressFBWarnings({"EI_EXPOSE_REP2", "EI_EXPOSE_REP"})
@Entity
@Table(name = "project")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class ProjectEntity extends EntitySuperClass {

    @Column(nullable = false)
    private String projectName;
    @Column(nullable = false)
    private String projectIdentifier;
    @Column(nullable = false)
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    @Column(nullable = false)
    private String projectLeader;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "project", orphanRemoval = true)
    @JsonManagedReference
    private Set<CollaboratorEntity> collaborators;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "project")
    @JsonIgnore
    private BacklogEntity backlog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserEntity user;

}
