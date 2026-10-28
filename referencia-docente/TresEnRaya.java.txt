package com.ucb.tdd;

public class TresEnRaya {
    private Character[][] tablero = {{'0', '0', '0'}, 
                                     {'0', '0', '0'}, 
                                     {'0', '0', '0'}};
    
    private char ultimoJugador = '+';
    
    private final int LIMITE = 3;
    
    
    public String jugar(int x, int y) throws Exception{
        
        verificarEje(x);
        verificarEje(y);
        
        ultimoJugador = siguienteTurno();
        asignarPosicion(x, y, ultimoJugador);
        
        if (hayGanador()) {
            return ultimoJugador+" es el ganador";
        }
        
        return "No hay ganador";
        
    }

    private boolean hayGanador() {
        
        int acumulado = ultimoJugador * LIMITE;
        //System.out.println("Numero Ascii:: "+acumulado);
        
        //Validacion por columna
        for (int i = 0; i < LIMITE; i++) {
            if (tablero[0][i] + tablero[1][i] + tablero[2][i] == acumulado) {
                return true;
            }
            if (tablero[i][0] + tablero[i][1] + tablero[i][2] == acumulado) {
                return true;
            }
        }
        
        if (tablero[LIMITE-3][LIMITE-3] + tablero[LIMITE-2][LIMITE-2] + tablero[LIMITE-1][LIMITE-1]  == acumulado) {
            return true;
        }
        if (tablero[LIMITE-1][LIMITE-3] + tablero[LIMITE-2][LIMITE-2] + tablero[LIMITE-3][LIMITE-1]  == acumulado) {
            return true;
        }

        return false;
    }
    
    private void asignarPosicion(int x, int y, char ultimoJugador) throws Exception {
        
        if(tablero[x-1][y-1] != '0'){
            throw new Exception("La posicion ya esta ocupada");
        } else{
            tablero[x-1][y-1] = ultimoJugador;
            System.out.println("Ocupando posicion: "+x+", "+y);
        }
    }
    
    private void verificarEje(int eje) throws Exception{
        if(eje < 1 || eje > 3){
            throw new Exception("La coordenada "+eje+" esta fuera de lugar");
        }
        
    }
    
    
    
    public char siguienteTurno(){
        if(ultimoJugador == 'X'){
            return '+';
        } 
        return 'X';
        
    }
}
