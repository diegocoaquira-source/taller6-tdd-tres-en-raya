"""Verificacion reproducible auxiliar (stdlib Python). El build oficial es Maven.

Usa JDK 17 y JUnit locales sin redistribuir sus binarios. No cambia el motor.
Los defectos de evaluacion se compilan exclusivamente en directorios temporales.
"""
from pathlib import Path
import argparse
import hashlib
import json
import os
import re
import shutil
import subprocess
import tempfile
import urllib.request
import zipfile

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / 'evidencia/actual'
BUILD = ROOT / '.local-build/final'
PREFIX = 'com.ucb.tdd.'
SUITES = {
    'base': ['TresEnRayaBaseTest'],
    'ia': ['TresEnRayaIATest'],
    'docente': ['TresEnRayaTest'],
    'guiados': ['TresEnRayaGuiadosTest'],
    'desarrollo': ['TresEnRayaDesarrolloTest'],
    'regresion': ['TresEnRayaRegresionTest'],
}


def sha(path):
    data = path.read_bytes()
    if path.suffix == '.java':
        # Git conserva las fuentes en LF; Windows puede tener CRLF en trabajo.
        data = data.replace(b'\r\n', b'\n')
    return hashlib.sha256(data).hexdigest()


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--jdk-home', default=os.environ.get('JAVA_HOME'))
    parser.add_argument('--junit-jar', default=os.environ.get('JUNIT_JAR'))
    args = parser.parse_args()
    suffix = '.exe' if os.name == 'nt' else ''
    program_files = Path(os.environ.get('ProgramFiles', '/nonexistent'))
    candidates = sorted(program_files.glob('JetBrains/PyCharm*/jbr/bin/javac.exe'))
    javac = Path(args.jdk_home)/('bin/javac'+suffix) if args.jdk_home else None
    if javac is None or not javac.is_file():
        found = shutil.which('javac')
        javac = Path(found) if found else (candidates[-1] if candidates else None)
    if javac is None or not javac.is_file():
        raise SystemExit('Falta JDK. Indique --jdk-home con una ruta real.')
    java = javac.parent / ('java'+suffix)
    junit_candidates = sorted(program_files.glob('JetBrains/PyCharm*/lib/junit4.jar'))
    junit = Path(args.junit_jar) if args.junit_jar else (junit_candidates[-1] if junit_candidates else None)
    if junit is None or not junit.is_file():
        raise SystemExit('Indique --junit-jar. Alternativa oficial: mvn -B clean verify.')
    OUT.mkdir(parents=True, exist_ok=True)
    BUILD.mkdir(parents=True, exist_ok=True)
    substitutions = [(str(ROOT), '<PROYECTO>'), (str(junit), '<JUNIT_LOCAL>'),
                     (str(javac.parent.parent), '<JDK_LOCAL>'), (str(Path.home()), '<USUARIO>')]

    def clean(text):
        for original, replacement in substitutions:
            text = text.replace(original, replacement)
        return text

    def run(command, filename, expected=0, input=None, env=None):
        command = [str(x) for x in command]
        result = subprocess.run(command, cwd=ROOT, capture_output=True, text=True,
                                encoding='utf-8', errors='replace', input=input, env=env)
        log = 'COMANDO: '+subprocess.list2cmdline(command)+'\nRETORNO: '+str(result.returncode)+'\n\n'+result.stdout+result.stderr
        (OUT/filename).write_text(clean(log), encoding='utf-8')
        if expected is not None and result.returncode != expected:
            raise RuntimeError(clean(log))
        return result

    version = run([javac, '-version'], 'compilador.txt')
    compiler_version = (version.stdout+version.stderr).strip()
    release = ['-source','1.8','-target','1.8'] if 'javac 1.8.' in compiler_version else ['--release','8']
    junit_version = run([java,'-cp',junit,'junit.runner.Version'], 'junit-version.txt').stdout.strip()
    sources = sorted((ROOT/'src/main/java').rglob('*.java')) + sorted((ROOT/'src/test/java').rglob('*.java'))
    run([javac,*release,'-encoding','UTF-8','-cp',junit,'-d',BUILD,*sources], 'compilacion.txt')
    assert int.from_bytes((BUILD/'com/ucb/tdd/TresEnRaya.class').read_bytes()[6:8],'big') == 52
    base_file = ROOT/'src/test/java/com/ucb/tdd/TresEnRayaBaseTest.java'
    expected_base = '5d38f9abbd557a1ec27aa55787283e2b5f1dbd17738fdd2a7daf4f8c1ca50586'
    assert sha(base_file) == expected_base, 'La base difiere de la version congelada. Revisar antes de comparar.'
    run_suites = dict(SUITES)
    run_suites['base-ia'] = SUITES['base']+SUITES['ia']
    run_suites['todas'] = [c for group in SUITES.values() for c in group]
    runtimes = [('jdk17',java)]
    if os.name == 'nt':
        runtime8s = sorted(Path(os.environ.get('ProgramFiles(x86)','/nonexistent')).glob('Java/jre1.8*/bin/java.exe'))
        if runtime8s:
            runtimes.append(('java8',runtime8s[-1]))
    report = {'fecha':'2026-10-28', 'compilador':compiler_version, 'bytecode_major':52,
              'junit_local':junit_version, 'junit_pom':'4.13', 'resultados':{},
              'base_congelada':{'tag':'base-didactica-revision-final','commit':'bd7486049cccf5a7d486bcfbd78e995bede932af','sha256':expected_base,'normalizacion':'LF, igual que las fuentes guardadas en Git','revision':'Solo se retiro el comentario de autoria; casos identicos a base-didactica-v1'},
              'sha256_motor':sha(ROOT/'src/main/java/com/ucb/tdd/TresEnRaya.java'),
              'sha256_ia':sha(ROOT/'src/test/java/com/ucb/tdd/TresEnRayaIATest.java'),
              'misma_compilacion_para_base_e_ia':True, 'cobertura_lineas':'NO MEDIDA',
              'autoria':'Ambas suites asistidas por IA; requisito humano independiente pendiente',
              'github':'NO PUBLICADO; Actions NO EJECUTADO'}
    cp = str(BUILD)+os.pathsep+str(junit)
    for label, executable in runtimes:
        runtime_version = run([executable,'-version'], label+'-version.txt')
        report['resultados'][label] = {'version':(runtime_version.stdout+runtime_version.stderr).strip(),'suites':{}}
        for suite, names in run_suites.items():
            tested = run([executable,'-Dfile.encoding=UTF-8','-cp',cp,'org.junit.runner.JUnitCore',*[PREFIX+n for n in names]], label+'-'+suite+'.txt')
            count = re.search(r'OK \((\d+) tests?\)', tested.stdout)
            assert count, tested.stdout
            report['resultados'][label]['suites'][suite] = {'pruebas':int(count.group(1)),'resultado':'PASS'}
        console = run([executable,'-Dfile.encoding=UTF-8','-cp',BUILD,'com.ucb.tdd.Main'], label+'-consola.txt', input='0 2\n1 1\n1 1\n2 1\n1 2\n2 2\n1 3\n')
        assert 'X es el ganador' in console.stdout and console.stdout.count('Jugada rechazada:') == 2

    # Evaluacion ilustrativa en copias temporales, sin cambiar el motor original.
    motor = (ROOT/'src/main/java/com/ucb/tdd/TresEnRaya.java').read_text(encoding='utf-8')
    mutations = [
        ('M1','Borra la ficha ocupada antes de rechazar', '            throw new IllegalStateException("La posicion ya esta ocupada");', '            tablero[x - 1][y - 1] = \'\\0\';\n            throw new IllegalStateException("La posicion ya esta ocupada");'),
        ('M2','Omite diagonal opuesta', '|| lineaCompleta(tablero[0][2], tablero[1][1], tablero[2][0]);', '|| false;'),
        ('M3','Consume turno al rechazar ocupada', '            throw new IllegalStateException("La posicion ya esta ocupada");', '            ultimoJugador = siguienteTurno();\n            throw new IllegalStateException("La posicion ya esta ocupada");'),
    ]
    report['defectos_sembrados'] = []
    for code, description, old, new in mutations:
        assert motor.count(old) == 1
        with tempfile.TemporaryDirectory(prefix='tdd-evaluacion-') as temp_name:
            temp = Path(temp_name).resolve()
            assert temp.is_relative_to(Path(tempfile.gettempdir()).resolve())
            substitutions.insert(0,(str(temp),'<COPIA_TEMPORAL>'))
            (temp/'TresEnRaya.java').write_text(motor.replace(old,new),encoding='utf-8')
            run([javac,*release,'-encoding','UTF-8','-cp',junit,'-d',temp,temp/'TresEnRaya.java',base_file,ROOT/'src/test/java/com/ucb/tdd/TresEnRayaIATest.java'],code+'-compilacion.txt')
            row = {'id':code,'defecto':description}
            for label, cls in [('base','TresEnRayaBaseTest'),('ia','TresEnRayaIATest')]:
                result = run([java,'-Dfile.encoding=UTF-8','-cp',str(temp)+os.pathsep+str(junit),'org.junit.runner.JUnitCore',PREFIX+cls],code+'-'+label+'.txt',expected=None)
                assert result.returncode in (0,1)
                if result.returncode: assert 'FAILURES!!!' in result.stdout
                row[label] = 'DETECTADO' if result.returncode else 'NO DETECTADO'
            report['defectos_sembrados'].append(row)
            substitutions.pop(0)
    assert sha(ROOT/'src/main/java/com/ucb/tdd/TresEnRaya.java') == report['sha256_motor']

    # Volver a observar el defecto de la referencia, sin usar informes antiguos.
    with tempfile.TemporaryDirectory(prefix='tdd-referencia-') as temp_name:
        temp = Path(temp_name).resolve()
        assert temp.is_relative_to(Path(tempfile.gettempdir()).resolve())
        substitutions.insert(0,(str(temp),'<REFERENCIA_TEMPORAL>'))
        (temp/'TresEnRaya.java').write_bytes((ROOT/'referencia-docente/TresEnRaya.java.txt').read_bytes())
        run([javac,*release,'-encoding','UTF-8','-cp',junit,'-d',temp,temp/'TresEnRaya.java',ROOT/'src/test/java/com/ucb/tdd/TresEnRayaTest.java',ROOT/'src/test/java/com/ucb/tdd/TresEnRayaRegresionTest.java'],'referencia-compilacion.txt')
        refcp = str(temp)+os.pathsep+str(junit)
        run([java,'-cp',refcp,'org.junit.runner.JUnitCore',PREFIX+'TresEnRayaTest'],'referencia-docente.txt')
        failure = run([java,'-cp',refcp,'org.junit.runner.JUnitCore',PREFIX+'TresEnRayaRegresionTest'],'referencia-regresion.txt',expected=1)
        assert 'expected:<43> but was:<88>' in failure.stdout
        substitutions.pop(0)
    report['referencia'] = {'docente':'10 PASS','regresion':'3 pruebas, 1 falla; turno + esperado frente a X observado'}

    # Intento real de Maven. Si no esta instalado se intenta obtener una distribucion oficial.
    maven = shutil.which('mvn') or shutil.which('mvn.cmd')
    portable = ROOT/'.local-build/tools/apache-maven-3.9.16/bin/mvn.cmd'
    if not maven and portable.is_file():
        maven = str(portable)
    maven_note = 'Solicitud: mvn -B clean verify\n'
    if not maven:
        maven_note += 'mvn no aparece en PATH. Intento de descarga desde Maven Central.\n'
        try:
            url = 'https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.9/apache-maven-3.9.9-bin.zip'
            data = urllib.request.urlopen(url,timeout=10).read()
            expected = urllib.request.urlopen(url+'.sha512',timeout=10).read().decode().split()[0]
            assert hashlib.sha512(data).hexdigest() == expected
            tools = ROOT/'.local-build/tools'
            tools.mkdir(exist_ok=True)
            archive = tools/'maven.zip'
            archive.write_bytes(data)
            with zipfile.ZipFile(archive) as bundle:
                assert all((tools/n).resolve().is_relative_to(tools.resolve()) for n in bundle.namelist())
                bundle.extractall(tools)
            maven = str(tools/'apache-maven-3.9.9/bin'/('mvn.cmd' if os.name=='nt' else 'mvn'))
        except Exception as error:
            maven_note += 'Descarga no completada: '+str(error)+'\nMaven NO EJECUTADO. JUnit 4.13 exacto NO VERIFICADO.\n'
    if maven:
        env = dict(os.environ)
        env['JAVA_HOME'] = str(javac.parent.parent)
        run([maven,'-version'],'maven-version.txt',env=env)
        result = run([maven,'-B','-Dmaven.repo.local='+str(ROOT/'.local-build/m2'),'-Dmaven.wagon.http.retryHandler.count=0','clean','verify'],'maven-ejecucion.txt',expected=None,env=env)
        report['maven'] = {'ejecutado':True,'retorno':result.returncode,'verificado':result.returncode==0}
        maven_note += 'Maven ejecutado; consultar maven-version.txt y maven-ejecucion.txt.\n'
    else:
        report['maven'] = {'ejecutado':False,'verificado':False,'motivo':'mvn ausente y descarga bloqueada por red'}
    (OUT/'maven-intento.txt').write_text(clean(maven_note),encoding='utf-8')

    # JAR de programa (sin pruebas ni bibliotecas externas) y ejecucion real.
    jar = ROOT/'dist/tres-en-raya.jar'
    jar.parent.mkdir(exist_ok=True)
    with zipfile.ZipFile(jar,'w',zipfile.ZIP_DEFLATED) as bundle:
        bundle.writestr('META-INF/MANIFEST.MF','Manifest-Version: 1.0\r\nMain-Class: com.ucb.tdd.Main\r\n\r\n')
        for name in ['TresEnRaya','Main']:
            f = BUILD/('com/ucb/tdd/'+name+'.class')
            bundle.write(f,f.relative_to(BUILD).as_posix())
    for label, executable in runtimes:
        result = run([executable,'-jar',jar],label+'-jar.txt',input='1 1\n2 1\n1 2\n2 2\n1 3\n')
        assert 'X es el ganador' in result.stdout
    report['sha256_jar'] = sha(jar)
    report['escenarios_agrupados_ia'] = {'limites_y_estados':24,'casillas_iniciales':9,'lineas_por_jugador':8,'victorias_en_novena':2,'victoria_mas_en_octava':1,'partidas_terminales':16,'rechazos_tras_victoria':144}
    (OUT/'resultados.json').write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf-8')
    print(json.dumps(report,ensure_ascii=False,indent=2))


if __name__ == '__main__':
    main()
