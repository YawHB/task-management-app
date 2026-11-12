package com.todoapp;

import com.todoapp.organisation.internal.model.Organisation;
import com.todoapp.project.internal.model.Project;
import com.todoapp.task.internal.Priority;
import com.todoapp.task.internal.Status;
import com.todoapp.task.internal.model.Task;
import com.todoapp.user.internal.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class FakeTestDataGenerator {

    //===================================================================//
    //============================= USER ================================//
    //===================================================================//

    private final static List<String> firstNames = List.of(
            "John", "Bob", "Tom", "Michael", "David",
            "James", "Robert", "Daniel", "William", "Thomas"
    );

    private final static List<String> lastNames = List.of(
            "Smith", "Johnson", "Williams", "Brown", "Jones",
            "Miller", "Davis", "Wilson", "Anderson", "Taylor"
    );

    public static List<User> createUsers(int numberOfUsers) {
        List<User> users = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < numberOfUsers; i++) {
            User user = new User();
            String firstName = firstNames.get(random.nextInt(0, firstNames.size()));
            String lastName = lastNames.get(random.nextInt(0, lastNames.size()));
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(firstName + "." + lastName + "@example.org");
            user.setUsername(firstName + "_" + lastName);
            user.setDeleted(false);

            users.add(user);
        }

        return users;
    }

    //===================================================================//
    //============================= TASK ================================//
    //===================================================================//
    private static final List<String> taskNames = List.of(
            "Implement User Authentication",
            "Design Database Schema",
            "Create Project Dashboard",
            "Implement Task Assignment",
            "Add Email Notifications"
    );

    private static final List<Status> statusList = List.of(
            Status.TODO,
            Status.IN_PROGRESS,
            Status.DONE,
            Status.ARCHIVED
    );

    private static final List<Priority> priorityList = List.of(
            Priority.LOW,
            Priority.MEDIUM,
            Priority.HIGH,
            Priority.CRITICAL
    );



    public static List<Task> createTasks(int numOfTasks){
        int maxDays = 5;
        List<Task> tasks = new ArrayList<>();
        for(int i = 0; i< numOfTasks; i++){
            Task task = new Task();
            Random random = new Random();
           task.setProjectId(UUID.fromString("bfad9468-03e6-450a-a07e-1d9504a28156"));
           task.setAssigneeId(UUID.fromString("17e56aa9-6791-4d18-a7e2-fb5d52fbb4cd"));
           task.setTaskCreatorId(UUID.fromString("17e56aa9-6791-4d18-a7e2-fb5d52fbb4cd"));
           task.setTitle(taskNames.get(i));
           task.setDescription(("This is a task description"));
           task.setCreatedAt(LocalDateTime.now().minusDays(i+1));
           task.setDueDate(LocalDate.now().plusWeeks(i+1));
           task.setUpdatedAt(LocalDateTime.now());
           task.setStatus(statusList.get(random.nextInt(0, statusList.size())));
           task.setPriority(priorityList.get(random.nextInt(0, priorityList.size())));

           tasks.add(task);
        }
        return tasks;
    }



    //===================================================================//
    //============================= PROJECT =============================//
    //===================================================================//
    private final static List<String> projectNames = List.of(
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

    private final static List<UUID> organisationIds = List.of(
            UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"),
            UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"),
            UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc")
    );

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


    //===================================================================//
    //============================= ORGANISATION ================================//
    //===================================================================//

    private static final List<String> organisationNames = List.of(
            "Nordic Software Solutions",
            "Copenhagen Business Group",
            "Scandinavian Logistics",
            "Danish Healthcare Services",
            "Global Engineering Solutions"
    );
        public static List<Organisation> createOrganisations(int numberOfOrganisations) {
        List<Organisation> organisations = new ArrayList<>();
        Random random = new Random();

        for(int i = 0; i< numberOfOrganisations; i++){
            Organisation organisation = new Organisation();
            String organisationName = organisationNames.get(random.nextInt(0, organisationNames.size()));
            organisation.setName(organisationName);
            organisations.add(organisation);
          }
        return organisations;
        }

    }

