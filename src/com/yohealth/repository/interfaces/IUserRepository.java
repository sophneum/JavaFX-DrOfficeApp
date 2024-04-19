package com.yohealth.repository.interfaces;

import com.yohealth.domain.model.User;
import java.util.Collection;
import java.util.Date;

public interface IUserRepository {
    int createUser(User user);
    User getUserById(long userId);
    Collection<User> getAllUsers();
    User getUserByUsername(String username);
    Collection<User> getUsersByName(String name);
    Collection<User> getUsersByRoleID(int roleID);
    Collection<User> getUsersByNameAndRoleID(String name, int roleID);
    Collection<User> getUsersByDOB(Date dob);
    int updateUser(User user);
}
