import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.HashMap;

public class Generator {
    
    private static SmartDevice lineToDevice(String line){
        String[] data = line.split(";");
        SmartDevice result = null;
        if(data[0].equals("SmartBulb")){
            result = new SmartBulb(data[1],Boolean.parseBoolean(data[2]),Integer.parseInt(data[3]),Double.parseDouble(data[4]));
        }else if(data[0].equals("SmartSpeaker")){
            result = new SmartSpeaker(data[1],Boolean.parseBoolean(data[2]),Integer.parseInt(data[3]),data[4],data[5]);
        }else if(data[0].equals("SmartCamera")){
            result = new SmartCamera(data[1],Boolean.parseBoolean(data[2]),Integer.parseInt(data[3]),Integer.parseInt(data[4]),Float.parseFloat(data[5]));
        }
        return result;
    }

    private static Map<String,SmartDevice> fileToDevices(String path) throws FileNotFoundException{
        Map<String,SmartDevice> result = new HashMap<>();
        Scanner reader = new Scanner(new File(path));
        while(reader.hasNextLine()){
            SmartDevice aux = lineToDevice(reader.nextLine());
            result.put(aux.getID(), aux);
        }
        reader.close();
        return result;
    }

    private static Map<String,SmartDevice> fileToDevices(File f) throws FileNotFoundException{
        Map<String,SmartDevice> result = new HashMap<>();
        Scanner reader = new Scanner(f);
        while(reader.hasNextLine()){
            SmartDevice aux = lineToDevice(reader.nextLine());
            result.put(aux.getID(), aux);
        }
        reader.close();
        return result;
    }

    private static Map<Integer,Pessoa> fileToPeople(String path) throws FileNotFoundException {
        Map<Integer,Pessoa> result = new HashMap<>();
        Scanner reader = new Scanner(new File(path));
        while(reader.hasNextLine()){
            String[] data = reader.nextLine().split(";");
            result.put(Integer.parseInt(data[1]),new Pessoa(data[0],Integer.parseInt(data[1])));
        }
        reader.close();
        return result;
    }

    private static Map<Integer,Pessoa> fileToPeople(File f) throws FileNotFoundException {
        Map<Integer,Pessoa> result = new HashMap<>();
        Scanner reader = new Scanner(f);
        while(reader.hasNextLine()){
            String[] data = reader.nextLine().split(";");
            result.put(Integer.parseInt(data[1]),new Pessoa(data[0],Integer.parseInt(data[1])));
        }
        reader.close();
        return result;
    }

    private static List<String> stringListToList(String listStr, String delim){
        List<String> result = new ArrayList<>();
        String[] devIDs = listStr.substring(1,listStr.length() - 1).split(delim);
        for(String id: devIDs) result.add(id);
        return result;
    }

    private static Map<String,List<String>> tuplesRoomDevicesToLocations(String tupleRoomDevices){
        Map<String,List<String>> result = new HashMap<>();
        String[] tuples = tupleRoomDevices.substring(1, tupleRoomDevices.length()-1).split(",");
        for(String tuple: tuples){
            String[] aux = tuple.substring(1, tuple.length() - 1).split(":");
            result.put(aux[0],stringListToList(aux[1], "\\."));
        }
        return result; 
    }

    public static Map<String,EnergyProvider> fileToProviders(String path) throws FileNotFoundException{
        Map<String, EnergyProvider> result = new HashMap<>();
        Scanner reader = new Scanner(new File(path));
        while(reader.hasNextLine()){
            String[] data = reader.nextLine().split(";");
            result.put(data[0], new EnergyProvider(data[0],Float.parseFloat(data[1]),Float.parseFloat(data[2])));
        }
        reader.close();
        return result;
    }

    public static Map<String,EnergyProvider> fileToProviders(File f) throws FileNotFoundException{
        Map<String, EnergyProvider> result = new HashMap<>();
        Scanner reader = new Scanner(f);
        while(reader.hasNextLine()){
            String[] data = reader.nextLine().split(";");
            result.put(data[0], new EnergyProvider(data[0],Float.parseFloat(data[1]),Float.parseFloat(data[2])));
        }
        reader.close();
        return result;
    }
    //  data[0]         data[1]          data[2]    data[3] 
    //Morada(string);[(Room:[d1.d2.d2])];NIF(int);Fornecedor(string)
    private static CasaInteligente lineToHouse(Map<String,SmartDevice> allDevices, Map<Integer,Pessoa> allPeople, String line){
        String[] data = line.split(";");
        CasaInteligente result = new CasaInteligente(data[0],allPeople.get(Integer.parseInt(data[2])),data[3]);
        Map<String,List<String>> devsPerRoom = tuplesRoomDevicesToLocations(data[1]);
        for(String room: devsPerRoom.keySet()){
            for(String devID: devsPerRoom.get(room)){
                result.addDevice(allDevices.get(devID), room);
            }
        }
        return result;
    }
    
    public static List<CasaInteligente> fileToHouses(String devicesF,String peopleF, String housesF) throws FileNotFoundException{
        Map<String,SmartDevice> devices = fileToDevices(devicesF);
        Map<Integer,Pessoa> people = fileToPeople(peopleF);
        List<CasaInteligente> houses = new ArrayList<>();
        Scanner reader = new Scanner(new File(housesF));
        while(reader.hasNext()){
            houses.add(lineToHouse(devices,people,reader.nextLine()));
        }
        reader.close();
        return houses;
    }

    public static List<CasaInteligente> fileToHouses(File devicesF,File peopleF, File housesF) throws FileNotFoundException{
        Map<String,SmartDevice> devices = fileToDevices(devicesF);
        Map<Integer,Pessoa> people = fileToPeople(peopleF);
        List<CasaInteligente> houses = new ArrayList<>();
        Scanner reader = new Scanner(housesF);
        while(reader.hasNext()){
            houses.add(lineToHouse(devices,people,reader.nextLine()));
        }
        reader.close();
        return houses;
    }
    
}
