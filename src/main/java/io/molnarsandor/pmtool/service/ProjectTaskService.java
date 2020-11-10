package io.molnarsandor.pmtool.service;

import io.molnarsandor.pmtool.domain.Backlog;
import io.molnarsandor.pmtool.domain.ProjectTask;
import io.molnarsandor.pmtool.exceptions.ProjectNotFoundException;
import io.molnarsandor.pmtool.repositories.BacklogRepository;
import io.molnarsandor.pmtool.repositories.ProjectRepository;
import io.molnarsandor.pmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {

        // Exceptions: Project not found
        try {
            // PTs to be added to a specific project, project != null, BL exists
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

        } catch (Exception e) {
            throw new ProjectNotFoundException("Project not found");
        }

    }

    public Iterable<ProjectTask> findBacklogById(String id, String username) {

        projectService.findProjectByIdentifier(id, username);

        return projectTaskRepository.findByProjectIdentifierIgnoreCaseOrderByPriority(id);
    }

    public ProjectTask findPTByProjectSequence(String backlogId, String ptId, String username) {

        projectService.findProjectByIdentifier(backlogId, username);

        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(ptId);
        if(projectTask == null) {
            throw new ProjectNotFoundException("Project Task '" + ptId + "' was not found");
        }

        if(!projectTask.getProjectIdentifier().equalsIgnoreCase(backlogId)) {
            throw new ProjectNotFoundException("Project Task '" + ptId + "' does not exists in project: '" + backlogId);
        }

        return projectTask;
    }


    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlogId, String ptId, String username) {

        findPTByProjectSequence(backlogId, ptId, username);

        return projectTaskRepository.save(updatedTask);
    }

    public void deletePTByProjectSequence(String backlogId, String ptId, String username) {

        ProjectTask projectTask = findPTByProjectSequence(backlogId, ptId, username);

        projectTaskRepository.delete(projectTask);
    }
}
