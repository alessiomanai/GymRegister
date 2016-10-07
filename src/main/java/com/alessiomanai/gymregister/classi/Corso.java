package com.alessiomanai.gymregister.classi;

public class Corso {

    private int id;
    private String nome;    //nome palestra
    private String noteCorso;

    public Corso() {
    }

    public Corso(String nomeCorso) {

        this.setNome(nomeCorso);
    }

    public Corso(int id, String nomeCorso) {

        this.setId(id);
        this.setNome(nomeCorso);
    }


    public String getNome() {
        return nome;
    }

    public String toString() {

        return this.getNome();

    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFileUsers() {
        return nome + "id.txt";
    }

    public String getFileIndirizzi() {
        return nome + "address.txt";
    }

    public String getFileNumeriTelefono() {
        return nome + "tel.txt";
    }

    public String getFiledatanascita() {
        return nome + "nascita.txt";
    }

    public String getFilecitta() {
        return nome + "citta.txt";
    }

    public String getFileiscrizione() {
        return nome + "iscrizione.txt";
    }

    public String getFileSettembre() {
        return nome + "settembre.txt";
    }

    public String getFileOttobre() {
        return nome + "ottobre.txt";
    }

    public String getFileNovembre() {
        return nome + "novembre.txt";
    }

    public String getFileDicembre() {
        return nome + "dicembre.txt";
    }

    public String getFileGennaio() {
        return nome + "gennaio.txt";
    }

    public String getFileFebbraio() {
        return nome + "febbraio.txt";
    }

    public String getFileMarzo() {
        return nome + "marzo.txt";
    }

    public String getFileAprile() {
        return nome + "aprile.txt";
    }

    public String getFileMaggio() {
        return nome + "maggio.txt";
    }

    public String getFileGiugno() {
        return nome + "giugno.txt";
    }

    public String getFileLuglio() {
        return nome + "luglio.txt";
    }

    public String getBackupOf(String nomeFile) {
        return "Backup" + nomeFile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String generateNoteFile() {

        return "note corso " + this.getId() + ".txt";

    }

    public String getNoteCorso() {
        return this.generateNoteFile();
    }

    private void setNoteCorso(String noteCorso) {
        this.noteCorso = noteCorso;
    }

    public void setNoteFromDatabase(String notePath) {

        this.setNoteCorso(notePath);
    }

}
