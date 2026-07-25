package com.todoapp.users;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/*TODO
 - getUserById✅
 - addUser✅
 - updateUser
 - deleteUser✅
 */

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserFacadeImpl userFacadeService;

    @GetMapping("")
    public ResponseEntity<List<User>> getAllUsers() {
       return ResponseEntity.ok(userFacadeService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findUserById(@PathVariable UUID id){
       Optional<User> existingUser = userFacadeService.findUserById(id);
       if(existingUser.isPresent()) {
           return ResponseEntity.ok().body(existingUser.get());
       }
       return ResponseEntity.notFound().build();
    }

    @PostMapping("")
    public ResponseEntity<User> addUser(@NonNull @RequestBody User user) {
         User newUser = userFacadeService.addUser(user);
        return ResponseEntity.ok(newUser);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable @NonNull UUID id, @RequestBody User updatedUser) {
            if(userFacadeService.findUserById(id).isEmpty()) {
                throw new RuntimeException("User object not found");
            }
           return userFacadeService.addUser(updatedUser);
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> deleteUser(@PathVariable @NonNull UUID id){
        Optional<User> existingUser = userFacadeService.findUserById(id);
        if(existingUser.isPresent()) {
            userFacadeService.deleteTask(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}


