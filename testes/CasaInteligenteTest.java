import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The test class CasaInteligenteTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class CasaInteligenteTest {
    /**
     * Default constructor for test class CasaInteligenteTest
     */
    public CasaInteligenteTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @BeforeEach
    public void setUp()
    {
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @AfterEach
    public void tearDown()
    {
    }

    @Test
    public void testConstructor() {
        CasaInteligente casaInte1 = new CasaInteligente();
        assertTrue(casaInte1!=null);
        casaInte1 = new CasaInteligente("Campus de Gualtar");
        assertTrue(casaInte1!=null);
    }
    
    @Test
    public void testAddFindDevice() {
        CasaInteligente casaInte1 = new CasaInteligente("Gualtar");
        SmartBulb smartBul1 = new SmartBulb("b1");
        SmartSpeaker smartSpe1 = new SmartSpeaker("s1");
        assertFalse(casaInte1.existsDevice("b1"));
        assertFalse(casaInte1.existsDevice("s1"));
        casaInte1.addDevice(smartBul1);
        assertTrue(casaInte1.existsDevice("b1"));
        casaInte1.addDevice(smartSpe1);
        assertTrue(casaInte1.existsDevice("s1"));
        assertTrue(casaInte1.existsDevice("b1"));
    }

    @Test
    public void testGetDevice() {
        CasaInteligente casaInte1 = new CasaInteligente("Gualtar");
        SmartBulb smartBul1 = new SmartBulb("b1");
        casaInte1.addDevice(smartBul1);
        assertTrue(casaInte1.getDevice("b1").equals(smartBul1));
    }

    @Test
    public void testSetOn() {
        CasaInteligente casaInte1 = new CasaInteligente("Gualtar");
        SmartBulb smartBul1 = new SmartBulb("b1");
        assertFalse(smartBul1.getOn());
        casaInte1.addDevice(smartBul1);
        smartBul1.setOn(true,LocalDateTime.of(2022,4,24,12,30));
        assertTrue(smartBul1.getOn());
        assertFalse(casaInte1.getDevice("b1").getOn());
    }

    @Test
    public void testSetAllOn() {
        CasaInteligente casaInte1 = new CasaInteligente("Gualtar");
        SmartBulb smartBul1 = new SmartBulb("b1");
        SmartSpeaker smartSpe1 = new SmartSpeaker("s1");
        casaInte1.addDevice(smartBul1);
        casaInte1.addDevice(smartSpe1);
        assertFalse(casaInte1.getDevice("b1").getOn());
        assertFalse(casaInte1.getDevice("s1").getOn());
        casaInte1.setAllOn(true,LocalDateTime.of(2022,4,24,12,30));
        assertTrue(casaInte1.getDevice("b1").getOn());
        assertTrue(casaInte1.getDevice("s1").getOn());
        casaInte1.setAllOn(false,LocalDateTime.of(2022,4,24,12,30));
        assertFalse(casaInte1.getDevice("b1").getOn());
        assertFalse(casaInte1.getDevice("s1").getOn());
    }

    @Test
    public void testAddRoom() {
        CasaInteligente casaInte1 = new CasaInteligente("Gualtar");
        casaInte1.addRoom("sala");
        assertTrue(casaInte1.hasRoom("sala"));
        assertFalse(casaInte1.hasRoom("quarto"));
    }

    @Test
    public void testAddToRoom() {
        CasaInteligente casaInte1 = new CasaInteligente("Gualtar");
        SmartBulb smartBul1 = new SmartBulb("b1");
        SmartSpeaker smartSpe1 = new SmartSpeaker("s1");
        SmartSpeaker smartSpe2 = new SmartSpeaker("s2");
        casaInte1.addDevice(smartBul1);
        casaInte1.addDevice(smartSpe1);
        casaInte1.addDevice(smartSpe2);
        casaInte1.addRoom("sala");
        casaInte1.addRoom("quarto");
        casaInte1.addToRoom("sala", "b1");
        casaInte1.addToRoom("sala", "s1");
        casaInte1.addToRoom("quarto", "s2");
        assertTrue(casaInte1.roomHasDevice("sala", "b1"));
        assertTrue(casaInte1.roomHasDevice("sala", "s1"));
        assertFalse(casaInte1.roomHasDevice("sala", "s2"));
        assertTrue(casaInte1.roomHasDevice("quarto", "s2"));
    }

    @Test
    public void testTotalConsumptionAndCost(){
        CasaInteligente casaInte1 = new CasaInteligente("Street",new Pessoa("Person",111222333),"EDP");
        EnergyProvider provider = new EnergyProvider("EDP",0.15,0.23);
        LocalDateTime start = LocalDateTime.of(2022,4,1,10,0), end = LocalDateTime.of(2022,4,3,10,0);

        for(int i = 1; i <= 3; i++){
            casaInte1.addDevice(new SmartBulb(String.format("b%d", i),true,1,10,start), "Sala");
            casaInte1.addDevice(new SmartSpeaker(String.format("s%d", i), true, 10, "RUM", "SAMSUNG",start),"Quarto");
            casaInte1.addDevice(new SmartCamera(String.format("c%d",i),true,1920,1080,100,start),"Sala");
        }
        casaInte1.updateConsumptionAllDevices(end);
        double consumo = casaInte1.getTotalConsumption();
        double custo = casaInte1.getTotalCost(provider,end);
        assertEquals(144.219,consumo);
        assertEquals(3.99,custo);
        Fatura f = provider.emitirFatura(casaInte1, start, end);
        assertEquals(custo,f.getMontante());
        assertTrue(casaInte1.getProprietario().equals(f.getCliente()));
        assertTrue(casaInte1.equals(f.getCasa()));
        assertTrue(f.getProviderName().equals(casaInte1.getFornecedor()));

        casaInte1.resetConsumption();
        consumo = casaInte1.getTotalConsumption();
        custo = casaInte1.getTotalCost(provider,end);
        assertEquals(0.0,consumo);
        assertEquals(0.0,custo);
        for(SmartDevice dev: casaInte1.getDevices()){
            assertEquals(0.0,dev.getTotalConsumption());
        }
    }

}

