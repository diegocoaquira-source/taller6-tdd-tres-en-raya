package com.ucb.tdd;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TresEnRayaIATest {
    private static final int[][][] VICTORIAS_X = {
        {{1, 1}, {2, 1}, {1, 2}, {2, 2}, {1, 3}},
        {{2, 1}, {1, 1}, {2, 2}, {1, 2}, {2, 3}},
        {{3, 1}, {1, 1}, {3, 2}, {1, 2}, {3, 3}},
        {{1, 1}, {1, 2}, {2, 1}, {1, 3}, {3, 1}},
        {{1, 2}, {1, 1}, {2, 2}, {1, 3}, {3, 2}},
        {{1, 3}, {1, 1}, {2, 3}, {1, 2}, {3, 3}},
        {{1, 1}, {1, 2}, {2, 2}, {1, 3}, {3, 3}},
        {{1, 3}, {1, 1}, {2, 2}, {1, 2}, {3, 1}}
    };

    private static final int[][][] VICTORIAS_MAS = {
        {{2, 1}, {1, 1}, {2, 2}, {1, 2}, {3, 1}, {1, 3}},
        {{1, 1}, {2, 1}, {1, 2}, {2, 2}, {3, 1}, {2, 3}},
        {{1, 1}, {3, 1}, {1, 2}, {3, 2}, {2, 1}, {3, 3}},
        {{1, 2}, {1, 1}, {1, 3}, {2, 1}, {2, 2}, {3, 1}},
        {{1, 1}, {1, 2}, {1, 3}, {2, 2}, {2, 1}, {3, 2}},
        {{1, 1}, {1, 3}, {1, 2}, {2, 3}, {2, 1}, {3, 3}},
        {{1, 2}, {1, 1}, {1, 3}, {2, 2}, {2, 1}, {3, 3}},
        {{1, 1}, {1, 3}, {1, 2}, {2, 2}, {2, 1}, {3, 1}}
    };

    @Test
    public void limitesYExtremosEnAmbosEjesConservanAmbosTurnos() throws Exception {
        int[][] coordenadas = {
            {0,2}, {4,2}, {2,0}, {2,4},
            {Integer.MIN_VALUE,2}, {Integer.MAX_VALUE,2},
            {2,Integer.MIN_VALUE}, {2,Integer.MAX_VALUE},
            {0,0}, {4,4}, {Integer.MIN_VALUE,Integer.MAX_VALUE},
            {Integer.MAX_VALUE,Integer.MIN_VALUE}
        };
        for (boolean despuesDeX : new boolean[]{false, true}) {
            for (int[] casilla : coordenadas) {
                TresEnRaya juego = new TresEnRaya();
                if (despuesDeX) juego.jugar(2,2);
                char turno = despuesDeX ? '+' : 'X';
                assertThrows(Exception.class, () -> juego.jugar(casilla[0],casilla[1]));
                assertEquals(turno, juego.siguienteTurno());
                assertEquals("No hay ganador", juego.jugar(1,1));
                assertEquals(despuesDeX ? 'X' : '+', juego.siguienteTurno());
            }
        }
    }

    @Test
    public void cualquieraDeLasNueveCasillasPermitePrimeraJugada() throws Exception {
        for (int x=1; x<=3; x++) {
            for (int y=1; y<=3; y++) {
                TresEnRaya juego = new TresEnRaya();
                assertEquals("No hay ganador", juego.jugar(x,y));
                assertEquals('+', juego.siguienteTurno());
                final int fila=x, columna=y;
                assertThrows(Exception.class, () -> juego.jugar(fila,columna));
                assertEquals('+', juego.siguienteTurno());
            }
        }
    }

    @Test
    public void rechazosConsecutivosDeOcupadaNoConsumenTurnos() throws Exception {
        TresEnRaya juego = new TresEnRaya();
        juego.jugar(2,2);
        for (int intento=0; intento<3; intento++) {
            assertThrows(Exception.class, () -> juego.jugar(2,2));
            assertEquals('+', juego.siguienteTurno());
        }
        assertEquals("No hay ganador", juego.jugar(1,1));
        assertEquals('X', juego.siguienteTurno());
    }

    @Test
    public void rechazarOcupadaEnTurnoDeXConservaX() throws Exception {
        TresEnRaya juego = new TresEnRaya();
        juego.jugar(1,1);
        juego.jugar(2,2);
        assertThrows(Exception.class, () -> juego.jugar(2,2));
        assertEquals('X', juego.siguienteTurno());
        assertEquals("No hay ganador", juego.jugar(3,3));
        assertEquals('+', juego.siguienteTurno());
    }

    @Test
    public void consultarTurnoRepetidamenteNoAvanzaLaPartida() throws Exception {
        TresEnRaya juego = new TresEnRaya();
        for (int i=0; i<3; i++) assertEquals('X', juego.siguienteTurno());
        juego.jugar(1,1);
        for (int i=0; i<3; i++) assertEquals('+', juego.siguienteTurno());
        juego.jugar(2,2);
        for (int i=0; i<3; i++) assertEquals('X', juego.siguienteTurno());
    }

    @Test
    public void dosPartidasMantienenTablerosYTurnosIndependientes() throws Exception {
        TresEnRaya primera = new TresEnRaya();
        TresEnRaya segunda = new TresEnRaya();
        primera.jugar(1,1);
        assertEquals('+', primera.siguienteTurno());
        assertEquals('X', segunda.siguienteTurno());
        assertEquals("No hay ganador", segunda.jugar(1,1));
        assertEquals('+', segunda.siguienteTurno());
    }

    @Test
    public void ochoLineasParaXVerificanCadaRespuestaYTurno() throws Exception {
        for (int[][] secuencia : VICTORIAS_X) comprobarPartida(secuencia, 'X');
    }

    @Test
    public void ochoLineasParaMasVerificanCadaRespuestaYTurno() throws Exception {
        for (int[][] secuencia : VICTORIAS_MAS) comprobarPartida(secuencia, '+');
    }

    @Test
    public void victoriaEnNovenaJugadaTienePrioridadSobreTableroLleno() throws Exception {
        comprobarPartida(new int[][]{{1,1},{1,2},{2,2},{1,3},{2,1},{2,3},{3,2},{3,1},{3,3}}, 'X');
        comprobarPartida(new int[][]{{1,3},{1,2},{2,2},{1,1},{2,3},{2,1},{3,2},{3,3},{3,1}}, 'X');
    }

    @Test
    public void masPuedeGanarEnLaOctavaJugada() throws Exception {
        comprobarPartida(new int[][]{{1, 1}, {1, 2}, {1, 3}, {2, 1}, {2, 3}, {2, 2}, {3, 1}, {3, 2}}, '+');
    }

    @Test
    public void tableroLlenoSinLineaMantieneNoHayGanador() throws Exception {
        TresEnRaya juego = new TresEnRaya();
        int[][] empate = {{1,1},{1,2},{1,3},{2,2},{2,1},{2,3},{3,2},{3,1},{3,3}};
        for (int[] casilla : empate) {
            assertEquals("No hay ganador", juego.jugar(casilla[0],casilla[1]));
        }
        assertEquals('+', juego.siguienteTurno());
        for (int[] casilla : empate) {
            assertThrows(Exception.class, () -> juego.jugar(casilla[0],casilla[1]));
            assertEquals('+', juego.siguienteTurno());
        }
    }

    @Test
    public void dosFichasPropiasYUnaAjenaNoFormanLineaGanadora() throws Exception {
        TresEnRaya juego = new TresEnRaya();
        assertEquals("No hay ganador", juego.jugar(1,1));
        assertEquals("No hay ganador", juego.jugar(1,2));
        assertEquals("No hay ganador", juego.jugar(1,3));
        assertEquals('+', juego.siguienteTurno());
    }

    @Test
    public void erroresDeRangoNoImpidenUnaVictoriaPosterior() throws Exception {
        TresEnRaya juego = new TresEnRaya();
        juego.jugar(1,1);
        assertThrows(Exception.class, () -> juego.jugar(4,1));
        assertThrows(Exception.class, () -> juego.jugar(1,0));
        assertEquals('+', juego.siguienteTurno());
        juego.jugar(2,1);
        juego.jugar(1,2);
        juego.jugar(2,2);
        assertEquals("X es el ganador", juego.jugar(1,3));
    }

    @Test
    public void rechazarOcupadaNoBorraNiReemplazaLaFichaOriginal() throws Exception {
        TresEnRaya juego = new TresEnRaya();
        juego.jugar(1,1);
        assertThrows(Exception.class, () -> juego.jugar(1,1));
        assertEquals('+', juego.siguienteTurno());
        juego.jugar(2,1);
        juego.jugar(1,2);
        juego.jugar(2,2);
        assertEquals("X es el ganador", juego.jugar(1,3));
    }

    @Test
    public void adicionalTiposDeExcepcionDeclaradosEnElContrato() throws Exception {
        TresEnRaya juego = new TresEnRaya();
        assertThrows(IllegalArgumentException.class, () -> juego.jugar(0,2));
        juego.jugar(1,1);
        assertThrows(IllegalStateException.class, () -> juego.jugar(1,1));
        assertEquals('+', juego.siguienteTurno());
    }

    @Test
    public void adicionalTrasCadaVictoriaBloqueaOcupadasYLibresSinCambiarTurno() throws Exception {
        for (char ganador : new char[]{'X','+'}) {
            int[][][] partidas = ganador=='X' ? VICTORIAS_X : VICTORIAS_MAS;
            for (int[][] secuencia : partidas) {
                TresEnRaya juego = comprobarPartida(secuencia, ganador);
                char turnoEsperado = ganador=='X' ? '+' : 'X';
                for (int x=1; x<=3; x++) {
                    for (int y=1; y<=3; y++) {
                        final int fila=x, columna=y;
                        assertThrows(IllegalStateException.class, () -> juego.jugar(fila,columna));
                        assertEquals(turnoEsperado, juego.siguienteTurno());
                    }
                }
            }
        }
    }

    /** Cada secuencia es publica e independiente de la implementacion del motor. */
    private TresEnRaya comprobarPartida(int[][] movimientos, char ganador) throws Exception {
        TresEnRaya juego = new TresEnRaya();
        for (int i=0; i<movimientos.length; i++) {
            assertEquals("Turno antes del movimiento " + i, i%2==0 ? 'X' : '+', juego.siguienteTurno());
            String resultado = juego.jugar(movimientos[i][0],movimientos[i][1]);
            String esperado = i==movimientos.length-1 ? ganador + " es el ganador" : "No hay ganador";
            assertEquals("Resultado del movimiento " + i, esperado, resultado);
            assertEquals("Turno posterior " + i, i%2==0 ? '+' : 'X', juego.siguienteTurno());
        }
        return juego;
    }
}
