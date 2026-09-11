package com.todoapp.users;

import com.task_management_app.api.model.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserDTOConverter {

    public UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setUserId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setProfilePicture(user.getProfilePicture());
        userDTO.setIsDeleted(user.isDeleted());
        userDTO.setUserName(user.getUsername());
        return userDTO;
    }
}