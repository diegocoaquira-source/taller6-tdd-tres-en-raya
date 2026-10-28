package com.ucb.tdd;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TresEnRayaTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    private TresEnRaya tresEnRaya;
    
    
    public final void before(){
        
    }
    
    
    @Test
    public void cuandoFueraXlanzarExcepcion() throws Exception{
        
        //1. Preparacion de la prueba
        tresEnRaya = new TresEnRaya();
        System.out.println("Inicializando Prueba.... " + tresEnRaya.toString());

        //2. Logica de la prueba
        
        //3. Verificacion o Assert
        exception.expect(Exception.class);
        tresEnRaya.jugar(5,2);
    }
    
    @Test
    public void cuandoFueraYlanzarExcepcion() throws Exception{
        //1. Preparacion de la prueba
        tresEnRaya = new TresEnRaya();
        System.out.println("Inicializando Prueba.... " + tresEnRaya.toString());

        //2. Logica de la prueba
        
        //3. Verificacion o Assert
        exception.expect(Exception.class);
        tresEnRaya.jugar(2,5);
    }
    
    @Test
    public void cuandoLugarOcupadoLanzarExcepcion()throws Exception {
        //1. Preparacion de la prueba
        tresEnRaya = new TresEnRaya();
        System.out.println("Inicializando Prueba.... " + tresEnRaya.toString());
        
        //2. Logica de la prueba
        tresEnRaya.jugar(1,3);
        
        //3. Verificacion o Assert
        exception.expect(Exception.class);
        tresEnRaya.jugar(1,3);
        
    }
    
    @Test
    public void siPrimerTurnoEntoncesJuegaX(){
        //1. Preparacion de la prueba
        tresEnRaya = new TresEnRaya();
        System.out.println("Inicializando Prueba.... " + tresEnRaya.toString());

        //3. Verificacion o Assert
        assertEquals('X',tresEnRaya.siguienteTurno());
        
    }
    
    @Test
    public void siPrimerTurnoXSiguienteMas() throws Exception{
        //1. Preparacion de la prueba
        tresEnRaya = new TresEnRaya();
        System.out.println("Inicializando Prueba.... " + tresEnRaya.toString());
        
        tresEnRaya.jugar(2, 1); //X
        assertEquals('+',tresEnRaya.siguienteTurno());
        
        tresEnRaya.jugar(2, 2); //+
        assertEquals('X',tresEnRaya.siguienteTurno());
        
        
    }
    
    @Test
    public void cuandoNoHayGanador() throws Exception{
        //1. Preparacion de la prueba
        tresEnRaya = new TresEnRaya();
        System.out.println("Inicializando Prueba.... " + tresEnRaya.toString());
        String  resultado = tresEnRaya.jugar(2, 1); //X
        assertEquals("No hay ganador", resultado);
        
    }
    
    @Test
    public void siLlenaColumnaEntoncesGana()  throws Exception{
        //1. Preparacion de la prueba
        tresEnRaya = new TresEnRaya();
        System.out.println("Inicializando Prueba.... " + tresEnRaya.toString());
        
        tresEnRaya.jugar(1, 1);//X
        tresEnRaya.jugar(1, 2);//+
        tresEnRaya.jugar(2, 1);//X
        tresEnRaya.jugar(2, 2);//+
        String  resultado = tresEnRaya.jugar(3, 1); //X
        assertEquals("X es el ganador", resultado);
    }
    
    @Test
    public void siLlenaFilaEntoncesGana() throws Exception{
        //1. Preparacion de la prueba
        tresEnRaya = new TresEnRaya();
        System.out.println("Inicializando Prueba.... " + tresEnRaya.toString());

        tresEnRaya.jugar(2, 1);//X
        tresEnRaya.jugar(1, 1);//+
        tresEnRaya.jugar(3, 1);//X
        tresEnRaya.jugar(1, 2);//+
        tresEnRaya.jugar(2, 2);//X
        String  resultado = tresEnRaya.jugar(1, 3); //+
        assertEquals("+ es el ganador", resultado);
    }
    
    @Test
    public void siLlenaDiagonalEntoncesGana() throws Exception{
        //1. Preparacion de la prueba
        tresEnRaya = new TresEnRaya();
        System.out.println("Inicializando Prueba.... " + tresEnRaya.toString());

        tresEnRaya.jugar(1, 1);//X
        tresEnRaya.jugar(1, 2);//+
        tresEnRaya.jugar(2, 2);//X
        tresEnRaya.jugar(1, 3);//+
        String  resultado = tresEnRaya.jugar(3, 3);//X
        assertEquals("X es el ganador", resultado);
    }
    
    @Test
    public void siLlenaDiagonalOpuestaEntoncesGana() throws Exception{
        //1. Preparacion de la prueba
        tresEnRaya = new TresEnRaya();
        System.out.println("Inicializando Prueba.... " + tresEnRaya.toString());
        
        tresEnRaya.jugar(1, 2);//X
        tresEnRaya.jugar(1, 3);//+
        tresEnRaya.jugar(1, 1);//X
        tresEnRaya.jugar(2, 2);//+
        tresEnRaya.jugar(2, 3);//X
        String  resultado = tresEnRaya.jugar(3, 1);//+
        assertEquals("+ es el ganador", resultado);
    }
}
