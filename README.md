BioGraph - Analizador de Redes Proteicas
Este proyecto es una herramienta en Java diseñada para estudiar cómo interactúan las proteínas entre sí. Utiliza grafos para representar estas conexiones, donde cada proteína es un nodo y cada interacción es una arista con un peso específico (que representa la fuerza o importancia de la unión).

¿Qué hace el programa?
Carga y Guardado: Lee archivos .csv o .txt con datos de proteínas y permite guardar los cambios que hagas manualmente.

Cálculo de Rutas (Dijkstra): Encuentra el camino más eficiente entre dos proteínas, algo útil para entender rutas metabólicas.

Detección de Hubs: Identifica cuáles son las proteínas "estrella" (las que tienen más conexiones) y que son críticas para la red.

Visualización Interactiva: Gracias a GraphStream, puedes ver el grafo en una ventana aparte, mover los nodos con el ratón y ver cómo se organiza la red en tiempo real.

Edición en Vivo: Puedes agregar o borrar proteínas y conexiones desde la interfaz y ver los resultados al instante en la consola integrada.

Cómo usarlo
Preparar los datos: Asegúrate de tener un archivo de texto donde cada línea sea: Proteina1, Proteina2, Peso.

Correr el proyecto: Abre el código en NetBeans, asegúrate de tener las librerías de GraphStream en el "ClassPath" y dale a Run.

Analizar: Usa el botón de Cargar Grafo para subir tu archivo. Una vez cargado, puedes usar los botones de algoritmos para ver los resultados en la consola de la derecha.

Ver el mapa: Presiona Visualizar Red para ver el dibujo interactivo de la red biológica.



Autor: Alejandro Simanca
