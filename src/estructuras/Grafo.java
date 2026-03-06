/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;

public class Grafo {
    private NodoProteina primeraProteina;
    private int numVertices;

    public Grafo() {
        this.primeraProteina = null;
        this.numVertices = 0;
    }

    public NodoProteina getPrimeraProteina() {
        return primeraProteina;
    }

    public boolean estaVacio() {
        return primeraProteina == null;
    }
    
    public int getNumVertices() {
        return numVertices;
    }

    public NodoProteina buscarProteina(String nombre) {
        NodoProteina actual = primeraProteina;
        while (actual != null) {
            if (actual.getNombre().equals(nombre)) {
                return actual;
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    public void insertarProteina(String nombre) {
        if (buscarProteina(nombre) == null) {
            NodoProteina nueva = new NodoProteina(nombre);
            if (primeraProteina == null) {
                primeraProteina = nueva;
            } else {
                NodoProteina actual = primeraProteina;
                while (actual.getSiguiente() != null) {
                    actual = actual.getSiguiente();
                }
                actual.setSiguiente(nueva);
            }
            numVertices++;
        }
    }

    public void insertarInteraccion(String p1, String p2, int peso) {
        if (buscarProteina(p1) == null) insertarProteina(p1);
        if (buscarProteina(p2) == null) insertarProteina(p2);

        NodoProteina n1 = buscarProteina(p1);
        NodoProteina n2 = buscarProteina(p2);

        if (n1 != null && n2 != null) {
            NodoArista nuevaArista1 = new NodoArista(n2, peso);
            nuevaArista1.setSiguiente(n1.getPrimeraArista());
            n1.setPrimeraArista(nuevaArista1);

            NodoArista nuevaArista2 = new NodoArista(n1, peso);
            nuevaArista2.setSiguiente(n2.getPrimeraArista());
            n2.setPrimeraArista(nuevaArista2);
        }
    }

    public void eliminarProteina(String nombre) {
        NodoProteina aEliminar = buscarProteina(nombre);
        if (aEliminar == null) return;

        NodoProteina actual = primeraProteina;
        while (actual != null) {
            if (!actual.getNombre().equals(nombre)) {
                eliminarAristaUnidireccional(actual.getNombre(), nombre);
            }
            actual = actual.getSiguiente();
        }

        if (primeraProteina == aEliminar) {
            primeraProteina = primeraProteina.getSiguiente();
        } else {
            actual = primeraProteina;
            while (actual.getSiguiente() != null && actual.getSiguiente() != aEliminar) {
                actual = actual.getSiguiente();
            }
            if (actual.getSiguiente() != null) {
                actual.setSiguiente(aEliminar.getSiguiente());
            }
        }
        numVertices--;
    }

    public void eliminarInteraccion(String p1, String p2) {
        eliminarAristaUnidireccional(p1, p2);
        eliminarAristaUnidireccional(p2, p1);
    }

    private void eliminarAristaUnidireccional(String origen, String destino) {
        NodoProteina nOrigen = buscarProteina(origen);
        if (nOrigen != null) {
            NodoArista actualA = nOrigen.getPrimeraArista();
            NodoArista anteriorA = null;
            while (actualA != null) {
                if (actualA.getDestino().getNombre().equals(destino)) {
                    if (anteriorA == null) {
                        nOrigen.setPrimeraArista(actualA.getSiguiente());
                    } else {
                        anteriorA.setSiguiente(actualA.getSiguiente());
                    }
                    break;
                }
                anteriorA = actualA;
                actualA = actualA.getSiguiente();
            }
        }
    }

    public String identificarHubs() {
        if (primeraProteina == null) return "Grafo vacío";
        
        NodoProteina actual = primeraProteina;
        String hub = "";
        int maxConexiones = -1;
        
        while (actual != null) {
            int contador = 0;
            NodoArista arista = actual.getPrimeraArista();
            while (arista != null) {
                contador++;
                arista = arista.getSiguiente();
            }
            
            if (contador > maxConexiones) {
                maxConexiones = contador;
                hub = actual.getNombre();
            } else if (contador == maxConexiones) {
                hub += ", " + actual.getNombre();
            }
            actual = actual.getSiguiente();
        }
        
        return "Proteína(s) Hub (Diana terapéutica): " + hub + " con " + maxConexiones + " interacciones.";
    }

    public String detectarComplejosProteicos() {
        if (primeraProteina == null) return "Grafo vacío";
        
        String[] visitados = new String[numVertices];
        int numVisitados = 0;
        String resultado = "";
        int numComplejo = 1;
        
        NodoProteina actual = primeraProteina;
        while (actual != null) {
            if (!estaEnArreglo(visitados, numVisitados, actual.getNombre())) {
                resultado += "Complejo " + numComplejo + ": ";
                
                String[] pila = new String[numVertices];
                int tope = 0;
                pila[tope++] = actual.getNombre();
                
                while (tope > 0) {
                    String nombreNodo = pila[--tope];
                    if (!estaEnArreglo(visitados, numVisitados, nombreNodo)) {
                        visitados[numVisitados++] = nombreNodo;
                        resultado += nombreNodo + " ";
                        
                        NodoProteina nodo = buscarProteina(nombreNodo);
                        if (nodo != null) {
                            NodoArista arista = nodo.getPrimeraArista();
                            while (arista != null) {
                                if (!estaEnArreglo(visitados, numVisitados, arista.getDestino().getNombre())) {
                                    pila[tope++] = arista.getDestino().getNombre();
                                }
                                arista = arista.getSiguiente();
                            }
                        }
                    }
                }
                resultado += "\n";
                numComplejo++;
            }
            actual = actual.getSiguiente();
        }
        return resultado;
    }

    private boolean estaEnArreglo(String[] arreglo, int cantidad, String valor) {
        for (int i = 0; i < cantidad; i++) {
            if (arreglo[i].equals(valor)) return true;
        }
        return false;
    }

    public String dijkstra(String origen, String destino) {
        NodoProteina nOrigen = buscarProteina(origen);
        NodoProteina nDestino = buscarProteina(destino);
        
        if (nOrigen == null || nDestino == null) return "Una o ambas proteínas no existen en el grafo.";
        
        String[] nodos = new String[numVertices];
        int[] distancias = new int[numVertices];
        String[] previos = new String[numVertices];
        boolean[] visitados = new boolean[numVertices];
        
        NodoProteina actual = primeraProteina;
        int index = 0;
        int origenIndex = -1;
        int destinoIndex = -1;
        
        while (actual != null) {
            nodos[index] = actual.getNombre();
            distancias[index] = Integer.MAX_VALUE;
            previos[index] = null;
            visitados[index] = false;
            
            if (actual.getNombre().equals(origen)) origenIndex = index;
            if (actual.getNombre().equals(destino)) destinoIndex = index;
            
            actual = actual.getSiguiente();
            index++;
        }
        
        distancias[origenIndex] = 0;
        
        for (int i = 0; i < numVertices; i++) {
            int u = -1;
            int minDistancia = Integer.MAX_VALUE;
            
            for (int j = 0; j < numVertices; j++) {
                if (!visitados[j] && distancias[j] < minDistancia) {
                    minDistancia = distancias[j];
                    u = j;
                }
            }
            
            if (u == -1 || u == destinoIndex) break; 
            visitados[u] = true;
            
            NodoProteina nodoActual = buscarProteina(nodos[u]);
            if (nodoActual != null) {
                NodoArista arista = nodoActual.getPrimeraArista();
                while (arista != null) {
                    String vecino = arista.getDestino().getNombre();
                    int v = obtenerIndice(nodos, vecino);
                    
                    if (!visitados[v] && distancias[u] != Integer.MAX_VALUE) {
                        int nuevaDistancia = distancias[u] + arista.getPeso();
                        if (nuevaDistancia < distancias[v]) {
                            distancias[v] = nuevaDistancia;
                            previos[v] = nodos[u];
                        }
                    }
                    arista = arista.getSiguiente();
                }
            }
        }
        
        if (distancias[destinoIndex] == Integer.MAX_VALUE) {
            return "No hay ruta metabólica entre " + origen + " y " + destino;
        }
        
        String camino = "";
        String paso = destino;
        while (paso != null) {
            camino = paso + (camino.isEmpty() ? "" : " -> " + camino);
            paso = previos[obtenerIndice(nodos, paso)];
        }
        
        return "Ruta más corta: " + camino + "\nResistencia total (Costo): " + distancias[destinoIndex];
    }

    private int obtenerIndice(String[] nodos, String nombre) {
        for (int i = 0; i < nodos.length; i++) {
            if (nodos[i] != null && nodos[i].equals(nombre)) return i;
        }
        return -1;
    }
}