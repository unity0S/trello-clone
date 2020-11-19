package io.molnarsandor.trelloclone.collaborator.controller;

import io.molnarsandor.trelloclone.collaborator.model.CollaboratorDTO;
import io.molnarsandor.trelloclone.collaborator.model.CollaboratorEntity;
import io.molnarsandor.trelloclone.collaborator.CollaboratorService;
import io.molnarsandor.trelloclone.user.model.UserEntity;
import io.molnarsandor.trelloclone.util.DeleteDTO;
import io.molnarsandor.trelloclone.util.EmailService;
import io.molnarsandor.trelloclone.util.MapValidationErrorService;
import io.molnarsandor.trelloclone.util.ModelConverter;
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
public class CollaboratorControllerImpl implements CollaboratorController {

    private final CollaboratorService collaboratorService;

    private final MapValidationErrorService mapValidationErrorService;

    private final EmailService emailService;

    private final ModelConverter modelConverter;

    @Override
    @PostMapping("/{projectIdentifier}")
    public ResponseEntity<CollaboratorDTO> addCollaboratorToProject(@Valid
                                                                    @RequestBody
                                                                    CollaboratorDTO collaboratorDTO,
                                                                    BindingResult result,
                                                                    @PathVariable
                                                                    String projectIdentifier,
                                                                    Authentication authentication) {

        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        mapValidationErrorService.mapValidationService(result);
        CollaboratorEntity collaboratorEntity = modelConverter.collaboratorDtoToEntity(collaboratorDTO);
        CollaboratorDTO savedCollaborator = modelConverter.collaboratorEntityToDto(
                collaboratorService.addCollaborator(projectIdentifier, collaboratorEntity, userEntity.getEmail()));

        emailService.sendMessage(savedCollaborator.getEmail(), "Invite", "You have been invited to collaborate in project: " + projectIdentifier);

        return new ResponseEntity<>(savedCollaborator, HttpStatus.CREATED);
    }

    @Override
    @DeleteMapping("/{projectIdentifier}/{collaboratorSequence}")
    public ResponseEntity<DeleteDTO> deleteCollaborator(@PathVariable
                                                        String projectIdentifier,
                                                        @PathVariable
                                                        String collaboratorSequence,
                                                        Authentication authentication) {

        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        DeleteDTO response = collaboratorService.deleteCollaborator(projectIdentifier.toUpperCase(), collaboratorSequence, userEntity.getEmail());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
