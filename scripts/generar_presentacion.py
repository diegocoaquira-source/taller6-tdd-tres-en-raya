"""Presentación de resultados: generación PDF con ReportLab."""
from pathlib import Path
from xml.sax.saxutils import escape
import json
from reportlab.pdfgen import canvas
from reportlab.pdfbase import pdfmetrics
from reportlab.pdfbase.ttfonts import TTFont
from reportlab.platypus import Paragraph, Table, TableStyle
from reportlab.lib.styles import ParagraphStyle
from reportlab.lib import colors

ROOT=Path(__file__).resolve().parents[1]
W,H=960,540
INK=colors.HexColor('#163246')
ACCENT=colors.HexColor('#007E80')
GRAY=colors.HexColor('#526977')
LIGHT=colors.HexColor('#F0F4F5')


def main():
    report=json.loads((ROOT/'evidencia/actual/resultados.json').read_text(encoding='utf-8'))
    suites=report['resultados']['jdk17']['suites']
    assert all(s['resultado']=='PASS' for s in suites.values())
    for name,file in [('Texto','arial.ttf'),('Negrita','arialbd.ttf'),('Codigo','consola.ttf')]:
        pdfmetrics.registerFont(TTFont(name,str(Path('C:/Windows/Fonts')/file)))
    target=ROOT/'presentacion_final.pdf'
    c=canvas.Canvas(str(target),pagesize=(W,H))
    c.setTitle('Taller 6 - TDD del Tres en Raya')
    c.setAuthor('Proyecto Tres en Raya')
    page_number=0

    def text(x,y,value,size=18,bold=False,color=INK,font=None):
        family=font or ('Negrita' if bold else 'Texto')
        assert x+pdfmetrics.stringWidth(str(value),family,size)<=925,(page_number,value)
        c.setFillColor(color); c.setFont(family,size)
        c.drawString(x,H-y-size,str(value))

    def para(x,y,value,width=870,size=20,color=INK):
        style=ParagraphStyle('p',fontName='Texto',fontSize=size,leading=size*1.4,textColor=color)
        p=Paragraph(escape(value),style)
        _,height=p.wrap(width,H)
        assert y+height<=478,(page_number,value,y,height)
        p.drawOn(c,x,H-y-height)
        return y+height

    def page(title):
        nonlocal page_number
        if page_number:c.showPage()
        page_number+=1
        text(45,26,'TALLER 6 / TDD',11,True,ACCENT)
        text(45,59,title,32,True)
        c.setStrokeColor(colors.HexColor('#CFDCE0')); c.setLineWidth(0.6)
        c.line(45,H-115,915,H-115)
        text(45,511,'Tres en Raya - Java / Maven / JUnit 4',11,color=GRAY)
        text(883,511,f'{page_number:02d}',12,True,ACCENT)

    def source(value):text(45,487,value,10,color=GRAY)

    def bullets(items,y=145,size=20):
        for i,item in enumerate(items):
            text(45,y,str(i+1)+'.',size,True,ACCENT)
            y=para(76,y,item,839,size)+17
        return y

    def code(x,y,lines,width=420,size=16):
        for line in lines:
            assert pdfmetrics.stringWidth(line,'Codigo',size)<=width,(page_number,line)
            text(x,y,line,size,font='Codigo')
            y+=size*1.55
        return y

    def table(headers,rows,widths,y=145,size=17):
        cell=ParagraphStyle('cell',fontName='Texto',fontSize=size,leading=size*1.27,textColor=INK)
        head=ParagraphStyle('head',parent=cell,fontName='Negrita',textColor=colors.white)
        data=[[Paragraph(escape(str(v)),head) for v in headers]]
        data.extend([[Paragraph(escape(str(v)),cell) for v in row] for row in rows])
        t=Table(data,colWidths=widths,hAlign='LEFT')
        t.setStyle(TableStyle([
            ('BACKGROUND',(0,0),(-1,0),INK),('ROWBACKGROUNDS',(0,1),(-1,-1),[LIGHT,colors.white]),
            ('VALIGN',(0,0),(-1,-1),'TOP'),('LEFTPADDING',(0,0),(-1,-1),11),
            ('RIGHTPADDING',(0,0),(-1,-1),11),('TOPPADDING',(0,0),(-1,-1),8),
            ('BOTTOMPADDING',(0,0),(-1,-1),8)]))
        _,height=t.wrap(W,H)
        assert y+height<=475,(page_number,height)
        t.drawOn(c,45,H-y-height)
        return y+height

    page('Objetivo y reglas del Tres en Raya')
    para(45,145,'Implementar y verificar las reglas del juego mediante pruebas unitarias reproducibles.',870,26)
    bullets([
        'Tablero 3x3. Coordenadas de 1 a 3 en ambos ejes.',
        'X inicia y alterna con +. Una casilla ocupada o fuera del tablero produce una excepción.',
        'Gana quien completa una fila, columna o diagonal. Sin una línea completa se informa No hay ganador.'
    ],y=245,size=20)
    source('Fuente: material TDD del docente, páginas 16-18. Referencia: github.com/gpalabral/TDD.')

    page('Herramientas, API y estructura')
    table(['Elemento','Configuración'],[
        ['Lenguaje y build','Código compatible con Java 8; Maven; JUnit 4.13 en pom.xml.'],
        ['API','com.ucb.tdd.TresEnRaya: jugar(int x,int y) y siguienteTurno().'],
        ['Programa','src/main/java: motor TresEnRaya y consola Main.'],
        ['Pruebas','src/test/java: base, ampliada, docente, guiados y regresiones separadas.'],
        ['Automatización','GitHub Actions configurado con Maven y JDK 17.'],
    ],[245,625],size=18)
    source('Fuente: pom.xml, árbol src y .github/workflows/maven.yml.')

    page('Proceso TDD y contribuciones')
    bullets([
        'El historial conserva 16 ciclos anteriores: prueba fallida, implementación suficiente y comprobación de regresiones.',
        'La refactorización separó validación y detección de líneas. Las suites comparadas se añadieron después del motor: son verificaciones posteriores.',
        'La base se guardó antes de generar la ampliada. Ambas suites utilizan el mismo motor, API y reglas, sin cambios para favorecer resultados.'
    ],size=19)
    para(45,395,'El programa y ambas suites recibieron asistencia de IA. Se conservan las pruebas del docente por separado. La comparación literal de autoría humana independiente frente a IA sigue pendiente.',870,18,GRAY)
    source('Fuentes: historial Git, AUTORIA.md y huellas de evidencia/actual/resultados.json.')

    page('Suite base y suite ampliada')
    table(['Dimensión','Base','Ampliada'],[
        ['Organización','28 métodos explícitos, preparación/acción/comprobación','16 métodos con datos agrupados y un ayudante'],
        ['Coordenadas','0 y 4 en ambos ejes','Añade extremos int y dos estados de turno'],
        ['Errores','Excepción y conservación del turno','Añade continuidad del tablero y rechazos repetidos'],
        ['Victoria','8 líneas para X y 8 para +','Mismas líneas, respuestas y turnos intermedios'],
        ['Escenarios extra','Reglas obligatorias del curso','Consultas, independencia, finales y tablero lleno'],
        ['Mantenimiento','Lectura directa, mayor repetición','Menos repetición, mayor nivel de abstracción'],
    ],[170,350,350],y=137,size=16)
    source('Fuente: TresEnRayaBaseTest.java y TresEnRayaIATest.java. Métodos y escenarios no son equivalentes.')

    page('Ejemplo 1: estado después de un error')
    text(45,145,'Base: excepción y turno',20,True,ACCENT)
    text(505,145,'Ampliada: continuidad del tablero',20,True,ACCENT)
    code(45,190,[
        'juego.jugar(2, 2);',
        'assertThrows(Exception.class,',
        '    () -> juego.jugar(2, 2));',
        "assertEquals('+',",
        '    juego.siguienteTurno());',
    ])
    code(505,190,[
        '// Tras rechazar una ocupada:',
        'juego.jugar(2, 1);',
        'juego.jugar(1, 2);',
        'juego.jugar(2, 2);',
        'assertEquals("X es el ganador",',
        '    juego.jugar(1, 3));',
    ],width=410)
    para(45,385,'La ampliada utiliza una victoria posterior para comprobar que la ficha inicial no se borró al rechazar la jugada. La base observa directamente la excepción y el turno.',870,20)
    source('Fragmentos de casillaOcupada... (base) y rechazarOcupadaNoBorra... (ampliada).')

    page('Ejemplo 2: comprobación de una victoria')
    text(45,145,'Base: secuencia concreta',20,True,ACCENT)
    text(505,145,'Ampliada: cada paso de la partida',20,True,ACCENT)
    code(45,190,[
        'juego.jugar(1, 1);',
        'juego.jugar(2, 1);',
        'juego.jugar(1, 2);',
        'juego.jugar(2, 2);',
        'String resultado = juego.jugar(1, 3);',
        'assertEquals("X es el ganador",',
        '    resultado);',
    ])
    code(505,190,[
        'assertEquals(',
        '    "Resultado del movimiento " + i,',
        '    esperado, resultado);',
        'assertEquals(',
        '    "Turno posterior " + i,',
        "    i%2==0 ? '+' : 'X',",
        '    juego.siguienteTurno());',
    ],width=410)
    para(45,401,'Ambas suites prueban las ocho líneas para cada jugador. La ampliada comprueba también las respuestas anteriores a la victoria y el turno tras cada movimiento.',870,19)
    source('Fragmentos de xGanaEnPrimeraFila (base) y comprobarPartida (ayudante de la ampliada).')

    page('Resultados de la versión definitiva')
    table(['Ejecución','Métodos','Java 8','JDK 17'],[
        ['Base',suites['base']['pruebas'],'PASS','PASS'],
        ['Ampliada',suites['ia']['pruebas'],'PASS','PASS'],
        ['Base y ampliada juntas',suites['base-ia']['pruebas'],'PASS','PASS'],
        ['Todas las suites',suites['todas']['pruebas'],'PASS','PASS'],
    ],[390,140,170,170],size=19)
    para(45,365,'Compilación con javac 17.0.6 y --release 8, bytecode major 52. Ejecución con JRE 1.8.0_241 y JDK 17.0.6, utilizando JUnit local 4.13.2. El JAR funciona en ambos runtimes.',870,20)
    source('Fuente: evidencia/actual/resultados.json y logs nuevos de suites, consola y JAR.')

    page('Diferencias observadas en copias de prueba')
    table(['Defecto introducido temporalmente','Base','Ampliada'],[
        ['M1: borra una ficha antes de rechazar','No detecta','Detecta'],
        ['M2: omite la diagonal opuesta','Detecta','Detecta'],
        ['M3: consume turno al rechazar ocupada','Detecta','Detecta'],
    ],[550,160,160],size=18)
    para(45,335,'La base es suficiente para explicar las reglas obligatorias con casos concretos. La ampliada observa más efectos posteriores. Tener más métodos no garantiza mayor calidad.',870,21)
    para(45,417,'Los tres defectos son una muestra ilustrativa. Se aplicaron en copias temporales, sin modificar el motor entregado.',870,17,GRAY)
    source('Fuente: evidencia/actual/M1-*, M2-* y M3-*. Cambios exactos en scripts/verificar_final.py.')

    page('Conclusiones y límites de verificación')
    bullets([
        'Las reglas del juego y la compatibilidad con las 10 pruebas del docente se verificaron localmente. No se midió cobertura de líneas ni se demuestra corrección total.',
        'Maven 3.9.16 se instaló localmente. Su descarga de dependencias está bloqueada por permisos de red; las pruebas locales usan JUnit 4.13.2.',
        'GitHub Actions verificó Maven y JUnit 4.13: base 28, ampliada 16, juntas 44 y conjunto completo 75; cero fallos y errores.'
    ],size=19)
    para(45,407,'Repositorio público: github.com/diegocoaquira-source/taller6-tdd-tres-en-raya',870,17)
    c.linkURL('https://github.com/diegocoaquira-source/taller6-tdd-tres-en-raya',(45,H-450,915,H-390),relative=0)
    source('Fuentes: evidencia/actual/github-actions.txt, ejecución 40772099194; referencia github.com/gpalabral/TDD.')

    c.save()
    assert page_number==9
    print(f'{target.name}: {page_number} páginas, {target.stat().st_size} bytes')


if __name__=='__main__':
    main()
