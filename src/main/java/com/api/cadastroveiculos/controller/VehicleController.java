package com.api.cadastroveiculos.controller;

import com.api.cadastroveiculos.model.Vehicle;
import com.api.cadastroveiculos.repository.VehicleRepository;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@ResponseBody
@RequestMapping("/veiculos")
public class VehicleController {
    @Autowired
    private VehicleRepository vehicleRepository;

    @GetMapping
    public ResponseEntity getAllVehicles(){
        return ResponseEntity.ok(vehicleRepository.findAll());
    }

    @GetMapping(params = {"marca", "ano", "cor"})
    public ResponseEntity getFilteredVehicles(@RequestParam String marca, @RequestParam int ano, @RequestParam String cor){
        if (vehicleRepository.findByMarcaAndAnoAndCor(marca, ano, cor).isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Veiculo nao encontrado");
        return ResponseEntity.status(HttpStatus.OK).body(vehicleRepository.findByMarcaAndAnoAndCor(marca, ano, cor));
    }

    @GetMapping(value = {"/{id}"})
    public ResponseEntity getVehicleDetails(@PathVariable("id") Long id){
        if (!vehicleRepository.findById(id).isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Veiculo nao encontrado");
        return ResponseEntity.status(HttpStatus.OK).body(vehicleRepository.findById(id));
    }

    @Transactional
    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping()
    public ResponseEntity addVehile(@RequestBody Vehicle vehicle){
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicleRepository.save(vehicle));
    }

    @Transactional
    @PutMapping(value = {"/{id}"})
    public ResponseEntity updateVehicleData(@PathVariable("id") Long id, @RequestBody @Valid Vehicle vehicle){

        Optional<Vehicle> vehicleModelOptional = vehicleRepository.findById(id);
        if (!vehicleModelOptional.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Veiculo nao encontrado");

        vehicle.setId(id);
        vehicle.setCreated(vehicleModelOptional.get().getCreated());

        return ResponseEntity.status(HttpStatus.OK).body(vehicleRepository.save(vehicle));
    }

    @Transactional
    @PatchMapping(value = {"/{id}"})
    public ResponseEntity updateSomeVehicleData(@PathVariable("id") Long id, @RequestBody @Valid Vehicle vehicle){
        Optional<Vehicle> vehicleModelOptional = vehicleRepository.findById(id);
        if (!vehicleModelOptional.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Veiculo nao encontrado");

        var vehicleModel = vehicleModelOptional.get();
        if (vehicle.getVeiculo() != null) vehicleModel.setVeiculo(vehicle.getVeiculo());
        if (vehicle.getMarca() != null) vehicleModel.setMarca(vehicle.getMarca());
        if (vehicle.getAno() != null) vehicleModel.setAno(vehicle.getAno());
        if (vehicle.getDescricao() != null) vehicleModel.setDescricao(vehicle.getDescricao());
        if (vehicle.getVendido() != null) vehicleModel.setVendido(vehicle.getVendido());
        if (vehicle.getCor() != null) vehicleModel.setCor(vehicle.getCor());

        return ResponseEntity.status(HttpStatus.OK).body(vehicleRepository.save(vehicleModel));
    }

    @Transactional
    @DeleteMapping(value = {"/{id}"})
    public ResponseEntity deleteVehicle(@PathVariable("id") Long id){
        if (!vehicleRepository.findById(id).isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Veiculo nao encontrado");
        vehicleRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Veiculo removido");
    }

//    @GetMapping(value = {"/qtd-nao-vendidos"})
//    public ResponseEntity getQtdNaoVendidos(){
//        return ResponseEntity.status(HttpStatus.OK).body(vehicleRepository.countByVendido(false));
//    }
//
//    @GetMapping(value = {"/qtd-por-marca"})
//    public ResponseEntity qtdPorMarca(String marca){
//        return ResponseEntity.status(HttpStatus.OK).body(vehicleRepository.countByMarca(marca));
//    }
}