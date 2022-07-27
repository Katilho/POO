import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.List;

public class Simulator implements Serializable{
    /**
     * Armazena o último id atribuído a uma casa inserida na simulação.
     */
    private int last_house_id;

    /**
     * Casas presentes nesta simulação indexadas por um inteiro atribuído autmaticamente.
     */
    private Map<Integer, CasaInteligente> houses;

    /**
     * Fornecedores de energia presentes nesta simulação indexados pelo seu nome,
     */
    private Map<String,EnergyProvider> energyProviders;
    
    /**
     * Faturas emitidas por cada fornecedor indexadas segundo o nome do fornecedor.
     */
    private Map<String, List<Fatura>> billsPerProvider; 
    
    /**
     * Volumes de faturação por fornecedor.
     */
    private Map<String, Double> profitPerProvider; 
    
    /**
     * Lista de casas ordenada (de forma decrescente) segundo o consumo total das casas na execução da simulação.
     * Esta lista está vazia no momento da criação do simulador.
     */
    private List<CasaInteligente> consumptionOrder;

    // CONSTRUTORES

    public Simulator(Collection<CasaInteligente> houses, Collection<EnergyProvider> providers) {
        this.last_house_id = 0;
        this.houses = new HashMap<>();
        this.energyProviders = new HashMap<>();
        this.billsPerProvider = new HashMap<>();
        this.profitPerProvider = new HashMap<>();
        
        houses.forEach(house -> this.addHouse(house));
        providers.forEach(provider -> this.addProvider(provider));
        
        for(EnergyProvider ep : providers){
            this.billsPerProvider.putIfAbsent(ep.getName().toLowerCase(), new ArrayList<Fatura>());
            this.profitPerProvider.putIfAbsent(ep.getName().toLowerCase(),0.0);
        }

        this.consumptionOrder = new ArrayList<>();
    }

    public Simulator(){
        this.houses = new HashMap<>();
        this.energyProviders = new HashMap<>();
        this.billsPerProvider = new HashMap<>();
        this.profitPerProvider = new HashMap<>();
        this.consumptionOrder = new ArrayList<>();
    }

    /**
     * Método para inserir todos os elementos da simulação.
     */
    public void setSimulator(Collection<CasaInteligente> houses, Collection<EnergyProvider> providers){
        houses.forEach(house -> this.addHouse(house));
        providers.forEach(provider -> this.addProvider(provider));
        for(EnergyProvider ep : providers){
            this.billsPerProvider.putIfAbsent(ep.getName().toLowerCase(), new ArrayList<Fatura>());
            this.profitPerProvider.putIfAbsent(ep.getName().toLowerCase(),0.0);
        }
    }

    // CORRER A SIMULAÇÃO

    public void startSimulation(LocalDateTime start, LocalDateTime end, List<Command> commands){
        this.resetAll();
        List<Command> end_commands = new ArrayList<Command>(); // armazena os comandos que são executados no fim da simulação
        
        // Não se aceitam comandos cuja data e hora estejam fora do intervalo imposto
        commands.removeIf(cmd -> cmd.getExecutionDateTime().compareTo(start) < 0 ||
                                 cmd.getExecutionDateTime().compareTo(end) > 0);

        // execução da simulacao
        this.houses.values().forEach(house -> house.setLastChangeDateAllDevices(start));
        for(Command cmd: commands){
            if(cmd.getFlag()){ 
                cmd.execute(this);
            }else{
                end_commands.add(cmd);
            }
        }
        this.houses.values().forEach(house -> house.updateConsumptionAllDevices(end));
        // fim da execução da simulação

        // gerar resultados (estatísticas)
        this.consumptionOrder = this.houses.values().stream().sorted((h1,h2) -> {
            double dif = h2.getTotalConsumption() - h1.getTotalConsumption();
            return dif < 0 ? -1 : dif == 0 ? 0 : 1;
        }).collect(Collectors.toList());

        for(CasaInteligente house: this.houses.values()) {
            String provider_name = house.getFornecedor().toLowerCase();
            this.billsPerProvider.get(provider_name).add(this.energyProviders.get(provider_name).emitirFatura(house,start,end));
            this.profitPerProvider.merge(provider_name, house.getTotalCost(this.energyProviders.get(provider_name),end), Double::sum);
        }
        // fim do cálculo das estatísticas

        // Executar os comandos finais
        for(Command cmd: end_commands){
            cmd.execute(this);
        }
        // fim da execução dos comandos finais
    }

