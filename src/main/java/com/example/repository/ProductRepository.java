package com.example.repository;

import com.example.binding.ExcelModelBinder;
import com.example.model.Product;
import com.example.model.User;
import com.example.service.ExcelService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
public class ProductRepository {

    private final ExcelService excelService;

    public ProductRepository(ExcelService excelService) {
        this.excelService = excelService;
    }

    public void saveAll(List<Product> products){
        excelService.writeToExcel(ExcelModelBinder.bindProductData(products), "product", "Product");
    }
}
