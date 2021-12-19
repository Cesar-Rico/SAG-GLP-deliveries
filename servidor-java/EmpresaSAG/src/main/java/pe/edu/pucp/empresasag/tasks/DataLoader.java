package pe.edu.pucp.empresasag.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pe.edu.pucp.empresasag.model.*;
import pe.edu.pucp.empresasag.model.Package;
import pe.edu.pucp.empresasag.repository.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DataLoader implements ApplicationRunner {

    private RequestRepository requestRepository;
    private StationRepository stationRepository;
    private PackageRepository packageRepository;
    private ParametersRepository parametersRepository;
    private TruckRepository truckRepository;

    @Autowired
    public DataLoader(RequestRepository requestRepository,StationRepository stationRepository,
                      PackageRepository packageRepository,ParametersRepository parametersRepository,
                      TruckRepository truckRepository) {
        this.requestRepository = requestRepository;
        this.stationRepository = stationRepository;
        this.packageRepository = packageRepository;
        this.parametersRepository = parametersRepository;
        this.truckRepository = truckRepository;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {

        Station s1 = stationRepository.save(new Station(1,12,8,true,0));
        Station s2 = stationRepository.save(new Station(2,42,42,false,1000));
        Station s3 = stationRepository.save(new Station(3,63,3,false,1000));

        Parameters p = parametersRepository.save(new Parameters(1,70,50,50,1));

        Truck t1 = truckRepository.save(new Truck(1,1,5));
        Truck t2 = truckRepository.save(new Truck(2,1,5));
        Truck t3 = truckRepository.save(new Truck(3,1,5));
        Truck t4 = truckRepository.save(new Truck(4,1,5));
        Truck t5 = truckRepository.save(new Truck(5,1,5));
        Truck t6 = truckRepository.save(new Truck(6,1,5));
        Truck t7 = truckRepository.save(new Truck(7,1,5));
        Truck t8 = truckRepository.save(new Truck(8,1,5));
        Truck t9 = truckRepository.save(new Truck(9,1,5));
        Truck t10 = truckRepository.save(new Truck(10,1,5));
        Truck t11 = truckRepository.save(new Truck(11,1,10));
        Truck t12 = truckRepository.save(new Truck(12,1,10));
        Truck t13 = truckRepository.save(new Truck(13,1,10));
        Truck t14 = truckRepository.save(new Truck(14,1,10));
        Truck t15 = truckRepository.save(new Truck(15,2,15));
        Truck t16 = truckRepository.save(new Truck(16,2,15));
        Truck t17 = truckRepository.save(new Truck(17,2,15));
        Truck t18 = truckRepository.save(new Truck(18,2,15));
        Truck t19 = truckRepository.save(new Truck(19,2,25));
        Truck t20 = truckRepository.save(new Truck(20,2,25));
//        Truck t1 = truckRepository.save(new Truck(1,1,10));
//        Truck t2 = truckRepository.save(new Truck(2,1,10));
//        Truck t3 = truckRepository.save(new Truck(3,2,15));
//        Truck t4 = truckRepository.save(new Truck(4,3,25));
//        Truck t5 = truckRepository.save(new Truck(5,3,25));

//        try{
//            Request r1 =  requestRepository.save(new Request(1,
//                    new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2021/12/08 14:12:00"),15,4,13,8));
//            Request r2 =  requestRepository.save(new Request(2,
//                    new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2021/12/08 14:17:14"),2,4,12,7));
//            Request r3 =  requestRepository.save(new Request(3,
//                    new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2021/12/08 14:18:22"),1,4,12,10));
//            Request r4 =  requestRepository.save(new Request(4,
//                    new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2021/12/08 14:20:23"),6,4,12,11));
//            Request r5 =  requestRepository.save(new Request(5,
//                    new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2021/12/08 14:23:30"),10,4,11,9));
//            Request r6 =  requestRepository.save(new Request(6,
//                    new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2021/12/08 14:23:59"),7,4,11,12));
//            Request r7 =  requestRepository.save(new Request(7,
//                    new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2021/12/08 14:23:59"),40,4,11,10));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        Package p1 = new Package(1,s1);
        Package p2 = new Package(2,s2);
        Package p3 = new Package(3,s3);
        packageRepository.save(p1);
        packageRepository.save(p2);
        packageRepository.save(p3);

    }
}