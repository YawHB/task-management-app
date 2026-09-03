package com.todoapp.organisation;

import com.task_management_app.api.model.OrganisationDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
 class OrganisationFacadeImpl implements OrganisationFacade {

    private OrganisationRepository organisationRepository;
    private OrganisationDTOConverter organisationDTOConverter;

    @Override
    public Organisation addOrganisation(OrganisationDTO organisationDTO) {
        log.info("[OrganisationFacadeImpl - addOrganisation] - calling the repository to save an organisation object");
        Organisation organisation = organisationDTOConverter.toEntity(organisationDTO);
        return organisationRepository.save(organisation);
    }

    @Override
    public OrganisationDTO findOrganisationById(UUID id) {
        Optional<Organisation> organisation = organisationRepository.findById(id);
        if (organisation.isPresent()) {
            return organisationDTOConverter.toDTO(organisation.get());
        }
        throw new RuntimeException();
    }

    @Override
    public boolean existsById(UUID organisationId) {
        Optional<Organisation> exists = organisationRepository.findById(organisationId);
        return exists.isPresent();
    }
}