    // MANIPULAR AS ENTIDADES DA SIMULAÇÃO

    /**
     * Repoe o estado das casas e dos dispositivos no estado inicial, excetuando alterações de preços e de fornecedores.
     */
    public void resetAll(){
        this.houses.values().forEach(house -> house.resetConsumption());
        this.billsPerProvider.values().forEach(list -> list.clear());
        this.profitPerProvider.keySet().forEach(key -> this.profitPerProvider.put(key,0.0));
        this.consumptionOrder = new ArrayList<>();
    }

    /**
     * Apaga todos os dados desta simulação.
     */
    public void clearSimulation(){
        this.houses = new HashMap<>();
        this.energyProviders = new HashMap<>();
        this.billsPerProvider = new HashMap<>();
        this.profitPerProvider = new HashMap<>();
        this.consumptionOrder = new ArrayList<>();
    }

    /**
     * Adiciona uma casa a esta simulacao.
     */
    public void addHouse(CasaInteligente house){
        this.houses.put(++this.last_house_id,house.clone());
    }

    /**
     * Altera o preco aplicado por um fornecedor.
     * @return true se a operação foi bem sucedida e false se o fornecedor dado não existe.
     */

    public boolean changeProviderPrice(EnergyProvider provider, double new_price){
        return this.changeProviderPrice(provider.getName(),new_price);
    }

    /**
     * Altera o preco aplicado por um fornecedor.
     * @return true se a operação foi bem sucedida e false se o fornecedor dado não existe.
     */

    public boolean changeProviderPrice(String providerName, double new_price){
        if(this.energyProviders.containsKey(providerName.toLowerCase())){
            this.energyProviders.get(providerName.toLowerCase()).setPrice_kwh(new_price);
            return true;
        }else return false;
    }

    /**
     * Altera o imposto aplicado por um fornecedor.
     */
    public boolean changeProviderTax(EnergyProvider provider, double new_tax){
        return this.changeProviderTax(provider.getName(),new_tax);
    }

    /**
     * Altera o imposto aplicado por um fornecedor dado o seu nome.
     */
    public boolean changeProviderTax(String providerName, double new_tax){
        if (this.energyProviders.containsKey(providerName.toLowerCase())) {
            this.energyProviders.get(providerName.toLowerCase()).setTax(new_tax);
            return true;
        } else return false;
    }

    /**
     * Adiciona um fornecedor a esta simulação
     */
    public void addProvider(EnergyProvider provider){
        if(!(this.energyProviders.containsKey(provider.getName().toLowerCase()))){
            this.energyProviders.put(provider.getName().toLowerCase(),provider.clone());
            this.billsPerProvider.put(provider.getName().toLowerCase(),new ArrayList<>());
            this.profitPerProvider.put(provider.getName().toLowerCase(), 0.0);
        }
    }

    /**
     * Remove um dispositivo de uma casa da simulação.
     */
    public void removeDeviceFromHouse(int houseID,String devID){
        if(this.houses.containsKey(houseID)){
            this.houses.get(houseID).removeDevice(devID);
        }
    }

    /**
     * Remove uma casa desta simulacao e retorna-a;
     */
    public CasaInteligente removeHouse(int houseID){
        return this.houses.remove(houseID);
    }

    /**
     * Devolve uma lista com as casas desta simulação
     */
    public List<CasaInteligente> getHouses(){
        return this.houses.values().stream().map(CasaInteligente::clone).collect(Collectors.toList());
    }

    /**
     * Devolve o mapa que contém as casas
     */
    public Map<Integer, CasaInteligente> getHousesMap(){
        Map<Integer, CasaInteligente> result = new HashMap<>();
        this.houses.keySet().forEach(id -> result.put(id,this.houses.get(id).clone()));
        return result;
    }

