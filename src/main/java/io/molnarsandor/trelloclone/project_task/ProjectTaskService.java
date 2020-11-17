package io.molnarsandor.trelloclone.project_task;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.molnarsandor.trelloclone.util.DeleteDTO;
import io.molnarsandor.trelloclone.exceptions.CustomInternalServerErrorException;
import io.molnarsandor.trelloclone.project.ProjectService;
import io.molnarsandor.trelloclone.project.exceptions.ProjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public final class ProjectTaskService {

    private final ProjectTaskRepository projectTaskRepository;

    private final ProjectService projectService;

    @SuppressFBWarnings("DLS_DEAD_LOCAL_STORE")
    public ProjectTaskEntity addProjectTask(final String projectIdentifier, final ProjectTaskEntity projectTaskEntity, final String username) {

        // Exceptions: Project not found
        try {
            // PTs to be added to a specific project, project != null, BL exists

            // TODO decouple
            BacklogEntity backlogEntity = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();
            // Set the BL to PT
            projectTaskEntity.setBacklog(backlogEntity);

            // Project sequence like this: IDPRO-1, IDPRO-2 ...IDPRO-100
            Integer backlogSequence = backlogEntity.getPtSequence();

            // Update the BL sequence
            backlogSequence++;

            backlogEntity.setPtSequence(backlogSequence);

            // Add the sequence to the PT
            projectTaskEntity.setProjectSequence(backlogEntity.getProjectIdentifier() + "-" + backlogSequence);
            projectTaskEntity.setProjectIdentifier(projectIdentifier.toUpperCase());

            // INITIAL priority when priority is null
            if (projectTaskEntity.getPriority() == null || projectTaskEntity.getPriority() == 0) {
                projectTaskEntity.setPriority(3);
            }

            // INITIAL status when status is null
            if (projectTaskEntity.getStatus() == null || projectTaskEntity.getStatus().equals("")) {
                projectTaskEntity.setStatus("TO_DO");
            }

            return projectTaskRepository.save(projectTaskEntity);

        } catch (DataAccessException io) {
            throw new CustomInternalServerErrorException("Internal Server Error", io);
        }

    }

    public List<ProjectTaskEntity> findBacklogById(final String id, final String username) {

        // TODO decouple
        projectService.findProjectByIdentifier(id, username);

        return projectTaskRepository.findByProjectIdentifierIgnoreCaseOrderByPriority(id);
    }

    public ProjectTaskEntity findPtByProjectSequence(final String backlogId, final String ptId, final String username) {

        // TODO decouple
        projectService.findProjectByIdentifier(backlogId, username);

        // TODO decouple
        ProjectTaskEntity projectTaskEntity = projectTaskRepository.findByProjectSequence(ptId);
        if (projectTaskEntity == null) {
            throw new ProjectNotFoundException("Project Task '" + ptId + "' was not found");
        }

        // TODO decouple
        if (!projectTaskEntity.getProjectIdentifier().equalsIgnoreCase(backlogId)) {
            throw new ProjectNotFoundException("Project Task '" + ptId + "' does not exists in project: '" + backlogId);
        }

        return projectTaskEntity;
    }


    public ProjectTaskEntity updateByProjectSequence(final ProjectTaskEntity updatedTask, final String backlogId, final String ptId, final String username) {

        // TODO decouple
        findPtByProjectSequence(backlogId, ptId, username);

        return projectTaskRepository.save(updatedTask);
    }

    public DeleteDTO deletePTByProjectSequence(final String backlogId, final String ptId, final String username) {

        // TODO decouple
        ProjectTaskEntity projectTaskEntity = findPtByProjectSequence(backlogId, ptId, username);

        projectTaskRepository.delete(projectTaskEntity);

        return new DeleteDTO("Project task " + ptId + " deleted");
    }
}
