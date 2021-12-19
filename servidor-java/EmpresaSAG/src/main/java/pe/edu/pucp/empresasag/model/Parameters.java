package pe.edu.pucp.empresasag.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "parameters")
public class Parameters {
    private int width;
    private int height;
    private int speed;
    private int distance;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private int id;

    public Parameters(){}

    public Parameters(int id,int width,int height, int speed, int distance) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.speed  =speed;
        this.distance = distance;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int weight) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}