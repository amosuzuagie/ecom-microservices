package com.ecommerce.product.services;

import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.models.Product;
import com.ecommerce.product.repositories.ProductRepository;
import org.antlr.v4.runtime.misc.MultiMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest request) {
        Product product = new Product();
        return mapToProductResponse(productRepository.save(mapRequestToProduct(product, request)));

    }

    public Optional<ProductResponse> updateProduct(long id, ProductRequest request) {
        return productRepository.findById(id)
                .map(product -> {
                    this.mapRequestToProduct(product, request);
                    return mapToProductResponse(productRepository.save(product));
                });
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findByActiveTrue()
                .stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    public Optional<ProductResponse> getProductById(Long productId) {
        return productRepository.findByIdAndActiveTrue(productId)
                .map(this::mapToProductResponse);
    }

    public boolean deleteProduct(long id) {
        return productRepository.findById(id)
                .map(prod -> {
                    prod.setActive(false);
                    productRepository.save(prod);
                    return true;
                }).orElse(false);
    }

    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository.searchProduct(keyword)
                .stream().map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    private ProductResponse mapToProductResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setPrice(product.getPrice());
        response.setDescription(product.getDescription());
        response.setStockQuantity(product.getStockQuantity());
        response.setCategory(product.getCategory());
        response.setImageUrl(product.getImageUrl());
        response.setActive(product.getActive());
        return response;
    }

    private Product mapRequestToProduct(Product product, ProductRequest request) {
        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setStockQuantity(request.getStockQuantity());
        product.setImageUrl(request.getImageUrl());
        return product;
    }
}
