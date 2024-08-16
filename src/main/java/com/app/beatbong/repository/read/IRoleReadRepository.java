package com.app.beatbong.repository.read;

import com.app.beatbong.model.user.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleReadRepository extends JpaRepository<UserRole, Integer> {
}
