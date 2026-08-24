package com.todoapp.organisation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
 interface OrganisationRepository extends JpaRepository<Organisation, UUID> {
}
