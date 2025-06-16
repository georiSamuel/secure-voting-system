package sistema.votacao.voto.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sistema.votacao.opcao_voto.model.OpcaoVoto;
import sistema.votacao.usuario.model.UsuarioModel;
import sistema.votacao.votacao.model.Votacao;

import java.time.LocalDateTime;

/**
 * Entidade que representa um voto individual no sistema.
 * Armazena a escolha do usuário, a qual votação pertence e um selo de
 * autenticidade (HMAC) para garantir a integridade do registro.
 * @author Lethycia
 * @author Horlan
 * @version 2.0
 * @since 29/05/2025
 */
@Entity
@Table(name = "tb_voto")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VotoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "voto_hmac", nullable = false)
    private String votoHmac; // CAMPO ADICIONADO PARA O HMAC

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