import random
import sys
import time

def main():
    # Verifica que se haya proporcionado el número de cadenas como argumento
    if len(sys.argv) < 2:
        print("Por favor, proporciona el número de cadenas como argumento.")
        return

    try:
        num_cadenas = int(sys.argv[1])
    except ValueError:
        print("El argumento debe ser un número entero.")
        return

    letras = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    objetivo = "IPN"
    ocurrencias = 0
    tiempos = []
    intentos_ocurrencia = []

    inicio = time.time()

    for i in range(num_cadenas):
        # Genera las primeras 3 letras
        primera_parte = ''.join(random.choice(letras) for _ in range(3))
        # Genera las siguientes 3 letras
        segunda_parte = ''.join(random.choice(letras) for _ in range(3))
        cadena = primera_parte + " " + segunda_parte

        if primera_parte == objetivo:
            ocurrencias += 1
            tiempo_ocurrencia = time.time() - inicio
            tiempos.append(tiempo_ocurrencia)
            intentos_ocurrencia.append(i + 1)
            print(f"¡Coincidencia encontrada: {cadena} en el intento {i + 1}. Tiempo desde inicio: {tiempo_ocurrencia:.3f} segundos.")

    fin = time.time()
    tiempo_total = fin - inicio

    print("\nResumen:")
    print(f"Total de ocurrencias de '{objetivo}': {ocurrencias}")
    if ocurrencias > 0:
        for k in range(ocurrencias):
            print(f"Ocurrencia #{k + 1} en el intento {intentos_ocurrencia[k]}, tiempo: {tiempos[k]:.3f} segundos.")
    else:
        print(f"No se encontró la coincidencia '{objetivo}'.")
    
    print(f"Tiempo total transcurrido: {tiempo_total:.3f} segundos.")

if __name__ == "__main__":
    main()
