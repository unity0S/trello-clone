package io.molnarsandor.pmtool.controller;

import com.google.gson.JsonObject;
import io.molnarsandor.pmtool.domain.Project;
import io.molnarsandor.pmtool.domain.User;
import io.molnarsandor.pmtool.dto.DeleteDTO;
import io.molnarsandor.pmtool.exceptions.*;
import io.molnarsandor.pmtool.service.MapValidationErrorService;
import io.molnarsandor.pmtool.service.ProjectService;
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
import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {

    private final ProjectService projectService;

    private final MapValidationErrorService mapValidationErrorService;

    @PostMapping("/")
    @ApiOperation(value = "Create New Project", notes = "Creates New Project to Logged In User", response = Project.class)
    @ApiResponses({
        @ApiResponse(code = 201, message = "Created", response = Project.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ValidationErrorExceptionResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = UserNotLoggedInExceptionResponse.class),
        @ApiResponse(code = 409, message = "Conflict", response = ProjectIdExceptionResponse.class),
        @ApiResponse(code = 500, message = "Internal server Error", response = CustomInternalServerErrorExceptionResponse.class)
    })
    public ResponseEntity<Project> createNewProject(
            @Valid
            @RequestBody
            @ApiParam(required = true, name = "project", value = "New Project")
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
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success", response = Project.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ValidationErrorExceptionResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = UserNotLoggedInExceptionResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = ProjectNotFoundExceptionResponse.class),
        @ApiResponse(code = 500, message = "Internal server Error", response = CustomInternalServerErrorExceptionResponse.class)
    })
    public ResponseEntity<Project> getProjectById(
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
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success", response = Project.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = UserNotLoggedInExceptionResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = ProjectNotFoundExceptionResponse.class),
        @ApiResponse(code = 500, message = "Internal server Error", response = CustomInternalServerErrorExceptionResponse.class)
    })
    public Iterable<Project> getAllProjects(Principal principal) {
        return projectService.findAllProject(principal);
    }

    @DeleteMapping("/{projectId}")
    @ApiOperation(value = "Delete Project", notes = "Deletes a Project by ID")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success", response = JsonObject.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ValidationErrorExceptionResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = UserNotLoggedInExceptionResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = ProjectNotFoundExceptionResponse.class),
        @ApiResponse(code = 500, message = "Internal server Error", response = CustomInternalServerErrorExceptionResponse.class)
    })
    public ResponseEntity<DeleteDTO> deleteProject(
            @PathVariable
            @ApiParam(required = true, name = "projectIdentifier", value = "ID of the Project you want to Delete")
            String projectId,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        DeleteDTO response = projectService.deleteProjectByIdentifier(projectId, user.getEmail());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
