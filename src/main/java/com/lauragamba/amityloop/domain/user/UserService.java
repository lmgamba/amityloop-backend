package com.lauragamba.amityloop.domain.user;

import org.springframework.stereotype.Service;

// Marks this class as a Spring-managed service (business logic layer)
// Spring creates a single instance and injects it wherever it's needed
@Service
public class UserService {

    // Spring automatically injects the repository instance here (constructor injection)
    // This is preferred over @Autowired field injection in production code
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}