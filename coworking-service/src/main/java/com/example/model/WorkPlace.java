package com.example.model;

/**
 * Класс рабочего места, представляющий собой конкретное рабочее место в системе.
 * Наследуется от класса Place и содержит специфические характеристики и методы для рабочего места.
 */
public class WorkPlace extends Place{

    /**
     * Конструктор для создания объекта рабочего места.
     * @param loginOwner Логин владельца рабочего места.
     */
    public WorkPlace(String loginOwner) {
        super(loginOwner);
        //Для будущих модификаций
    }

}
