package io.molnarsandor.trelloclone.projectTask.controller;

import io.molnarsandor.trelloclone.projectTask.ProjectTaskService;
import io.molnarsandor.trelloclone.projectTask.model.ProjectTaskDTO;
import io.molnarsandor.trelloclone.util.DeleteDTO;
import io.molnarsandor.trelloclone.util.MapValidationErrorService;
import io.molnarsandor.trelloclone.util.ModelConverter;
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
@RequestMapping(Paths.Backlog.PATH)
@CrossOrigin
public class BacklogControllerImpl implements BacklogController {

    private final ProjectTaskService projectTaskService;

    private final MapValidationErrorService mapValidationErrorService;

    private final ModelConverter modelConverter;

    @Override
    @PostMapping(Paths.Backlog.AddProjectTaskToBacklog.PATH)
    public ResponseEntity<ProjectTaskDTO> addProjectTaskToBacklog(@Valid
                                                         @RequestBody
                                                         ProjectTaskDTO projectTaskDTO,
                                                                  BindingResult result,
                                                                  @PathVariable
                                                         String backlogId,
                                                                  Principal principal) {

        ProjectTaskDTO savedProjectTask = projectTaskService.addProjectTask(backlogId, projectTaskDTO, principal.getName());

        return new ResponseEntity<>(savedProjectTask, HttpStatus.CREATED);
    }

    @Override
    @GetMapping(Paths.Backlog.GetProjectBacklog.PATH)
    public ResponseEntity<List<ProjectTaskDTO>> getProjectBacklog(@PathVariable
                                                                  String backlogId,
                                                                  Principal principal) {

        List<ProjectTaskDTO> projectTasks = projectTaskService.findBacklogById(backlogId, principal.getName());

        return new ResponseEntity<>(projectTasks, HttpStatus.OK);
    }

    @Override
    @GetMapping(Paths.Backlog.GetProjectTask.PATH)
    public ResponseEntity<ProjectTaskDTO> getProjectTask(@PathVariable
                                                         String backlogId,
                                                         @PathVariable
                                                         String projectSequence,
                                                         Principal principal) {

        ProjectTaskDTO projectTask = projectTaskService.findProjectTaskByProjectSequenceDTO(backlogId, projectSequence, principal.getName());

        return new ResponseEntity<>(projectTask, HttpStatus.OK);
    }

    @Override
    @PatchMapping(Paths.Backlog.UpdateProjectTask.PATH)
    public ResponseEntity<ProjectTaskDTO> updateProjectTask(@Valid
                                                            @RequestBody
                                                            ProjectTaskDTO projectTaskDTO,
                                                            BindingResult result,
                                                            @PathVariable
                                                            String backlogId,
                                                            @PathVariable
                                                            String projectSequence,
                                                            Principal principal) {

        mapValidationErrorService.mapValidationService(result);
        ProjectTaskDTO updatedTask = projectTaskService.updateByProjectSequence(projectTaskDTO,backlogId, projectSequence, principal.getName());

        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @Override
    @DeleteMapping(Paths.Backlog.DeleteProjectTask.PATH)
    public ResponseEntity<DeleteDTO> deleteProjectTask(@PathVariable
                                                       String backlogId,
                                                       @PathVariable
                                                       String projectSequence,
                                                       Principal principal) {

        DeleteDTO response = projectTaskService.deleteProjectTaskByProjectSequence(backlogId, projectSequence, principal.getName());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
