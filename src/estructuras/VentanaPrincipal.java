/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

public class VentanaPrincipal extends JFrame {
    
    private Grafo grafo;
    private GestorArchivos gestor;
    private JTextArea areaResultados;

    public VentanaPrincipal() {
        grafo = new Grafo();
        gestor = new GestorArchivos();
        
        setTitle("BioGraph - Análisis de Interacciones Proteicas");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(10, 1, 5, 5));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnCargar = new JButton("1. Cargar Grafo (CSV/TXT)");
        JButton btnGuardar = new JButton("2. Actualizar Repositorio");
        JButton btnHubs = new JButton("3. Identificar Hubs");
        JButton btnComplejos = new JButton("4. Detectar Complejos Proteicos");
        JButton btnDijkstra = new JButton("5. Ruta Metabólica (Dijkstra)");
        JButton btnAgregarProteina = new JButton("6. Agregar Proteína");
        JButton btnEliminarProteina = new JButton("7. Eliminar Proteína");
        JButton btnAgregarInteraccion = new JButton("8. Agregar Interacción");
        JButton btnVisualizar = new JButton("9. Visualizar Grafo");

        panelBotones.add(btnCargar);
        panelBotones.add(btnGuardar);
        panelBotones.add(new JSeparator());
        panelBotones.add(btnHubs);
        panelBotones.add(btnComplejos);
        panelBotones.add(btnDijkstra);
        panelBotones.add(new JSeparator());
        panelBotones.add(btnAgregarProteina);
        panelBotones.add(btnEliminarProteina);
        panelBotones.add(btnAgregarInteraccion);
        panelBotones.add(btnVisualizar);

        add(panelBotones, BorderLayout.WEST);

        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        areaResultados.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scroll = new JScrollPane(areaResultados);
        scroll.setBorder(BorderFactory.createTitledBorder("Resultados del Análisis"));
        
        add(scroll, BorderLayout.CENTER);

        btnCargar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                grafo = gestor.cargarGrafoDesdeArchivo();
                if (grafo != null && !grafo.estaVacio()) {
                    areaResultados.setText("Archivo cargado exitosamente.\nTotal de proteínas en el sistema: " + grafo.getNumVertices());
                } else {
                    areaResultados.setText("No se pudo cargar el archivo o está vacío.");
                }
            }
        });

        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gestor.guardarGrafoEnArchivo(grafo);
            }
        });

        btnHubs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validarGrafo()) {
                    areaResultados.setText("Calculando centralidad de grado...\n\n");
                    areaResultados.append(grafo.identificarHubs());
                }
            }
        });

        btnComplejos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validarGrafo()) {
                    areaResultados.setText("Realizando búsqueda DFS para encontrar componentes conexos...\n\n");
                    areaResultados.append(grafo.detectarComplejosProteicos());
                }
            }
        });

        btnDijkstra.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validarGrafo()) {
                    String origen = JOptionPane.showInputDialog(VentanaPrincipal.this, "Ingrese el nombre de la Proteína de Origen:");
                    String destino = JOptionPane.showInputDialog(VentanaPrincipal.this, "Ingrese el nombre de la Proteína de Destino:");
                    
                    if (origen != null && destino != null && !origen.trim().isEmpty() && !destino.trim().isEmpty()) {
                        areaResultados.setText("Calculando la ruta más corta (Dijkstra)...\n\n");
                        areaResultados.append(grafo.dijkstra(origen.trim(), destino.trim()));
                    }
                }
            }
        });

        btnAgregarProteina.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (grafo == null) grafo = new Grafo();
                String nombre = JOptionPane.showInputDialog(VentanaPrincipal.this, "Nombre de la nueva proteína:");
                if (nombre != null && !nombre.trim().isEmpty()) {
                    grafo.insertarProteina(nombre.trim());
                    areaResultados.setText("Proteína '" + nombre + "' agregada con éxito.");
                }
            }
        });

        btnEliminarProteina.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validarGrafo()) {
                    String nombre = JOptionPane.showInputDialog(VentanaPrincipal.this, "Nombre de la proteína a eliminar:");
                    if (nombre != null && !nombre.trim().isEmpty()) {
                        grafo.eliminarProteina(nombre.trim());
                        areaResultados.setText("Proteína '" + nombre + "' y sus conexiones han sido eliminadas.");
                    }
                }
            }
        });
        
        btnAgregarInteraccion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (grafo == null) grafo = new Grafo();
                String p1 = JOptionPane.showInputDialog(VentanaPrincipal.this, "Proteína Origen:");
                String p2 = JOptionPane.showInputDialog(VentanaPrincipal.this, "Proteína Destino:");
                String pesoStr = JOptionPane.showInputDialog(VentanaPrincipal.this, "Peso/Resistencia:");
                
                if (p1 != null && p2 != null && pesoStr != null) {
                    try {
                        int peso = Integer.parseInt(pesoStr.trim());
                        grafo.insertarInteraccion(p1.trim(), p2.trim(), peso);
                        areaResultados.setText("Interacción entre " + p1 + " y " + p2 + " agregada con éxito.");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(VentanaPrincipal.this, "El peso debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        btnVisualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                visualizarGrafo();
            }
        });
    }

    private boolean validarGrafo() {
        if (grafo == null || grafo.estaVacio()) {
            JOptionPane.showMessageDialog(this, "El grafo está vacío. Cargue un archivo o agregue proteínas primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void visualizarGrafo() {
        if (!validarGrafo()) return;

        System.setProperty("org.graphstream.ui", "swing");
        Graph visualGraph = new SingleGraph("BioGraph_Visual");

        visualGraph.setAttribute("ui.stylesheet", "node { fill-color: #4A90E2; size: 20px; text-size: 14px; text-color: #333; } edge { fill-color: #888; }");

        NodoProteina actual = grafo.getPrimeraProteina();
        
        while (actual != null) {
            visualGraph.addNode(actual.getNombre()).setAttribute("ui.label", actual.getNombre());
            actual = actual.getSiguiente();
        }

        actual = grafo.getPrimeraProteina();
        while (actual != null) {
            NodoArista aristaActual = actual.getPrimeraArista();
            while (aristaActual != null) {
                String origen = actual.getNombre();
                String destino = aristaActual.getDestino().getNombre();
                String idArista = origen + "-" + destino;
                String idAristaInversa = destino + "-" + origen;

                if (visualGraph.getEdge(idArista) == null && visualGraph.getEdge(idAristaInversa) == null) {
                    visualGraph.addEdge(idArista, origen, destino).setAttribute("ui.label", aristaActual.getPeso());
                }
                aristaActual = aristaActual.getSiguiente();
            }
            actual = actual.getSiguiente();
        }

        visualGraph.display();
        areaResultados.setText("Abriendo ventana de visualización del grafo...");
    }
}