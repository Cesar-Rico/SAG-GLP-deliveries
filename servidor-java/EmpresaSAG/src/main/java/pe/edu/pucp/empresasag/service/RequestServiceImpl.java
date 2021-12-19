package pe.edu.pucp.empresasag.service;

import org.springframework.stereotype.Service;
import pe.edu.pucp.empresasag.model.Blocking;
import pe.edu.pucp.empresasag.model.Request;
import pe.edu.pucp.empresasag.repository.RequestRepository;

import java.util.ArrayList;
import java.util.Date;
@Service
public class RequestServiceImpl implements RequestService{
    RequestRepository requestRepository;

    RequestServiceImpl(RequestRepository requestRepository){
        this.requestRepository = requestRepository;
    }

    public ArrayList<Request> getOrdersByBatchTime(Date initialTime){

        Date start = new Date(initialTime.getTime() - 6*60*1000);
        Date end = new Date(initialTime.getTime());

        ArrayList<Request> orders = requestRepository.findByDateOrderBetween(start,end);

        ArrayList<Request> response =  new ArrayList<>();


        for(Request request : orders){
            if(request.getDateOrder().compareTo(end)<0 && request.getDateOrder().compareTo(start)>=0){
                response.add(request);
            }
        }
        return response;
    }
    @Override
    public ArrayList<Request> getOrdersByCompletedFalse(){
        ArrayList<Request> orders = requestRepository.findByCompletedFalse();
        return orders;
    }
}