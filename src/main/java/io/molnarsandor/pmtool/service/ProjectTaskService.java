package io.molnarsandor.pmtool.service;

import io.molnarsandor.pmtool.domain.Backlog;
import io.molnarsandor.pmtool.domain.ProjectTask;
import io.molnarsandor.pmtool.dto.DeleteDTO;
import io.molnarsandor.pmtool.exceptions.CustomInternalServerErrorException;
import io.molnarsandor.pmtool.exceptions.ProjectNotFoundException;
import io.molnarsandor.pmtool.repositories.BacklogRepository;
import io.molnarsandor.pmtool.repositories.ProjectRepository;
import io.molnarsandor.pmtool.repositories.ProjectTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProjectTaskService {

    private final ProjectTaskRepository projectTaskRepository;

    private final ProjectService projectService;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {

        // Exceptions: Project not found
        try {
            // PTs to be added to a specific project, project != null, BL exists

            // TODO decouple
            Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();
            // Set the BL to PT
            projectTask.setBacklog(backlog);

            // Project sequence like this: IDPRO-1, IDPRO-2 ...IDPRO-100
            Integer backlogSequence = backlog.getPTSequence();

            // Update the BL sequence
            backlogSequence++;

            backlog.setPTSequence(backlogSequence);

            // Add the sequence to the PT
            projectTask.setProjectSequence(backlog.getProjectIdentifier() + "-" + backlogSequence);
            projectTask.setProjectIdentifier(projectIdentifier.toUpperCase());

            // INITIAL priority when priority is null
            if(projectTask.getPriority() == null || projectTask.getPriority() == 0) {
                projectTask.setPriority(3);
            }

            // INITIAL status when status is null
            if(projectTask.getStatus() == null || projectTask.getStatus().equals("")) {
                projectTask.setStatus("TO_DO");
            }

            return projectTaskRepository.save(projectTask);

        } catch (DataAccessException io) {
            throw new CustomInternalServerErrorException("Internal Server Error", io);
        }

    }

    public Iterable<ProjectTask> findBacklogById(String id, String username) {

        // TODO decouple
        projectService.findProjectByIdentifier(id, username);

        return projectTaskRepository.findByProjectIdentifierIgnoreCaseOrderByPriority(id);
    }

    public ProjectTask findPTByProjectSequence(String backlogId, String ptId, String username) {

        // TODO decouple
        projectService.findProjectByIdentifier(backlogId, username);

        // TODO decouple
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(ptId);
        if(projectTask == null) {
            throw new ProjectNotFoundException("Project Task '" + ptId + "' was not found");
        }

        // TODO decouple
        if(!projectTask.getProjectIdentifier().equalsIgnoreCase(backlogId)) {
            throw new ProjectNotFoundException("Project Task '" + ptId + "' does not exists in project: '" + backlogId);
        }

        return projectTask;
    }


    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlogId, String ptId, String username) {

        // TODO decouple
        findPTByProjectSequence(backlogId, ptId, username);

        return projectTaskRepository.save(updatedTask);
    }

    public DeleteDTO deletePTByProjectSequence(String backlogId, String ptId, String username) {

        // TODO decouple
        ProjectTask projectTask = findPTByProjectSequence(backlogId, ptId, username);

        projectTaskRepository.delete(projectTask);

        return new DeleteDTO("Project task " + ptId + " deleted");
    }
}
