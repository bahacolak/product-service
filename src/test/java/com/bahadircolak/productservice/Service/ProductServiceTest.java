package com.bahadircolak.productservice.Service;

import com.bahadircolak.productservice.model.Product;
import com.bahadircolak.productservice.repository.ProductRepository;
import com.bahadircolak.productservice.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDeleteProduct() {
        Long productId = 1L;
        Product productToDelete = new Product();
        productToDelete.setId(productId);
        productToDelete.setDeleted(false);

        when(productService.getProductById(productId)).thenReturn(productToDelete);

        productService.deleteProduct(productId);

        assertTrue(productToDelete.isDeleted());

        verify(productRepository, times(1)).save(productToDelete);
    }


    @Test
    public void testGetProductByIdNotFound() {
        Long productId = 1L;
        when(productRepository.findByIdAndDeletedFalse(productId)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> productService.getProductById(productId));
    }


    @Test
    public void testSearchProducts() {
        String name = "Test";
        String code = "TEST";
        String brand = "Brand";
        Double minPrice = 10.0;
        Double maxPrice = 50.0;
        List<Product> mockProducts = new ArrayList<>();
        when(productRepository.findActiveProductsByNameCodeBrandAndPriceRange(name, code, brand, minPrice, maxPrice)).thenReturn(mockProducts);

        List<Product> products = productService.searchProducts(name, code, brand, minPrice, maxPrice);

        assertNotNull(products);
    }

}

