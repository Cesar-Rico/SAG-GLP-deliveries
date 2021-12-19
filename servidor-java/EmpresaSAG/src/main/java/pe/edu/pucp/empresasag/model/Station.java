package pe.edu.pucp.empresasag.model;

import javax.persistence.*;

@Entity
public class Station{

    private static  int n=0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private int id;
    private boolean isCentral;
    private int lpgCapacity;
    private int lpgMeter;
    private int x;
    private int y;


    public Station() {

    }


    public Station(int id,int x, int y, boolean isCentral, int lpgCapacity) {
        this.id = id;
        this.setX(x);
        this.setY(y);
        this.setIsCentral(isCentral);
        this.setLpgCapacity(lpgCapacity);
        this.setLpgMeter(lpgCapacity);
    }



    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getIsCentral() {
        return isCentral;
    }

    public void setIsCentral(boolean central) {
        isCentral = central;
    }

    public int getLpgCapacity() {
        return lpgCapacity;
    }

    public void setLpgCapacity(int lpgCapacity) {
        this.lpgCapacity = lpgCapacity;
    }

    public int getLpgMeter() {
        return lpgMeter;
    }

    public void setLpgMeter(int lpgMeter) {
        this.lpgMeter = lpgMeter;
    }

    public int getX() {return x;}

    public void setX(int x) {this.x = x;}

    public int getY() {return y;}

    public void setY(int y) {this.y = y;}
}
