import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class EnergyProviderTest {
    
    @Test
    public void testConstructor(){
        EnergyProvider provider = new EnergyProvider();
        assertTrue(provider != null);
        provider = new EnergyProvider("EDP",10,0.23f);
        assertTrue(provider != null);
        EnergyProvider provider2 = new EnergyProvider(provider);
        assertTrue(provider2 != null);
    }

    @Test
    public void testGetName(){
        EnergyProvider provider = new EnergyProvider();
        assertEquals("",provider.getName());
        provider = new EnergyProvider("EDP",10,0.23f);
        assertEquals("EDP",provider.getName());
    }

    @Test
    public void testSetName(){
        EnergyProvider provider = new EnergyProvider();
        assertEquals("",provider.getName());
        provider.setName("EDP");
        assertEquals("EDP",provider.getName());
    }

    @Test
    public void testGetPriceKwh(){
        EnergyProvider provider = new EnergyProvider();
        assertEquals(0.0f,provider.getPrice_kwh());
        provider = new EnergyProvider("EDP",10.0f,0.23f);
        assertEquals(10.0f,provider.getPrice_kwh());
    }

    @Test
    public void testSetPriceKwh(){
        EnergyProvider provider = new EnergyProvider();
        assertEquals(0.0f,provider.getPrice_kwh());
        provider.setPrice_kwh(10.0f);
        assertEquals(10.0f,provider.getPrice_kwh());
    }

    @Test
    public void testGetTaxKwh(){
        EnergyProvider provider = new EnergyProvider();
        assertEquals(0.0f,provider.getTax());
        provider = new EnergyProvider("EDP",10.0f,0.23f);
        assertEquals(0.23f,provider.getTax());
    }

    @Test
    public void testSetTaxKwh(){
        EnergyProvider provider = new EnergyProvider();
        assertEquals(0.0f,provider.getTax());
        provider.setTax(0.23f);
        assertEquals(0.23f,provider.getTax());
    }

}
