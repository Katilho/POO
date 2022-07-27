import java.time.LocalDateTime;

public class SmartCamera extends SmartDevice{
    private int resX;
    private int resY;
    private double sizeOfFile;

    public SmartCamera() {
        // initialise instance variables
        super();
        this.resX = 0;
        this.resY = 0;
        this.sizeOfFile = 0;
    }

    public SmartCamera(String id) {
        // initialise instance variables
        super(id);
        this.resX = 0;
        this.resY = 0;
        this.sizeOfFile = 0;
    }

    public SmartCamera(String id, int resX, int resY, int size) {
        // initialise instance variables
        super(id);
        this.resX = resX;
        this.resY = resY;
        this.sizeOfFile = size;
    }

    public SmartCamera(String id, boolean state, int resX, int resY, double sizeOfFile){
        super(id,state);
        this.resX = resX;
        this.resY = resY;
        this.sizeOfFile = sizeOfFile;
    }

    public SmartCamera(String id, boolean state, int resX, int resY, double sizeOfFile, LocalDateTime change_date){
        super(id,state,change_date);
        this.resX = resX;
        this.resY = resY;
        this.sizeOfFile = sizeOfFile;
    }

    public SmartCamera(SmartCamera sc) {
        super(sc);
        this.resX = sc.getResX();
        this.resY = sc.getResY();
        this.sizeOfFile = sc.getSizeOfFile();
    }

    /**
     * Devolve a resolução desta câmara (X)
     */
    public int getResX() {
        return resX;
    }

    /**
     * Devolve a resolução desta câmara (Y)
     */
    public int getResY() {
        return resY;
    }

    /**
     * Devolve o tamanho do ficheiro gerado por esta câmara.
     */
    public double getSizeOfFile() {
        return sizeOfFile;
    }

    /**
     * Altera a resolução desta câmara (X)
     */
    public void setResX(int resX) {
        this.resX = resX;
    }

    /**
     * Altera a resolução desta câmara (Y)
     */
    public void setResY(int resY) {
        this.resY = resY;
    }

    /**
     * Altera o tamanho do ficheiro gerado por esta câmara.
     */
    public void setSizeOfFile(double sizeOfFile) {
        this.sizeOfFile = sizeOfFile;
    }

    @Override
    public double dailyConsumption(){
        return this.getOn() ? (0.00000144*this.sizeOfFile*resX*resY*24.0)/1000.0 : 0.0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SmartCamera that = (SmartCamera) o;
        return resX == that.resX && resY == that.resY && this.sizeOfFile == that.sizeOfFile;
    }

    @Override
    public String toString(){
        return String.format("{Device: SmartCamera, ID: %s, ON/OFF: %s, Dimensions: %dx%d, FileSize: %f}", this.getID(), this.getOn() ? "ON" : "OFF" ,this.resX,this.resY,this.sizeOfFile);
    }

    @Override
    public SmartDevice clone() {
        return new SmartCamera(this);
    }

}


