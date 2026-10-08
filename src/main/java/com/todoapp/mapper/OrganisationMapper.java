package com.todoapp.mapper;

import com.task_management_app.api.model.AddOrganisationRequestDTO;
import com.task_management_app.api.model.OrganisationDTO;

import com.todoapp.organisation.internal.model.Organisation;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrganisationMapper {

    // Request DTO -> Entity
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name", source = "organisationName")
    Organisation toEntity(AddOrganisationRequestDTO requestDTO);


    // Entity -> Response DTO
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "organisationId", source = "id")
    @Mapping(target = "name", source = "name")
    OrganisationDTO toDTO(Organisation organisation);
}