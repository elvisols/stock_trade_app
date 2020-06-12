package com.iex.stocktrading.model.dto.mapper;

import com.iex.stocktrading.model.User;
import com.iex.stocktrading.model.dto.NewUserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public interface NewUserMapper extends EntityMapper<NewUserDTO, User> {

//    @Mapping(source = "hobbies", target = "hobby")
//    @Mapping(source = "email", target = "username")
//    @Mapping(source = "color", target = "favourite_color")
    NewUserDTO toDto(User user);

    List<NewUserDTO> toDtoList(List<User> users);

//    @Mapping(source = "favourite_color", target = "color")
//    @Mapping(source = "username", target = "email")
//    @Mapping(source = "hobby", target = "hobbies")
    User toEntity(NewUserDTO userDTO);

}
