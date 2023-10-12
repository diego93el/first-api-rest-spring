package com.practice.controller;

import com.practice.model.dto.ClienteDto;
import com.practice.model.entity.Cliente;
import com.practice.model.payload.MensajeResponse;
import com.practice.service.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**Esta anotacion se aplica a una clase para marcalar como controlador de solicitudes,
 * Spring restcontroller se usa para crear servicios web RESTful usando SPRING MAVEN*/
@RestController
/**Se usa para asignar solicitudes a controladores en epecifico y/o metodos, se puede usar,
 * tanto en controladores como en metodos*/
@RequestMapping("/api/v1")

public class ClienteController {
         @Autowired
         private IClienteService clienteService;
    @GetMapping("clientes")
    public ResponseEntity<?> showAll() {
       List<Cliente> getList = clienteService.listAll();
        if (getList==null){
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("No hay registros")
                            .objeto(null)
                            .build()
                    , HttpStatus.OK );
        }

        return new ResponseEntity<>(
                MensajeResponse.builder()
                        .mensaje("")
                        .objeto(getList)
                        .build()
                , HttpStatus.OK );
    }
         //Metodo de Spring para generar la url.
         @PostMapping("cliente")
         public ResponseEntity <?> create(@RequestBody ClienteDto clienteDto){
             Cliente clienteSave = null;
             try {
                 clienteSave = clienteService.save(clienteDto);
                 return new ResponseEntity<>( MensajeResponse.builder()
                         .mensaje("Guardado correctamente")
                         .objeto(ClienteDto.builder()
                                 .idCliente(clienteSave.getIdCliente())
                                 .nombre(clienteSave.getNombre())
                                 .apellido(clienteSave.getApellido())
                                 .correo(clienteSave.getCorreo())
                                 .fechaRegistro(clienteSave.getFechaRegistro())
                                 .build())
                         .build()
                         //Metodo para crear el HTTP code.
                         ,HttpStatus.CREATED);
             }catch (DataAccessException exDt){
                 return new ResponseEntity<>(
                         MensajeResponse.builder()
                                 .mensaje(exDt.getMessage())
                                 .objeto(null)
                                 .build()
                         , HttpStatus.METHOD_NOT_ALLOWED );
             }

         }

         @PutMapping("cliente/{id}")
         public ResponseEntity <?> update(@RequestBody ClienteDto clienteDto,@PathVariable Integer id){
             Cliente clienteUpdate = null;

             try {
                 //Algoritmo para actualizar el mismo id y que no cree uno nuevo
                 Cliente findCliente = clienteService.findById(id);
                 //existById se obtuvo de los servicios .
                 if (clienteService.existsById(id) ){
                     clienteDto.setIdCliente(id);
                     clienteUpdate = clienteService.save(clienteDto);
                     return new ResponseEntity<>( MensajeResponse.builder()
                             .mensaje("Guardado correctamente")
                             .objeto(ClienteDto.builder()
                                     .idCliente(clienteUpdate.getIdCliente())
                                     .nombre(clienteUpdate.getNombre())
                                     .apellido(clienteUpdate.getApellido())
                                     .correo(clienteUpdate.getCorreo())
                                     .fechaRegistro(clienteUpdate.getFechaRegistro())
                                     .build())
                             .build()
                             ,HttpStatus.CREATED);
                 }else {
                     return new ResponseEntity<>(
                             MensajeResponse.builder()
                                     .mensaje("El registro que intenta actualizar no se encuentra en la base de datos")
                                     .objeto(null)
                                     .build()
                             , HttpStatus.NOT_FOUND );
                 }

             }catch (DataAccessException exDt){
                 return new ResponseEntity<>(
                         MensajeResponse.builder()
                                 .mensaje(exDt.getMessage())
                                 .objeto(null)
                                 .build()
                         , HttpStatus.METHOD_NOT_ALLOWED );
             }

         }
         /**ResponseEntity maneja la respueta HTTP permitiendonos total libertad de configurar
          * la respuesta que queremos que se envie desde nuestros endpoint.
          * */
         @DeleteMapping("cliente/{id}")

        public ResponseEntity <?> delete(@PathVariable Integer id){
             Map<String,Object> response = new HashMap<>();
             /**Hacemos un try/catch para validar el response status
              * */
             try {
                 Cliente clienteDelete = clienteService.findById(id);
                 clienteService.delete(clienteDelete);
                 return  new ResponseEntity<>(clienteDelete, HttpStatus.NO_CONTENT);
             }catch (DataAccessException exDt){
                 response.put("mensaje", exDt.getMessage());
                 response.put("cliente", null);
                 return new ResponseEntity<>(
                         MensajeResponse.builder()
                                 .mensaje(exDt.getMessage())
                                 .objeto(null)
                                 .build()
                         , HttpStatus.INTERNAL_SERVER_ERROR );

             }

        }

        @GetMapping("cliente/{id}")
        public ResponseEntity<?> showById(@PathVariable Integer id){
            Cliente cliente = clienteService.findById(id);
            if (cliente==null){
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("El registro que intenta buscar no existe")
                                .objeto(null)
                                .build()
                        , HttpStatus.NOT_FOUND );
            }

            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("")
                            .objeto(ClienteDto.builder()
                                    .idCliente(cliente.getIdCliente())
                                    .nombre(cliente.getNombre())
                                    .apellido(cliente.getApellido())
                                    .correo(cliente.getCorreo())
                                    .fechaRegistro(cliente.getFechaRegistro())
                                    .build())
                            .build()
                    , HttpStatus.OK );
        }
}
