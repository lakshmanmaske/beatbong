package com.app.beatbong.repository.read;

import com.app.beatbong.model.user.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAddressRepository extends JpaRepository<Address, Integer> {
}
