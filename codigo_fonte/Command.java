import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Command{

    /**
     * Instante em que este comando é executado.
     */
    private LocalDateTime executionDateTime;

    /**
     * Tratamento a ser aplicado na simulação.
     */
    private Consumer<Simulator> command;

    /**
     * Indica se o comando pode ser executado durante a simulação (true) ou apenas no fim da simulação (false).
     */
    private boolean flag;
    
    public Command(LocalDateTime executionDateTime, Consumer<Simulator> command, boolean flag){
        this.executionDateTime = executionDateTime;
        this.command = command;
        this.flag = flag;
    }

    /**
     * Executa este comando na simulação fornecida.
     */
    public void execute(Simulator simulator){
        this.command.accept(simulator);
    }

    /**
     * Retorna o instante em que este comando é executado.
     */
    public LocalDateTime getExecutionDateTime() {
        return this.executionDateTime;
    }

    /**
     * Retorna o valor da flag.
     */
    public boolean getFlag(){
        return this.flag;
    }

    /**
     * Faz o parse de uma string e devolve no formato de um comando.
     */
    public static Command fromString(String s) throws InvalidCommandException {
        // DATETIME,id_elemento,nome_operacao,novo_valor ou identificação de algo (dispositivo, fornecedor, etc...)
        String[] aux = s.split(",");
        // Comandos com configuração particular (sem id's associados)
        try {
            switch(aux[1]){
                case "ligaTudoEmTodasAsCasas":{
                    try{
                        LocalDateTime auxDT = LocalDateTime.parse(aux[0], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                        return new Command(auxDT, sim -> sim.setStateAllDevicesInAllHouses(true, auxDT), true);
                    }catch (DateTimeParseException e){
                        throw new InvalidCommandException(String.format("O comando '%s' tem uma data com formato inválido"));
                    }
                }
                
                case "desligaTudoEmTodasAsCasas":{
                    try{
                        LocalDateTime auxDT = LocalDateTime.parse(aux[0], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                        return new Command(auxDT, sim -> sim.setStateAllDevicesInAllHouses(false, auxDT), true);
                    }catch (DateTimeParseException e){
                        throw new InvalidCommandException(String.format("O comando '%s' tem uma data com formato inválido"));
                    }
                }
    
                default: break;
            }
            
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidCommandException(String.format("O comando '%s' não tem argumentos suficientes"));
        }

        // Comandos normais
        LocalDateTime executionDateTime = null;
        String id = null ,action = null;
        try {
            executionDateTime = LocalDateTime.parse(aux[0],DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            id = aux[1]; action = aux[2];
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidCommandException(String.format("O comando '%s' não tem argumentos suficientes.",s));
        } catch (DateTimeParseException e){
            throw new InvalidCommandException(String.format("O comando '%s' tem uma data com formato inválido.", s));
        }

        Command result = null;
        switch(action){

            case "alteraPreco":{
                double price = 0.0;
                try {
                    price = Double.parseDouble(aux[3]);
                } catch (NumberFormatException e){
                    throw new InvalidCommandException(String.format("Preço com formato inválido no comando '%s'",s));
                } catch (IndexOutOfBoundsException e) {
                    throw new InvalidCommandException(String.format("O comando '%s' não tem argumentos suficientes.",s));
                }
                double new_price = price; 
                String provID = id;
                result = new Command(executionDateTime, sim -> sim.changeProviderPrice(provID,new_price), false);
                break;
            }

            case "alteraImposto":{
                double tax = 0.0;
                try {
                    tax = Double.parseDouble(aux[3]);
                } catch (NumberFormatException e){
                    throw new InvalidCommandException(String.format("Impostos com formato inválido no comando '%s'",s));
                } catch (IndexOutOfBoundsException e) {
                    throw new InvalidCommandException(String.format("O comando '%s' não tem argumentos suficientes.",s));
                }
                double new_tax = tax; 
                String provID = id;
                result = new Command(executionDateTime, sim -> sim.changeProviderTax(provID,new_tax), false);
                break;
            }

            case "alteraFornecedor":{
                int house_id = 0;
                String new_provider = "";
                try{
                    new_provider = aux[3];
                    house_id = Integer.parseInt(id);
                } catch (NumberFormatException e){
                    throw new InvalidCommandException(String.format("ID da casa com formato inválido no comando '%s'",s));
                } catch (IndexOutOfBoundsException e) {
                    throw new InvalidCommandException(String.format("O comando '%s' não tem argumentos suficientes.",s));
                }
                String newProv = new_provider;
                int houseID = house_id;
                result = new Command(executionDateTime, sim -> sim.changeHouseProvider(newProv, houseID), false);
                break;
            }

            case "ligaTodosNaCasa":{
                int house_id = 0;
                try{
                    house_id = Integer.parseInt(id);
                } catch (NumberFormatException e){
                    throw new InvalidCommandException(String.format("ID da casa com formato inválido no comando '%s'",s));
                }
                int houseID = house_id;
                LocalDateTime dateTime = executionDateTime;
                result = new Command(executionDateTime, sim -> sim.setStateAllDevicesHouse(houseID, true, dateTime), true);
                break;
            }

            case "desligaTodosNaCasa":{
                int house_id = 0;
                try{
                    house_id = Integer.parseInt(id);
                } catch (NumberFormatException e){
                    throw new InvalidCommandException(String.format("ID da casa com formato inválido no comando '%s'",s));
                }
                int houseID = house_id;
                LocalDateTime dateTime = executionDateTime;
                result = new Command(executionDateTime, sim -> sim.setStateAllDevicesHouse(houseID, false, dateTime), true);
                break;
            }

            case "ligaTodosNaReparticao":{
                int house_id = 0;
                String room_ = "";
                try {
                    house_id = Integer.parseInt(id);
                    room_ = aux[3];
                } catch (NumberFormatException e) {
                    throw new InvalidCommandException(String.format("ID da casa com formato inválido no comando '%s'",s));
                } catch (IndexOutOfBoundsException e) {
                    throw new InvalidCommandException(String.format("O comando '%s' não tem argumentos suficientes.",s));
                }
                int houseID = house_id;
                String room = room_;
                LocalDateTime dateTime = executionDateTime;
                result = new Command(executionDateTime, sim -> sim.setStateAllDevicesInRoom(houseID, room, true, dateTime), true);
                break;
            }

            case "desligaTodosNaReparticao":{
                int house_id = 0;
                String room_ = "";
                try {
                    house_id = Integer.parseInt(id);
                    room_ = aux[3];
                } catch (NumberFormatException e) {
                    throw new InvalidCommandException(String.format("ID da casa com formato inválido no comando '%s'",s));
                } catch (IndexOutOfBoundsException e) {
                    throw new InvalidCommandException(String.format("O comando '%s' não tem argumentos suficientes.",s));
                }
                int houseID = house_id;
                String room = room_;
                LocalDateTime dateTime = executionDateTime;
                result = new Command(executionDateTime, sim -> sim.setStateAllDevicesInRoom(houseID, room, false, dateTime), true);
                break;
            }

            case "ligaDispositivoNaCasa":{
                int house_id = 0;
                String dev_id = "";
                try {
                    house_id = Integer.parseInt(id);
                    dev_id = aux[3];
                } catch (NumberFormatException e) {
                    throw new InvalidCommandException(String.format("ID da casa com formato inválido no comando '%s'",s));
                } catch (IndexOutOfBoundsException e) {
                    throw new InvalidCommandException(String.format("O comando '%s' não tem argumentos suficientes.",s));
                }
                int houseID = house_id;
                LocalDateTime dateTime = executionDateTime;
                String devID = dev_id;
                result = new Command(executionDateTime,sim -> sim.setStateInDeviceInHouse(devID, houseID, true, dateTime), true);
                break;
            }

            case "desligaDispositivoNaCasa":{
                int house_id = 0;
                String dev_id = "";
                try {
                    house_id = Integer.parseInt(id);
                    dev_id = aux[3];
                } catch (NumberFormatException e) {
                    throw new InvalidCommandException(String.format("ID da casa com formato inválido no comando '%s'",s));
                } catch (IndexOutOfBoundsException e) {
                    throw new InvalidCommandException(String.format("O comando '%s' não tem argumentos suficientes.",s));
                }
                int houseID = house_id;
                LocalDateTime dateTime = executionDateTime;
                String devID = dev_id;
                result = new Command(executionDateTime,sim -> sim.setStateInDeviceInHouse(devID, houseID, false, dateTime), true);
                break;
            }

            case "alteraTonalidade":{
                //data,id_casa,alteraTonalidade,id_lampada,tonalidade
                int house_id = 0, tone = -1;
                String bulb_id = "", toneS = "";
                try {
                    house_id = Integer.parseInt(id);
                    bulb_id = aux[3];
                    toneS = aux[4].toLowerCase();
                } catch (NumberFormatException e) {
                    throw new InvalidCommandException(String.format("ID da casa com formato inválido no comando '%s'",s));
                } catch (IndexOutOfBoundsException e) {
                    throw new InvalidCommandException(String.format("O comando '%s' não tem argumentos suficientes.",s));
                }

                if(!(toneS.equals("cold") || toneS.equals("warm") || toneS.equals("neutral"))){
                    throw new InvalidCommandException(String.format("A tonalidade no comando '%s' é inválida",s));
                }
                switch(toneS){
                    case "cold": tone = 0; break;
                    case "neutral": tone = 1; break;
                    case "warm": tone = 2; break;
                }

                int houseID = house_id;
                int newTone = tone;
                String bulbID = bulb_id;
                LocalDateTime dateTime = executionDateTime;
                result = new Command(executionDateTime, sim -> sim.changeBulbToneInHouse(houseID, bulbID, newTone, dateTime),true);
                break;
            }

            case "alteraCanal":{
                //data,id_casa,alteraCanal,id_speaker,canal
                int house_id = 0;
                String speaker_id = "", channel = "";
                try {
                    house_id = Integer.parseInt(id);
                    speaker_id = aux[3];
                    channel = aux[4];
                } catch (NumberFormatException e) {
                    throw new InvalidCommandException(String.format("ID da casa com formato inválido no comando '%s'",s));
                } catch (IndexOutOfBoundsException e) {
                    throw new InvalidCommandException(String.format("O comando '%s' não tem argumentos suficientes.",s));
                }

                int houseID = house_id;
                String speakerID = speaker_id, canal = channel;
                result = new Command(executionDateTime, sim -> sim.changeSpeakerChannelInHouse(houseID, speakerID, canal), true);
                break;
            }

            case "aumentaVolume":{
                //data,id_casa,aumentaVolume,id_speaker
                int house_id = 0;
                String speaker_id = "";
                try {
                    house_id = Integer.parseInt(id);
                    speaker_id = aux[3];
                } catch (NumberFormatException e) {
                    throw new InvalidCommandException(String.format("ID da casa com formato inválido no comando '%s'",s));
                } catch (IndexOutOfBoundsException e) {
                    throw new InvalidCommandException(String.format("O comando '%s' não tem argumentos suficientes.",s));
                }

                int houseID = house_id;
                String speakerID = speaker_id;
                LocalDateTime dateTime = executionDateTime;
                result = new Command(executionDateTime, sim -> sim.volumeUpSpeakerInHouse(houseID, speakerID, dateTime),true);
                break;
            }

            case "diminuiVolume":{
                //data,id_casa,diminuiVolume,id_speaker
                int house_id = 0;
                String speaker_id = "";
                try {
                    house_id = Integer.parseInt(id);
                    speaker_id = aux[3];
                } catch (NumberFormatException e) {
                    throw new InvalidCommandException(String.format("ID da casa com formato inválido no comando '%s'",s));
                } catch (IndexOutOfBoundsException e) {
                    throw new InvalidCommandException(String.format("O comando '%s' não tem argumentos suficientes.",s));
                }

                int houseID = house_id;
                String speakerID = speaker_id;
                LocalDateTime dateTime = executionDateTime;
                result = new Command(executionDateTime, sim -> sim.volumeDownSpeakerInHouse(houseID, speakerID, dateTime),true);
                break;
            }

            default:{
                throw new InvalidCommandException(String.format("O comando '%s' é inválido",s));
            }
        }
        return result;
    }
    
    /**
     * Lê um ficheiro de texto que contém os comandos.
     * Se algum comando for inválido, o mesmo é descartado.
     */   
    public static List<Command> commandsFromFile(String filePath) throws IOException{
        List<Command> result = new ArrayList<>();
        List<String> lines = new ArrayList<>();
        lines = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
        for(String line: lines){
            try{result.add(fromString(line));}
            catch(InvalidCommandException e){System.out.println(e.getMessage());}
        }
        result.sort((c1,c2) -> c1.getExecutionDateTime().compareTo(c2.getExecutionDateTime()));
        return result;
    }

}
