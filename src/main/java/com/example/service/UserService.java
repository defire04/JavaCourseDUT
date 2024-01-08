package com.example.service;

import com.example.model.User;
import com.example.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final FakeStoreApiConnection<User> faceStoreApiConnect;

    private final UserRepository userRepository;

    @Value("${api.path.users}")
    private String usersPath;

    public UserService(FakeStoreApiConnection<User> faceStoreApiConnect, UserRepository userRepository) {
        this.faceStoreApiConnect = faceStoreApiConnect;
        this.userRepository = userRepository;
    }
    @PostConstruct
    public void saveToExcel() {
        List<User> usersFromApi = getUsersFromApi();
        userRepository.saveAll(usersFromApi);
    }

    public List<User> getUsersFromApi() {
        return faceStoreApiConnect.getListFromApi(usersPath, User.class);
    }
}
