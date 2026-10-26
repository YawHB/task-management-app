package com.todoapp.user.internal.service;

import com.task_management_app.api.model.UserDTO;
import com.task_management_app.api.model.UserSummaryDTO;
import com.todoapp.exceptionHandling.exceptions.ExceptionReason;
import com.todoapp.exceptionHandling.exceptions.OrganisationNotFoundException;
import com.todoapp.exceptionHandling.exceptions.ProjectNotFoundException;
import com.todoapp.exceptionHandling.exceptions.UserNotFoundException;
import com.task_management_app.api.model.OrganisationDTO;
import com.todoapp.organisation.facade.OrganisationFacade;
import com.todoapp.project.facade.ProjectFacade;
import com.todoapp.user.UserDTOConverter;
import com.todoapp.user.facade.UserSummaryProjection;
import com.todoapp.user.internal.mapper.UserMapper;
import com.todoapp.user.internal.repository.UserRepository;
import com.todoapp.user.facade.UserFacade;
import com.todoapp.user.internal.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class UserFacadeImpl implements UserFacade {

    private final UserRepository userRepository;
    private final UserDTOConverter userDTOConverter;
    private final UserMapper userMapper;
    private final OrganisationFacade organisationFacade;

    public UserDTO addUser(UserDTO userDTO) {
        log.info("[UserService - addUser] - calling toEntity ");
        User user = userMapper.toEntity(userDTO);
        log.info("[UserService - addUser] - calling userRepository to save user");
        User createdUser = userRepository.save(user);
        log.info("[UserService - addUser] - return the created user as a userDTO");
        return userMapper.toDto(createdUser);
    }

    @Override
    public List<UserSummaryDTO> getUsersInProject(UUID projectId) {
        log.info("[UserFacadeImpl - getUsersInProject] - calling user repo to get all users");
        List<UserSummaryProjection> users = userRepository.findMembersInProject(projectId);
        log.info("[UserFacadeImpl - getUsersInProject] - convert to DTO and return");
       return userMapper.toUserSummaryDTO(users);
    }

    @Override
    public List<UserSummaryDTO> getUsersInOrganisation(UUID organisationId) {
        if(!organisationFacade.existsById(organisationId)) {
            throw new OrganisationNotFoundException(ExceptionReason.ORG_NOT_FOUND_BY_ID.getValue());
        }
       List<UserSummaryProjection> users = userRepository.findMembersInOrganisation(organisationId);
        return userMapper.toUserSummaryDTO(users);
    }

    @Override
    public UserDTO getUserById(UUID userId) {
        log.info("[UserFacadeImpl - getUserByEmail] - calling user repo to find user by email");
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isPresent()) {

            log.info("[UserFacadeImpl - getUserByEmail] - user is found. calling mapper to convert user -> toDTO");
            return userMapper.toDto(foundUser.get());
        }
        log.info("[UserFacadeImpl - getUserByEmail] - user not found. throwing user not found exception");
        throw new UserNotFoundException(ExceptionReason.USER_NOT_FOUND_BY_ID.getValue());
    }

    public void deleteTask(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDTO getUserByEmail(String userEmail) {
        log.info("[UserFacadeImpl - findUserByEmail] - calling user repo to fetch user by email");
        Optional<User> foundUser = userRepository.findByEmail(userEmail);
        if (foundUser.isPresent()) {
            log.info("[UserFacadeImpl - findUserByEmail] - user found. returns a userDTO");
            return userMapper.toDto(foundUser.get());
        }
        log.info("[UserFacadeImpl - findUserByEmail] - user NOT found . throws USER_NOT_FOUND exception");
        throw new UserNotFoundException(ExceptionReason.USER_NOT_FOUND_BY_EMAIL.getValue());
    }

    public List<OrganisationDTO> getOrganisationsByUserId(UUID userId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(ExceptionReason.USER_NOT_FOUND_BY_ID.getValue()));
        return userDTOConverter.toDTO(userRepository.findOrganisationsByUserId(userId));
    }

    @Override
    public boolean existById(UUID userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.isPresent();
    }

    @Override
    public boolean userExistInProject(UUID userId, UUID projectId) {
        return userRepository.userMemberOfProject(userId, projectId);
    }

    @Override
    public boolean userExistInOrganisation(UUID userId, UUID organisationId) {
        return  userRepository.userMemberOfOrganisation(userId, organisationId);
    }
}








