package io.molnarsandor.pmtool.controller;

import io.molnarsandor.pmtool.domain.dto.ProjectTaskDTO;
import io.molnarsandor.pmtool.domain.entity.ProjectTask;
import io.molnarsandor.pmtool.domain.entity.User;
import io.molnarsandor.pmtool.domain.dto.DeleteDTO;
import io.molnarsandor.pmtool.exceptions.CustomInternalServerErrorExceptionResponse;
import io.molnarsandor.pmtool.exceptions.ProjectNotFoundExceptionResponse;
import io.molnarsandor.pmtool.exceptions.UserNotLoggedInExceptionResponse;
import io.molnarsandor.pmtool.exceptions.ValidationErrorExceptionResponse;
import io.molnarsandor.pmtool.service.MapValidationErrorService;
import io.molnarsandor.pmtool.service.ProjectTaskService;
import io.molnarsandor.pmtool.util.ModelConverter;
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

        User user = (User) authentication.getPrincipal();

        ProjectTask projectTask = modelConverter.projectTaskDtoToEntity(projectTaskDTO);

        ProjectTaskDTO savedProjectTask = modelConverter.projectTaskEntityToDto(
                projectTaskService.addProjectTask(backlogId, projectTask, user.getEmail()));

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

        User user = (User) authentication.getPrincipal();

        List<ProjectTaskDTO> projectTasks = modelConverter.projectTasksEntityListToDto(
                projectTaskService.findBacklogById(backlogId, user.getEmail()));

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

        User user = (User) authentication.getPrincipal();

        ProjectTaskDTO projectTask = modelConverter.projectTaskEntityToDto(
                projectTaskService.findPTByProjectSequence(backlogId, ptId, user.getEmail()));

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

        User user = (User) authentication.getPrincipal();

        mapValidationErrorService.mapValidationService(result);

        ProjectTask projectTask = modelConverter.projectTaskDtoToEntity(projectTaskDTO);

        ProjectTaskDTO updatedTask = modelConverter.projectTaskEntityToDto(
                projectTaskService.updateByProjectSequence(projectTask,backlogId, ptId, user.getEmail()));

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

        User user = (User) authentication.getPrincipal();

        DeleteDTO response = projectTaskService.deletePTByProjectSequence(backlogId, ptId, user.getEmail());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
