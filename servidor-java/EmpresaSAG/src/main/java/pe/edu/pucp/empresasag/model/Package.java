package pe.edu.pucp.empresasag.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Package {
    private static  int n=3;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private int id;
    @ManyToOne(optional = true)
    @JoinColumn(columnDefinition="integer", name="request_id")
    private Request request;
    @OneToOne(optional = true)
    private Station station;
    @ManyToOne(optional = true)
    @JoinColumn(columnDefinition="integer", name="truck_id")
    private Truck truck;
    private Date dateCompleted;
    private int amountGLP;
    private boolean completed;
    private boolean isOrder;

    public Package() {

    }

    public Package(boolean simulation, Request request, int amountGLP) {
        n+=1;
        this.id = n;
        this.request = request;
        this.setStation(null);
        this.setTruck(null);
        this.setDateCompleted(null);
        this.setAmountGLP(amountGLP);
        this.setIsOrder(true);
        this.setCompleted(false);
    }

    public Package(Request request, int amountGLP) {
        this.request = request;
        this.setStation(null);
        this.setTruck(null);
        this.setDateCompleted(null);
        this.setAmountGLP(amountGLP);
        this.setIsOrder(true);
        this.setCompleted(false);
    }


    public Package(int id,Station station) {
        this.id = id;
        this.request = null;
        this.setStation(station);
        this.setTruck(null);
        this.setDateCompleted(null);
        this.setAmountGLP(0);
        this.setIsOrder(false);
        this.setCompleted(false);
    }
//
//    public Package(int id, Order order, Station station, int amountGLP, boolean isOrder){
//        this.id = id;
//        this.order = order;
//        this.setStation(station);
//        this.setTruck(null);
//        this.setAmountGLP(amountGLP);
//        this.setIsOrder(isOrder);
//        this.setCompleted(isOrder ? false : true);
//        this.setDateCompleted(null);
//    }


    public static void clear(){n =3;}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Truck getTruck() {
        return truck;
    }

    public void setTruck(Truck truck) {
        this.truck = truck;
    }

    public Date getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(Date dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public int getAmountGLP() {
        return amountGLP;
    }

    public void setAmountGLP(int amountGLP) {
        this.amountGLP = amountGLP;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean getIsOrder() {
        return isOrder;
    }

    public void setIsOrder(boolean order) {
        isOrder = order;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    @Override
    public String toString() {
        String res;
        if (getIsOrder()){
           res = "("+request.getX()+","+request.getY()+")";
        }
        else{
            res = "("+station.getX()+","+station.getY()+")";
        }
        return res;
    }

}
