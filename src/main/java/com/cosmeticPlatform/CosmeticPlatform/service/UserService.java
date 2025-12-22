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
}
