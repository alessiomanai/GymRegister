package com.alessiomanai.gymregister.classi;

/**
 * Created by alessio on 04/09/16.
 */
public class Presenza {

    private Iscritto iscritto;
    private Corso corso;
    private String data;

    public Presenza() {

        this.iscritto = new Iscritto();
        this.corso = new Corso();

    }

    public Presenza(Iscritto iscritto, Corso corso) {

        this.iscritto = iscritto;
        this.corso = corso;
        this.data = "0";

    }

    public Presenza(Iscritto iscritto, Corso corso, String data) {

        this.iscritto = iscritto;
        this.corso = corso;
        this.data = data;

        this.iscritto.setId(iscritto.getId());
        this.iscritto.setIdDatabase(iscritto.getIdDatabase());

        this.corso.setId(corso.getId());

    }

    public String getNomeIscritto() {

        return this.iscritto.getId();
    }

    public int getIdIscritto() {

        return this.iscritto.getIdDatabase();
    }

    public int getIdCorso() {

        return this.corso.getId();
    }

    public Iscritto getIscritto() {
        return iscritto;
    }

    public void setIscritto(Iscritto iscritto) {
        this.iscritto = iscritto;
    }

    public Corso getCorso() {
        return corso;
    }

    public void setCorso(Corso corso) {
        this.corso = corso;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setIdIscritto(int idIscritto) {

        this.iscritto.setIdDatabase(idIscritto);

    }

    public void setIdCorso(int idCorso) {

        this.corso.setId(idCorso);

    }

}
