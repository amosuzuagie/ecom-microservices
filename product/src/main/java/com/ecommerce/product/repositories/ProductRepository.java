package com.ecommerce.product.repositories;

import com.ecommerce.product.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByActiveTrue();

    @Query("""
            SELECT p FROM products p
            WHERE p.active = true AND p.stockQuantity > 0
            AND LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    List<Product> searchProduct(@Param("keyword") String keyword);
}
