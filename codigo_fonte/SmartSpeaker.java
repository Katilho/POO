import java.time.LocalDateTime;

/**
 * Um SmartSpeaker é um SmartDevice que além de ligar e desligar permite também
 * reproduzir som.
 * Consegue ligar-se a um canal (por simplificação uma rádio online) e permite
 * a regulação do seu nível de volume.
 * @author (your name)
 * @version (a version number or a date)
 */
public class SmartSpeaker extends SmartDevice {
    public static final int MAX = 20;

    private int volume;
    private String channel;
    private String marca;

    /**
     * Constructor for objects of class SmartSpeaker
     */
    public SmartSpeaker() {
        super(); // chama o construtor da superclasse.
        // initialise instance variables
        this.volume = 0;
        this.channel = "";
        this.marca = "";
    }

    public SmartSpeaker(String s) {
        super(s); // chama o construtor da superclasse com o determinado parametro.
        // initialise instance variables
        this.volume = 10;
        this.channel = "";
        this.marca = "";
    }


    public SmartSpeaker(String cod, String channel, int volume, String marca) {
        // initialise instance variables
        super(cod); // chama o construtor da superclasse com o determinado parametro.
        this.channel = channel;
        if (volume >= 0 && volume <= MAX){
            this.volume = volume;
        }
        else {
            this.volume = 0;
        }
        this.marca = marca;
    }

    public SmartSpeaker(String id, boolean state, int volume, String channel, String marca){
        super(id,state);
        this.volume = volume;
        this.channel = channel;
        this.marca = marca;
    }

    public SmartSpeaker(String id, boolean state, int volume, String channel, String marca, LocalDateTime change_date){
        super(id,state,change_date);
        this.volume = volume;
        this.channel = channel;
        this.marca = marca;
    }

    public SmartSpeaker(SmartSpeaker ss){
        super(ss);
        this.volume = ss.getVolume();
        this.channel = ss.getChannel();
        this.marca = ss.marca;
    }

    /**
     * Aumenta o volume desta coluna.
     */
    public void volumeUp(LocalDateTime change_date) {
        this.updateConsumption(change_date);
        if (this.volume < MAX) this.volume++;
    }

    /**
     * Diminui o volume desta coluna.
     */
    public void volumeDown(LocalDateTime change_date) {
        this.updateConsumption(change_date);
        if (this.volume > 0) this.volume--;
    }

    /**
     * Devolve o volume desta coluna.
     */
    public int getVolume() {
        return this.volume;
    }
    
    /**
     * Devolve o canal desta coluna.
     */
    public String getChannel() {
        return this.channel;
    }

    /**
     * Altera o canal desta coluna.
     */
    public void setChannel(String c) {
        this.channel = c;
    }

    /**
     * Devolve a marca desta coluna.
     */
    public String getMarca(){
        return this.marca;
    }


    @Override
    public double dailyConsumption(){
        return this.getOn() ? 10*(this.marca.length() + ((this.volume * 3.0)*24.0))/1000.0 : 0.0;
    }

    @Override
    public boolean equals (Object o){
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != this.getClass()){
            return false;
        }
        SmartSpeaker s = (SmartSpeaker) o;
        return super.equals(o) && this.volume == s.getVolume() && this.channel == s.getChannel() && this.marca == s.getMarca();
    }

    @Override
    public String toString(){
        return String.format("{Device: SmartBulb, ID: %s, ON/OFF: %s, Volume: %d, Channel: %s, Marca: %s}", this.getID(), this.getOn() ? "ON" : "OFF" ,this.volume, this.channel, this.marca);
    }

    @Override
    public SmartDevice clone() {
        return new SmartSpeaker(this);
    }

}
