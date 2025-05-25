package com.example.hava101;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class UserService {
    private static final String USERS_FILE = "users.txt";

    public static void saveUser(String username, String password, String prefCountry) throws IOException {
        // Add user to file
        try (BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(USERS_FILE),
                StandardOpenOption.CREATE, // If the file doesn't exist, it will be created.
                StandardOpenOption.APPEND)) {
            writer.write(username + ":" + password + ":" + prefCountry);
            writer.newLine();
        }
    }

    public static boolean validateUser(String username, String password) throws IOException {
        // Read all users and validate
        if (!Files.exists(Paths.get(USERS_FILE))) {
            return false;
        }

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 3 && parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean userExists(String username) throws IOException {
        // Checking if the username exists or not. If username exists, it will return true.
        if (!Files.exists(Paths.get(USERS_FILE))) {
            return false;
        }

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length > 0 && parts[0].equals(username)) {
                    return true;
                }
            }
        }
        return false;
    }
}