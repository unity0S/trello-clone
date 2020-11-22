package io.molnarsandor.trelloclone.project_task.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.molnarsandor.trelloclone.util.EntitySuperClass;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Project Task", description = "A Project Task")
public class ProjectTaskDTO extends EntitySuperClass {

    @ApiModelProperty(value = "Unique identifier of the Project where the Task belongs to", required = true)
    private String projectIdentifier;
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
    private LocalDateTime dueDate;
}
