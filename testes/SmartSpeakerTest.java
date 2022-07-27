import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The test class SmartSpeakerTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class SmartSpeakerTest {
    /**
     * Default constructor for test class SmartSpeakerTest
     */
    public SmartSpeakerTest()
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
        SmartSpeaker smartSpe1 = new SmartSpeaker();
        assertTrue(smartSpe1!=null);
        smartSpe1 = new SmartSpeaker("b1");
        assertTrue(smartSpe1!=null);
        smartSpe1 = new SmartSpeaker("b1", "RUM", 5, "Brand");
        assertTrue(smartSpe1!=null);
    }

    @Test
    public void testGetVolume() {
        SmartSpeaker smartSpe1 = new SmartSpeaker("s1", "RUM", 5,"Brand");
        assertEquals(5, smartSpe1.getVolume());
        smartSpe1 = new SmartSpeaker("b1", "RUM", SmartSpeaker.MAX,"Brand");
        assertEquals(20, smartSpe1.getVolume());
        smartSpe1 = new SmartSpeaker("b1", "RUM", -10,"Brand");
        assertEquals(0, smartSpe1.getVolume());
        smartSpe1 = new SmartSpeaker();
        assertEquals(0, smartSpe1.getVolume());
    }

    @Test
    public void testSetVolume() {
        SmartSpeaker smartSpe1 = new SmartSpeaker("s1", "RUM", 5,"Brand");
        smartSpe1.setLastChangeDate(LocalDateTime.of(2022,4,24,0,0));
        smartSpe1.volumeUp(LocalDateTime.of(2022,4,24,12,30));
        smartSpe1.volumeUp(LocalDateTime.of(2022,4,24,12,30));
        assertEquals(7, smartSpe1.getVolume());
        for (int i=0; i<25; i++) smartSpe1.volumeUp(LocalDateTime.of(2022,4,24,12,30));
        assertEquals(20, smartSpe1.getVolume());
        for (int i=0; i<30; i++) smartSpe1.volumeDown(LocalDateTime.of(2022,4,24,12,30));
        assertEquals(0, smartSpe1.getVolume());
    }

    @Test
    public void testGetChannel() {
        SmartSpeaker smartSpe1 = new SmartSpeaker("s1", "RUM", 5,"Brand");
        assertEquals("RUM", smartSpe1.getChannel());
        smartSpe1 = new SmartSpeaker("s2", "XPTO", 5,"Brand");
        assertEquals("XPTO", smartSpe1.getChannel());
        smartSpe1 = new SmartSpeaker();
        assertEquals("", smartSpe1.getChannel());
    }

    @Test
    public void testSetChannel() {
        SmartSpeaker smartSpe1 = new SmartSpeaker("s1");
        smartSpe1.setChannel("RUM");
        assertEquals("RUM", smartSpe1.getChannel());
        smartSpe1.setChannel("XPTO");
        assertEquals("XPTO", smartSpe1.getChannel());
    }
}

