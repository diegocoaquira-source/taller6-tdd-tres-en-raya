# Referencia docente y reglas

Fuentes revisadas: TDD-main.zip, `3. TDD - CI CD(1).pdf` (páginas 2, 7-18),
y presentaciones de Introducción e Integración Continua.

## Requisitos obligatorios

Tablero 3x3; excepción por rango en ambos ejes o casilla ocupada; primer jugador
X; alternancia X/+; ausencia de ganador; victoria horizontal, vertical y en ambas
diagonales. API `jugar(int x,int y)` y `siguienteTurno()`, índices 1..3. En la
referencia, x es fila e y es columna. Se conservan sus tres retornos textuales.

## Error observado en el original

El original cambia `ultimoJugador` antes de comprobar ocupación. Si la casilla
está ocupada, lanza la excepción después de consumir el turno. Se volvió a
ejecutar una copia original temporal en esta verificación: sus 10 pruebas pasan,
pero una de las 3 regresiones falla al esperar + (43) y observar X (88).
El motor corregido pasa las 3 regresiones. Evidencia actual:
`evidencia/actual/referencia-docente.txt`, `referencia-regresion.txt` y
`jdk17-regresion.txt`.

No se copió la lógica del error. El motor valida antes de modificar el estado.
La suma de códigos de caracteres del original no mostró colisiones con su
alfabeto actual, pero se sustituyó por comparación directa para expresar la regla.

## Decisiones adicionales al enunciado

| Decisión | Comportamiento acordado |
|---|---|
| Rechazo sin cambios | Conserva turno y fichas |
| Tipo de excepción | IllegalArgumentException para rango, IllegalStateException para ocupación o fin |
| Después de ganar | Bloquea nuevas jugadas, extensión explícita |
| Tablero lleno sin ganador | El motor devuelve No hay ganador; la consola anuncia empate |
| Consulta tras victoria | siguienteTurno sigue alternando, aunque ya no admite jugadas |

Se conserva `throws Exception` en la firma pública. No se mezclan jugadores O,
coordenadas 0..2 ni métodos de la entrega anterior Python.
