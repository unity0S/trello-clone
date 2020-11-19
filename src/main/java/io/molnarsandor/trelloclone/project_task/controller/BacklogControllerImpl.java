package io.molnarsandor.trelloclone.project_task.controller;

import io.molnarsandor.trelloclone.project_task.model.ProjectTaskDTO;
import io.molnarsandor.trelloclone.project_task.model.ProjectTaskEntity;
import io.molnarsandor.trelloclone.project_task.ProjectTaskService;
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
                                                         Authentication authentication) {

        mapValidationErrorService.mapValidationService(result);
        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        ProjectTaskEntity projectTaskEntity = modelConverter.projectTaskDtoToEntity(projectTaskDTO);
        ProjectTaskDTO savedProjectTask = modelConverter.projectTaskEntityToDto(
                projectTaskService.addProjectTask(backlogId, projectTaskEntity, userEntity.getEmail()));

        return new ResponseEntity<>(savedProjectTask, HttpStatus.CREATED);
    }

    @Override
    @GetMapping("/{backlogId}")
    public ResponseEntity<List<ProjectTaskDTO>> getProjectBacklog(@PathVariable
                                                                  String backlogId,
                                                                  Authentication authentication) {

        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        List<ProjectTaskDTO> projectTasks = modelConverter.projectTasksEntityListToDto(
                projectTaskService.findBacklogById(backlogId, userEntity.getEmail()));

        return new ResponseEntity<>(projectTasks, HttpStatus.OK);
    }

    @Override
    @GetMapping("/{backlogId}/{ptId}")
    public ResponseEntity<ProjectTaskDTO> getProjectTask(@PathVariable
                                                         String backlogId,
                                                         @PathVariable
                                                         String ptId,
                                                         Authentication authentication) {

        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        ProjectTaskDTO projectTask = modelConverter.projectTaskEntityToDto(
                projectTaskService.findPtByProjectSequence(backlogId, ptId, userEntity.getEmail()));

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
                                                            Authentication authentication) {

        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        mapValidationErrorService.mapValidationService(result);
        ProjectTaskEntity projectTaskEntity = modelConverter.projectTaskDtoToEntity(projectTaskDTO);
        ProjectTaskDTO updatedTask = modelConverter.projectTaskEntityToDto(
                projectTaskService.updateByProjectSequence(projectTaskEntity,backlogId, ptId, userEntity.getEmail()));

        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{backlogId}/{ptId}")
    public ResponseEntity<DeleteDTO> deleteProjectTask(@PathVariable
                                                       String backlogId,
                                                       @PathVariable
                                                       String ptId,
                                                       Authentication authentication) {

        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        DeleteDTO response = projectTaskService.deletePTByProjectSequence(backlogId, ptId, userEntity.getEmail());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
