package sistema_votacao.Voto.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

import sistema_votacao.Usuario.Model.UsuarioModel;
import sistema_votacao.Votacao.Model.Votacao;

@Entity
@Table(name = "tb_voto")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VotoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String votoCriptografado;

    // @ManyToOne
    // @JoinColumn(name = "usuario_id")
    // private UsuarioModel usuariomodel; --> não é necessário, já que o voto é
    // secreto

    @ManyToOne
    @JoinColumn(name = "opcaoVoto_id")
    private OpcaoVoto opcaoVoto;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioModel usuario;

    @ManyToOne
    @JoinColumn(name = "votacao_id")
    private Votacao votacao;

    private LocalDateTime dataHoraVoto;

}
