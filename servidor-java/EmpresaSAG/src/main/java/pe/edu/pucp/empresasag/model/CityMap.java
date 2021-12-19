package pe.edu.pucp.empresasag.model;

import java.util.ArrayList;
import java.util.Date;

public class CityMap {
    private int height;
    private int width;
    private ArrayList<Node> vertax;
    private Package central;
    private ArrayList<Package> intermediatePlants;
    private ArrayList<Blocking> blockings;

    public CityMap(int width, int height){
        this.width = width;
        this.height = height;
        vertax = new ArrayList<>();
        this.central = null;
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                vertax.add(new Node(j,i));
            }
        }
        intermediatePlants = new ArrayList<>();
        blockings = new ArrayList<>();

        buildEdges();
    }


    /**Construct edges for the graph**/
    public void buildEdges(){
        int x,y;
        for(Node nod : getVertax()){
            x = nod.getX();
            y = nod.getY();
            if(x-1>=0)
                addEdge(x, y, x-1, y);
            if(x+1<width)
                addEdge(x, y, x+1, y);
            if(y-1>=0)
                addEdge(x, y, x, y-1);
            if(y+1<height)
                addEdge(x, y, x, y+1);
        }
    }


    public Node getNode(Package pack){
        if (pack.getIsOrder()){
            return vertax.get(pack.getRequest().getY()*width + pack.getRequest().getX());
        }

        return  vertax.get(pack.getStation().getY()*width + pack.getStation().getX());
    }

    public void addEdge(int x1, int y1, int x2, int y2) {
        int pos1, pos2;
        pos1 = (y1*width) + x1;
        pos2 = (y2*width) + x2;
        //System.out.println(vertax.get(pos1).toString()+"-"+vertax.get(pos2).toString());
        if(!vertax.get(pos1).getEdges().contains(vertax.get(pos2))){
            vertax.get(pos1).getEdges().add(vertax.get(pos2));
            vertax.get(pos2).getEdges().add(vertax.get(pos1));
        }
    }

    public void removeEdge(int x1, int y1, int x2, int y2){
        int pos1, pos2;
        pos1 = (y1*width) + x1;
        pos2 = (y2*width) + x2;
        //System.out.println("Remove: "+vertax.get(pos1).toString()+"-"+vertax.get(pos2).toString());
        if(!vertax.get(pos1).getEdges().contains(vertax.get(pos2))){
            vertax.get(pos1).getEdges().remove(vertax.get(pos2));
            vertax.get(pos2).getEdges().remove(vertax.get(pos1));
        }
    }

    public ArrayList<Node> getBlockingsForNodeAndHour(Node nod, Date currentDate){
        ArrayList<Node> arr = new ArrayList<Node>();
        for(Blocking block : blockings){
            if(currentDate.after(block.getStart()) && currentDate.before(block.getEnd()) && block.getNodes().contains(nod)){
                for(Node node : block.getNodes()){
                    if(node.getX()==nod.getX() || node.getY()==nod.getY())
                        arr.add(node);
                }
            }
        }
        return arr;
    }

    public boolean isBlockingForNodeAndHour(Node nod, Date currentDate){
        int pos = (nod.getY()*width) + nod.getX();
        for(Blocking block : blockings){
            if(currentDate.compareTo(block.getStart())<0) break;
            if(currentDate.after(block.getStart()) && currentDate.before(block.getEnd())){
                if(block.getHash().containsKey(pos)) return true;
            }
        }
        return false;
    }

    public void constructBlockings(ArrayList<Blocking> b1, ArrayList<Blocking> b2){
        ArrayList<Blocking> blockings = new ArrayList<>();
        for(Blocking blocking : b1) blockings.add(blocking);
        for(Blocking blocking : b2) blockings.add(blocking);
        setBlockings(blockings);
    }

    @Override
    public String toString() {
        String res = "\n";
        for(Node nod : vertax){
            res = res + nod.toString() + " ";
            if(nod.getX()==width-1)
                res = res + '\n';
        }
        return res;
    }

    /**
     * @return int return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @return int return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @return ArrayList<Node> return the vertax
     */
    public ArrayList<Node> getVertax() {
        return vertax;
    }

    /**
     * @param vertax the vertax to set
     */
    public void setVertax(ArrayList<Node> vertax) {
        this.vertax = vertax;
    }

    /**
     * @return Node return the central
     */
    public Package getCentral() {
        return central;
    }

    /**
     * @param central the central to set
     */
    public void setCentral(Package central) {
        this.central = central;
    }



    /**
     * @return ArrayList<Node> return the intermediatePlant
     */
    public ArrayList<Package> getIntermediatePlants() {
        return intermediatePlants;
    }

    /**
     * @param intermediatePlants the intermediatePlant to set
     */
    public void setIntermediatePlants(ArrayList<Package> intermediatePlants) {
        this.intermediatePlants = intermediatePlants;
    }

    /**
     * @return ArrayList<Blocking> return the blockings
     */
    public ArrayList<Blocking> getBlockings() {
        return blockings;
    }

    /**
     * @param blockings the blockings to set
     */
    public void setBlockings(ArrayList<Blocking> blockings) {
        this.blockings = blockings;
    }

}
