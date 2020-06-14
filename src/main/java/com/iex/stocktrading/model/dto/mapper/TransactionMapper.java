package com.iex.stocktrading.model.dto.mapper;

import com.iex.stocktrading.model.Transaction;
import com.iex.stocktrading.model.dto.TransactionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring", uses = {})
public interface TransactionMapper extends EntityMapper<TransactionDTO, Transaction> {

    @Mapping(source = "user.fullname", target = "user")
    @Mapping(source = "stock.name", target = "stock")
    TransactionDTO toDto(Transaction transaction);

    List<TransactionDTO> toDtoList(List<Transaction> transactions);

    @Mapping(source = "user", target = "user.fullname")
    @Mapping(source = "stock", target = "stock.name")
    Transaction toEntity(TransactionDTO transactionDTO);

}
