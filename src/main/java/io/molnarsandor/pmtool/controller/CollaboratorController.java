package io.molnarsandor.pmtool.controller;

import io.molnarsandor.pmtool.domain.Collaborator;
import io.molnarsandor.pmtool.domain.User;
import io.molnarsandor.pmtool.service.CollaboratorService;
import io.molnarsandor.pmtool.service.EmailService;
import io.molnarsandor.pmtool.service.MapValidationErrorService;
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
@RequestMapping("/api/collaborator")
@CrossOrigin
public class CollaboratorController {

    @Autowired
    private CollaboratorService collaboratorService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/{projectIdentifier}")
    @ApiOperation(value = "Add Collaborator to Project", notes = "Add New Collaborator to Project", response = Collaborator.class)
    @ApiResponse(code = 200, message = "Success", response = Collaborator.class)
    public ResponseEntity<?> addCollaboratorToProject(
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
    @ApiResponse(code = 200, message = "Success")
    public ResponseEntity<?> deleteCollaborator(
            @PathVariable
            @ApiParam(required = true, name = "projectIdentifier", value = "ID of the Project where you want to Delete the Collaborator")
            String projectIdentifier,
            @PathVariable
            @ApiParam(required = true, name = "collaboratorSequence", value = "ID of the Collaborator which you want to Delete")
            String collaboratorSequence,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        collaboratorService.deleteCollaborator(projectIdentifier.toUpperCase(), collaboratorSequence, user.getEmail());

        return new ResponseEntity<>("Collaborator '" + collaboratorSequence + "' was deleted successfully from project: '" + projectIdentifier + "'", HttpStatus.OK);
    }
}
