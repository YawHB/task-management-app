package com.todoapp.users;

import com.task_management_app.api.model.OrganisationDTO;
import com.todoapp.exceptions.ExceptionReason;
import com.todoapp.exceptions.UserNotFoundException;
import com.todoapp.organisation.OrganisationFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
 class UserFacadeImpl implements UserFacade {

    private final UserRepository userRepository;
    private final UserDTOConverter userDTOConverter;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

     public Optional<User> findUserById(UUID id) {
        return userRepository.findById(id);
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

     public void deleteTask(UUID id) {
        userRepository.deleteById(id);
    }

    public List<OrganisationDTO> getOrganisationsByUserId(UUID userId) {

      userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(ExceptionReason.USER_NOT_FOUND_BY_ID.getValue()));

        return userDTOConverter.toDTOList(userRepository.findOrganisationsByUserId(userId));
    }
}








