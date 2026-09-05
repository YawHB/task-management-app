package com.todoapp.users;

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

     public Optional<User> findUserById(UUID id) {
        return userRepository.findById(id);
    }

    public User addUser(User user) {
        return userRepository.save(user);
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
        return null; //TODO Add exception handling
    }
}








