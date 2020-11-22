package io.molnarsandor.trelloclone.collaborator.controller;

import io.molnarsandor.trelloclone.collaborator.CollaboratorService;
import io.molnarsandor.trelloclone.collaborator.model.CollaboratorDTO;
import io.molnarsandor.trelloclone.util.DeleteDTO;
import io.molnarsandor.trelloclone.util.EmailService;
import io.molnarsandor.trelloclone.util.MapValidationErrorService;
import io.molnarsandor.trelloclone.util.ModelConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/collaborator")
@CrossOrigin
public class CollaboratorControllerImpl implements CollaboratorController {

    private final CollaboratorService collaboratorService;

    private final MapValidationErrorService mapValidationErrorService;

    private final EmailService emailService;

    @Override
    @PostMapping("/{projectIdentifier}")
    public ResponseEntity<CollaboratorDTO> addCollaboratorToProject(@Valid
                                                                    @RequestBody
                                                                    CollaboratorDTO collaboratorDTO,
                                                                    BindingResult result,
                                                                    @PathVariable
                                                                    String projectIdentifier,
                                                                    Principal principal) {

        mapValidationErrorService.mapValidationService(result);
        CollaboratorDTO savedCollaborator = collaboratorService.addCollaborator(projectIdentifier, collaboratorDTO, principal.getName());

        emailService.sendMessage(savedCollaborator.getEmail(), "Invite", "You have been invited to collaborate in project: " + projectIdentifier);

        return new ResponseEntity<>(savedCollaborator, HttpStatus.CREATED);
    }

    @Override
    @DeleteMapping("/{projectIdentifier}/{collaboratorSequence}")
    public ResponseEntity<DeleteDTO> deleteCollaborator(@PathVariable
                                                        String projectIdentifier,
                                                        @PathVariable
                                                        String collaboratorSequence,
                                                        Principal principal) {

        DeleteDTO response = collaboratorService.deleteCollaborator(projectIdentifier.toUpperCase(), collaboratorSequence, principal.getName());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
