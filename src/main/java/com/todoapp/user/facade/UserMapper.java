package com.todoapp.user.facade;

import com.task_management_app.api.model.UserDTO;
import com.todoapp.user.internal.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "userId", source = "id")
    @Mapping(target = "userName", source = "username")
    @Mapping(target = "isDeleted", source = "deleted")
    @Mapping(target = "keycloakId", ignore = true)
    UserDTO toDto(User user);
}
