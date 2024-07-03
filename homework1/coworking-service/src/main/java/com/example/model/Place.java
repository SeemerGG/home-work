package com.example.model;

/**
 * Абстрактный класс, представляющий место в системе бронирования.
 * Этот класс служит базой для всех типов мест, которые могут быть забронированы.
 */
public class Place {
    private int id;
    private String loginOwner;
    private PlaceType placeType;
    private int seats;
    
    /**
     * Конструктор для создания нового места.
     * @param id Идентификатор места.
     * @param loginOwner Логин владельца места.
     * @param placeType Тип места. (CONFERENCEROOM, WORKPLACE)
     * @param seats Количество мест.
     */
    public Place(int id, String loginOwner, String placeType, int seats) {
        this.id = id;
        this.loginOwner = loginOwner;
        this.seats = seats;
        this.placeType = PlaceType.valueOf(placeType);
    }

    /**
     * Альтернативный конструктор для создания нового рабочего места.
     * @param id Идентификатор места.
     * @param loginOwner Логин владельца места.
     */
    public Place(int id, String loginOwner) {
        this.id = id;
        this.loginOwner = loginOwner;
        this.placeType = PlaceType.WORKPLACE;
        this.seats = 1;
    }

    /**
     * Альтернативный конструктор для создания нового места (конференц зала).
     * @param id Идентификатор места.
     * @param loginOwner Логин владельца места.
     * @param seats Количество мест
     */
    public Place(int id, String loginOwner, int seats) {
        this(id, loginOwner);
        this.placeType = PlaceType.CONFERENCEROOM;
        this.seats = seats;
    }

    /**
     * Метод получения идентификатора места.
     * @return Возвращает уникальный идентификатор места.
     */
    public int getId() {
        return id;
    }


    /**
     * Метод получения типа места.
     * @return Возвращает тип места.
     */
    public PlaceType getPlaceType() {
        return placeType;
    }

    /**
     * Метод получения количества мест.
     * @return Возвращает количество мест.
     */
    public int getSeats() {
        return seats;
    }

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

    /**
     * Метод получения логина владельца места.
     * @return Возвращает логин владельца места.
     */
    public String getLoginOwner() {
        return loginOwner;
    }

}
