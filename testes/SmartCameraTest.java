import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class SmartCameraTest {

    @Test
    public void testContructor() {
        SmartCamera smartCam1 = new SmartCamera();
        assertTrue(smartCam1 !=null);
        smartCam1 = new SmartCamera("c1");
        assertTrue(smartCam1 !=null);
        smartCam1 = new SmartCamera("c1", 1300, 600, 5);
        assertTrue(smartCam1 !=null);
    }

    @Test
    public void testGetRes() {
        SmartCamera smartCam1 = new SmartCamera("c1", 1300, 600, 1);
        assertEquals(1300, smartCam1.getResX());
        assertEquals(600, smartCam1.getResY());
        SmartCamera smartCam2 = new SmartCamera(smartCam1);
        assertEquals(1300, smartCam2.getResX());
        assertEquals(600, smartCam2.getResY());
        smartCam1 = new SmartCamera("c1");
        assertEquals(0, smartCam1.getResX());
        assertEquals(0, smartCam1.getResY());
        smartCam1 = new SmartCamera();
        assertEquals(0, smartCam1.getResX());
        assertEquals(0, smartCam1.getResY());
    }

    @Test
    public void testSetRes() {
        SmartCamera smartCam1 = new SmartCamera("c1");
        smartCam1.setResX(1800);
        smartCam1.setResY(1300);
        assertEquals(1800, smartCam1.getResX());
        assertEquals(1300, smartCam1.getResY());
    }

    @Test
    public void testGetSizeOfFIle() {
        SmartCamera smartCam1 = new SmartCamera("c1", 2160, 1600, 100);
        assertEquals(100, smartCam1.getSizeOfFile());
        SmartCamera smartCam2 = new SmartCamera(smartCam1);
        assertEquals(100, smartCam2.getSizeOfFile());
        smartCam1 = new SmartCamera("c1");
        assertEquals(0, smartCam1.getSizeOfFile());
        smartCam1 = new SmartCamera();
        assertEquals(0, smartCam1.getSizeOfFile());
    }

    @Test
    public void testSetSizeOfFIle() {
        SmartCamera smartCam1 = new SmartCamera("c1");
        smartCam1.setSizeOfFile(100);
        assertEquals(100, smartCam1.getSizeOfFile());
        smartCam1.setSizeOfFile(30);
        smartCam1.setSizeOfFile(80);
        assertEquals(80, smartCam1.getSizeOfFile());
    }
    
}
