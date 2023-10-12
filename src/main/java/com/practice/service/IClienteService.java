package com.practice.service;

import com.practice.model.dto.ClienteDto;
import com.practice.model.entity.Cliente;

import java.util.List;

public interface IClienteService {

    List<Cliente> listAll();

    Cliente save(ClienteDto cliente);

    Cliente findById(Integer id);


    void delete(Cliente cliente);

    boolean existsById(Integer id);

}
