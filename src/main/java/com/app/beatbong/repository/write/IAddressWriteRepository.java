package com.app.beatbong.repository.write;

import com.app.beatbong.model.user.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAddressWriteRepository extends JpaRepository<Address, Integer> {
}
