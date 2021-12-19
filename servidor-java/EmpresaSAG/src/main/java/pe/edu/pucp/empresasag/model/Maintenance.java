package pe.edu.pucp.empresasag.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Maintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private int id;

    @Column(name="startTime")
    private Date start;
    @Column(name="endTime")
    private Date end;

    @ManyToOne(optional = true)
    @JoinColumn(columnDefinition="integer", name="truck_id")
    private Truck truck;
    private int type;
    private boolean completed;

    public Maintenance() {

    }

    public Maintenance(Date start, Date end, Truck truck, int type){
        this.start = start;
        this.end = end;
        this.truck = truck;
        this.type = type;
        this.completed = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int idTruck) {
        this.id = id;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Truck getTruck() {
        return truck;
    }

    public void setTruck(Truck truck) {
        this.truck = truck;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}

