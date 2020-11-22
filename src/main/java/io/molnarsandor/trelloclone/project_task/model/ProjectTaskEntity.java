package io.molnarsandor.trelloclone.project_task.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.molnarsandor.trelloclone.util.EntitySuperClass;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

@SuppressFBWarnings({"EI_EXPOSE_REP2", "EI_EXPOSE_REP"})
@Entity
@Table(name = "project_task")
@Data
@EqualsAndHashCode(callSuper = false)
public class ProjectTaskEntity extends EntitySuperClass {

    @Column(updatable = false)
    private String projectIdentifier;
    @Column(updatable = false, unique = true)
    private String projectSequence;
    @Column(nullable = false)
    private String summary;
    @Column(nullable = false)
    private String status;
    @Column(nullable = false)
    private Integer priority;
    private LocalDateTime dueDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "backlog_id", updatable = false, nullable = false)
    @JsonIgnore
    private BacklogEntity backlog;

    public ProjectTaskEntity() {
        this.status = "TO_DO";
        this.priority = 3;
    }
}
