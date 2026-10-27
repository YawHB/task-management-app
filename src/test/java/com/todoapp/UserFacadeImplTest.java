package com.todoapp;

import com.task_management_app.api.model.UserDTO;
import com.todoapp.user.internal.mapper.UserMapper;
import com.todoapp.user.internal.model.User;
import com.todoapp.user.internal.repository.UserRepository;
import com.todoapp.user.internal.service.UserFacadeImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserFacadeImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserFacadeImpl userFacadeService;

    @Test
    public void getUserByEmail_returnsMappedUser() {
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

        UserDTO expectedUserDTO = userFacadeService.getUserByEmail("john@gmail.com");

        assertSame(expectedUserDTO, userDTO);
        verify(userRepository).findByEmail("john@gmail.com");
        verify(userMapper).toDto(user);
    }

}
