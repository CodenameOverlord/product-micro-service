package com.ver.product_service.service;

import com.ver.product_service.dto.ProductRequest;
import com.ver.product_service.dto.ProductResponse;
import com.ver.product_service.model.Product;
import com.ver.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor //this will create all the constructors for the product repository
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;


    public void createProduct(ProductRequest productRequest){
        Product product = Product.builder().
                description(productRequest.getDescription())
                        .name(productRequest.getName())
                                .price(productRequest.getPrice())
                                        .build();
        productRepository.save(product);
        log.info("product {} is saved", product.getId());

    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(p->mapToProductResponse(p)).collect(Collectors.toList());
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .description(product.getDescription())
                .price(product.getPrice())
                .name(product.getName())
                .id(product.getId())
                .build();
    }
}
