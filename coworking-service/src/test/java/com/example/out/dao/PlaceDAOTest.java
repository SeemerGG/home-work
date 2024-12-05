// package com.example.out.dao;

// import static org.junit.jupiter.api.Assertions.assertFalse;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertTrue;

// import java.sql.Connection;
// import java.util.Map;
// import java.util.stream.Stream;

// import org.junit.jupiter.api.AfterAll;
// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;

// import com.example.domain.model.Place;
// import com.example.domain.model.PlaceType;

// import org.testcontainers.containers.PostgreSQLContainer;
// import org.testcontainers.junit.jupiter.Container;
// import org.testcontainers.junit.jupiter.Testcontainers;
// import org.testcontainers.lifecycle.Startables;

// @Testcontainers
// public class PlaceDAOTest {


//     @Container
//     private static PostgreSQLContainer<?> container;

//     private static PlaceDAO placeDAO;

//     private static Connection connection;


//     static {
//         try {
//             String containerVer = ConfigurationProperties.properties.getProperty("container.version");
//             container = new PostgreSQLContainer<>(containerVer);
//         } catch (Exception e) {
//             System.out.println(e.getMessage());
//         }
//     }

//     @BeforeAll 
//     static void runConfiguration() throws Exception{
        
//         Startables.deepStart(Stream.of(container)).join();
//         connection = container.createConnection("");
//         MigrationConfig.performingMigration(connection);
//         placeDAO = new PlaceDAO(connection);
//     }
    

//     @Test
//     @DisplayName("Добавление нового рабочего места")
//     void testAddPlace() throws Exception {

//         String loginOwner = "user0002";
//         placeDAO.add(loginOwner, 1, PlaceType.WORKPLACE);
//         Map<Integer, Place> places = placeDAO.getPlaces();
//         assertTrue(places.values().stream().anyMatch(place -> place.getLoginOwner().equals(loginOwner)),
//                 "Новое рабочее место должно быть добавлено");
//     }

//     @Test
//     @DisplayName("Добавление нового конференц-зала")
//     void testAddConferenceRoom() throws Exception{

//             String loginOwner = "user0003";
//             int seats = 50;
//             placeDAO.add(loginOwner, seats, PlaceType.CONFERENCEROOM);
//             Map<Integer, Place> places = placeDAO.getForPlaceType(PlaceType.CONFERENCEROOM);
//             assertTrue(places.values().stream().anyMatch(place -> place.getLoginOwner().equals(loginOwner) && place.getSeats() == seats),
//                     "Новый конференц-зал должен быть добавлен");
//     }

//     @Test
//     @DisplayName("Получение места по идентификатору")
//     void testGetPlace() throws Exception{

//         int id = 1; 
//         Place place = placeDAO.getPlace(id);
//         assertNotNull(place, "Место с данным идентификатором должно существовать"); 
//     }

//     @Test
//     @DisplayName("Проверка существования места по идентификатору")
//     void testExist() throws Exception{

//         int id = 1; 
//         assertTrue(placeDAO.exist(id), "Место с данным идентификатором должно существовать");
//     }

//     @Test
//     @DisplayName("Удаление места по идентификатору")
//     void testDeletePlace() throws Exception{

//         int id = 1; 
//         assertTrue(placeDAO.exist(id), "Место до удаления должно существовать");
//         placeDAO.deletePlace(id);
//         System.out.println(placeDAO.getPlaces());
//         System.out.println(placeDAO.exist(id));
//         assertFalse(placeDAO.exist(id), "Место после удаления не должно существовать");
//     }

//     @Test
//     @DisplayName("Получение списка мест одного владельца")
//     void testGetPlacesOneOwner() throws Exception {

//         String loginOwner = "user0001";
//         Map<Integer, Place> places = placeDAO.getPlacesOneOwner(loginOwner);
//         assertFalse(places.isEmpty(), "Список мест одного владельца не должен быть пустым");
//         assertTrue(places.values().stream().allMatch(place -> place.getLoginOwner().equals(loginOwner)),
//                 "Все места в списке должны принадлежать одному владельцу");
//     }

//     @AfterAll
//     static void down() {
        
//         MigrationConfig.closeMigration();
//         container.stop();
//     } 
// }
