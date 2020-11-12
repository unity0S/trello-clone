package io.molnarsandor.pmtool.util;

import io.molnarsandor.pmtool.domain.dto.*;
import io.molnarsandor.pmtool.domain.entity.Collaborator;
import io.molnarsandor.pmtool.domain.entity.Project;
import io.molnarsandor.pmtool.domain.entity.ProjectTask;
import io.molnarsandor.pmtool.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ModelConverter {

    private final ModelMapper modelMapper;

    public User userRegisterDtoToEntity(UserRegisterDTO dto) {

        return modelMapper.map(dto, User.class);
    }

    public UserDTO userEntityToDto(User user) {

        return modelMapper.map(user, UserDTO.class);
    }

    public ProjectTask projectTaskDtoToEntity(ProjectTaskDTO projectTaskDTO) {

        return modelMapper.map(projectTaskDTO, ProjectTask.class);
    }

    public ProjectTaskDTO projectTaskEntityToDto(ProjectTask projectTask) {

        return modelMapper.map(projectTask, ProjectTaskDTO.class);
    }

    public List<ProjectTaskDTO> projectTasksEntityListToDto(List<ProjectTask> projectTasks) {

        return projectTasks.stream()
                .map(this::projectTaskEntityToDto)
                .collect(Collectors.toList());
    }

    public Collaborator collaboratorDtoToEntity(CollaboratorDTO collaboratorDTO) {

        return modelMapper.map(collaboratorDTO, Collaborator.class);
    }

    public CollaboratorDTO collaboratorEntityToDto(Collaborator collaborator) {

        return modelMapper.map(collaborator, CollaboratorDTO.class);
    }

    public Project projectDtoToEntity(ProjectDTO projectDTO) {

        return modelMapper.map(projectDTO, Project.class);
    }

    public ProjectDTO projectEntityToDto(Project project) {

        return modelMapper.map(project, ProjectDTO.class);
    }

    public List<ProjectDTO> projectEntityListToDto(List<Project> projects) {

        return projects.stream()
                .map(this::projectEntityToDto)
                .collect(Collectors.toList());
    }
}
