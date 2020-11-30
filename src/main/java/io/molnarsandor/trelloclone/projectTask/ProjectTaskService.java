package io.molnarsandor.trelloclone.projectTask;

import io.molnarsandor.trelloclone.projectTask.model.ProjectTaskDTO;
import io.molnarsandor.trelloclone.util.DeleteDTO;

import java.util.List;

public interface ProjectTaskService {

    ProjectTaskDTO addProjectTask(final String projectIdentifier, final ProjectTaskDTO projectTaskDTO, final String username);
    List<ProjectTaskDTO> findProjectTasksByProjectId(final String id, final String username);
    ProjectTaskDTO findProjectTaskByIdDTO(final String projectId, final Long projectTaskId, final String username);
    ProjectTaskDTO updateByProjectTaskById(final ProjectTaskDTO projectTaskDTO, final String projectId, final Long projectTaskId, final String username);
    DeleteDTO deleteProjectTaskById(final String projectId, final Long projectTaskId, final String username);
}
