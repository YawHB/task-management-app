package com.todoapp.organisation.facade.mapper;

import com.task_management_app.api.model.AddOrganisationRequestDTO;
import com.task_management_app.api.model.OrganisationDTO;
import com.todoapp.organisation.internal.model.Organisation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrganisationMapper {



    @Mapping(target = "name", source = "organisationName")
    Organisation toEntity(AddOrganisationRequestDTO orgRequestDTO);

    @Mapping(target = "organisationId", source = "id")
    OrganisationDTO toDTO(Organisation organisation);
}
