package com.todoapp.organisation.internal.controller;

import com.task_management_app.api.OrganisationApi;
import com.task_management_app.api.model.AddOrganisationRequestDTO;
import com.task_management_app.api.model.OrganisationDTO;
import com.task_management_app.api.model.OrganisationDetailsDTO;
import com.task_management_app.api.model.UserDTO;
import com.todoapp.exceptionHandling.exceptions.OrganisationNotFoundException;
import com.todoapp.exceptionHandling.exceptions.UserExistsInOrganisationException;
import com.todoapp.exceptionHandling.exceptions.UserNotFoundException;
import com.todoapp.mediator.MediatorFacade;
import com.todoapp.organisation.internal.service.OrganisationFacadeImpl;
import com.todoapp.organisation.facade.OrganisationFacade;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@Slf4j
class OrganisationController implements OrganisationApi {

    private OrganisationFacadeImpl organisationService;
    private MediatorFacade mediatorFacadeImpl;

    @Override
    public ResponseEntity<OrganisationDTO> addOrganisation(AddOrganisationRequestDTO orgRequestDTO) {
        log.info("[OrganisationController - addOrganisation] - calling facade");
        OrganisationDTO organisationDTO = organisationService.addOrganisation(orgRequestDTO);
        log.info("[OrganisationController - addOrganisation] - return an dto organisation to the client");

        return ResponseEntity.status(HttpStatus.CREATED).body(organisationDTO);
    }

    @Override
    public ResponseEntity<UserDTO> addUserToOrganisation(UUID id, String userEmail) {
        log.info("[OrganisationController - addUserToOrganisation] - calling mediator to subscribe user");
        UserDTO addedUser = mediatorFacadeImpl.subscribeMemberToOrganisation(id, userEmail);
        log.info("[OrganisationController - addUserToOrganisation] - operation successful. returning 201 to client");
        return ResponseEntity.status(HttpStatus.CREATED).body(addedUser);
    }

    @Override
    public ResponseEntity<OrganisationDetailsDTO> getOrganisationById(UUID id) throws OrganisationNotFoundException {
            OrganisationDetailsDTO organisationDTO = organisationService.findOrganisationById(id);
            return ResponseEntity.ok(organisationDTO);
    }

    @Override
    public ResponseEntity<List<OrganisationDTO>> getOrganisationsByUserId(UUID userId) {
        log.info("[OrganisationController - getOrganisationsByUserId] - calling mediatorImpl");
        List<OrganisationDTO> organisations = mediatorFacadeImpl.getOrganisationsByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(organisations);
    }


}