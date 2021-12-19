package pe.edu.pucp.empresasag.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.empresasag.model.Parameters;
import pe.edu.pucp.empresasag.repository.ParametersRepository;

import java.util.ArrayList;


@Service
public class ParametersServiceImpl implements ParametersService{

    ParametersRepository parametersRepository;

    public ParametersServiceImpl(ParametersRepository parametersRepository){
        this.parametersRepository = parametersRepository;
    }

    @Override
    public Parameters getParameters() {
        ArrayList<Parameters> list = (ArrayList<Parameters>) parametersRepository.findAll();
        if(list.size()==1){
            return list.get(0);
        }else return null;
    }


}
