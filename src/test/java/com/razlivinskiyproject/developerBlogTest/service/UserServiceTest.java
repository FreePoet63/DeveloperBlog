package com.razlivinskiyproject.developerBlogTest.service;

import com.razlivinskiyproject.developerBlog.DeveloperBlogApplication;
import com.razlivinskiyproject.developerBlog.models.Role;
import com.razlivinskiyproject.developerBlog.models.User;
import com.razlivinskiyproject.developerBlog.repository.UserRepository;
import com.razlivinskiyproject.developerBlog.service.UserService;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static java.util.Optional.ofNullable;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DeveloperBlogApplication.class})
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    public void addUserTest() {
        User user = new User();
        boolean createUser = userService.createUser(user);
        assertTrue(createUser);
        assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.USER)));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void addUserFailTest() {
        User user = new User();
        user.setUsername("Senya");

        doReturn(new User())
                .when(userRepository)
                .findByUsername("Senya");

        boolean createUser = userService.createUser(user);
        assertFalse(createUser);
        verify(userRepository, times(0))
                .save(ArgumentMatchers.any(User.class));
    }

    @Test
    public void userServiceShouldRepository() {
        User user = mock(User.class);
        when(userRepository.findById(1L)).thenReturn(ofNullable(user));
        User newUser = userService.findUserById(1L);
        assertNotNull(newUser);
        assertEquals(user, newUser);
        verify(userRepository).findById(1L);
    }
}
