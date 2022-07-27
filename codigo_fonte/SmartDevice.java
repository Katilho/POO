import java.time.LocalDateTime;
import java.io.Serializable;
import java.time.Duration;
/**
 * A classe SmartDevice é um contactor simples.
 * Permite ligar ou desligar circuitos. 
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public abstract class SmartDevice implements Serializable {

    private String id;
    private boolean on;
    private double totalConsumption;
    private LocalDateTime last_change;


    /**
     * Constructor for objects of class SmartDevice
     */
    public SmartDevice() {
        this.id = "";
        this.on = false;
        this.totalConsumption = 0.0;
        this.last_change = null;
    }

    public SmartDevice(String id) {
        this.id = id;
        this.on = false;
        this.totalConsumption = 0.0;
        this.last_change = null;
    }


    public SmartDevice(String id, boolean b){
        this.id = id;
        this.on = b;
        this.totalConsumption = 0.0;
        this.last_change = null;
    }

    public SmartDevice(String id, boolean b, LocalDateTime last_change){
        this.id = id;
        this.on = b;
        this.totalConsumption = 0.0;
        this.last_change = last_change;
    }

    public SmartDevice(SmartDevice sd) {
        this.id = sd.getID();
        this.on = sd.getOn();
        this.totalConsumption = sd.totalConsumption;
        this.last_change = sd.last_change;
    }


    /**
     * Liga o dispositivo registando o momento que foi feito.
     */
    public void turnOn(LocalDateTime change_date) {
        if(!this.on){
            this.on = true;
            this.last_change = change_date;
        }
    }
    
    /**
     * Desliga o dispositivo registando o momento que foi feito.
     */
    public void turnOff(LocalDateTime change_date) {
        if(this.on){
            this.updateConsumption(change_date);
            this.on = false;
        }
    }

    /**
     * Devolve a data e hora em que o dispositivo foi ligado ou desligado pela última vez.
     */
    public LocalDateTime getLastChange(){
        return this.last_change;
    }
    
    /**
     * Devolve o estado do dispositivo.
     */
    public boolean getOn() {
        return this.on;
    }
    
    /**
     * Altera o estado do dispositivo.
     */
    public void setOn(boolean b, LocalDateTime change_date) {
        if(b) this.turnOn(change_date);
        else this.turnOff(change_date);
    }
    
    /**
     * Devolve o ID do dispositivo.
     */
    public String getID() {
        return this.id;
    }

    /**
     * Retorna o consumo diario de um dispositivo.
     */
    public abstract double dailyConsumption();

    /**
     * Retorna o consumo de um dispositivo por minuto.
     */
    public double consumptionPerMinute(){
        return this.dailyConsumption()/1440.0;
    }

    /**
     * Retorna o consumo total de um dispositivo desde o inicio da contagem.
     */
    public double getTotalConsumption(){
        return Math.round(this.totalConsumption*1000.0)/1000.0; // arredonda para tres casas decimais
    }

    /**
     * Atualiza o consumo de um dispositivo necessitando, obviamente, de saber o momento da atualização para efectuar os devidos cálculos.
     */
    public void updateConsumption(LocalDateTime update_date){
        double minutes = Math.abs(Duration.between(this.last_change, update_date).getSeconds())/60.0;
        this.totalConsumption += this.consumptionPerMinute()*minutes;
        this.last_change = update_date;
    }

    /**
     * Altera o momento em que o estado do dispositivo foi alterado.
     * ATENÇÃO: Esta função apenas deve ser usada quando uma simulação se inicia. NUNCA USAR ESTA FUNÇÃO NOUTRO CONTEXTO
     */
    public void setLastChangeDate(LocalDateTime change_date){
        this.last_change = change_date;
    }

    /**
     * Reinicia a contagem de consumo do dispositivo.
     */
    public void resetTotalConsumption(){
        this.totalConsumption = 0.0;
    }


    @Override
    /**
     * Verifica a igualdade entre dois dispositivos.
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SmartDevice that = (SmartDevice) o;
        return on == that.on && this.id.equals(that.id);
    }

    @Override
    /**
     * Copia um dispositivo.
     */
    public abstract SmartDevice clone();

    @Override
    /**
     * Devolve uma string com informação relevante sobre este dispositivo.
     */
    public abstract String toString();
}
