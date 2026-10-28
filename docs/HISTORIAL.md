# Historial y evidencia vigente

La evidencia definitiva está únicamente en `evidencia/actual/`.
El repositorio Git local conserva los commits reales y las ejecuciones previas,
incluidos los 16 ciclos de desarrollo TDD. No se publican esos registros como
si fueran resultados de la versión actual.

La base inicial se guardó en `base-didactica-v1`, antes de crear la ampliada.
La base revisada está en `base-didactica-revision-final`; solo cambia el comentario
inicial, sin modificar pruebas ni expectativas. Las suites finales son
verificaciones posteriores al motor, no ciclos RED-GREEN nuevos.

Para revisar el historial en el repositorio local:

```sh
git log --oneline --all
git show base-didactica-revision-final:src/test/java/com/ucb/tdd/TresEnRayaBaseTest.java
```

La carpeta `taller6-java` del ZIP es el árbol limpio preparado para publicar.
El ZIP no incluye `.git`, bundles históricos ni reportes de versiones previas.
