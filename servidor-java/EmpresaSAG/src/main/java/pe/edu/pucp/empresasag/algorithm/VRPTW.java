package pe.edu.pucp.empresasag.algorithm;

import io.jenetics.*;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionStatistics;
import io.jenetics.util.MSeq;
import pe.edu.pucp.empresasag.model.*;
import pe.edu.pucp.empresasag.model.Package;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.jenetics.engine.EvolutionResult.toBestPhenotype;
import static io.jenetics.engine.Limits.bySteadyFitness;

public class VRPTW {

    private ArrayList<Truck> trucks;
    private ArrayList<Package> packs;
    private CityMap map;
    private Date initialTime;
    private GA ga;


    public VRPTW(ArrayList<Truck> trucks, ArrayList<Package> packs, CityMap map, Date initialTime){
        this.trucks = trucks;
        this.packs = packs;
        this.map = map;
        this.initialTime = initialTime;
        buildGA(trucks,packs,map,initialTime);

    }

    public void buildGA(ArrayList<Truck> tru, ArrayList<Package> pck, CityMap map, Date currentTime){
        final MSeq<Truck> trucks = MSeq.ofLength(tru.size());
        final MSeq<Package> packages = MSeq.ofLength(pck.size());
        for(int i=0; i<tru.size(); i++)
            trucks.set(i, tru.get(i));
        for(int i=0; i<pck.size(); i++)
            packages.set(i, pck.get(i));
        this.ga = new GA(trucks.toISeq(), packages.toISeq(), map, currentTime);

    }

    public ArrayList<Truck> getTrucks() {
        return trucks;
    }

    public void setTrucks(ArrayList<Truck> trucks) {
        this.trucks = trucks;
    }

    public ArrayList<Package> getPacks() {
        return packs;
    }

    public void setPacks(ArrayList<Package> packs) {
        this.packs = packs;
    }

    public CityMap getMap() {
        return map;
    }

    public void setMap(CityMap map) {
        this.map = map;
    }

    public Date getInitialTime() {
        return initialTime;
    }

    public void setInitialTime(Date initialTime) {
        this.initialTime = initialTime;
    }

