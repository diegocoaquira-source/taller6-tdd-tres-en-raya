# Prompts realmente usados y procedencia

La solicitud inicial guió la base didáctica, la comparación y los
entregables. El prompt operativo de la sección 2 fue redactado, guardado y
aplicado por el mismo asistente en esta sesión después de congelar la base.
No se afirma una llamada a otro proveedor, otra sesión independiente o una
evaluación ciega. Ambas suites recibieron asistencia de IA.

## 1. Solicitud de generación inicial, transcripción literal

```text
Completa el taller de Tres en Raya y deja todos los archivos listos para subir a GitHub y entregar. No sigas haciendo preguntas para que yo proponga casos: genera los casos restantes y explícame brevemente su funcionamiento.

Mantén el enfoque del docente:

- Java compatible con Java 8.
- Maven y JUnit 4.
- Paquete com.ucb.tdd.
- Jugadores X y +.
- Coordenadas de 1 a 3.
- API jugar(int x, int y) y siguienteTurno().

1. Completa una suite base didáctica.\
   Escribe pruebas sencillas, explícitas y fáciles de explicar por un estudiante principiante: nombres descriptivos en español y comentarios de preparación, acción y comprobación. Evita abstracciones innecesarias.

Cubre:

- Coordenadas por debajo y por encima del rango en ambos ejes.
- Casilla ocupada.
- Primer turno de X y alternancia X/+.
- Conservación del turno al rechazar una jugada.
- Ausencia de ganador.
- Victorias horizontales, verticales y en ambas diagonales, para ambos jugadores.

No atribuyas estas pruebas a mi autoría independiente ni simules trabajo humano. Identifica la suite como “base didáctica elaborada con asistencia de IA”. Conserva por separado las pruebas del docente y los ejemplos guiados E01/E02.

2. Genera una suite comparativa de IA.\
   Guarda primero la versión de la suite base y luego genera otra suite separada, contra la misma API y las mismas reglas, buscando casos adicionales y comprobaciones más completas. Guarda el prompt exacto en PROMPTS.md. No debilites deliberadamente la suite base para favorecer a la otra.
3. Ejecuta y verifica.\
   Compila el proyecto y ejecuta las suites por separado y juntas. Intenta completar la verificación con Maven. Mantén documentadas las versiones reales de Java y JUnit utilizadas, y cualquier diferencia respecto de la referencia del docente.

Registra resultados reales. No inventes ejecuciones, porcentajes de cobertura ni ciclos RED–GREEN. Para pruebas añadidas después de implementar el comportamiento, indica que son verificaciones posteriores.

Si detectas errores del programa, corrígelos y vuelve a ejecutar las pruebas pertinentes. Si hay un bloqueo del entorno, deja comandos exactos para reproducir la verificación y señala qué quedó sin comprobar.

4. Prepara la comparación y el PDF final.\
   Compara la suite base asistida con la suite ampliada de IA:

- Reglas y escenarios cubiertos.
- Casos límite.
- Comprobación de excepciones y del estado posterior.
- Claridad, organización y mantenimiento.
- Ejemplos concretos de diferencias.
- Resultados obtenidos y limitaciones.

Explica con claridad que ambas suites recibieron asistencia de IA y que esta comparación no equivale a una comparación de autoría humana independiente frente a IA. Señala que, por ello, sigue pendiente ese requisito literal de la consigna. No presentes el taller como plenamente cumplido si ese punto no está resuelto.

El PDF debe ser legible, contener ejemplos de código breves y utilizar únicamente cifras verificadas. Revisa visualmente sus páginas.

5. Deja listo el paquete de entrega.\
   Incluye:

- Código fuente y pruebas.
- pom.xml.
- README con requisitos y comandos.
- PROMPTS.md con los prompts realmente usados.
- AUTORIA.md con las contribuciones identificadas.
- Presentación final en PDF.
- Evidencia de ejecución.
- GitHub Actions para ejecutar las pruebas con Maven.
- ZIP final.

Elimina del paquete archivos temporales, rutas personales innecesarias y resultados antiguos que puedan confundirse con los actuales. Conserva el historial real disponible y las referencias al docente.

6. Prepara la publicación.\
   No publiques todavía. Deja el repositorio listo para subir a GitHub, con instrucciones exactas para crear un repositorio público y subirlo. No inventes una URL ni afirmes que GitHub Actions pasó si no se ejecutó allí.

Al terminar, entrégame enlaces a los archivos finales y una lista breve de:

- Qué quedó completado y verificado.
- Qué no se pudo verificar.
- Qué debo hacer para subirlo.
- Qué requisito académico sigue pendiente por la procedencia de las pruebas.

Avanza con todo lo que puedas completar sin mi intervención.
```

## 2. Prompt operativo exacto de la suite ampliada

Se guardó antes de crear `TresEnRayaIATest.java`. La base ya estaba guardada en
el tag `base-didactica-v1` y se conservó sin cambios.

