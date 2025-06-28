package com.apifestivos.api_festivos.infraestructura.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apifestivos.api_festivos.modelo.Festivo;

@Repository
public interface IFestivoRepositorio extends JpaRepository<Festivo, Long> {
    
}