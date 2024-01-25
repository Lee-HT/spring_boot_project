package com.example.demo.Service.Impl;

import com.example.demo.Converter.UserConverter;
import com.example.demo.DTO.UserDto;
import com.example.demo.DTO.UserPageDto;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
            UserConverter userConverter) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
    }

    private String GetProvider() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }

    @Override
    public UserDto findByUid(Long uid) {
        return userConverter.toDto(
                userRepository.findByUid(uid).orElseGet(() -> UserEntity.builder().build()));
    }

    @Override
    public UserPageDto findByUsernameContaining(String username, Pageable pageable) {
        return userConverter.toDto(userRepository.findByUsernameContaining(username, pageable));
    }

    @Override
    public UserDto findByProvider() {
        UserEntity userEntity = GetUserProv().orElseGet(() -> UserEntity.builder().build());
        return userConverter.toDto(userEntity);

    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        UserEntity user = userConverter.toEntity(userDto);
        return userConverter.toDto(userRepository.save(user));
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        UserEntity userEntity = GetUserProv()
                .orElseGet(() -> UserEntity.builder().build());
        if (EqualUid(userEntity)) {
            String username =
                    userDto.getUsername() != null ? userDto.getUsername() : userEntity.getUsername();
            String email = userDto.getEmail() != null ? userDto.getEmail() : userEntity.getEmail();
            String profilePic = userDto.getProfilePic() != null ? userDto.getProfilePic()
                    : userEntity.getProfilePic();
            userEntity.updateUser(username, email, profilePic);
        }

        return userConverter.toDto(userEntity);
    }

    @Override
    public int deleteUsers(List<Long> uid) {
        try {
            List<UserEntity> userEntities = new ArrayList<>();
            for (Long i : uid
            ) {
                Optional<UserEntity> userEntity = userRepository.findByUid(i);
                userEntity.ifPresent(userEntities::add);
            }
            userRepository.deleteAll(userEntities);
            if (userEntities.size() == uid.size()) {
                return uid.size();
            }
            return 0;
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Long deleteUser() {
        Optional<UserEntity> user = GetUserProv();
        if (user.isPresent()) {
            userRepository.delete(user.get());
            return user.get().getUid();
        } else {
            return 0L;
        }
    }

    private Optional<UserEntity> GetUserProv() {
        return userRepository.findByProvider(GetProvider());
    }

    private Boolean EqualUid(UserEntity userEntity) {
        Optional<UserEntity> authUser = GetUserProv();
        return authUser.isPresent() && authUser.get() == userEntity;
    }
}
