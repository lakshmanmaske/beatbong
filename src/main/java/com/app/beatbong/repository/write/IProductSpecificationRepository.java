package com.app.beatbong.repository.write;

import com.app.beatbong.model.product.ProductSpecification;
import com.app.beatbong.model.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductSpecificationRepository extends JpaRepository<ProductSpecification, Integer> {
}
