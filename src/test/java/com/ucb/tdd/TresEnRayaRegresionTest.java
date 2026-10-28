package com.ucb.tdd;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TresEnRayaRegresionTest {
    @Test
    public void rechazarOcupadaConservaTurnoYJugadaSiguiente() throws Exception {
        // Preparación
        TresEnRaya juego = new TresEnRaya();
        juego.jugar(1, 1);
        char turnoAntes = juego.siguienteTurno();
        // Acción: Exception permite comprobar también la referencia sin cambiarla.
        assertThrows(Exception.class, () -> juego.jugar(1, 1));
        // Comprobación del estado observable y de la siguiente jugada válida.
        assertEquals(turnoAntes, juego.siguienteTurno());
        juego.jugar(2, 1);
        juego.jugar(1, 2);
        juego.jugar(2, 2);
        juego.jugar(3, 3);
        assertEquals("+ es el ganador", juego.jugar(2, 3));
    }

    @Test
    public void fueraDeRangoEnCadaEjeNoConsumeTurnoNiOcupaCasillas() throws Exception {
        TresEnRaya juego = new TresEnRaya();
        juego.jugar(1, 1);
        assertThrows(Exception.class, () -> juego.jugar(0, 2));
        assertThrows(Exception.class, () -> juego.jugar(2, 4));
        assertEquals('+', juego.siguienteTurno());
        assertEquals("No hay ganador", juego.jugar(2, 2));
        assertEquals('X', juego.siguienteTurno());
    }

    @Test
    public void consultarSiguienteTurnoVariasVecesNoLoModifica() throws Exception {
        TresEnRaya juego = new TresEnRaya();
        assertEquals('X', juego.siguienteTurno());
        assertEquals('X', juego.siguienteTurno());
        juego.jugar(3, 3);
        assertEquals('+', juego.siguienteTurno());
        assertEquals('+', juego.siguienteTurno());
    }
}
