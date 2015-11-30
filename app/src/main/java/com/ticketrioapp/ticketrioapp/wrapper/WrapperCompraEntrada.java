package com.ticketrioapp.ticketrioapp.wrapper;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Juanma on 04/11/2015.
 */
public class WrapperCompraEntrada {

    @SerializedName("tenantId")
    private int tenantId;

    @SerializedName("idCompetencia")
    private int idCompetencia;

    @SerializedName("cantEntradas")
    private int cantEntradas;

    @SerializedName("mail")
    private String mail;


    public WrapperCompraEntrada() {

    }


    public WrapperCompraEntrada(int tenantId, int idCompetencia, int cantEntradas, String mail) {
        this.tenantId = tenantId;
        this.idCompetencia = idCompetencia;
        this.cantEntradas = cantEntradas;
        this.mail = mail;
    }

    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    public int getIdCompetencia() {
        return idCompetencia;
    }

    public void setIdCompetencia(int idCompetencia) {
        this.idCompetencia = idCompetencia;
    }

    public int getCantEntradas() {
        return cantEntradas;
    }

    public void setCantEntradas(int cantEntradas) {
        this.cantEntradas = cantEntradas;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
