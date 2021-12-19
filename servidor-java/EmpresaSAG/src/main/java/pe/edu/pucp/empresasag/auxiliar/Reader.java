package pe.edu.pucp.empresasag.auxiliar;

import org.springframework.web.multipart.MultipartFile;
import pe.edu.pucp.empresasag.model.Blocking;
import pe.edu.pucp.empresasag.model.CityMap;
import pe.edu.pucp.empresasag.model.Request;
import pe.edu.pucp.empresasag.model.Truck;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Reader {

    public ArrayList<Blocking> readBlocks(MultipartFile file,CityMap map){
        ArrayList<Blocking> blockings = new ArrayList<>();
        String data;
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String fileName = file.getOriginalFilename();

        try {
            InputStream fis = file.getInputStream();
            Scanner s = new Scanner(fis).useDelimiter("\\r\\n");
            while(true){
                data = s.hasNext() ? s.next() : null;
                if(data == null) break;
                Blocking blocking = new Blocking(
                        formater.parse(fileName.substring(0, 4) + "-" + fileName.substring(4, 6) + "-" +data.substring(0,2)
                                +" "+data.substring(3,8)+ ":00"),
                        formater.parse(fileName.substring(0, 4) + "-" + fileName.substring(4, 6) + "-" +data.substring(9,11)
                                +" "+data.substring(12,17)+ ":00"),
                        data.substring(18, data.length()), map);
                blocking.buildNumberDate();
                blockings.add(blocking);
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }
        return blockings;
    }

    public ArrayList<Request> readOrders(MultipartFile file) {
        ArrayList<Request> orders = new ArrayList<>();
        String data;
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String fileName = file.getOriginalFilename();

        try {
            InputStream fis = file.getInputStream();
            Scanner s = new Scanner(fis).useDelimiter("\\r\\n");
            while(true){
                data = s.hasNext() ? s.next() : null;
                if(data == null) break;
                int[] numbers = Arrays.stream(data.substring(9).split(",")).mapToInt(Integer::parseInt).toArray();
                Request order = new Request(formater.parse(fileName.substring(6, 10) + "-" + fileName.substring(10, 12) + "-" + data.substring(0, 2)
                                + " " + data.substring(3, 8) + ":00"), numbers[2], numbers[3],numbers[0],numbers[1]);
                orders.add(order);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return orders;
    }
}
