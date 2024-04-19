package com.yohealth.services;

import com.yohealth.models.UserPersistence;

public class UserPersistenceService {
    private static UserPersistence userPersistence;
    private static UserPersistenceService instance;

    public static void initialize() {
        userPersistence = new UserPersistence();
        instance = new UserPersistenceService();
    }

    public UserPersistence getUserPersistence() {
        return userPersistence;
    }

    public static UserPersistenceService getInstance() {
        return instance;
    }

    
}