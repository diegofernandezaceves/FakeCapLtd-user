package com.fakecap.service;

import com.fakecap.dto.UserDto;
import com.fakecap.mapper.UserMapper;
import com.fakecap.model.User;
import com.fakecap.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.java.Log;
import net.datafaker.Faker;
import org.bson.types.ObjectId;

import java.math.BigDecimal;

@Log
@ApplicationScoped
public class UserService {

    public static final String NO_ALPHANUMERIC_REGEX = "[^a-zA-Z0-9]";
    public static final String EMPTY = "";

    private final Faker faker;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserService(Faker faker, UserMapper userMapper, UserRepository userRepository) {
        this.faker = faker;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    public UserDto createUser() {

        String name = faker.name().name();
        String email = faker.internet().emailAddress(normalizeName(name));

        UserDto userDto = new UserDto(name, email);
        User user = this.userMapper.toEntity(userDto);

        this.userRepository.persist(user);
        return this.userMapper.toDto(user);
    }

    public UserDto getUser(String userId) {
        return this.userRepository.findByIdOptional(new ObjectId(userId))
                .map(this.userMapper::toDto)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
    }

    public UserDto addBalance(String userId, BigDecimal amount) {
        User user = this.userRepository.findByIdOptional(new ObjectId(userId))
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        user.addBalance(amount);
        this.userRepository.update(user);
        return this.userMapper.toDto(user);

    }

    private static String normalizeName(String name) {
        return name.replaceAll(NO_ALPHANUMERIC_REGEX, EMPTY).toLowerCase();
    }

}
