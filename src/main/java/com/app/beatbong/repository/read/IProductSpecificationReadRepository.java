package com.app.beatbong.repository.read;

import com.app.beatbong.model.product.ProductSpecification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductSpecificationReadRepository extends JpaRepository<ProductSpecification, Integer> {
}
