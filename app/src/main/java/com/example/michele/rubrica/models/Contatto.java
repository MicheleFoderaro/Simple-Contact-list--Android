package com.example.michele.rubrica.models;

/**
 * Created by Michele on 09/03/2017.
 */

public class Contatto {

    private int id;
    private String nome;
    private String numero;
    private int speciale;


    public Contatto() {
        speciale = 0;
    }

    public Contatto(String nome, String numero) {
        this.nome = nome;
        this.numero = numero;
        speciale = 0;
    }

    public int getSpeciale() {
        return speciale;
    }

    public void setSpeciale(int speciale) {
        this.speciale = speciale;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
