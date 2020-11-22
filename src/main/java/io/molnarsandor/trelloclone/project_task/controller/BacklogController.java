package io.molnarsandor.trelloclone.project_task.controller;

import io.molnarsandor.trelloclone.global_exceptions.CustomGlobalExceptionResponse;
import io.molnarsandor.trelloclone.global_exceptions.ValidationErrorExceptionResponse;
import io.molnarsandor.trelloclone.project.exceptions.ProjectNotFoundExceptionResponse;
import io.molnarsandor.trelloclone.project_task.model.ProjectTaskDTO;
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

public interface BacklogController {

    @ApiOperation(value = "Add Project Task to Project", notes = "Add New Project Task to Project", response = ProjectTaskDTO.class)
    @ApiResponses({
        @ApiResponse(code = 201, message = "Created", response = ProjectTaskDTO.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ValidationErrorExceptionResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = UserNotLoggedInExceptionResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = ProjectNotFoundExceptionResponse.class),
        @ApiResponse(code = 500, message = "Internal server Error", response = CustomGlobalExceptionResponse.class)})
    ResponseEntity<ProjectTaskDTO> addPTtoBacklog(@ApiParam(required = true, name = "projectTask", value = "New Project Task")
                                                  ProjectTaskDTO projectTaskDTO,
                                                  @ApiIgnore
                                                  BindingResult result,
                                                  @ApiParam(required = true, name = "projectIdentifier", value = "ID of the Project where you want to add a Project Task")
                                                  String backlogId,
                                                  @ApiIgnore
                                                  Principal principal);

    @ApiOperation(value = "Get Project Tasks", notes = "Retrieves List of Project Tasks", response = ProjectTaskDTO.class)
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success", response = ProjectTaskDTO.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ValidationErrorExceptionResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = UserNotLoggedInExceptionResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = ProjectNotFoundExceptionResponse.class),
        @ApiResponse(code = 500, message = "Internal server Error", response = CustomGlobalExceptionResponse.class)})
    ResponseEntity<List<ProjectTaskDTO>> getProjectBacklog(@ApiParam(required = true, name = "projectIdentifier", value = "ID of the Project")
                                                           String backlogId,
                                                           @ApiIgnore
                                                           Principal principal);

    @ApiOperation(value = "Get Project Task", notes = "Retrieves a single Project Task", response = ProjectTaskDTO.class)
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success", response = ProjectTaskDTO.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ValidationErrorExceptionResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = UserNotLoggedInExceptionResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = ProjectNotFoundExceptionResponse.class),
        @ApiResponse(code = 500, message = "Internal server Error", response = CustomGlobalExceptionResponse.class)})
    ResponseEntity<ProjectTaskDTO> getProjectTask(@ApiParam(required = true, name = "projectIdentifier", value = "ID of the Project")
                                                  String backlogId,
                                                  @ApiParam(required = true, name = "projectSequence", value = "ID of the Project Task you want to retrieve")
                                                  String ptId,
                                                  @ApiIgnore
                                                  Principal principal);

    @ApiOperation(value = "Update Project Task", notes = "Updates a Project Task", response = ProjectTaskDTO.class)
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success", response = ProjectTaskDTO.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ValidationErrorExceptionResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = UserNotLoggedInExceptionResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = ProjectNotFoundExceptionResponse.class),
        @ApiResponse(code = 500, message = "Internal server Error", response = CustomGlobalExceptionResponse.class)})
    ResponseEntity<ProjectTaskDTO> updateProjectTask(@ApiParam(required = true, name = "Project Task", value = "Updated Project Task")
                                                     ProjectTaskDTO projectTaskDTO,
                                                     @ApiIgnore
                                                     BindingResult result,
                                                     @ApiParam(required = true, name = "projectIdentifier", value = "ID of the Project containing the updatable Project Task")
                                                     String backlogId,
                                                     @ApiParam(required = true, name = "projectSequence", value = "ID of the Project Task to be updated")
                                                     String ptId,
                                                     @ApiIgnore
                                                     Principal principal);

    @ApiOperation(value = "Delete Project Task", notes = "Deleting an existing Project Task", response = DeleteDTO.class)
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success", response = DeleteDTO.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ValidationErrorExceptionResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = UserNotLoggedInExceptionResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = ProjectNotFoundExceptionResponse.class),
        @ApiResponse(code = 500, message = "Internal server Error", response = CustomGlobalExceptionResponse.class)})
    ResponseEntity<DeleteDTO> deleteProjectTask(@ApiParam(required = true, name = "projectIdentifier", value = "ID of the Project that contains the Project Task you want to delete")
                                                String backlogId,
                                                @ApiParam(required = true, name = "projectSequence", value = "ID of the Project Task you want to delete")
                                                String ptId,
                                                @ApiIgnore
                                                Principal principal);
}
