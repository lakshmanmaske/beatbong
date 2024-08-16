package com.app.beatbong.repository.write;

import com.app.beatbong.model.product.Mobile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMobileWriteRepository extends JpaRepository<Mobile, Integer> {
}
