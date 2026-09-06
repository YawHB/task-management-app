package com.todoapp.users;

import com.task_management_app.api.UserApi;
import com.task_management_app.api.model.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;



@RestController
@AllArgsConstructor
public class UserController implements UserApi {

    private final UserFacadeImpl userFacadeService;
    private final UserDTOConverter userDTOConverter;

    @Override
    public ResponseEntity<UserDTO> addUser(UserDTO userDTO) {

        User createdUser = userFacadeService.addUser(userDTOConverter.toEntity(userDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body( userDTOConverter.toDTO(createdUser));

    }

    @Override
    public ResponseEntity<UserDTO> getUserById(UUID id) {
        return null;
    }


   /*

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

    */
}