```text
Genera TresEnRayaIATest.java como una suite comparativa ampliada de IA en
Java compatible con Java 8 y JUnit 4.13. La base didactica ya se guardo en
el tag base-didactica-v1 y no debe modificarse para favorecer esta suite.
Ambas suites son asistidas por IA. No atribuyas autoría humana independiente.

Contrato obligatorio del curso: paquete com.ucb.tdd; clase TresEnRaya;
String jugar(int x,int y) throws Exception y char siguienteTurno(). Tablero
3x3, coordenadas 1..3 en ambos ejes, x=fila, y=columna. Jugadores X y +.
X comienza, alternan tras jugadas validas. Fuera de rango y ocupada lanzan
Exception. Una jugada rechazada conserva el turno. jugar devuelve exactamente
No hay ganador, X es el ganador o + es el ganador. Probar las 8 lineas para
ambos jugadores, usando secuencias legales y sin victorias previas.

Amplia escenarios sin reducir la base: valores Integer.MIN_VALUE y MAX_VALUE,
ambos ejes y estados de turno, inicio en cualquiera de las nueve casillas,
rechazos consecutivos, independencia de partidas, consultas repetidas sin
efectos, lineas mixtas sin ganador y continuidad del tablero tras errores.
Comprueba respuestas intermedias y turnos de cada secuencia ganadora.

Separa extensiones del contrato obligatorio: el motor usa
IllegalArgumentException para rango e IllegalStateException para ocupada y
para jugar despues de ganar. Bloquea despues de la primera victoria.
En empate el motor conserva No hay ganador. siguienteTurno sigue alternando
incluso despues de una victoria, aunque no admite nuevas jugadas. Prueba
victoria en novena jugada, victoria de + en octava y tablero lleno sin ganador.

Puedes agrupar datos repetidos y usar ayudantes privados breves para las
secuencias. Usa solo la API publica: no reflection, atributos privados ni
listas internas de lineas. No introduzcas requisitos de mensajes de error
no acordados ni cambies el programa para hacer pasar expectativas inventadas.

Estas son verificaciones posteriores a la implementacion. No inventes ciclos
RED-GREEN, cobertura, métricas ni ejecuciones. Cuenta métodos y subcasos por
separado. Ejecuta base y ampliada por separado y juntas contra el mismo motor.
```

## 3. Solicitud de revisión definitiva, transcripción literal

`	ext
Termina el taller de TDD del Tres en Raya usando el proyecto Java actual y la referencia del docente. Necesito los archivos definitivos, preparados para publicar y entregar. Avanza sin seguir pidiéndome que proponga casos.

CONFIGURACIÓN

- Java compatible con Java 8.
- Maven y JUnit 4.
- Paquete com.ucb.tdd.
- Jugadores X y +.
- Coordenadas del 1 al 3.
- Métodos jugar(int x, int y) y siguienteTurno().
- Conserva la compatibilidad con las pruebas del docente.

PRUEBAS BASE\
Completa y revisa una suite con estilo de estudiante principiante:

- Métodos cortos y nombres descriptivos en español.
- Preparación, acción y comprobación fáciles de reconocer.
- Valores concretos y aserciones claras.
- Sin abstracciones innecesarias ni comentarios artificiales.
- No introduzcas errores ni omisiones deliberadas para aparentar autoría humana.
- No incluyas afirmaciones de autoría en los comentarios del código.

Cubre coordenadas inválidas en ambos ejes, casillas ocupadas, primer turno, alternancia, conservación del turno tras errores, ausencia de ganador y todas las líneas ganadoras para ambos jugadores.

PRUEBAS AMPLIADAS\
Mantén otra suite separada con escenarios adicionales y comprobaciones más completas. Utiliza el mismo programa y contrato. Conserva las pruebas del docente por separado y guarda los prompts exactos utilizados.

VERIFICACIÓN

- Revisa y corrige el programa.
- Ejecuta Maven y las suites por separado y juntas.
- Comprueba que el JAR funciona.
- Si necesitas dependencias, intenta obtenerlas mediante las herramientas disponibles.
- No inventes resultados ni reutilices resultados antiguos como si correspondieran a la versión final.
- Documenta brevemente cualquier comprobación bloqueada.
- No fabriques ciclos TDD: distingue los ciclos registrados de las pruebas añadidas posteriormente.

PRESENTACIÓN PDF\
Prepara una presentación breve y legible con:

1. Objetivo y reglas del juego.
2. Herramientas y estructura.
3. Proceso TDD realmente realizado.
4. Suite base y suite ampliada.
5. Tabla de diferencias sustanciales.
6. Dos ejemplos concretos de código comparado.
7. Resultados de ejecución comprobados.
8. Conclusiones y limitaciones.

Usa lenguaje académico sencillo. Elimina las narraciones sobre conversaciones anteriores y las advertencias repetidas. Incluye una sola sección breve de metodología y contribuciones con información precisa, sin atribuirme autoría independiente que no esté acreditada. Revisa visualmente todas las páginas.

DOCUMENTACIÓN Y PAQUETE\
Actualiza:

- README.md con instalación, ejecución y estructura.
- PROMPTS.md con los prompts realmente utilizados.
- AUTORIA.md con contribuciones resumidas y exactas.
- Workflow de GitHub Actions para Maven y JDK 17.
- Evidencia vigente y coherente con el código final.

Genera un ZIP definitivo con código, pruebas, configuración, documentación y PDF. Excluye archivos temporales, cachés, rutas personales innecesarias y resultados obsoletos. Conserva las referencias al material del docente.

PUBLICACIÓN\
Deja el proyecto listo para un repositorio público de GitHub. Si tienes acceso autenticado y autorizado, publica el proyecto y verifica su URL y el resultado de Actions. Si no tienes acceso, entrega instrucciones exactas para subirlo y distingue ese pendiente. No inventes una URL.

AL TERMINAR\
Entrégame:

- ZIP definitivo.
- PDF definitivo.
- PROMPTS.md.
- URL pública, si se publicó.
- Una lista corta de lo verificado y lo pendiente.

No afirmes cumplimiento total de la comparación de pruebas propias frente a IA si ambas suites fueron elaboradas con asistencia. Completa igualmente todos los entregables técnicos posibles.
`
