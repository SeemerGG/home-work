package com.example.service.imp;

import java.sql.SQLException;
import java.util.Collection;

import com.example.annotation.Loggable;
import com.example.domain.model.Place;
import com.example.out.dao.PlaceDAO;
import com.example.service.PlaceService;

@Loggable
/**
 * Класс реализующий логику взаимодействия с местами.
 */
public class PlaceServiceImp implements PlaceService {

    private final PlaceDAO placeDAO;
    
    public PlaceServiceImp(PlaceDAO placeDAO) {

        this.placeDAO = placeDAO;
    }

    @Override
    public Collection<Place> myPublication(String login) throws SQLException {

        return placeDAO.getPlacesOneOwner(login).values();
    }

    @Override
    public void deleteMyPlace(int id, String login) throws Exception {

        if(!placeDAO.getPlace(id).getLoginOwner().equals(login)) {
            throw new Exception("Недостаточно прав.");
        }
        placeDAO.deletePlace(id);
    }

    @Override
    public void createMyPlace(Place place) throws SQLException {

        placeDAO.add(place.getLoginOwner(), place.getSeats(), place.getPlaceType());
    }

}

