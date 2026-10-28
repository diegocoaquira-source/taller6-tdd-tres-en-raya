package com.ucb.tdd;

import org.junit.Test;
import static org.junit.Assert.*;

public class TresEnRayaDesarrolloTest {
    @Test
    public void c01_primerTurnoX() throws Exception {
        TresEnRaya juego = new TresEnRaya();
        assertEquals('X', juego.siguienteTurno());
    }

    @Test
    public void c02_rechazaXMenorUno() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> new TresEnRaya().jugar(0, 2));
    }

    @Test
    public void c03_rechazaXMayorTres() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> new TresEnRaya().jugar(4, 2));
    }

    @Test
    public void c04_rechazaYMenorUno() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> new TresEnRaya().jugar(2, 0));
    }

    @Test
    public void c05_rechazaYMayorTres() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> new TresEnRaya().jugar(2, 4));
    }

    @Test
    public void c06_unaJugadaNoTieneGanador() throws Exception {
        assertEquals("No hay ganador", new TresEnRaya().jugar(1, 1));
    }

    @Test
    public void c07_rechazaCasillaOcupada() throws Exception {
        TresEnRaya j = new TresEnRaya();
        j.jugar(1, 1);
        assertThrows(IllegalStateException.class, () -> j.jugar(1, 1));
    }

    @Test
    public void c08_despuesXJuegaMas() throws Exception {
        TresEnRaya j = new TresEnRaya();
        j.jugar(1, 1);
        assertEquals('+', j.siguienteTurno());
    }

    @Test
    public void c09_despuesMasJuegaX() throws Exception {
        TresEnRaya j = new TresEnRaya();
        j.jugar(1, 1);
        j.jugar(2, 1);
        assertEquals('X', j.siguienteTurno());
    }

    @Test
    public void c10_ganaHorizontalPrimera() throws Exception {
        TresEnRaya j = new TresEnRaya();
        j.jugar(1,1);
        j.jugar(2,1);
        j.jugar(1,2);
        j.jugar(2,2);
        assertEquals("X es el ganador", j.jugar(1,3));
    }

    @Test
    public void c11_ganaHorizontalOtraFila() throws Exception {
        TresEnRaya j = new TresEnRaya();
        j.jugar(2,1);
        j.jugar(1,1);
        j.jugar(2,2);
        j.jugar(1,2);
        assertEquals("X es el ganador", j.jugar(2,3));
    }

    @Test
    public void c12_ganaVertical() throws Exception {
        TresEnRaya j = new TresEnRaya();
        j.jugar(1,1);
        j.jugar(1,2);
        j.jugar(2,1);
        j.jugar(2,2);
        assertEquals("X es el ganador", j.jugar(3,1));
    }

    @Test
    public void c13_ganaDiagonalPrincipal() throws Exception {
        TresEnRaya j = new TresEnRaya();
        j.jugar(1,1);
        j.jugar(1,2);
        j.jugar(2,2);
        j.jugar(1,3);
        assertEquals("X es el ganador", j.jugar(3,3));
    }

    @Test
    public void c14_ganaDiagonalOpuesta() throws Exception {
        TresEnRaya j = new TresEnRaya();
        j.jugar(1,3);
        j.jugar(1,2);
        j.jugar(2,2);
        j.jugar(1,1);
        assertEquals("X es el ganador", j.jugar(3,1));
    }

    @Test
    public void c15_tambienGanaMas() throws Exception {
        TresEnRaya j = new TresEnRaya();
        j.jugar(2,1);
        j.jugar(1,1);
        j.jugar(3,1);
        j.jugar(1,2);
        j.jugar(2,2);
        assertEquals("+ es el ganador", j.jugar(1,3));
    }

    @Test
    public void c16_adicionalBloqueaTrasVictoria() throws Exception {
        TresEnRaya j = new TresEnRaya();
        j.jugar(1,1);
        j.jugar(2,1);
        j.jugar(1,2);
        j.jugar(2,2);
        j.jugar(1,3);
        assertThrows(IllegalStateException.class, () -> j.jugar(3,3));
    }

}
