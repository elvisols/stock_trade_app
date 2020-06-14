package com.iex.stocktrading.model.dto.mapper;

import com.iex.stocktrading.model.UserStock;
import com.iex.stocktrading.model.dto.UserStockDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public interface UserStockMapper extends EntityMapper<UserStockDTO, UserStock> {

    @Mapping(source = "currentPrice", target = "current_price")
    @Mapping(source = "user.account.no", target = "user.account")
    @Mapping(source = "user.account.balance", target = "user.balance")
    UserStockDTO toDto(UserStock userStock);

    List<UserStockDTO> toDtoList(List<UserStock> userStocks);

    @Mapping(source = "current_price", target = "currentPrice")
    @Mapping(source = "user.account", target = "user.account.no")
    @Mapping(source = "user.balance", target = "user.account.balance")
    UserStock toEntity(UserStockDTO userStockDTO);

}
