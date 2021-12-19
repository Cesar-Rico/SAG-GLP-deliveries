package pe.edu.pucp.empresasag.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;


public class Node {

    private int idNode;
    private int x;
    private int y;
    @JsonIgnore
    private ArrayList<Node> edges;

    public Node(){

    }
    public Node(int x, int y) {
        this.setX(x);
        this.setY(y);
        setEdges(new ArrayList<Node>());
    }

    @Override
    public String toString() {
        String res = "("+ getX() +","+ getY() +")";
        return res;
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

    public ArrayList<Node> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<Node> edges) {
        this.edges = edges;
    }

    public boolean compare(Node node){
        return this.x == node.getX() && this.y ==node.getY();
    }
}
