package com.example.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.domain.dto.PlaceDTO;
import com.example.domain.model.Place;

@Mapper(componentModel = "spring")
public interface PlaceMapper {

    @Mapping(target = "placeType", expression = "java(place.getPlaceType().name())")
    PlaceDTO modelToDTO(Place place);

    @Mapping(target = "placeType", expression = "java(com.example.domain.model.PlaceType.valueOf(placeDTO.placeType()))")
    Place DtoToModel(PlaceDTO placeDTO);

    List<Place> toUserResponseList(List<Place> places);
}
