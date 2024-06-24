package com.example.model;

/**
 * Абстрактный класс, представляющий место в системе бронирования.
 * Этот класс служит базой для всех типов мест, которые могут быть забронированы.
 */
public abstract class Place {
    //Не обращаем внимание...
    private static int last_id = 0;
    private int id;
    private String loginOwner;
    
    /**
     * Конструктор для создания нового места.
     * @param loginOwner Логин владельца места.
     */
    public Place(String loginOwner) {
        this.id = ++last_id;
        this.loginOwner = loginOwner;
    }

    /**
     * Метод получения идентификатора места.
     * @return Возвращает уникальный идентификатор места.
     */
    public int getId() {
        return id;
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
