package com.example.immo.services;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.immo.dto.responses.ResponseUserDto;
import com.example.immo.exceptions.UserNotFoundException;
import com.example.immo.models.User;
import com.example.immo.repositories.UserRepository;

import lombok.Data;

@Data
@Service
/*
 * @Service : tout comme l’annotation @Repository, c’est une spécialisation
 * de @Component. Son rôle est donc le même, mais son nom a une valeur
 * sémantique pour ceux qui lisent votre code.
 */
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public User getUser(final Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Target user cannot be found."));
        return user;
    }

    public ResponseUserDto getReturnableUser(final Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Target user cannot be found."));
        return new ResponseUserDto(user);
    }

    public User getUserByEmail(final String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Target user cannot be found."));
        return user;
    }

    public Iterable<User> getUsers() {
        Iterable<User> users = userRepository.findAll();
        if (!users.iterator().hasNext())
            throw new UserNotFoundException("No user can be found.");
        return users;
    }

    public Iterable<ResponseUserDto> getReturnableUsers() {
        Iterable<User> users = userRepository.findAll();
        if (!users.iterator().hasNext())
            throw new UserNotFoundException("No user can be found.");
        Iterable<ResponseUserDto> returnableUsers = StreamSupport.stream(users.spliterator(), false)
                .map(user -> {
                    ResponseUserDto returnableUser = new ResponseUserDto(user);
                    return returnableUser;
                })
                .collect(Collectors.toList());
        return returnableUsers;
    }

    public void deleteUser(final Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Target user cannot be deleted."));
        userRepository.delete(user);
    }

    public User saveUser(User user) {
        try {
            User savedUser = userRepository.save(user);
            return savedUser;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save message: " + e.getMessage());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not valid."));
    }

}
