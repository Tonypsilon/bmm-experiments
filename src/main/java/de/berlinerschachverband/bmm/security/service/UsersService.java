package de.berlinerschachverband.bmm.security.service;

import de.berlinerschachverband.bmm.exceptions.UserAlreadyExistsException;
import de.berlinerschachverband.bmm.exceptions.UserDoesNotExistException;
import de.berlinerschachverband.bmm.exceptions.WrongPasswordException;
import de.berlinerschachverband.bmm.security.data.ChangePasswordData;
import de.berlinerschachverband.bmm.security.data.thymeleaf.CreateUserData;
import de.berlinerschachverband.bmm.security.data.Users;
import de.berlinerschachverband.bmm.security.data.UsersRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsersService {

    private final PasswordEncoder passwordEncoder;

    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Users getUser(String username) {
        return usersRepository.findById(username).orElseThrow(
                () -> new UserDoesNotExistException(username)
        );
    }

    /**
     * Provide a list of all users sorted alphabetically.
     * @return
     */
    public List<String> getAllUserNames() {
        return usersRepository.findAll()
                .stream()
                .map(Users::getUsername)
                .sorted()
                .toList();
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

    public void changePassword(String username, ChangePasswordData changePasswordData) {
        if(Boolean.FALSE.equals(usersRepository.existsByUsername(username))) {
            throw new UserDoesNotExistException(username);
        }
        Users user = usersRepository.getOne(username);
        if(Boolean.FALSE.equals(passwordEncoder.matches(changePasswordData.getOldPassword(),
                user.getPassword()))) {
            throw new WrongPasswordException("old password does not match");
        }
        user.setPassword(passwordEncoder.encode(changePasswordData.getNewPassword()));
        usersRepository.saveAndFlush(user);
    }
}
