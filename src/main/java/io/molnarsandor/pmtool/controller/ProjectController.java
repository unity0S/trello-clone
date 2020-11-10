package io.molnarsandor.pmtool.controller;

import io.molnarsandor.pmtool.domain.Project;
import io.molnarsandor.pmtool.domain.User;
import io.molnarsandor.pmtool.service.MapValidationErrorService;
import io.molnarsandor.pmtool.service.ProjectService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @PostMapping("/")
    @ApiOperation(value = "Create New Project", notes = "Creates New Project to Logged In User", response = Project.class)
    @ApiResponse(code = 200, message = "Success", response = Project.class)
    public ResponseEntity<?> createNewProject(
            @Valid
            @RequestBody
            @ApiParam(required = true, name = "project", value = "New Project", examples = @Example(value = {
                    @ExampleProperty(value = "{\"when\":\"2010-01-01\",\"where\":\"Auto Repair Shop\"}")
            }))
            Project project,
            BindingResult result,
            Authentication authentication) {

        mapValidationErrorService.mapValidationService(result);

        User user = (User) authentication.getPrincipal();

        Project project1 = projectService.saveOrUpdateProject(project, user.getEmail());
        return new ResponseEntity<>(project1, HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}")
    @ApiOperation(value = "Get Project By ID", notes = "Retrieves a Project by ID", response = Project.class)
    @ApiResponse(code = 200, message = "Success", response = Project.class)
    public ResponseEntity<?> getProjectById(
            @PathVariable
            @ApiParam(required = true, name = "projectIdentifier", value = "ID of the Project you want to retrieve")
            String projectId,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        Project project = projectService.findProjectByIdentifier(projectId, user.getEmail());

        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @GetMapping("/all")
    @ApiOperation(value = "Get All Projects", notes = "Retrieves the Projects of the Logged in User and the Projects where the User is a Collaborator", response = Project.class)
    @ApiResponse(code = 200, message = "Success", response = Project.class)
    public Iterable<Project> getAllProjects(Principal principal) { return projectService.findAllProject(principal); }

    @DeleteMapping("/{projectId}")
    @ApiOperation(value = "Delete Project", notes = "Deletes a Project by ID")
    @ApiResponse(code = 200, message = "Success")
    public ResponseEntity<?> deleteProject(
            @PathVariable
            @ApiParam(required = true, name = "projectIdentifier", value = "ID of the Project you want to Delete")
            String projectId,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        projectService.deleteProjectByIdentifier(projectId, user.getEmail());

        return new ResponseEntity<>("Project with ID: '" + projectId + "' deleted.", HttpStatus.OK);
    }
}
