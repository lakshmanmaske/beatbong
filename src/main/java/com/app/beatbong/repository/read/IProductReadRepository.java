package com.app.beatbong.repository.read;

import com.app.beatbong.model.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductReadRepository extends JpaRepository<Product, Integer> {
}
