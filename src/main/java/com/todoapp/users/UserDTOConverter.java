package com.todoapp.users;

import com.task_management_app.api.model.OrganisationDTO;
import com.task_management_app.api.model.UserDTO;
import com.todoapp.users.repository.OrganisationProjection;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDTOConverter {
    public User toEntity(UserDTO userDTO) {
        User newUser = new User();

        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setEmail(userDTO.getEmail());
        newUser.setProfilePicture(userDTO.getProfilePicture());
        newUser.setDeleted(false);
        newUser.setUsername(userDTO.getUserName());
        return  newUser;
    }

    public UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.userId(user.getId());
        userDTO.firstName(user.getFirstName());
        userDTO.lastName(user.getLastName());
        userDTO.email(user.getEmail());
        userDTO.profilePicture(user.getProfilePicture());
        userDTO.isDeleted(user.isDeleted());
        userDTO.userName(user.getUsername());

        return userDTO;
    }

    public List<OrganisationDTO> toDTOList(List<OrganisationProjection>  projections){
        List<OrganisationDTO> organisationDTOList = new ArrayList<>();
        for(OrganisationProjection projection : projections) {
            OrganisationDTO dto = new OrganisationDTO();
            dto.setOrganisationId(projection.getOrganisationId());
            dto.setName(projection.getOrganisationName());
            organisationDTOList.add(dto);
        }
        return organisationDTOList;
    }
}
