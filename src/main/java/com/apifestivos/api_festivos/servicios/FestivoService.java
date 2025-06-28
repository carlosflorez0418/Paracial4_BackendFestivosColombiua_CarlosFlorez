package com.apifestivos.api_festivos.servicios;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apifestivos.api_festivos.infraestructura.repositorios.IFestivoRepositorio;
import com.apifestivos.api_festivos.modelo.Festivo;

@Service
public class FestivoService {

    @Autowired
    private IFestivoRepositorio festivoRepositorio;

    public Festivo esFestivo(LocalDate fechaConsulta) {
        int anio = fechaConsulta.getYear();
        LocalDate pascua = calcularPascua(anio);
        List<Festivo> festivos = festivoRepositorio.findAll();

        return festivos.stream().filter(f -> {
            try {
                LocalDate fechaFestiva;

                if (f.getDia() != null && f.getMes() != null && (f.getDiasPascua() == null || f.getDiasPascua() == 0)) {
                    fechaFestiva = LocalDate.of(anio, f.getMes(), f.getDia());
                } else if (f.getDiasPascua() != null && f.getDiasPascua() != 0) {
                    fechaFestiva = pascua.plusDays(f.getDiasPascua());
                } else {
                    return false;
                }

                if (f.getTipo() != null && (f.getTipo().getId() == 2 || f.getTipo().getId() == 4)) {
                    fechaFestiva = trasladarAlLunes(fechaFestiva);
                }

                return fechaConsulta.equals(fechaFestiva);

            } catch (Exception e) {
                System.err.println("Error procesando festivo: " + f.getNombre());
                e.printStackTrace();
                return false;
            }
        }).findFirst().orElse(null);
    }

    public List<LocalDate> obtenerFestivosPorAnio(int anio) {
        List<LocalDate> festivosAnuales = new ArrayList<>();
        LocalDate pascua = calcularPascua(anio);
        List<Festivo> festivosBase = festivoRepositorio.findAll();

        for (Festivo f : festivosBase) {
            try {
                LocalDate fechaFestiva = null;

                if (f.getDia() != null && f.getMes() != null && (f.getDiasPascua() == null || f.getDiasPascua() == 0)) {
                    fechaFestiva = LocalDate.of(anio, f.getMes(), f.getDia());
                } else if (f.getDiasPascua() != null && f.getDiasPascua() != 0) {
                    fechaFestiva = pascua.plusDays(f.getDiasPascua());
                }

                if (fechaFestiva != null) {
                    if (f.getTipo() != null && (f.getTipo().getId() == 2 || f.getTipo().getId() == 4)) {
                        fechaFestiva = trasladarAlLunes(fechaFestiva);
                    }
                    festivosAnuales.add(fechaFestiva);
                }

            } catch (Exception e) {
                System.err.println("Error calculando festivo " + f.getNombre() + " para el año " + anio + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
        return festivosAnuales;
    }

    private LocalDate trasladarAlLunes(LocalDate fecha) {
        return fecha.getDayOfWeek() == DayOfWeek.MONDAY
            ? fecha
            : fecha.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
    }

    private LocalDate calcularPascua(int anio) {
        int a = anio % 19;
        int b = anio / 100;
        int c = anio % 100;
        int d = b / 4;
        int e = b % 4;
        int f = (b + 8) / 25;
        int g = (b - f + 1) / 3;
        int h = (19 * a + b - d - g + 15) % 30;
        int i = c / 4;
        int k = c % 4;
        int l = (32 + 2 * e + 2 * i - h - k) % 7;
        int m = (a + 11 * h + 22 * l) / 451;
        int mes = (h + l - 7 * m + 114) / 31;
        int dia = ((h + l - 7 * m + 114) % 31) + 1;

        return LocalDate.of(anio, mes, dia);
    }
}