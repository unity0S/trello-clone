package io.molnarsandor.trelloclone.project.controller;

import io.molnarsandor.trelloclone.project.model.ProjectDTO;
import io.molnarsandor.trelloclone.project.model.ProjectEntity;
import io.molnarsandor.trelloclone.project.ProjectService;
import io.molnarsandor.trelloclone.user.model.UserEntity;
import io.molnarsandor.trelloclone.util.DeleteDTO;
import io.molnarsandor.trelloclone.util.MapValidationErrorService;
import io.molnarsandor.trelloclone.util.ModelConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectControllerImpl implements ProjectController {

    private final ProjectService projectService;

    private final MapValidationErrorService mapValidationErrorService;

    private final ModelConverter modelConverter;

    @Override
    @PostMapping("/")
    public ResponseEntity<ProjectDTO> createNewProject(@Valid
                                                       @RequestBody
                                                       ProjectDTO projectDTO,
                                                       BindingResult result,
                                                       Authentication authentication) {

        mapValidationErrorService.mapValidationService(result);
        ProjectEntity projectEntity = modelConverter.projectDtoToEntity(projectDTO);
        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        ProjectDTO savedProject = modelConverter.projectEntityToDto(
                projectService.saveOrUpdateProject(projectEntity, userEntity.getEmail()));

        return new ResponseEntity<>(savedProject, HttpStatus.CREATED);
    }

    @Override
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable
                                                     String projectId,
                                                     Authentication authentication) {

        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        ProjectDTO project = modelConverter.projectEntityToDto(
                projectService.findProjectByIdentifier(projectId, userEntity.getEmail()));

        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<List<ProjectDTO>> getAllProjects(Authentication authentication) {

        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        List<ProjectDTO> projects = modelConverter.projectEntityListToDto(
                projectService.findAllProject(userEntity.getEmail()));

        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{projectId}")
    public ResponseEntity<DeleteDTO> deleteProject(@PathVariable
                                                   String projectId,
                                                   Authentication authentication) {

        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        DeleteDTO response = projectService.deleteProjectByIdentifier(projectId, userEntity.getEmail());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
