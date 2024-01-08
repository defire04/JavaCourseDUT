package com.example.repository;

import com.example.binding.ExcelModelBinder;
import com.example.model.User;
import com.example.service.ExcelService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserRepository {

    private final ExcelService excelService;

    public UserRepository(ExcelService excelService) {
        this.excelService = excelService;
    }

    public void saveAll(List<User> users){
        excelService.writeToExcel(ExcelModelBinder.bindUserData(users), "user", "User");
    }
}
