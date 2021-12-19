package pe.edu.pucp.empresasag.tasks;


import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.pucp.empresasag.algorithm.VRPTW;
import pe.edu.pucp.empresasag.model.*;
import pe.edu.pucp.empresasag.model.Package;
import pe.edu.pucp.empresasag.service.*;

import java.util.*;
import java.util.stream.Collectors;


@Component
@EnableScheduling
public class Scheduler {

    ParametersService parametersService;
    PackageService packageService;
    TruckService truckService;
    MaintenanceService maintenanceService;
    BlockingService blockingService;
    RequestService requestService;
    public Parameters parameters;
    public HashMap<String, Package> mapPackages;
    public CityMap map;
    public ArrayList<Truck> trucks;


    Scheduler(ParametersService parametersService, PackageService packageService,
              TruckService truckService, MaintenanceService maintenanceService,
              BlockingService blockingService, RequestService requestService) {
        this.parametersService = parametersService;
        this.packageService = packageService;
        this.maintenanceService = maintenanceService;
        this.blockingService = blockingService;
        this.requestService = requestService;
        this.parameters = null;
        this.truckService = truckService;
        this.mapPackages = new HashMap<>();
        this.map = null;
        this.trucks = new ArrayList<>();

    }

    private boolean initialize = false;
    private int counter = -1;


    /**
     * Get information from BD
     **/
    private void prepareData() {
        parameters = parametersService.getParameters();
        if (parameters != null) {
            mapPackages = packageService.getIncompletedPackages();
            map = new CityMap(parameters.getWidth(), parameters.getHeight());
            packageService.buildPlants(map, mapPackages);
            trucks = truckService.getAllTrucks();
            for (Truck truck : trucks) {
                truck.buildRoute(map);
                truck.buildPackages(mapPackages);
            }
        }
    }


    private void moveTrucks() {
        for (Truck truck : trucks) {
            if (truck.getActive() && !truck.isAveriado()) truck.move();
        }
    }


    private void dropRequests(Date initialTime) {

        /**check if truck is in a critical node**/
        for (Truck truck : trucks) {
            if (truck.getActive()) truck.cleanPackages(map, initialTime);
        }
    }

    @Transactional
    public void validate_averias(Date initialTime) {

        ArrayList<Maintenance> maintenances = maintenanceService.getActiveMaintenances(initialTime);

        for (Truck truck : trucks) {
            if (truck.isFlag_reasign()) {
                truck.setStart_averia(initialTime);
                truck.setEnd_averia(new Date(initialTime.getTime() + 1 * 3600 * 1000));
                truck.setFlag_reasign(false);
                truck.setAveriado(true);
            }
            if (truck.isAveriado()) {
                if (truck.getEnd_averia().compareTo(initialTime) <= 0) {
                    truck.setAveriado(false);
                    truck.setStart_averia(null);
                    truck.setEnd_averia(null);
                    Maintenance newMaintenance =
                            new Maintenance(new Date(initialTime.getTime() - 1 * 3600 * 1000), new Date(initialTime.getTime() + 47 * 3600 * 1000), truck, 1); //Type Maintenance 1: Correctivo, 2:Preventivo
                    maintenances.add(newMaintenance);
                    truck.setActive(false);
                }
            }

            for (Maintenance maint : maintenances) {
                if (maint.getTruck().getId() == truck.getId()) {
                    if (maint.getStart().compareTo(initialTime) == 0) {
                        if (!truck.isAveriado() && truck.getActive()) {
                            truck.setActive(false);
                        } else {
                            maint.setCompleted(true);
                        }
                    }
                    if (maint.getEnd().compareTo(initialTime) <= 0) {
                        truck.setActive(true);
                        maint.setCompleted(true);
                        truck.setX(12);
                        truck.set_x(12);
                        truck.setY(8);
                        truck.set_y(7);
                    }
                }
            }
        }

        maintenanceService.saveMaintenance(maintenances);
    }


    public void routePlanning(Date initialTime) {

        packageService.buildPlants(map, mapPackages);
        ArrayList<Blocking> blockings = blockingService.getBlockingsByBatchTime(initialTime, map);
        Collections.sort(blockings,new Comparator(){
            public int compare(Object o1, Object o2){
                Integer x1 = ((Blocking)o1).getNumberDate();
                Integer x2 = ((Blocking)o2).getNumberDate();
                int sComp = x1.compareTo(x2);
                if (sComp !=0){
                    return sComp;
                }
                Date v1 = ((Blocking)o1).getStart();
                Date v2 = ((Blocking)o2).getStart();
                return v1.compareTo(v2);
            }
        });
        int _w = parameters.getWidth();
        int _h = parameters.getHeight();
        for(Blocking blocking : blockings){
            blocking.buildHash(_w,_h);
        }
        map.setBlockings(blockings);
        /**Collect Orders**/
        ArrayList<Request> orders = requestService.getOrdersByBatchTime(initialTime);
        ArrayList<Package> processPackages = packageService.splitOrders(orders);

        /**Collect incomplete packages**/
        for (Truck truck : trucks) {
            if (truck.isAveriado() || !truck.getActive()) {
                truck.dropPackages(processPackages);
            } else {
                truck.collectPackages(processPackages);
            }
        }

        ArrayList<Truck> activeTrucks = new ArrayList<>();
        for (Truck truck : trucks) {
            if (truck.getActive() && !truck.isAveriado()) {
                activeTrucks.add(truck);
            }
        }

        VRPTW problem = new VRPTW(activeTrucks, processPackages, map, initialTime);
        problem.solve();

    }


    @Transactional
    @Scheduled(cron = "*/12 * * * * *")
    public void realTime() {

        Date timer = new Date(System.currentTimeMillis());
        long time = timer.getTime();
        timer.setTime((time / 1000) * 1000);

        boolean ready = false;

        if (counter == 5) {
            ready = true;
            prepareData();
            moveTrucks();
            dropRequests(timer);
            validate_averias(timer);
        }

        if (initialize) {
            if (counter == 5) counter = 0;
            else counter++;
        }

        if ((timer.getSeconds() == 0 && timer.getMinutes() % 6 == 0)) {
            if (!initialize) {
                initialize = true;
                counter = 0;
                System.out.println("Real-time tracking running");
            }
            if (!ready) {
                ready = true;
                prepareData();
            }
            routePlanning(timer);
            dropRequests(timer);
            generateRouteReporte();
        }

        if (ready) {
            truckService.saveTrucks(trucks);
            List<Package> packs = mapPackages.values()
                    .stream()
                    .collect(Collectors.toList());
            packageService.savePackages(packs);
        }
    }


    private void generateRouteReporte() {
        for (Truck truck : trucks) {
            String hojaRuta = "";
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
                while(packs.size()>0){
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
                    if(truck.getPackages().size()>0){
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
            truck.setRouteReport(hojaRuta);
        }
    }
}
