package io.molnarsandor.pmtool.controller;

import io.molnarsandor.pmtool.domain.ProjectTask;
import io.molnarsandor.pmtool.domain.User;
import io.molnarsandor.pmtool.dto.DeleteDTO;
import io.molnarsandor.pmtool.exceptions.CustomInternalServerErrorExceptionResponse;
import io.molnarsandor.pmtool.exceptions.ProjectNotFoundExceptionResponse;
import io.molnarsandor.pmtool.exceptions.UserNotLoggedInExceptionResponse;
import io.molnarsandor.pmtool.exceptions.ValidationErrorExceptionResponse;
import io.molnarsandor.pmtool.service.MapValidationErrorService;
import io.molnarsandor.pmtool.service.ProjectTaskService;
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

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {

    private final ProjectTaskService projectTaskService;

    private final MapValidationErrorService mapValidationErrorService;

    @PostMapping("/{backlogId}")
    @ApiOperation(value = "Add Project Task to Project", notes = "Add New Project Task to Project", response = ProjectTask.class)
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created", response = ProjectTask.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ValidationErrorExceptionResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = UserNotLoggedInExceptionResponse.class),
            @ApiResponse(code = 404, message = "Not Found", response = ProjectNotFoundExceptionResponse.class),
            @ApiResponse(code = 500, message = "Internal server Error", response = CustomInternalServerErrorExceptionResponse.class)
    })
    public ResponseEntity<ProjectTask> addPTtoBacklog(
            @Valid
            @RequestBody
            @ApiParam(required = true, name = "projectTask", value = "New Project Task")
            ProjectTask projectTask,
            BindingResult result,
            @PathVariable
            @ApiParam(required = true, name = "projectIdentifier", value = "ID of the Project where you want to add a Project Task")
            String backlogId,
            Authentication authentication) {

        mapValidationErrorService.mapValidationService(result);

        User user = (User) authentication.getPrincipal();

        ProjectTask projectTask1 = projectTaskService.addProjectTask(backlogId, projectTask, user.getEmail());

        return new ResponseEntity<>(projectTask1, HttpStatus.CREATED);
    }

    @GetMapping("/{backlogId}")
    @ApiOperation(value = "Get Project Tasks", notes = "Retrieves Project Tasks", response = ProjectTask.class)
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success", response = ProjectTask.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ValidationErrorExceptionResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = UserNotLoggedInExceptionResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = ProjectNotFoundExceptionResponse.class),
        @ApiResponse(code = 500, message = "Internal server Error", response = CustomInternalServerErrorExceptionResponse.class)
    })
    public Iterable<ProjectTask> getProjectBacklog(
            @PathVariable
            @ApiParam(required = true, name = "projectIdentifier", value = "ID of the Project")
            String backlogId,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        return projectTaskService.findBacklogById(backlogId, user.getEmail());
    }

    @GetMapping("/{backlogId}/{ptId}")
    @ApiOperation(value = "Get Project Task", notes = "Retrieves a single Project Task", response = ProjectTask.class)
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success", response = ProjectTask.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ValidationErrorExceptionResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = UserNotLoggedInExceptionResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = ProjectNotFoundExceptionResponse.class),
        @ApiResponse(code = 500, message = "Internal server Error", response = CustomInternalServerErrorExceptionResponse.class)
    })
    public ResponseEntity<ProjectTask> getProjectTask(
            @PathVariable
            @ApiParam(required = true, name = "projectIdentifier", value = "ID of the Project")
            String backlogId,
            @PathVariable
            @ApiParam(required = true, name = "projectSequence", value = "ID of the Project Task you want to retrieve")
            String ptId,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        ProjectTask projectTask = projectTaskService.findPTByProjectSequence(backlogId, ptId, user.getEmail());

        return new ResponseEntity<>(projectTask, HttpStatus.OK);
    }

    @PatchMapping("/{backlogId}/{ptId}")
    @ApiOperation(value = "Update Project Task", notes = "Updates a Project Task", response = ProjectTask.class)
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success", response = ProjectTask.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ValidationErrorExceptionResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = UserNotLoggedInExceptionResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = ProjectNotFoundExceptionResponse.class),
        @ApiResponse(code = 500, message = "Internal server Error", response = CustomInternalServerErrorExceptionResponse.class)
    })
    public ResponseEntity<ProjectTask> updateProjectTask(
            @Valid
            @RequestBody
            @ApiParam(required = true, name = "Project Task", value = "Updated Project Task")
            ProjectTask projectTask,
            BindingResult result,
            @PathVariable
            @ApiParam(required = true, name = "projectIdentifier", value = "ID of the Project containing the updatable Project Task")
            String backlogId,
            @PathVariable
            @ApiParam(required = true, name = "projectSequence", value = "ID of the Project Task to be updated")
            String ptId,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        mapValidationErrorService.mapValidationService(result);

        ProjectTask updatedTask = projectTaskService.updateByProjectSequence(projectTask,backlogId, ptId, user.getEmail());

        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @DeleteMapping("/{backlogId}/{ptId}")
    @ApiOperation(value = "Delete Project Task", notes = "Deleting an existing Project Task")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success"),
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
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        DeleteDTO response = projectTaskService.deletePTByProjectSequence(backlogId, ptId, user.getEmail());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
