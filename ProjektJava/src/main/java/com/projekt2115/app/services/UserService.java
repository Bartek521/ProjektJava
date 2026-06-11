package com.projekt2115.app.services;

import com.projekt2115.app.models.Role;
import com.projekt2115.app.models.User;
import com.projekt2115.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    public Optional<User> getUserById(Long id){
        return userRepository.findById(id);
    }
    public User saveUser (User user) {
        if (user.getId() == null) {
            Optional<User> existingUser = userRepository.findByEmial(user.getEmial());
            if (existingUser.isPresent()){
                throw new IllegalArgumentException("Email jest już zajęty");
            }
        }
        if(user.getRole() == Role.ARTIST) {

            if (user.getNickname() == null || user.getNickname().isBlank()) {
                throw new IllegalArgumentException("Artysta musi podaj swoją ksywkę");
            }
        }
        return userRepository.save(user);
    }

    public void deletUser(Long id){
        userRepository.deleteById(id);
    }

}
