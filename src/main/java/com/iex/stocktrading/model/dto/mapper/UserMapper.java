package com.iex.stocktrading.model.dto.mapper;

import com.iex.stocktrading.model.User;
import com.iex.stocktrading.model.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring", uses = {})
public interface UserMapper extends EntityMapper<UserDTO, User> {

    @Mapping(source = "account.balance", target = "balance")
    @Mapping(source = "account.no", target = "account")
    @Mapping(source = "fullname", target = "full_name")
    UserDTO toDto(User user);

    List<UserDTO> toDtoList(List<User> users);

    @Mapping(source = "full_name", target = "fullname")
    @Mapping(source = "account", target = "account.no")
    @Mapping(source = "balance", target = "account.balance")
    User toEntity(UserDTO userDTO);

}
