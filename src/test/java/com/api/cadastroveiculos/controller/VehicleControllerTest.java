package com.api.cadastroveiculos.controller;

import com.api.cadastroveiculos.model.Vehicle;
import com.api.cadastroveiculos.repository.VehicleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class VehicleControllerTest {

    private static int vehicleSequentialIDs = 0;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    MockMvc mockMvc;

    static String convertVehicleAsPayload(Vehicle vehicle) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(vehicle);
    }

    static Vehicle generateTestVehicle(){
        Vehicle vehicle = new Vehicle();
        vehicle.setVeiculo("Gol G5");
        vehicle.setMarca("Volkswagen");
        vehicle.setAno(2015);
        vehicle.setDescricao("Veículo 1.6 automático");
        vehicle.setVendido(false);
        vehicle.setCor("Prata");
        return vehicle;
    }

    static Vehicle generateRandomTestVehicle(){
        Vehicle vehicle = new Vehicle();
        vehicle.setVeiculo("veiculo "+String.valueOf((int) Math.random()));
        vehicle.setMarca("Fiat");
        vehicle.setAno((int) Math.random());
        vehicle.setDescricao("");
        vehicle.setVendido( (((int)Math.random()) % 2) != 0);
        vehicle.setCor("cor "+String.valueOf((int) Math.random()));
        return vehicle;
    }

    void insertTestVehicle(Vehicle vehicle){
        vehicleRepository.save(vehicle);
    }

    @Test
    void getAllVehicles() throws Exception{
        String testURL = "/veiculos";

        mockMvc.perform(
                MockMvcRequestBuilders.get(testURL)
        ).andExpect(
                status().isOk()
        );
    }

    @Test
    void getFilteredVehicles() throws Exception{

        Vehicle vehicle = VehicleControllerTest.generateRandomTestVehicle();

        String testURL = "/veiculos?marca="+vehicle.getMarca()+"&ano="+vehicle.getAno()+"&cor="+vehicle.getCor();

        mockMvc.perform(
                MockMvcRequestBuilders.get(testURL)
        ).andExpect(
                status().isNotFound()
        );

        this.insertTestVehicle(vehicle);
        VehicleControllerTest.vehicleSequentialIDs += 1;

        mockMvc.perform(
                MockMvcRequestBuilders.get(testURL)
        ).andExpect(
                status().isOk()
        );
    }

    @Test
    void getVehicleDetails() throws Exception {
        String testURL = "/veiculos/" + String.valueOf(VehicleControllerTest.vehicleSequentialIDs + 1);

        Vehicle vehicle = VehicleControllerTest.generateRandomTestVehicle();

        mockMvc.perform(
                MockMvcRequestBuilders.get(testURL)
        ).andExpect(
                status().isNotFound()
        );

        this.insertTestVehicle(vehicle);
        VehicleControllerTest.vehicleSequentialIDs += 1;

        mockMvc.perform(
                MockMvcRequestBuilders.get(testURL)
        ).andExpect(
                status().isOk()
        );
    }

    @Test
    void addVehile() throws Exception {
        String testURL = "/veiculos";

        Vehicle vehicleMarcaInvalida = VehicleControllerTest.generateRandomTestVehicle();
        vehicleMarcaInvalida.setMarca("marcaInvalida");

        mockMvc.perform(
                MockMvcRequestBuilders.post(testURL).contentType(APPLICATION_JSON)
                        .content(VehicleControllerTest.convertVehicleAsPayload(vehicleMarcaInvalida))
        ).andExpect(
                status().isBadRequest()
        );

        Vehicle vehicle = VehicleControllerTest.generateRandomTestVehicle();
        String payload = VehicleControllerTest.convertVehicleAsPayload(vehicle);

        mockMvc.perform(
                MockMvcRequestBuilders.post(testURL).contentType(APPLICATION_JSON).content(payload)
        ).andExpect(
                status().isCreated()
        );

        VehicleControllerTest.vehicleSequentialIDs += 1;
    }

    @Test
    void updateVehicleData() throws Exception {
        String testURL = "/veiculos/" + String.valueOf(VehicleControllerTest.vehicleSequentialIDs + 1);

        Vehicle vehicle = VehicleControllerTest.generateTestVehicle();
        String payload = VehicleControllerTest.convertVehicleAsPayload(vehicle);

        mockMvc.perform(
                MockMvcRequestBuilders.put(testURL).contentType(APPLICATION_JSON).content(payload)
        ).andExpect(
                status().isNotFound()
        );

        this.insertTestVehicle(vehicle);
        VehicleControllerTest.vehicleSequentialIDs += 1;


        Vehicle vehicle2 = new Vehicle();
        vehicle2.setVeiculo("Palio");
        vehicle2.setMarca("marcaInvalida");
        vehicle2.setAno(2010);
        vehicle2.setDescricao("Veiculo 1.0 Manual");
        vehicle2.setVendido(true);
        vehicle2.setCor("Prata");


        mockMvc.perform(
                MockMvcRequestBuilders.put(testURL).contentType(APPLICATION_JSON)
                        .content(VehicleControllerTest.convertVehicleAsPayload(vehicle2))
        ).andExpect(
                status().isBadRequest()
        );

        vehicle2.setMarca("Fiat");

        mockMvc.perform(
                MockMvcRequestBuilders.put(testURL).contentType(APPLICATION_JSON)
                        .content(VehicleControllerTest.convertVehicleAsPayload(vehicle2))
        ).andExpect(
                status().isOk()
        );
    }

    @Test
    void updateSomeVehicleData() throws Exception {
        String testURL = "/veiculos/" + String.valueOf(VehicleControllerTest.vehicleSequentialIDs + 1);

        Vehicle originalVehicle = VehicleControllerTest.generateTestVehicle();
        Vehicle modifiedVehicle = VehicleControllerTest.generateTestVehicle();
        modifiedVehicle.setVendido(!modifiedVehicle.getVendido());

        mockMvc.perform(
                MockMvcRequestBuilders.patch(testURL).contentType(APPLICATION_JSON)
                        .content(VehicleControllerTest.convertVehicleAsPayload(modifiedVehicle))
        ).andExpect(
                status().isNotFound()
        );

        this.insertTestVehicle(originalVehicle);
        VehicleControllerTest.vehicleSequentialIDs += 1;

        Vehicle vehicleMarcaInvalida = VehicleControllerTest.generateRandomTestVehicle();
        vehicleMarcaInvalida.setMarca("marcaInvalida");
        mockMvc.perform(
                MockMvcRequestBuilders.patch(testURL).contentType(APPLICATION_JSON)
                        .content(VehicleControllerTest.convertVehicleAsPayload(vehicleMarcaInvalida))
        ).andExpect(
                status().isBadRequest()
        );

        mockMvc.perform(
                MockMvcRequestBuilders.patch(testURL).contentType(APPLICATION_JSON)
                        .content(VehicleControllerTest.convertVehicleAsPayload(modifiedVehicle))
        ).andExpect(
                status().isOk()
        );
    }

    @Test
    void deleteVehicle() throws Exception{
        String testURL = "/veiculos/" + String.valueOf(VehicleControllerTest.vehicleSequentialIDs + 1);

        mockMvc.perform(
                MockMvcRequestBuilders.delete(testURL)
        ).andExpect(
                status().isNotFound()
        );

        Vehicle vehicle = VehicleControllerTest.generateTestVehicle();
        this.insertTestVehicle(vehicle);
        VehicleControllerTest.vehicleSequentialIDs += 1;

        mockMvc.perform(
                MockMvcRequestBuilders.delete(testURL)
        ).andExpect(
                status().isOk()
        );

        mockMvc.perform(
                MockMvcRequestBuilders.get(testURL)
        ).andExpect(
                status().isNotFound()
        );


    }
}