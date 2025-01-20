package com.mindhub.ToDoList.dtos.userDTOs;

import com.mindhub.ToDoList.models.EntityUser;
import com.mindhub.ToDoList.models.enums.RoleType;

public class UserDTORequest {


    private Long id;
    private String username;
    private String email;
    private String password;
    private RoleType roleType;


    public UserDTORequest() {
    }

    public UserDTORequest(EntityUser user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.roleType = user.getRoleType();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public static EntityUser toEntity(UserDTORequest userDTORequest){
        if(userDTORequest==null){
            return null;
        }
        EntityUser user = new EntityUser();
        user.setUsername(userDTORequest.getUsername());
        user.setEmail(userDTORequest.getEmail());
        user.setPassword(userDTORequest.getPassword());
        user.setRoleType(userDTORequest.getRoleType());
        return user;
    }

}
