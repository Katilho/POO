import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Interface {

    private Simulator sim;                  // este objecto está encarregue de fazer a simulação

    public Interface(){
        this.sim = new Simulator(); 
    }

    private LocalDateTime getDateFromInput(String message,Scanner s){
        LocalDateTime date = null;
        boolean flag = true;
        while(flag){
            try {
                System.out.print(message);
                String aux = s.nextLine();
                date = LocalDateTime.parse(aux, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                flag = false;
            } catch (DateTimeParseException e) {
                System.out.println("A data tem de estar no formato AAAA-MM-DD HH:mm!");
            }
        }
        return date;
    }

    private SmartBulb bulbFromInput(Scanner sc){
        // Variáveis auxiliares
        String isOn,idBulb,tone; boolean state, flag = true;
        double dimension = 1.0;
        SmartBulb sb = null;

        // Criação da lâmpada
        System.out.print("Insira o id desta lâmpada: ");
        idBulb = sc.nextLine();
        while(this.sim.existsDevice(idBulb)){
            System.out.println("O id que pretende atribuir já existe!");
            System.out.print("Insira o id desta lâmpada: ");
            idBulb = sc.nextLine();
        }
        //this.devs_ids.add(idBulb);

        System.out.print("O dispositivo inicia-se ligado?(s/n): ");
        isOn = sc.nextLine();
        while (isOn.length() != 1 && !("sSnN".contains(isOn))) {
            System.out.println("Opção inválida, escreva 's' ou 'S' para sim ou 'n' ou 'N' para não (sem aspas)!");
            System.out.print("O dispositivo inicia-se ligado?(s/n): ");
            isOn = sc.nextLine();
        }
        if(isOn.toLowerCase().equals("s")) state = true;
        else state = false;

        System.out.print("Insira a tonalidade da lâmpada (WARM/COLD/NEUTRAL): ");
        tone = sc.nextLine().toLowerCase();
        while(!(tone.equals("warm") || tone.equals("cold") || tone.equals("neutral"))){
            System.out.println("Opção inválida, escreva 'warm','cold' ou 'neutral'");
            System.out.print("Insira a tonalidade da lâmpada (WARM/COLD/NEUTRAL): ");
            tone = sc.nextLine().toLowerCase();
        }

        System.out.print("Insira a dimensão desta lâmpada: ");
        while(flag){
            try{
                dimension = Double.parseDouble(sc.nextLine());
                flag = false;
            } catch (NumberFormatException e){
                System.out.println("A dimensão deve ser um número real!");
                System.out.print("Insira a dimensão desta lâmpada: ");
            }
        }
        switch (tone) {
            case "warm":sb = new SmartBulb(idBulb,state,2,dimension); break;
            case "cold":sb = new SmartBulb(idBulb,state,0,dimension);break;
            case "neutral":sb = new SmartBulb(idBulb,state,1,dimension);break;
        }
        return sb;
    }

    private SmartSpeaker speakerFromInput(Scanner sc){
        // Variáveis auxiliares
        String isOn,idSpeaker,channel,marca; boolean state, flag = true;
        int volume = 0;

        // Criação do speaker
        System.out.print("Insira o id deste speaker : ");
        idSpeaker = sc.nextLine();
        while(this.sim.existsDevice(idSpeaker)){
            System.out.println("O id que pretende atribuir já existe!");
            System.out.print("Insira o id deste speaker: ");
            idSpeaker = sc.nextLine();
        }
        //this.devs_ids.add(idSpeaker);

        System.out.print("O dispositivo inicia-se ligado?(s/n): ");
        isOn = sc.nextLine();
        while (isOn.length() != 1 && !("sSnN".contains(isOn))) {
            System.out.println("Opção inválida, escreva 's' ou 'S' para sim ou 'n' ou 'N' para não (sem aspas)!");
            System.out.print("O dispositivo inicia-se ligado?(s/n): ");
            isOn = sc.nextLine();
        }
        if(isOn.toLowerCase().equals("s")) state = true;
        else state = false;

        System.out.print("Insira o volume com que o speaker inicia: ");
        while (flag) {
            try {
                volume = Integer.parseInt(sc.nextLine());
                flag = false;
            } catch (NumberFormatException e){
                System.out.println("O volume deve ser um número inteiro!");
                System.out.print("Insira o volume com que o speaker inicia: ");
            }
        }

        System.out.print("Insira o canal com que este speaker inicia: ");
        channel = sc.nextLine();

        System.out.print("Insira a marca deste speaker: ");
        marca = sc.nextLine();

        return new SmartSpeaker(idSpeaker,state,volume,channel,marca);
    }

    private SmartCamera cameraFromInput(Scanner sc){
        // Variáveis auxiliares
        String isOn,idCam; boolean state, flag = true;
        int resX = 0, resY = 0; double sizeOfFile = 0;

        // Criação da câmara
        System.out.print("Insira o id desta câmara: ");
        idCam = sc.nextLine();
        while(this.sim.existsDevice(idCam)){
            System.out.println("O id que pretende atribuir já existe!");
            System.out.print("Insira o id deste speaker: ");
            idCam = sc.nextLine();
        }
        //this.devs_ids.add(idCam);

        System.out.print("O dispositivo inicia-se ligado?(s/n): ");
        isOn = sc.nextLine();
        while (isOn.length() != 1 && !("sSnN".contains(isOn))) {
            System.out.println("Opção inválida, escreva 's' ou 'S' para sim ou 'n' ou 'N' para não (sem aspas)!");
            System.out.print("O dispositivo inicia-se ligado?(s/n): ");
            isOn = sc.nextLine();
        }
        if(isOn.toLowerCase().equals("s")) state = true;
        else state = false;

        System.out.print("Insira a resolução horizontal(X): ");
        while (flag) {
            try{
                resX = Integer.parseInt(sc.nextLine());
                flag = false;
            } catch (NumberFormatException e){
                System.out.println("A resolução horizontal deve ser um número inteiro!");
                System.out.print("Insira a resolução horizontal(X): ");
            }
        }
        flag = true;

        System.out.print("Insira a resolução vertical(Y): ");
        while (flag) {
            try{
                resY = Integer.parseInt(sc.nextLine());
                flag = false;
            } catch (NumberFormatException e){
                System.out.println("A resolução vertical deve ser um número inteiro!");
                System.out.print("Insira a resolução vertical(Y): ");
            }
        }
        flag = true;

        System.out.print("Insira o tamanho do ficheiro gerado por esta câmara: ");
        while (flag) {
            try{
                sizeOfFile = Double.parseDouble(sc.nextLine());
                flag = false;
            } catch (NumberFormatException e){
                System.out.println("O tamanho do ficheiro deve ser um número real!");
                System.out.print("Insira o tamanho do ficheiro gerado por esta câmara: ");
            }
        }
        flag = true;

        return new SmartCamera(idCam,state,resX,resY,sizeOfFile);
    }

    private SmartDevice deviceFromInput(Scanner s){
        int dev_type = 0;
        boolean flag = true;
        SmartDevice device = null;
        while(flag){
            System.out.println("------------------------------------");
            System.out.println("| Selecione o tipo de dispositivo  |");
            System.out.println("------------------------------------");
            System.out.println("| 1 | SmartBulb                    |");
            System.out.println("------------------------------------");
            System.out.println("| 2 | SmartCamera                  |");
            System.out.println("------------------------------------");
            System.out.println("| 3 | SmartSpeaker                 |");
            System.out.println("------------------------------------");
            while(flag){
                try {
                    System.out.print("Insira a opção: ");
                    dev_type = Integer.parseInt(s.nextLine());
                    flag = false;
                } catch (NumberFormatException e) {
                    System.out.println("O opção deve ser um número inteiro!\nPressione ENTER para continuar");
                    s.nextLine();
                }
            }

            switch (dev_type) {
                case 1:{
                    device = bulbFromInput(s);
                    break;
                }

                case 2:{
                    device = cameraFromInput(s);
                    break;
                }

                case 3:{
                    device = speakerFromInput(s);
                    break;
                }
            
                default:{
                    System.out.println("Opção inválida (deve ser um inteiro entre 1 e 3)\nPressione ENTER para continuar");
                    s.nextLine();
                    flag = true;
                    break;
                }
            }
        }
        return device;
    }

    private CasaInteligente houseFromInput(Scanner s){
        boolean flag = true;
        int option = 0;
        CasaInteligente house = new CasaInteligente();
        while(flag){
            System.out.println("--------------------------------------------------------");
            System.out.println("| 1 | Definir o proprietário desta casa                |");
            System.out.println("--------------------------------------------------------");
            System.out.println("| 2 | Adicionar um dispositivo a esta casa             |");
            System.out.println("--------------------------------------------------------");
            System.out.println("| 3 | Definir o fornecedor desta casa                  |");
            System.out.println("--------------------------------------------------------");
            System.out.println("| 4 | Definir a morada desta casa                      |");
            System.out.println("--------------------------------------------------------");
            System.out.println("| 5 | Ver o que ainda não se informou sobre esta casa  |");
            System.out.println("--------------------------------------------------------");
            System.out.println("| 6 | Guardar esta casa e voltar ao menu anterior      |");
            System.out.println("--------------------------------------------------------");
            while(flag){
                try{
                    System.out.print("Insira a opção: ");
                    option = Integer.parseInt(s.nextLine());
                    flag = false;
                }catch(NumberFormatException e){
                    System.out.println("A opção deve ser um número inteiro!");
                }
            }
            flag = true;

            switch(option){

                case 1:{
                    int nif = 0;
                    while(flag){
                        try{
                            System.out.print("Insira o NIF do proprietário desta casa: ");
                            nif = Integer.parseInt(s.nextLine());
                            flag = false;
                        } catch(NumberFormatException e){
                            System.out.println("O NIF deve ser um número inteiro!");
                        }
                    }
                    flag = true;
                    System.out.print("Insira o nome do proprietário desta casa: ");
                    house.setProprietario(new Pessoa(s.nextLine(), nif));
                    break;
                }

                case 2:{
                    System.out.println("Insira a repartição onde o dispositivo se irá localizar: ");
                    String room = s.nextLine();
                    house.addDevice(deviceFromInput(s), room);
                    break;
                }

                case 3:{
                    String prov_name = null;
                    while (flag) {
                        System.out.print("Insira o nome do fornecedor (!já existente!): ");
                        prov_name = s.nextLine();
                        if(this.sim.existsProvider(prov_name)){
                            house.setFornecedor(prov_name);
                            flag = false;
                        }else{
                            System.out.printf("O fornecedor %s não existe\n",prov_name);
                            System.out.print("Deseja voltar ao menu anterior\npara adicionar o fornecedor pretendido? (s/n): ");
                            String answer = s.nextLine();
                            if("sS".contains(answer)){
                                return null;
                            }
                        }
                    }
                    flag = true;
                    break;
                }

                case 4:{
                    System.out.print("Insira a morada desta casa: ");
                    house.setMorada(s.nextLine());
                    break;
                }

                case 5:{
                    if(house.getProprietario() == null) System.out.println("| Proprietário ");
                    if(house.getDevices().size() <= 0) System.out.println("| Pelo menos um dispositivo");
                    if(house.getFornecedor() == null) System.out.println("| O fornecedor");
                    if(house.getMorada() == null || house.getMorada().equals("")) System.out.println("| A morada");
                    System.out.println("Pressione ENTER para continuar");
                    s.nextLine();
                    break;
                }

                case 6:{
                    if(house.getProprietario() != null && house.getDevices().size() > 0 && house.getFornecedor() != null && house.getMorada().compareTo("") != 0){
                        flag = false;
                    }else{
                        System.out.println("A casa ainda não tem tudo definido. Verifique o que falta (opção 5)\nPressione ENTER para continuar: ");
                        s.nextLine();
                    }
                    break;
                }

                default:{
                    System.out.println("Opção inválida (deve ser um inteiro entre 1 e 6)!");
                    break;
                }

            }

        }
        return house;
    }

    private EnergyProvider providerFromInput(Scanner s){
        boolean flag = true;
        String name = "";
        double price = 0, tax = 0;
        
        while(flag){
            System.out.print("Insira o nome deste fornecedor: ");
            name = s.nextLine();
            if(this.sim.existsProvider(name)){
                System.out.println("Este fornecedor já existe! Tente outro nome\nPressione ENTER para continuar");
                s.nextLine();
            }else{
                flag = false;
            }
        }
        flag = true;

        while (flag) {
            System.out.print("Insira o preco/kWh deste fornecedor: ");
            try {
                price = Double.parseDouble(s.nextLine());
                flag = false;
            } catch (NumberFormatException e) {
                System.out.println("O preço/kWh deve ser um número real!");
            }
        }
        flag = true;

        while (flag) {
            System.out.print("Insira o imposto deste fornecedor: ");
            try {
                tax = Double.parseDouble(s.nextLine());
                flag = false;
            } catch (NumberFormatException e) {
                System.out.println("O imposto deve ser um número real!");
            }
        }

        return new EnergyProvider(name, price, tax);
    }

    private void stateFromInput(Scanner s) {
        int option = 0;
        boolean flag = true;

        while(flag){
            System.out.println("---------------------------------------------");
            System.out.println("| Carregamento manual                       |");
            System.out.println("---------------------------------------------");
            System.out.println("| 1 | Adicionar um fornecedor               |");
            System.out.println("---------------------------------------------");
            System.out.println("| 2 | Ver os ids dos fornecedores           |");
            System.out.println("---------------------------------------------");
            System.out.println("| 3 | Adicionar uma casa                    |");
            System.out.println("---------------------------------------------");
            System.out.println("| 4 | Ver os ids das casas                  |");
            System.out.println("---------------------------------------------");
            System.out.println("| 5 | Adicionar um dispositivo a uma casa   |");
            System.out.println("---------------------------------------------");
            System.out.println("| 6 | Remover um dispositivo de uma casa    |");
            System.out.println("---------------------------------------------");
            System.out.println("| 7 | Limpar o estado atual                 |");
            System.out.println("---------------------------------------------");
            System.out.println("| 8 | Guardar este estado e voltar          |");
            System.out.println("---------------------------------------------");
            while(flag){
                try{
                    System.out.print("Insira a opção: ");
                    option = Integer.parseInt(s.nextLine());
                    flag = false;
                } catch(NumberFormatException e){
                    System.out.println("A opção deve ser um número inteiro!");
                }
            }
            flag = true;

            switch(option){

                case 1:{
                    EnergyProvider provider = providerFromInput(s);
                    if(!this.sim.existsProvider(provider.getName())){
                        this.sim.addProvider(provider);
                    }else{
                        System.out.printf("O fornecedor %s já existe!\nPressione ENTER para continuar\n",provider.getName());
                        s.nextLine();
                    }
                    break;
                }

                case 2:{
                    Map<String, EnergyProvider> aux = this.sim.getProvidersMap();
                    if(aux.size() <= 0){
                        System.out.println("Sem fornecedores!");
                    }else{
                        System.out.println("ID | Nome | Preço/kWh | Imposto");
                        for(String key: aux.keySet()){
                            EnergyProvider provider = aux.get(key);
                            System.out.printf("%s | %s | %f | %f\n",key,provider.getName(),provider.getPrice_kwh(),provider.getTax());
                        }
                        System.out.println("Pressione ENTER para continuar");
                        s.nextLine();
                    }
                    break;
                }

                case 3:{
                    CasaInteligente house = this.houseFromInput(s);
                    if(house != null){
                        this.sim.addHouse(house);
                    }
                    break;
                }

                case 4:{
                    Map<Integer,CasaInteligente> aux = this.sim.getHousesMap();
                    if(aux.size() <= 0){
                        System.out.println("Sem casas na simulação");
                    }
                    else{
                        System.out.println("ID | Morada | Nome Proprietario | Fornecedor");
                        for(Integer key: aux.keySet()){
                            CasaInteligente house = aux.get(key);
                            System.out.printf("%d | %s | %s | %s\n",key,house.getMorada(),house.getOwnerName(),house.getFornecedor());
                        }
                        System.err.println("Pressione ENTER para continuar");
                        s.nextLine();
                    }
                    break;
                }

                case 5:{
                    int id_house = 0;
                    while(flag){
                        try {
                            System.out.print("Insira o id da casa: ");
                            id_house = Integer.parseInt(s.nextLine());
                            if(!(this.sim.getHousesMap().containsKey(id_house))){
                                System.out.printf("A casa com id %d não existe\n",id_house);
                            } else flag = false;
                        } catch (NumberFormatException e) {
                            System.out.println("O id da casa é um número inteiro!");
                        }
                    }
                    System.out.print("Insira a repartição onde o dispositivo será colocado: ");
                    String room = s.nextLine();
                    this.sim.addDevice(deviceFromInput(s), id_house, room);
                    flag = true;
                    break;
                }

                case 6:{
                    Map<Integer,CasaInteligente> aux = this.sim.getHousesMap();
                    int house_id = 0;
                    while (flag) {
                        try {
                            System.out.print("Insira o id da casa: ");
                            house_id = Integer.parseInt(s.nextLine());
                            if(aux.containsKey(house_id)){
                                flag = false;
                            }else{
                                System.out.printf("A casa com id %d não existe\n",house_id);
                            }
                        }catch(NumberFormatException e){
                            System.out.println("O id da casa é um número inteiro.");
                        }
                    }
                    flag = true;
                    System.out.println("Dispositivos da casa\nID | Tipo");
                    for(SmartDevice dev: aux.get(house_id).getDevices()){
                        System.out.printf("%s | %s\n",dev.getID(),dev.getClass().getName());
                    }
                    System.out.println("Insira o ID do dispositivo a remover");
                    String devID = s.nextLine();
                    this.sim.removeDevice(devID, house_id);
                    break;
                }

                case 7:{
                    System.out.println("Atenção: esta ação vai apagar TODAS as informações de ");
                    System.out.println("         casas e fornecedores de forma irreversível.");
                    System.out.print("Pretende prosseguir? (s/n):");
                    String answer = s.nextLine();
                    if("Ss".contains(answer)){
                        this.sim.clearSimulation();
                        System.out.println("Estado apagado com sucesso!\nPressione ENTER para continuar");
                        s.nextLine();
                    }
                    break;
                }

                case 8:{
                    flag = false;
                    break;
                }

                default:{
                    System.out.println("Opção inválida (deve ser um inteiro entre 1 e 8)");
                    break;
                }
                
            }
        }
    }
    
    private Command commandFromInput(Scanner s){
        boolean flag = true;
        int option = 0;
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("| Registar um pedido                                                  |");
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("| 1  | Alterar o preço de um fornecedor                               |");
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("| 2  | Alterar o imposto de um fornecedor                             |");
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("| 3  | Alterar o fornecedor de uma casa                               |");
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("| 4  | Ligar todos os dispositivos de uma casa                        |");
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("| 5  | Desligar todos os dispositivos de uma casa                     |");
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("| 6  | Ligar todos os dispositivos de uma repartição de uma casa      |");
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("| 7  | Desligar todos os dispositivos de uma repartição de uma casa   |");
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("| 8  | Ligar todos os dispositivos de todas as casas                  |");
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("| 9  | Desligar todos os dispositivos de todas as casas               |");
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("| 10 | Ligar um dispositivo de uma casa                               |");
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("| 11 | Desligar um dispositivo de uma casa                            |");
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("| 12 | Alterar a tonalidade de uma lâmpada                            |");
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("| 13 | Alterar o canal de um speaker                                  |");
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("| 14 | Reduzir o volume de um speaker                                 |");
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("| 15 | Aumentar o volume de um speaker                                |");
        System.out.println("-----------------------------------------------------------------------");
        while(flag){
            try {
                System.out.print("Insira a opção: ");
                option = Integer.parseInt(s.nextLine());
                flag = false;
            } catch (NumberFormatException e) {
                System.out.println("A opção deve ser um número inteiro!");
            }
        }
        flag = true;
        LocalDateTime executionDateTime = null;
        Command result = null;
        executionDateTime = getDateFromInput("Insira a data em que se executa: ", s);

        switch (option) {
            case 1:{
                Map<String,EnergyProvider> aux = this.sim.getProvidersMap();
                System.out.println("--------------------------------");
                for(String key: aux.keySet()){
                    EnergyProvider p = aux.get(key);
                    System.out.printf("ID: %s; Nome: %s, Preço/kWh: %f; Imposto: %f\n",key,p.getName(),p.getPrice_kwh(),p.getTax());
                }
                System.out.println("--------------------------------");

                String provName = null;
                while(flag){
                    System.out.print("Insira o nome do fornecedor: ");
                    provName = s.nextLine();
                    if(this.sim.existsProvider(provName)){
                        flag = false;
                    }
                    else{
                        System.out.printf("O fornecedor %s não existe\n",provName);
                    }
                }
                flag = true;
                double newPrice = 0;
                while(flag){
                    try {
                        System.out.print("Insira o novo preço: ");
                        newPrice = Double.parseDouble(s.nextLine());
                        flag = false;
                    } catch (NumberFormatException e) {
                        System.out.println("O novo preço tem de ser um número real!");
                    }
                }
                String auxN = provName; double auxP = newPrice;
                result = new Command(executionDateTime, simul -> simul.changeProviderPrice(auxN, auxP), false);
                break;
            }

            case 2:{
                Map<String,EnergyProvider> aux = this.sim.getProvidersMap();
                System.out.println("------------------------------------------------------");
                for(String key: aux.keySet()){
                    EnergyProvider p = aux.get(key);
                    System.out.printf("ID: %s; Nome: %s, Preço/kWh: %f; Imposto: %f\n",key,p.getName(),p.getPrice_kwh(),p.getTax());
                }
                System.out.println("------------------------------------------------------");
                

                String provName = null;
                while(flag){
                    System.out.print("Insira o nome do fornecedor: ");
                    provName = s.nextLine();
                    if(this.sim.existsProvider(provName)){
                        flag = false;
                    }
                    else{
                        System.out.printf("O fornecedor %s não existe\n",provName);
                    }
                }
                flag = true;
                double newTax = 0;
                while(flag){
                    try {
                        System.out.print("Insira o novo imposto: ");
                        newTax = Double.parseDouble(s.nextLine());
                        flag = false;
                    } catch (NumberFormatException e) {
                        System.out.println("O novo imposto tem de ser um número real!");
                    }
                }
                String auxN = provName; double auxT = newTax;
                result = new Command(executionDateTime, simul -> simul.changeProviderTax(auxN, auxT), false);
                break;
            }

            case 3:{
                Map<Integer,CasaInteligente> aux = this.sim.getHousesMap();
                System.out.println("-------------------------Casas------------------------");
                for(Integer key: aux.keySet()){
                    CasaInteligente h = aux.get(key);
                    System.out.printf("ID: %d; Morada: %s; Proprietário: %s; Fornecedor: %s\n",key,h.getMorada(),h.getOwnerName(),h.getFornecedor());
                }
                System.out.println("------------------------------------------------------");
                System.out.println("----------------------Fornecedores--------------------");
                Map<String,EnergyProvider> auxP = this.sim.getProvidersMap();
                for(String key: auxP.keySet()){
                    EnergyProvider p = auxP.get(key);
                    System.out.printf("ID: %s; Nome: %s, Preço/kWh: %f; Imposto: %f\n",key,p.getName(),p.getPrice_kwh(),p.getTax());
                }
                System.out.println("------------------------------------------------------");

                int house_id = 0;
                while(flag){
                    try {
                        System.out.print("Insira o ID da casa: ");
                        house_id = Integer.parseInt(s.nextLine());
                        if(aux.containsKey(house_id)){
                            flag = false;
                        }else{
                            System.out.printf("A casa com id %d não existe\n",house_id);
                        }
                        
                    } catch (NumberFormatException e) {
                        System.out.println("O id da casa é um número inteiro");
                    }
                }
                flag = true;
                String provName = null;
                while(flag){
                    System.out.print("Insira o id do fornecedor: ");
                    provName = s.nextLine();
                    if(this.sim.existsProvider(provName)){
                        flag = false;
                    }
                    else{
                        System.out.printf("O fornecedor %s não existe\n",provName);
                    }
                }
                String auxN = provName; int auxI = house_id;
                result = new Command(executionDateTime, sim -> sim.changeHouseProvider(auxN, auxI), false);
                break;
            }

            case 4:{
                Map<Integer,CasaInteligente> aux = this.sim.getHousesMap();
                System.out.println("-------------------------Casas------------------------");
                for(Integer key: aux.keySet()){
                    CasaInteligente h = aux.get(key);
                    System.out.printf("ID: %d; Morada: %s; Proprietário: %s; Fornecedor: %s\n",key,h.getMorada(),h.getOwnerName(),h.getFornecedor());
                }
                System.out.println("------------------------------------------------------");
                int house_id = 0; 
                while(flag){
                    try {
                        System.out.print("Insira o ID da casa: ");
                        house_id = Integer.parseInt(s.nextLine());
                        if(aux.containsKey(house_id)){
                            flag = false;
                        }else{
                            System.out.printf("A casa com id %d não existe!\n",house_id);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("O id da casa é um número inteiro.");
                    }
                }
                int auxI = house_id; LocalDateTime auxDT = executionDateTime;
                result = new Command(executionDateTime, sim -> sim.setStateAllDevicesHouse(auxI, true, auxDT) , true);
                break;
            }

            case 5:{
                Map<Integer,CasaInteligente> aux = this.sim.getHousesMap();
                System.out.println("-------------------------Casas------------------------");
                for(Integer key: aux.keySet()){
                    CasaInteligente h = aux.get(key);
                    System.out.printf("ID: %d; Morada: %s; Proprietário: %s; Fornecedor: %s\n",key,h.getMorada(),h.getOwnerName(),h.getFornecedor());
                }
                System.out.println("------------------------------------------------------");
                int house_id = 0; 
                while(flag){
                    try {
                        System.out.print("Insira o ID da casa: ");
                        house_id = Integer.parseInt(s.nextLine());
                        if(aux.containsKey(house_id)){
                            flag = false;
                        }else{
                            System.out.printf("A casa com id %d não existe!\n",house_id);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("O id da casa é um número inteiro.");
                    }
                }
                int auxI = house_id; LocalDateTime auxDT = executionDateTime;
                result = new Command(executionDateTime, sim -> sim.setStateAllDevicesHouse(auxI, false, auxDT) , true);
                break;
            }

            case 6:{
                Map<Integer,CasaInteligente> aux = this.sim.getHousesMap();
                System.out.println("-------------------------Casas------------------------");
                for(Integer key: aux.keySet()){
                    CasaInteligente h = aux.get(key);
                    System.out.printf("ID: %d; Morada: %s; Proprietário: %s; Fornecedor: %s\n",key,h.getMorada(),h.getOwnerName(),h.getFornecedor());
                }
                System.out.println("------------------------------------------------------");
                
                int house_id = 0; 
                while(flag){
                    try {
                        System.out.print("Insira o ID da casa: ");
                        house_id = Integer.parseInt(s.nextLine());
                        if(aux.containsKey(house_id)){
                            flag = false;
                        }else{
                            System.out.printf("A casa com id %d não existe!\n",house_id);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("O id da casa é um número inteiro.");
                    }
                }
                CasaInteligente h = this.sim.getHouse(house_id);
                System.out.println(h.devicesPerRoomInfo());
                flag = true;
                String room = null;
                while(flag){
                    System.out.print("Insira a repartição: ");
                    room = s.nextLine();
                    if(h.hasRoom(room)){
                        flag = false;
                    }else{
                        System.out.printf("A repartição: %s não existe\n",room);
                    }
                }
                String auxR = room; Integer auxI = house_id; LocalDateTime auxDT = executionDateTime;
                result = new Command(executionDateTime, sim -> sim.setStateAllDevicesInRoom(auxI, auxR, true, auxDT), true);
                break;
            }

            case 7:{
                Map<Integer,CasaInteligente> aux = this.sim.getHousesMap();
                System.out.println("-------------------------Casas------------------------");
                for(Integer key: aux.keySet()){
                    CasaInteligente h = aux.get(key);
                    System.out.printf("ID: %d; Morada: %s; Proprietário: %s; Fornecedor: %s\n",key,h.getMorada(),h.getOwnerName(),h.getFornecedor());
                }
                System.out.println("------------------------------------------------------");
                
                int house_id = 0; 
                while(flag){
                    try {
                        System.out.print("Insira o ID da casa: ");
                        house_id = Integer.parseInt(s.nextLine());
                        if(aux.containsKey(house_id)){
                            flag = false;
                        }else{
                            System.out.printf("A casa com id %d não existe!\n",house_id);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("O id da casa é um número inteiro.");
                    }
                }
                CasaInteligente h = this.sim.getHouse(house_id);
                System.out.println(h.devicesPerRoomInfo());
                flag = true;
                String room = null;
                while(flag){
                    System.out.print("Insira a repartição: ");
                    room = s.nextLine();
                    if(h.hasRoom(room)){
                        flag = false;
                    }else{
                        System.out.printf("A repartição: %s não existe\n",room);
                    }
                }
                String auxR = room; Integer auxI = house_id; LocalDateTime auxDT = executionDateTime;
                result = new Command(executionDateTime, sim -> sim.setStateAllDevicesInRoom(auxI, auxR, false, auxDT), true);
                break;
            }

            case 8:{
                LocalDateTime auxDT = executionDateTime;
                result = new Command(executionDateTime, sim -> sim.setStateAllDevicesInAllHouses(true, auxDT), true);
                break;
            }

            case 9:{
                LocalDateTime auxDT = executionDateTime;
                result = new Command(executionDateTime, sim -> sim.setStateAllDevicesInAllHouses(false, auxDT), true);
                break;
            }

            case 10:{// ligar um dispositivo de uma casa;
                Map<Integer,CasaInteligente> aux = this.sim.getHousesMap();
                System.out.println("-------------------------Casas------------------------");
                for(Integer key: aux.keySet()){
                    CasaInteligente h = aux.get(key);
                    System.out.printf("ID: %d; Morada: %s; Proprietário: %s; Fornecedor: %s\n",key,h.getMorada(),h.getOwnerName(),h.getFornecedor());
                }
                System.out.println("------------------------------------------------------");
                
                int house_id = 0; 
                while(flag){
                    try {
                        System.out.print("Insira o ID da casa: ");
                        house_id = Integer.parseInt(s.nextLine());
                        if(aux.containsKey(house_id)){
                            flag = false;
                        }else{
                            System.out.printf("A casa com id %d não existe!\n",house_id);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("O id da casa é um número inteiro.");
                    }
                }
                CasaInteligente h = this.sim.getHouse(house_id);
                System.out.println(h.devicesPerRoomInfo());
                flag = true;
                String devID = null;
                while(flag){
                    System.out.print("Insira o id do dispositivo: ");
                    devID = s.nextLine();
                    if(h.existsDevice(devID)){
                        flag = false;
                    }else{
                        System.out.printf("O dispositivo com id %s não existe\n",devID);
                    }
                }
                String auxDI = devID; int auxHI = house_id; LocalDateTime auxDT = executionDateTime;
                result = new Command(executionDateTime, sim -> sim.setStateInDeviceInHouse(auxDI, auxHI, true, auxDT), true);
                break;
            }

            case 11:{// desligar um dispositivo de uma casa;
                Map<Integer,CasaInteligente> aux = this.sim.getHousesMap();
                System.out.println("-------------------------Casas------------------------");
                for(Integer key: aux.keySet()){
                    CasaInteligente h = aux.get(key);
                    System.out.printf("ID: %d; Morada: %s; Proprietário: %s; Fornecedor: %s\n",key,h.getMorada(),h.getOwnerName(),h.getFornecedor());
                }
                System.out.println("------------------------------------------------------");
                
                int house_id = 0; 
                while(flag){
                    try {
                        System.out.print("Insira o ID da casa: ");
                        house_id = Integer.parseInt(s.nextLine());
                        if(aux.containsKey(house_id)){
                            flag = false;
                        }else{
                            System.out.printf("A casa com id %d não existe!\n",house_id);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("O id da casa é um número inteiro.");
                    }
                }
                CasaInteligente h = this.sim.getHouse(house_id);
                System.out.println(h.devicesPerRoomInfo());
                flag = true;
                String devID = null;
                while(flag){
                    System.out.print("Insira o id do dispositivo: ");
                    devID = s.nextLine();
                    if(h.existsDevice(devID)){
                        flag = false;
                    }else{
                        System.out.printf("O dispositivo com id %s não existe\n",devID);
                    }
                }
                String auxDI = devID; int auxHI = house_id; LocalDateTime auxDT = executionDateTime;
                result = new Command(executionDateTime, sim -> sim.setStateInDeviceInHouse(auxDI, auxHI, false, auxDT), true);
                break;
            }

            case 12:{
                Map<Integer,CasaInteligente> aux = this.sim.getHousesMap();
                System.out.println("-------------------------Casas------------------------");
                for(Integer key: aux.keySet()){
                    CasaInteligente h = aux.get(key);
                    System.out.printf("ID: %d; Morada: %s; Proprietário: %s; Fornecedor: %s\n",key,h.getMorada(),h.getOwnerName(),h.getFornecedor());
                }
                System.out.println("------------------------------------------------------");
                
                int house_id = 0; 
                while(flag){
                    try {
                        System.out.print("Insira o ID da casa: ");
                        house_id = Integer.parseInt(s.nextLine());
                        if(aux.containsKey(house_id)){
                            flag = false;
                        }else{
                            System.out.printf("A casa com id %d não existe!\n",house_id);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("O id da casa é um número inteiro.");
                    }
                }
                flag = true;
                CasaInteligente h = this.sim.getHouse(house_id);
                Set<SmartDevice> bulbs = h.getMapDevices().values().stream().filter(dev -> dev.getClass().getName().equals("SmartBulb")).collect(Collectors.toSet());
                Set<String> bulbsIDs = bulbs.stream().map(SmartDevice::getID).collect(Collectors.toSet());
                System.out.println("------------------Lâmpadas------------------");
                for(SmartDevice dev: bulbs){
                    SmartBulb b = (SmartBulb)dev;
                    String tone = b.getTone() == 0 ? "COLD" : (b.getTone() == 1 ? "NEUTRAL" : (b.getTone() == 2 ? "WARM" : "???"));
                    System.out.printf("SmartBulb ID: %s, TONE: %s\n",b.getID(),tone);
                }
                System.out.println("--------------------------------------------");
                String bID = "";
                while(flag){
                    System.out.print("Insira o id da lâmpada: ");
                    bID = s.nextLine();
                    if(bulbsIDs.contains(bID)){
                        flag = false;
                    }else{
                        System.out.printf("A lâmpada com id %s não existe\n",bID);
                    }
                }
                flag = true;
                int new_tone = -1;
                while(flag){
                    try{
                        System.out.print("Insira a tonalidade (0: Cold, 1: Neutral, 2: Warm): ");
                        new_tone = Integer.parseInt(s.nextLine());
                        if(new_tone == 0 || new_tone == 1 || new_tone == 2){
                            flag = false;
                        }else{
                            System.out.println("Tonalidade inválida, insira 0, 1 ou 2");
                        }
                    }catch(NumberFormatException e){
                        System.out.println("A tonalidade é um número inteiro!");
                    }
                }
                LocalDateTime auxDT = executionDateTime;
                int houseID = house_id;
                int newTone = new_tone;
                String bulbID = bID;
                result = new Command(executionDateTime,sim -> sim.changeBulbToneInHouse(houseID, bulbID, newTone, auxDT),true);
                break;
            }

            case 13:{
                Map<Integer,CasaInteligente> aux = this.sim.getHousesMap();
                System.out.println("-------------------------Casas------------------------");
                for(Integer key: aux.keySet()){
                    CasaInteligente h = aux.get(key);
                    System.out.printf("ID: %d; Morada: %s; Proprietário: %s; Fornecedor: %s\n",key,h.getMorada(),h.getOwnerName(),h.getFornecedor());
                }
                System.out.println("------------------------------------------------------");
                
                int house_id = 0; 
                while(flag){
                    try {
                        System.out.print("Insira o ID da casa: ");
                        house_id = Integer.parseInt(s.nextLine());
                        if(aux.containsKey(house_id)){
                            flag = false;
                        }else{
                            System.out.printf("A casa com id %d não existe!\n",house_id);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("O id da casa é um número inteiro.");
                    }
                }
                flag = true;
                CasaInteligente h = this.sim.getHouse(house_id);
                Set<SmartDevice> speakers = h.getMapDevices().values().stream().filter(dev -> dev.getClass().getName().equals("SmartSpeaker")).collect(Collectors.toSet());
                Set<String> speakersIDs = speakers.stream().map(SmartDevice::getID).collect(Collectors.toSet());
            
                System.out.println("------------------Speakers------------------");
                for(SmartDevice dev: speakers){
                    SmartSpeaker sp = (SmartSpeaker)dev;
                    System.out.printf("SmartSpeaker ID: %s, Canal: %s\n",sp.getID(),sp.getChannel());
                }
                System.out.println("--------------------------------------------");
                String sID = "";
                while(flag){
                    System.out.print("Insira o id do speaker: ");
                    sID = s.nextLine();
                    if(speakersIDs.contains(sID)){
                        flag = false;
                    }else{
                        System.out.printf("O speaker com id %s não existe\n",sID);
                    }
                }
                System.out.print("Insira o canal: ");
                String newChannel = s.nextLine();
                String speakerID = sID;
                int houseID = house_id;
                result = new Command(executionDateTime, sim -> sim.changeSpeakerChannelInHouse(houseID, speakerID, newChannel), true);
                break;
            }

            case 14:{
                Map<Integer,CasaInteligente> aux = this.sim.getHousesMap();
                System.out.println("-------------------------Casas------------------------");
                for(Integer key: aux.keySet()){
                    CasaInteligente h = aux.get(key);
                    System.out.printf("ID: %d; Morada: %s; Proprietário: %s; Fornecedor: %s\n",key,h.getMorada(),h.getOwnerName(),h.getFornecedor());
                }
                System.out.println("------------------------------------------------------");
                
                int house_id = 0; 
                while(flag){
                    try {
                        System.out.print("Insira o ID da casa: ");
                        house_id = Integer.parseInt(s.nextLine());
                        if(aux.containsKey(house_id)){
                            flag = false;
                        }else{
                            System.out.printf("A casa com id %d não existe!\n",house_id);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("O id da casa é um número inteiro.");
                    }
                }
                flag = true;
                CasaInteligente h = this.sim.getHouse(house_id);
                Set<SmartDevice> speakers = h.getMapDevices().values().stream().filter(dev -> dev.getClass().getName().equals("SmartSpeaker")).collect(Collectors.toSet());
                Set<String> speakersIDs = speakers.stream().map(SmartDevice::getID).collect(Collectors.toSet());
                
                System.out.println("------------------Speakers------------------");
                for(SmartDevice dev: speakers){
                    SmartSpeaker sp = (SmartSpeaker)dev;
                    System.out.printf("SmartSpeaker ID: %s, Canal: %s\n",sp.getID(),sp.getChannel());
                }
                System.out.println("--------------------------------------------");
                String sID = "";
                while(flag){
                    System.out.print("Insira o id do speaker: ");
                    sID = s.nextLine();
                    if(speakersIDs.contains(sID)){
                        flag = false;
                    }else{
                        System.out.printf("O speaker com id %s não existe\n",sID);
                    }
                }
                int houseID = house_id;
                String speakerID = sID;
                LocalDateTime auxDT = executionDateTime;
                result = new Command(executionDateTime, sim -> sim.volumeDownSpeakerInHouse(houseID, speakerID, auxDT), true);
                break;
            }

            case 15:{
                Map<Integer,CasaInteligente> aux = this.sim.getHousesMap();
                System.out.println("-------------------------Casas------------------------");
                for(Integer key: aux.keySet()){
                    CasaInteligente h = aux.get(key);
                    System.out.printf("ID: %d; Morada: %s; Proprietário: %s; Fornecedor: %s\n",key,h.getMorada(),h.getOwnerName(),h.getFornecedor());
                }
                System.out.println("------------------------------------------------------");
                
                int house_id = 0; 
                while(flag){
                    try {
                        System.out.print("Insira o ID da casa: ");
                        house_id = Integer.parseInt(s.nextLine());
                        if(aux.containsKey(house_id)){
                            flag = false;
                        }else{
                            System.out.printf("A casa com id %d não existe!\n",house_id);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("O id da casa é um número inteiro.");
                    }
                }
                flag = true;
                CasaInteligente h = this.sim.getHouse(house_id);
                Set<SmartDevice> speakers = h.getMapDevices().values().stream().filter(dev -> dev.getClass().getName().equals("SmartSpeaker")).collect(Collectors.toSet());
                Set<String> speakersIDs = speakers.stream().map(SmartDevice::getID).collect(Collectors.toSet());
                
                System.out.println("------------------Speakers------------------");
                for(SmartDevice dev: speakers){
                    SmartSpeaker sp = (SmartSpeaker)dev;
                    System.out.printf("SmartSpeaker ID: %s, Canal: %s\n",sp.getID(),sp.getChannel());
                }
                System.out.println("--------------------------------------------");
                String sID = "";
                while(flag){
                    System.out.print("Insira o id do speaker: ");
                    sID = s.nextLine();
                    if(speakersIDs.contains(sID)){
                        flag = false;
                    }else{
                        System.out.printf("O speaker com id %s não existe\n",sID);
                    }
                }
                int houseID = house_id;
                String speakerID = sID;
                LocalDateTime auxDT = executionDateTime;
                result = new Command(executionDateTime, sim -> sim.volumeUpSpeakerInHouse(houseID, speakerID, auxDT), true);
                break;
            }
        
            default:{
                System.out.println("Opção inválida (deve ser um inteiro entre 1 e 15)");
                break;
            }
        }
        return result;
    }

    private void simulationExecution(Scanner s){
        boolean flag = true;
        int option = 0;
        List<Command> requests = new ArrayList<>();
        while(flag){
            System.out.println("---------------------------------------------------------");
            System.out.println("| Executar simulação                                    |");
            System.out.println("---------------------------------------------------------");
            System.out.println("| 1  | Adicionar um pedido                              |");
            System.out.println("---------------------------------------------------------");
            System.out.println("| 2  | Iniciar simulação                                |");
            System.out.println("---------------------------------------------------------");
            System.out.println("| 3  | Adicionar pedidos de um ficheiro                 |");
            System.out.println("---------------------------------------------------------");
            System.out.println("| 4  | Ver a casa que mais consumiu                     |");
            System.out.println("---------------------------------------------------------");
            System.out.println("| 5  | Ver o fornecedor com maior volume de faturação   |");
            System.out.println("---------------------------------------------------------");
            System.out.println("| 6  | Ver as faturas emitidas por um fornecedor        |");
            System.out.println("---------------------------------------------------------");
            System.out.println("| 7  | Ranking de consumidores de energia               |");
            System.out.println("---------------------------------------------------------");
            System.out.println("| 8  | Ver informações das casas                        |");
            System.out.println("---------------------------------------------------------");
            System.out.println("| 9  | Ver informações dos fornecedores de energia      |");
            System.out.println("---------------------------------------------------------");
            System.out.println("| 10 | Voltar ao menu anterior                          |");
            System.out.println("---------------------------------------------------------");
            while(flag){
                try {
                    System.out.print("Insira a opção: ");
                    option = Integer.parseInt(s.nextLine());
                    flag = false;
                } catch (NumberFormatException e) {
                    System.out.println("A opção deve ser um número inteiro!");
                }
            }
            flag = true;

            switch(option){

                case 1: {
                    requests.add(this.commandFromInput(s));
                    break;
                }

                case 2: {
                    if(this.sim.isReady()){
                        LocalDateTime start, end;
                        start = getDateFromInput("Insira a data e hora do início da simulação: ",s);
                        end = getDateFromInput("Insira a data e hora do fim da simulação: ",s);
                        requests.sort((c1,c2) -> c1.getExecutionDateTime().compareTo(c2.getExecutionDateTime()));
                        this.sim.startSimulation(start, end, requests);
                        requests.clear();
                    }else{
                        System.out.println("A simulação não está pronta a executar.\nVerifique as informações nela presentes.\nPressione ENTER para continuar");
                        s.nextLine();
                    }
                    break;
                }

                case 3: {
                    System.out.print("Insira o nome (caminho) do ficheiro: ");
                    String path = s.nextLine();
                    try {
                        requests = Command.commandsFromFile(path);
                    } catch (IOException e) {
                        System.out.println("Erro de IO\nPressione ENTER para continuar");
                        s.nextLine();
                    }
                    break;
                }

                case 4: {
                    CasaInteligente house = this.sim.getBiggestConsumer();
                    if(house != null){
                        System.out.println("-------------------------------");
                        System.out.printf("Morada: %s\n",house.getMorada());
                        System.out.printf("Nome proprietário: %s\nNIF proprietário: %d\n",house.getOwnerName(),house.getOwnerNif());
                        System.out.printf("Fornecedor: %s\n",house.getFornecedor());
                        System.out.printf("Consumo: %f\n",house.getTotalConsumption());
                        System.out.println("-------------------------------");
                        System.out.println("Pressione ENTER para continuar\n");
                        s.nextLine();
                    }else{
                        System.out.println("Sem informação disponível\nProvavelmente a simulação não foi executada.\nPressione ENTER para continuar");
                        s.nextLine();
                    }
                    break;
                }

                case 5: {
                    EnergyProvider provider = this.sim.getBiggestProvider();
                    if(provider != null){
                        System.out.println("-------------------------------");
                        System.out.printf("Fornecedor: %s\n",provider.getName());
                        System.out.printf("Preço kWh: %f\n",provider.getPrice_kwh());
                        System.out.printf("Imposto: %f\n",provider.getTax());
                        System.out.printf("Lucro: %.2f €\n",this.sim.getProfitOfProvider(provider.getName()));
                        System.out.println("-------------------------------");
                        System.out.println("Pressione ENTER para continuar\n");
                        s.nextLine();
                    }else{
                        System.out.println("Sem informação disponível\nProvavelmente a simulação não foi executada.\nPressione ENTER para continuar");
                        s.nextLine();
                    }
                    
                    break;
                }

                case 6: {
                    String provID = null;
                    while(flag){
                        System.out.print("Insira o id do fornecedor: ");
                        provID = s.nextLine();
                        if(this.sim.existsProvider(provID)){
                            flag = false;
                        }else{
                            System.out.printf("O fornecedor com id %s não existe!\n",provID);
                        }
                    }
                    flag = true;
                    List<Fatura> faturas = this.sim.getBillsFromProvider(provID);
                    if(faturas.size() > 0){
                        for(Fatura fat: this.sim.getBillsFromProvider(provID)){
                            System.out.println(fat.printFatura());
                        }
                        System.out.println("Pressione ENTER para continuar\n");
                        s.nextLine();
                    }else{
                        System.out.println("Sem informação disponível\nProvavelmente a simulação não foi executada.\nPressione ENTER para continuar");
                        s.nextLine();
                    }
                    
                    break;
                }

                case 7: {
                    List <CasaInteligente> consOrder = this.sim.getConsumptionOrder();
                    if(consOrder != null && consOrder.size() > 0){
                        System.out.println("-------------------------------------------------------");
                        for(CasaInteligente house: consOrder){
                            System.out.printf("Morada: %s\n",house.getMorada());
                            System.out.printf("Nome proprietário: %s\nNIF proprietário: %d\n",house.getOwnerName(),house.getOwnerNif());
                            System.out.printf("Fornecedor: %s\n",house.getFornecedor());
                            System.out.printf("Consumo: %f kWh\n",house.getTotalConsumption());
                            System.out.println("-------------------------------------------------------");
                        }
                        System.out.println("Pressione ENTER para continuar\n");
                        s.nextLine();
                    }else{
                        System.out.println("Sem informação disponível\nProvavelmente a simulação não foi executada.\nPressione ENTER para continuar");
                        s.nextLine();
                    }
                    break;
                }

                case 8:{
                    Map<Integer,CasaInteligente> aux = this.sim.getHousesMap();
                    System.out.println("-------------------------Casas------------------------");
                    for(Integer key: aux.keySet()){
                        CasaInteligente h = aux.get(key);
                        System.out.printf("ID: %d; Morada: %s; Proprietário: %s; Fornecedor: %s\n",key,h.getMorada(),h.getOwnerName(),h.getFornecedor());
                        System.out.println(h.devicesPerRoomInfo());
                    }
                    System.out.println("------------------------------------------------------");
                    System.out.println("Pressione ENTER para continuar");
                    s.nextLine();
                    break;
                }

                case 9:{
                    Map<String,EnergyProvider> aux = this.sim.getProvidersMap();
                    System.out.println("-------------------------Fornecedores-------------------------");
                    for(String key: aux.keySet()){
                        EnergyProvider p = aux.get(key);
                        System.out.printf("ID: %s; Nome: %s, Preço/kWh: %f; Imposto: %f\n",key,p.getName(),p.getPrice_kwh(),p.getTax());
                    }
                    System.out.println("-------------------------------------------------------------");
                    System.out.println("Pressione ENTER para continuar");
                    s.nextLine();
                    break;
                }

                case 10:{ 
                    flag = false;
                    break;
                }

                default:{
                    System.out.println("Opção inválida (deve ser um inteiro entre 1 e 6)");
                }
            }

        }
    }

    private int getMainOption(Scanner s){
        int op = 0; boolean flag = true;
        System.out.println("--------------------------------------------------------");
        System.out.println("| 1 | Carregar informação de ficheiro                  |");
        System.out.println("--------------------------------------------------------");
        System.out.println("| 2 | Carregar informação manualmente                  |");
        System.out.println("--------------------------------------------------------");
        System.out.println("| 3 | Guardar estado atual em ficheiro                 |");
        System.out.println("--------------------------------------------------------");
        System.out.println("| 4 | Iniciar simulação                                |");
        System.out.println("--------------------------------------------------------");
        System.out.println("| 5 | Sair                                             |");
        System.out.println("--------------------------------------------------------");
        while (flag){
            try {
                System.out.print("Insira a opção: ");
                op = Integer.parseInt(s.nextLine());
                flag = false;
            } catch (NumberFormatException e) {
                System.out.println("A opção deve ser um número inteiro!");
            } catch (Exception e){
                System.out.println("Ocorreu um erro desconhecido!");
            }
        }
        return op;
    }

    public void run() {
        Scanner s = new Scanner(System.in);
        boolean flag = true;
        int option;

        while (flag) {
            option = getMainOption(s);
            switch (option){
                case 1:{
                    System.out.print("Insira o caminho para o ficheiro: ");
                    String path = s.nextLine();
                    try {
                        sim = Simulator.loadState(path);
                        System.out.println("Estado carregado com sucesso!\nPressione ENTER para continuar");
                        s.nextLine();
                    } catch (FileNotFoundException e){
                        System.out.printf("O ficheiro %s não existe\nPressione ENTER para continuar\n",path);
                        s.nextLine();
                    } catch (ClassNotFoundException e){
                        System.out.println("A classe não existe\nPressione ENTER para continuar");
                        s.nextLine();
                    } catch (IOException e){
                        System.out.println("Erro de input/output\nPressione ENTER para continuar");
                        s.nextLine();
                    }/*
                    Map<String,EnergyProvider> providers = null;
                    List<CasaInteligente> houses = null;
                    try {
                        providers = Generator.fileToProviders("/home/orlando/Desktop/Trabalho-POO-2021_2022/files/providers.txt");
                        houses = Generator.fileToHouses("/home/orlando/Desktop/Trabalho-POO-2021_2022/files/devices.txt", 
                                                "/home/orlando/Desktop/Trabalho-POO-2021_2022/files/people.txt",
                                                "/home/orlando/Desktop/Trabalho-POO-2021_2022/files/houses.txt");
                    } catch (FileNotFoundException e) {}
                    this.sim = new Simulator(houses, providers.values());
                    break;*/
                    break;
                }
                case 2:{
                    this.stateFromInput(s);
                    break;
                }
                case 3:{
                    if(!(this.sim.isReady())){ // o isReady indica se a simulação é válida/está pronta
                        System.out.println("Não existem dados para ser guardados!\nPressione ENTER para continuar");
                        s.nextLine();
                    } else{
                        System.out.print("Insira o caminho para o ficheiro: ");
                        String path = s.nextLine();
                        try{
                            sim.saveState(path);
                            System.out.println("Informação guardada com sucesso!\nPressione ENTER para continuar");
                            s.nextLine();
                        } catch (FileNotFoundException e){
                            System.out.printf("O ficheiro %s não existe\nPressione ENTER para continuar\n");
                            s.nextLine();
                        } catch (IOException e){
                            System.out.println("Erro do input/output\nPressione ENTER para continuar");
                            s.nextLine();
                        }
                        
                    }
                    break;
                }

                case 4:{
                    this.simulationExecution(s);
                    break;
                }

                case 5:{
                    flag = false;
                    break;
                }

                default:{
                    System.out.println("Opção inválida (deve ser um inteiro entre 1 e 5)");
                    System.out.println("Pressione ENTER para continuar");
                    s.nextLine();
                    break;
                }
            }
        }
        s.close();
    }
}
