package com.yohealth.repository.interfaces;

import com.yohealth.domain.model.Role;

public interface IRoleRepository { // Define interface for RoleRepository
    Role getRoleByID(long roleID);
    Role getRoleByName(String roleName);
    int createRole(Role role);

}