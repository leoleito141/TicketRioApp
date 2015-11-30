package com.ticketrioapp.ticketrioapp.wrapper;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

/**
 * Created by Juanma on 01/11/2015.
 */
public class WrapperCompetencia {


    @SerializedName("fecha")
    private Date fecha;

    @SerializedName("estadio")
    private String estadio;

    @SerializedName("precioEntrada")
    private float precioEntrada;

    @SerializedName("cantEntradas")
    private int cantEntradas;

    @SerializedName("entradasVendidas")
    private int entradasVendidas;

    @SerializedName("tenantId")
    private int tenantID;

    @SerializedName("idCompetencia")
    private int CompetenciaId;


    public WrapperCompetencia() {
    }

    public WrapperCompetencia( Date fecha, float precioEntrada, String estadio, int cantEntradas,  int entradasVendidas,int tenantID, int competenciaId) {
        this.fecha = fecha;
        this.precioEntrada = precioEntrada;
        this.estadio = estadio;
        this.cantEntradas = cantEntradas;
        this.entradasVendidas = entradasVendidas;
        this.tenantID = tenantID;
        this.CompetenciaId = competenciaId;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getEntradasVendidas() {
        return entradasVendidas;
    }

    public void setEntradasVendidas(int entradasVendidas) {
        this.entradasVendidas = entradasVendidas;
    }

    public String getEstadio() {
        return estadio;
    }

    public void setEstadio(String estadio) {
        this.estadio = estadio;
    }

    public float getPrecioEntrada() {
        return precioEntrada;
    }

    public void setPrecioEntrada(float precioEntrada) {
        this.precioEntrada = precioEntrada;
    }

    public int getCantEntradas() {
        return cantEntradas;
    }

    public void setCantEntradas(int cantEntradas) {
        this.cantEntradas = cantEntradas;
    }

    public int getTenantID() {
        return tenantID;
    }

    public void setTenantID(int tenantID) {
        this.tenantID = tenantID;
    }

    public int getCompetenciaId() {
        return CompetenciaId;
    }

    public void setCompetenciaId(int competenciaId) {
        CompetenciaId = competenciaId;
    }
}
