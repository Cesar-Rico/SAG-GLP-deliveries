package pe.edu.pucp.empresasag.model;

import lombok.Getter;
import lombok.Setter;
import pe.edu.pucp.empresasag.auxiliar.StringListConverter;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Entity
@Getter
@Setter
public class Truck {
    private static int n=0;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private int id;
    private int x = 12;
    private int _x = 12; // Save last position x
    private int y = 8;
    private int _y = 7; // Save last position y
    private int type;
    private double grossWeight = 0;
    private int lpgCapacity;
    private int lpgMeter = 0;
    private double fuelTankCapacity = 25;
    private double fuelMeter = 0;
    private int counter = 0;
    @Transient
    private ArrayList<Package> packages = null;
    @Transient
    private ArrayList<Node> route = null;
    @Column(columnDefinition="TEXT")
    @Convert(converter = StringListConverter.class)
    private List<String> l_packages;
    @Column(columnDefinition="TEXT")
    @Convert(converter = StringListConverter.class)
    private List<String> l_route;
    @Column(columnDefinition="TEXT")
    private String routeReport;


    private boolean active;


    private boolean averiado = false;
    private Date start_averia;
    private Date end_averia;
    private boolean flag_reasign = false;

    public Truck(){}

    public Truck(int id,int grossWeight, int lpgCapacity) {
        this.id = id;
        this.grossWeight = grossWeight;
        this.lpgCapacity = lpgCapacity;
        this.fuelMeter = this.fuelTankCapacity;
        this.lpgMeter = this.lpgCapacity;
        this.setPackages(new ArrayList<Package>());
        this.route = new ArrayList<Node>();
        this.active = true;
//        this.averiado = false;
    }

    @Override
    public String toString() {
        String res = "Truck"+id+":";
        for(Package pck : getPackages())
            res = res + " " + pck;
        return res;
    }


    public int getId() {
        return id;
    }

