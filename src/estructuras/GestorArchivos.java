/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;

public class GestorArchivos {

    public Grafo cargarGrafoDesdeArchivo() {
        Grafo nuevoGrafo = new Grafo();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar archivo dataset de Proteínas");
        
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos de texto y CSV (*.txt, *.csv)", "txt", "csv");
        fileChooser.setFileFilter(filtro);

        int seleccion = fileChooser.showOpenDialog(null);

        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    // 1. Limpiar espacios en blanco al inicio y final de la línea
                    linea = linea.trim();
                    if (linea.isEmpty()) continue;
                    
                    // 2. Separar por coma
                    String[] partes = linea.split(",");
                    if (partes.length >= 3) {
                        String p1 = partes[0].trim();
                        String p2 = partes[1].trim();
                        
                        // 3. LIMPIEZA CRÍTICA: Eliminar cualquier caracter que no sea un número (como las comillas o barras de Mac)
                        String pesoTexto = partes[2].trim().replaceAll("[^0-9]", "");
                        
                        if (!pesoTexto.isEmpty()) {
                            int peso = Integer.parseInt(pesoTexto);
                            nuevoGrafo.insertarInteraccion(p1, p2, peso);
                        }
                    }
                }
                JOptionPane.showMessageDialog(null, "Archivo cargado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                // Esto te mostrará exactamente qué parte falló si hay otro error
                JOptionPane.showMessageDialog(null, "Error al leer el archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
        return nuevoGrafo;
    }

    public void guardarGrafoEnArchivo(Grafo grafo) {
        if (grafo == null || grafo.estaVacio()) {
            JOptionPane.showMessageDialog(null, "No hay datos en el grafo para guardar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar repositorio de Proteínas");
        
        int seleccion = fileChooser.showSaveDialog(null);

        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            
            // Asegurar extensión .csv
            if (!archivo.getName().toLowerCase().endsWith(".csv") && !archivo.getName().toLowerCase().endsWith(".txt")) {
                archivo = new File(archivo.getParentFile(), archivo.getName() + ".csv");
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
                NodoProteina actual = grafo.getPrimeraProteina();
                
                while (actual != null) {
                    NodoArista aristaActual = actual.getPrimeraArista();
                    while (aristaActual != null) {
                        // Guardar solo una vez cada interacción (evitar duplicados en el archivo)
                        if (actual.getNombre().compareTo(aristaActual.getDestino().getNombre()) < 0) {
                            bw.write(actual.getNombre() + "," + aristaActual.getDestino().getNombre() + "," + aristaActual.getPeso());
                            bw.newLine();
                        }
                        aristaActual = aristaActual.getSiguiente();
                    }
                    actual = actual.getSiguiente();
                }
                JOptionPane.showMessageDialog(null, "Repositorio actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al guardar el archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
    

