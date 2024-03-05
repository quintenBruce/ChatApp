package com.qcbrucee.FirstApplication.models;

import com.qcbrucee.FirstApplication.dtos.UserDTO;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "users")
public class User {
    @Id
    private ObjectId id;
    private String name;
    private String username;

    public User() {}

    public User(ObjectId id, String name, String username) {
        this.id = id;
        this.name = name;
        this.username = username;
    }

    public User(UserDTO userDTO) {
        this.id = (userDTO.getId() != null) ? new ObjectId(userDTO.getId()) : null;
        this.name = userDTO.getName();
        this.username = userDTO.getUserName();
    }

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}

