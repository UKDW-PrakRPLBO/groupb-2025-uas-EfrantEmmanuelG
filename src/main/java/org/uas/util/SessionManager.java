package org.uas.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SessionManager implements Serializable {
    private static final String SESSION_FILE = "session.ser";
    private static final long serialVersionUID = 1L;

    private static SessionManager instance;
    private boolean isLoggedIn = false;

    // Private constructor to prevent instantiation
    private SessionManager() {
        // Load existing session if available
        loadSession();
    }

    // Static method to get the singleton instance
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    // Method to check if the session file doesn't exist and create it if needed
    public void createSessionFile() {
        if (!Files.exists(Paths.get(SESSION_FILE))) {
            saveSession();
        }
    }

    private void loadSession() {
        try (ObjectInputStream ois = new ObjectInputStream(
                Files.newInputStream(Paths.get(SESSION_FILE)))) {
            SessionManager loadedSession = (SessionManager) ois.readObject();
            this.isLoggedIn = loadedSession.isLoggedIn;
        } catch (IOException | ClassNotFoundException e) {
            // If file doesn't exist or error occurs, start with default values
            this.isLoggedIn = false;
        }
    }

    private void saveSession() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                Files.newOutputStream(Paths.get(SESSION_FILE)))) {
            oos.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to check if user is logged in
    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    // Method to simulate login
    public void login() {
        this.isLoggedIn = true;
        saveSession();
    }

    // Method to simulate logout
    public void logout() {
        this.isLoggedIn = false;
        saveSession();
    }

    // For testing purposes, you might want to add this method
    public void clearSession() {
        try {
            Files.deleteIfExists(Paths.get(SESSION_FILE));
            this.isLoggedIn = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}