    /**
     * Retorna uma casa da simulação.
     */
    public CasaInteligente getHouse(Integer houseID){
        return this.houses.get(houseID).clone();
    }

    /**
     * Devolve uma lista com os fornecedores desta simulação
     */
    public List<EnergyProvider> getProviders(){
        List<EnergyProvider> result = new ArrayList<>();
        this.energyProviders.values().forEach(ep -> result.add(ep.clone()));
        return result;
    }


    /**
     * Devolve o mapa que contém os fornecedores
     */
    public Map<String,EnergyProvider> getProvidersMap(){
        Map<String,EnergyProvider> result = new HashMap<>();
        this.energyProviders.keySet().forEach(name -> result.put(name,this.energyProviders.get(name).clone()));
        return result;
    }

    /**
     * Altera o estado(ligado/desligado) de todos os dispositivos de todas as casas.
     */
    public void setStateAllDevicesInAllHouses(boolean state, LocalDateTime change_date){
        this.houses.values().forEach(house -> house.setAllOn(state,change_date));
    }

    /** 
     * Altera o estado(ligado/desligado) de todos os dispositivos de uma certa casa.
     * @return true se a operação foi bem sucedida e false se a casa não existe.
    */
    public boolean setStateAllDevicesHouse(int houseID, boolean state, LocalDateTime change_date){
        if(this.houses.containsKey(houseID)){
            this.houses.get(houseID).setAllOn(state,change_date);
            return true;
        }else return false;
    }

    /** 
     * Altera o estado(ligado/desligado) de um dispositivo de uma certa casa.
     * @return 0 se a operação foi bem sucedida; 1 se a casa não existe; 2 se o dispositivo não existe na casa
    */
    public int setStateInDeviceInHouse(String devID, int houseID, boolean state, LocalDateTime change_date){
        if(this.houses.containsKey(houseID)){
            CasaInteligente house = this.houses.get(houseID);
            if(house.existsDevice(devID)){
                house.setDeviceOn(devID, state, change_date);
                return 0;
            } else return 2;
        } else return 1;
    }

    /**
     * Altera o estado(ligado/desligado) de todos os dispositivos de uma reparticao de uma certa casa.
     * @return 0 se a operação foi bem sucedida; 1 se a casa não existe; 2 se a reparticao não existe na casa.
     */
    public int setStateAllDevicesInRoom(int houseID, String room, boolean state, LocalDateTime change_date){
        if(this.houses.containsKey(houseID)){
            CasaInteligente house = this.houses.get(houseID);
            if(house.hasRoom(room)){
                house.setAllinDivisionOn(room, state, change_date);
                return 0;
            }else return 2;
        }else return 1;
    }

    /**
     * Altera o fornecedor de uma casa.
     */
    public void changeHouseProvider(String providername, Integer houseID){
        CasaInteligente house = this.houses.get(houseID);
        if(this.energyProviders.containsKey(providername.toLowerCase())){
            house.setFornecedor(providername);
        }
    }

    /**
     * Altera a tonalidade de uma lâmpada de uma casa.
     */
    public void changeBulbToneInHouse(int houseID, String bulbID, int tone, LocalDateTime change_date){
        if(this.houses.containsKey(houseID)){
            this.houses.get(houseID).setToneInBulb(bulbID, tone, change_date);
        }
    }

    /**
     * Altera o canal de um speaker de uma casa.
     */
    public void changeSpeakerChannelInHouse(int houseID, String speakerID, String channel){
        if(this.houses.containsKey(houseID)){
            this.houses.get(houseID).setChannelInSpeaker(speakerID, channel);
        }
    }
    
    /**
     * Reduz o volume de um speaker de uma casa.
    */
    public void volumeDownSpeakerInHouse(int houseID, String speakerID, LocalDateTime change_date){
        if(this.houses.containsKey(houseID)){
            this.houses.get(houseID).volumeDownInSpeaker(speakerID, change_date);
        }
    }

    /**
     * Aumenta o volume de um speaker de uma casa.
    */
    public void volumeUpSpeakerInHouse(int houseID, String speakerID, LocalDateTime change_date){
        if(this.houses.containsKey(houseID)){
            this.houses.get(houseID).volumeUpInSpeaker(speakerID, change_date);
        }
    }

