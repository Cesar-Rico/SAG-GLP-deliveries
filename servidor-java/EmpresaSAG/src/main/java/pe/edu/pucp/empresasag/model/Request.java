package pe.edu.pucp.empresasag.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
public class Request {
    private static int n = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private int id;
    private Date dateOrder;
    private Date dateCompleted;
    private int amountGLP;
    private int completedGLP;
    private int hLimit;
    private boolean completed;
    private int x;
    private int y;

    public Request(int id,Date dateOrder,int amountGLP,int hLimit,int x,int y){
        this.id = id;
        this.dateOrder = dateOrder;
        this.dateCompleted = null;
        this.amountGLP  = amountGLP;
        this.completedGLP = 0;
        this.hLimit = hLimit;
        this.completed = false;
        this.x = x;
        this.y = y;
    }

    public Request(Date dateOrder, int amountGLP, int hLimit, int x, int y){
        n+=1;
        this.id = n ;
        this.dateOrder = dateOrder;
        this.amountGLP = amountGLP;
        this.hLimit = hLimit;
        this.x = x;
        this.y = y;
        this.completed = false;
        this.dateCompleted = null;
        this.completedGLP = 0;
    }

    public static void clear(){n =0;}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(Date dateOrder) {
        this.dateOrder = dateOrder;
    }

    public int getAmountGLP() {
        return amountGLP;
    }

    public void setAmountGLP(int amountGLP) {
        this.amountGLP = amountGLP;
    }

    public int gethLimit() {
        return hLimit;
    }

    public void sethLimit(int hLimit) {
        this.hLimit = hLimit;
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


    @Override
    public String toString() {
        String res = "("+ id+"|"+completedGLP+"/" +amountGLP +")";
        return res;
    }

    public ArrayList<Package> split(){

        ArrayList<Package> packages = new ArrayList<>();

        int valueGlp = getAmountGLP();

        while(valueGlp > 5) {
            packages.add(new Package(this, 5));
            valueGlp = valueGlp - 5;
        }
        packages.add(new Package(this,valueGlp));
        return packages;
    }

    public ArrayList<Package> splitSimulation(){

        ArrayList<Package> packages = new ArrayList<>();

        int valueGlp = getAmountGLP();

        while(valueGlp > 5) {
            packages.add(new Package(true,this, 5));
            valueGlp = valueGlp - 5;
        }
        packages.add(new Package(true,this,valueGlp));
        return packages;
    }

}
