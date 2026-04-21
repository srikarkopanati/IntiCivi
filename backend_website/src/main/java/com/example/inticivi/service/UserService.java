package com.example.inticivi.service;

import com.example.inticivi.dto.UserDto;
import com.example.inticivi.entity.Role;
import com.example.inticivi.entity.User;
import com.example.inticivi.exception.ResourceNotFoundException;
import com.example.inticivi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserDto> listAllUsers() {
        return userRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public UserDto getUserById(String id) {
        return userRepository.findById(id).map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
    }

    public User updateUserRole(String id, String roleValue) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        user.setRole(Role.valueOf(roleValue.toUpperCase()));
        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id " + id);
        }
        userRepository.deleteById(id);
    }

    private UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getRole().name());
    }
}
