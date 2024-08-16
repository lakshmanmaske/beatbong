package com.app.beatbong.repository.read;

import com.app.beatbong.model.product.Mobile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMobileReadRepository extends JpaRepository<Mobile, Integer> {
}
