/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;

public class NodoProteina {
    
    private String nombre;
    private NodoArista primeraArista;
    private NodoProteina siguiente;

    public NodoProteina(String nombre) {
        this.nombre = nombre;
        this.primeraArista = null;
        this.siguiente = null;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public NodoArista getPrimeraArista() {
        return primeraArista;
    }

    public void setPrimeraArista(NodoArista primeraArista) {
        this.primeraArista = primeraArista;
    }

    public NodoProteina getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoProteina siguiente) {
        this.siguiente = siguiente;
    }
}