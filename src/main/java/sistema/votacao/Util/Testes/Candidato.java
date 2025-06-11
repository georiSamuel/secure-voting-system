package sistema.votacao.Util.Testes;

public class Candidato {

    private final String nome;
    private int votosRecebidos = 0;

    public Candidato(String nome){
        this.nome = nome;
    }
    public void votar(){
        votosRecebidos++;
    }

    public int getVotosRecebidos() {
        return votosRecebidos;
    }

    public String getNome() {
        return nome;
    }

}
