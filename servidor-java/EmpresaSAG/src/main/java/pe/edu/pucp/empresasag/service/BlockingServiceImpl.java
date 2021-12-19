package pe.edu.pucp.empresasag.service;

import org.springframework.stereotype.Service;
import pe.edu.pucp.empresasag.auxiliar.Reader;
import pe.edu.pucp.empresasag.model.Blocking;
import pe.edu.pucp.empresasag.model.CityMap;
import pe.edu.pucp.empresasag.model.Parameters;
import pe.edu.pucp.empresasag.repository.BlockingRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
@Service
public class BlockingServiceImpl implements BlockingService{
    BlockingRepository blockingRepository;
    ParametersService parametersService;
    private static final long HOUR = 3600*1000;

    BlockingServiceImpl(BlockingRepository blockingRepository, ParametersService parametersService){
        this.blockingRepository = blockingRepository;
        this.parametersService = parametersService;
    }

    @Override
    public ArrayList<Blocking> getBlockingsByBatchTime(Date initialTime, CityMap map) {

        Date limitTime = new Date(initialTime.getTime() + 48*HOUR);

        LocalDate localDate = initialTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int start = localDate.getYear()*10000 + localDate.getMonthValue()*100 + localDate.getDayOfMonth();
        localDate = limitTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int end = localDate.getYear()*10000 + localDate.getMonthValue()*100 + localDate.getDayOfMonth();

        ArrayList<Blocking> blockings = blockingRepository.findByNumberDateBetween(start,end);

        ArrayList<Blocking> response = new ArrayList<>();

        for(Blocking blocking : blockings){
            if(blocking.getEnd().after(initialTime) && blocking.getStart().before(limitTime)){
                blocking.buildNodes(map);
                response.add(blocking);
            }
        }

        return response;
    }

    @Override
    public ArrayList<Blocking> getBlockingsByActualTime() {

        Date currentTime = new Date();

        LocalDate localDate = currentTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        int start = localDate.getYear()*10000 + localDate.getMonthValue()*100 + localDate.getDayOfMonth();

        ArrayList<Blocking> blockings = blockingRepository.findByNumberDate(start);

        ArrayList<Blocking> response = new ArrayList<>();

        Parameters parameters = parametersService.getParameters();

        if(parameters!=null) {
            CityMap map = new CityMap(parameters.getWidth(), parameters.getHeight());

            for(Blocking blocking : blockings){
                blocking.buildNodes(map);
                response.add(blocking);

            }
        }



        return response;
    }
}