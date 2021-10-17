package de.berlinerschachverband.bmm.security.service;

import de.berlinerschachverband.bmm.exceptions.UserAlreadyExistsException;
import de.berlinerschachverband.bmm.navigation.data.CreateUserData;
import de.berlinerschachverband.bmm.security.data.Users;
import de.berlinerschachverband.bmm.security.data.UsersRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    private final PasswordEncoder passwordEncoder;

    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(CreateUserData createUserData) {
        if(Boolean.TRUE.equals(usersRepository.existsByUsername(createUserData.getUsername()))) {
            throw new UserAlreadyExistsException(createUserData.getUsername());
        }
        Users user = new Users();
        user.setUsername(createUserData.getUsername());
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(createUserData.getPassword()));
        usersRepository.saveAndFlush(user);
    }
}
