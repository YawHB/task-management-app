package com.todoapp;

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

import java.util.Optional;

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
    @EntityScan(basePackageClasses = User.class)
    @EnableJpaRepositories(basePackageClasses = UserRepository.class)
    static class JpaOnlyConfig { }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void findByEmail_returnsExistingUserByEmail(){
        // ARRANGE
        User user = new User();
        user.setFirstName("Ada");
        user.setLastName("Lovelace");
        user.setEmail("ada.integration@example.org");
        user.setUsername("ada_integration");
        user.setDeleted(false);

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

}
