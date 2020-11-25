package io.molnarsandor.trelloclone.projectTask;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.molnarsandor.trelloclone.project.ProjectService;
import io.molnarsandor.trelloclone.project.exceptions.ProjectNotFoundException;
import io.molnarsandor.trelloclone.project.model.ProjectEntity;
import io.molnarsandor.trelloclone.projectTask.model.BacklogEntity;
import io.molnarsandor.trelloclone.projectTask.model.ProjectTaskDTO;
import io.molnarsandor.trelloclone.projectTask.model.ProjectTaskEntity;
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

    public ProjectTaskDTO addProjectTask(final String projectIdentifier, final ProjectTaskDTO projectTaskDTO, final String username) {

        ProjectTaskEntity projectTaskEntity = modelConverter.projectTaskDtoToEntity(projectTaskDTO);

        ProjectEntity projectEntity = projectService.findProjectByIdentifier(projectIdentifier, username);
        BacklogEntity backlogEntity = projectEntity.getBacklog();
        projectTaskEntity.setBacklog(backlogEntity);
        Integer backlogSequence = backlogEntity.getProjectTaskSequence();
        backlogSequence++;
        backlogEntity.setProjectTaskSequence(backlogSequence);
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

    public ProjectTaskEntity findProjectTaskByProjectSequence(final String backlogId, final String projectTaskId, final String username) {

        return getProjectTaskByProjectSequence(backlogId, projectTaskId, username);
    }

    public ProjectTaskDTO findProjectTaskByProjectSequenceDTO(final String backlogId, final String projectTaskId, final String username) {

        return modelConverter.projectTaskEntityToDto(
                getProjectTaskByProjectSequence(backlogId, projectTaskId, username));
    }

    public ProjectTaskDTO updateByProjectSequence(final ProjectTaskDTO projectTaskDTO, final String backlogId, final String projectTaskId, final String username) {

        ProjectTaskEntity projectTaskEntity = modelConverter.projectTaskDtoToEntity(projectTaskDTO);

        findProjectTaskByProjectSequence(backlogId, projectTaskId, username);

        return modelConverter.projectTaskEntityToDto(
                projectTaskRepository.save(projectTaskEntity));
    }

    public DeleteDTO deleteProjectTaskByProjectSequence(final String backlogId, final String projectTaskId, final String username) {

        ProjectTaskEntity projectTaskEntity = findProjectTaskByProjectSequence(backlogId, projectTaskId, username);
        projectTaskRepository.delete(projectTaskEntity);

        return new DeleteDTO("Project task " + projectTaskId + " deleted");
    }

    private void validateProjectTask(final ProjectTaskEntity projectTaskEntity, final String projectTaskId, final String backlogId) {

        if (projectTaskEntity == null) {
            throw new ProjectNotFoundException("Project Task '" + projectTaskId + "' was not found");
        }

        if (!projectTaskEntity.getProjectIdentifier().equalsIgnoreCase(backlogId)) {
            throw new ProjectNotFoundException("Project Task '" + projectTaskId + "' does not exists in project: '" + backlogId);
        }
    }

    private ProjectTaskEntity getProjectTaskByProjectSequence(final String backlogId, final String projectTaskId, final String username) {

        projectService.findProjectByIdentifier(backlogId, username);
        ProjectTaskEntity projectTaskEntity;

        projectTaskEntity = projectTaskRepository.findByProjectSequence(projectTaskId);

        validateProjectTask(projectTaskEntity, projectTaskId, backlogId);

        return projectTaskEntity;
    }
}
