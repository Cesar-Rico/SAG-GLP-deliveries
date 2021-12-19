package pe.edu.pucp.empresasag.service;

import pe.edu.pucp.empresasag.model.Blocking;
import pe.edu.pucp.empresasag.model.CityMap;

import java.util.ArrayList;
import java.util.Date;

public interface BlockingService {
    ArrayList<Blocking> getBlockingsByBatchTime(Date initalTime, CityMap map);
    ArrayList<Blocking> getBlockingsByActualTime();
}