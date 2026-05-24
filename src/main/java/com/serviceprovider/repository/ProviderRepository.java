package com.serviceprovider.repository;

import com.serviceprovider.entity.Provider;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProviderRepository extends MongoRepository<Provider, String> {

    Optional<Provider> findByCode(String code);

    boolean existsByCode(String code);
}
