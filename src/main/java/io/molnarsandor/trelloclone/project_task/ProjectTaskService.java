package io.molnarsandor.trelloclone.project_task;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.molnarsandor.trelloclone.project.ProjectService;
import io.molnarsandor.trelloclone.project.exceptions.ProjectNotFoundException;
import io.molnarsandor.trelloclone.project.model.ProjectEntity;
import io.molnarsandor.trelloclone.project_task.model.BacklogEntity;
import io.molnarsandor.trelloclone.project_task.model.ProjectTaskDTO;
import io.molnarsandor.trelloclone.project_task.model.ProjectTaskEntity;
import io.molnarsandor.trelloclone.util.DeleteDTO;
import io.molnarsandor.trelloclone.util.ModelConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressFBWarnings("DLS_DEAD_LOCAL_STORE")
@RequiredArgsConstructor
@Service
public final class ProjectTaskService {

    private final ProjectTaskRepository projectTaskRepository;

    private final ProjectService projectService;

    private final ModelConverter modelConverter;

    // == PUBLIC METHODS ==
    public ProjectTaskDTO addProjectTask(final String projectIdentifier, final ProjectTaskDTO projectTaskDTO, final String username) {

        ProjectTaskEntity projectTaskEntity = modelConverter.projectTaskDtoToEntity(projectTaskDTO);

        ProjectEntity projectEntity = projectService.findProjectByIdentifier(projectIdentifier, username);
        BacklogEntity backlogEntity = projectEntity.getBacklog();
        projectTaskEntity.setBacklog(backlogEntity);
        Integer backlogSequence = backlogEntity.getPtSequence();
        backlogSequence++;
        backlogEntity.setPtSequence(backlogSequence);
        projectTaskEntity.setProjectSequence(backlogEntity.getProjectIdentifier() + "-" + backlogSequence);
        projectTaskEntity.setProjectIdentifier(projectIdentifier.toUpperCase());

        return modelConverter.projectTaskEntityToDto(
                projectTaskRepository.save(projectTaskEntity));
    }

    public List<ProjectTaskDTO> findBacklogById(final String id, final String username) {

        projectService.findProjectByIdentifier(id, username);

        return modelConverter.projectTasksEntityListToDto(
                projectTaskRepository.findByProjectIdentifierIgnoreCaseOrderByPriority(id));
    }

    public ProjectTaskEntity findPtByProjectSequence(final String backlogId, final String ptId, final String username) {

        return getPTByProjectSequence(backlogId, ptId, username);
    }

    public ProjectTaskDTO findPtByProjectSequenceDTO(final String backlogId, final String ptId, final String username) {

        return modelConverter.projectTaskEntityToDto(
                getPTByProjectSequence(backlogId, ptId, username));
    }

    public ProjectTaskDTO updateByProjectSequence(final ProjectTaskDTO projectTaskDTO, final String backlogId, final String ptId, final String username) {

        ProjectTaskEntity projectTaskEntity = modelConverter.projectTaskDtoToEntity(projectTaskDTO);

        findPtByProjectSequence(backlogId, ptId, username);

        return modelConverter.projectTaskEntityToDto(
                projectTaskRepository.save(projectTaskEntity));
    }

    public DeleteDTO deletePTByProjectSequence(final String backlogId, final String ptId, final String username) {

        ProjectTaskEntity projectTaskEntity = findPtByProjectSequence(backlogId, ptId, username);
        projectTaskRepository.delete(projectTaskEntity);

        return new DeleteDTO("Project task " + ptId + " deleted");
    }

    // == PRIVATE METHODS ==
    private void validateProjectTask(ProjectTaskEntity projectTaskEntity, String ptId, String backlogId) {

        if (projectTaskEntity == null) {
            throw new ProjectNotFoundException("Project Task '" + ptId + "' was not found");
        }

        if (!projectTaskEntity.getProjectIdentifier().equalsIgnoreCase(backlogId)) {
            throw new ProjectNotFoundException("Project Task '" + ptId + "' does not exists in project: '" + backlogId);
        }
    }

    private ProjectTaskEntity getPTByProjectSequence(String backlogId, String ptId, String username) {
        projectService.findProjectByIdentifier(backlogId, username);
        ProjectTaskEntity projectTaskEntity;

        projectTaskEntity = projectTaskRepository.findByProjectSequence(ptId);

        validateProjectTask(projectTaskEntity, ptId, backlogId);

        return projectTaskEntity;
    }
}
