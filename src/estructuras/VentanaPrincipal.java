/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;

import javax.swing.*;
import java.awt.*;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.view.Viewer;

public class VentanaPrincipal extends JFrame {

    private Grafo grafo;
    private GestorArchivos gestor;
    private JTextArea areaResultados;
    private JPanel panelBotones;

    public VentanaPrincipal() {
        this.grafo = new Grafo();
        this.gestor = new GestorArchivos();
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("BioGraph - Análisis de Redes Proteicas");
        setSize(1100, 750); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(44, 62, 80));
        JLabel lblTitulo = new JLabel("BIOGRAPH PRO: INTERACCIONES PROTEÍNAS");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        panelBotones = new JPanel(new GridLayout(10, 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
        panelBotones.setPreferredSize(new Dimension(280, 0));

        Color cCarga = new Color(174, 214, 241);
        Color cAlgo = new Color(171, 235, 198);
        Color cPeligro = new Color(245, 183, 177);
        Color cVisual = new Color(210, 180, 222);

        JButton btnCargar = crearBoton("1. Cargar Grafo (CSV/TXT)", cCarga);
        JButton btnGuardar = crearBoton("2. Actualizar Repositorio", cCarga);
        JButton btnHubs = crearBoton("3. Identificar Hubs", cAlgo);
        JButton btnComplejos = crearBoton("4. Detectar Complejos", cAlgo);
        JButton btnDijkstra = crearBoton("5. Ruta Metabólica", cAlgo);
        JButton btnAgregarP = crearBoton("6. Agregar Proteína", Color.LIGHT_GRAY);
        JButton btnEliminarP = crearBoton("7. Eliminar Proteína", cPeligro);
        JButton btnAgregarI = crearBoton("8. Agregar Interacción", Color.LIGHT_GRAY);
        JButton btnVisualizar = crearBoton("9. VISUALIZAR RED", cVisual);

        panelBotones.add(btnCargar);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnHubs);
        panelBotones.add(btnComplejos);
        panelBotones.add(btnDijkstra);
        panelBotones.add(btnAgregarP);
        panelBotones.add(btnEliminarP);
        panelBotones.add(btnAgregarI);
        panelBotones.add(btnVisualizar);

        add(panelBotones, BorderLayout.WEST);

        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        areaResultados.setFont(new Font("Monospaced", Font.PLAIN, 15));
        areaResultados.setBackground(new Color(252, 252, 252));
        areaResultados.setMargin(new Insets(20, 20, 20, 20));
        areaResultados.setText(">>> BIOGRAPH LISTO. POR FAVOR, CARGUE UN DATASET.\n");

        JScrollPane scroll = new JScrollPane(areaResultados);
        scroll.setBorder(BorderFactory.createTitledBorder("RESULTADOS DEL ANÁLISIS"));
        add(scroll, BorderLayout.CENTER);

        btnCargar.addActionListener(e -> {
            Grafo nuevo = gestor.cargarGrafoDesdeArchivo();
            if (nuevo != null && !nuevo.estaVacio()) {
                this.grafo = nuevo;
                areaResultados.setText(">>> ARCHIVO CARGADO EXITOSAMENTE.\n");
                areaResultados.append("Nodos totales en memoria: " + grafo.getNumVertices() + "\n");
            }
        });

        btnGuardar.addActionListener(e -> {
            if (validarGrafo()) {
                gestor.guardarGrafoEnArchivo(grafo);
            }
        });

        btnHubs.addActionListener(e -> {
            if (validarGrafo()) {
                areaResultados.append("\n[ANÁLISIS DE HUBS]\n" + grafo.identificarHubs() + "\n");
            }
        });

        btnComplejos.addActionListener(e -> {
            if (validarGrafo()) {
                areaResultados.append("\n[DETECCIÓN DE COMPLEJOS]\n" + grafo.detectarComplejosProteicos() + "\n");
            }
        });

        btnDijkstra.addActionListener(e -> {
            if (validarGrafo()) {
                String origen = JOptionPane.showInputDialog(this, "Nombre de Proteína Origen:");
                String destino = JOptionPane.showInputDialog(this, "Nombre de Proteína Destino:");
                if (origen != null && destino != null) {
                    areaResultados.append("\n[RUTA DIJKSTRA]\n" + grafo.dijkstra(origen.trim(), destino.trim()) + "\n");
                }
            }
        });

        btnVisualizar.addActionListener(e -> visualizarGrafo());

        btnAgregarP.addActionListener(e -> {
            String nombre = JOptionPane.showInputDialog(this, "Nombre de nueva proteína:");
            if (nombre != null && !nombre.trim().isEmpty()) {
                grafo.insertarProteina(nombre.trim());
                areaResultados.append(">>> Proteína " + nombre + " agregada correctamente.\n");
            }
        });

        btnEliminarP.addActionListener(e -> {
            if (validarGrafo()) {
                String nombre = JOptionPane.showInputDialog(this, "Proteína a eliminar:");
                if (nombre != null) {
                    grafo.eliminarProteina(nombre.trim());
                    areaResultados.append(">>> " + nombre + " y sus conexiones han sido removidas.\n");
                }
            }
        });

        btnAgregarI.addActionListener(e -> {
            if (validarGrafo()) {
                String p1 = JOptionPane.showInputDialog(this, "Proteína de Origen:");
                String p2 = JOptionPane.showInputDialog(this, "Proteína de Destino:");
                String pS = JOptionPane.showInputDialog(this, "Peso de la interacción (número):");
                
                try {
                    if (p1 != null && p2 != null && pS != null) {
                        int peso = Integer.parseInt(pS.trim());
                        grafo.insertarInteraccion(p1.trim(), p2.trim(), peso);
                        areaResultados.append(">>> Nueva interacción: " + p1 + " <-> " + p2 + " (Peso: " + peso + ")\n");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "El peso debe ser un número entero.");
                }
            }
        });
    }

