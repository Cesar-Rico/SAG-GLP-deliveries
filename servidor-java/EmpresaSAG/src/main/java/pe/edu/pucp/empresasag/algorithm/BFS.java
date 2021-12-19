package pe.edu.pucp.empresasag.algorithm;

import pe.edu.pucp.empresasag.model.CityMap;
import pe.edu.pucp.empresasag.model.Node;
import pe.edu.pucp.empresasag.model.Truck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;

public class BFS {
    public static final long HOUR = 3600*1000;

    public boolean shortestPathWithHours(CityMap map, Node src, Node dest,Node last, Date currentTime, int pred[], int dist[]){
        int pos1,pos2,posf;
        long h;
        Double timetravel=0.0;
        int v = map.getVertax().size();
        int w = map.getWidth();
        LinkedList<Node> queue = new LinkedList<>();
        boolean visited[] = new boolean[v];
        Double timeVisited[] = new Double[v];
        ArrayList<Node> blockings;

        pos1 = (src.getY()*w) + src.getX();
        posf = (dest.getY()*w) + dest.getX();

        if(Math.abs(last.getX() - src.getX()) + Math.abs(last.getY() - src.getY()) >1){
            System.out.println("Error in last node!");
        }
        if(pos1 == posf){
            dist[posf] = 0;
            return true;
        }

        for (int i=0; i<v; i++){
            visited[i] = false;
            dist[i] = Integer.MAX_VALUE;
            pred[i] = -1;
            timeVisited[i] = 0.0;
        }

        visited[pos1] = true;
        dist[pos1] = 0;
        timeVisited[pos1] = 0.0;
        queue.add(map.getVertax().get(pos1));

        blockings = map.getBlockingsForNodeAndHour(src,currentTime);
        /**If starting point is in a blocking**/
        if (blockings.contains(src)){

            /**The only possible movement is towards the previous position**/
            timetravel = Math.round((timeVisited[pos1] + 0.02)*100)/100.0;
            Node u = queue.remove();

            pos2 = last.getY()*w + last.getX();

            visited[pos2] = true;
            dist[pos2] = dist[pos1] + 1;
            timeVisited[pos2] = timetravel;
            pred[pos2] = pos1;
            queue.add(map.getVertax().get(pos2));
            if(pos2 == posf) return true;

        }

        while(!queue.isEmpty()){
            Node u = queue.remove();
            pos1 = (u.getY()*w) + u.getX();
            timetravel = Math.round((timeVisited[pos1] + 0.02)*100)/100.0;
            h = (long) (timetravel * HOUR);
            for(Node nod : u.getEdges()){
                pos2 = (nod.getY()*w) + nod.getX();
                if(visited[pos2] == true)continue;
                //blockings = map.getBlockingsForNodeAndHour(nod, new Date(currentTime.getTime() + h));
                boolean flag = map.isBlockingForNodeAndHour(nod, new Date(currentTime.getTime() + h));
                if(!flag || pos2 == posf){
                    if(visited[pos2]==false){
                        visited[pos2] = true;
                        dist[pos2] = dist[pos1] + 1;
                        timeVisited[pos2] = timetravel;
                        pred[pos2] = pos1;
                        queue.add(map.getVertax().get(pos2));
                        if(pos2 == posf){
                            return true;
                        }
                    }
                }
            }
        }

        return false;

    }

    public ArrayList<Integer> getRoute(CityMap map, Node src, Node dest, int pred[] ){

        int w = map.getWidth();

        int start = (src.getY()*w) + src.getX();
        int end = (dest.getY()*w) + dest.getX();
        int pos = end;

        ArrayList<Integer> route = new ArrayList<>();
        route.add(end);

        while(pos != start){
            pos = pred[pos];
            route.add(pos);
        }

        Collections.reverse(route);
        return route;
    }

    public ArrayList<Node> getPath(CityMap map,ArrayList<Integer> route){
        ArrayList<Node> path = new ArrayList<>();

        for(int i=0;i<route.size();i++){
            Node nod = map.getVertax().get(route.get(i));
            path.add(nod);
        }
        return path;
    }

    public int getDistance(Node src, Node dest){
        return Math.abs(src.getX() - dest.getX()) + Math.abs(src.getY()- dest.getY());
    }

}
