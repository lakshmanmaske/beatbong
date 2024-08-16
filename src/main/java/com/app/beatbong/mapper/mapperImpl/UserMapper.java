package com.app.beatbong.mapper.mapperImpl;

import com.app.beatbong.mapper.IUserMapper;
import com.app.beatbong.model.user.AppUser;
import org.openapitools.model.SignUpResponseJSON;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements IUserMapper {
    @Override
    public SignUpResponseJSON userSignUp(AppUser user) {
        return new SignUpResponseJSON()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().getRoleName());
    }
}
