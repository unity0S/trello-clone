package io.molnarsandor.trelloclone.projectTask.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.molnarsandor.trelloclone.project.model.ProjectEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "backlog")
@Data
@NoArgsConstructor
public class BacklogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer projectTaskSequence = 0;
    private String projectIdentifier;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id", nullable = false)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private ProjectEntity project;

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, mappedBy = "backlog", orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    private Set<ProjectTaskEntity> projectTasks = new HashSet<>();
}
