package com.example.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс, представляющий место в системе бронирования.
 * Этот класс служит базой для всех типов мест, которые могут быть забронированы.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Place {
    private int id;
    private String loginOwner;
    private PlaceType placeType;
    private int seats;

    /**
     * Представление информации о месте в текстовом формате.
     * @return Возвращает строковое представление места, включая ID и логин владельца.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ");
        sb.append(id);
        sb.append(" Логин собственника: ");
        sb.append(loginOwner);
        sb.append(" Тип места: ");
        if(placeType == PlaceType.CONFERENCEROOM) {
            sb.append("Конференц зал Количество мест: ");
            sb.append(seats);
        }
        else {
            sb.append("Рабочие место");
        }
        return sb.toString();
    }
}