    public void setId(int idTruck) {
        this.id = id;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getCounter() {
        return counter;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(double grossWeight) {
        this.grossWeight = grossWeight;
    }

    public int getLpgMeter() {
        return lpgMeter;
    }

    public void setLpgMeter(int lpgMeter) {
        this.lpgMeter = lpgMeter;
    }

    public int getLpgCapacity() {
        return lpgCapacity;
    }

    public void setLpgCapacity(int lpgCapacity) {
        this.lpgCapacity = lpgCapacity;
    }

    public boolean getActive() {
        return active;
    }
//    public boolean getAveriado() {
//        return averiado;
//    }

    public void setActive(boolean active) {
        this.active = active;
    }
//    public void setAveriado(boolean averiado) {
//        this.averiado = averiado;
//    }

    public double getFuelTankCapacity() {
        return fuelTankCapacity;
    }

    public void setFuelTankCapacity(double fuelTankCapacity) {
        this.fuelTankCapacity = fuelTankCapacity;
    }

    public double getFuelMeter() {
        return fuelMeter;
    }

    public void setFuelMeter(double fuelMeter) {
        this.fuelMeter = fuelMeter;
    }

    public double getFuelComsuption(int lpgMeter, int distance){
        double combinedWeight = this.grossWeight + lpgMeter*0.5;
        return distance*combinedWeight/150;
    }

    public void consumeFuel(int distance){
        this.fuelMeter-= this.getFuelComsuption(this.lpgMeter,distance);
    }

    public ArrayList<Package> getPackages() {
        return packages;
    }

    public void setPackages(ArrayList<Package> route) {
        this.packages = route;
    }

    public ArrayList<Node> getRoute() {
        return route;
    }

    public void setRoute(ArrayList<Node> route) {
        this.route = route;
    }

    public int get_x() {
        return _x;
    }

    public void set_x(int _x) {
        this._x = _x;
    }

    public int get_y() {
        return _y;
    }

    public void set_y(int _y) {
        this._y = _y;
    }

    public  void reinitialize(){
        this.x = 12;
        this._x = 12;
        this.y = 8;
        this._y = 7;
        this.lpgMeter = lpgCapacity;
        this.fuelMeter = fuelTankCapacity;
        this.packages = new ArrayList<>();
        this.route = new ArrayList<>();
        this.active = true;
        this.setFlag_reasign(false);
        this.averiado = false;
        this.routeReport = "";
    }

    public void buildRoute(CityMap map){

        ArrayList<Node> nodes = new ArrayList<>();
        int _w = map.getWidth();

        for(int i=0;i<l_route.size();i+=2){
            int pos = Integer.parseInt(l_route.get(i+1))*_w + Integer.parseInt(l_route.get(i));
            Node nod = map.getVertax().get(pos);
            nodes.add(nod);
        }

        setRoute(nodes);
    }

    public void build_l_route(){
        List<String> list = new ArrayList<>();
        for(Node node : getRoute()){
            list.add(String.valueOf(node.getX()));
            list.add(String.valueOf(node.getY()));
        }
        setL_route(list);
    }

    public void build_l_packages(){
        List<String> list = new ArrayList<>();
        for(Package pack : getPackages()){
            list.add(String.valueOf(pack.getId()));
        }
        setL_packages(list);
    }


    public void buildPackages(HashMap<String,Package> mapPackages){
        ArrayList<Package> packs = new ArrayList<>();
        for(int i=0;i<l_packages.size();i++){
            packs.add(mapPackages.get(l_packages.get(i)));
        }
        setPackages(packs);
    }

    public void dropPackages(ArrayList<Package> processPackages){
        while(getPackages().size()>0){
            Package pck = getPackages().remove(0);
            if(pck.getIsOrder()){
                processPackages.add(pck);
            }
        }
        while(getRoute().size()>0){
            Node node = getRoute().remove(0);
        }
    }

    public void collectPackages(ArrayList<Package> processPackages){
        Package nod = null;
        if(getPackages().size()>0){
            nod = getPackages().remove(0);
            if (!nod.getIsOrder()) nod = null;
        }

        while(getPackages().size()>0){
            Package pck = getPackages().remove(0);
            if(pck.getIsOrder()){
                processPackages.add(pck);
            }
        }
        if(nod!=null) getPackages().add(nod);

        while(getRoute().size()>0){
            Node node = getRoute().remove(0);
        }
    }

    public void move(){
        if(getRoute().size()>0){
            Node node = getRoute().remove(0);
            set_x(getX());
            set_y(getY());
            setX(node.getX());
            setY(node.getY());
            consumeFuel(1);
            ArrayList<String> list =  new ArrayList<>(l_route);
            list.remove(0);
            list.remove(0);
            setL_route(list);
        }
    }

    public void cleanPackages(CityMap map, Date timer){

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        if(getPackages().size()>0){
            int _w = map.getWidth();

            boolean flag = true;
            do{
                Node src = map.getVertax().get(getY()*_w + getX());
                Node pack = map.getNode(getPackages().get(0));
                /**Check if the vehicle position matches the package**/
                if(src.compare(pack)){
                    Package pck = getPackages().remove(0);
                    boolean isOrder = pck.getIsOrder();
                    if (isOrder) {
                        /**Set up truck that delivers package**/
                        pck.setTruck(this);
                        pck.setCompleted(true);
                        pck.setDateCompleted(timer);

                        Request ord = pck.getRequest();
                        ord.setCompletedGLP(ord.getCompletedGLP() + pck.getAmountGLP());
                        if(ord.getCompletedGLP() == ord.getAmountGLP()) ord.setCompleted(true);
                        setLpgMeter(getLpgMeter() -  pck.getAmountGLP());
                        if(ord.getDateCompleted()==null){
                            ord.setDateCompleted(timer);
                        }else{
                            if(timer.compareTo(ord.getDateCompleted())>0){
                                ord.setDateCompleted(timer);
                            }
                        }

                        Date limitDate = new Date(ord.getDateOrder().getTime() + ord.gethLimit()*3600*1000);
                        System.out.println("Truck "+getId()+" ("+getLpgMeter()+"GLP)"+" delivered package "+pck.getId()
                                +" ("+pck.getAmountGLP()+") | Order "+ord.getId()+" ("+ord.getCompletedGLP()+"/"
                                +ord.getAmountGLP()+") ("+ ord.getX()+ "," +ord.getY() + ")- RD: "+dateFormat.format(timer) +" LD: "+dateFormat.format(limitDate));

                    }else{
                        /**On Station **/
                        Station sta = pck.getStation();
                        if(!sta.getIsCentral()){
                            sta.setLpgMeter(sta.getLpgMeter() - (getLpgCapacity() - getLpgMeter()));
                        }
                        setFuelMeter(getFuelTankCapacity());
                        setLpgMeter(getLpgCapacity());
                        System.out.println("Truck "+ getId()+" ("+getLpgMeter()+"GLP)"+" recharge GLP on Station ("+sta.getX()+","+sta.getY()+") - "+ dateFormat.format(timer));
                    }
                }else flag=false;

            }while(getPackages().size()>0 && flag);
        }

    }
}

