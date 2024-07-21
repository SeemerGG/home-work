package com.example.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.example.domain.dto.ReservationDTO;
import com.example.domain.model.Reservation;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReservationMapper {

    Reservation dtoToModel(ReservationDTO reservationDto);

    ReservationDTO modelToDto(Reservation reservation);
}
