package io.molnarsandor.trelloclone.project;

import io.molnarsandor.trelloclone.exceptions.CustomInternalServerErrorExceptionResponse;
import io.molnarsandor.trelloclone.exceptions.ValidationErrorExceptionResponse;
import io.molnarsandor.trelloclone.project.exceptions.ProjectIdExceptionResponse;
import io.molnarsandor.trelloclone.project.exceptions.ProjectNotFoundExceptionResponse;
import io.molnarsandor.trelloclone.user.UserEntity;
import io.molnarsandor.trelloclone.user.exceptions.UserNotLoggedInExceptionResponse;
import io.molnarsandor.trelloclone.util.DeleteDTO;
import io.molnarsandor.trelloclone.util.MapValidationErrorService;
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
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {

    private final ProjectService projectService;

    private final MapValidationErrorService mapValidationErrorService;

    private final ModelConverter modelConverter;

    @PostMapping("/")
    @ApiOperation(value = "Create New Project", notes = "Creates New Project to Logged In User", response = ProjectDTO.class)
    @ApiResponses({
        @ApiResponse(code = 201, message = "Created", response = ProjectDTO.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ValidationErrorExceptionResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = UserNotLoggedInExceptionResponse.class),
        @ApiResponse(code = 409, message = "Conflict", response = ProjectIdExceptionResponse.class),
        @ApiResponse(code = 500, message = "Internal server Error", response = CustomInternalServerErrorExceptionResponse.class)
    })
    public ResponseEntity<ProjectDTO> createNewProject(
            @Valid
            @RequestBody
            @ApiParam(required = true, name = "project", value = "New Project")
            ProjectDTO projectDTO,
            @ApiIgnore
            BindingResult result,
            @ApiIgnore
            Authentication authentication) {

        mapValidationErrorService.mapValidationService(result);

        ProjectEntity projectEntity = modelConverter.projectDtoToEntity(projectDTO);

        UserEntity userEntity = (UserEntity) authentication.getPrincipal();

        ProjectDTO savedProject = modelConverter.projectEntityToDto(
                projectService.saveOrUpdateProject(projectEntity, userEntity.getEmail()));

        return new ResponseEntity<>(savedProject, HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}")
    @ApiOperation(value = "Get Project By ID", notes = "Retrieves a Project by ID", response = ProjectDTO.class)
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success", response = ProjectDTO.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ValidationErrorExceptionResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = UserNotLoggedInExceptionResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = ProjectNotFoundExceptionResponse.class),
        @ApiResponse(code = 500, message = "Internal server Error", response = CustomInternalServerErrorExceptionResponse.class)
    })
    public ResponseEntity<ProjectDTO> getProjectById(
            @PathVariable
            @ApiParam(required = true, name = "projectIdentifier", value = "ID of the Project you want to retrieve")
            String projectId,
            @ApiIgnore
            Authentication authentication) {

        UserEntity userEntity = (UserEntity) authentication.getPrincipal();

        ProjectDTO project = modelConverter.projectEntityToDto(
                projectService.findProjectByIdentifier(projectId, userEntity.getEmail()));

        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @GetMapping("/all")
    @ApiOperation(value = "Get All Projects", notes = "Retrieves the Projects of the Logged in User and the Projects where the User is a Collaborator", response = ProjectDTO.class)
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success", response = ProjectDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = UserNotLoggedInExceptionResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = ProjectNotFoundExceptionResponse.class),
        @ApiResponse(code = 500, message = "Internal server Error", response = CustomInternalServerErrorExceptionResponse.class)
    })
    public ResponseEntity<List<ProjectDTO>> getAllProjects(Authentication authentication) {

        UserEntity userEntity = (UserEntity) authentication.getPrincipal();

        List<ProjectDTO> projects = modelConverter.projectEntityListToDto(
                projectService.findAllProject(userEntity.getEmail()));

        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @DeleteMapping("/{projectId}")
    @ApiOperation(value = "Delete Project", notes = "Deletes a Project by ID")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success", response = DeleteDTO.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ValidationErrorExceptionResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = UserNotLoggedInExceptionResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = ProjectNotFoundExceptionResponse.class),
        @ApiResponse(code = 500, message = "Internal server Error", response = CustomInternalServerErrorExceptionResponse.class)
    })
    public ResponseEntity<DeleteDTO> deleteProject(
            @PathVariable
            @ApiParam(required = true, name = "projectIdentifier", value = "ID of the Project you want to Delete")
            String projectId,
            @ApiIgnore
            Authentication authentication) {

        UserEntity userEntity = (UserEntity) authentication.getPrincipal();

        DeleteDTO response = projectService.deleteProjectByIdentifier(projectId, userEntity.getEmail());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
