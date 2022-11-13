package com.api.cadastroveiculos.repository;

import com.api.cadastroveiculos.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
//    @Query("SELECT * from vehicle as v WHERE v.marca = ?1 AND v.ano = ?2 AND v.cor = ?3")
    public List<Vehicle> findByMarcaAndAnoAndCor(String marca, int ano, String cor);

//    @Query("SELECT * FROM Vehicle as v WHERE v.vendido")
    public Long countByVendido(Boolean vendido);

    public Long countByMarca(String marca);
}
