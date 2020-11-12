package io.molnarsandor.pmtool.service;

import io.molnarsandor.pmtool.domain.dto.DeleteDTO;
import io.molnarsandor.pmtool.domain.entity.Backlog;
import io.molnarsandor.pmtool.domain.entity.Collaborator;
import io.molnarsandor.pmtool.domain.entity.Project;
import io.molnarsandor.pmtool.domain.entity.User;
import io.molnarsandor.pmtool.exceptions.ProjectIdException;
import io.molnarsandor.pmtool.exceptions.ProjectNotFoundException;
import io.molnarsandor.pmtool.exceptions.UserNotLoggedInException;
import io.molnarsandor.pmtool.repositories.BacklogRepository;
import io.molnarsandor.pmtool.repositories.CollaboratorRepository;
import io.molnarsandor.pmtool.repositories.ProjectRepository;
import io.molnarsandor.pmtool.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final BacklogRepository backlogRepository;

    private final UserRepository userRepository;

    private final CollaboratorRepository collaboratorRepository;

    public Project saveOrUpdateProject(Project project, String username) {

        if(project.getId() != null) {
            Project existingProject = projectRepository.findByProjectIdentifierIgnoreCase(project.getProjectIdentifier());

            if(existingProject != null && !existingProject.getProjectLeader().equals(username)) {
                throw new ProjectNotFoundException("Project not found in your account");
            } else if(existingProject == null) {
                throw new ProjectNotFoundException("Project with ID: '" + project.getProjectIdentifier() + "' cannot be updated because it does not exist");
            }

        }
        try {
            User user = userRepository.findByEmail(username);
            project.setUser(user);
            project.setProjectLeader(user.getEmail());
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

            if(project.getId() == null) {
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }

            if(project.getId() != null) {
                project.setBacklog(backlogRepository.findByProjectIdentifierIgnoreCase(project.getProjectIdentifier().toUpperCase()));
            }

            return projectRepository.save(project);
        } catch (Exception e) {
            throw new ProjectIdException("Project ID '" + project.getProjectIdentifier().toUpperCase() + "' already exists");
        }
    }

    public Project findProjectByIdentifier(String projectId, String username) {

        Project project = projectRepository.findByProjectIdentifierIgnoreCase(projectId);

        if(project == null) {
            throw new ProjectNotFoundException("Project ID '" + projectId + "' does not exists");
        }

       Set<Collaborator> collaborators = project.getCollaborators();

        Predicate<Collaborator> collaboratorPredicate = collaborator ->
                collaborator.getProjectIdentifier().equalsIgnoreCase(projectId) &&
                collaborator.getEmail().equals(username);


        boolean collaboratorExists = false;
        if (!collaborators.isEmpty()) {
            collaboratorExists = collaborators
                    .stream()
                    .anyMatch(collaboratorPredicate);
        }

        if(!project.getProjectLeader().equals(username) && !collaboratorExists) {
            throw new ProjectNotFoundException("Project not found in your account");
        }

        return project;
    }

    public List<Project> findAllProject(Principal username) {
        if(username == null || username.getName() == null) {
            throw new UserNotLoggedInException("Log in to continue");
        }

        List<Project> byLeader = projectRepository.findAllByProjectLeader(username.getName());

        Collaborator collaborator = collaboratorRepository.findByEmail(username.getName());

        List<Project> byCollaborator = projectRepository.findAllByCollaborators(collaborator);

        byLeader.addAll(byCollaborator);

        return byLeader;
    }

    public DeleteDTO deleteProjectByIdentifier(String projectId, String username) {

        projectRepository.delete(findProjectByIdentifier(projectId, username));

        return new DeleteDTO("Project " + projectId + " deleted");
    }
}
