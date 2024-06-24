package com.example.model;

/**
 * Класс, представляющий конференц-зал.
 * Наследует класс Place и добавляет количество мест в зале.
 */
public class ConferenceRoom extends Place{

    private int seats;
    
     /**
     * Конструктор для создания экземпляра конференц-зала.
     * @param loginOwner Логин владельца зала.
     * @param seats Количество мест в зале.
     */
    public ConferenceRoom(String loginOwner, int seats) {
        super(loginOwner);
        this.seats = seats;
    }
    
    /**
     * Возвращает строковое представление объекта конференц-зала.
     * @return Строковое представление, содержащее информацию о владельце и количестве мест.
     */
    @Override
    public String toString() {
        return super.toString()+" Количество мест: "+seats;
    }

    /**
     * Получает количество мест в конференц-зале.
     * @return Количество мест.
     */
    public int getSeats() {
        return seats;
    }

    /**
     * Устанавливает количество мест в конференц-зале.
     * @param seats Новое количество мест в зале.
     */
    public void setSeats(int seats) {
        this.seats = seats;
    }
}
