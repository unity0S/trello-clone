package io.molnarsandor.trelloclone.project_task;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@SuppressFBWarnings({"EI_EXPOSE_REP2", "EI_EXPOSE_REP"})
@Data
@ApiModel
public class ProjectTaskDTO {

    @ApiModelProperty(value = "Unique identifier of the Project where the Task belongs to")
    private String projectIdentifier;
    @ApiModelProperty(value = "Unique identifier of a Project Task", position = 1)
    private String projectSequence;
    @NotBlank(message = "Please include a project summary")
    @Size(min = 5, max = 200, message = "Please use 5 to 200 characters")
    @Pattern(regexp = "^([A-Z0-9a-záéúőóüö.]+\\s?){2,}$", message = "Special characters not allowed!")
    @ApiModelProperty(value = "Description of the Project Task, 5 to 200 chars, no special characters", required = true, position = 2)
    private String summary;
    @ApiModelProperty(value = "Status of a task", position = 3)
    private String status;
    @ApiModelProperty(value = "Priority of a task", position = 4)
    private Integer priority;
    @JsonFormat(pattern = "yyyy-mm-dd")
    @ApiModelProperty(value = "Date when the task should be finished", position = 5)
    private Date dueDate;
}
