package com.example.sistema_votacao.Cadastro.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.sistema_votacao.Cadastro.Model.CadastroModel;

public interface CadastroRepository extends JpaRepository<CadastroModel, Long> {

    
}
