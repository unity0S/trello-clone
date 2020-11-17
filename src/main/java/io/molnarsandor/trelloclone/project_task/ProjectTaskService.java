package io.molnarsandor.trelloclone.project_task;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.molnarsandor.trelloclone.global_exceptions.CustomInternalServerErrorException;
import io.molnarsandor.trelloclone.project.ProjectEntity;
import io.molnarsandor.trelloclone.project.ProjectService;
import io.molnarsandor.trelloclone.project.exceptions.ProjectNotFoundException;
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

    // == PROTECTED METHODS ==
    @SuppressFBWarnings("DLS_DEAD_LOCAL_STORE")
    protected ProjectTaskEntity addProjectTask(final String projectIdentifier, final ProjectTaskEntity projectTaskEntity, final String username) {

        try {
            // VALIDATE PROJECT
            ProjectEntity projectEntity = projectService.findProjectByIdentifier(projectIdentifier, username);

            // GET BACKLOG
            BacklogEntity backlogEntity = projectEntity.getBacklog();

            // SET BACKLOG TO PROJECTTASK
            projectTaskEntity.setBacklog(backlogEntity);

            // GET CURRENT PROJECTTASK SEQUENCE FROM BACKLOG
            Integer backlogSequence = backlogEntity.getPtSequence();

            // UPDATE THE SEQUENCE
            backlogSequence++;

            // SET UPDATED SEQUENCE TO BACKLOG
            backlogEntity.setPtSequence(backlogSequence);

            // SET THE UPDATED SEQUENCE TO THE PROJECTTASK
            projectTaskEntity.setProjectSequence(backlogEntity.getProjectIdentifier() + "-" + backlogSequence);
            projectTaskEntity.setProjectIdentifier(projectIdentifier.toUpperCase());

            // INITIAL PRIORITY
            if (projectTaskEntity.getPriority() == null || projectTaskEntity.getPriority() == 0) {
                projectTaskEntity.setPriority(PRIORITY);
            }

            // INITIAL STATUS
            if (projectTaskEntity.getStatus() == null || projectTaskEntity.getStatus().equals("")) {
                projectTaskEntity.setStatus(TO_DO);
            }

            return projectTaskRepository.save(projectTaskEntity);

        } catch (DataAccessException io) {
            throw new CustomInternalServerErrorException(io);
        }

    }

    protected List<ProjectTaskEntity> findBacklogById(final String id, final String username) {

        // VALIDATE PROJECT
        projectService.findProjectByIdentifier(id, username);

        List<ProjectTaskEntity> projectTaskEntities;

        try {
            projectTaskEntities = projectTaskRepository.findByProjectIdentifierIgnoreCaseOrderByPriority(id);
        } catch (DataAccessException io) {
            throw new CustomInternalServerErrorException(io);
        }

        return  projectTaskEntities;
    }

    protected ProjectTaskEntity findPtByProjectSequence(final String backlogId, final String ptId, final String username) {

        // VALIDATE PROJECT
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

    protected ProjectTaskEntity updateByProjectSequence(final ProjectTaskEntity updatedTask, final String backlogId, final String ptId, final String username) {

        findPtByProjectSequence(backlogId, ptId, username);

        return projectTaskRepository.save(updatedTask);
    }

    protected DeleteDTO deletePTByProjectSequence(final String backlogId, final String ptId, final String username) {

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
