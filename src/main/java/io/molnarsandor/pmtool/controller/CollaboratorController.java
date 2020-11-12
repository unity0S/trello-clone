package io.molnarsandor.pmtool.controller;

import com.google.gson.JsonObject;
import io.molnarsandor.pmtool.domain.Collaborator;
import io.molnarsandor.pmtool.domain.User;
import io.molnarsandor.pmtool.dto.DeleteDTO;
import io.molnarsandor.pmtool.exceptions.*;
import io.molnarsandor.pmtool.service.CollaboratorService;
import io.molnarsandor.pmtool.service.EmailService;
import io.molnarsandor.pmtool.service.MapValidationErrorService;
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
@RequestMapping("/api/collaborator")
@CrossOrigin
public class CollaboratorController {

    private final CollaboratorService collaboratorService;

    private final MapValidationErrorService mapValidationErrorService;

    private final EmailService emailService;

    @PostMapping("/{projectIdentifier}")
    @ApiOperation(value = "Add Collaborator to Project", notes = "Add New Collaborator to Project", response = Collaborator.class)
    @ApiResponses({
        @ApiResponse(code = 201, message = "Created", response = Collaborator.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ValidationErrorExceptionResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = UserNotLoggedInExceptionResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = ProjectNotFoundExceptionResponse.class),
        @ApiResponse(code = 409, message = "Conflict", response = CollaboratorAlreadyAssignedExceptionResponse.class),
        @ApiResponse(code = 500, message = "Internal server Error", response = CustomInternalServerErrorExceptionResponse.class)
    })
    public ResponseEntity<Collaborator> addCollaboratorToProject(
            @Valid
            @RequestBody
            @ApiParam(required = true, name = "collaborator", value = "New Collaborator to be added to the Project")
            Collaborator collaborator,
            BindingResult result,
            @PathVariable
            @ApiParam(required = true, name = "projectIdentifier", value = "ID of the Project where you want to add the Collaborator")
            String projectIdentifier,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        mapValidationErrorService.mapValidationService(result);

        Collaborator collaborator1 = collaboratorService.addCollaborator(projectIdentifier, collaborator, user.getEmail());

        // TODO projectIdentifier to project name or desc or smthng
        emailService.sendMessage(collaborator1.getEmail(), "Invite", "You have been invited to collaborate in project: " + projectIdentifier);

        return new ResponseEntity<>(collaborator1, HttpStatus.CREATED);
    }

    @DeleteMapping("/{projectIdentifier}/{collaboratorSequence}")
    @ApiOperation(value = "Delete Collaborator from Project", notes = "Delete existing Collaborator from Project")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success", response = JsonObject.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ValidationErrorExceptionResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = UserNotLoggedInExceptionResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = ProjectNotFoundExceptionResponse.class),
        @ApiResponse(code = 409, message = "Conflict", response = CollaboratorAlreadyAssignedExceptionResponse.class),
        @ApiResponse(code = 500, message = "Internal server Error", response = CustomInternalServerErrorExceptionResponse.class)
    })
    public ResponseEntity<DeleteDTO> deleteCollaborator(
            @PathVariable
            @ApiParam(required = true, name = "projectIdentifier", value = "ID of the Project where you want to Delete the Collaborator")
            String projectIdentifier,
            @PathVariable
            @ApiParam(required = true, name = "collaboratorSequence", value = "ID of the Collaborator which you want to Delete")
            String collaboratorSequence,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        DeleteDTO response = collaboratorService.deleteCollaborator(projectIdentifier.toUpperCase(), collaboratorSequence, user.getEmail());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
