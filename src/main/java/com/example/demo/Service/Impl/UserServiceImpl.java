package com.example.demo.Service.Impl;

import com.example.demo.Converter.UserConverter;
import com.example.demo.DTO.UserDto;
import com.example.demo.DTO.UserPageDto;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Mapper.UserMapper;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.UserService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
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
    public UserDto findByUid(Long uid) {
        return userConverter.toDto(userRepository.findByUid(uid));
    }

    @Override
    public UserPageDto findByUsernameContaining(String username, Pageable pageable) {
        return userConverter.toDto(userRepository.findByUsernameContaining(username,pageable));
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        UserEntity user = userConverter.toEntity(userDto);
        return userConverter.toDto(userRepository.save(user));
    }

    @Override
    // save 삭제 필요
    public UserDto updateUser(UserDto userDto) {
        UserEntity user = userRepository.findByUid(userDto.getUid());
        user.updateUser(userDto.getUsername(), userDto.getEmail(), user.getProfilePic());

        return userConverter.toDto(user);
    }

    @Override
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
