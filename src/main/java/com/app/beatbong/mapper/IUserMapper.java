package com.app.beatbong.mapper;

import com.app.beatbong.model.user.AppUser;
import org.openapitools.model.SignUpResponseJSON;

public interface IUserMapper {
    public SignUpResponseJSON userSignUp(AppUser user);
}
