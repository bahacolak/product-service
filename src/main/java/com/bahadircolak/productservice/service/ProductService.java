package com.bahadircolak.productservice.service;

import com.bahadircolak.productservice.model.Product;
import com.bahadircolak.productservice.repository.ProductRepository;
import com.bahadircolak.productservice.web.dto.ProductDto;
import com.bahadircolak.productservice.web.request.ProductRequest;
import io.micrometer.common.util.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(ProductRequest productRequest) {
        if (productRequest == null || StringUtils.isBlank(productRequest.getName()) || StringUtils.isBlank(productRequest.getCode())) {
            throw new IllegalArgumentException("Product, name, code, and price are required.");
        } //burada olmayacak

        Product product = new Product();
        product.setName(productRequest.getName());
        product.setCode(productRequest.getCode());
        product.setDescription(productRequest.getDescription());
        product.setBrand(productRequest.getBrand());
        product.setCurrency(productRequest.getCurrency());
        product.setPrice(productRequest.getPrice());
        product.setStock(productRequest.getStock());

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
        return productRepository.findByIdAndDeletedFalse(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found."));
    }


    public Product updateProduct(Long productId, ProductDto updatedProductDto) {
        if (StringUtils.isBlank(updatedProductDto.getName()) || StringUtils.isBlank(updatedProductDto.getCode())) {
            throw new IllegalArgumentException("Product name, code, and price are required.");
        }

        Product existingProduct = getProductById(productId);

        existingProduct.setName(updatedProductDto.getName());
        existingProduct.setCode(updatedProductDto.getCode());
        existingProduct.setDescription(updatedProductDto.getDescription());
        existingProduct.setBrand(updatedProductDto.getBrand());
        existingProduct.setCurrency(updatedProductDto.getCurrency());
        existingProduct.setPrice(updatedProductDto.getPrice());
        existingProduct.setStock(updatedProductDto.getStock());

        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long productId) {
        Product productToDelete = getProductById(productId);
        productToDelete.setDeleted(true);
        productRepository.save(productToDelete);
    }

    public List<Product> searchProducts(String name, String code, String brand, Double minPrice, Double maxPrice) {
        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new IllegalArgumentException("Minimum price cannot be greater than maximum price.");
        }//burada olmayacak
        return productRepository.findActiveProductsByNameCodeBrandAndPriceRange(name, code, brand, minPrice, maxPrice);
    }

    public List<Product> findProductsMarkedForDeletion(LocalDateTime currentTime) {
        return productRepository.findProductsByDeletionTimeBefore(currentTime);
    }

}
