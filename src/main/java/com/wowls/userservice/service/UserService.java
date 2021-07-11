package com.wowls.userservice.service;

import com.wowls.userservice.Entity.UserEntity;
import com.wowls.userservice.client.OrderServiceClient;
import com.wowls.userservice.dto.UserDto;
import com.wowls.userservice.error.FeignErrorDecoder;
import com.wowls.userservice.repository.UserRepository;
import com.wowls.userservice.vo.ResponseOrder;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Slf4j
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final OrderServiceClient orderServiceClient;
    private final FeignErrorDecoder feignErrorDecoder;
//    private final Environment environment;
//    private final RestTemplate restTemplate;

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

        UserDto userDto = userRepository.findByUserId(userId)
                .map(userEntity -> new ModelMapper().map(userEntity, UserDto.class))
                .orElseThrow(() -> new IllegalArgumentException("there is no user for that id:"+userId));
//        try {
            List<ResponseOrder> orders = orderServiceClient.getOrders(userId);
            userDto.setOrders(orders);
//        } catch (FeignException.FeignClientException e) {
//            log.error(e.getMessage());
//        }
//        String orderUrl = String.format(environment.getProperty("order_service.url"), userId);
//        ResponseEntity<List<ResponseOrder>> responseEntity =
//                restTemplate.exchange(
//                        orderUrl,
//                        HttpMethod.GET,
//                        null,
//                        new ParameterizedTypeReference<List<ResponseOrder>>() {
//        });
//        userDto.setOrders(responseEntity.getBody());
        return userDto;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if(userEntity == null) throw new UsernameNotFoundException(email);
        return new User(userEntity.getEmail(),
                userEntity.getEncryptedPassword(),
                true,
                true,
                true,
                true,
                new ArrayList<>()); // 권한
    }

    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
        return userDto;
    }
}
