import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;


/**
 * A CasaInteligente faz a gestão dos SmartDevices que existem e dos 
 * espaços (as salas) que existem na casa.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class CasaInteligente implements Serializable {
   
    private String morada;
    private Map<String, SmartDevice> devices; // identificador -> SmartDevice
    private Map<String, Set<String>> locations; // Espaço -> Conjunto codigo dos devices
    private Pessoa proprietario;
    private String fornecedor;

    /**
     * Constructor for objects of class CasaInteligente
     */
    public CasaInteligente() {
        // initialise instance variables
        this.morada = "";
        this.devices = new HashMap<>();
        this.locations = new HashMap<>();
        this.proprietario = null;
        this.fornecedor = null;
    }

    public CasaInteligente(String morada) {
        // initialise instance variables
        this.morada = morada;
        this.devices = new HashMap<>();
        this.locations = new HashMap<>();
        this.proprietario = null;
        this.fornecedor = null;
    }

    public CasaInteligente(String morada, Pessoa proprietario, String fornecedor){
        // initialise instance variables
        this.morada = morada;
        this.devices = new HashMap<>();
        this.locations = new HashMap<>();
        this.proprietario = proprietario.clone();
        this.fornecedor = fornecedor;
    }

    /**
     * Construtor de cópia.
     */
    public CasaInteligente(CasaInteligente ci){
        this.morada = ci.getMorada();
        this.devices = ci.getMapDevices();
        this.locations = ci.getMapLocations();
        this.proprietario = ci.getProprietario();
        this.fornecedor = ci.getFornecedor();
    }

    /**
     * Devolve a morada da casa.
     */
    public String getMorada(){
        return this.morada;
    }
     /**
      * Altera a morada da casa.
      */
    public void setMorada(String m){
        this.morada = m;
    }

    /**
     * Devolve um map com os devices.
     */
    public Map<String, SmartDevice> getMapDevices() {
        Map<String, SmartDevice> r = new HashMap<>();
        this.devices.keySet().forEach(key -> r.put(key,this.devices.get(key).clone()));
        return r;
    }

    /**
     * Devolve uma lista com os devices.
     */
    public List<SmartDevice> getDevices(){
        List<SmartDevice> result = new ArrayList<>();
        this.devices.values().forEach(device -> result.add(device.clone()));
        return result;
    }

    /**
     * Devolve os dispositivos de uma certa repartição.
     */
    public List<SmartDevice> getDevicesInRoom(String room){
        List<SmartDevice> result = new ArrayList<>();
        for(String devID: this.locations.get(room)){
            result.add(this.getDevice(devID).clone());
        }
        return result;
    }

    /**
     * Devolve um map com as locations.
     */
    public Map<String, Set<String>> getMapLocations() {
        Map<String, Set<String>> r = new HashMap<>();
        this.locations.keySet().forEach(room -> r.put(room,this.locations.get(room).stream().collect(Collectors.toSet())));
        return r;
    }

    /**
     * Devolve o proprietario da casa.
     */
    public Pessoa getProprietario(){
        return this.proprietario != null ? this.proprietario.clone() : null;
    }

    /**
     * Devolve o nif do proprietario.
     */
    public int getOwnerNif(){
        return this.proprietario != null ? this.proprietario.getNif() : -1;
    }

    /**
     * Devolve o nome do proprietario.
     */
    public String getOwnerName(){
        return this.proprietario != null ? this.proprietario.getNome() : "";
    }

    /**
     * Altera o proprietario da casa.
     */
    public void setProprietario(Pessoa p){
        this.proprietario = p.clone();
    }

    /**
     * Devolve o nome do seu fornecedor de energia.
     */
    public String getFornecedor(){
        return this.fornecedor;
    }
    
    /**
     * Altera o fornecedor de energia.
     */
    public void setFornecedor(String fornecedor){
        this.fornecedor = fornecedor;
    }

    /**
     * Verifica a existencia de um dispositivo.
     */
    public boolean existsDevice(String id) {
        return this.devices.containsKey(id);
    }
    
    /**
     * Adiciona um dispositivo.
     */
    public void addDevice(SmartDevice dev) {
        if(!this.existsDevice(dev.getID())){
            this.devices.put(dev.getID(), dev.clone());
        }
    }

    /**
     * Adiciona um dispositivo numa reparticao.
     */
    public void addDevice(SmartDevice dev, String room){
        if(!this.existsDevice(dev.getID())){
            if(this.hasRoom(room)){
                this.devices.put(dev.getID(), dev.clone());
                this.locations.get(room).add(dev.getID());
            }else{
                this.addRoom(room);
                this.devices.put(dev.getID(), dev.clone());
                this.locations.get(room).add(dev.getID());
            }
        }
    }

    /**
     * Retira um dispositivo da casa.
     */
    public void removeDevice(String devID){
        if(this.devices.containsKey(devID)){
            this.locations.values().forEach(devsInRoom -> devsInRoom.remove(devID));
            this.devices.remove(devID);
        }
    }

    /**
     * Devolve o dispositivo solicitado.
     */
    public SmartDevice getDevice(String idDev) {
        if(this.devices.containsKey(idDev)){
            return this.devices.get(idDev).clone();
        }
        else return null;
    }

    /**
     * Liga um dispositivo da casa.
     */
    public void turnDeviceOn(String devCode, LocalDateTime change_date) {
        if(this.existsDevice(devCode)){
            this.devices.get(devCode).turnOn(change_date);
        }
    }

    /**
     * Desliga um dispositivo da casa.
     */
    public void turnDeviceOff(String devCode, LocalDateTime change_date){
        if(this.existsDevice(devCode)){
            this.devices.get(devCode).turnOff(change_date);
        }
    }

    /**
     * Altera o estado de um dispositivo da casa.
     */
    public void setDeviceOn(String devCode, boolean state, LocalDateTime change_date){
        if(state) this.turnDeviceOn(devCode, change_date);
        else this.turnDeviceOff(devCode, change_date);
    }

    /**
     * Altera o estado de todos os dispositivos.
     */
    public void setAllOn(boolean b, LocalDateTime change_date) {
        this.devices.values().forEach(device -> device.setOn(b,change_date));
    }

    /**
     * Altera o estado de todos os dispositivos de uma repartição.
     */
    public void setAllinDivisionOn(String room, boolean b, LocalDateTime change_date){
        if(b) this.locations.get(room).forEach(devID -> turnDeviceOn(devID,change_date));
        else this.locations.get(room).forEach(devID -> turnDeviceOff(devID,change_date));
    }

    /**
     * Adiciona uma reparticao a casa.
     */
    public void addRoom(String room) {
        if(!this.hasRoom(room)){
            this.locations.put(room, new HashSet<>());
        }
    }

    /**
     * Verifica a existencia de uma reparticao.
     */
    public boolean hasRoom(String room) {
        return this.locations.containsKey(room);
    }

    /**
     * Adiciona um dispositivo numa reparticao.
    */
    public void addToRoom (String room, String devID) {
        if(!this.roomHasDevice(room, devID)){
            this.locations.get(room).add(devID);
        }
    }

    /**
     * Verifica a existencia de um dispositivo numa reparticao.
    */
    public boolean roomHasDevice (String room, String devID) {
        return this.locations.get(room).contains(devID);
    }

    /**
     * Retorna o consumo total desta casa.
     */
    public double getTotalConsumption(){
        return this.devices.values()
                           .stream()
                           .mapToDouble(SmartDevice::getTotalConsumption)
                           .sum();
    }

    /**
     * Retorna o custo associado ao consumo desta casa.
     */
    public double getTotalCost(EnergyProvider provider, LocalDateTime end){
        if(this.fornecedor.toLowerCase().equals(provider.getName().toLowerCase())){
            this.devices.values().forEach(dev -> dev.updateConsumption(end));
            return provider.cost(this.devices.values());
        }
        return 0.0;
    }

    
    /**
     * Reinicia os contadores de consumo e custo desta casa bem como os contadores dos seus dispositivos.
     */
    public void resetConsumption(){
        this.devices.values().forEach(device -> device.resetTotalConsumption());
    }

    /**
     * Altera a data da última alteração de estado de todos os dispositivos.
     * ATENÇÃO: FUNÇÃO PERIGOSA, USAR APENAS NO INÍCIO DE UMA SIMULAÇÃO
     */
    public void setLastChangeDateAllDevices(LocalDateTime change_date){
        this.devices.values().forEach(device -> device.setLastChangeDate(change_date));
    }

    /**
     * Atualiza o consumo de todos os dispositivos registando a data em que a atualização ocorreu.
     */
    public void updateConsumptionAllDevices(LocalDateTime update_date){
        this.devices.values().forEach(device -> device.updateConsumption(update_date));
    }

    /**
     * Altera a tonalidade de uma lâmpada desta casa.
     */
    public void setToneInBulb(String bulbID, int new_tone, LocalDateTime change_date){
        if(this.existsDevice(bulbID) && (new_tone == 0 || new_tone == 1 || new_tone == 2)){
            SmartDevice dev = this.devices.get(bulbID);
            if(dev instanceof SmartBulb){
                SmartBulb b = (SmartBulb)dev;
                b.setTone(new_tone,change_date);
            }
        }
    }

    /**
     * Altera o canal de um speaker desta casa.
     */
    public void setChannelInSpeaker(String speakerID,String channel){
        if(this.existsDevice(speakerID)){
            SmartDevice dev = this.devices.get(speakerID);
            if(dev instanceof SmartSpeaker){
                SmartSpeaker sp = (SmartSpeaker)dev;
                sp.setChannel(channel);
            }
        }
    }

    /**
     * Reduz o volume de um speaker desta casa.
     */
    public void volumeDownInSpeaker(String speakerID, LocalDateTime change_date){
        if(this.existsDevice(speakerID)){
            SmartDevice dev = this.devices.get(speakerID);
            if(dev instanceof SmartSpeaker){
                SmartSpeaker sp = (SmartSpeaker)dev;
                sp.volumeDown(change_date);
            }
        }
    }

    /**
     * Aumenta o volume de um speaker desta casa.
     */
    public void volumeUpInSpeaker(String speakerID, LocalDateTime change_date){
        if(this.existsDevice(speakerID)){
            SmartDevice dev = this.devices.get(speakerID);
            if(dev instanceof SmartSpeaker){
                SmartSpeaker sp = (SmartSpeaker)dev;
                sp.volumeUp(change_date);
            }
        }
    }

    /**
     * Devolve uma string com informação dos dispositivos por cada repartição.
     */
    public String devicesPerRoomInfo(){
        StringBuilder sb = new StringBuilder();
        for(String room: this.locations.keySet()){
            sb.append(String.format("Divisão: %s\n",room));
            for(String devID: this.locations.get(room)){
                SmartDevice dev = this.devices.get(devID);
                sb.append(String.format("    Tipo: %s, ID: %s, Estado: %s\n",dev.getClass().getName(),dev.getID(),dev.getOn() ? "ON" : "OFF"));
            }
        }
        return sb.toString();
    }

    @Override
    /**
     * Verifica a igualdade entre duas casas.
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CasaInteligente ci = (CasaInteligente) o;
        return this.morada.equals(ci.morada) && 
               this.devices.equals(ci.devices) && 
               this.locations.equals(ci.locations) &&
               this.proprietario.equals(ci.proprietario) &&
               this.fornecedor.equals(ci.fornecedor);
    }

    @Override
    /**
     * Devolve uma string com informação revelante sobre a casa.
     */
    public String toString() {
        return String.format("{Morada: %s, Nome proprietario: %s, NIF proprietario: %d, Fornecedor: %s}",this.morada,this.getOwnerName(),this.getOwnerNif(),this.fornecedor);
    }
    
    @Override
    /**
     * Copia esta casa.
     */
    public CasaInteligente clone(){
        return new CasaInteligente(this);
    }
}
