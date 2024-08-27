package ru.itmentor.spring_boot_security_demo.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.itmentor.spring_boot_security_demo.entity.User;
import ru.itmentor.spring_boot_security_demo.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsers() {
        User user = new User();
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> users = userService.getAllUsers();

        assertNotNull(users);

        assertEquals(1, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void saveUser() {
        User user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword("test");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        when(passwordEncoder.encode(anyString())).thenReturn("tests");

        when(userRepository.save(any(User.class))).thenReturn(user);

        User saveUser = userService.saveUser(user);

        assertNotNull(saveUser);
        assertEquals("tests", saveUser.getPassword());
        assertNotNull(saveUser.getToken());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setId(1);
        user.setEmail("test@gmail.com");

        User userUpdate = new User();
        userUpdate.setEmail("newTest@gmail.com");
        userUpdate.setName("name");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(userUpdate.getEmail())).thenReturn(Optional.of(new User()));
        when(passwordEncoder.encode(any(String.class))).thenReturn("test");

        User result = userService.updateUser(1, userUpdate);

        assertNull(result);
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).findByEmail(userUpdate.getEmail());
    }


    @Test
    void updateUser_NotFound() {
        User userUpdate = new User();
        userUpdate.setEmail("newTest@gmail.com");

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        User result = userService.updateUser(1, userUpdate);

        assertNull(result);
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(0)).findByEmail(anyString());
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void deleteUserById() {

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        userService.deleteUserById(1);

        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(0)).deleteById(1);

    }

    @Test
    void loadUserByUsername() {
        User user = new User();
        user.setEmail("test@gmail.com");

        when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(user));

        UserDetails result = userService.loadUserByUsername("test@gmail.com");

        assertNotNull(result);
        assertEquals("test@gmail.com", result.getUsername());
        verify(userRepository, times(1)).findByEmail("test@gmail.com");
    }

    @Test
    void getById() {

        User user = new User();
        user.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User result = userService.getById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(userRepository, times(1)).findById(1);
    }
}