package com.ucb.tdd;

import java.util.Scanner;

public final class Main {
    private Main() { }

    public static void main(String[] args) {
        TresEnRaya juego = new TresEnRaya();
        char[][] vista = new char[3][3];
        int jugadas = 0;
        System.out.println("Tres en Raya: X y +. Fila y columna de 1 a 3. q para salir.");
        try (Scanner entrada = new Scanner(System.in)) {
            while (jugadas < 9) {
                mostrar(vista);
                System.out.print("Turno " + juego.siguienteTurno() + "> ");
                if (!entrada.hasNextLine()) {
                    return;
                }
                String linea = entrada.nextLine().trim();
                if (linea.equalsIgnoreCase("q")) {
                    return;
                }
                String[] valores = linea.split("\\s+");
                if (valores.length != 2) {
                    System.out.println("Escribe dos numeros separados por un espacio. Ejemplo: 1 3");
                    continue;
                }
                try {
                    int x = Integer.parseInt(valores[0]);
                    int y = Integer.parseInt(valores[1]);
                    char jugador = juego.siguienteTurno();
                    String resultado = juego.jugar(x, y);
                    vista[x - 1][y - 1] = jugador;
                    jugadas++;
                    System.out.println(resultado);
                    if (!resultado.equals("No hay ganador")) {
                        mostrar(vista);
                        return;
                    }
                } catch (NumberFormatException error) {
                    System.out.println("Las coordenadas deben ser numeros enteros.");
                } catch (Exception error) {
                    System.out.println("Jugada rechazada: " + error.getMessage());
                }
            }
            mostrar(vista);
            System.out.println("Tablero lleno sin ganador: empate.");
        }
    }

    private static void mostrar(char[][] tablero) {
        for (char[] fila : tablero) {
            for (char ficha : fila) {
                System.out.print((ficha == '\0' ? '.' : ficha) + " ");
            }
            System.out.println();
        }
    }
}
