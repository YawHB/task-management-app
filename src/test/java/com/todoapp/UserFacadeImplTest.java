package com.todoapp;


import com.task_management_app.api.model.UserDTO;
import com.task_management_app.api.model.UserSummaryDTO;
import com.todoapp.organisation.facade.OrganisationFacade;
import com.todoapp.organisation.internal.repository.OrganisationRepository;
import com.todoapp.organisation.internal.service.OrganisationFacadeImpl;
import com.todoapp.user.facade.UserSummaryProjection;
import com.todoapp.user.internal.mapper.UserMapper;
import com.todoapp.user.internal.model.User;
import com.todoapp.user.internal.repository.UserRepository;
import com.todoapp.user.internal.service.UserFacadeImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserFacadeImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserFacadeImpl userFacadeImpl;
    @Mock
    private OrganisationFacadeImpl organisationFacade;

    @Test
    public void getUserByEmail_returnsMappedUser() {
        //ARRANGE
        User user = new User();
        user.setEmail("john@gmail.com");
        user.setId(UUID.randomUUID());

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getId());
        userDTO.setEmail("john@gmail.com");

        // MOCKED the expected call "sequence"
        when(userRepository.findByEmail(user.getEmail())).
                thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDTO);

        //ACT
        UserDTO expectedUserDTO = userFacadeImpl.getUserByEmail("john@gmail.com");

        // ASSERT
        assertSame(expectedUserDTO, userDTO);
        verify(userRepository).findByEmail("john@gmail.com");
        verify(userMapper).toDto(user);
    }


    @Test
    public void getUsersInProject_returnsUsersInProject() {
        //ARRANGE
        UUID projectId = UUID.randomUUID();
        UserSummaryProjection user1 = mock( "users1");
        UserSummaryProjection user2 = mock( "users2");
        List<UserSummaryProjection> users = List.of(user1, user2);

        UserSummaryDTO userDTO1 = new UserSummaryDTO();
        UserSummaryDTO userDTO2 = new UserSummaryDTO();
        List<UserSummaryDTO> usersSummeryDTO = List.of(userDTO1, userDTO2);

        //MOCK
        when(userRepository.findMembersInProject(projectId)).thenReturn(users);
        when(userMapper.toUserSummaryDTO(users)).thenReturn(usersSummeryDTO);

        //ACT
        List<UserSummaryDTO> actualUsers = userFacadeImpl.getUsersInProject(projectId);

        //ASSERT
        assertSame(usersSummeryDTO, actualUsers);
        verify(userRepository).findMembersInProject(projectId);
        verify(userMapper).toUserSummaryDTO(users);
    }

    @Test public void getUsersInOrganisation_returnsUsersInOrganisation(){
        //ARRANGE
        UUID organisationId = UUID.randomUUID();
        UserSummaryProjection userProjection1 = mock("userProjection1");
        UserSummaryProjection userProjection2 = mock("userProjection12");
        List<UserSummaryProjection> userProjectionList = List.of(userProjection1, userProjection2);
        UserSummaryDTO userDTO1 =  new UserSummaryDTO();
        UserSummaryDTO userDTO2 =  new UserSummaryDTO();
        List<UserSummaryDTO> expectedUserDTOList = List.of(userDTO1, userDTO2);

        //MOCK
        when(organisationFacade.existsById(organisationId)).thenReturn(true);

        when(userRepository.findMembersInOrganisation(organisationId)).
                thenReturn(userProjectionList);
        when(userMapper.toUserSummaryDTO(userProjectionList)).
                thenReturn(expectedUserDTOList);
        //ACT
        List<UserSummaryDTO> actualUserDTOList = userFacadeImpl.getUsersInOrganisation(organisationId);

        //ASSERT
        assertSame(expectedUserDTOList, actualUserDTOList);
        verify(userRepository).findMembersInOrganisation(organisationId);
        verify(userMapper).toUserSummaryDTO(userProjectionList);
    }

    @Test
    public void getUserById_returnsUser(){
        //ARRANGE
        UUID id = UUID.randomUUID();
        UserDTO expectedUserDTO = new UserDTO();
        expectedUserDTO.setUserId(id);
        expectedUserDTO.setFirstName("John");

        User expectedUser = new User();
        expectedUser.setId(id);
        expectedUser.setFirstName(expectedUserDTO.getFirstName());

        //MOCK
        when(userRepository.findById(id)).
                thenReturn(Optional.of(expectedUser));
        when(userMapper.toDto(expectedUser)).
                thenReturn(expectedUserDTO);

        //ACT
        UserDTO actualUserDTO = userFacadeImpl.getUserById(id);

        //ASSERT
        assertSame(expectedUserDTO, actualUserDTO);
        verify(userRepository).findById(expectedUser.getId());
        verify(userMapper).toDto(expectedUser);

    }
}






