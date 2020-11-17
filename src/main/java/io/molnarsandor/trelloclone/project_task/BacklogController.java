package io.molnarsandor.trelloclone.project_task;

import io.molnarsandor.trelloclone.user.UserEntity;
import io.molnarsandor.trelloclone.util.DeleteDTO;
import io.molnarsandor.trelloclone.global_exceptions.CustomInternalServerErrorExceptionResponse;
import io.molnarsandor.trelloclone.project.exceptions.ProjectNotFoundExceptionResponse;
import io.molnarsandor.trelloclone.user.exceptions.UserNotLoggedInExceptionResponse;
import io.molnarsandor.trelloclone.global_exceptions.ValidationErrorExceptionResponse;
import io.molnarsandor.trelloclone.util.MapValidationErrorService;
import io.molnarsandor.trelloclone.util.ModelConverter;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {

    private final ProjectTaskService projectTaskService;

    private final MapValidationErrorService mapValidationErrorService;

    private final ModelConverter modelConverter;

    @PostMapping("/{backlogId}")
    @ApiOperation(value = "Add Project Task to Project", notes = "Add New Project Task to Project", response = ProjectTaskDTO.class)
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created", response = ProjectTaskDTO.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ValidationErrorExceptionResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = UserNotLoggedInExceptionResponse.class),
            @ApiResponse(code = 404, message = "Not Found", response = ProjectNotFoundExceptionResponse.class),
            @ApiResponse(code = 500, message = "Internal server Error", response = CustomInternalServerErrorExceptionResponse.class)
    })
    public ResponseEntity<ProjectTaskDTO> addPTtoBacklog(
            @Valid
            @RequestBody
            @ApiParam(required = true, name = "projectTask", value = "New Project Task")
            ProjectTaskDTO projectTaskDTO,
            @ApiIgnore
            BindingResult result,
            @PathVariable
            @ApiParam(required = true, name = "projectIdentifier", value = "ID of the Project where you want to add a Project Task")
            String backlogId,
            @ApiIgnore
            Authentication authentication) {

        mapValidationErrorService.mapValidationService(result);

        UserEntity userEntity = (UserEntity) authentication.getPrincipal();

        ProjectTaskEntity projectTaskEntity = modelConverter.projectTaskDtoToEntity(projectTaskDTO);

        ProjectTaskDTO savedProjectTask = modelConverter.projectTaskEntityToDto(
                projectTaskService.addProjectTask(backlogId, projectTaskEntity, userEntity.getEmail()));

        return new ResponseEntity<>(savedProjectTask, HttpStatus.CREATED);
    }

    @GetMapping("/{backlogId}")
    @ApiOperation(value = "Get Project Tasks", notes = "Retrieves List of Project Tasks", response = ProjectTaskDTO.class)
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success", response = ProjectTaskDTO.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ValidationErrorExceptionResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = UserNotLoggedInExceptionResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = ProjectNotFoundExceptionResponse.class),
        @ApiResponse(code = 500, message = "Internal server Error", response = CustomInternalServerErrorExceptionResponse.class)
    })
    public ResponseEntity<List<ProjectTaskDTO>> getProjectBacklog(
            @PathVariable
            @ApiParam(required = true, name = "projectIdentifier", value = "ID of the Project")
            String backlogId,
            @ApiIgnore
            Authentication authentication) {

        UserEntity userEntity = (UserEntity) authentication.getPrincipal();

        List<ProjectTaskDTO> projectTasks = modelConverter.projectTasksEntityListToDto(
                projectTaskService.findBacklogById(backlogId, userEntity.getEmail()));

        return new ResponseEntity<>(projectTasks, HttpStatus.OK);
    }

    @GetMapping("/{backlogId}/{ptId}")
    @ApiOperation(value = "Get Project Task", notes = "Retrieves a single Project Task", response = ProjectTaskDTO.class)
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success", response = ProjectTaskDTO.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ValidationErrorExceptionResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = UserNotLoggedInExceptionResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = ProjectNotFoundExceptionResponse.class),
        @ApiResponse(code = 500, message = "Internal server Error", response = CustomInternalServerErrorExceptionResponse.class)
    })
    public ResponseEntity<ProjectTaskDTO> getProjectTask(
            @PathVariable
            @ApiParam(required = true, name = "projectIdentifier", value = "ID of the Project")
            String backlogId,
            @PathVariable
            @ApiParam(required = true, name = "projectSequence", value = "ID of the Project Task you want to retrieve")
            String ptId,
            @ApiIgnore
            Authentication authentication) {

        UserEntity userEntity = (UserEntity) authentication.getPrincipal();

        ProjectTaskDTO projectTask = modelConverter.projectTaskEntityToDto(
                projectTaskService.findPtByProjectSequence(backlogId, ptId, userEntity.getEmail()));

        return new ResponseEntity<>(projectTask, HttpStatus.OK);
    }

    @PatchMapping("/{backlogId}/{ptId}")
    @ApiOperation(value = "Update Project Task", notes = "Updates a Project Task", response = ProjectTaskDTO.class)
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success", response = ProjectTaskDTO.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ValidationErrorExceptionResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = UserNotLoggedInExceptionResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = ProjectNotFoundExceptionResponse.class),
        @ApiResponse(code = 500, message = "Internal server Error", response = CustomInternalServerErrorExceptionResponse.class)
    })
    public ResponseEntity<ProjectTaskDTO> updateProjectTask(
            @Valid
            @RequestBody
            @ApiParam(required = true, name = "Project Task", value = "Updated Project Task")
            ProjectTaskDTO projectTaskDTO,
            @ApiIgnore
            BindingResult result,
            @PathVariable
            @ApiParam(required = true, name = "projectIdentifier", value = "ID of the Project containing the updatable Project Task")
            String backlogId,
            @PathVariable
            @ApiParam(required = true, name = "projectSequence", value = "ID of the Project Task to be updated")
            String ptId,
            @ApiIgnore
            Authentication authentication) {

        UserEntity userEntity = (UserEntity) authentication.getPrincipal();

        mapValidationErrorService.mapValidationService(result);

        ProjectTaskEntity projectTaskEntity = modelConverter.projectTaskDtoToEntity(projectTaskDTO);

        ProjectTaskDTO updatedTask = modelConverter.projectTaskEntityToDto(
                projectTaskService.updateByProjectSequence(projectTaskEntity,backlogId, ptId, userEntity.getEmail()));

        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @DeleteMapping("/{backlogId}/{ptId}")
    @ApiOperation(value = "Delete Project Task", notes = "Deleting an existing Project Task", response = DeleteDTO.class)
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success", response = DeleteDTO.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ValidationErrorExceptionResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = UserNotLoggedInExceptionResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = ProjectNotFoundExceptionResponse.class),
        @ApiResponse(code = 500, message = "Internal server Error", response = CustomInternalServerErrorExceptionResponse.class)
    })
    public ResponseEntity<DeleteDTO> deleteProjectTask(
            @PathVariable
            @ApiParam(required = true, name = "projectIdentifier", value = "ID of the Project that contains the Project Task you want to delete")
            String backlogId,
            @PathVariable
            @ApiParam(required = true, name = "projectSequence", value = "ID of the Project Task you want to delete")
            String ptId,
            @ApiIgnore
            Authentication authentication) {

        UserEntity userEntity = (UserEntity) authentication.getPrincipal();

        DeleteDTO response = projectTaskService.deletePTByProjectSequence(backlogId, ptId, userEntity.getEmail());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
