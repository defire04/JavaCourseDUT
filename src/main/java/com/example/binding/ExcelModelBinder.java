package com.example.binding;


import com.example.model.Product;
import com.example.model.User;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ExcelModelBinder {


    public static Map<Integer, Object[]> bindUserData(List<User> users) {

        Map<Integer, Object[]> data = new TreeMap<>();
        int rowCont = 1;

        data.put(rowCont++, new Object[]{"ID", "Email", "Username", "Password", "First Name", "Last Name", "Phone", "City", "Street", "Number", "Zipcode", "Latitude", "Longitude", "__v"});

        for (User user : users) {
            data.put(rowCont++, new Object[]{
                    user.getId(),
                    user.getEmail(),
                    user.getUsername(),
                    user.getPassword(),
                    user.getName().getFirstname(),
                    user.getName().getLastname(),
                    user.getPhone(),
                    user.getAddress().getCity(),
                    user.getAddress().getStreet(),
                    user.getAddress().getNumber(),
                    user.getAddress().getZipcode(),
                    user.getAddress().getGeolocation().getLat(),
                    user.getAddress().getGeolocation().getLon(),
                    user.get__v()
            });
        }

        return data;
    }

    public static Map<Integer, Object[]> bindProductData(List<Product> products) {

        Map<Integer, Object[]> data = new TreeMap<>();
        int rowCont = 1;

        data.put(rowCont++, new Object[]{"ID", "Title", "Price", "Description", "Category", "Image", "Rating Rate", "Rating Count"});

        for (Product product : products) {
            data.put(rowCont++, new Object[]{
                    product.getId(),
                    product.getTitle(),
                    product.getPrice(),
                    product.getDescription(),
                    product.getCategory(),
                    product.getImage(),
                    product.getRating().getRate(),
                    product.getRating().getCount()
            });
        }
        return data;
    }
}
