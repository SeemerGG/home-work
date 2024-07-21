// package com.example.controller;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.setup.MockMvcBuilders;

// import com.example.domain.dto.UserDTO;
// import com.example.domain.model.User;
// import com.example.mapper.UserMapper;
// import com.example.service.AuthentificationService;

// @ExtendWith(MockitoExtension.class)
// public class AuthenticationControllerTest {

//     private MockMvc mockMvc;

//     @Mock 
//     private AuthentificationService service;
    
//     @Mock
//     private UserMapper mapper;

//     @InjectMocks
//     private AuthenticationController controller;

//     @BeforeEach
//     public void setUp() {
//         mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
//     }

//     @Test
//     @DisplayName("Проверка авторизации при корректных данных.")
//     void testAuthentification_ValidToken() throws Exception {

//         User user = new User("user0001", "36e5eebcf7aee8f72bc168e0f8ae6eed00d60446e9f7c03989ddea84ac7c8711");
//         String token = "token";
//         when(mapper.DtoToModel(any(UserDTO.class))).thenReturn(user);
//         when(service.authorization(user)).thenReturn(token);
//         mockMvc.perform(get("/auth")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"login\":\"user0001\",\"password\":\"user0001\"}"))
//                 .andExpect(status().isOk())
//                 .andExpect(header().string("Authorization", "Bearer " + token));
//     }

//     @Test
//     @DisplayName("Проверка регистрации при корректных данных")
//     void tesRegistration_ValidToken() throws Exception {

//         User user = new User("user0001", "36e5eebcf7aee8f72bc168e0f8ae6eed00d60446e9f7c03989ddea84ac7c8711");
//         String token = "token";
//         when(mapper.DtoToModel(any(UserDTO.class))).thenReturn(user);
//         when(service.registration(user)).thenReturn(token);
//         mockMvc.perform(post("/auth")
//                 .content("{\"login\":\"user0001\",\"password\":\"user0001\"}"))
//                 .andExpect(status().isOk())
//                 .andExpect(header().string("Authorization", "Bearer " + token));
//     }
// }
