package com.todoapp.organisation;

import com.task_management_app.api.model.*;
import com.todoapp.organisation.facade.OrganisationDetailsProjection;
import com.todoapp.organisation.internal.model.Organisation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;


@Component
@Slf4j
@AllArgsConstructor
 public class OrganisationDTOConverter {



    public OrganisationDTO toDTO(Organisation organisation) {
        log.info("[OrganisationDTO - toDTO] - populating the organisationDTO object");

        OrganisationDTO organisationDTO = new OrganisationDTO();

        organisationDTO.setOrganisationId(organisation.getId());
        organisationDTO.setName(organisation.getName());
        organisationDTO.setCreatorId(organisation.getCreatorId());
        return organisationDTO;
    }


    public OrganisationDetailsDTO toDTO(List<OrganisationDetailsProjection> organisationDetailsList) {
        log.info("[OrganisationDTO - toDTO] - populating a list organisationDTO object");

        OrganisationDetailsDTO organisationDetailsDTO = new OrganisationDetailsDTO();
        List<OrganisationProjectDTO> projects = new ArrayList<>();
        List<OrganisationUserDTO> users = new ArrayList<>();

        organisationDetailsDTO.setOrganisationId(organisationDetailsList.getFirst().getOrganisationId());
        organisationDetailsDTO.setName(organisationDetailsList.getFirst().getOrganisationName());

        if(organisationDetailsList.getFirst().getProjectId() != null) {

        for(OrganisationDetailsProjection project : organisationDetailsList) {

            if( projectIsUnique(projects, project)) {
                OrganisationProjectDTO organisationProjectDTO = new OrganisationProjectDTO();
                organisationProjectDTO.setProjectId(project.getProjectId());
                organisationProjectDTO.setProjectName(project.getProjectName());
                organisationProjectDTO.setOrganisationId(project.getOrganisationId());
                organisationProjectDTO.setCreatedAt(project.getActiveSince().atOffset(ZoneOffset.UTC));
                organisationProjectDTO.setCreatedAt((project.getActiveSince()).atOffset(ZoneOffset.UTC));

                projects.add(organisationProjectDTO);
            }
          }
        }
        if(organisationDetailsList.getFirst().getUserId() != null) {
            for(OrganisationDetailsProjection user : organisationDetailsList) {

            if(userIsUnique(users, user)) {
                OrganisationUserDTO organisationUserDTO = new OrganisationUserDTO();
                organisationUserDTO.setUserId(user.getUserId());
                organisationUserDTO.setFirstName(user.getFirstName());
                organisationUserDTO.setLastName(user.getLastName());
                organisationUserDTO.setEmail(user.getEmail());
                organisationUserDTO.setOrganisationId(user.getOrganisationId());
                organisationUserDTO.setActiveSince(user.getActiveSince().atOffset(ZoneOffset.UTC));
            users.add(organisationUserDTO);
             }
            }
        }
        organisationDetailsDTO.setProjects(projects);
        organisationDetailsDTO.setUsers(users);
        return organisationDetailsDTO;
    }

    private boolean projectIsUnique(List<OrganisationProjectDTO> projects, OrganisationDetailsProjection project) {
        log.info("INSIDE PROJECT_IS_UNIQUE " + project.getProjectId());
        for(OrganisationProjectDTO p : projects) {
           if( p.getProjectId() != null && p.getProjectId().equals(project.getProjectId()) ) return false;
        }
        return true;
    }

    private boolean userIsUnique(List<OrganisationUserDTO> users, OrganisationDetailsProjection organisationDetail) {
        for(OrganisationUserDTO u : users) {
            if(u.getUserId() != null && u.getUserId().equals(organisationDetail.getUserId())) return false;
        }
        return true;
    }
}
