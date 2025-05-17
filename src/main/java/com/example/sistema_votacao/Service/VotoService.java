package com.example.sistema_votacao.Service;

import org.springframework.stereotype.Service;

import com.example.sistema_votacao.Model.Voto;
import com.example.sistema_votacao.Repository.VotoRepository;

import java.util.List;

@Service
public class VotoService {

    private final VotoRepository votoRepository;

    public VotoService(VotoRepository votoRepository) {
        this.votoRepository = votoRepository;
    }

    public Voto criarVoto(Voto voto) {
        return votoRepository.save(voto);
    }

    public List<Voto> buscarTodosVotos() {
        return votoRepository.findAll();
    }

    public Voto buscarPorId(Long id) {
        return votoRepository.findById(id).orElse(null);
    }

    public void deletarVoto(Long id) {
        votoRepository.deleteById(id);
    }
}
