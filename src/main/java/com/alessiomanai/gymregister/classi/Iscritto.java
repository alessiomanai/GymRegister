package com.alessiomanai.gymregister.classi;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.util.Calendar;

public class Iscritto {

    private String id;    //nome
    private int idDatabase;
    private String telefono;
    private String indirizzo;
    private String citta;
    private String dataDiNascita;
    private String urlFoto;
    private String note;
    private Drawable foto;
    private String notePath;
    private Pagamento pagamenti = new Pagamento();
    private Importi importi = new Importi();
    private String corso;
    private Corso palestra = new Corso();
    private String certificatoMedico;

    public Iscritto() {
    }

    /**
     * utile per caricare utenti al volo
     */
    public Iscritto(String id) {

        this.setId(id);

        this.pagamenti = new Pagamento();

    }

    /***
     * inizializza un nuovo utente
     */
    public Iscritto(String id, String telefono, String indirizzo, String citta, String dataDiNascita) {

        this.setId(id);
        this.setTelefono(telefono);
        this.setIndirizzo(indirizzo);
        this.setCitta(citta);
        this.setDataDiNascita(dataDiNascita);


        this.pagamenti = new Pagamento();
        this.pagamenti.setIscrizione("nonpagato\n");
        this.pagamenti.setSettembre("nonpagato\n");
        this.pagamenti.setOttobre("nonpagato\n");
        this.pagamenti.setNovembre("nonpagato\n");
        this.pagamenti.setDicembre("nonpagato\n");
        this.pagamenti.setGennaio("nonpagato\n");
        this.pagamenti.setFebbraio("nonpagato\n");
        this.pagamenti.setMarzo("nonpagato\n");
        this.pagamenti.setAprile("nonpagato\n");
        this.pagamenti.setMaggio("nonpagato\n");
        this.pagamenti.setGiugno("nonpagato\n");
        this.pagamenti.setLuglio("nonpagato\n");

    }

    /**
     * carica un utente
     */
    public Iscritto(String id, String telefono, String indirizzo, String citta, String dataDiNascita, String iscrizione,
                    String settembre, String ottobre, String novembre, String dicembre, String gennaio, String febbraio,
                    String marzo, String aprile, String maggio, String giugno, String luglio) {

        this.setId(id);
        this.setTelefono(telefono);
        this.setIndirizzo(indirizzo);
        this.setCitta(citta);
        this.setDataDiNascita(dataDiNascita);

        this.pagamenti = new Pagamento();
        this.pagamenti.setIscrizione(iscrizione);
        this.pagamenti.setSettembre(settembre);
        this.pagamenti.setOttobre(ottobre);
        this.pagamenti.setNovembre(novembre);
        this.pagamenti.setDicembre(dicembre);
        this.pagamenti.setGennaio(gennaio);
        this.pagamenti.setFebbraio(febbraio);
        this.pagamenti.setMarzo(marzo);
        this.pagamenti.setAprile(aprile);
        this.pagamenti.setMaggio(maggio);
        this.pagamenti.setGiugno(giugno);
        this.pagamenti.setLuglio(luglio);

    }

