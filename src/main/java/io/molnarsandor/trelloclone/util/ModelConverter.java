package io.molnarsandor.trelloclone.util;

import io.molnarsandor.trelloclone.collaborator.model.CollaboratorDTO;
import io.molnarsandor.trelloclone.collaborator.model.CollaboratorEntity;
import io.molnarsandor.trelloclone.project_task.model.ProjectTaskDTO;
import io.molnarsandor.trelloclone.user.model.UserRegistrationResponseDTO;
import io.molnarsandor.trelloclone.user.model.UserEntity;
import io.molnarsandor.trelloclone.project.model.ProjectDTO;
import io.molnarsandor.trelloclone.project.model.ProjectEntity;
import io.molnarsandor.trelloclone.project_task.model.ProjectTaskEntity;
import io.molnarsandor.trelloclone.user.model.UserRegisterDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ModelConverter {

    private final ModelMapper modelMapper;

    public UserEntity userRegisterDtoToEntity(UserRegisterDTO dto) {

        return modelMapper.map(dto, UserEntity.class);
    }

    public UserRegistrationResponseDTO userEntityToDto(UserEntity userEntity) {

        return modelMapper.map(userEntity, UserRegistrationResponseDTO.class);
    }

    public ProjectTaskEntity projectTaskDtoToEntity(ProjectTaskDTO projectTaskDTO) {

        return modelMapper.map(projectTaskDTO, ProjectTaskEntity.class);
    }

    public ProjectTaskDTO projectTaskEntityToDto(ProjectTaskEntity projectTaskEntity) {

        return modelMapper.map(projectTaskEntity, ProjectTaskDTO.class);
    }

    public List<ProjectTaskDTO> projectTasksEntityListToDto(List<ProjectTaskEntity> projectTaskEntities) {

        return projectTaskEntities.stream()
                .map(this::projectTaskEntityToDto)
                .collect(Collectors.toList());
    }

    public CollaboratorEntity collaboratorDtoToEntity(CollaboratorDTO collaboratorDTO) {

        return modelMapper.map(collaboratorDTO, CollaboratorEntity.class);
    }

    public CollaboratorDTO collaboratorEntityToDto(CollaboratorEntity collaboratorEntity) {

        return modelMapper.map(collaboratorEntity, CollaboratorDTO.class);
    }

    public ProjectEntity projectDtoToEntity(ProjectDTO projectDTO) {

        return modelMapper.map(projectDTO, ProjectEntity.class);
    }

    public ProjectDTO projectEntityToDto(ProjectEntity projectEntity) {

        return modelMapper.map(projectEntity, ProjectDTO.class);
    }

    public List<ProjectDTO> projectEntityListToDto(List<ProjectEntity> projectEntities) {

        return projectEntities.stream()
                .map(this::projectEntityToDto)
                .collect(Collectors.toList());
    }
}
