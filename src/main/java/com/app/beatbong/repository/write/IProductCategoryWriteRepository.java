package com.app.beatbong.repository.write;

import com.app.beatbong.model.product.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductCategoryWriteRepository extends JpaRepository<ProductCategory, Integer> {
}
