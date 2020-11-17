package io.molnarsandor.trelloclone.project_task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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

@SuppressFBWarnings({"EI_EXPOSE_REP2", "EI_EXPOSE_REP"})
@Entity
@Table(name = "project_task")
@Getter
@Setter
@NoArgsConstructor
@ApiModel(value = "Project Task", description = "A Project Task")
public class ProjectTaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    @Column(updatable = false)
    @ApiModelProperty(value = "Unique identifier of the Project where the Task belongs to", required = true)
    private String projectIdentifier;
    @Column(updatable = false, unique = true)
    @ApiModelProperty(value = "Unique identifier of a Project Task")
    private String projectSequence;
    @NotBlank(message = "Please include a project summary")
    @Size(min = 5, max = 200, message = "Please use 5 to 200 characters")
    @Pattern(regexp = "^([A-Z0-9a-záéúőóüö.]+\\s?){2,}$", message = "Special characters not allowed!")
    @ApiModelProperty(value = "Description of the Project Task", required = true)
    private String summary;
    @ApiModelProperty(value = "Status of a task")
    private String status;
    @ApiModelProperty(value = "Priority of a task")
    private Integer priority;
    @JsonFormat(pattern = "yyyy-mm-dd")
    @ApiModelProperty(value = "Date when the task should be finished")
    private Date dueDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "backlog_id", updatable = false, nullable = false)
    @JsonIgnore
    private BacklogEntity backlog;
    @Column(updatable = false)
    @JsonFormat(pattern = "yyyy-mm-dd")
    @ApiModelProperty(value = "Date when the Task was created")
    private Date createdAt;
    @JsonFormat(pattern = "yyyy-mm-dd")
    @ApiModelProperty(value = "Date when the Task was Updated last time")
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }
}
