package sistema.votacao.util_testes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sistema.votacao.util.ReproduzirSom;

/**
 * Teste simples para Reproduzir Som
 *
 * @author Georis
 * @version 1.0
 * @since 11/06/2025
 */
public class ReproduzirSomTest {

    @Test
    @DisplayName("Teste: tocando som da ura")
    void TocarbeepUrnaTest(){

        ReproduzirSom.tocarBeepUrna("Assets/audioUrna.wav");
    }
}
