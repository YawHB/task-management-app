package com.todoapp.organisation.facade.mapper;

import com.task_management_app.api.model.*;
import com.todoapp.organisation.facade.OrganisationDetailsProjection;
import com.todoapp.organisation.internal.model.Organisation;
import com.todoapp.user.facade.OrganisationProjection;
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

    @Mapping(target = "name", source = "organisationName")
    OrganisationDTO toOrganisationDTO(OrganisationProjection projection);

    List<OrganisationDTO> toOrganisationDTO(List<OrganisationProjection> projections);

    OrganisationDetailsDTO toOrganisationDetailsDTO(OrganisationDetailsProjection projection);

    OrganisationProjectDTO toOrganisationProjectDTO(OrganisationDetailsProjection projection);

    OrganisationUserDTO toOrganisationUserDTO(OrganisationDetailsProjection projection);

    default OffsetDateTime toUtcOffsetDateTimeOrNull(LocalDateTime time) {
        return time != null
                ? time.atOffset(ZoneOffset.UTC)
                : null;
    }

    default OrganisationDetailsDTO toDTO(List<OrganisationDetailsProjection> projections) {

        // Get first project entry from db
        OrganisationDetailsProjection organisation = projections.getFirst();
        //Convert it to a DTO
        OrganisationDetailsDTO organisationDTO = toOrganisationDetailsDTO(organisation);
        //Set the organisation id and name
        organisationDTO.setOrganisationId(organisation.getOrganisationId());
        organisationDTO.name(organisation.getOrganisationName());

        //Prepare the project and user list for OrganisationDTO
        LinkedHashMap<UUID, OrganisationProjectDTO> projects = new LinkedHashMap<>();
        LinkedHashMap<UUID, OrganisationUserDTO> users = new LinkedHashMap<>();

        //Add only unique projects to project list
        for(OrganisationDetailsProjection projection : projections ) {
            if(projection.getProjectId() != null || projections.isEmpty()) {
                projects.putIfAbsent(
                        projection.getProjectId(),
                        toOrganisationProjectDTO(projection));
            }
        }
        //Add only unique users to user list
        for(OrganisationDetailsProjection projection : projections) {
            if(projection.getUserId() != null || projections.isEmpty()) {
                users.putIfAbsent(
                        projection.getUserId(),
                        toOrganisationUserDTO(projection));
            }
        }
        //Add project and user list to organisationDTO (this gives us the nested object structure)
        organisationDTO.setProjects(new LinkedList<>(projects.values()));
        organisationDTO.setUsers(new LinkedList<>(users.values()));

        return organisationDTO;
    }


}


