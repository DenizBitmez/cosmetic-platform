package com.cosmeticPlatform.CosmeticPlatform.service;

import jakarta.validation.ValidationException;
import com.cosmeticPlatform.CosmeticPlatform.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.cosmeticPlatform.CosmeticPlatform.repository.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    public User addUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ValidationException("Bu email ile kayıtlı kullanıcı var.");
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
    }

    // güncelleme silme

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                getAuthorities(user));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        switch (user.getUserType()) {
            case ADMIN:
                return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
            case CLIENT:
                return List.of(new SimpleGrantedAuthority("ROLE_CLIENT"));
            case EXPERT:
                return List.of(new SimpleGrantedAuthority("ROLE_EXPERT"));
            default:
                return List.of();
        }
    }

    public void updatePassword(User user, String newPassword) {
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        userRepository.save(user);
    }

    public User processOAuthPostLogin(String email, String name) {
        var userOptional = userRepository.findByEmail(email);
        User user;

        if (userOptional.isPresent()) {
            user = userOptional.get();
            // Update existing user's name if it's different/missing
            if (name != null && !name.isEmpty() && !name.equals(user.getUsername())) {
                user.setUsername(name);
                userRepository.save(user);
            }
            return user;
        }

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setUsername(name);
        newUser.setPassword(new BCryptPasswordEncoder().encode("OAUTH2_USER")); // Placeholder password
        newUser.setUserType(com.cosmeticPlatform.CosmeticPlatform.model.UserType.CLIENT);

        return userRepository.save(newUser);
    }
}
