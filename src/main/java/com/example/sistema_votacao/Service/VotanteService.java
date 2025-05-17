package com.example.sistema_votacao.Service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.example.sistema_votacao.Model.Votante;
import com.example.sistema_votacao.Repository.VotanteRepository;

@Service
public class VotanteService {

    private final VotanteRepository votanteRepository;

    public VotanteService(VotanteRepository votanteRepository) {
        this.votanteRepository = votanteRepository;
    }

    public Votante criarVotante(Votante votante) {
        return votanteRepository.save(votante);
    }

    public Votante buscarVotantePorCpf(String cpf) {
        return votanteRepository.findByCpf(cpf);
    }

    public void deletarVotante(Long id) {
        votanteRepository.deleteById(id);
    }

    public List<Votante> buscarTodos() {
        return votanteRepository.findAll();
    }
}
