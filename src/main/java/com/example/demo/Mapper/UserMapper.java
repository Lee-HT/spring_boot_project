package com.example.demo.Mapper;

import com.example.demo.Domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    void insertUser(User user);

}
