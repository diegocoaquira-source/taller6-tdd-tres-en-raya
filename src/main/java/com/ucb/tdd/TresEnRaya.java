package com.ucb.tdd;

/** x es fila e y es columna, ambas coordenadas de 1 a 3. */
public class TresEnRaya {
    private static final int LIMITE = 3;
    private final char[][] tablero = new char[LIMITE][LIMITE];
    private char ultimoJugador = '\0';
    private boolean terminado;
    /** Consulta sin efectos. Conserva la alternancia incluso tras la victoria. */
    public char siguienteTurno() {
        return ultimoJugador == 'X' ? '+' : 'X';
    }

    /**
     * Coloca la ficha del turno actual y devuelve el mensaje del docente.
     * Valida todo antes de cambiar el tablero o el turno.
     * Adicional explícito: rechaza nuevas jugadas después de una victoria.
     */
    public String jugar(int x, int y) throws Exception {
        verificarEje(x);
        verificarEje(y);
        if (terminado) {
            throw new IllegalStateException("La partida ya tiene ganador");
        }
        if (tablero[x - 1][y - 1] != '\0') {
            throw new IllegalStateException("La posicion ya esta ocupada");
        }
        char jugador = siguienteTurno();
        tablero[x - 1][y - 1] = jugador;
        ultimoJugador = jugador;
        if (hayGanador()) {
            terminado = true;
            return ultimoJugador + " es el ganador";
        }
        return "No hay ganador";
    }

    private void verificarEje(int eje) {
        if (eje < 1 || eje > LIMITE) {
            throw new IllegalArgumentException("La coordenada " + eje + " esta fuera de lugar");
        }
    }

    private boolean hayGanador() {
        for (int i = 0; i < LIMITE; i++) {
            if (lineaCompleta(tablero[i][0], tablero[i][1], tablero[i][2])
                    || lineaCompleta(tablero[0][i], tablero[1][i], tablero[2][i])) {
                return true;
            }
        }
        return lineaCompleta(tablero[0][0], tablero[1][1], tablero[2][2])
                || lineaCompleta(tablero[0][2], tablero[1][1], tablero[2][0]);
    }

    private boolean lineaCompleta(char primera, char segunda, char tercera) {
        return primera == ultimoJugador && segunda == ultimoJugador && tercera == ultimoJugador;
    }
}
