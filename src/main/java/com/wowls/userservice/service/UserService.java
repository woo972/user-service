package com.wowls.userservice.service;

import com.wowls.userservice.Entity.UserEntity;
import com.wowls.userservice.dto.UserDto;
import com.wowls.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userEntity.getPassword()));

        userRepository.save(userEntity);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(userEntity -> new ModelMapper().map(userEntity, UserDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDto getUser(String userId) {
        return userRepository.findByUserId(userId)
                .map(userEntity -> new ModelMapper().map(userEntity, UserDto.class))
                .orElseThrow(() -> new IllegalArgumentException("there is no user for that id:"+userId));
    }
}
