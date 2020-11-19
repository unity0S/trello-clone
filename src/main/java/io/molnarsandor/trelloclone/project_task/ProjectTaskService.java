package io.molnarsandor.trelloclone.project_task;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.molnarsandor.trelloclone.global_exceptions.CustomInternalServerErrorException;
import io.molnarsandor.trelloclone.project.model.ProjectEntity;
import io.molnarsandor.trelloclone.project.ProjectService;
import io.molnarsandor.trelloclone.project.exceptions.ProjectNotFoundException;
import io.molnarsandor.trelloclone.project_task.model.BacklogEntity;
import io.molnarsandor.trelloclone.project_task.model.ProjectTaskEntity;
import io.molnarsandor.trelloclone.util.DeleteDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public final class ProjectTaskService {

    private static final String TO_DO = "TO_DO";

    private static final int PRIORITY = 3;

    private final ProjectTaskRepository projectTaskRepository;

    private final ProjectService projectService;

    // == PUBLIC METHODS ==
    @SuppressFBWarnings("DLS_DEAD_LOCAL_STORE")
    public ProjectTaskEntity addProjectTask(final String projectIdentifier, final ProjectTaskEntity projectTaskEntity, final String username) {

        try {
            ProjectEntity projectEntity = projectService.findProjectByIdentifier(projectIdentifier, username);
            BacklogEntity backlogEntity = projectEntity.getBacklog();
            projectTaskEntity.setBacklog(backlogEntity);
            Integer backlogSequence = backlogEntity.getPtSequence();
            backlogSequence++;
            backlogEntity.setPtSequence(backlogSequence);
            projectTaskEntity.setProjectSequence(backlogEntity.getProjectIdentifier() + "-" + backlogSequence);
            projectTaskEntity.setProjectIdentifier(projectIdentifier.toUpperCase());

            if (projectTaskEntity.getPriority() == null || projectTaskEntity.getPriority() == 0) {
                projectTaskEntity.setPriority(PRIORITY);
            }
            if (projectTaskEntity.getStatus() == null || projectTaskEntity.getStatus().equals("")) {
                projectTaskEntity.setStatus(TO_DO);
            }

            return projectTaskRepository.save(projectTaskEntity);
        } catch (DataAccessException io) {
            throw new CustomInternalServerErrorException(io);
        }

    }

    public List<ProjectTaskEntity> findBacklogById(final String id, final String username) {

        projectService.findProjectByIdentifier(id, username);
        List<ProjectTaskEntity> projectTaskEntities;

        try {
            projectTaskEntities = projectTaskRepository.findByProjectIdentifierIgnoreCaseOrderByPriority(id);
        } catch (DataAccessException io) {
            throw new CustomInternalServerErrorException(io);
        }

        return  projectTaskEntities;
    }

    public ProjectTaskEntity findPtByProjectSequence(final String backlogId, final String ptId, final String username) {

        projectService.findProjectByIdentifier(backlogId, username);
        ProjectTaskEntity projectTaskEntity;

        try {
            projectTaskEntity = projectTaskRepository.findByProjectSequence(ptId);
        } catch (DataAccessException io) {
            throw new CustomInternalServerErrorException(io);
        }

        validateProjectTask(projectTaskEntity, ptId, backlogId);

        return projectTaskEntity;
    }

    public ProjectTaskEntity updateByProjectSequence(final ProjectTaskEntity updatedTask, final String backlogId, final String ptId, final String username) {

        findPtByProjectSequence(backlogId, ptId, username);

        return projectTaskRepository.save(updatedTask);
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
}
