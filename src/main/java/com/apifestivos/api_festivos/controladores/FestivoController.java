package com.apifestivos.api_festivos.controladores;

import java.time.DateTimeException;
import java.time.LocalDate; 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apifestivos.api_festivos.modelo.Festivo;
import com.apifestivos.api_festivos.servicios.FestivoService; 

@RestController
@RequestMapping("/festivos")
public class FestivoController {

    @Autowired
    private FestivoService festivoService;

    @GetMapping("/verificar/{anio}/{mes}/{dia}")
    public ResponseEntity<String> verificarFestivo(
                @PathVariable int anio,
                @PathVariable int mes,
                @PathVariable int dia) {

        LocalDate fecha;

        try {
            fecha = LocalDate.of(anio, mes, dia);
        } catch (DateTimeException e) { // Ahora capturamos directamente DateTimeException
            // Esto atrapará tanto DateTimeException (para fechas como Feb 30)
            // como DateTimeParseException (si el formato fuera distinto y fallara al parsear)
            return ResponseEntity.ok("Fecha No valida");
        } catch (Exception e) {
            // Este catch atrapará CUALQUIER otra excepción inesperada
            // (por ejemplo, si la base de datos no está disponible al iniciar FestivoService)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }

        Festivo festivoEncontrado = festivoService.esFestivo(fecha);

        if (festivoEncontrado != null) {
            return ResponseEntity.ok("Es Festivo");
        } else {
            return ResponseEntity.ok("No es festivo");
        }
    }

    @GetMapping("/todos/{anio}")
    public ResponseEntity<List<LocalDate>> obtenerFestivosPorAnio(@PathVariable int anio) {
        List<LocalDate> festivos = festivoService.obtenerFestivosPorAnio(anio);
        return ResponseEntity.ok(festivos);
    }
}