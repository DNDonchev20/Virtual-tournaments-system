package org.codingburgas.dndonchev20.nastolni_igri.repositories;

import org.codingburgas.dndonchev20.nastolni_igri.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
