package com.todoapp.organisation;

import com.task_management_app.api.model.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;


@Component
@Slf4j
@AllArgsConstructor
 class OrganisationDTOConverter {

    public Organisation toEntity(OrganisationDTO organisationDTO) {
        Organisation organisation = new Organisation();
        organisation.setName(organisationDTO.getName());

        return organisation;
    }

    public OrganisationDTO toDTO(Organisation organisation) {
        log.info("[OrganisationDTO - toDTO] - populating the organisationDTO object");

        OrganisationDTO organisationDTO = new OrganisationDTO();

        organisationDTO.setOrganisationId(organisation.getId());
        organisationDTO.setName(organisation.getName());
        return organisationDTO;
    }


    public OrganisationDTO toDTO(List<OrganisationDetailsProjection> organisationDetailsList) {
        log.info("[OrganisationDTO - toDTO] - populating the organisationDTO object");

        OrganisationDTO organisationDTO = new OrganisationDTO();
        List<OrganisationProjectDTO> projects = new ArrayList<>();
        List<OrganisationUserDTO> users = new ArrayList<>();

        organisationDTO.setOrganisationId(organisationDetailsList.getFirst().getOrganisationId());
        organisationDTO.setName(organisationDetailsList.getFirst().getOrganisationName());
        for(OrganisationDetailsProjection organisationDetail : organisationDetailsList) {

            if(projectIsUnique(projects, organisationDetail)) {
                OrganisationProjectDTO organisationProjectDTO = new OrganisationProjectDTO();
                organisationProjectDTO.setProjectId(organisationDetail.getProjectId());
                organisationProjectDTO.setProjectName(organisationDetail.getProjectName());
                organisationProjectDTO.setOrganisationId(organisationDetail.getOrganisationId());
                organisationProjectDTO.setCreatedAt(organisationDetail.getActiveSince().atOffset(ZoneOffset.UTC));
                organisationProjectDTO.setCreatedAt((organisationDetail.getActiveSince()).atOffset(ZoneOffset.UTC));

                projects.add(organisationProjectDTO);
            }

            if(userIsUnique(users, organisationDetail)) {
                OrganisationUserDTO organisationUserDTO = new OrganisationUserDTO();
                organisationUserDTO.setUserId(organisationDetail.getUserId());
                organisationUserDTO.setFirstName(organisationDetail.getFirstName());
                organisationUserDTO.setLastName(organisationDetail.getLastName());
                organisationUserDTO.setEmail(organisationDetail.getEmail());
                organisationUserDTO.setOrganisationId(organisationDetail.getOrganisationId());
                organisationUserDTO.setActiveSince(organisationDetail.getActiveSince().atOffset(ZoneOffset.UTC));


            users.add(organisationUserDTO);
            }
        }
        organisationDTO.setProjects(projects);
        organisationDTO.setUsers(users);
        return organisationDTO;
    }

    private boolean projectIsUnique(List<OrganisationProjectDTO> projects, OrganisationDetailsProjection organisationDetail) {
        log.info("INSIDE PROJECT_IS_UNIQUE " + organisationDetail.getOrganisationId());
        for(OrganisationProjectDTO p : projects) {
            if(projects.isEmpty()) return true;
           if(p.getProjectId().equals(organisationDetail.getProjectId()) ) return false;
        }
        return true;
    }

    private boolean userIsUnique(List<OrganisationUserDTO> users, OrganisationDetailsProjection organisationDetail) {
        for(OrganisationUserDTO u : users) {
            if(users.isEmpty()) return true;
            if(u.getUserId().equals(organisationDetail.getUserId())) return false;
        }
        return true;
    }
}
