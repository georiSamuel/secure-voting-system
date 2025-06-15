package sistema.votacao.Voto.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sistema.votacao.OpcaoVoto.Model.OpcaoVoto;
import sistema.votacao.Usuario.Model.UsuarioModel;
import sistema.votacao.Votacao.Model.Votacao;

import java.time.LocalDateTime;

/**
 * Entidade que representa um voto individual no sistema.
 * Armazena a escolha do usuário, a qual votação pertence e um selo de
 * autenticidade (HMAC) para garantir a integridade do registro.
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


    /*
     * O selo de autenticidade (HMAC) que garante a integridade deste registro de voto.
     * É gerado a partir dos dados do voto e de uma chave secreta do servidor.
     */
    @Column(name = "voto_hmac", nullable = false)
    private String votoHmac; // CAMPO ADICIONADO PARA O HMAC

    /*
     * A opção de voto que foi escolhida.
     */
    @ManyToOne
    @JoinColumn(name = "opcaoVoto_id")
    private OpcaoVoto opcaoVoto;

    /*
     * O usuário que registrou o voto. Essencial para prevenir votos duplicados.
     */
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioModel usuario;

    /*
     * A votação à qual este voto pertence.
     */
    @ManyToOne
    @JoinColumn(name = "votacao_id")
    private Votacao votacao;

    /*
     * A data e hora exatas em que o voto foi registrado.
     */
    private LocalDateTime dataHoraVoto;
}