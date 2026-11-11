package org.tucn.pt.Data;

import org.tucn.pt.Business.TasksManagement;

import java.io.*;

public class SerializationOperations {
    private static final String FILE_NAME = "tasks_data.txt";

    public static void saveData(TasksManagement system) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(FILE_NAME);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {

            objectOutputStream.writeObject(system);
            objectOutputStream.flush();

        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    public static TasksManagement loadData() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return null;
        }

        try (FileInputStream fileInputStream = new FileInputStream(file);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

            return (TasksManagement) objectInputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data: " + e.getMessage());
            return null;
        }
    }
}