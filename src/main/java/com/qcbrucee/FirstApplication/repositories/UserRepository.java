package com.qcbrucee.FirstApplication.repositories;

import com.qcbrucee.FirstApplication.models.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    public List<User> findUserById(ObjectId _id);
    List<User> findUserByIdIn(List<String> ids);
    boolean existsById(ObjectId id);
    boolean existsByUsername(String username);
    ObjectId findUserIdByUsername(String username);
    User findByUsername(String username);
}
