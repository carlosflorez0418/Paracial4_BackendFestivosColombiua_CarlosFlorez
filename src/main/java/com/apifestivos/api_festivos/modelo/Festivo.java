package com.apifestivos.api_festivos.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "festivo")
public class Festivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private Integer dia;
    private Integer mes;

    @Column(name = "diaspascua")
    private Integer diasPascua;

    @ManyToOne
    @JoinColumn(name = "idtipo")
    private Tipo tipo;

    public Festivo() {}

    public Festivo(Long id, String nombre, Integer dia, Integer mes, Integer diasPascuaValor, Tipo tipoObj) {
        this.id = id;
        this.nombre = nombre;
        this.dia = dia;
        this.mes = mes;
        this.diasPascua = diasPascuaValor;
        this.tipo = tipoObj;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public Integer getDia() { return dia; }
    public Integer getMes() { return mes; }
    public Integer getDiasPascua() { return diasPascua; }
    public Tipo getTipo() { return tipo; }

    public void setId(Long id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDia(Integer dia) { this.dia = dia; }
    public void setMes(Integer mes) { this.mes = mes; }
    public void setDiasPascua(Integer diasPascua) { this.diasPascua = diasPascua; }
    public void setTipo(Tipo tipo) { this.tipo = tipo; }
}