    public Iscritto(int idDatabase, String id, String telefono, String indirizzo, String citta, String dataDiNascita,
                    String iscrizione, int idCorso,
                    String settembre, String ottobre, String novembre, String dicembre, String gennaio, String febbraio,
                    String marzo, String aprile, String maggio, String giugno, String luglio) {

        this.setIdDatabase(idDatabase);
        this.setId(id);
        this.setTelefono(telefono);
        this.setIndirizzo(indirizzo);
        this.setCitta(citta);
        this.setDataDiNascita(dataDiNascita);
        this.setIdCorso(idCorso);

        this.pagamenti = new Pagamento();
        this.pagamenti.setIscrizione(iscrizione);
        this.pagamenti.setSettembre(settembre);
        this.pagamenti.setOttobre(ottobre);
        this.pagamenti.setNovembre(novembre);
        this.pagamenti.setDicembre(dicembre);
        this.pagamenti.setGennaio(gennaio);
        this.pagamenti.setFebbraio(febbraio);
        this.pagamenti.setMarzo(marzo);
        this.pagamenti.setAprile(aprile);
        this.pagamenti.setMaggio(maggio);
        this.pagamenti.setGiugno(giugno);
        this.pagamenti.setLuglio(luglio);

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public String getDataDiNascita() {
        return dataDiNascita;
    }

    public void setDataDiNascita(String dataDiNascita) {
        this.dataDiNascita = dataDiNascita;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getIscrizione() {
        return this.pagamenti.getIscrizione();
    }

    public void setIscrizione(String iscrizione) {
        this.pagamenti.setIscrizione(iscrizione);
    }

    public String getSettembre() {
        return this.pagamenti.getSettembre();
    }

    public void setSettembre(String settembre) {
        this.pagamenti.setSettembre(settembre);
    }

    public String getOttobre() {
        return this.pagamenti.getOttobre();
    }

    public void setOttobre(String ottobre) {
        this.pagamenti.setOttobre(ottobre);
    }

    public String getNovembre() {
        return this.pagamenti.getNovembre();
    }

    public void setNovembre(String novembre) {
        this.pagamenti.setNovembre(novembre);
    }

    public String getDicembre() {
        return this.pagamenti.getDicembre();
    }

    public void setDicembre(String dicembre) {
        this.pagamenti.setDicembre(dicembre);
    }

    public String getGennaio() {
        return this.pagamenti.getGennaio();
    }

    public void setGennaio(String gennaio) {
        this.pagamenti.setGennaio(gennaio);
    }

    public String getFebbraio() {
        return this.pagamenti.getFebbraio();
    }

    public void setFebbraio(String febbraio) {
        this.pagamenti.setFebbraio(febbraio);
    }

    public String getMarzo() {
        return this.pagamenti.getMarzo();
    }

    public void setMarzo(String marzo) {
        this.pagamenti.setMarzo(marzo);
    }

    public String getAprile() {
        return this.pagamenti.getAprile();
    }

    public void setAprile(String aprile) {
        this.pagamenti.setAprile(aprile);
    }

    public String getMaggio() {
        return this.pagamenti.getMaggio();
    }

    public void setMaggio(String maggio) {
        this.pagamenti.setMaggio(maggio);
    }

    public String getGiugno() {
        return this.pagamenti.getGiugno();
    }

    public void setGiugno(String giugno) {
        this.pagamenti.setGiugno(giugno);
    }

    public String getLuglio() {
        return this.pagamenti.getLuglio();
    }

    public void setLuglio(String luglio) {
        this.pagamenti.setLuglio(luglio);
    }

    public String getAgosto() {
        return this.pagamenti.getAgosto();
    }

    public void setAgosto(String agosto) {
        this.pagamenti.setAgosto(agosto);
    }

    public Drawable getFoto() {
        return foto;
    }

    public void setFoto(Drawable foto) {
        this.foto = foto;
    }

    public String getNote() {
        return this.generateNoteFile();
    }

    private void setNote(String note) {
        this.note = note;
    }

    private String generateNoteFile() {

        return "note utente " + this.getIdDatabase() + ".txt";

    }

    @Override
    public String toString() {

        return id;
    }

    public String getCorso() {
        return corso;
    }

    public void setCorso(String corso) {
        this.corso = corso;
    }

    public void calendarToString(Calendar data) {

        this.setDataDiNascita(data.toString());
    }

    public int getIdDatabase() {
        return idDatabase;
    }

    public void setIdDatabase(int idDatabase) {
        this.idDatabase = idDatabase;
    }

    public Corso getPalestra() {
        return palestra;
    }

    public void setPalestra(Corso palestra) {
        this.palestra = palestra;
    }

    public int getIdCorso() {
        return this.palestra.getId();
    }

    public void setIdCorso(int id) {
        this.palestra = new Corso(id, null);
    }

    public Pagamento getPagamenti() {
        return pagamenti;
    }

    public void setPagamenti(Pagamento pagamenti) {
        this.pagamenti = pagamenti;
    }

    public Importi getImporti() {
        return importi;
    }

    public void setImporti(Importi importi) {
        this.importi = importi;
    }

    public String getCertificatoMedico() {
        return certificatoMedico;
    }

    public void setCertificatoMedico(String certificatoMedico) {
        this.certificatoMedico = certificatoMedico;
    }
}
