package com.example.out.dao;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.sql.Connection;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.model.Place;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.lifecycle.Startables;

@Testcontainers
public class PlaceDAOTest {


    @Container
    private static PostgreSQLContainer<?> container;

    private static PlaceDAO placeDAO;

    private static Connection connection;


    static {
        Configurations configs = new Configurations();
        try {
            Configuration config = configs.properties(new File("application.properties"));
            String containerVer = config.getString("container.version");
            container = new PostgreSQLContainer<>(containerVer);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @SuppressWarnings("deprecation")
    @BeforeAll 
    static void runConfiguration() {
        try {
            Startables.deepStart(Stream.of(container)).join();
            connection = container.createConnection("");
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update(new Contexts());
            liquibase.close();
            placeDAO = new PlaceDAO(connection);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }
    

    @Test
    @DisplayName("Добавление нового рабочего места")
    void testAddPlace() {
        try {
            String loginOwner = "user0002";
            placeDAO.addPlace(loginOwner);
            Map<Integer, Place> places = placeDAO.getPlaces();
            assertTrue(places.values().stream().anyMatch(place -> place.getLoginOwner().equals(loginOwner)),
                    "Новое рабочее место должно быть добавлено");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
    }

    @Test
    @DisplayName("Добавление нового конференц-зала")
    void testAddConferenceRoom() {
        try {
            String loginOwner = "user0003";
            int seats = 50;
            placeDAO.addConferenseRoom(loginOwner, seats);
            Map<Integer, Place> places = placeDAO.getPlacesConferenceRoom();
            assertTrue(places.values().stream().anyMatch(place -> place.getLoginOwner().equals(loginOwner) && place.getSeats() == seats),
                    "Новый конференц-зал должен быть добавлен");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
    }

    @Test
    @DisplayName("Получение места по идентификатору")
    void testGetPlace() {
        try {
            int id = 1; 
            Place place = placeDAO.getPlace(id);
            assertNotNull(place, "Место с данным идентификатором должно существовать");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
    }

    @Test
    @DisplayName("Удаление места по идентификатору")
    void testDeletePlace() {
        try {
            int id = 1; 
            assertTrue(placeDAO.exist(id), "Место до удаления должно существовать");
            placeDAO.deletePlace(id);
            assertFalse(placeDAO.exist(id), "Место после удаления не должно существовать");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка существования места по идентификатору")
    void testExist() {
        try {
            int id = 1; 
            assertTrue(placeDAO.exist(id), "Место с данным идентификатором должно существовать");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
    }

    @Test
    @DisplayName("Получение списка мест одного владельца")
    void testGetPlacesOneOwner() {
        try {
            String loginOwner = "user0001";
            Map<Integer, Place> places = placeDAO.getPlacesOneOwner(loginOwner);
            assertFalse(places.isEmpty(), "Список мест одного владельца не должен быть пустым");
            assertTrue(places.values().stream().allMatch(place -> place.getLoginOwner().equals(loginOwner)),
                    "Все места в списке должны принадлежать одному владельцу");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
    }

    @AfterAll
    static void down() {
        container.stop();
    } 
}
