import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.function.Function;
public class EnergyProvider implements Serializable {
    private String name;
    private double price_kwh;
    private double tax;
    

    public EnergyProvider(){
        this.name = "";
        this.price_kwh = 0.0f;
        this.tax = 0.0f;
    }

    public EnergyProvider(String name){
        this.name = name;
        this.price_kwh = 0.15f;
        this.tax = 0.23f;
    }

    public EnergyProvider(String name, double price_kwh, double tax){
        this.name = name;
        this.price_kwh = price_kwh;
        this.tax = tax;
    }

    public EnergyProvider(EnergyProvider ep){
        this.name = ep.name;
        this.price_kwh = ep.price_kwh;
        this.tax = ep.tax;
    }

    /**
     * Devolve o nome deste fornecedor.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Altera o nome deste fornecedor.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Devolve o preco por kWh deste fornecedor.
     */
    public double getPrice_kwh() {
        return this.price_kwh;
    }

    /**
     * Altera o preco por kWh deste fornecedor.
     */
    public void setPrice_kwh(double price_kwh) {
        this.price_kwh = price_kwh;
    }

    /**
     * Devolve o imposto aplicado por este fornecedor.
     */
    public double getTax() {
        return this.tax;
    }

    /**
     * Altera o imposto aplicado por este fornecedor.
     */
    public void setTax(double tax) {
        this.tax = tax;
    }

    /**
     * Calcula o custo total da utilização de um conjunto de dispositivos de uma casa de acordo com uma fórmula standard.
     */
    public double cost(Collection<SmartDevice> devices){
        return this.cost(devices,cons -> 0.2*this.price_kwh*cons* (1 + this.tax));
    }

    /** 
     * Calcula o custo total da utilização de um conjunto de dispositivos de uma casa de acordo com uma função personalizada.
    */
    public double cost(Collection<SmartDevice> devices, Function<Double,Double> func){
        double cost_ =  func.apply(devices.stream().mapToDouble(SmartDevice::getTotalConsumption).sum());
        return Math.round((devices.size() > 10 ? cost_ * 0.9 : cost_ * 0.75)*100.0)/100.0;
    }

    /**
     * Emite a fatura para uma certa casa.
     */
    public Fatura emitirFatura(CasaInteligente casa, LocalDateTime start, LocalDateTime end){
        String ep = casa.getFornecedor();
        if(this.name.toLowerCase().equals(ep.toLowerCase())){
            return new Fatura(casa,start,end,this);
        }else return null;
    }

    @Override
    /**
     * Verifica a igualdade entre dois fornecedores.
     */
    public boolean equals(Object o) {
        if (o == this){
            return true;
        }
        if (o == null || (this.getClass() != o.getClass())) {
            return false;
        }
        EnergyProvider ep = (EnergyProvider) o;
        return this.name.equals(ep.name) && 
               this.price_kwh == ep.price_kwh && 
               this.tax == ep.tax;
    }

    @Override
    /**
     * Devolve uma string com informação relevante sobre este fornecedor.
     */
    public String toString() {
        return String.format("{EnergyProvider, Name = %d, price_kwh = %f, tax = %f}",
                             this.name,this.price_kwh,this.tax);
    }

    @Override
    /**
     * Copia este fornecedor.
     */
    public EnergyProvider clone(){
        return new EnergyProvider(this);
    }

}
