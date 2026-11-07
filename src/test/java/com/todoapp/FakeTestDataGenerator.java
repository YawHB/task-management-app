package com.todoapp;

import com.todoapp.project.internal.model.Project;
import com.todoapp.user.internal.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class FakeTestDataGenerator {

    private static List<String> firstNames = List.of(
            "John", "Bob", "Tom", "Michael", "David",
            "James", "Robert", "Daniel", "William", "Thomas"
    );

    private static List<String> lastNames = List.of(
            "Smith", "Johnson", "Williams", "Brown", "Jones",
            "Miller", "Davis", "Wilson", "Anderson", "Taylor"
    );

    private static List<String> projectNames = List.of(
            "Website Redesign",
            "Customer Portal",
            "Inventory Management System",
            "Payment Integration",
            "Employee Management",
            "Reporting Dashboard",
            "Mobile Application",
            "Order Processing System",
            "Document Management",
            "Task Automation Platform"
    );
/*
    private static List<UUID> projectIds = List.of(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            UUID.fromString("22222222-2222-2222-2222-222222222222"),
            UUID.fromString("33333333-3333-3333-3333-333333333333")
    );
 */


    private static List<UUID> organisationIds = List.of(
            UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"),
            UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"),
            UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc")
    );

    public static List<User> createUsers(int numberOfUsers) {
        List<User> users = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < numberOfUsers; i++) {
            User user = new User();
            String firstName = firstNames.get(random.nextInt(0, firstNames.size()));
            String lastName = lastNames.get(random.nextInt(0, firstNames.size()));
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(firstName + "." + lastName + "@example.org");
            user.setUsername(firstName + "_" + lastName);
            user.setDeleted(false);
            users.add(user);
        }

        return users;
    }

        public static List<Project> createProjects(int numberOfProjects) {
            List<Project> projects = new ArrayList<>();
            Random random = new Random();

            for (int i = 0; i < numberOfProjects; i++) {
                Project project = new Project();

                String projectName = projectNames.get(random.nextInt(0, projectNames.size()));

                project.setName(projectName);
                project.setOrganisationId(organisationIds.getFirst());

                projects.add(project);
            }

            return projects;
        }
    }

