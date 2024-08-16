package com.app.beatbong.repository.write;

import com.app.beatbong.model.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IUserWriteRepository extends JpaRepository<AppUser, Integer>, JpaSpecificationExecutor<AppUser> {
}
