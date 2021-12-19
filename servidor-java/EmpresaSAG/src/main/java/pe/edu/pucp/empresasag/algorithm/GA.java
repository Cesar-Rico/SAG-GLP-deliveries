package pe.edu.pucp.empresasag.algorithm;

import io.jenetics.EnumGene;
import io.jenetics.engine.Codec;
import io.jenetics.engine.Codecs;
import io.jenetics.engine.Problem;
import io.jenetics.util.ISeq;
import pe.edu.pucp.empresasag.model.*;
import pe.edu.pucp.empresasag.model.Package;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GA implements Problem<Map<Package, Truck>, EnumGene<Integer>, Double> {

    private static final long HOUR = 3600*1000;
    private final ISeq<Truck> _trucks;
    private final ISeq<Package> _packages;
    private final CityMap _map;
    private final Date startTime;
    private final int _v;
    private final int _w;
    private BFS bfs;
    private static final int BATCH_TIME = 12;

    public GA(ISeq<Truck> trucks, ISeq<Package> packages, CityMap map, Date startTime) {
        this._trucks = trucks;
        this._packages = packages;
        this._map = map;
        this._v = map.getVertax().size();
        this._w = map.getWidth();
        this.startTime = startTime;
        bfs = new BFS();
    }


    @Override
    public Function<Map<Package,Truck>, Double> fitness() {
        return input -> {

            List<Truck> listed = new ArrayList<>(input.values());
            listed = listed.stream().distinct().collect(Collectors.toList());

            double max = 0;

            for(Truck truck : listed){

                ArrayList<Package> packs = new ArrayList<>();

                int glp = truck.getLpgMeter();
                double hoursTraveled = 0.0;

                if (truck.getPackages().size() == 1){
                    packs.add(truck.getPackages().get(0));
                    glp -= truck.getPackages().get(0).getAmountGLP();
                }

                for(Map.Entry<Package,Truck> entry : input.entrySet()){
                    if(entry.getValue().equals(truck)){

                        if(entry.getKey().getAmountGLP()>glp){
                            packs.add(_map.getCentral());
                            glp = truck.getLpgCapacity();

                        }
                        packs.add(entry.getKey());
                        glp -= entry.getKey().getAmountGLP();
                    }
                }



                glp = truck.getLpgMeter();
                double fuel = truck.getFuelMeter();
                double remainingHours = 0.0;
                double fuelComsuption = 0.0;
                double penality = 0.0;

                long h;
                int distance;

                for (int i = 0; i < packs.size(); i++) {

                    Node node0 = i!=0 ?
                            _map.getNode(packs.get(i-1))
                            : _map.getVertax().get(truck.getY()*_w + truck.getX());

                    /** Determination of the nearest supply point**/
                    if (!packs.get(i).getIsOrder()) {
                        int d = 5000;

                        for (Package nod : _map.getIntermediatePlants()) {
                            distance  = bfs.getDistance(node0,_map.getNode(nod));
                            if(nod.getStation().getLpgMeter()> 0.2*nod.getStation().getLpgCapacity()) {
                                if (d > distance) {
                                    d = distance;
                                    packs.set(i, nod);
                                }
                            }
                        }

                        distance = bfs.getDistance(node0, _map.getNode(_map.getCentral()));
                        if (d > distance){
                            d = distance;
                            packs.set(i,_map.getCentral());
                        }

                    }

                    Node node1 = _map.getNode(packs.get(i));

                    /**Moving the truck from order i-1 to order i**/
                    h = (long) (hoursTraveled * HOUR);
                    distance = bfs.getDistance(node0,node1);
                    hoursTraveled += Double.valueOf(distance)/50;
                    fuelComsuption = truck.getFuelComsuption(glp, distance);
                    if (fuelComsuption > fuel) {
                        penality += 2000.0;
                    }
                    fuel -= fuelComsuption;
                    if (!packs.get(i).getIsOrder()) {
                        glp = truck.getLpgCapacity();
                        fuel = truck.getFuelTankCapacity();
                    } else if (packs.get(i).getIsOrder()) {
                        Request ord = packs.get(i).getRequest();
                        double num = (startTime.getTime() - ord.getDateOrder().getTime());
                        num = num/(double) HOUR;
                        remainingHours = (double)ord.gethLimit()-num;
                        if(packs.get(i).getAmountGLP() > truck.getLpgCapacity()){
                            penality += 10000.0;
                        }
                        glp -= packs.get(i).getAmountGLP();
                        if (remainingHours < hoursTraveled) {
                            penality += 1000;
                        }
                    }

                    /**Moving the truck from last order to central**/
                    if (i == packs.size() - 1) {
                        h = (long) (hoursTraveled * HOUR);
                        distance = bfs.getDistance(node1, _map.getNode(_map.getCentral()));
                        hoursTraveled += Double.valueOf(distance) / 50;
                        fuelComsuption = truck.getFuelComsuption(glp, distance);
                        if (fuelComsuption > fuel) {
                            penality += 3000.0;
                        }
                        fuel -= fuelComsuption;
                    }

                }

                hoursTraveled+=penality;
                if(hoursTraveled>max){
                    max = hoursTraveled;
                }
            }

            return max;
        };
    }

    @Override
    public Codec<Map<Package, Truck>, EnumGene<Integer>> codec() {
        return Codecs.ofMapping(_packages, _trucks, HashMap::new);
    }

    public void decoding(Map<Package, Truck> list,ArrayList<Truck> trucks){

        long h;
        int pos;
        int pred[] = new int[_v];
        int dist[] = new int[_v];

        for(Truck truck: trucks){

            ArrayList<Package> packs = new ArrayList<>();

            int glp = truck.getLpgMeter();
            double hoursTraveled = 0.0;

            if (truck.getPackages().size() == 1){
                packs.add(truck.getPackages().get(0));
                glp -= truck.getPackages().get(0).getAmountGLP();
            }


            for(Map.Entry<Package,Truck> entry : list.entrySet()){
                if(entry.getValue().equals(truck)){

                    if(entry.getKey().getAmountGLP()>glp){
                        packs.add(_map.getCentral());
                        glp = truck.getLpgCapacity();
                    }
                    packs.add(entry.getKey());
                    glp -= entry.getKey().getAmountGLP();
                }
            }

            int distance;

            Node last = _map.getVertax().get(truck.get_y()*_w + truck.get_x());

            for(int i=0;i<packs.size();i++){

                Node node0 = i!=0 ?
                        _map.getNode(packs.get(i-1))
                        : _map.getVertax().get(truck.getY()*_w + truck.getX());

                /** Determination of the nearest supply point**/
                if (!packs.get(i).getIsOrder()) {
                    int d = 5000;
                    h = (long) (hoursTraveled * HOUR);

                    for (Package nod : _map.getIntermediatePlants()) {
                        if (nod.getStation().getLpgMeter()> 0.2*nod.getStation().getLpgCapacity()){
                            Node station = _map.getNode(nod);
                            if (bfs.shortestPathWithHours(_map, node0, station, last, new Date(startTime.getTime() + h), pred, dist)) {
                                distance = dist[(station.getY() * _w) + station.getX()];
                                if (d > distance) {
                                    d = distance;
                                    packs.set(i, nod);
                                }
                            }
                        }
                    }

                    Node station = _map.getNode(_map.getCentral());
                    if (bfs.shortestPathWithHours(_map, node0, station,last, new Date(startTime.getTime() + h), pred, dist)) {
                        distance = dist[(station.getY() * _w) + station.getX()];
                        if (d > distance) {
                            d = distance;
                            packs.set(i, _map.getCentral());
                        }
                    }
                }


                Node node1 = _map.getNode(packs.get(i));

                /**Moving the truck from order i-1 to order i**/
                h = (long) (hoursTraveled * HOUR);
                if (bfs.shortestPathWithHours(_map, node0, node1,last, new Date(startTime.getTime() + h), pred, dist)) {
                    pos = (node1.getY()*_w + node1.getX());
                    distance = dist[pos];
                    if(distance>0) last = _map.getVertax().get(pred[pos]);
                    hoursTraveled = Math.round((hoursTraveled + Double.valueOf(distance) / 50)*100)/100.0;
                    ArrayList<Integer> route = bfs.getRoute(_map,node0,node1,pred);
                    ArrayList<Node> routes = bfs.getPath(_map,route);
                    routes.remove(routes.size()-1);
                    truck.getRoute().addAll(routes);
                }

            }

            truck.setPackages(packs);
            truck.getPackages().add(_map.getCentral());
            Node node1 = _map.getNode(_map.getCentral());
            Node node0 = truck.getPackages().size()>1 ?
                    _map.getNode(truck.getPackages().get(truck.getPackages().size()-2))
                    : _map.getVertax().get(truck.getY()*_w + truck.getX());
            h = (long) (hoursTraveled * HOUR);
            if(bfs.shortestPathWithHours(_map,node0,node1,last,new Date(startTime.getTime() + h), pred, dist)){
                ArrayList<Integer> route = bfs.getRoute(_map,node0,node1,pred);
                ArrayList<Node> routes = bfs.getPath(_map,route);
                truck.getRoute().addAll(routes);
            }
            if(truck.getRoute().size()>0){
                node1 = _map.getVertax().get(truck.getRoute().get(0).getY()*_w + truck.getRoute().get(0).getX());
                node0  = _map.getVertax().get(truck.getY()*_w + truck.getX());
                if(node0.compare(node1)){
                    truck.getRoute().remove(0);
                }
            }
        }
    }


}
