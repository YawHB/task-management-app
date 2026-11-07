package com.todoapp;

import com.todoapp.organisation.internal.model.Organisation;
import com.todoapp.organisation.internal.repository.OrganisationRepository;
import com.todoapp.project.internal.model.Project;
import com.todoapp.project.internal.repository.ProjectRepository;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties = {
        "spring.flyway.enabled=false",
        "spring.jpa.hibernate.ddl-auto=create-drop",
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
    @EntityScan(basePackageClasses = {
            User.class,
            Organisation.class,
            Project.class})

    @EnableJpaRepositories(basePackageClasses = {
            UserRepository.class,
            OrganisationRepository.class,
            ProjectRepository.class})
    static class JpaOnlyConfig {
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private OrganisationRepository organisationRepository;

    @Autowired
    private EntityManager entityManager;

    private static final String CREATE_PROJECTS_USERS_TABLE = """
        CREATE TABLE IF NOT EXISTS projects_users (
            id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
            user_id UUID NOT NULL,
            project_id UUID NOT NULL,
            CONSTRAINT fk_projects_users_user
                FOREIGN KEY (user_id)
                REFERENCES users(id)
                ON DELETE CASCADE,
            CONSTRAINT fk_projects_users_project
                FOREIGN KEY (project_id)
                REFERENCES project(id)
                ON DELETE CASCADE,
            CONSTRAINT uk_projects_users
                UNIQUE (project_id, user_id)
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

        // ASSERT
        assertTrue(actualUserObj.isPresent());
        assertEquals(expectedUser, actualUserObj.get());
    }

    @Test
    public void findByEmail_returnsEmptyWhenEmailDoesNotExists() {
        //ARRANGE
        String nonExistingEmail = "nonExistingEmail@mail.com";
        //ACT
        Optional<User> user = userRepository.findByEmail(nonExistingEmail);
        //ASSERT
        assertTrue(user.isEmpty());
    }

    @Test
    public void findMembersInProject_returnsMemberWhenOneMemberExist() {
        // ARRANGE
        // Create join table projects_users
        entityManager.createNativeQuery(CREATE_PROJECTS_USERS_TABLE).executeUpdate();

        // create project
        Project project = new Project();
        project.setName("Integration Test Project");
        Project expectedProject = projectRepository.saveAndFlush(project);


        // create users
        User user = FakeTestDataGenerator.createUsers(1).getFirst();
        User expectedUser = userRepository.saveAndFlush(user);

        // insert into project_users table
        entityManager.createNativeQuery("insert into projects_users(project_id, user_id)" +
                        "values(:projectId, :userId)")
                .setParameter("projectId", expectedProject.getId())
                .setParameter("userId", expectedUser.getId())
                .executeUpdate();

        entityManager.flush();
        entityManager.clear();

        //ACT
        List<UserSummaryProjection> actualMembersInProject = userRepository.findMembersInProject(expectedProject.getId());

        //ASSERT
        assertEquals(1, actualMembersInProject.size());

        UserSummaryProjection actualUser = actualMembersInProject.getFirst();

        //assertEquals(expectedUser.getId(), actualUser.getId());
        assertEquals(expectedUser.getFirstName(), actualUser.getFirstName());
        assertEquals(expectedUser.getLastName(), actualUser.getLastName());
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        assertEquals(expectedUser.getUsername(), actualUser.getUserName());
    }

    @Test
    public void findMembersInProject_returnsEmptyListWhenNoMembersExist(){
        //ARRANGE
        //no arrange
        //ACT
        List<UserSummaryProjection> actualMembersInProject = userRepository.findMembersInProject(UUID.randomUUID());
        //ASSERT
        assertTrue(actualMembersInProject.isEmpty());
    }

    @Test
    public void findMembersInProject_returnsAllMembersWhenMultipleMembersExist() {
        //ARRANGE
        entityManager.createNativeQuery(CREATE_PROJECTS_USERS_TABLE).executeUpdate();

        // create a project
        Project project = FakeTestDataGenerator.createProjects(1).getFirst();
        Project expectedProject = projectRepository.saveAndFlush(project);

        //create 2 users
        List<User> expectedUsers = FakeTestDataGenerator.createUsers(2);
        expectedUsers = userRepository.saveAllAndFlush(expectedUsers);


        for(User user : expectedUsers) {
            entityManager.createNativeQuery(
                    "INSERT INTO projects_users(project_id, user_id)" +
                    "VALUES(:projectId, :userId)")
                    .setParameter("projectId", expectedProject.getId())
                    .setParameter("userId", user.getId())
                    .executeUpdate();
        }
        entityManager.flush();
        entityManager.clear();

        //ACT
        List<UserSummaryProjection> actualUsers = userRepository.findMembersInProject(project.getId());

        //ASSERT
        assertEquals(2, actualUsers.size());
    }


    /*
    @Test
    public void findMembersInOrganisation_returnsListOfUserSummaryProjections() {
        //ARRANGE
        entityManager.createNativeQuery(CREATE_ORGANISATIONS_USERS_TABLE).executeUpdate();
        User expectedUser = FakeTestDataGenerator.createUsers(1).getFirst();
        userRepository.saveAndFlush(expectedUser);

        Organisation organisation = new Organisation();
        organisation.setName(":(");

        organisationRepository.saveAndFlush(organisation);
        UUID expectedOrgId = organisation.getId();

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

     */
}
