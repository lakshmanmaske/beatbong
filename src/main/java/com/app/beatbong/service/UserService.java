package com.app.beatbong.service;

import com.app.beatbong.exception.BeatBongErrorEnum;
import com.app.beatbong.exception.BeatBongServiceException;
import com.app.beatbong.model.user.Address;
import com.app.beatbong.model.user.AppUser;
import com.app.beatbong.model.user.UserRole;
import com.app.beatbong.repository.read.IRoleReadRepository;
import com.app.beatbong.repository.read.IUserReadRepository;
import com.app.beatbong.repository.write.IAddressWriteRepository;
import com.app.beatbong.repository.write.IUserWriteRepository;
import com.app.beatbong.security.JWTPayload;
import com.app.beatbong.security.JwtTokenizer;
import com.app.beatbong.security.utils.PasswordUtils;
import org.openapitools.model.LoginRequestJSON;
import org.openapitools.model.LoginResponseJSON;
import org.openapitools.model.SignUpRequestJSON;
import org.openapitools.model.UserInfoResponseJSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    private final IUserWriteRepository userWriteRepository;
    private final IUserReadRepository userReadRepository;
    private final IRoleReadRepository iRoleReadRepository;
    private final IAddressWriteRepository iAddressWriteRepository;
    private final JwtTokenizer jwtTokenizer;

    @Autowired
    public UserService(IUserWriteRepository userWriteRepository,
                       IUserReadRepository userReadRepository,
                       IRoleReadRepository iRoleReadRepository, IAddressWriteRepository iAddressWriteRepository, JwtTokenizer jwtTokenizer) {
        this.userWriteRepository = userWriteRepository;
        this.userReadRepository = userReadRepository;
        this.iRoleReadRepository = iRoleReadRepository;
        this.iAddressWriteRepository = iAddressWriteRepository;
        this.jwtTokenizer = jwtTokenizer;
    }

    @Transactional
    public AppUser signUp(SignUpRequestJSON signUp){
        UserRole role = iRoleReadRepository.findById(signUp.getRoleId()).get();
        AppUser user = AppUser.builder()
                .userName(signUp.getUsername())
                .firstName(signUp.getFirstName())
                .lastName(signUp.getLastName())
                .email(signUp.getEmail())
                .password(PasswordUtils.encrypt(signUp.getPassword()))
                .role(role)
                .build();
        AppUser savedUser = userWriteRepository.save(user);
        Address address = Address.builder()
                .houseNumber(signUp.getAddress().getHouseNumber())
                .street(signUp.getAddress().getStreet())
                .landmark(signUp.getAddress().getLandmark())
                .village(signUp.getAddress().getVillage())
                .city(signUp.getAddress().getCity())
                .district(signUp.getAddress().getDistrict())
                .pinCode(signUp.getAddress().getPinCode())
                .state(signUp.getAddress().getState())
                .country(signUp.getAddress().getCountry())
                .user(savedUser)
                .build();
        iAddressWriteRepository.save(address);
       return savedUser;
    }

    @Transactional
    public LoginResponseJSON login(LoginRequestJSON loginRequestJSON) {
        String password = loginRequestJSON.getPassword();
        AppUser user = userReadRepository.findByEmail(loginRequestJSON.getEmail()).orElseThrow(()->{
            throw new BeatBongServiceException("Invalid user name or password.", BeatBongErrorEnum.UNAUTHORIZED);
        });
        if(!Objects.equals(PasswordUtils.decrypt(user.getPassword()), password)){
            throw new BeatBongServiceException("Invalid user name or password.", BeatBongErrorEnum.UNAUTHORIZED);
        }
        JWTPayload payload = JWTPayload.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUserName())
                .role(user.getRole().getRoleName())
                .build();
        String token = jwtTokenizer.generateAuthonticatedToken(payload);
        return new LoginResponseJSON()
                .id(user.getUserId())
                .token(token)
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName());
    }

    public UserInfoResponseJSON getUser(Integer id) {
        Optional<AppUser> user1 = Optional.ofNullable(this.userReadRepository.findById(id).orElseThrow(() -> {
            throw new BeatBongServiceException("User not found", BeatBongErrorEnum.UNAUTHORIZED);
        }));
        AppUser user = user1.get();
        return  new UserInfoResponseJSON().id(user.getUserId()).userName(user.getUserName()).firstName(user.getFirstName()).lastName(user.getLastName()).email(user.getEmail());
    }
}
