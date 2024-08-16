package com.app.beatbong.repository.read;

import com.app.beatbong.model.product.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductCategoryReadRepository extends JpaRepository<ProductCategory, Integer> {
}
