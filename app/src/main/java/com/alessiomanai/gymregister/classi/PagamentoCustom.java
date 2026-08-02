package com.alessiomanai.gymregister.classi;


public class PagamentoCustom {

    private String nome;
    private int pagato;
    private Iscritto iscritto;
    private Corso corso;

    public PagamentoCustom() {
    }

    public PagamentoCustom(String nome) {

        this.setNome(nome);
    }

    public PagamentoCustom(String nome, int pagato, Iscritto iscritto) {

        this.setNome(nome);
        this.setPagato(pagato);
        this.setIscritto(iscritto);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getPagato() {
        return pagato;
    }

    public void setPagato(int pagato) {
        this.pagato = pagato;
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

    private void setCorso() {

        this.corso = this.iscritto.getPalestra();
    }
}
