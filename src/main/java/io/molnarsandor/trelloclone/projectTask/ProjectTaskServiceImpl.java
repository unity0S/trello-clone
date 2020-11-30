package io.molnarsandor.trelloclone.projectTask;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.molnarsandor.trelloclone.project.ProjectService;
import io.molnarsandor.trelloclone.project.exceptions.ProjectNotFoundException;
import io.molnarsandor.trelloclone.project.model.ProjectEntity;
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
public final class ProjectTaskServiceImpl implements ProjectTaskService {

    private final ProjectTaskRepository projectTaskRepository;

    private final ProjectService projectService;

    private final ModelConverter modelConverter;

    @Override
    public ProjectTaskDTO addProjectTask(final String projectIdentifier, final ProjectTaskDTO projectTaskDTO, final String username) {

        ProjectTaskEntity projectTaskEntity = modelConverter.projectTaskDtoToEntity(projectTaskDTO);

        ProjectEntity projectEntity = projectService.findProjectByIdentifier(projectIdentifier, username);
        projectTaskEntity.setProject(projectEntity);
        projectTaskEntity.setProjectIdentifier(projectIdentifier.toUpperCase());

        if (projectTaskEntity.getPriority() == null) {
            projectTaskEntity.setPriority(3);
        }

        if (projectTaskEntity.getStatus() == null || projectTaskEntity.getStatus().equals("")) {
            projectTaskEntity.setStatus("TO_DO");
        }

        return modelConverter.projectTaskEntityToDto(
            projectTaskRepository.save(projectTaskEntity));
    }

    @Override
    public List<ProjectTaskDTO> findProjectTasksByProjectId(final String id, final String username) {

        projectService.findProjectByIdentifier(id, username);

        return modelConverter.projectTasksEntityListToDto(
                projectTaskRepository.findByProjectIdentifierIgnoreCaseOrderByPriority(id));
    }

    @Override
    public ProjectTaskDTO findProjectTaskByIdDTO(final String projectId, final Long projectTaskId, final String username) {

        return modelConverter.projectTaskEntityToDto(
                getProjectTaskById(projectId, projectTaskId, username));
    }

    @Override
    public ProjectTaskDTO updateByProjectTaskById(final ProjectTaskDTO projectTaskDTO, final String projectId, final Long projectTaskId, final String username) {

        ProjectTaskEntity projectTaskEntity = modelConverter.projectTaskDtoToEntity(projectTaskDTO);

        getProjectTaskById(projectId, projectTaskId, username);

        return modelConverter.projectTaskEntityToDto(
                projectTaskRepository.save(projectTaskEntity));
    }

    @Override
    public DeleteDTO deleteProjectTaskById(final String projectId, final Long projectTaskId, final String username) {

        getProjectTaskById(projectId, projectTaskId, username);

        ProjectTaskEntity projectTaskEntity = projectTaskRepository.findById(projectTaskId).orElse(null);

        validateProjectTask(projectTaskEntity, projectTaskId, projectId);

        projectTaskRepository.delete(projectTaskEntity);

        return new DeleteDTO("Project task " + projectTaskId + " deleted");
    }

    private void validateProjectTask(final ProjectTaskEntity projectTaskEntity, final Long projectTaskId, final String projectId) {

        if (projectTaskEntity == null) {
            throw new ProjectNotFoundException("Project Task '" + projectTaskId + "' was not found");
        }

        if (!projectTaskEntity.getProjectIdentifier().equalsIgnoreCase(projectId)) {
            throw new ProjectNotFoundException("Project Task '" + projectTaskId + "' does not exists in project: '" + projectId);
        }
    }

    private ProjectTaskEntity getProjectTaskById(final String projectId, final Long projectTaskId, final String username) {

        projectService.findProjectByIdentifier(projectId, username);
        ProjectTaskEntity projectTaskEntity = projectTaskRepository.findById(projectTaskId).orElse(null);

        validateProjectTask(projectTaskEntity, projectTaskId, projectId);

        return projectTaskEntity;
    }
}
