package com.example.sistema_votacao.Votante;

import java.util.List;
import org.springframework.stereotype.Service;

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
