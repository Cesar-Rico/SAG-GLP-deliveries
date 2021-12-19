package pe.edu.pucp.empresasag.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pe.edu.pucp.empresasag.algorithm.VRPTW;
import pe.edu.pucp.empresasag.auxiliar.Reader;
import pe.edu.pucp.empresasag.model.*;
import pe.edu.pucp.empresasag.model.Package;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class ColapseServiceImpl implements ColapseService{
    private static final long HOUR = 3600*1000;
    private static final int BATCH_TIME = 12;

    ParametersService parametersService;
    PackageService packageService;
    TruckService truckService;

    private Date getInitialTime(MultipartFile file){
        Date initialTime = null;
        try{
            String fileName = file.getOriginalFilename();
            InputStream fis = file.getInputStream();
            Scanner s = new Scanner(fis).useDelimiter("\\r\\n");
            String numbers = s.hasNext() ? s.next() : "";
            SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            initialTime = formater.parse(fileName.substring(6, 10) + "-" + fileName.substring(10, 12) + "-" + numbers.substring(0, 2)
                    + " 00:00:00");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return initialTime;
    }


    ColapseServiceImpl(ParametersService parametersService,PackageService packageService,
                          TruckService truckService){
        this.parametersService = parametersService;
        this.packageService = packageService;
        this.truckService = truckService;
    }

    private void generateRouteReporte(ArrayList<Truck> trucks,CityMap map) {
        for (Truck truck : trucks) {
            String hojaRuta = "";
            if(!truck.getActive() || truck.isAveriado()) continue;
            try {
                ArrayList<Node> route = new ArrayList<>();
                for(Node node : truck.getRoute()){
                    route.add(node);
                }
                ArrayList<Package> packs = new ArrayList<>();
                for(Package pack : truck.getPackages()){
                    packs.add(pack);
                }
                int x=truck.getX();
                int y=truck.getY();
//                Node nodeAnt = null;
//                Node nodeSig = null;
//                Node nodeAct = null;
                int BATCH_TIME = 12;
                int max_Steps = 50*BATCH_TIME/60;
                int steps=0;
                while(steps<=max_Steps && packs.size()>0){
                    String strCoord = "(" + x + ", " + y + ")\n";
                    boolean flag = true;
                    do{
                        Node src = map.getVertax().get(y*map.getWidth() + x);
                        Node pack = map.getNode(packs.get(0));
                        if(src.compare(pack)){
                            Package pck = packs.remove(0);
                            boolean isOrder = pck.getIsOrder();
                            if(isOrder){
                                Request request = pck.getRequest();
                                hojaRuta = hojaRuta.concat("Entregar Pedido # " + request.getId() + " : " + "(" + pck.getAmountGLP() + " GLP) en " + strCoord);
                            }else{
                                Station sta = pck.getStation();
                                if(!sta.getIsCentral()){
                                    hojaRuta = hojaRuta.concat("Ir al Punto de Recarga  en " + strCoord);
                                }else{
                                    hojaRuta = hojaRuta.concat("Abastecimiento en la estacion central en " + strCoord);
                                }
                            }
                        }else flag = false;
                    }while(packs.size()>0 && flag);
                    steps+=1;
                    if(truck.getPackages().size()>0 && steps<=max_Steps){
                        Node node = route.remove(0);
                        x = node.getX();
                        y = node.getY();
                    }
                }
//                for (int i = 0; i < truck.getRoute().size(); i++) {
//                    Boolean isOrder = false;
//                    nodeAct = truck.getRoute().get(i);
//                    String strCoord = "(" + nodeAct.getX() + ", " + nodeAct.getY() + ")\n";
//                    for (Package pack : truck.getPackages()) {
//                        if (pack.getIsOrder()) {
//                            Request request = pack.getRequest();
//                            if (request.getX() == nodeAct.getX() && request.getY() == nodeAct.getY()) {
//                                hojaRuta = hojaRuta.concat("Entregar Pedido # " + request.getId() + " : " + "(" + pack.getAmountGLP() + " GLP) en " + strCoord);
//                                nodeAnt = nodeAct;
//                                isOrder = true;
//                                break;
//                            }
//                        }
//                    }
//                    if (isOrder) {
//                        continue;
//                    }
//                    ;
//                    if (nodeAct.getX() == 12 && nodeAct.getY() == 8) {
//                        hojaRuta = hojaRuta.concat("Ir a la estacion central en " + strCoord);
//                    } else if (nodeAct.getX() == 42 && nodeAct.getY() == 42) {
//                        hojaRuta = hojaRuta.concat("Ir al Punto de Recarga #1 en " + strCoord);
//                    } else if (nodeAct.getX() == 63 && nodeAct.getY() == 3) {
//                        hojaRuta = hojaRuta.concat("Ir al Punto de Recarga #2 en " + strCoord);
//                    } else {
//                        /**Si estamos en el primer punto de la ruta**/
//                        if (i == 0) {
//                            /**Si vas hacia abajo**/
//                            if (nodeAct.getX() == truck.getX() && nodeAct.getY() < truck.getY()) {
//                                hojaRuta = hojaRuta.concat("Ve hacia al Sur en " + strCoord);
//                            }
//                            /**Si vas hacia arriba**/
//                            else if (nodeAct.getX() == truck.getX() && nodeAct.getY() > truck.getY()) {
//                                hojaRuta = hojaRuta.concat("Ve hacia al Norte en " + strCoord);
//                            }
//                            /**Si vas a la derecha**/
//                            else if (nodeAct.getX() > truck.getX() && nodeAct.getY() == truck.getY()) {
//                                hojaRuta = hojaRuta.concat("Ve hacia al Este en " + strCoord);
//                            }
//                            /**Si vas a la izquierda**/
//                            else if (nodeAct.getX() < truck.getX() && nodeAct.getY() == truck.getY()) {
//                                hojaRuta = hojaRuta.concat("Ve hacia al Oeste en " + strCoord);
//                            }
//                        }
//                        /**Si estamos en el ultimo punto de la ruta**/
//                        else if (i == truck.getRoute().size() - 2) {
//                            nodeSig = truck.getRoute().get(truck.getRoute().size() - 1);
//
//                            /**Si viene de un punto vertical**/
//                            if (nodeAct.getX() == nodeAnt.getX() && nodeAct.getY() != nodeAnt.getY()) {
//
//                                /**Va hacia la izquierda**/
//                                if (nodeSig.getX() < nodeAct.getX()) {
//                                    hojaRuta = hojaRuta.concat("Girar al Oeste en " + strCoord);
//                                }
//                                /**Va hacia la derecha**/
//                                else if (nodeSig.getX() > nodeAct.getX()) {
//                                    hojaRuta = hojaRuta.concat("Girar al Este en " + strCoord);
//                                }
//
//                            }
//                            /**Si viene de un punto horizontal**/
//                            else if (nodeAct.getX() != nodeAnt.getX() && nodeAct.getY() == nodeAnt.getY()) {
//
//                                /**Va hacia abajo**/
//                                if (nodeSig.getY() < nodeAct.getY()) {
//                                    hojaRuta = hojaRuta.concat("Girar al Sur en " + strCoord);
//                                }
//                                /**Va hacia arriba**/
//                                else if (nodeSig.getY() > nodeAct.getY()) {
//                                    hojaRuta = hojaRuta.concat("Girar al Norte en " + strCoord);
//                                }
//
//                            }
//                            break;
//                        }
//                        /**Si estamos en un punto intermedio de la ruta**/
//                        else {
//                            nodeSig = truck.getRoute().get(i + 1);
//
//                            /**Si viene de un punto vertical**/
//                            if (nodeAct.getX() == nodeAnt.getX() && nodeAct.getY() != nodeAnt.getY()) {
//
//                                /**Va hacia la izquierda**/
//                                if (nodeSig.getX() < nodeAct.getX()) {
//                                    hojaRuta = hojaRuta.concat("Girar al Oeste en " + strCoord);
//                                }
//                                /**Va hacia la derecha**/
//                                else if (nodeSig.getX() > nodeAct.getX()) {
//                                    hojaRuta = hojaRuta.concat("Girar al Este en " + strCoord);
//                                }
//
//                            }
//                            /**Si viene de un punto horizontal**/
//                            if (nodeAct.getX() != nodeAnt.getX() && nodeAct.getY() == nodeAnt.getY()) {
//
//                                /**Va hacia abajo**/
//                                if (nodeSig.getY() < nodeAct.getY()) {
//                                    hojaRuta = hojaRuta.concat("Girar al Sur en " + strCoord);
//                                }
//                                /**Va hacia arriba**/
//                                else if (nodeSig.getY() > nodeAct.getY()) {
//                                    hojaRuta = hojaRuta.concat("Girar al Norte en " + strCoord);
//                                }
//

//                            }
//                        }
//
//                    }
//                    nodeAnt = nodeAct;
//                }


//                if (hojaRuta != "") {
//                    System.out.println("------------------------------------------------------------------");
//                    System.out.println(hojaRuta);
//                    System.out.println("------------------------------------------------------------------");
//                    truck.setRouteReport(hojaRuta);
//                }
            }
            catch(Exception e){
                System.out.println("ERROR EN HOJA DE RUTAS");
            }
            String cad = truck.getRouteReport();
            cad = cad.concat(hojaRuta);
            truck.setRouteReport(cad);
        }
    }
    @Override
    public void run(MultipartFile file1, MultipartFile file2, SseEmitter sseEmitter, String guid) {
        try {
            /** Validating files **/
            MultipartFile fileAux;
            if(file1.getOriginalFilename().compareTo(file2.getOriginalFilename()) > 0)
            {
                fileAux = file2;
                file2 = file1;
                file1 = fileAux;
            }

            /**Load Trucks**/
            ArrayList<Truck> trucks = truckService.getActiveTrucks();
            for(Truck truck: trucks){
                truck.reinitialize();
            }

            Date initialtime = getInitialTime(file2);
            Map<String,Object> payload2 = new HashMap<>();
            payload2.put("initialTime", initialtime);
            Collections.sort(trucks,(o1, o2) -> o1.getId() - o2.getId());
            payload2.put("trucks",trucks);
            Map<String, Map> response2 = new HashMap<>();
            response2.put("data",payload2);

            /**Send message **/
            Thread.sleep(2000);
            sseEmitter.send(SseEmitter.event().name(guid).data(response2));

            /**Initialize clock**/
            Date timer = new Date(initialtime.getTime());
            Date endTime = new Date(initialtime.getTime() + 24*HOUR*31 );

            Parameters parameters = parametersService.getParameters();
            int _w = parameters.getWidth();
            int _h = parameters.getHeight();

            /** Load CityMap**/
            CityMap map = new CityMap(_w,_h);
            packageService.buildPlants(map);
            Package.clear();

            Reader lector = new Reader();
            //TODO: CARGAR blockings a map
            ArrayList<Blocking> blockings = lector.readBlocks(file1,map);
            for(Blocking block: blockings){
                block.buildNodes(map);
                block.buildHash(_w,_h);
            }
            map.setBlockings(blockings);

            Request.clear();
            ArrayList<Request> orders = lector.readOrders(file2);
            ArrayList<Request> ordersAux = new ArrayList<>(orders);
            //ArrayList<Request> processOrders = new ArrayList<>();
            ArrayList<Package> processPackages = new ArrayList<>();
            ArrayList<Blocking> processBlockings = new ArrayList<>();

            //ArrayList<Request> lastProcessOrders = new ArrayList<>();
            ArrayList<Package> lastProcessPackages = new ArrayList<>();
            ArrayList<Package> deliveredPackages = new ArrayList<>();
            ArrayList<Package> duePackages = new ArrayList<>();

            while(timer.getTime()<= endTime.getTime()){

                /**Recharge Intermediate plants**/
                if(timer.getHours()==0 &&  timer.getMinutes()==0){
                    for(Package pack : map.getIntermediatePlants()){
                        pack.getStation().setLpgMeter(pack.getStation().getLpgCapacity());
                    }

                    LocalDate localDate = timer.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    int numberDate = localDate.getYear()*10000 + localDate.getMonthValue()*100 + localDate.getDayOfMonth();

                    /**LIMPIAR BLOQUEOS PASADOS**/
                    while(blockings.size()>0){
                        Blocking blck = blockings.get(0);
                        if(blck.getNumberDate() < numberDate){
                            blockings.remove(0);
                        }else{
                            break;
                        }
                    }

                    /**Setear los bloqueos Necesarios**/
                    int limitNumber = numberDate + 1;
                    ArrayList<Blocking> mapBlockings = new ArrayList<>();
                    for(Blocking blocking : blockings){
                        if(blocking.getNumberDate()<=limitNumber){
                            blocking.buildHash(_w,_h);
                            mapBlockings.add(blocking);
                        }else{
                            break;
                        }
                    }
                    map.setBlockings(mapBlockings);

                    /**METER BLOQUEOS DEL DIA**/
                    processBlockings = new ArrayList<>();
                    for(Blocking blocking : blockings){
                        if(blocking.getNumberDate()== numberDate){
                            processBlockings.add(blocking);
                        }
                        if(blocking.getNumberDate()>numberDate) break;
                    }


                }

                if(timer.getMinutes() % BATCH_TIME == 0){
                    System.out.println("Hora Actual: "+timer);
                    for(Package pack: duePackages){
                        if(!processPackages.contains(pack)){
                            processPackages.add(pack);
                        }
                    }
                    ArrayList<Package> packagesAux = new ArrayList<>(processPackages);
                    VRPTW problem = new VRPTW(trucks,processPackages,map,timer);
                    problem.solve();

                    generateRouteReporte(trucks,map);

                    //ArrayList<Request> messageOrders = new ArrayList<>();
                    ArrayList<Package> messagePackages = new ArrayList<>();
                    //ArrayList<Package> duePackages = new ArrayList<>(problem.getPacks());
                    /**
                     for (Package pack : duePackages) {
                     packagesAux.remove(pack);
                     }
                     **/



                    for (Package pack : packagesAux){
                        messagePackages.add(pack);
                    }

                    for (Package pack : lastProcessPackages){
                        if (!messagePackages.contains(pack))
                            messagePackages.add(pack);
                    }

                    for(Truck truck: trucks){
                        for(Package pack: truck.getPackages()){
                            if(pack.getIsOrder() && !messagePackages.contains(pack)){
                                messagePackages.add(pack);
                            }
                        }
                    }

                    for (Package pack : deliveredPackages){
                        if (!messagePackages.contains(pack))
                            messagePackages.add(pack);
                    }


                    /**Construct response**/
                    Map<String,Object> payload = new HashMap<>();

                    Collections.sort(trucks,(o1, o2) -> o1.getId() - o2.getId());
                    payload.put("trucks",trucks);
                    payload.put("orders",messagePackages);
                    payload.put("startTime",new Date(timer.getTime()));
                    payload.put("endTime",new Date(timer.getTime() + (long)(12 * 60 * 1000)));
                    payload.put("initialTime", null);
                    payload.put("stations",map.getIntermediatePlants());
                    payload.put("blockings",processBlockings);
                    Map<String, Map> response = new HashMap<>();
                    response.put("data",payload);

                    /**Send message **/
                    Thread.sleep(2000);
                    sseEmitter.send(SseEmitter.event().name(guid).data(response));
                    //lastProcessOrders = processOrders;
                    lastProcessPackages = processPackages;


                    deliveredPackages = problem.moveTrucks();

                    ArrayList<Request> ordersIterator = new ArrayList<>(ordersAux);

                    duePackages = new ArrayList<>(problem.getPacks());
                    /**
                     for(Request order: deliveredPackages){
                     if(ordersAux.contains(order)){
                     ordersAux.remove(order);
                     }
                     }
                     **/
                    //processOrders = new ArrayList<>();a
                    //processBlockings = new ArrayList<>();
                    processPackages = new ArrayList<>();
                    //System.out.println(ordersAux);
                    //if (duePackages.size() > 0)
                    //    processPackages.addAll(duePackages);
                }
                int day = timer.getDate();
                int hours = timer.getHours();
                int minutes = timer.getMinutes();
                for(Request order : orders){
                    if(order.getDateOrder().getDate()==day
                            && order.getDateOrder().getHours()==hours
                            && order.getDateOrder().getMinutes()==minutes){
                        //orders.add(order);
                        processPackages.addAll(order.splitSimulation());
                    }

                }
//                if(blockings != null){
//                    for(Blocking blocking : blockings){
//                        if(blocking.getStart().getTime() <= timer.getTime() &&
//                                blocking.getEnd().getTime() >= timer.getTime() && !processBlockings.contains(blocking)){
//                            processBlockings.add(blocking);
//                        }
//                    }
//                }

                timer = new Date(timer.getTime() + (long)60*1000);
            }

        } catch (Exception e) {
            sseEmitter.completeWithError(e);
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }

    }

}


