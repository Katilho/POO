import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SimulatorTest {
    
    @Test
    public void testBills() throws FileNotFoundException{
        /* 
        String path = new File("SimulatorTest.java").getAbsolutePath();
        File devices_f = new File("/home/orlando/Desktop/Trabalho-POO-2021_2022/files/devices.txt");
        File providers_f = new File("/home/orlando/Desktop/Trabalho-POO-2021_2022/files/providers.txt");
        File people_f = new File("/home/orlando/Desktop/Trabalho-POO-2021_2022/files/people.txt");
        File houses_f = new File("/home/orlando/Desktop/Trabalho-POO-2021_2022/files/houses.txt");
        Map<String, EnergyProvider> providers = Generator.fileToProviders(providers_f);
        List<CasaInteligente> houses = Generator.fileToHouses(devices_f,people_f,houses_f);
        Simulator sim = new Simulator(houses,providers.values().stream().collect(Collectors.toList()));
        sim.startSimulation(LocalDateTime.parse("2022-04-14T10:00"),LocalDateTime.parse("2022-04-15T10:00"),new ArrayList<>());
        */
        
        Simulator sim = null;
        try {
            sim = Simulator.loadState("/home/orlando/Desktop/Trabalho-POO-2021_2022/files/stateForTesting.obj");
            sim.startSimulation(LocalDateTime.parse("2022-04-14T10:00"),LocalDateTime.parse("2022-04-15T10:00"),new ArrayList<>());
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); 
        } catch (IOException e) {
            e.printStackTrace(); 
        }
        
        boolean test = true;
        assertEquals(11, sim.getBillsFromProvider("EDP").size());
        for(Fatura f: sim.getBillsFromProvider("EDP")){
            if(!f.getProviderName().equals("EDP")){
                test = false; break;
            }
        }
        assertTrue(test);
        assertEquals(12, sim.getBillsFromProvider("Endesa").size());
        for(Fatura f: sim.getBillsFromProvider("Endesa")){
            if(!f.getProviderName().equals("Endesa")){
                test = false; break;
            }
        }
        assertTrue(test);
        assertEquals(12, sim.getBillsFromProvider("GoldEnergy").size());
        for(Fatura f: sim.getBillsFromProvider("GoldEnergy")){
            if(!f.getProviderName().equals("GoldEnergy")){
                test = false; break;
            }
        }
        assertTrue(test);
        assertEquals(12, sim.getBillsFromProvider("Iberdrola").size());
        for(Fatura f: sim.getBillsFromProvider("Iberdrola")){
            if(!f.getProviderName().equals("Iberdrola")){
                test = false; break;
            }
        }
        assertTrue(test);
        assertEquals(12, sim.getBillsFromProvider("Galp").size());
        for(Fatura f: sim.getBillsFromProvider("Galp")){
            if(!f.getProviderName().equals("Galp")){
                test = false; break;
            }
        }
        assertTrue(test);
        assertEquals(12, sim.getBillsFromProvider("Coopernico").size());
        for(Fatura f: sim.getBillsFromProvider("Coopernico")){
            if(!f.getProviderName().equals("Coopernico")){
                test = false; break;
            }
        }
        assertTrue(test);
        assertEquals(12, sim.getBillsFromProvider("Enat").size());
        for(Fatura f: sim.getBillsFromProvider("Enat")){
            if(!f.getProviderName().equals("Enat")){
                test = false; break;
            }
        }
        assertTrue(test);
        assertEquals(11, sim.getBillsFromProvider("YIce").size());
        for(Fatura f: sim.getBillsFromProvider("YIce")){
            if(!f.getProviderName().equals("YIce")){
                test = false; break;
            }
        }
        assertTrue(test);
        assertEquals(11, sim.getBillsFromProvider("MEO Energia").size());
        for(Fatura f: sim.getBillsFromProvider("MEO Energia")){
            if(!f.getProviderName().equals("MEO Energia")){
                test = false; break;
            }
        }
        assertTrue(test);
        assertEquals(11, sim.getBillsFromProvider("Muon").size());
        for(Fatura f: sim.getBillsFromProvider("Muon")){
            if(!f.getProviderName().equals("Muon")){
                test = false; break;
            }
        }
        assertTrue(test);
        assertEquals(11, sim.getBillsFromProvider("Luzboa").size());
        for(Fatura f: sim.getBillsFromProvider("Luzboa")){
            if(!f.getProviderName().equals("Luzboa")){
                test = false; break;
            }
        }
        assertTrue(test);
        assertEquals(11, sim.getBillsFromProvider("Energia Simples").size());
        for(Fatura f: sim.getBillsFromProvider("Energia Simples")){
            if(!f.getProviderName().equals("Energia Simples")){
                test = false; break;
            }
        }
        assertTrue(test);
        assertEquals(11, sim.getBillsFromProvider("SU Electricidade").size());
        for(Fatura f: sim.getBillsFromProvider("SU Electricidade")){
            if(!f.getProviderName().equals("SU Electricidade")){
                test = false; break;
            }
        }
        assertTrue(test);
        assertEquals(11, sim.getBillsFromProvider("EDA").size());
        for(Fatura f: sim.getBillsFromProvider("EDA")){
            if(!f.getProviderName().equals("EDA")){
                test = false; break;
            }
        }
        assertTrue(test);
    }

    @Test
    public void testConsumptionOrder() throws FileNotFoundException{
        Simulator sim = null;
        try {
            sim = Simulator.loadState("/home/orlando/Desktop/Trabalho-POO-2021_2022/files/stateForTesting.obj");
            sim.startSimulation(LocalDateTime.parse("2022-04-14T10:00"),LocalDateTime.parse("2022-04-15T10:00"),new ArrayList<>());
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        List<CasaInteligente> consumptionOrder = sim.getConsumptionOrder();
        boolean test = true;
        for (int i = 0; i+1 < consumptionOrder.size(); i++){
            if(consumptionOrder.get(i).getTotalConsumption() < 
               consumptionOrder.get(i+1).getTotalConsumption()){
                   test = false;
                   break;
            }
        }
        assertTrue(test);
    }

    @Test
    public void testBiggestConsumer() throws FileNotFoundException{
        Simulator sim = null;
        Command cm1 = new Command(LocalDateTime.of(2022,4,1,10,0),simul -> simul.setStateAllDevicesInAllHouses(false, LocalDateTime.of(2022,4,1,10,0)), true);
        Command cm2 = new Command(LocalDateTime.of(2022,4,2,10,0),simul -> simul.setStateAllDevicesHouse(1, true, LocalDateTime.of(2022,4,2,10,0)), true);
        List<Command> cmds = new ArrayList<>(); 
        cmds.add(cm1); 
        cmds.add(cm2);
        try {
            sim = Simulator.loadState("/home/orlando/Desktop/Trabalho-POO-2021_2022/files/stateForTesting.obj");
            sim.startSimulation(LocalDateTime.parse("2022-04-01T10:00"),LocalDateTime.parse("2022-04-30T10:00"),cmds);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        CasaInteligente biggestConsumer = sim.getBiggestConsumer();
        assertEquals("7984 High Noon Street North Royalton, OH 44133",biggestConsumer.getMorada());
        
    }

    @Test
    public void testBiggestProvider() throws FileNotFoundException{
        Simulator sim = null;
        try {
            sim = Simulator.loadState("/home/orlando/Desktop/Trabalho-POO-2021_2022/files/stateForTesting.obj");
            sim.startSimulation(LocalDateTime.parse("2022-04-14T10:00"),LocalDateTime.parse("2022-04-15T10:00"),new ArrayList<>());
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        EnergyProvider ep = sim.getBiggestProvider();
        assertEquals("SU Electricidade",ep.getName());
    }


}