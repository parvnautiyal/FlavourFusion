package com.flavour.fusion.app.repository;

import com.flavour.fusion.app.model.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {
    Mono<User> findUserByEmail(String email);

    Mono<User> findByUsername(String username);

    Mono<Void> deleteByUsername(String username);

    @Query("{'isEnabled':true , 'roles':[\"ROLE_USER\"]}")
    Flux<User> findAllUsers(Pageable pageable);
}
