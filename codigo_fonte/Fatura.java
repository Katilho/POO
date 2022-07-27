import java.io.Serializable;
import java.time.LocalDateTime;

public class Fatura implements Serializable {
    private static int last_id = 0;

    private EnergyProvider provider;
    private CasaInteligente casa;
    private LocalDateTime start;
    private LocalDateTime end;
    private double montante;
    private int id;

    public Fatura(CasaInteligente casa, LocalDateTime start, LocalDateTime end, EnergyProvider provider) {
        this.provider = provider.clone();
        this.casa = casa.clone();
        this.id = ++Fatura.last_id;
        this.montante = casa.getTotalCost(provider, end);
        this.start = start;
        this.end = end;
    }

    private Fatura(Fatura f){
        this.provider = f.provider.clone();
        this.casa = f.casa.clone();
        this.montante = f.montante;
        this.id = f.id;
        this.start = f.start;
        this.end = f.end;
    }

    /**
     * Devolve o montante constante nesta fatura.
     */
    public double getMontante(){
        return this.montante;
    }

    /**
     * Devolve o cliente constante nesta fatura.
     */
    public Pessoa getCliente(){
        return this.casa.getProprietario();
    }

    /**
     * Devolve a casa presente nesta fatura.
     */
    public CasaInteligente getCasa(){
        return this.casa.clone();
    }

    /**
     * Devolve o nome do fornecedor que emitiu esta fatura.
     */
    public String getProviderName(){
        return this.provider.getName();
    }

    /**
     * Imprime a fatura num formato legível.
     */
    public String printFatura(){
        StringBuilder sb = new StringBuilder();
        
        sb.append("----------------------------<<Fatura>>---------------------------\n");
        sb.append(String.format("%s\n\n",this.provider.getName()));
        sb.append(String.format("Fatura: F%d\n",this.id));
        sb.append(String.format("Cliente: %s\nNIF: %d\n",this.casa.getOwnerName(),this.casa.getOwnerNif()));
        sb.append(String.format("Morada: %s\n",this.casa.getMorada()));
        sb.append("Período de faturação: ");
        sb.append(this.start.toLocalDate().toString()); sb.append(" - "); sb.append(this.end.toLocalDate().toString()); sb.append("\n");
        sb.append(String.format("Montante: %.2f€\n", this.casa.getTotalCost(this.provider,this.end)));
        sb.append("-----------------------Registo de Consumos-----------------------\n");
        sb.append("Tipo de Dispositivo | ID | Consumo\n");
        for(SmartDevice dev: this.casa.getDevices()){
            sb.append(String.format("%s | %s | %f\n",dev.getClass().getName(),dev.getID(),dev.getTotalConsumption()));
        }
        sb.append("-----------------------------------------------------------------\n");

        return sb.toString();
    }

    @Override
    /**
     * Copia uma fatura.
     */
    public Fatura clone(){
        return new Fatura(this);
    }

    @Override
    /**
     * Verifica a igualdade.
     */
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Fatura f = (Fatura) o;
        return this.provider.equals(f.provider) && 
               this.casa.equals(f.casa) && 
               this.id == f.id && 
               this.start.equals(f.start) &&
               this.end.equals(f.end) && 
               this.montante == f.montante;
    }

    @Override
    /**
     * Devolve uma string com informação relevante sobre esta fatura.
     */
    public String toString(){
        return String.format("{Fatura: F%d, Provider: %s, Cliente: %s, NIF: %s, Morada: %s, Montante: %f€}",
                             this.id,this.provider.getName(),this.casa.getOwnerName(),this.casa.getOwnerNif(),this.casa.getMorada(),this.montante);
    }

}
