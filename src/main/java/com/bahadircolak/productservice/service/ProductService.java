package com.bahadircolak.productservice.service;

import com.bahadircolak.productservice.model.Product;
import com.bahadircolak.productservice.repository.ProductRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(Product product) {
        if (product == null || StringUtils.isBlank(product.getName()) || StringUtils.isBlank(product.getCode())) {
            throw new IllegalArgumentException("Product, name, code, and price are required.");
        }

        try {
            return productRepository.save(product);
        } catch (Exception ex) {
            throw new RuntimeException("An error occurred while saving the product.", ex);
        }
    }




    public List<Product> getAllProducts() {
        return productRepository.findByDeletedFalse();
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found."));
    }


    public Product updateProduct(Long productId, Product updatedProduct) {
        Product existingProduct = getProductById(productId);

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setCode(updatedProduct.getCode());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setBrand(updatedProduct.getBrand());
        existingProduct.setCurrency(updatedProduct.getCurrency());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setStock(updatedProduct.getStock());

        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long productId) {
        Product productToDelete = getProductById(productId);
        productToDelete.setDeleted(true);
        productRepository.save(productToDelete);
    }

    public List<Product> searchProducts(String name, String code, String brand, Double minPrice, Double maxPrice) {

        return productRepository.findActiveProductsByNameCodeBrandAndPriceRange(name, code, brand, minPrice, maxPrice);
    }
}
