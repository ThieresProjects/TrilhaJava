package br.com.thieres.todolist.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;


public interface IUserRepository extends JpaRepository<UserViewModel, UUID> {
    UserViewModel findByUsername(String username);
}
