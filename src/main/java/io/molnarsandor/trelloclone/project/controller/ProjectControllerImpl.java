package io.molnarsandor.trelloclone.project.controller;

import io.molnarsandor.trelloclone.project.ProjectService;
import io.molnarsandor.trelloclone.project.model.ProjectDTO;
import io.molnarsandor.trelloclone.util.DeleteDTO;
import io.molnarsandor.trelloclone.util.MapValidationErrorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectControllerImpl implements ProjectController {

    private final ProjectService projectService;

    private final MapValidationErrorService mapValidationErrorService;

    @Override
    @PostMapping("/")
    public ResponseEntity<ProjectDTO> createNewProject(@Valid
                                                       @RequestBody
                                                       ProjectDTO projectDTO,
                                                       BindingResult result,
                                                       Principal principal) {

        mapValidationErrorService.mapValidationService(result);
        ProjectDTO savedProject = projectService.saveOrUpdateProject(projectDTO, principal.getName());

        return new ResponseEntity<>(savedProject, HttpStatus.CREATED);
    }

    @Override
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable
                                                     String projectId,
                                                     Principal principal) {

        ProjectDTO project = projectService.findProjectByIdentifierDTO(projectId, principal.getName());

        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<List<ProjectDTO>> getAllProjects(Principal principal) {

        List<ProjectDTO> projects = projectService.findAllProject(principal.getName());

        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{projectId}")
    public ResponseEntity<DeleteDTO> deleteProject(@PathVariable
                                                   String projectId,
                                                   Principal principal) {

        DeleteDTO response = projectService.deleteProjectByIdentifier(projectId, principal.getName());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
