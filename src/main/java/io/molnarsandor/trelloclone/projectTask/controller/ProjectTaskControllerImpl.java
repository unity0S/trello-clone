package io.molnarsandor.trelloclone.projectTask.controller;

import io.molnarsandor.trelloclone.projectTask.ProjectTaskService;
import io.molnarsandor.trelloclone.projectTask.model.ProjectTaskDTO;
import io.molnarsandor.trelloclone.util.DeleteDTO;
import io.molnarsandor.trelloclone.util.MapValidationErrorService;
import io.molnarsandor.trelloclone.util.Paths;
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
@RequestMapping(Paths.ProjectTask.PATH)
@CrossOrigin
public class ProjectTaskControllerImpl implements ProjectTaskController {

    private final ProjectTaskService projectTaskService;

    private final MapValidationErrorService mapValidationErrorService;

    @Override
    @PostMapping(Paths.ProjectTask.AddProjectTaskToProject.PATH)
    public ResponseEntity<ProjectTaskDTO> addProjectTaskToProject(@Valid
                                                                  @RequestBody
                                                                  ProjectTaskDTO projectTaskDTO,
                                                                  BindingResult result,
                                                                  @PathVariable
                                                                  String projectId,
                                                                  Principal principal) {

        ProjectTaskDTO savedProjectTask = projectTaskService.addProjectTask(projectId, projectTaskDTO, principal.getName());

        return new ResponseEntity<>(savedProjectTask, HttpStatus.CREATED);
    }

    @Override
    @GetMapping(Paths.ProjectTask.GetProjectTasks.PATH)
    public ResponseEntity<List<ProjectTaskDTO>> getProjectTasks(@PathVariable
                                                                String projectId,
                                                                Principal principal) {

        List<ProjectTaskDTO> projectTasks = projectTaskService.findProjectTasksByProjectId(projectId, principal.getName());

        return new ResponseEntity<>(projectTasks, HttpStatus.OK);
    }

    @Override
    @GetMapping(Paths.ProjectTask.GetProjectTask.PATH)
    public ResponseEntity<ProjectTaskDTO> getProjectTask(@PathVariable
                                                         String projectId,
                                                         @PathVariable
                                                         Long projectTaskId,
                                                         Principal principal) {

        ProjectTaskDTO projectTask = projectTaskService.findProjectTaskByIdDTO(projectId, projectTaskId, principal.getName());

        return new ResponseEntity<>(projectTask, HttpStatus.OK);
    }

    @Override
    @PatchMapping(Paths.ProjectTask.UpdateProjectTask.PATH)
    public ResponseEntity<ProjectTaskDTO> updateProjectTask(@Valid
                                                            @RequestBody
                                                            ProjectTaskDTO projectTaskDTO,
                                                            BindingResult result,
                                                            @PathVariable
                                                            String projectId,
                                                            @PathVariable
                                                            Long projectTaskId,
                                                            Principal principal) {

        mapValidationErrorService.mapValidationService(result);
        ProjectTaskDTO updatedTask = projectTaskService.updateByProjectTaskById(projectTaskDTO, projectId, projectTaskId, principal.getName());

        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @Override
    @DeleteMapping(Paths.ProjectTask.DeleteProjectTask.PATH)
    public ResponseEntity<DeleteDTO> deleteProjectTask(@PathVariable
                                                       String projectId,
                                                       @PathVariable
                                                       Long projectTaskId,
                                                       Principal principal) {

        DeleteDTO response = projectTaskService.deleteProjectTaskById(projectId, projectTaskId, principal.getName());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
