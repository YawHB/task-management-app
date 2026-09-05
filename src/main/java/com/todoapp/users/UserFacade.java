package com.todoapp.users;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.task_management_app.api.model.UserDTO;


public interface UserFacade {
     List<User> getAllUsers();

    Optional<User> findUserById(UUID id);


    User addUser(User user);

    void deleteTask(UUID id);

UserDTO findUserByEmail(String userEmail);
}
