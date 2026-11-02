package com.todoapp;

import com.todoapp.user.internal.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class FakeTestDataGenerator {

    private static List<String> firstNames = List.of("John", "Bob", "Tom");
    private static List<String> lastNames = List.of("Smith", "Pickles", "Johnson");

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
            user.setUsername(firstName + "_user");
            user.setDeleted(false);
            users.add(user);
        }

        return users;
    }
}
