package com.example.finalproject.Storage;

public class SystemStorage {
    private static String uid;

    public static void setCurrentUID(String uid) { SystemStorage.uid = uid; }
    public static String getCurrentUID() { return SystemStorage.uid; }
}
