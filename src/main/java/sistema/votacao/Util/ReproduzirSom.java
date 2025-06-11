package sistema.votacao.Util;

import javax.sound.sampled.*; // Utilitárias de som
import java.io.File; // carregar o arquivo
import java.io.IOException;



/**
 * Classe utilitária para reprodução de arquivo de áudio em sistema de votação eletrônica.
 *
 * Suporta os seguintes formatos de áudio:
 * <ul>
 *   <li>.wav</li>
 *   <li>.au</li>
 *   <li>.aiff</li>
 * </ul>
 *
 * <u>Nota</u>: Formatos como .mp3 não são suportados nativamente pelo Java Sound API.
 *
 * @author Georis
 * @version 1.0
 * @since 10/06/2025
 */
public final class ReproduzirSom {


    /**
     * Método utilitário para tocar som de confirmação de voto
     * @since 10/06/25
     * @param caminhoArquivo
     */

    public static void tocarBeepUrna(String caminhoArquivo) {
        try {


            File arquivo = new File(caminhoArquivo);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(arquivo);
            Clip clip = AudioSystem.getClip(); // Tenho que usar o try e catch por causa da excessão que o próprio método lança
            clip.open(audioStream);
            clip.start(); // toca o som de fato

            // Aguarda o som terminar
            Thread.sleep(clip.getMicrosecondLength() / 1000);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException e) {
            e.printStackTrace();
        }
    }



}

