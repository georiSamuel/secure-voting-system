package sistema.votacao.Voto.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sistema.votacao.Voto.DTO.VotoRequestDTO;
import sistema.votacao.Voto.DTO.VotoResponseDTO;
import sistema.votacao.Voto.Service.VotoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/votos")
public class VotoController {

    @Autowired
    private VotoService votoservice;

    @PostMapping
    public ResponseEntity<VotoResponseDTO> votar(@Valid @RequestBody VotoRequestDTO dto) {

        VotoResponseDTO response = votoservice.registrarVoto(dto);
        return ResponseEntity.ok(response);
    }
}