    private JButton crearBoton(String texto, Color colorFondo) {
        JButton btn = new JButton(texto);
        btn.setOpaque(true);
        btn.setBorderPainted(true);
        btn.setBackground(colorFondo);
        btn.setForeground(Color.BLACK); 
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private boolean validarGrafo() {
        if (grafo == null || grafo.estaVacio()) {
            JOptionPane.showMessageDialog(this, "Error: El grafo está vacío. Cargue datos primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void visualizarGrafo() {
        if (!validarGrafo()) return;

        System.setProperty("org.graphstream.ui", "swing");
        Graph gsGrafo = new SingleGraph("BioRed");

        String styleSheet = 
            "node { fill-color: #2980b9; size: 35px; text-size: 16px; text-style: bold; text-color: #2c3e50; text-alignment: at-right; text-offset: 5px, 5px; stroke-mode: plain; stroke-color: #ecf0f1; stroke-width: 2px; } " +
            "edge { fill-color: #95a5a6; width: 2px; text-size: 14px; text-background-mode: rounded-box; text-background-color: white; }";
        
        gsGrafo.setAttribute("ui.stylesheet", styleSheet);

        NodoProteina p = grafo.getPrimeraProteina();
        while (p != null) {
            Node n = gsGrafo.addNode(p.getNombre());
            n.setAttribute("ui.label", p.getNombre());
            p = p.getSiguiente();
        }

        p = grafo.getPrimeraProteina();
        while (p != null) {
            NodoArista a = p.getPrimeraArista();
            while (a != null) {
                String id = p.getNombre() + "-" + a.getDestino().getNombre();
                String idInv = a.getDestino().getNombre() + "-" + p.getNombre();
                if (gsGrafo.getEdge(id) == null && gsGrafo.getEdge(idInv) == null) {
                    Edge e = gsGrafo.addEdge(id, p.getNombre(), a.getDestino().getNombre());
                    e.setAttribute("ui.label", String.valueOf(a.getPeso()));
                }
                a = a.getSiguiente();
            }
            p = p.getSiguiente();
        }

        Viewer viewer = gsGrafo.display();
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
    }
}
