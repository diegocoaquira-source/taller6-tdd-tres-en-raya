# Metodología y contribuciones

Las suites base y ampliada recibieron asistencia de IA. No se acredita autoría
humana independiente; por ello sigue pendiente el requisito literal de comparar
pruebas propias frente a IA.

| Elemento | Contribución |
|---|---|
| Reglas, API y configuración de referencia | Material del docente, ZIP TDD-main y diapositivas TDD |
| TresEnRayaTest.java | Copia sin cambios de las 10 pruebas del docente |
| Motor, consola y documentación | Elaboración y revisión con asistencia de IA |
| TresEnRayaBaseTest.java | 28 pruebas didácticas asistidas, explícitas y con estructura preparación/acción/comprobación |
| TresEnRayaIATest.java | 16 métodos ampliados de IA, con datos agrupados y comprobaciones intermedias |
| TresEnRayaGuiadosTest.java | E01/E02: ejemplos guiados por IA, separados de la comparación |
| Desarrollo y regresión | 16 pruebas de desarrollo y 3 regresiones conservadas por separado |

El historial disponible conserva 16 ciclos TDD anteriores. Las suites base y
ampliada se añadieron después del motor: se registran como verificaciones
posteriores, no como nuevos ciclos RED-GREEN.

La base inicial se guardó antes de generar la ampliada en `base-didactica-v1`.
La revisión definitiva elimina únicamente su comentario de autoría, sin cambiar
casos ni expectativas. Queda identificada por `base-didactica-revision-final` y
por su huella en `evidencia/actual/resultados.json`. No se introdujeron omisiones
o defectos para favorecer la suite ampliada.

Los prompts operativos están en `PROMPTS.md`. La referencia docente se conserva
separada del programa y no se reutilizan sus resultados target como evidencia.
