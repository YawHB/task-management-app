package com.todoapp.users;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserFacade {
     List<User> getAllUsers();

    Optional<User> findUserById(UUID id);


    User addUser(User user);


    void deleteTask(UUID id);
}
