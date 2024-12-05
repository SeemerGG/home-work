package com.example.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.example.domain.dto.UserDTO;
import com.example.domain.model.User;
import com.example.security.PasswordHashing;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {


    @Test
    void testModeltoToModel() throws Exception{
        UserDTO userDTO = new UserDTO("user0001", "user0001");

        UserMapper mapper = Mappers.getMapper(UserMapper.class);

        User user = mapper.DtoToModel(userDTO);

        assertEquals(PasswordHashing.getPasswordHash(userDTO.password()), user.getPassword());
    }
}
