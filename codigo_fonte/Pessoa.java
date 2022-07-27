import java.io.Serializable;

public class Pessoa implements Serializable {
    private String nome;
    private int nif;

    public Pessoa(){
        this.nome = "";
        this.nif = 0;
    }

    public Pessoa(String nome, int nif){
        this.nome = nome;
        this.nif = nif;
    }

    public Pessoa(Pessoa pessoa){
        this.nome = pessoa.nome;
        this.nif = pessoa.nif;
    }

    /**
     * Devolve o nome desta pessoa.
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * Altera o nome desta pessoa.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Devolve o NIF desta pessoa.
     */
    public int getNif() {
        return this.nif;
    }

    /**
     * Altera o NIF desta pessoa.
     */
    public void setNif(int nif) {
        this.nif = nif;
    }

    @Override
    /**
     * Verifica a igualdade entre duas pessoas
     */
    public boolean equals(Object o){
        if (o == this)
            return true;
        if (o == null || this.getClass() != o.getClass()){
            return false;
        }
        Pessoa pessoa = (Pessoa) o;
        return this.nome.equals(pessoa.nome) && this.nif == pessoa.nif;
    }


    @Override
    /**
     * Devolve uma string com informação relevante sobre esta pessoa.
     */
    public String toString(){
        return String.format("{Pessoa, Nome: %s, NIF: %d}", this.nome, this.nif);
    }

    @Override
    /**
     * Copia esta pessoa.
     */
    public Pessoa clone(){
        return new Pessoa(this);
    }
}
