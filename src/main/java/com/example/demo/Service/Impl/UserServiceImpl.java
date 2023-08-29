package com.example.demo.Service.Impl;

import com.example.demo.Converter.UserConverter;
import com.example.demo.DTO.UserDto;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Mapper.UserMapper;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.UserService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserConverter userConverter;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper,
            UserConverter userConverter) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userConverter = userConverter;
    }

    @Override
    @Transactional
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public List<UserEntity> findByUsernameContaining(String username) {
        return userRepository.findByUsernameContaining(username);
    }

    @Override
    @Transactional
    public UserEntity saveUser(UserDto userDto) {
        UserEntity user = userConverter.toEntity(userDto);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public UserEntity updateUser(UserDto userDto) {
        UserEntity user = userRepository.findByUid(userDto.getUid());
        user.updateUser(userDto.getUsername(), userDto.getEmail(), user.getProfilePic());

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public int deleteUsers(List<Long> uid){
        try{
            List<UserEntity> userEntities = new ArrayList<>();
            for (Long i:uid
            ) {
                userEntities.add(userRepository.findByUid(i));
            }
            userRepository.deleteAll(userEntities);
            if(userEntities.size() == uid.size()){
                return uid.size();
            }
            return 0;
        }catch (NullPointerException e){
            e.printStackTrace();
            throw e;
        }

    }
}
