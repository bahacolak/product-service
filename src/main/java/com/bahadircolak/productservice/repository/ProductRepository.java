package com.bahadircolak.productservice.repository;

import com.bahadircolak.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByDeletedFalse();

    Optional<Product> findByIdAndDeletedFalse(Long id);

    @Query("SELECT p FROM Product p WHERE " +
            "(:name IS NULL OR p.name LIKE %:name%) AND " +
            "(:code IS NULL OR p.code = :code) AND " +
            "(:brand IS NULL OR p.brand = :brand) AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice)")
    List<Product> findActiveProductsByNameCodeBrandAndPriceRange(@Param("name") String name,
                                                                 @Param("code") String code,
                                                                 @Param("brand") String brand,
                                                                 @Param("minPrice") Double minPrice,
                                                                 @Param("maxPrice") Double maxPrice);

}
