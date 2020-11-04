package io.molnarsandor.pmtool.controller;

import io.molnarsandor.pmtool.domain.Collaborator;
import io.molnarsandor.pmtool.service.CollaboratorService;
import io.molnarsandor.pmtool.service.EmailService;
import io.molnarsandor.pmtool.service.MapValidationErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

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
    public ResponseEntity<?> addCollaboratorToProject(@Valid @RequestBody Collaborator collaborator,
                                                                  BindingResult result, @PathVariable String projectIdentifier, Principal principal) {

        mapValidationErrorService.MapValidationService(result);

        Collaborator collaborator1 = collaboratorService.addCollaborator(projectIdentifier, collaborator, principal.getName());

        // TODO projectIdentifier to project name or desc or smthng
        emailService.sendMessage(collaborator.getEmail(), "Invite", "You have been invited to collaborate in project: " + projectIdentifier);

        return new ResponseEntity<>(collaborator1, HttpStatus.CREATED);
    }

    @DeleteMapping("/{projectIdentifier}/{collaboratorSequence}")
    public ResponseEntity<?> deleteCollaborator(@PathVariable String projectIdentifier, @PathVariable String collaboratorSequence, Principal principal) {

        collaboratorService.deleteCollaborator(projectIdentifier.toUpperCase(), collaboratorSequence, principal.getName());

        return new ResponseEntity<>("Collaborator '" + collaboratorSequence + "' was deleted successfully from project: '" + projectIdentifier + "'", HttpStatus.OK);
    }
}
