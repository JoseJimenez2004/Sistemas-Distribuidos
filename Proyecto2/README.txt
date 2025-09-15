PROYECTO 2 - Simulación de Nave Espacial con Asteroides

Descripción
Simulación gráfica en 2D de una nave espacial que debe atravesar un campo de asteroides, desde el lado izquierdo hasta el derecho de la pantalla, evitando colisiones con asteroides de diferentes tamaños y velocidades.

## Requisitos Cumplidos
- ✅ Recibe como parámetro el número de asteroides (5-30)
- ✅ Ventana de 1280x720 píxeles
- ✅ Asteroides con formas irregulares y movimiento realista
- ✅ Nave espacial con movimiento acelerado (no velocidad constante)
- ✅ Detección de colisiones nave-asteroide
- ✅ Sistema de progreso y estadísticas


## Estructura de Archivos

Proyecto2/
├── SpaceSimulation.java    # Clase principal - Ventana y control general
├── SpacePanel.java         # Panel de juego - Lógica de renderizado y actualización
├── Spaceship.java          # Nave espacial - Movimiento y gráficos
└── Asteroid.java           # Asteroides - Comportamiento y apariencia


## Ejecución del Proyecto

### Compilación

# Compilar todos los archivos
javac -d . Proyecto2/SpaceSimulation.java Proyecto2/SpacePanel.java Proyecto2/Spaceship.java Proyecto2/Asteroid.java


### Ejecución

# Ejecutar la aplicación principal
java Proyecto2.SpaceSimulation
```

## Características Principales

### Nave Espacial
- Movimiento acelerado realista (no velocidad constante)
- Rotación automática según dirección
- Estela visual de partículas
- Propulsores animados que varían con la velocidad

### Sistema de Asteroides
- Generación procedural de formas irregulares
- Movimiento y rotación aleatoria
- Diferentes tamaños (15-40 píxeles)
- Velocidad inversamente proporcional al tamaño
- Sistema de reciclaje para optimización

### Interfaz de Usuario
- Selector de dificultad (número de asteroides)
- Barra de progreso con diseño moderno
- Contadores de tiempo y asteroides evitados
- Diálogos de game over y victoria mejorados
- Botones con efectos visuales y hover

## Parámetros Configurables

### Dificultad
- **Asteroides**: 5-30 (seleccionable al inicio)
- **Velocidad máxima nave**: 5.0 unidades/frame
- **Aceleración nave**: 0.15 unidades/frame²

### Visuales
- **Tamaño nave**: 30 píxeles
- **Tamaño asteroides**: 15-40 píxeles
- **Estela máxima**: 50 partículas
- **Ratio de generación asteroides**: 40 frames

## Controles
- **Automático**: La nave se controla automáticamente
- **Interfaz**: Botones para iniciar/aceptar/cancelar
- **Teclado**: ESC para salir de diálogos

## Métricas Mostradas
- Porcentaje de progreso (0-100%)
- Tiempo transcurrido (segundos)
- Asteroides evitados
- Dificultad seleccionada

## Tecnologías Utilizadas
- **Java Swing** para gráficos
- **Java AWT** para geometría y rendering
- **Gradientes** para efectos visuales
- **Polygon** para formas irregulares
- **Timer** para animaciones suaves


