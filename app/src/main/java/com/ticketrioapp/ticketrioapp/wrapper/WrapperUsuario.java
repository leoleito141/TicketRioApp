package com.ticketrioapp.ticketrioapp.wrapper;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Juanma on 25/10/2015.
 */


public class WrapperUsuario {

    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;

    @SerializedName("tenantId")
    private int tenantID;

    public WrapperUsuario() {
    }

    public WrapperUsuario(String email, String password, int tenantID) {

        this.email = email;
        this.password = password;
        this.tenantID = tenantID;
    }


    public String imprimirBookData(){

        return (this.tenantID+" "+this.password+" "+this.email);
    }





    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return email;
    }

    public void setMail(String email) {
        this.email = email;
    }

    public int getTenantID() {
        return tenantID;
    }

    public void setTenantID(int tenantID) {
        this.tenantID = tenantID;
    }





}