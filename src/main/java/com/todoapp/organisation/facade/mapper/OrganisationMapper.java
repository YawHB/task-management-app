package com.todoapp.organisation.facade.mapper;

import com.task_management_app.api.model.*;
import com.todoapp.organisation.facade.OrganisationDetailsProjection;
import com.todoapp.organisation.facade.OrganisationProjectProjection;
import com.todoapp.organisation.internal.model.Organisation;
import com.todoapp.user.facade.UserSummaryProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring",
unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrganisationMapper {



    @Mapping(target = "name", source = "organisationName")
    Organisation toEntity(AddOrganisationRequestDTO orgRequestDTO);

    @Mapping(target = "organisationId", source = "id")
    OrganisationDTO toDTO(Organisation organisation);


    OrganisationDetailsDTO toOrganisationDetailsDTO(OrganisationDetailsProjection projection);

    OrganisationProjectDTO toOrganisationProjectDTO(OrganisationDetailsProjection projection);

    OrganisationUserDTO toOrganisationUserDTO(OrganisationDetailsProjection projection);

    default OffsetDateTime toUtcOffsetDateTimeOrNull(LocalDateTime time) {
        return time != null
                ? time.atOffset(ZoneOffset.UTC)
                : null;
    }


    default OrganisationDetailsDTO toDTO(List<OrganisationDetailsProjection> projections) {

        OrganisationDetailsProjection organisation = projections.getFirst();
        OrganisationDetailsDTO organisationDTO = toOrganisationDetailsDTO(organisation);
        organisationDTO.setOrganisationId(organisation.getOrganisationId());
        organisationDTO.name(organisation.getOrganisationName());


        LinkedHashMap<UUID, OrganisationProjectDTO> projects = new LinkedHashMap<>();
        LinkedHashMap<UUID, OrganisationUserDTO> users = new LinkedHashMap<>();

        for(OrganisationDetailsProjection projection : projections ) {
            if(projection.getProjectId() != null || projections.isEmpty()) {
                projects.putIfAbsent(
                        projection.getProjectId(),
                        toOrganisationProjectDTO(projection));
            }
        }

        for(OrganisationDetailsProjection projection : projections) {
            if(projection.getUserId() != null || projections.isEmpty()) {
                users.putIfAbsent(
                        projection.getUserId(),
                        toOrganisationUserDTO(projection));
            }
        }

        organisationDTO.setProjects(new LinkedList<>(projects.values()));
        organisationDTO.setUsers(new LinkedList<>(users.values()));

        return organisationDTO;
    }


}


