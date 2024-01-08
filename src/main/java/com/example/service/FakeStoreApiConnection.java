package com.example.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.annotation.NonNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

@Service
public class FakeStoreApiConnection <T> {

    private final WebClient.Builder webclintBuilder;

    @Value("${api.path.base}")
    private String basePath;

    public FakeStoreApiConnection(WebClient.Builder webclintBuilder) {
        this.webclintBuilder = webclintBuilder;
    }

    public List<T> getListFromApi(String endpoint, Class<T> elementType) {
        ParameterizedTypeReference<List<T>> typeReference = new ParameterizedTypeReference<>() {
            @Override
            @NonNull
            public Type getType() {
                return new ParameterizedType() {
                    @Override
                    public Type[] getActualTypeArguments() {
                        return new Type[]{elementType};
                    }

                    @Override
                    public Type getRawType() {
                        return List.class;
                    }

                    @Override
                    public Type getOwnerType() {
                        return null;
                    }
                };
            }
        };

        return webclintBuilder.baseUrl(basePath)
                .build()
                .get()
                .uri(endpoint)
                .retrieve()
                .bodyToMono(typeReference)
                .blockOptional()
                .orElseThrow();
    }
}
