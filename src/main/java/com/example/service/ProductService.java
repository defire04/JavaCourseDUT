package com.example.service;

import com.example.model.Product;
import com.example.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final FakeStoreApiConnection<Product> faceStoreApiConnect;

    private final ProductRepository productRepository;

    @Value("${api.path.products}")
    private String productsPath;

    public ProductService(FakeStoreApiConnection<Product> faceStoreApiConnect, ProductRepository productRepository) {
        this.faceStoreApiConnect = faceStoreApiConnect;
        this.productRepository = productRepository;
    }

    @PostConstruct
    public void saveToExcel() {
        List<Product> usersFromApi = getProductsPathFromApi();
        productRepository.saveAll(usersFromApi);
    }

    public List<Product> getProductsPathFromApi() {
        return faceStoreApiConnect.getListFromApi(productsPath, Product.class);
    }
}
