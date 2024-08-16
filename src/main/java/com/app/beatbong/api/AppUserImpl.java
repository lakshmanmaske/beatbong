package com.app.beatbong.api;

import com.app.beatbong.gen.json.UserApi;
import com.app.beatbong.mapper.IUserMapper;
import com.app.beatbong.model.user.AppUser;
import com.app.beatbong.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.openapitools.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Log4j2
public class AppUserImpl implements UserApi {

    private final IUserMapper userMapper;
    private final UserService userService;

    @Autowired
    public AppUserImpl(IUserMapper userMapper, UserService userService) {
        this.userMapper = userMapper;
        this.userService = userService;
    }

    @Override
    public ResponseEntity<UserInfoResponseJSON> getUser(String userId) {
        return new ResponseEntity<>(this.userService.getUser(Integer.valueOf(userId)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<LoginResponseJSON> login(@Valid @RequestBody LoginRequestJSON loginRequestJSON) {
        log.info("user login for {}", loginRequestJSON.getEmail());
        return new ResponseEntity<>(userService.login(loginRequestJSON), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SignUpResponseJSON> signUp(@Valid @RequestBody SignUpRequestJSON signUpRequestJSON) {
        AppUser user = userService.signUp(signUpRequestJSON);
        SignUpResponseJSON responseJSON = userMapper.userSignUp(user);
        return new ResponseEntity<>(responseJSON, HttpStatus.CREATED);
    }
}
