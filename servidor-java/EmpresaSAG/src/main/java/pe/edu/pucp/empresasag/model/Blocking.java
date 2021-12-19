package pe.edu.pucp.empresasag.model;

import pe.edu.pucp.empresasag.auxiliar.StringListConverter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Entity
public class Blocking {
    private static  int n=0;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private int id;
    @Column(name="startTime")
    private Date start;
    @Column(name="endTime")
    private Date end;
    private int numberDate;
    @Transient
    private ArrayList<Node> nodes;
    @Column(columnDefinition="TEXT")
    @Convert(converter = StringListConverter.class)
    private List<String> l_nodes;
    @Transient
    private HashMap<Integer,Boolean> hash;

    public Blocking() {

    }

    public Blocking(Date start, Date end, String nods, CityMap map) {
        this.setStart(start);
        this.setEnd(end);
        this.setNodes(new ArrayList<Node>());
        this.l_nodes = new ArrayList<>();
        int[] numbers = Arrays.stream(nods.split(",")).mapToInt(Integer::parseInt).toArray();

        for(int i=0;i<numbers.length;i+=1){
           l_nodes.add(String.valueOf(numbers[i]));
        }
    }

    public void buildHash(int w, int h){
        setHash(new HashMap<>());
        for(Node node : nodes){
            int pos = (node.getY()*w) + node.getX();
            getHash().put(pos,true);
        }
    }



    public String toString(){
        String res = getStart() + "/" + getEnd() + ": ";
        for(Node nod: getNodes()){
            res = res + nod.toString() + " ";
        }
        return res;
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

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<Node> nodes) {
        this.nodes = nodes;
    }

    public int getNumberDate() {
        return numberDate;
    }

    public void setNumberDate(int numberDate) {
        this.numberDate = numberDate;
    }

    public void buildNodes(CityMap map){

        ArrayList<Node> nodes = new ArrayList<>();
        int _w = map.getWidth();
        int pos;

        for(int i=0; i<l_nodes.size()-2; i+=2){

            int Vx = Integer.parseInt(l_nodes.get(i)), Vy = Integer.parseInt(l_nodes.get(i+1));
            int Wx = Integer.parseInt(l_nodes.get(i+2)), Wy = Integer.parseInt(l_nodes.get(i+3));
            int movX = 0,movY = 0;


            /**Vertical Line**/
            if(Vx == Wx){
                if(Vy > Wy) movY= -1;
                else movY = 1;
                /**Horiontal Line**/
            }else{
                if(Vx>Wx) movX=-1;
                else movX = 1;
            }
            /** Inserting nodes of the blocking**/
            while( Vx != Wx || Vy != Wy){
                pos = (Vy*_w) + Vx;
                nodes.add(map.getVertax().get(pos));
                Vx = Vx + movX;
                Vy = Vy + movY;
            }
        }

        /** Inseting last node of the blocking **/
        int size = l_nodes.size();
        pos = Integer.parseInt(l_nodes.get(size-1))*_w + Integer.parseInt(l_nodes.get(size-2));
        nodes.add(map.getVertax().get(pos));

        setNodes(nodes);
    }

    public List<String> getL_nodes() {
        return l_nodes;
    }

    public void setL_nodes(List<String> l_nodes) {
        this.l_nodes = l_nodes;
    }

    public void buildNumberDate(){
        LocalDate localDate = this.start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        this.numberDate = localDate.getYear()*10000 + localDate.getMonthValue()*100 + localDate.getDayOfMonth();
    }

    public HashMap<Integer, Boolean> getHash() {
        return hash;
    }

    public void setHash(HashMap<Integer, Boolean> hash) {
        this.hash = hash;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}