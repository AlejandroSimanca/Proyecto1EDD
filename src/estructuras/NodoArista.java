/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;

public class NodoArista {
    
    private NodoProteina destino;
    private int peso;
    private NodoArista siguiente;

    public NodoArista(NodoProteina destino, int peso) {
        this.destino = destino;
        this.peso = peso;
        this.siguiente = null;
    }

    public NodoProteina getDestino() {
        return destino;
    }

    public void setDestino(NodoProteina destino) {
        this.destino = destino;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public NodoArista getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoArista siguiente) {
        this.siguiente = siguiente;
    }
}