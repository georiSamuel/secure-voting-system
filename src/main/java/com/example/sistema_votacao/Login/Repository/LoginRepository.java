package com.example.sistema_votacao.Login.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.sistema_votacao.Login.Model.LoginModel;

public interface LoginRepository extends JpaRepository<LoginModel, Long> {
}
