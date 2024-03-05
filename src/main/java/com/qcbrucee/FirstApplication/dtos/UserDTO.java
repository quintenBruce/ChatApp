package com.qcbrucee.FirstApplication.dtos;

import com.qcbrucee.FirstApplication.models.User;
import org.bson.types.ObjectId;

public class UserDTO {
    private String id;
    private String name;
    private String userName;

    public UserDTO() {
    }

    public UserDTO(String Id, String name, String userName) {
        this.id = Id;
        this.name = name;
        this.userName = userName;
    }

    public UserDTO(User user) {
        this.id = user.getId().toString();
        this.name = user.getName();
        this.userName = user.getUsername();
    }

    public static User DTOToUser(UserDTO userDTO) {
        if (userDTO.getId().isEmpty() || userDTO.getId().isBlank() || userDTO.getId().equals("") || userDTO.getId().length() != 24) {
            return new User(null, userDTO.name, userDTO.userName);
        }
        return new User(new ObjectId(userDTO.id), userDTO.name, userDTO.userName);
    }


    public static UserDTO UserToDTO(User user) {
        return new UserDTO(user.getId().toString(), user.getName(), user.getUsername());
    }

    public String getId() { return id; }
    public void setId(String Id) {
        this.id = Id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
