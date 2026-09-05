package com.todoapp.organisation;

import com.task_management_app.api.OrganisationApi;
import com.task_management_app.api.model.OrganisationDTO;
import com.task_management_app.api.model.UserDTO;
import com.todoapp.exceptions.OrganisationNotFoundException;
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
        try {
            OrganisationDTO organisationDTO = organisationFacade.findOrganisationById(id);
            return ResponseEntity.ok(organisationDTO);
        } catch (OrganisationNotFoundException exception) {
            log.error("[getOrganisationById] - " + exception.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<UserDTO> addUserToOrganisation(UUID id, String userEmail) {
        try{
        log.info("[OrganisationController - addUserToOrganisation] - Calling org facade ");
         UserDTO addedUser = organisationFacade.addUserToOrganisation(id, userEmail);
         return ResponseEntity.status(HttpStatus.CREATED).body(addedUser);
        } catch (UserExistsInOrganisationException exception) {
            log.error("[addUserToOrganisation] - " +exception.getMessage() );
            return ResponseEntity.badRequest().build();
        } catch (UserNotFoundException exception) {
            log.error("[addUserToOrganisation - ] " + exception.getMessage());
            return ResponseEntity.badRequest().build();

        }
    }
}