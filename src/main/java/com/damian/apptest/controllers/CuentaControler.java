package com.damian.apptest.controllers;

import com.damian.apptest.entidad.Cuenta;
import com.damian.apptest.entidad.TransaccionDto;
import com.damian.apptest.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import  static org.springframework.http.HttpStatus.*;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaControler {
    @Autowired
    private CuentaService cuentaService;
    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public Cuenta detalle(@PathVariable(name = "id") Long id){
        return cuentaService.findById(id);
    }
    @PostMapping("/transferir")
    public ResponseEntity<?>transferir(@RequestBody TransaccionDto dto){
        cuentaService.tranferir(dto.getCuentaOrigenId(),dto.getCuentaDestinoId(),
                dto.getMonto(),dto.getBancoId());
        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status","ok");
        response.put("mensaje","transferencia realizada con exito");
        response.put("transaccion", dto);
        return ResponseEntity.ok().body(response);

    }
}
