package com.todoapp.organisation;

import jakarta.validation.Valid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.UUID;

@Repository
 interface OrganisationRepository extends JpaRepository<Organisation, UUID> {


 @Modifying
 @Query(value = "INSERT INTO organisations_users (active_since, id, organisation_id, user_id) " +
                " VALUES (:activeSince, :id, :organisationId, :userId)", nativeQuery = true)
 void addUserToOrganisation(LocalDateTime activeSince, UUID id, UUID organisationId, @Valid UUID userId);

 //boolean userExistInOrganisation(UUID orgId, UUID userId);  //TODO - need custom query to find if a user already exist in organisations_users
}
