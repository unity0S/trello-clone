package io.molnarsandor.trelloclone.collaborator;

import io.molnarsandor.trelloclone.util.DeleteDTO;
import io.molnarsandor.trelloclone.collaborator.exceptions.CollaboratorAlreadyAssignedExceptionResponse;
import io.molnarsandor.trelloclone.global_exceptions.CustomInternalServerErrorExceptionResponse;
import io.molnarsandor.trelloclone.global_exceptions.ValidationErrorExceptionResponse;
import io.molnarsandor.trelloclone.project.exceptions.ProjectNotFoundExceptionResponse;
import io.molnarsandor.trelloclone.util.EmailService;
import io.molnarsandor.trelloclone.util.MapValidationErrorService;
import io.molnarsandor.trelloclone.user.UserEntity;
import io.molnarsandor.trelloclone.user.exceptions.UserNotLoggedInExceptionResponse;
import io.molnarsandor.trelloclone.util.ModelConverter;
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
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/collaborator")
@CrossOrigin
public class CollaboratorController {

    private final CollaboratorService collaboratorService;

    private final MapValidationErrorService mapValidationErrorService;

    private final EmailService emailService;

    private final ModelConverter modelConverter;

    @PostMapping("/{projectIdentifier}")
    @ApiOperation(value = "Add Collaborator to Project", notes = "Add New Collaborator to Project", response = CollaboratorDTO.class)
    @ApiResponses({
        @ApiResponse(code = 201, message = "Created", response = CollaboratorDTO.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ValidationErrorExceptionResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = UserNotLoggedInExceptionResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = ProjectNotFoundExceptionResponse.class),
        @ApiResponse(code = 409, message = "Conflict", response = CollaboratorAlreadyAssignedExceptionResponse.class),
        @ApiResponse(code = 500, message = "Internal server Error", response = CustomInternalServerErrorExceptionResponse.class)
    })
    public ResponseEntity<CollaboratorDTO> addCollaboratorToProject(
            @Valid
            @RequestBody
            @ApiParam(required = true, name = "collaborator", value = "New Collaborator to be added to the Project")
            CollaboratorDTO collaboratorDTO,
            @ApiIgnore
            BindingResult result,
            @PathVariable
            @ApiParam(required = true, name = "projectIdentifier", value = "ID of the Project where you want to add the Collaborator")
            String projectIdentifier,
            @ApiIgnore
            Authentication authentication) {

        UserEntity userEntity = (UserEntity) authentication.getPrincipal();

        mapValidationErrorService.mapValidationService(result);

        CollaboratorEntity collaboratorEntity = modelConverter.collaboratorDtoToEntity(collaboratorDTO);

        CollaboratorDTO savedCollaborator = modelConverter.collaboratorEntityToDto(
                collaboratorService.addCollaborator(projectIdentifier, collaboratorEntity, userEntity.getEmail()));

        emailService.sendMessage(savedCollaborator.getEmail(), "Invite", "You have been invited to collaborate in project: " + projectIdentifier);

        return new ResponseEntity<>(savedCollaborator, HttpStatus.CREATED);
    }

    @DeleteMapping("/{projectIdentifier}/{collaboratorSequence}")
    @ApiOperation(value = "Delete Collaborator from Project", notes = "Delete existing Collaborator from Project")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success", response = DeleteDTO.class),
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
            @ApiIgnore
            Authentication authentication) {

        UserEntity userEntity = (UserEntity) authentication.getPrincipal();

        DeleteDTO response = collaboratorService.deleteCollaborator(projectIdentifier.toUpperCase(), collaboratorSequence, userEntity.getEmail());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
