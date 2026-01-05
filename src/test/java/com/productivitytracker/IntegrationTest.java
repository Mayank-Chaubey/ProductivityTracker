package com.productivitytracker;

import com.productivitytracker.dao.UserDAO;
import com.productivitytracker.dao.TaskDAO;
import com.productivitytracker.model.Task;
import org.junit.jupiter.api.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IntegrationTest {
    static final String TEST_NAME = "Test User";
    static final String TEST_EMAIL = "testuser@example.com";
    static final String TEST_PASSWORD = "testpass123";
    static final String TEST_TASK = "Integration Test Task";
    static final String TEST_PRIORITY = "High";
    static Integer userId;

    @BeforeAll
    static void cleanUpTestUser() {
        UserDAO userDAO = new UserDAO();
        // Remove test user if exists
        try (java.sql.Connection con = com.productivitytracker.util.DBConnection.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement("DELETE FROM users WHERE email = ?")) {
            ps.setString(1, TEST_EMAIL);
            ps.executeUpdate();
        } catch (Exception ignored) {}
    }

    @Test
    @Order(1)
    void testRegisterUser() {
        UserDAO userDAO = new UserDAO();
        boolean registered = userDAO.registerUser(TEST_NAME, TEST_EMAIL, TEST_PASSWORD);
        assertTrue(registered, "User should be registered successfully");
        userId = userDAO.getUserIdByEmail(TEST_EMAIL);
        assertNotNull(userId, "User ID should be retrievable after registration");
    }

    @Test
    @Order(2)
    void testLoginUser() {
        UserDAO userDAO = new UserDAO();
        boolean valid = userDAO.validateUser(TEST_EMAIL, TEST_PASSWORD);
        assertTrue(valid, "User should be able to log in with correct credentials");
        Integer fetchedId = userDAO.getUserIdByEmail(TEST_EMAIL);
        assertEquals(userId, fetchedId, "Fetched userId should match registered userId");
    }

    @Test
    @Order(3)
    void testAddTask() {
        assertNotNull(userId, "User ID must be set from registration test");
        TaskDAO taskDAO = new TaskDAO();
        boolean added = taskDAO.addTask(userId, TEST_TASK, TEST_PRIORITY);
        assertTrue(added, "Task should be added successfully");
        List<Task> tasks = taskDAO.getTasks(userId);
        boolean found = tasks.stream().anyMatch(t -> TEST_TASK.equals(t.getName()) && TEST_PRIORITY.equals(t.getPriority()));
        assertTrue(found, "Inserted task should be found in user's task list");
    }
}