package io.molnarsandor.trelloclone.project_task.controller;

import io.molnarsandor.trelloclone.project_task.ProjectTaskService;
import io.molnarsandor.trelloclone.project_task.model.ProjectTaskDTO;
import io.molnarsandor.trelloclone.util.DeleteDTO;
import io.molnarsandor.trelloclone.util.MapValidationErrorService;
import io.molnarsandor.trelloclone.util.ModelConverter;
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
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogControllerImpl implements BacklogController {

    private final ProjectTaskService projectTaskService;

    private final MapValidationErrorService mapValidationErrorService;

    private final ModelConverter modelConverter;

    @Override
    @PostMapping("/{backlogId}")
    public ResponseEntity<ProjectTaskDTO> addPTtoBacklog(@Valid
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
    @GetMapping("/{backlogId}")
    public ResponseEntity<List<ProjectTaskDTO>> getProjectBacklog(@PathVariable
                                                                  String backlogId,
                                                                  Principal principal) {

        List<ProjectTaskDTO> projectTasks = projectTaskService.findBacklogById(backlogId, principal.getName());

        return new ResponseEntity<>(projectTasks, HttpStatus.OK);
    }

    @Override
    @GetMapping("/{backlogId}/{ptId}")
    public ResponseEntity<ProjectTaskDTO> getProjectTask(@PathVariable
                                                         String backlogId,
                                                         @PathVariable
                                                         String ptId,
                                                         Principal principal) {

        ProjectTaskDTO projectTask = projectTaskService.findPtByProjectSequenceDTO(backlogId, ptId, principal.getName());

        return new ResponseEntity<>(projectTask, HttpStatus.OK);
    }

    @Override
    @PatchMapping("/{backlogId}/{ptId}")
    public ResponseEntity<ProjectTaskDTO> updateProjectTask(@Valid
                                                            @RequestBody
                                                            ProjectTaskDTO projectTaskDTO,
                                                            BindingResult result,
                                                            @PathVariable
                                                            String backlogId,
                                                            @PathVariable
                                                            String ptId,
                                                            Principal principal) {

        mapValidationErrorService.mapValidationService(result);
        ProjectTaskDTO updatedTask = projectTaskService.updateByProjectSequence(projectTaskDTO,backlogId, ptId, principal.getName());

        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{backlogId}/{ptId}")
    public ResponseEntity<DeleteDTO> deleteProjectTask(@PathVariable
                                                       String backlogId,
                                                       @PathVariable
                                                       String ptId,
                                                       Principal principal) {

        DeleteDTO response = projectTaskService.deletePTByProjectSequence(backlogId, ptId, principal.getName());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
