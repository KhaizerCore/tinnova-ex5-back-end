package com.api.cadastroveiculos.service;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Data
public class Validacao{
    public static boolean validaMarca(String marca){
        String marcas[] = {
                "Volkswagen","Chevrolet","Fiat","Hyundai","Toyota","Jeep","Caoa Chery","Renault","Nissan","Honda",
                "Peugeot","Ford","Citroen","Mitsubishi","Audi","BWM","Volvo","Mercedes-Benz","JAC Motors","Kia",
                "Land Rover","Suzuki","RAM","Porsche","Ferrari"
        };
        List<String> listMarcas = new ArrayList<>(Arrays.asList(marcas));
        return listMarcas.contains(marca);
    }
}
