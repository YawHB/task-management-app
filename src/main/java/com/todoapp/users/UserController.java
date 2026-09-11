package com.todoapp.users;

import com.task_management_app.api.UserApi;
import com.task_management_app.api.model.OrganisationDTO;
import com.task_management_app.api.model.UserDTO;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
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
public class UserController implements UserApi {

    private final UserFacadeImpl userFacadeService;

    @Override
    public ResponseEntity<UserDTO> addUser(UserDTO userDTO) {
        User newUser = new User();
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setEmail(userDTO.getEmail());
        newUser.setProfilePicture(userDTO.getProfilePicture());
        newUser.setDeleted(false);
        newUser.setUsername(userDTO.getUserName());

        var createdUser = userFacadeService.addUser(newUser);
        UserDTO resolvedDTO = new UserDTO();
        resolvedDTO.userId(createdUser.getId());
        resolvedDTO.firstName(createdUser.getFirstName());
        resolvedDTO.lastName(createdUser.getLastName());
        resolvedDTO.email(createdUser.getEmail());
        resolvedDTO.profilePicture(createdUser.getProfilePicture());
        resolvedDTO.isDeleted(createdUser.isDeleted());
        resolvedDTO.userName(createdUser.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(resolvedDTO);
    }

   /* @Override
    public ResponseEntity<List<OrganisationDTO>> getOrganisationByUserId(UUID userId) {
        return null;
    }
    */

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


