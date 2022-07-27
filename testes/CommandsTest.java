import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class CommandsTest {
    
    @Test
    public void alteraPrecoTest(){
        Simulator sim = null;
        double novoPreco = 1.00;
        List<Command> cmds = new ArrayList<>();
        cmds.add(new Command(LocalDateTime.of(2022,4,5,12,30),simul -> simul.changeProviderPrice("edp",novoPreco),false));
        try {
            sim = Simulator.loadState("/home/orlando/Desktop/Trabalho-POO-2021_2022/files/stateForTesting.obj");
            sim.startSimulation(LocalDateTime.of(2022,04,01,10,0),LocalDateTime.of(2022,04,30,10,00),cmds);
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); 
        } catch (IOException e) {
            e.printStackTrace(); 
        }
        EnergyProvider provider = sim.getProvidersMap().get("edp");
        assertEquals("EDP",provider.getName());
        assertEquals(novoPreco,provider.getPrice_kwh());
        
    }

    @Test
    public void alteraImpostoTest(){
        Simulator sim = null;
        double novoImposto = 1.00;
        List<Command> cmds = new ArrayList<>();
        cmds.add(new Command(LocalDateTime.of(2022,4,5,12,30),simul -> simul.changeProviderTax("edp",novoImposto),false));
        try {
            sim = Simulator.loadState("/home/orlando/Desktop/Trabalho-POO-2021_2022/files/stateForTesting.obj");
            sim.startSimulation(LocalDateTime.of(2022,04,01,10,0),LocalDateTime.of(2022,04,30,10,00),cmds);
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); 
        } catch (IOException e) {
            e.printStackTrace(); 
        }
        EnergyProvider provider = sim.getProvidersMap().get("edp");
        assertEquals("EDP",provider.getName());
        assertEquals(novoImposto,provider.getTax());
    }

    @Test
    public void alteraFornecedorTest(){
        Simulator sim = null;
        String novoFornecedor = "iberdrola", antigoFornecedor = "";
        List<Command> cmds = new ArrayList<>();
        cmds.add(new Command(LocalDateTime.of(2022,4,5,12,30),simul -> simul.changeHouseProvider(novoFornecedor, 5),false));
        try {
            sim = Simulator.loadState("/home/orlando/Desktop/Trabalho-POO-2021_2022/files/stateForTesting.obj");
            antigoFornecedor = sim.getHouse(5).getFornecedor();
            sim.startSimulation(LocalDateTime.of(2022,04,01,10,00),LocalDateTime.of(2022,04,30,10,00),cmds);
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); 
        } catch (IOException e) {
            e.printStackTrace(); 
        }
        assertNotEquals(antigoFornecedor, novoFornecedor);
        assertEquals(novoFornecedor,sim.getHouse(5).getFornecedor().toLowerCase());

    }

    @Test
    public void ligaTodosNaCasaTest(){
        Simulator sim = null;
        List<Command> cmds = new ArrayList<>();
        cmds.add(new Command(LocalDateTime.of(2022,4,1,12,30),simul -> simul.setStateAllDevicesHouse(5, false, LocalDateTime.of(2022,4,1,12,30)),true));
        cmds.add(new Command(LocalDateTime.of(2022,4,2,12,30),simul -> simul.setStateAllDevicesHouse(5, true, LocalDateTime.of(2022,4,2,12,30)),true));
        try {
            sim = Simulator.loadState("/home/orlando/Desktop/Trabalho-POO-2021_2022/files/stateForTesting.obj");
            sim.startSimulation(LocalDateTime.of(2022,04,01,10,00),LocalDateTime.of(2022,04,30,10,00),cmds);
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); 
        } catch (IOException e) {
            e.printStackTrace(); 
        }
        boolean test = true;
        for(SmartDevice dev: sim.getHouse(5).getDevices()){
            if(dev.getOn() == false){
                test = false; break;
            }
        }
        assertTrue(test);
    }

    @Test
    public void desligaTodosNaCasaTest(){
        Simulator sim = null;
        List<Command> cmds = new ArrayList<>();
        cmds.add(new Command(LocalDateTime.of(2022,4,1,12,30),simul -> simul.setStateAllDevicesHouse(5, true, LocalDateTime.of(2022,4,1,12,30)),true));
        cmds.add(new Command(LocalDateTime.of(2022,4,2,12,30),simul -> simul.setStateAllDevicesHouse(5, false, LocalDateTime.of(2022,4,2,12,30)),true));
        try {
            sim = Simulator.loadState("/home/orlando/Desktop/Trabalho-POO-2021_2022/files/stateForTesting.obj");
            sim.startSimulation(LocalDateTime.of(2022,04,01,10,00),LocalDateTime.of(2022,04,30,10,00),cmds);
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); 
        } catch (IOException e) {
            e.printStackTrace(); 
        }
        boolean test = true;
        for(SmartDevice dev: sim.getHouse(5).getDevices()){
            if(dev.getOn() == true){
                test = false;
            }
        }
        assertTrue(test);
    }

    @Test
    public void ligaTudoEmTodasAsCasasTest(){
        Simulator sim = null;
        List<Command> cmds = new ArrayList<>();
        cmds.add(new Command(LocalDateTime.of(2022,4,1,12,30),simul -> simul.setStateAllDevicesInAllHouses(false, LocalDateTime.of(2022,4,1,12,30)),true));
        cmds.add(new Command(LocalDateTime.of(2022,4,2,12,30),simul -> simul.setStateAllDevicesInAllHouses(true, LocalDateTime.of(2022,4,2,12,30)),true));
        try {
            sim = Simulator.loadState("/home/orlando/Desktop/Trabalho-POO-2021_2022/files/stateForTesting.obj");
            sim.startSimulation(LocalDateTime.of(2022,04,01,10,00),LocalDateTime.of(2022,04,30,10,00),cmds);
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); 
        } catch (IOException e) {
            e.printStackTrace(); 
        }
        boolean test = true;
        for(CasaInteligente house: sim.getHouses()){
            for(SmartDevice dev: house.getDevices()){
                if(dev.getOn() == false){
                    test = false; break;
                }
            }
            if(test == false) break;
        }
        assertTrue(test);
    }

    @Test
    public void desligaTudoEmTodasAsCasasTest(){
        Simulator sim = null;
        List<Command> cmds = new ArrayList<>();
        cmds.add(new Command(LocalDateTime.of(2022,4,1,12,30),simul -> simul.setStateAllDevicesInAllHouses(true, LocalDateTime.of(2022,4,1,12,30)),true));
        cmds.add(new Command(LocalDateTime.of(2022,4,2,12,30),simul -> simul.setStateAllDevicesInAllHouses(false, LocalDateTime.of(2022,4,2,12,30)),true));
        try {
            sim = Simulator.loadState("/home/orlando/Desktop/Trabalho-POO-2021_2022/files/stateForTesting.obj");
            sim.startSimulation(LocalDateTime.of(2022,04,01,10,00),LocalDateTime.of(2022,04,30,10,00),cmds);
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); 
        } catch (IOException e) {
            e.printStackTrace(); 
        }
        boolean test = true;
        for(CasaInteligente house: sim.getHouses()){
            for(SmartDevice dev: house.getDevices()){
                if(dev.getOn() == true){
                    test = false; break;
                }
            }
            if(test == false) break;
        }
        assertTrue(test);
    }

    @Test
    public void ligaTodosNaReparticaoTest(){
        Simulator sim = null;
        List<Command> cmds = new ArrayList<>();
        cmds.add(new Command(LocalDateTime.of(2022,4,1,12,30),simul -> simul.setStateAllDevicesInRoom(5,"Sala", false, LocalDateTime.of(2022,4,1,12,30)),true));
        cmds.add(new Command(LocalDateTime.of(2022,4,2,12,30),simul -> simul.setStateAllDevicesInRoom(5,"Sala", true, LocalDateTime.of(2022,4,1,12,30)),true));
        try {
            sim = Simulator.loadState("/home/orlando/Desktop/Trabalho-POO-2021_2022/files/stateForTesting.obj");
            sim.startSimulation(LocalDateTime.of(2022,04,01,10,00),LocalDateTime.of(2022,04,30,10,00),cmds);
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); 
        } catch (IOException e) {
            e.printStackTrace(); 
        }
        boolean test = true;
        for(SmartDevice dev: sim.getHouse(5).getDevicesInRoom("Sala")){
            if(dev.getOn() == false){
                test = false; break;
            }
        }
        assertTrue(test);
    }

    @Test
    public void desligaTodosNaReparticaoTest(){
        Simulator sim = null;
        List<Command> cmds = new ArrayList<>();
        cmds.add(new Command(LocalDateTime.of(2022,4,1,12,30),simul -> simul.setStateAllDevicesInRoom(5,"Sala", true, LocalDateTime.of(2022,4,1,12,30)),true));
        cmds.add(new Command(LocalDateTime.of(2022,4,2,12,30),simul -> simul.setStateAllDevicesInRoom(5,"Sala", false, LocalDateTime.of(2022,4,1,12,30)),true));
        try {
            sim = Simulator.loadState("/home/orlando/Desktop/Trabalho-POO-2021_2022/files/stateForTesting.obj");
            sim.startSimulation(LocalDateTime.of(2022,04,01,10,00),LocalDateTime.of(2022,04,30,10,00),cmds);
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); 
        } catch (IOException e) {
            e.printStackTrace(); 
        }
        boolean test = true;
        for(SmartDevice dev: sim.getHouse(5).getDevicesInRoom("Sala")){
            if(dev.getOn() == true){
                test = false; break;
            }
        }
        assertTrue(test);
    }

    @Test
    public void ligaDispositivoNaCasaTest(){
        Simulator sim = null;
        List<Command> cmds = new ArrayList<>();
        cmds.add(new Command(LocalDateTime.of(2022,4,1,12,30),simul -> simul.setStateInDeviceInHouse("s22",5, false, LocalDateTime.of(2022,4,1,12,30)),true));
        cmds.add(new Command(LocalDateTime.of(2022,4,2,12,30),simul -> simul.setStateInDeviceInHouse("s22",5, true, LocalDateTime.of(2022,4,2,12,30)),true));
        try {
            sim = Simulator.loadState("/home/orlando/Desktop/Trabalho-POO-2021_2022/files/stateForTesting.obj");
            sim.startSimulation(LocalDateTime.of(2022,04,01,10,00),LocalDateTime.of(2022,04,30,10,00),cmds);
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); 
        } catch (IOException e) {
            e.printStackTrace(); 
        }
        assertTrue(sim.getHouse(5).getDevice("s22").getOn());
    }

    @Test
    public void desligaDispositivoNaCasaTest(){
        Simulator sim = null;
        List<Command> cmds = new ArrayList<>();
        cmds.add(new Command(LocalDateTime.of(2022,4,1,12,30),simul -> simul.setStateInDeviceInHouse("s22",5, true, LocalDateTime.of(2022,4,1,12,30)),true));
        cmds.add(new Command(LocalDateTime.of(2022,4,2,12,30),simul -> simul.setStateInDeviceInHouse("s22",5, false, LocalDateTime.of(2022,4,2,12,30)),true));
        try {
            sim = Simulator.loadState("/home/orlando/Desktop/Trabalho-POO-2021_2022/files/stateForTesting.obj");
            sim.startSimulation(LocalDateTime.of(2022,04,01,10,00),LocalDateTime.of(2022,04,30,10,00),cmds);
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); 
        } catch (IOException e) {
            e.printStackTrace(); 
        }
        assertFalse(sim.getHouse(5).getDevice("s22").getOn());
    }

    @Test
    public void alteraTonalidadeTest(){
        Simulator sim = null;
        List<Command> cmds = new ArrayList<>();
        int oldTone = -1;
        cmds.add(new Command(LocalDateTime.of(2022,4,1,12,30),simul -> simul.changeBulbToneInHouse(5, "b21", SmartBulb.COLD, LocalDateTime.of(2022,4,1,12,30)),true));
        try {
            sim = Simulator.loadState("/home/orlando/Desktop/Trabalho-POO-2021_2022/files/stateForTesting.obj");
            oldTone = ((SmartBulb)sim.getHouse(5).getDevice("b21")).getTone(); // temos a certeza que é uma lâmpada
            sim.startSimulation(LocalDateTime.of(2022,04,01,10,00),LocalDateTime.of(2022,04,30,10,00),cmds);
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); 
        } catch (IOException e) {
            e.printStackTrace(); 
        }
        int newTone = ((SmartBulb)sim.getHouse(5).getDevice("b21")).getTone();
        assertNotEquals(oldTone, newTone);
        assertEquals(SmartBulb.COLD,newTone);
    }

    @Test
    public void alteraCanalTest(){
        Simulator sim = null;
        List<Command> cmds = new ArrayList<>();
        String oldChannel = "";
        cmds.add(new Command(LocalDateTime.of(2022,4,1,12,30),simul -> simul.changeSpeakerChannelInHouse(5, "s21", "Megahits"),true));
        try {
            sim = Simulator.loadState("/home/orlando/Desktop/Trabalho-POO-2021_2022/files/stateForTesting.obj");
            oldChannel = ((SmartSpeaker)sim.getHouse(5).getDevice("s21")).getChannel(); // temos a certeza que é uma lâmpada
            sim.startSimulation(LocalDateTime.of(2022,04,01,10,00),LocalDateTime.of(2022,04,30,10,00),cmds);
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); 
        } catch (IOException e) {
            e.printStackTrace();
        }
        String newChannel = ((SmartSpeaker)sim.getHouse(5).getDevice("s21")).getChannel();
        assertNotEquals(oldChannel,"Megahits");
        assertEquals(newChannel,"Megahits");
    }

    @Test
    public void aumentaVolumeTest(){
        Simulator sim = null;
        List<Command> cmds = new ArrayList<>();
        int oldVolume = -1;
        cmds.add(new Command(LocalDateTime.of(2022,4,1,12,30),simul -> simul.volumeUpSpeakerInHouse(5, "s21", LocalDateTime.of(2022,4,1,12,30)),true));
        try {
            sim = Simulator.loadState("/home/orlando/Desktop/Trabalho-POO-2021_2022/files/stateForTesting.obj");
            oldVolume = ((SmartSpeaker)sim.getHouse(5).getDevice("s21")).getVolume(); // temos a certeza que é uma lâmpada
            sim.startSimulation(LocalDateTime.of(2022,04,01,10,00),LocalDateTime.of(2022,04,30,10,00),cmds);
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); 
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(oldVolume +1, ((SmartSpeaker)sim.getHouse(5).getDevice("s21")).getVolume());
    }

    @Test
    public void diminuiVolumeTest(){
        Simulator sim = null;
        List<Command> cmds = new ArrayList<>();
        int oldVolume = -1;
        cmds.add(new Command(LocalDateTime.of(2022,4,1,12,30),simul -> simul.volumeDownSpeakerInHouse(5, "s21", LocalDateTime.of(2022,4,1,12,30)),true));
        try {
            sim = Simulator.loadState("/home/orlando/Desktop/Trabalho-POO-2021_2022/files/stateForTesting.obj");
            oldVolume = ((SmartSpeaker)sim.getHouse(5).getDevice("s21")).getVolume(); // temos a certeza que é uma lâmpada
            sim.startSimulation(LocalDateTime.of(2022,04,01,10,00),LocalDateTime.of(2022,04,30,10,00),cmds);
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); 
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(oldVolume -1, ((SmartSpeaker)sim.getHouse(5).getDevice("s21")).getVolume());
    }

    @Test
    public void commandOutOfIntervalTest(){
        Simulator sim = null;
        double novoPreco = 1.00, antigoPreco = -1.0;
        List<Command> cmds = new ArrayList<>();
        cmds.add(new Command(LocalDateTime.of(2022,4,30,10,01),simul -> simul.changeProviderPrice("edp",novoPreco),false));
        try {
            sim = Simulator.loadState("/home/orlando/Desktop/Trabalho-POO-2021_2022/files/stateForTesting.obj");
            antigoPreco = sim.getProvidersMap().get("edp").getPrice_kwh();
            sim.startSimulation(LocalDateTime.of(2022,04,01,10,0),LocalDateTime.of(2022,04,30,10,00),cmds);
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); 
        } catch (IOException e) {
            e.printStackTrace(); 
        }
        EnergyProvider provider = sim.getProvidersMap().get("edp");
        assertEquals("EDP",provider.getName());
        assertEquals(antigoPreco,provider.getPrice_kwh());
    }

}