    public ArrayList<Package> moveTrucks() {
        int BATCH_TIME = 12;
        long HOUR = 3600*1000;
        /**Number max of steps in a period of BATCH_TIME**/
        int max_Steps = 50*BATCH_TIME/60;
        ArrayList<Package> deliveredPackages = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        int _w = map.getWidth();
        Date initialTime = new Date(this.initialTime.getTime());
        Date endTime = new Date(this.initialTime.getTime() + (long)BATCH_TIME*60000);

        System.out.println("------------------------------------------------------------------------------------");

        for(Truck truck : trucks){
            int steps=0;
            double hoursTraveled = 0.0;

            while(steps<=max_Steps && truck.getPackages().size()>0){

                Date date = new Date(initialTime.getTime() + (long) (hoursTraveled * HOUR));
                if(date.getSeconds()%BATCH_TIME!=0) {
                    System.out.println("Float precision error!");
                    while(true);
                }
                boolean flag = true;

                do{
                    Node src = map.getVertax().get(truck.getY()*_w + truck.getX());
                    Node pack = map.getNode(truck.getPackages().get(0));
                    /**Check if the vehicle position matches the package**/

                    if(src.compare(pack)){
                        Package pck = truck.getPackages().remove(0);
                        boolean isOrder = pck.getIsOrder();

                        if(isOrder){
                            /**Set up truck that delivers package**/
                            pck.setTruck(truck);
                            pck.setCompleted(true);
                            pck.setDateCompleted(date);

                            deliveredPackages.add(pck);
                            Request ord = pck.getRequest();
                            ord.setCompletedGLP(ord.getCompletedGLP() + pck.getAmountGLP());
                            truck.setLpgMeter(truck.getLpgMeter() -  pck.getAmountGLP());
                            if(truck.getLpgMeter()<0){
                                System.out.println("Bad assignment!");
                                while(true);
                            }


                            /**Check if associated Order has been completed**/
                            Date limitDate = new Date(ord.getDateOrder().getTime() + ord.gethLimit()*HOUR);
                            System.out.println("Truck "+truck.getId()+" ("+truck.getLpgMeter()+"GLP)"+" delivered package "+pck.getId()
                                    +" ("+pck.getAmountGLP()+") | Order "+ord.getId()+" ("+ord.getCompletedGLP()+"/"
                                    +ord.getAmountGLP()+") ("+ ord.getX()+ "," +ord.getY() + ")- RD: "+dateFormat.format(date) +" LD: "+dateFormat.format(limitDate));
                            /**
                             if(date.getTime() > limitDate.getTime()){
                             System.out.println("Logistics collapse!");
                             while(true);
                             }
                             **/
                        }
                        else{
                            /**On Station **/
                            Station sta = pck.getStation();
                            if(!sta.getIsCentral()){
                                sta.setLpgMeter(sta.getLpgMeter() - (truck.getLpgCapacity() - truck.getLpgMeter()));
                            }
                            truck.setFuelMeter(truck.getFuelTankCapacity());
                            truck.setLpgMeter(truck.getLpgCapacity());
                            System.out.println("Truck "+truck.getId()+" ("+truck.getLpgMeter()+"GLP)"+" recharge GLP on Station ("+sta.getX()+","+sta.getY()+") - "+ dateFormat.format(date));
                        }
                    }else flag=false;

                }while(truck.getPackages().size()>0 && flag);

                steps+=1;

                /**If there are packages, then the path cannot be empty**/
                if(truck.getPackages().size()>0 && steps<=max_Steps){
                    if(truck.getRoute().size() == 0){
                        System.out.println("?");
                    }
                    Node node = truck.getRoute().remove(0);
                    /**Save last position od truck**/
                    truck.set_x(truck.getX());
                    truck.set_y(truck.getY());
                    truck.setX(node.getX());
                    truck.setY(node.getY());
                    /**precision to 2 decimal places**/
                    hoursTraveled = Math.round((hoursTraveled + 0.02)*100)/100.0;
                    truck.consumeFuel(1);

                }
            }

            System.out.println("Truck "+truck.getId()+" ("+truck.getLpgMeter()+"GLP)"+" is in ("+truck.getX()+","+truck.getY()+") at "+ dateFormat.format(endTime));
            System.out.println("------------------------------------------------------------------------------------");
        }

        /**Release undelivered orders for each vehicle**/
        for(Truck truck : trucks){

            Package nod = null;
            if(truck.getPackages().size()>0){
                nod = truck.getPackages().remove(0);
                if (!nod.getIsOrder()) nod = null;
            }

            while(truck.getPackages().size()>0){
                Package pck = truck.getPackages().remove(0);
                if(pck.getIsOrder()){
                    this.packs.add(pck);
                }
            }
            if(nod!=null) truck.getPackages().add(nod);

            while(truck.getRoute().size()>0){
                Node node = truck.getRoute().remove(0);
            }

        }

        return deliveredPackages;
    }

    public void solve(){

        Map<Package, Truck> list = new HashMap<Package,Truck>() ;

        if(getPacks().size()>0) {

            Engine<EnumGene<Integer>, Double> engine = Engine
                    .builder(this.ga)
                    .minimizing()
                    .offspringFraction(1.0)
                    .survivorsSelector(new RouletteWheelSelector<>())
                    .offspringSelector(new TournamentSelector<>())
                    .optimize(Optimize.MINIMUM)
                    .maximalPhenotypeAge(50)
                    .populationSize(500)
                    .alterers(
                            new SwapMutator<>(0.8),
                            new SinglePointCrossover<>(0.8))
                    .build();

            EvolutionStatistics<Double, ?> statistics = EvolutionStatistics.ofNumber();
            Phenotype<EnumGene<Integer>, Double> best = engine
                    .stream()
                    .limit(bySteadyFitness(50))
                    .limit(250)
                    .peek(statistics)
                    .collect(toBestPhenotype());

            System.out.println("best: " + best.fitness());


            /**Decoding the solution**/
            list = ga.codec().decode(best.genotype());
        }
        else{
            list = new HashMap<Package,Truck>();
        }
        packs.clear();



        this.ga.decoding(list,trucks);

        for(Truck truck: trucks){
            System.out.println("Camion "+truck.getId()+": "+ truck.getPackages());
        }


    }

}
