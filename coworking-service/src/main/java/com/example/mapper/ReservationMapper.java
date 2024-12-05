package com.example.mapper;

import org.mapstruct.Mapper;

import com.example.domain.dto.ReservationDTO;
import com.example.domain.model.Reservation;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    Reservation dtoToModel(ReservationDTO reservationDto);

    ReservationDTO modelToDto(Reservation reservation);
}
