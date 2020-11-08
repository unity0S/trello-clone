package io.molnarsandor.pmtool.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

@Entity
@Getter
@Setter
@NoArgsConstructor
@ApiModel(value = "Project Task", description = "A Project Task")
public class ProjectTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    @Column(updatable = false, unique = true)
    @ApiModelProperty(value = "Unique identifier of a Project Task")
    private String projectSequence;
    @NotBlank(message = "Please include a project summary")
    @Size(min = 5, max = 200, message = "Please use 5 to 200 characters")
    @Pattern(regexp = "^([A-Z0-9a-záéúőóüö.]+\\s?){2,}$", message = "Special characters not allowed!")
    @ApiModelProperty(value = "Description of the Project Task", required = true)
    private String summary;
//    @Pattern(regexp = "^([A-Z0-9]([a-záéúőóüö.]+\\s?)){2,}$", message = "Special characters not allowed!")
//    private String acceptanceCriteria;
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
    private Backlog backlog;
    @Column(updatable = false)
    @ApiModelProperty(value = "Unique identifier of the Project where the Task belongs to", required = true)
    private String projectIdentifier;
    @Column(updatable = false)
    @JsonFormat(pattern = "yyyy-mm-dd")
    @ApiModelProperty(value = "Date when the Task was created")
    private Date created_At;
    @JsonFormat(pattern = "yyyy-mm-dd")
    @ApiModelProperty(value = "Date when the Task was Updated last time")
    private Date updated_At;

    @PrePersist
    protected void onCreate() {
        this.created_At = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updated_At = new Date();
    }
}
