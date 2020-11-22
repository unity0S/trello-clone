package io.molnarsandor.trelloclone.project.controller;

import io.molnarsandor.trelloclone.global_exceptions.CustomGlobalExceptionResponse;
import io.molnarsandor.trelloclone.global_exceptions.ValidationErrorExceptionResponse;
import io.molnarsandor.trelloclone.project.exceptions.ProjectIdExceptionResponse;
import io.molnarsandor.trelloclone.project.exceptions.ProjectNotFoundExceptionResponse;
import io.molnarsandor.trelloclone.project.model.ProjectDTO;
import io.molnarsandor.trelloclone.user.exceptions.UserNotLoggedInExceptionResponse;
import io.molnarsandor.trelloclone.util.DeleteDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.List;

public interface ProjectController {

    @ApiOperation(value = "Create New Project", notes = "Creates New Project to Logged In User", response = ProjectDTO.class)
    @ApiResponses({
        @ApiResponse(code = 201, message = "Created", response = ProjectDTO.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ValidationErrorExceptionResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = UserNotLoggedInExceptionResponse.class),
        @ApiResponse(code = 409, message = "Conflict", response = ProjectIdExceptionResponse.class),
        @ApiResponse(code = 500, message = "Internal server Error", response = CustomGlobalExceptionResponse.class)})
    ResponseEntity<ProjectDTO> createNewProject(@ApiParam(required = true, name = "project", value = "New Project")
                                                ProjectDTO projectDTO,
                                                @ApiIgnore
                                                BindingResult result,
                                                @ApiIgnore
                                                Principal principal);

    @ApiOperation(value = "Get Project By ID", notes = "Retrieves a Project by ID", response = ProjectDTO.class)
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success", response = ProjectDTO.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ValidationErrorExceptionResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = UserNotLoggedInExceptionResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = ProjectNotFoundExceptionResponse.class),
        @ApiResponse(code = 500, message = "Internal server Error", response = CustomGlobalExceptionResponse.class)})
    ResponseEntity<ProjectDTO> getProjectById(@ApiParam(required = true, name = "projectIdentifier", value = "ID of the Project you want to retrieve")
                                              String projectId,
                                              @ApiIgnore
                                              Principal principal);

    @ApiOperation(value = "Get All Projects", notes = "Retrieves the Projects of the Logged in User and the Projects where the User is a Collaborator", response = ProjectDTO.class)
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success", response = ProjectDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = UserNotLoggedInExceptionResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = ProjectNotFoundExceptionResponse.class),
        @ApiResponse(code = 500, message = "Internal server Error", response = CustomGlobalExceptionResponse.class)})
    ResponseEntity<List<ProjectDTO>> getAllProjects(@ApiIgnore Principal principal);

    @ApiOperation(value = "Delete Project", notes = "Deletes a Project by ID")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success", response = DeleteDTO.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ValidationErrorExceptionResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = UserNotLoggedInExceptionResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = ProjectNotFoundExceptionResponse.class),
        @ApiResponse(code = 500, message = "Internal server Error", response = CustomGlobalExceptionResponse.class)
    })
    ResponseEntity<DeleteDTO> deleteProject(@ApiParam(required = true, name = "projectIdentifier", value = "ID of the Project you want to Delete")
                                            String projectId,
                                            @ApiIgnore
                                            Principal principal);
}
