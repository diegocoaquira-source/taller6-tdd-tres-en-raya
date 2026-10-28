package com.ucb.tdd;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TresEnRayaBaseTest {

    @Test
    public void xMenorQueUnoLanzaExcepcionYConservaX() throws Exception {
        // Preparacion
        TresEnRaya juego = new TresEnRaya();

        // Accion
        assertThrows(Exception.class, () -> juego.jugar(0, 2));

        // Comprobacion
        assertEquals('X', juego.siguienteTurno());
    }

    @Test
    public void xMayorQueTresLanzaExcepcionYConservaX() throws Exception {
        // Preparacion
        TresEnRaya juego = new TresEnRaya();

        // Accion
        assertThrows(Exception.class, () -> juego.jugar(4, 2));

        // Comprobacion
        assertEquals('X', juego.siguienteTurno());
    }

    @Test
    public void yMenorQueUnoLanzaExcepcionYConservaX() throws Exception {
        // Preparacion
        TresEnRaya juego = new TresEnRaya();

        // Accion
        assertThrows(Exception.class, () -> juego.jugar(2, 0));

        // Comprobacion
        assertEquals('X', juego.siguienteTurno());
    }

    @Test
    public void yMayorQueTresLanzaExcepcionYConservaX() throws Exception {
        // Preparacion
        TresEnRaya juego = new TresEnRaya();

        // Accion
        assertThrows(Exception.class, () -> juego.jugar(2, 4));

        // Comprobacion
        assertEquals('X', juego.siguienteTurno());
    }

    @Test
    public void casillaOcupadaLanzaExcepcionYConservaMas() throws Exception {
        // Preparacion
        TresEnRaya juego = new TresEnRaya();
        juego.jugar(2, 2);

        // Accion
        assertThrows(Exception.class, () -> juego.jugar(2, 2));

        // Comprobacion
        assertEquals('+', juego.siguienteTurno());
    }

    @Test
    public void primeraConsultaDeTurnoEsX() throws Exception {
        // Preparacion
        TresEnRaya juego = new TresEnRaya();

        // Accion
        char turno = juego.siguienteTurno();

        // Comprobacion
        assertEquals('X', turno);
    }

    @Test
    public void despuesDeXElTurnoEsMas() throws Exception {
        // Preparacion
        TresEnRaya juego = new TresEnRaya();

        // Accion
        juego.jugar(1, 1);

        // Comprobacion
        assertEquals('+', juego.siguienteTurno());
    }

    @Test
    public void despuesDeMasElTurnoEsX() throws Exception {
        // Preparacion
        TresEnRaya juego = new TresEnRaya();
        juego.jugar(1, 1);

        // Accion
        juego.jugar(2, 2);

        // Comprobacion
        assertEquals('X', juego.siguienteTurno());
    }

    @Test
    public void unaFichaNoTieneGanador() throws Exception {
        // Preparacion
        TresEnRaya juego = new TresEnRaya();

        // Accion
        String resultado = juego.jugar(3, 3);

        // Comprobacion
        assertEquals("No hay ganador", resultado);
    }

    @Test
    public void filaConFichasDistintasNoTieneGanador() throws Exception {
        // Preparacion
        TresEnRaya juego = new TresEnRaya();
        juego.jugar(1, 1);
        juego.jugar(1, 2);

        // Accion
        String resultado = juego.jugar(1, 3);

        // Comprobacion
        assertEquals("No hay ganador", resultado);
    }

    @Test
    public void rechazoDeEjeXConservaMasTrasJugadaValida() throws Exception {
        // Preparacion
        TresEnRaya juego = new TresEnRaya();
        juego.jugar(2, 2);

        // Accion
        assertThrows(Exception.class, () -> juego.jugar(4, 2));

        // Comprobacion
        assertEquals('+', juego.siguienteTurno());
        assertEquals("No hay ganador", juego.jugar(1, 1));
        assertEquals('X', juego.siguienteTurno());
    }

    @Test
    public void rechazoDeEjeYConservaMasTrasJugadaValida() throws Exception {
        // Preparacion
        TresEnRaya juego = new TresEnRaya();
        juego.jugar(2, 2);

        // Accion
        assertThrows(Exception.class, () -> juego.jugar(2, 0));

        // Comprobacion
        assertEquals('+', juego.siguienteTurno());
        assertEquals("No hay ganador", juego.jugar(1, 1));
        assertEquals('X', juego.siguienteTurno());
    }

    @Test
    public void xGanaEnPrimeraFila() throws Exception {
        // Preparacion
        TresEnRaya juego = new TresEnRaya();
        juego.jugar(1, 1);
        juego.jugar(2, 1);
        juego.jugar(1, 2);
        juego.jugar(2, 2);

        // Accion
        String resultado = juego.jugar(1, 3);

        // Comprobacion
        assertEquals("X es el ganador", resultado);
    }

    @Test
    public void xGanaEnSegundaFila() throws Exception {
        // Preparacion
        TresEnRaya juego = new TresEnRaya();
        juego.jugar(2, 1);
        juego.jugar(1, 1);
        juego.jugar(2, 2);
        juego.jugar(1, 2);

        // Accion
        String resultado = juego.jugar(2, 3);

        // Comprobacion
        assertEquals("X es el ganador", resultado);
    }

    @Test
    public void xGanaEnTerceraFila() throws Exception {
        // Preparacion
        TresEnRaya juego = new TresEnRaya();
        juego.jugar(3, 1);
        juego.jugar(1, 1);
        juego.jugar(3, 2);
        juego.jugar(1, 2);

        // Accion
        String resultado = juego.jugar(3, 3);

        // Comprobacion
        assertEquals("X es el ganador", resultado);
    }

    @Test
    public void xGanaEnPrimeraColumna() throws Exception {
        // Preparacion
        TresEnRaya juego = new TresEnRaya();
        juego.jugar(1, 1);
        juego.jugar(1, 2);
        juego.jugar(2, 1);
        juego.jugar(1, 3);

        // Accion
        String resultado = juego.jugar(3, 1);

        // Comprobacion
        assertEquals("X es el ganador", resultado);
    }

    @Test
    public void xGanaEnSegundaColumna() throws Exception {
        // Preparacion
        TresEnRaya juego = new TresEnRaya();
        juego.jugar(1, 2);
        juego.jugar(1, 1);
        juego.jugar(2, 2);
        juego.jugar(1, 3);

        // Accion
        String resultado = juego.jugar(3, 2);

        // Comprobacion
        assertEquals("X es el ganador", resultado);
    }

    @Test
    public void xGanaEnTerceraColumna() throws Exception {
        // Preparacion
        TresEnRaya juego = new TresEnRaya();
        juego.jugar(1, 3);
        juego.jugar(1, 1);
        juego.jugar(2, 3);
        juego.jugar(1, 2);

        // Accion
        String resultado = juego.jugar(3, 3);

        // Comprobacion
        assertEquals("X es el ganador", resultado);
    }

    @Test
    public void xGanaEnDiagonalPrincipal() throws Exception {
        // Preparacion
        TresEnRaya juego = new TresEnRaya();
        juego.jugar(1, 1);
        juego.jugar(1, 2);
        juego.jugar(2, 2);
        juego.jugar(1, 3);

        // Accion
        String resultado = juego.jugar(3, 3);

        // Comprobacion
        assertEquals("X es el ganador", resultado);
    }

    @Test
    public void xGanaEnDiagonalOpuesta() throws Exception {
        // Preparacion
        TresEnRaya juego = new TresEnRaya();
        juego.jugar(1, 3);
        juego.jugar(1, 1);
        juego.jugar(2, 2);
        juego.jugar(1, 2);

        // Accion
        String resultado = juego.jugar(3, 1);

        // Comprobacion
        assertEquals("X es el ganador", resultado);
    }

    @Test
    public void masGanaEnPrimeraFila() throws Exception {
        // Preparacion
        TresEnRaya juego = new TresEnRaya();
        juego.jugar(2, 1);
        juego.jugar(1, 1);
        juego.jugar(2, 2);
        juego.jugar(1, 2);
        juego.jugar(3, 1);

        // Accion
        String resultado = juego.jugar(1, 3);

        // Comprobacion
        assertEquals("+ es el ganador", resultado);
    }

    @Test
    public void masGanaEnSegundaFila() throws Exception {
        // Preparacion
        TresEnRaya juego = new TresEnRaya();
        juego.jugar(1, 1);
        juego.jugar(2, 1);
        juego.jugar(1, 2);
        juego.jugar(2, 2);
        juego.jugar(3, 1);

        // Accion
        String resultado = juego.jugar(2, 3);

        // Comprobacion
        assertEquals("+ es el ganador", resultado);
    }

    @Test
    public void masGanaEnTerceraFila() throws Exception {
        // Preparacion
        TresEnRaya juego = new TresEnRaya();
        juego.jugar(1, 1);
        juego.jugar(3, 1);
        juego.jugar(1, 2);
        juego.jugar(3, 2);
        juego.jugar(2, 1);

        // Accion
        String resultado = juego.jugar(3, 3);

        // Comprobacion
        assertEquals("+ es el ganador", resultado);
    }

    @Test
    public void masGanaEnPrimeraColumna() throws Exception {
        // Preparacion
        TresEnRaya juego = new TresEnRaya();
        juego.jugar(1, 2);
        juego.jugar(1, 1);
        juego.jugar(1, 3);
        juego.jugar(2, 1);
        juego.jugar(2, 2);

        // Accion
        String resultado = juego.jugar(3, 1);

        // Comprobacion
        assertEquals("+ es el ganador", resultado);
    }

    @Test
    public void masGanaEnSegundaColumna() throws Exception {
        // Preparacion
        TresEnRaya juego = new TresEnRaya();
        juego.jugar(1, 1);
        juego.jugar(1, 2);
        juego.jugar(1, 3);
        juego.jugar(2, 2);
        juego.jugar(2, 1);

        // Accion
        String resultado = juego.jugar(3, 2);

        // Comprobacion
        assertEquals("+ es el ganador", resultado);
    }

    @Test
    public void masGanaEnTerceraColumna() throws Exception {
        // Preparacion
        TresEnRaya juego = new TresEnRaya();
        juego.jugar(1, 1);
        juego.jugar(1, 3);
        juego.jugar(1, 2);
        juego.jugar(2, 3);
        juego.jugar(2, 1);

        // Accion
        String resultado = juego.jugar(3, 3);

        // Comprobacion
        assertEquals("+ es el ganador", resultado);
    }

    @Test
    public void masGanaEnDiagonalPrincipal() throws Exception {
        // Preparacion
        TresEnRaya juego = new TresEnRaya();
        juego.jugar(1, 2);
        juego.jugar(1, 1);
        juego.jugar(1, 3);
        juego.jugar(2, 2);
        juego.jugar(2, 1);

        // Accion
        String resultado = juego.jugar(3, 3);

        // Comprobacion
        assertEquals("+ es el ganador", resultado);
    }

    @Test
    public void masGanaEnDiagonalOpuesta() throws Exception {
        // Preparacion
        TresEnRaya juego = new TresEnRaya();
        juego.jugar(1, 1);
        juego.jugar(1, 3);
        juego.jugar(1, 2);
        juego.jugar(2, 2);
        juego.jugar(2, 1);

        // Accion
        String resultado = juego.jugar(3, 1);

        // Comprobacion
        assertEquals("+ es el ganador", resultado);
    }
}
