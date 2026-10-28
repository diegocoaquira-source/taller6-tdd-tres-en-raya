# Comparación: base asistida y suite ampliada de IA

Las contribuciones y el alcance académico se resumen en `AUTORIA.md`.

## Diseño de la comparación

La base inicial se congeló en `base-didactica-v1` antes de generar la ampliada.
La revisión `base-didactica-revision-final` solo elimina el comentario de autoría;
conserva los mismos casos y expectativas. Las dos
se ejecutaron contra la misma compilación del motor, sin cambiarlo entre suites.
El verificador comprueba la huella de la base y del motor. No se afirma ausencia
de toda influencia de contexto: ambas se elaboraron en la misma sesión de IA.

| Dimensión | Base didáctica asistida | Ampliada de IA |
|---|---|---|
| Organización | 28 métodos explícitos, comentarios preparación/acción/comprobación | 16 métodos, datos agrupados y un ayudante para secuencias |
| Rango | 0 y 4 en ambos ejes | Lo anterior, extremos int y ambos ejes inválidos, en dos estados de turno |
| Ocupación | Excepción y conservación de turno | Rechazos repetidos, ambos turnos, continuidad y ficha original |
| Turnos | X inicial y alternancia X/+ | Añade consultas repetidas, seguimiento por jugada e independencia de partidas |
| Victoria | 8 líneas para X y 8 para +, ejemplos explícitos | Mismas 16 líneas, respuestas y turnos intermedios, novena y octava jugada |
| Ausencia de ganador | Una ficha y fila mixta | Añade prefijos completos de partidas y tablero lleno sin línea |
| Excepciones | Exception, como exige el curso | Exception en reglas obligatorias; tipos concretos en un test adicional separado |
| Mantenimiento | Fácil de leer caso por caso, repite preparación | Reduce repetición, exige entender bucles y un ayudante |

28 métodos frente a 16 no significa mejor o peor calidad. En la ampliada, un
método puede ejecutar varios escenarios. Los números de métodos y los escenarios
agrupados se registran por separado, sin sumarlos como si fueran equivalentes.

## Ejemplo concreto: estado después de una casilla ocupada

La base exige la excepción y verifica el turno `+`. La ampliada, además, continúa
una partida que debe ganar X usando su ficha original. Si la jugada rechazada
borrara esa ficha pero conservara el turno, el primer chequeo no lo observaría.
Esto se confirmó con M1 en una copia temporal del motor.

## Defectos sembrados, muestra ilustrativa

| Defecto temporal | Base | Ampliada |
|---|---|---|
| M1: borra ficha ocupada antes de lanzar excepción | No detecta | Detecta |
| M2: omite diagonal opuesta | Detecta | Detecta |
| M3: consume turno al rechazar ocupada | Detecta | Detecta |

Cada copia compiló correctamente antes de ejecutar las suites. Los cambios
exactos están en `scripts/verificar_final.py`, y las salidas reales en
`evidencia/actual/M*-*.txt`. El motor original no se modifica. Es una muestra
manual pequeña, seleccionada para explicar diferencias concretas. No es una
medida universal de calidad ni un porcentaje de cobertura.

## Resultados reales

| Suite | Métodos ejecutados | JDK 17 / JUnit 4.13.2 | Java 8 / JUnit 4.13.2 |
|---|---:|---|---|
| Base asistida | 28 | PASS | PASS |
| Ampliada IA | 16 | PASS | PASS |
| Base y ampliada juntas | 44 | PASS | PASS |
| Todas las suites del proyecto | 75 | PASS | PASS |

Compilación: javac 17.0.6 con `--release 8`, bytecode major 52. Las ejecuciones
usaron JDK 17.0.6 y JRE 1.8.0_241. JUnit local fue 4.13.2, mientras que el POM
conserva 4.13. Maven 3.9.16 se instaló y ejecutó localmente, pero no pudo
descargar plugins por permisos de red. GitHub Actions sí verificó JUnit 4.13
y Maven con JDK 17: 28, 16, 44 y 75 pruebas respectivamente, sin fallos ni errores.
Ejecución 40772099194; extracto de logs en `evidencia/actual/github-actions.txt`.

## Extensiones y límites

Bloqueo después de ganar, tipos concretos de excepción, empate expresado como
`No hay ganador` y consulta de turno tras victoria son decisiones explícitas del
programa, diferenciadas de las diapositivas. La base cubre los requisitos del
curso; la ampliada incluye pruebas adicionales de estas decisiones.

No se midió cobertura de líneas o ramas. No se demuestra corrección total ni
superioridad general de IA. El alcance académico
se describe una sola vez en la sección de contribuciones de la presentación.
