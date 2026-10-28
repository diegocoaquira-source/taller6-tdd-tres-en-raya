# Taller 6: Tres en Raya con TDD

Java compatible con Java 8, Maven y JUnit 4.13. Paquete `com.ucb.tdd`, jugadores
X y +, coordenadas de 1 a 3. Se conserva la API del docente:

```java
String jugar(int x, int y) throws Exception
char siguienteTurno()
```

## Requisitos e instalación

1. Un JDK con compilador. Se recomienda JDK 17, como el workflow de GitHub.
2. Maven 3: descarga el Binary ZIP desde https://maven.apache.org/download.cgi,
   extráelo y añade su carpeta `bin` al PATH.
3. Configura `JAVA_HOME` con la carpeta del JDK, no la de un JRE.
4. Abre en VS Code la carpeta que contiene `pom.xml`.

Comprueba la instalación en una terminal nueva:

```sh
java -version
javac -version
mvn -version
```

En este equipo Windows ya existían Git, VS Code, JRE 1.8.0_241 y JDK 17.0.6.
Se instaló Maven 3.9.16 portable dentro de `.local-build/tools`, comprobando
SHA-512 oficial. Maven se ejecutó, pero la descarga de plugins fue rechazada
por los permisos de red del entorno. La alternativa local usa JUnit 4.13.2.
GitHub Actions sí completó Maven con JUnit 4.13 exacto y JDK 17.

VS Code tiene recomendaciones de extensiones Java en `.vscode/extensions.json`
y una configuración «Jugar Tres en Raya». La instalación de extensiones quedó
bloqueada por EACCES al acceder al Marketplace; su funcionamiento en la interfaz
no está verificado. No se cambiaron políticas de seguridad.

## Compilar, probar y ejecutar

Desde la carpeta del proyecto:

```sh
mvn -B clean verify
mvn -B "-Dtest=TresEnRayaBaseTest" test
mvn -B "-Dtest=TresEnRayaIATest" test
mvn -B "-Dtest=TresEnRayaBaseTest,TresEnRayaIATest" test
java -jar target/taller6-tres-en-raya-1.0-SNAPSHOT.jar
```

El JAR entregado ya se generó y ejecutó localmente, sin depender de Maven:

```sh
java -jar dist/tres-en-raya.jar
```

Escribe fila y columna separadas por un espacio, por ejemplo `1 3`. `q` sale.
El motor valida rango y ocupación antes de colocar una ficha o cambiar el turno.
Las victorias se detectan por igualdad de fichas en las ocho líneas.

En Windows, la alternativa comprobada detecta JDK/JUnit de PyCharm o utiliza
las variables `JAVA_HOME` y `JUNIT_JAR`:

```powershell
.\scripts\verificar-local.cmd --base
.\scripts\verificar-local.cmd --ia
.\scripts\verificar-local.cmd --ambas
.\scripts\verificar-local.cmd --guiados
.\scripts\verificar-local.cmd
```

Para regenerar la evidencia y las evaluaciones temporales, con Python 3.9+:

```sh
python scripts/verificar_final.py
```

Acepta `--jdk-home` y `--junit-jar` si no detecta las herramientas. Compila una
sola versión del motor para ambas suites, registra sus versiones y huellas,
ejecuta las suites separadas y juntas, y comprueba consola y JAR. Las copias
defectuosas de evaluación nunca modifican el motor entregado.

## Estructura

| Ruta | Contenido |
|---|---|
| src/main/java/com/ucb/tdd | Motor TresEnRaya y consola Main |
| src/test/java/com/ucb/tdd/TresEnRayaBaseTest.java | 28 pruebas explícitas con preparación, acción y comprobación |
| src/test/java/com/ucb/tdd/TresEnRayaIATest.java | 16 métodos ampliados, con escenarios agrupados |
| TresEnRayaTest.java en src/test | 10 pruebas del docente, sin cambios |
| TresEnRayaGuiadosTest.java en src/test | E01 y E02 separados |
| TresEnRayaDesarrolloTest.java y TresEnRayaRegresionTest.java | 16 pruebas de desarrollo y 3 regresiones |
| evidencia/actual | Evidencia exclusiva de la versión definitiva |
| docs/COMPARACION.md | Diferencias y ejemplos detallados |
| docs/REVISION_REFERENCIA.md | Reglas obligatorias y decisiones adicionales |
| PROMPTS.md y AUTORIA.md | Instrucciones utilizadas y contribuciones |
| presentacion_final.pdf | Presentación breve de nueve páginas |
| .github/workflows/maven.yml | Automatización con Maven y JDK 17 |

La base utiliza valores concretos y cubre todas las líneas para ambos jugadores.
La ampliada agrupa variaciones y añade comprobaciones de estado, consultas sin
efectos, extremos int, partidas independientes y finales de partida.

## Resultados vigentes

| Suite | Métodos ejecutados | Java 8 | JDK 17 |
|---|---:|---|---|
| Base | 28 | PASS | PASS |
| Ampliada | 16 | PASS | PASS |
| Ambas | 44 | PASS | PASS |
| Todas las suites | 75 | PASS | PASS |

Entorno real: javac 17.0.6 con `--release 8`, bytecode major 52; ejecución con
JRE 1.8.0_241 y JDK 17.0.6, JUnit local 4.13.2. No se midió cobertura de líneas.
GitHub Actions verificó Maven y JUnit 4.13 con 75 pruebas, cero fallos y errores.
La ejecución local de Maven quedó bloqueada al descargar plugins. Detalles y logs:
`evidencia/actual/resultados.json`. No se reutilizan informes target del docente.

## Metodología y contribuciones

El historial conserva 16 ciclos TDD anteriores. Las suites comparadas son
verificaciones posteriores al motor. Ambas recibieron asistencia de IA; la
comparación literal de autoría humana independiente frente a IA sigue pendiente.
El detalle resumido está en `AUTORIA.md`. El historial real se conserva en el
repositorio local; el ZIP contiene solo el estado vigente. Ver `docs/HISTORIAL.md`.

## Repositorio público y entrega

Proyecto publicado en la rama `main`, con `pom.xml`, `src` y `.github` en la raíz:

https://github.com/diegocoaquira-source/taller6-tdd-tres-en-raya

Ejecución Maven verificada (código del commit `6abd120`):
https://github.com/diegocoaquira-source/taller6-tdd-tres-en-raya/actions/runs/40772099194

Se ejecutaron por separado la base (28), la ampliada (16), ambas (44) y todas
las suites (75). El detalle verificable está en `evidencia/actual/github-actions.txt`.
La publicación remota comenzó con una instantánea del proyecto; el historial
anterior y sus etiquetas siguen en el repositorio local, según `docs/HISTORIAL.md`.

Para abrir el proyecto en otra computadora sin usar terminal: visita el enlace,
selecciona **Code → Download ZIP**, extrae y abre la carpeta en VS Code. Instala
**Extension Pack for Java**, desde el Marketplace oficial; abre `pom.xml` y permite
la importación Maven. En el panel **Testing** aparecen las suites; el botón de
reproducción de `Main.java` inicia el juego. Esto requiere acceso de red a Maven.
Los comandos anteriores quedan como referencia reproducible.

Se intentó abrir Chrome automáticamente, pero la política de ejecución rechazó
el lanzamiento. No se pudo confirmar la apertura de VS Code ni del PDF en una
ventana visible. El juego se inició en una consola Java, además de las ejecuciones
automatizadas comprobadas. Estos límites de interfaz no afectan al resultado de CI.

Referencia: https://github.com/gpalabral/TDD y diapositivas TDD, páginas 7–18.
