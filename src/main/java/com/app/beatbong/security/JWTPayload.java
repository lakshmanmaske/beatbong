package com.app.beatbong.security;

import com.app.beatbong.model.user.AppUser;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class JWTPayload {

    @JsonProperty("role")
    @JsonPropertyDescription("user role")
    private String role;

    @JsonProperty("username")
    @JsonPropertyDescription("username")
    private String username;

    @JsonProperty("firstname")
    @JsonPropertyDescription("first name")
    private String firstName;

    @JsonProperty("last name")
    @JsonPropertyDescription("last name")
    private String lastName;

    @Override
    public boolean equals(Object obj) {
        if(obj==null)
            return false;
        if(this==obj)
            return true;
        JWTPayload jwtPayload = (JWTPayload) obj;
        if(role!=null)
            return jwtPayload.role==null;
        return true;
    }

    @Override
    public String toString() {
        return "jwtPayload";
    }
}
