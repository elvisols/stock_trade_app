package com.iex.stocktrading.model.dto.mapper;

import com.iex.stocktrading.model.User;
import com.iex.stocktrading.model.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring", uses = {})
public interface UserMapper extends EntityMapper<UserDTO, User> {

//    @Mapping(source = "hobbies", target = "hobby")
//    @Mapping(source = "color", target = "favourite_color")
    UserDTO toDto(User user);

    List<UserDTO> toDtoList(List<User> users);

//    @Mapping(source = "favourite_color", target = "color")
//    @Mapping(source = "hobby", target = "hobbies")
    User toEntity(UserDTO userDTO);

}
