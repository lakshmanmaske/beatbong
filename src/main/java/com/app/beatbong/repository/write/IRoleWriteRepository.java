package com.app.beatbong.repository.write;

import com.app.beatbong.model.user.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleWriteRepository extends JpaRepository<UserRole, Integer> {
}
