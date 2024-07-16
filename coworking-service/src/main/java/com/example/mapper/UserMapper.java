package com.example.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import com.example.domain.dto.UserDTO;
import com.example.domain.model.User;
import com.example.security.PasswordHashing;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "login", source = "userDTO.login")
    @Mapping(target = "password", expression = "java(getPasswordHash(userDTO.password()))")
    User DtoToModel(UserDTO userDTO);

    @Named("getPasswordHash")
    default String getPasswordHash(String password) {
        try {
            return PasswordHashing.getPasswordHash(password);
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException("Ошибка при хешировании пароля", e);
        }
    }
}
