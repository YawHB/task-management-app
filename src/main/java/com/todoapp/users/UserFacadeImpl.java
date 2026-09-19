package com.todoapp.users;

import com.task_management_app.api.model.UserDTO;
import com.todoapp.exceptions.*;
import com.task_management_app.api.model.OrganisationDTO;
import com.todoapp.organisation.OrganisationFacade;
import com.task_management_app.api.model.UserDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
 class UserFacadeImpl implements UserFacade {

    private final UserRepository userRepository;
    private final UserDTOConverter userDTOConverter;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean existById(UUID userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.isPresent();
    }

    public Optional<User> findUserById(UUID id) {
        return userRepository.findById(id);
    }

    public User addUser(User user) {
        log.info("[UserService - addUser] - calling userRepository to save user");
        return userRepository.save(user);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        log.info("[UserFacadeImpl - getUserByEmail] - calling user repo to find user by email");
      Optional<User> foundUser = userRepository.findByEmail(email);
      if(foundUser.isPresent()) {
        log.info("[UserFacadeImpl - getUserByEmail] - user is found. calling userDTOConverter to convert user toDTO");
          return userDTOConverter.toDTO(foundUser.get());
      }
        log.info("[UserFacadeImpl - getUserByEmail] - user not found. throwing user not found exception");
        throw new UserNotFoundException(ExceptionReason.USER_NOT_FOUND_BY_EMAIL.getValue());
    }

    public void deleteTask(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDTO findUserByEmail(String userEmail) {
        log.info("[UserFacadeImpl - findUserByEmail] - calling user repo to fetch user by email");
        Optional<User> foundUser = userRepository.findByEmail(userEmail);
        if(foundUser.isPresent()) {
        log.info("[UserFacadeImpl - findUserByEmail] - user found. returns a userDTO");
            return userDTOConverter.toDTO(foundUser.get());
        }
        log.info("[UserFacadeImpl - findUserByEmail] - user NOT found . throws USER_NOT_FOUND exception");
        throw new UserNotFoundException(ExceptionReason.USER_NOT_FOUND_BY_EMAIL.getValue());
    }

    public List<OrganisationDTO> getOrganisationsByUserId(UUID userId) {

      userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(ExceptionReason.USER_NOT_FOUND_BY_ID.getValue()));

        return userDTOConverter.toDTOList(userRepository.findOrganisationsByUserId(userId));
    }
}








