package com.bahadircolak.productservice.web.ScheduledJob;

import com.bahadircolak.productservice.model.Product;
import com.bahadircolak.productservice.service.ProductService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ProductCleanupScheduler {

    private final ProductService productService;

    public ProductCleanupScheduler(ProductService productService) {
        this.productService = productService;
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void cleanupProductsMarkedForDeletion() {
        LocalDateTime currentTime = LocalDateTime.now();
        List<Product> productsToDelete = productService.findProductsMarkedForDeletion(currentTime);

        for (Product product : productsToDelete) {
            productService.deleteProduct(product.getId());
        }
    }
}