    // ESTATÍSTICAS DA SIMULAÇÃO

    /**
     * Retorna a casa que mais energia consumiu na simulacao ou null se a simulação ainda não executou!
     */
    public CasaInteligente getBiggestConsumer(){
        return this.consumptionOrder != null && this.consumptionOrder.size() > 0 ? this.consumptionOrder.get(0).clone() : null;
    }

    /**
     * Retorna o fornecedor com maior volume de faturacao ou null se a simulação ainda executou.
     */
    public EnergyProvider getBiggestProvider(){
        String providerName;
        providerName = this.profitPerProvider.keySet().stream()
                                             .max((p1,p2) -> {
                                                 double aux = this.profitPerProvider.get(p1.toLowerCase()) - this.profitPerProvider.get(p2.toLowerCase());
                                                 return aux < 0 ? -1 : aux == 0 ? 0 : 1;
                                             }).orElse(null);
        return providerName != null ? this.energyProviders.get(providerName.toLowerCase()).clone() : null;
    }

    /**
     * Devolve as faturas emitidas por um certo fornecedor.
     */
    public List<Fatura> getBillsFromProvider(EnergyProvider provider){
        return this.getBillsFromProvider(provider.getName());
    }

    /**
     * Devolve as faturas emitidas por um certo fornecedor dado o seu nome.
     */
    public List<Fatura> getBillsFromProvider(String providerName){ 
        return this.billsPerProvider.get(providerName.toLowerCase())
                                    .stream()
                                    .map(Fatura::clone)
                                    .collect(Collectors.toList());
    }

    /**
     * Retorna o volume de facturação de um certo fornecedor.
     */
    public double getProfitOfProvider(String providername){
        return this.profitPerProvider.get(providername.toLowerCase());
    }

    /**
     * Devolve uma ordenação dos consumidores pelo consumo total.
     */
    public List<CasaInteligente> getConsumptionOrder(){
        return this.consumptionOrder != null ? this.consumptionOrder.stream()
                                                   .map(CasaInteligente::clone)
                                                   .collect(Collectors.toList()) : null;
    }

    // MÉTODOS AUXILIARES

    /**
     * Verifica a existência de um dispositivo em todas as casas da simulação.
     */
    public boolean existsDevice(String devID){
        boolean result = false;
        for(CasaInteligente house: this.houses.values()){
            if(house.existsDevice(devID)){
                result = true; break;
            }
        }
        return result;
    }

    /**
     * Verifica a existência de um fornecedor na simulação dado o seu nome.
     */
    public boolean existsProvider(String providerName){
        return this.energyProviders.containsKey(providerName.toLowerCase());
    }

    /**
     * Verifica a existência de um fornecedor na simulação.
     */
    public boolean existsProvider(EnergyProvider provider){
        return this.existsProvider(provider.getName());
    }

    /**
     * Adiciona um dispositivo a uma casa da simulação
     */
    public void addDevice(SmartDevice device, int id_house, String room){
        this.houses.get(id_house).addDevice(device, room);
    }

    /**
     * Remove um dispositivo de uma casa da simulação
     */
    public void removeDevice(String devID, int id_house){
        this.houses.get(id_house).removeDevice(devID);
    }

    /**
     * Verifica as condições para se saber se a simulação pode arrancar.
     */
    public boolean isReady(){
        return !(this.houses.isEmpty()) && !(this.energyProviders.isEmpty());
    }

    // GUARDAR O ESTADO DESTA SIMULAÇÃO
    /**
     * Guarda esta simulação num ficheiro binário.
     */
    public void saveState(String path) throws FileNotFoundException,IOException{
        FileOutputStream fos = new FileOutputStream(path);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this);
        oos.flush();
        oos.close();
    }

    /**
     * Recebe um caminho para um ficheiro de objetos e converte-o numa simulação.
     */
    public static Simulator loadState(String path) throws FileNotFoundException, 
                                                          IOException,
                                                          ClassNotFoundException {
        FileInputStream fis = new FileInputStream(path);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Simulator sim = (Simulator) ois.readObject();
        ois.close();
        return sim;                     
    }

}
