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

    @PostMapping("/{backlog_id}")
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
            String backlog_id,
            Authentication authentication) {

        mapValidationErrorService.MapValidationService(result);

        User user = (User) authentication.getPrincipal();

        ProjectTask projectTask1 = projectTaskService.addProjectTask(backlog_id, projectTask, user.getEmail());

        return new ResponseEntity<>(projectTask1, HttpStatus.CREATED);
    }

    @GetMapping("/{backlog_id}")
    @ApiOperation(value = "Get Project Tasks", notes = "Retrieves Project Tasks", response = ProjectTask.class)
    @ApiResponse(code = 200, message = "Success", response = ProjectTask.class)
    public Iterable<ProjectTask> getProjectBacklog(
            @PathVariable
            @ApiParam(required = true, name = "projectIdentifier", value = "ID of the Project")
            String backlog_id,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        return projectTaskService.findBacklogById(backlog_id, user.getEmail());
    }

    @GetMapping("/{backlog_id}/{pt_id}")
    @ApiOperation(value = "Get Project Task", notes = "Retrieves a single Project Task", response = ProjectTask.class)
    @ApiResponse(code = 200, message = "Success", response = ProjectTask.class)
    public ResponseEntity<?> getProjectTask(
            @PathVariable
            @ApiParam(required = true, name = "projectIdentifier", value = "ID of the Project")
            String backlog_id,
            @PathVariable
            @ApiParam(required = true, name = "projectSequence", value = "ID of the Project Task you want to retrieve")
            String pt_id,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        ProjectTask projectTask = projectTaskService.findPTByProjectSequence(backlog_id, pt_id, user.getEmail());

        return new ResponseEntity<>(projectTask, HttpStatus.OK);
    }

    @PatchMapping("/{backlog_id}/{pt_id}")
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
            String backlog_id,
            @PathVariable
            @ApiParam(required = true, name = "projectSequence", value = "ID of the Project Task to be updated")
            String pt_id,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        mapValidationErrorService.MapValidationService(result);

        ProjectTask updatedTask = projectTaskService.updateByProjectSequence(projectTask,backlog_id, pt_id, user.getEmail());

        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @DeleteMapping("/{backlog_id}/{pt_id}")
    @ApiOperation(value = "Delete Project Task", notes = "Deleting an existing Project Task")
    @ApiResponse(code = 200, message = "Success")
    public ResponseEntity<?> deleteProjectTask(
            @PathVariable
            @ApiParam(required = true, name = "projectIdentifier", value = "ID of the Project that contains the Project Task you want to delete")
            String backlog_id,
            @PathVariable
            @ApiParam(required = true, name = "projectSequence", value = "ID of the Project Task you want to delete")
            String pt_id,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        projectTaskService.deletePTByProjectSequence(backlog_id, pt_id, user.getEmail());

        return new ResponseEntity<>("Project Task '" + pt_id + "' was deleted successfully", HttpStatus.OK);
    }
}
