package io.molnarsandor.trelloclone.collaborator.controller;

import io.molnarsandor.trelloclone.collaborator.exceptions.CollaboratorAlreadyAssignedExceptionResponse;
import io.molnarsandor.trelloclone.collaborator.model.CollaboratorDTO;
import io.molnarsandor.trelloclone.global_exceptions.CustomGlobalExceptionResponse;
import io.molnarsandor.trelloclone.global_exceptions.ValidationErrorExceptionResponse;
import io.molnarsandor.trelloclone.project.exceptions.ProjectNotFoundExceptionResponse;
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

public interface CollaboratorController {

    @ApiOperation(value = "Add Collaborator to Project", notes = "Add New Collaborator to Project", response = CollaboratorDTO.class)
    @ApiResponses({
        @ApiResponse(code = 201, message = "Created", response = CollaboratorDTO.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ValidationErrorExceptionResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = UserNotLoggedInExceptionResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = ProjectNotFoundExceptionResponse.class),
        @ApiResponse(code = 409, message = "Conflict", response = CollaboratorAlreadyAssignedExceptionResponse.class),
        @ApiResponse(code = 500, message = "Internal server Error", response = CustomGlobalExceptionResponse.class)})
    ResponseEntity<CollaboratorDTO> addCollaboratorToProject(@ApiParam(required = true, name = "collaborator", value = "New Collaborator to be added to the Project")
                                                             CollaboratorDTO collaboratorDTO,
                                                             @ApiIgnore
                                                             BindingResult result,
                                                             @ApiParam(required = true, name = "projectIdentifier", value = "ID of the Project where you want to add the Collaborator")
                                                             String projectIdentifier,
                                                             @ApiIgnore
                                                             Principal principal);

    @ApiOperation(value = "Delete Collaborator from Project", notes = "Delete existing Collaborator from Project")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success", response = DeleteDTO.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ValidationErrorExceptionResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = UserNotLoggedInExceptionResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = ProjectNotFoundExceptionResponse.class),
        @ApiResponse(code = 409, message = "Conflict", response = CollaboratorAlreadyAssignedExceptionResponse.class),
        @ApiResponse(code = 500, message = "Internal server Error", response = CustomGlobalExceptionResponse.class)})
    ResponseEntity<DeleteDTO> deleteCollaborator(@ApiParam(required = true, name = "projectIdentifier", value = "ID of the Project where you want to Delete the Collaborator")
                                                 String projectIdentifier,
                                                 @ApiParam(required = true, name = "collaboratorSequence", value = "ID of the Collaborator which you want to Delete")
                                                 String collaboratorSequence,
                                                 @ApiIgnore
                                                 Principal principal);
}
