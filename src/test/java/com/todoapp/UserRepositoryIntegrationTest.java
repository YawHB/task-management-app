package com.todoapp;

import com.todoapp.organisation.internal.model.Organisation;
import com.todoapp.organisation.internal.repository.OrganisationRepository;
import com.todoapp.user.facade.UserSummaryProjection;
import com.todoapp.user.internal.model.User;
import com.todoapp.user.internal.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties = {
        "spring.flyway.enabled=false",
        "spring.jpa.hibernate.ddl-auto=update",
        "spring.datasource.url=jdbc:h2:file:./h2-test-db",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password="
})
@ContextConfiguration(classes =
        UserRepositoryIntegrationTest.JpaOnlyConfig.class)
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
public class UserRepositoryIntegrationTest {

    @SpringBootConfiguration
    @EnableAutoConfiguration
    @EntityScan(basePackageClasses = {User.class, Organisation.class})
    @EnableJpaRepositories(basePackageClasses = {UserRepository.class, OrganisationRepository.class})
    static class JpaOnlyConfig {
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganisationRepository organisationRepository;

    @Autowired
    private EntityManager entityManager;

    private static final String CREATE_JOIN_TABLE = """
            CREATE TABLE IF NOT EXISTS projects_users (
                id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
                user_id UUID NOT NULL,
                project_id UUID NOT NULL,
                CONSTRAINT fk_projects_users_user
                    FOREIGN KEY (user_id)
                    REFERENCES users(id)
                    ON DELETE CASCADE
            )
            """;

    private static final String CREATE_ORGANISATIONS_USERS_TABLE = """
    CREATE TABLE IF NOT EXISTS ORGANISATIONS_USERS (
        id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
        user_id UUID NOT NULL,
        organisation_id UUID NOT NULL,
        active_since TIMESTAMP NOT NULL DEFAULT NOW(),
        CONSTRAINT fk_organisation_users_id 
            FOREIGN KEY (user_id) 
            REFERENCES USERS (id) 
            ON DELETE CASCADE,
        CONSTRAINT fk_organisation_id 
            FOREIGN KEY (organisation_id) 
            REFERENCES ORGANISATION (id) 
            ON DELETE CASCADE
    )
""";



    private User createUser() {
        User user = new User();
        user.setFirstName("Ada");
        user.setLastName("Lovelace");
        user.setEmail("ada.integration@example.org");
        user.setUsername("ada_integration");
        user.setDeleted(false);
        return user;
    }

    @Test
    public void findByEmail_returnsExistingUserByEmail() {
        // ARRANGE
        User user = FakeTestDataGenerator.createUsers(1).getFirst();

        User expectedUser = userRepository.saveAndFlush(user);
        entityManager.clear();

        // ACT
        Optional<User> actualUserObj = userRepository.findByEmail(user.getEmail());
        User actualUser = actualUserObj.get();

        // ASSERT
        assertTrue(actualUserObj.isPresent());
        assertNotNull(actualUser);
        assertEquals(actualUser, expectedUser);
    }

    @Test
    public void findMembersInProject_returnsListOfUserSummeryProjections() {
        // ARRANGE

        // Create join table projecT_users
        entityManager.createNativeQuery(CREATE_JOIN_TABLE).executeUpdate();

        // create project and user IDs
        UUID randomProjId = UUID.randomUUID();

        // create users
        User user = FakeTestDataGenerator.createUsers(1).getFirst();
        User expectedUser = userRepository.saveAndFlush(user);

        // insert into project_users table
        entityManager.createNativeQuery("insert into projects_users(project_id, user_id)" +
                        "values(:projectId, :userId)")
                .setParameter("projectId", randomProjId)
                .setParameter("userId", user.getId())
                .executeUpdate();

        entityManager.flush();
        entityManager.clear();

        //ACT
        List<UserSummaryProjection> actualMembersInProject = userRepository.findMembersInProject(randomProjId);

        //ASSERT
        assertEquals(1, actualMembersInProject.size());
//        assertEquals(user.getId(), actualMembersInProject.getFirst().getId());
        assertEquals(user.getFirstName(), actualMembersInProject.getFirst().getFirstName());
        assertEquals(user.getLastName(), actualMembersInProject.getFirst().getLastName());

    }

    @Test
    public void findMembersInOrganisation_returnsListOfUserSummaryProjections() {
        //ARRANGE
        entityManager.createNativeQuery(CREATE_ORGANISATIONS_USERS_TABLE).executeUpdate();
        User expectedUser = FakeTestDataGenerator.createUsers(1).getFirst();

        Organisation organisation = new Organisation();
        organisation.setName("BULLSHIT");
        UUID expectedOrgId = organisation.getId();

        organisationRepository.saveAndFlush(organisation);

        //MOCK

        entityManager.createNativeQuery("INSERT INTO organisations_users(organisation_id, user_id) "+
        "values (:organisationId, :userId)").setParameter("organisationId",expectedOrgId).
                setParameter("userId", expectedUser.getId()).executeUpdate();
        entityManager.flush();
        entityManager.clear();

        //ACT
        List<UserSummaryProjection> actualUsers = userRepository.findMembersInOrganisation(expectedOrgId);

        //ASSERT
        assertEquals(expectedUser.getEmail() ,actualUsers.getFirst().getEmail());
    }
}
