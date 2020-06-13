package com.iex.stocktrading.model.dto.mapper;

import com.iex.stocktrading.model.User;
import com.iex.stocktrading.model.dto.NewUserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public interface NewUserMapper extends EntityMapper<NewUserDTO, User> {

    @Mapping(source = "account.balance", target = "balance")
    @Mapping(source = "account.no", target = "account")
    @Mapping(source = "fullname", target = "full_name")
    NewUserDTO toDto(User user);

    List<NewUserDTO> toDtoList(List<User> users);

    @Mapping(source = "full_name", target = "fullname")
    @Mapping(source = "account", target = "account.no")
    @Mapping(source = "balance", target = "account.balance")
    @Mapping(source = "full_name", target = "account.name")
    User toEntity(NewUserDTO userDTO);

}
