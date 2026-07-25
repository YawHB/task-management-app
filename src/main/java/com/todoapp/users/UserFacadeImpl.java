package com.todoapp.users;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
 class UserFacadeImpl implements UserFacade {

    private final UserRepository userRepository;

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
}








