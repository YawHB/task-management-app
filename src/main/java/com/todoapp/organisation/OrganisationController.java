package com.todoapp.organisation;

import com.task_management_app.api.OrganisationApi;
import com.task_management_app.api.model.OrganisationDTO;
import com.todoapp.utils.OrganisationDTOConverter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class OrganisationController implements OrganisationApi {

    private OrganisationFacade organisationFacade;
    private OrganisationDTOConverter organisationDTOConverter;
    @Override
    public ResponseEntity<OrganisationDTO> addOrganisation(OrganisationDTO organisationDTO) {
        Organisation organisation = organisationFacade.addOrganisation(organisationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(organisationDTOConverter.toDTO(organisation));
    }

    @Override
    public ResponseEntity<OrganisationDTO> getOrganisationById(UUID id) {
        return null;
    }
}









