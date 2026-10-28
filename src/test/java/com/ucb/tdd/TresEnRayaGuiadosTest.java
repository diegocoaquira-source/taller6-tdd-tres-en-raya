package com.ucb.tdd;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TresEnRayaGuiadosTest {

    @Test
    public void e01_coordenadaXFueraDeRangoNoConsumeTurno() {
        // Preparacion: partida nueva.
        TresEnRaya juego = new TresEnRaya();

        // Accion y comprobacion de la excepcion.
        assertThrows(Exception.class, () -> juego.jugar(4, 2));

        // Comprobacion del turno despues del rechazo.
        assertEquals('X', juego.siguienteTurno());
    }

    @Test
    public void e02_coordenadaYMenorUnoNoConsumeTurno() {
        // Preparacion: partida nueva.
        TresEnRaya juego = new TresEnRaya();

        // Accion y comprobacion: y = 0 esta fuera del rango 1 a 3.
        assertThrows(Exception.class, () -> juego.jugar(2, 0));

        // Comprobacion del estado despues del rechazo.
        assertEquals('X', juego.siguienteTurno());
    }
}
