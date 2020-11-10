package io.molnarsandor.pmtool.controller;

import io.molnarsandor.pmtool.domain.Project;
import io.molnarsandor.pmtool.domain.ProjectTask;
import io.molnarsandor.pmtool.domain.User;
import io.molnarsandor.pmtool.service.MapValidationErrorService;
import io.molnarsandor.pmtool.service.ProjectTaskService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {

    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @PostMapping("/{backlogId}")
    @ApiOperation(value = "Add Project Task to Project", notes = "Add New Project Task to Project", response = Project.class)
    @ApiResponse(code = 200, message = "Success", response = Project.class)
    public ResponseEntity<?> addPTtoBacklog(
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
    @ApiResponse(code = 200, message = "Success", response = ProjectTask.class)
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
    @ApiResponse(code = 200, message = "Success", response = ProjectTask.class)
    public ResponseEntity<?> getProjectTask(
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
    @ApiResponse(code = 200, message = "Success", response = ProjectTask.class)
    public ResponseEntity<?> updateProjectTask(
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
    @ApiResponse(code = 200, message = "Success")
    public ResponseEntity<?> deleteProjectTask(
            @PathVariable
            @ApiParam(required = true, name = "projectIdentifier", value = "ID of the Project that contains the Project Task you want to delete")
            String backlogId,
            @PathVariable
            @ApiParam(required = true, name = "projectSequence", value = "ID of the Project Task you want to delete")
            String ptId,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        projectTaskService.deletePTByProjectSequence(backlogId, ptId, user.getEmail());

        return new ResponseEntity<>("Project Task '" + ptId + "' was deleted successfully", HttpStatus.OK);
    }
}
