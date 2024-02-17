package com.example.demo.Service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.Converter.UserConverter;
import com.example.demo.DTO.UserDto;
import com.example.demo.DTO.UserPageDto;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.Impl.UserServiceImpl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserConverter userConverter;
    private final Pageable pageable = PageRequest.of(0, 3, Direction.ASC, "uid");
    private final UserEntity userEntity = UserEntity.builder().uid(1L).build();
    private final UserDto userDto = UserDto.builder().uid(1L).username("user").email("email")
            .profilePic("img").build();
    private final UserPageDto userPageDto = UserPageDto.builder().build();


    @Test
    void findByUsername() {
        when(userRepository.findByUid(any(Long.class))).thenReturn(Optional.of(userEntity));
        when(userConverter.toDto(any(UserEntity.class))).thenReturn(userDto);

        UserDto user = userService.findByUid(1L);
        Assertions.assertThat(user).isEqualTo(userDto);
    }

    @Test
    void findByUsernameContaining() {
        when(userRepository.findByUsernameContaining(anyString(), any(Pageable.class))).thenReturn(
                new PageImpl<>(new ArrayList<>()));
        when(userConverter.toDto(ArgumentMatchers.<Page<UserEntity>>any())).thenReturn(userPageDto);

        UserPageDto result = userService.findByUsernameContaining("user", this.pageable);
        Assertions.assertThat(result).isEqualTo(userPageDto);
    }

    @Test
    void saveUser() {
        when(userConverter.toEntity(any(UserDto.class))).thenReturn(userEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(userConverter.toDto(any(UserEntity.class))).thenReturn(userDto);

        UserDto result = userService.saveUser(userDto);
        Assertions.assertThat(result).isEqualTo(userDto);

    }

    @Test
    void updateUser() {
        setUserProv();
        when(userConverter.toDto(any(UserEntity.class))).thenReturn(userDto);

        UserDto user = userService.updateUser(userDto);
        Assertions.assertThat(user).isEqualTo(userDto);
    }

    @Test
    void deleteUsers() {
        List<Long> uid = Arrays.asList(1L, 2L);
        when(userRepository.findByUid(anyLong())).thenReturn(Optional.of(userEntity));

        int count = userService.deleteUsers(uid);
        Assertions.assertThat(count).isEqualTo(2);
    }

    @Test
    void deleteUser() {
        setUserProv();
        // 메소드 동작 x
        doNothing().when(userRepository).delete(any(UserEntity.class));

        Long result = userService.deleteUser();
        Assertions.assertThat(result).isEqualTo(1L);
        // 메소드 한번(times(int)) 호출 확인
        verify(userRepository, times(1)).delete(any(UserEntity.class));
    }

    private void setUserContextByUsername() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("principal", null, null));
    }

    private void setUserProv() {
        setUserContextByUsername();
        when(userRepository.findByProvider(anyString())).thenReturn(Optional.of(userEntity));
    }
}
