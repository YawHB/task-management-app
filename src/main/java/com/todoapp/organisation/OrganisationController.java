package com.todoapp.organisation;

import com.task_management_app.api.OrganisationApi;
import com.task_management_app.api.model.OrganisationDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@AllArgsConstructor
@Slf4j
 class OrganisationController implements OrganisationApi {

    private OrganisationFacade organisationFacade;
    private OrganisationDTOConverter organisationDTOConverter;

    @Override
    public ResponseEntity<OrganisationDTO> addOrganisation(OrganisationDTO organisationDTO) {

        log.info("[OrganisationController - addOrganisation] - calling facade");
        Organisation organisation = organisationFacade.addOrganisation(organisationDTO);
        log.info("[OrganisationController - addOrganisation] - return an dto organisation to the client");
        return ResponseEntity.status(HttpStatus.CREATED).body(organisationDTOConverter.toDTO(organisation));
    }

    @Override
    public ResponseEntity<OrganisationDTO> getOrganisationById(UUID id) {
        return null;
    }
}









