package com.example.demo.Service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.demo.Converter.UserConverter;
import com.example.demo.DTO.UserDto;
import com.example.demo.DTO.UserPageDto;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Mapper.UserMapper;
import com.example.demo.Repository.UserRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.demo.Service.Impl.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserConverter userConverter;
    @InjectMocks
    private UserServiceImpl userService;
    private List<UserEntity> users = new ArrayList<>();
    private List<UserDto> userDtos = new ArrayList<>();
    private Pageable pageable = PageRequest.of(0, 3, Direction.ASC, "uid");

    public UserServiceTest() {
        for (int i = 1; i < 6; i++) {
            users.add(UserEntity.builder().uid((long) i).username("user" + i)
                    .email("email" + i + "@gmail.com").build());
        }
        for (int i = 1; i < 6; i++) {
            userDtos.add(UserDto.builder().uid((long) i).username("user" + i)
                    .email("email" + i + "@gmail.com").build());
        }
    }

    @Test
    public void findByUsername() {
        System.out.println("======== findByUsername ========");
        when(userRepository.findByUid(any(Long.class))).thenReturn(this.users.get(0));
        when(userConverter.toDto(any(UserEntity.class))).thenReturn(this.userDtos.get(0));
        UserDto user = userService.findByUid(1L);

        Assertions.assertThat(user).isEqualTo(this.userDtos.get(0));

        System.out.println(user);
    }

    @Test
    public void findByUsernameContaining() {
        System.out.println("======== findByUsernameContaining ========");
        String username = "user";
        Page<UserEntity> pages = new PageImpl<>(this.users, this.pageable, this.users.size());
        UserPageDto pageDto = UserPageDto.builder()
                .contents(new ArrayList<>(userDtos.subList(0, 3)))
                .totalPages(pages.getTotalPages()).numberOfElements(pages.getNumberOfElements())
                .sorted(pages.getSort()).size(pages.getSize()).build();
        when(userRepository.findByUsernameContaining(username, this.pageable)).thenReturn(
                pages);
        when(userConverter.toDto(pages)).thenReturn(pageDto);
        UserPageDto result = userService.findByUsernameContaining(username, this.pageable);

        Assertions.assertThat(result).isEqualTo(pageDto);

        System.out.println(result);
    }

    @Test
    public void saveUser() {
        System.out.println("======== saveUser ========");
        when(userConverter.toEntity(any(UserDto.class))).thenReturn(this.users.get(0));
        when(userRepository.save(any(UserEntity.class))).thenReturn(this.users.get(0));
        when(userConverter.toDto(any(UserEntity.class))).thenReturn(this.userDtos.get(0));
        UserDto result = userService.saveUser(this.userDtos.get(0));

        Assertions.assertThat(result).isEqualTo(this.userDtos.get(0));

        System.out.println(result);
    }

    @Test
    public void updateUser() {
        System.out.println("======== updateUser ========");
        UserDto userDto = UserDto.builder().uid(1L).email("email1@gmail.com").username("user1")
                .build();
        when(userRepository.findByUid(any(Long.class))).thenReturn(users.get(0));
        when(userConverter.toDto(any(UserEntity.class))).thenReturn(userDto);
        UserDto user = userService.updateUser(userDto);

        Assertions.assertThat(user).isEqualTo(userDto);

        System.out.println(user);
    }

    @Test
    public void deleteUser() {
        System.out.println("======== deleteUser ========");
        List<Long> uid = Arrays.asList(1L, 2L);
        for (Long i : uid) {
            when(userRepository.findByUid(i)).thenReturn(users.get(i.intValue() - 1));
        }
        int count = userService.deleteUsers(uid);

        Assertions.assertThat(count).isEqualTo(2);
    }
}
