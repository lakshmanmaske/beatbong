package com.app.beatbong.repository.write;

import com.app.beatbong.model.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductWriteRepository extends JpaRepository<Product, Integer> {
}
