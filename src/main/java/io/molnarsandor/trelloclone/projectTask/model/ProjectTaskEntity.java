package io.molnarsandor.trelloclone.projectTask.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.molnarsandor.trelloclone.project.model.ProjectEntity;
import io.molnarsandor.trelloclone.util.EntitySuperClass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "project_task")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProjectTaskEntity extends EntitySuperClass {

    @Column(updatable = false)
    private String projectIdentifier;
    @Column(nullable = false)
    private String summary;
    @Column(nullable = false)
    private String status;
    @Column(nullable = false)
    private Integer priority;
    private LocalDateTime dueDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", updatable = false, nullable = false)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private ProjectEntity project;
}
