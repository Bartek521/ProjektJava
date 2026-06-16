package com.projekt2115.app.services;

import com.projekt2115.app.models.Artist;
import com.projekt2115.app.models.User;
import com.projekt2115.app.models.UserStatus;
import com.projekt2115.app.repositories.ArtistRepository;
import com.projekt2115.app.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
            Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
            if (existingUser.isPresent()){
                throw new IllegalArgumentException("Email jest już zajęty");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if (user.getStatusUser() == null) {
            user.setStatusUser(UserStatus.BAMBIK);
        }

        return userRepository.save(user);
    }
    @Transactional
    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

}
