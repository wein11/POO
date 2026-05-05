# PROYECTO: Sistema de Gestión Clínica Odontológica "Sonrisa Feliz"
## Estado: Entrega 2 completada

---

## ARQUITECTURA EN CAPAS

El proyecto aplica una arquitectura en capas estricta. **Entity** define el modelo de dominio puro (clases, enums, herencia). **Repository** encapsula la persistencia en memoria con colecciones Java. **Service** orquesta la lógica de negocio, valida reglas antes de delegar al Repository. **Controller** coordina el flujo completo: recibe los datos formateados de la Vista, invoca los Services y devuelve respuestas. **View** maneja exclusivamente la E/S por consola: muestra menús, lee input del usuario y lo empaqueta en objetos `Dato` que el Controller consume.

```
src/
├── Entity/
│   ├── Especialidad.java           ← enum: ORTODONCIA, ENDODONCIA, PERIODONCIA, PROTESIS, CIRUGIA
│   ├── EstadoTurno.java            ← enum: PENDIENTE, CONFIRMADO, CANCELADO, COMPLETADO
│   ├── Domicilio.java              ← concreta, composición dentro de Paciente
│   ├── Consultorio.java            ← concreta, referenciada opcionalmente por Turno
│   ├── HistorialClinico.java       ← concreta, agrega lista de Turno
│   ├── Paciente.java               ← concreta, tiene Domicilio e HistorialClinico
│   ├── Odontologo.java             ← ABSTRACT, define calcularDuracionConsulta()
│   ├── OdontologoOrtodoncista.java ← extiende Odontologo, duración 45 min
│   ├── OdontologoEndodoncista.java ← extiende Odontologo, duración 60 min
│   └── Turno.java                  ← concreta, une Paciente + Odontologo + fecha/hora/estado
├── Repository/
│   ├── iRepository.java            ← interfaz genérica CRUD
│   ├── RepositorioPaciente.java    ← clase concreta, ArrayList<Paciente>
│   ├── RepositorioOdontologo.java  ← clase concreta, ArrayList<Odontologo>
│   └── RepositorioTurno.java       ← clase concreta, ArrayList<Turno>
├── Service/
│   ├── iService.java               ← interfaz genérica CRUD (misma firma que iRepository)
│   ├── PacienteServiceImpl.java    ← implements iService<Paciente>
│   ├── OdontologoServiceImpl.java  ← implements iService<Odontologo>
│   └── TurnoServiceImpl.java       ← implements iService<Turno>
├── Controller/
│   └── ClinicaController.java      ← implements Runnable, orquesta todo
├── View/
│   ├── Dato.java                   ← abstract, campo tipo (int)
│   ├── DatoPaciente.java           ← extiende Dato, tipo=1
│   ├── DatoOdontologo.java         ← extiende Dato, tipo=2
│   ├── DatoTurno.java              ← extiende Dato, tipo=3
│   └── VistaClinica.java           ← Scanner, todos los menús y pedirDatos*
└── Main.java                       ← punto de entrada, 3 líneas
```

---

## ENTIDADES (Entity/)

**Especialidad** — enum
- Valores: `ORTODONCIA`, `ENDODONCIA`, `PERIODONCIA`, `PROTESIS`, `CIRUGIA`
- Relaciones: usada como campo en `Odontologo`

---

**EstadoTurno** — enum
- Valores: `PENDIENTE`, `CONFIRMADO`, `CANCELADO`, `COMPLETADO`
- Relaciones: usada como campo en `Turno`

---

**Domicilio** — concreta
- Atributos: `long id`, `String calle`, `int numero`, `String localidad`, `String provincia`, `String codigoPostal`, `Integer piso`, `String especificaciones`
- Constructores: vacío + completo con id + 4 variantes sin id (de E1, preservadas)
- Métodos clave: `toString()` — muestra dirección formateada, maneja `piso` y `especificaciones` nullable
- Relaciones: tiene-un en `Paciente` (composición)

---

**Consultorio** — concreta
- Atributos: `long id`, `int numero`, `int piso`
- Constructores: vacío + `(long id, int numero, int piso)` + `(long id, int numero)`
- Métodos clave: `toString()`, getters/setters estándar
- Relaciones: referenciada opcionalmente por `Turno` (puede ser null)

---

**HistorialClinico** — concreta
- Atributos: `long id`, `Paciente paciente`, `List<Turno> turnos`, `String resumenPaciente`
- Constructores: vacío + `(long id, Paciente, String resumen)` + `(long id, Paciente, List<Turno>, String)` + `(long id, Paciente)`
- Métodos clave: `agregarTurno(Turno)` — appende a la lista; `toString()` — itera turnos y los concatena
- Relaciones: tiene-un `Paciente` (referencia), tiene-lista-de `Turno` (agregación)

---

**Paciente** — concreta
- Atributos: `long id`, `String nombre`, `String apellido`, `Integer dni`, `String email`, `LocalDate fechaIngreso`, `Domicilio domicilio`, `String obraSocial`, `HistorialClinico historialClinico`
- Constructores: vacío + `(long id, nombre, apellido, dni, email, fechaIngreso, domicilio)` + completo con obraSocial e historialClinico
- Métodos clave: `getNombreCompleto()` → `nombre + " " + apellido`; `toString()` muestra todos los campos
- Relaciones: tiene-un `Domicilio` (composición), tiene-un `HistorialClinico` (asociación, puede ser null)

---

**Odontologo** — **abstract**
- Atributos: `long id`, `String nombre`, `String apellido`, `String matricula`, `Especialidad especialidad`
- Constructores: vacío + `(long id, nombre, apellido, matricula)` + `(long id, nombre, apellido, matricula, Especialidad)`
- Métodos clave:
  - `calcularDuracionConsulta()` — **abstracto**, retorna `int` (minutos); implementado por subclases
  - `getNombreCompleto()` → `nombre + " " + apellido`
  - `getPresentacion()` → `"Dr. X - Matrícula: Y"`
  - `toString()` — incluye duración llamando al método polimórfico
- Relaciones: superclase de `OdontologoOrtodoncista` y `OdontologoEndodoncista`

---

**OdontologoOrtodoncista** — concreta, extiende `Odontologo`
- Constructores: `super()` vacío + `super(id, nombre, apellido, matricula, Especialidad.ORTODONCIA)`
- Métodos clave:
  - `@Override calcularDuracionConsulta()` → retorna `45`
  - `@Override toString()` → `"Ortodoncista - " + super.toString()`

---

**OdontologoEndodoncista** — concreta, extiende `Odontologo`
- Constructores: `super()` vacío + `super(id, nombre, apellido, matricula, Especialidad.ENDODONCIA)`
- Métodos clave:
  - `@Override calcularDuracionConsulta()` → retorna `60`
  - `@Override toString()` → `"Endodoncista - " + super.toString()`

---

**Turno** — concreta
- Atributos: `long id`, `Paciente paciente`, `Odontologo odontologo`, `LocalDate fecha`, `LocalTime hora`, `EstadoTurno estado`, `Consultorio consultorio` (nullable)
- Constructores: vacío + `(long id, Paciente, Odontologo, LocalDate, LocalTime, EstadoTurno)` + ídem con `Consultorio`
- Métodos clave:
  - `getDetalle()` — descripción larga multi-línea, maneja `consultorio == null` con texto alternativo
  - `toString()` — línea única, omite consultorio si es null
- Relaciones: tiene-un `Paciente`, tiene-un `Odontologo`, tiene-un `EstadoTurno`, tiene-un `Consultorio` (opcional)

---

## REPOSITORIOS (Repository/)

**iRepository\<T\>** — interfaz genérica
- Colección interna: ninguna (es contrato)
- Métodos:
  - `guardar(T t)` — persiste un objeto
  - `buscarPorId(long id)` — retorna T o null
  - `eliminarPorId(long id)` — elimina por id
  - `actualizar(T t)` — reemplaza el objeto que tenga el mismo id
  - `listarTodos()` — retorna `List<T>`
- Nota: los repositorios concretos **NO declaran `implements iRepository`** (convención del profesor); la interfaz existe como contrato de diseño

---

**RepositorioPaciente** — clase concreta
- Colección interna: `ArrayList<Paciente>`, contador `long contadorId` (inicia en 1)
- Métodos CRUD: `guardar`, `buscarPorId`, `eliminarPorId`, `actualizar`, `listarTodos`
  - `guardar` asigna el id antes de agregar: `paciente.setId(contadorId++)`
  - `eliminarPorId` usa `removeIf` con lambda
  - `actualizar` itera por índice y hace `set(i, paciente)` cuando encuentra match
- Sin métodos de validación — la responsabilidad de validar es exclusiva de la capa Service

---

**RepositorioOdontologo** — clase concreta
- Colección interna: `ArrayList<Odontologo>`, contador `long contadorId`
- Métodos CRUD: idéntica estructura a RepositorioPaciente
- Sin métodos de validación

---

**RepositorioTurno** — clase concreta
- Colección interna: `ArrayList<Turno>`, contador `long contadorId`
- Métodos CRUD: idéntica estructura
- Sin métodos de validación

---

## SERVICIOS (Service/)

**iService\<T\>** — interfaz genérica
- Métodos: `guardar(T t) boolean`, `buscarPorId(long id) T`, `eliminarPorId(long id)`, `actualizar(T t)`, `listarTodos() List<T>`
- Nota: `guardar` retorna `boolean` (true = éxito, false = validación fallida) en lugar de `void`

---

**PacienteServiceImpl** — implementa `iService<Paciente>`
- Delega a: `RepositorioPaciente` (instanciado en constructor)
- Validaciones en `guardar(Paciente)` — si falla imprime `[ERROR]` y retorna `false`:
  - Nombre no null ni vacío
  - DNI no null y mayor a 0
  - DNI no duplicado (itera `repositorio.listarTodos()` y compara con `equals`)
- Si todas pasan: llama `repositorio.guardar(paciente)` y retorna `true`
- Los otros 4 métodos delegan directo al repositorio sin validaciones adicionales

---

**OdontologoServiceImpl** — implementa `iService<Odontologo>`
- Delega a: `RepositorioOdontologo`
- Validaciones en `guardar(Odontologo)` — si falla imprime `[ERROR]` y retorna `false`:
  - Nombre no null ni vacío
  - Matrícula no null ni vacía
  - Matrícula no duplicada (itera `repositorio.listarTodos()`)
- Si todas pasan: llama `repositorio.guardar(odontologo)` y retorna `true`
- Resto delega directo

---

**TurnoServiceImpl** — implementa `iService<Turno>`
- Delega a: `RepositorioTurno`; recibe en constructor `PacienteServiceImpl` y `OdontologoServiceImpl`
- Validaciones en `guardar(Turno)` — si falla imprime `[ERROR]` y retorna `false`:
  - Paciente existe (`servicioPaciente.buscarPorId(turno.getPaciente().getId()) != null`)
  - Odontólogo existe (`servicioOdontologo.buscarPorId(...) != null`)
  - Odontólogo no tiene otro turno en esa fecha+hora (itera `repositorio.listarTodos()` y compara id/fecha/hora)
- Si todas pasan: llama `repositorio.guardar(turno)` y retorna `true`
- Resto delega directo

---

## CONTROLADOR (Controller/)

**ClinicaController** — implements `Runnable`

**Flujo:** `run()` ejecuta un `while (ejecutando)` que muestra el menú principal, lee la opción e invoca el sub-menú correspondiente; se detiene cuando `salir()` pone `ejecutando = false`, luego llama `vista.cerrar()`.

**Opciones del menú principal (switch en run()):**
- `1` → `menuPacientes()`
- `2` → `menuOdontologos()`
- `3` → `menuTurnos()`
- `0` → `salir()`
- default → `vista.mostrarError("Opcion invalida...")`

**Métodos privados — Pacientes:**

| Método | Qué hace |
|---|---|
| `menuPacientes()` | Loop propio con switch 1-5/0, llama a los métodos de abajo |
| `registrarPaciente()` | Llama `vista.pedirDatosPaciente()`, si devuelve `null` aborta; construye `Domicilio` y `Paciente`, llama `servicioPaciente.guardar()`, si retorna `true` muestra éxito |
| `buscarPaciente()` | Llama `vista.pedirId()`, llama `servicioPaciente.buscarPorId()`, muestra resultado o error |
| `eliminarPaciente()` | Pide ID, pide confirmación con `vista.pedirConfirmacion()`, llama `eliminarPorId()` |
| `actualizarPaciente()` | Pide ID, verifica existencia, llama `pedirDatosPaciente()`, si devuelve `null` aborta; construye nuevo objeto con mismo id, llama `actualizar()` |
| `listarPacientes()` | Llama `listarTodos()`, itera e imprime con `System.out.println(p)` |

**Métodos privados — Odontólogos:**

| Método | Qué hace |
|---|---|
| `menuOdontologos()` | Loop propio, misma estructura |
| `registrarOdontologo()` | Llama `pedirDatosOdontologo()`, si devuelve `null` aborta; instancia `OdontologoOrtodoncista` o `OdontologoEndodoncista` según `tipoEspecialista`, llama `servicioOdontologo.guardar()`, si retorna `true` muestra éxito |
| `buscarOdontologo()` | Busca por ID, muestra |
| `eliminarOdontologo()` | Pide confirmación, elimina |
| `actualizarOdontologo()` | Llama `pedirDatosOdontologo()`, si devuelve `null` aborta; reconstruye subclase correcta con mismo id, llama `actualizar()` |
| `listarOdontologos()` | Itera e imprime |

**Métodos privados — Turnos:**

| Método | Qué hace |
|---|---|
| `menuTurnos()` | Loop propio |
| `registrarTurno()` | Llama `pedirDatosTurno()`, si devuelve `null` aborta; busca Paciente y Odontologo por id, valida que no sean null, construye `Turno` con `EstadoTurno.PENDIENTE`, llama `servicioTurno.guardar()`, si retorna `true` muestra éxito |
| `buscarTurno()` | Busca por ID, muestra |
| `eliminarTurno()` | Pide confirmación, elimina |
| `actualizarTurno()` | Llama `pedirDatosTurno()`, si devuelve `null` aborta; reconstruye Turno con mismo id y conserva el `estado` del existente, llama `actualizar()` |
| `listarTurnos()` | Itera e imprime |

**Método privado especial:**

| Método | Qué hace |
|---|---|
| `salir()` | Llama `vista.pedirConfirmacion()`, si "s" → `ejecutando = false` + mensaje despedida |

---

## VISTA (View/)

**Dato** — clase abstracta
- Campo: `protected int tipo`
- Métodos: `getTipo()` → retorna `tipo`
- Propósito: jerarquía de DTOs (Data Transfer Objects) que encapsulan el input del usuario

---

**DatoPaciente** — extiende `Dato`
- `tipo = 1` (asignado en constructor vacío)
- Campos agregados: `String nombre`, `String apellido`, `int dni`, `String email`, `LocalDate fechaIngreso`, `String calle`, `int numeroCalle`, `String localidad`, `String provincia`
- Métodos: constructor vacío + getters/setters para cada campo

---

**DatoOdontologo** — extiende `Dato`
- `tipo = 2`
- Campos agregados: `String nombre`, `String apellido`, `String matricula`, `int tipoEspecialista` (1=Ortodoncista, 2=Endodoncista)
- Métodos: constructor vacío + getters/setters

---

**DatoTurno** — extiende `Dato`
- `tipo = 3`
- Campos agregados: `long idPaciente`, `long idOdontologo`, `LocalDate fecha`, `LocalTime hora`
- Métodos: constructor vacío + getters/setters

---

**VistaClinica** — vista principal con Scanner

| Método | Descripción |
|---|---|
| `VistaClinica()` | Instancia `Scanner(System.in)` |
| `mostrarMenuInicial()` | Imprime el banner + opciones 0-3 del menú principal |
| `mostrarMenuPacientes()` | Imprime submenu pacientes (opciones 0-5) |
| `mostrarMenuOdontologos()` | Imprime submenu odontólogos (opciones 0-5) |
| `mostrarMenuTurnos()` | Imprime submenu turnos (opciones 0-5) |
| `pedirOpcion()` | Lee línea, parsea a `int`; retorna -1 si no es número |
| `pedirId()` | Imprime "Ingrese el ID:", parsea a `long`; retorna -1 si falla |
| `pedirConfirmacion(String)` | Imprime mensaje + "(s/n):", retorna `true` si input es "s" (case-insensitive) |
| `mostrarMensaje(String)` | Imprime `"[OK] " + mensaje` |
| `mostrarError(String)` | Imprime `"[ERROR] " + error` |
| `pausar()` | Imprime "Presione ENTER para continuar..." y bloquea con `scanner.nextLine()` |
| `cerrar()` | Llama `scanner.close()` |
| `pedirDatosPaciente()` | Lee campo por campo con prompts; si el parseo de DNI, número de calle o fecha falla imprime `[ERROR]` y retorna `null` |
| `pedirDatosOdontologo()` | Lee nombre, apellido, matrícula, tipoEspecialista (1 o 2); si el parseo falla imprime `[ERROR]` y retorna `null` |
| `pedirDatosTurno()` | Lee idPaciente, idOdontologo, fecha (AAAA-MM-DD), hora (HH:MM); si el parseo falla imprime `[ERROR]` y retorna `null` |

---

## FLUJO COMPLETO — EJEMPLO: Registrar Turno

```
USUARIO teclea "3" en menú principal
  └─ VistaClinica.pedirOpcion() → 3
       └─ ClinicaController.run() → switch(3) → menuTurnos()

USUARIO teclea "1" en menú turnos
  └─ VistaClinica.pedirOpcion() → 1
       └─ ClinicaController.menuTurnos() → switch(1) → registrarTurno()

CAPTURA DE DATOS
  └─ VistaClinica.pedirDatosTurno()
       │  Lee: idPaciente (long), idOdontologo (long), fecha (LocalDate), hora (LocalTime)
       └─ retorna DatoTurno

VALIDACIÓN PRE-CREACIÓN en ClinicaController.registrarTurno()
  ├─ PacienteServiceImpl.buscarPorId(dato.getIdPaciente())
  │    └─ RepositorioPaciente.buscarPorId(id) → itera ArrayList → Paciente o null
  └─ OdontologoServiceImpl.buscarPorId(dato.getIdOdontologo())
       └─ RepositorioOdontologo.buscarPorId(id) → itera ArrayList → Odontologo o null

Si alguno es null → VistaClinica.mostrarError(...) → vista.pausar() → return

CONSTRUCCIÓN
  └─ new Turno(0, paciente, odontologo, fecha, hora, EstadoTurno.PENDIENTE)

PERSISTENCIA
  └─ TurnoServiceImpl.guardar(turno) → boolean
       ├─ PacienteServiceImpl.buscarPorId(turno.getPaciente().getId())
       │    └─ Si null → System.out.println("[ERROR]...") → return false
       ├─ OdontologoServiceImpl.buscarPorId(turno.getOdontologo().getId())
       │    └─ Si null → System.out.println("[ERROR]...") → return false
       ├─ Itera listarTodos() buscando triple coincidencia id/fecha/hora
       │    └─ Si existe → System.out.println("[ERROR]...") → return false
       └─ RepositorioTurno.guardar(turno) → return true
            └─ turno.setId(contadorId++)  →  ArrayList.add(turno)

RESPUESTA
  └─ ClinicaController.registrarTurno()
       └─ Si guardar() retornó true → VistaClinica.mostrarMensaje("Turno registrado con ID: " + turno.getId())
            └─ VistaClinica.pausar()
```

---

## DECISIONES DE DISEÑO

**1. Por qué Odontologo es abstracta**

No existe un "odontólogo genérico" en la clínica — siempre es un especialista concreto (Ortodoncista o Endodoncista). Declarar la clase `abstract` impide instanciar `new Odontologo(...)` directamente, forzando al programador a elegir la especialización correcta. Esto también habilita el polimorfismo: el Controller puede declarar `Odontologo odontologo` y asignarle cualquier subclase sin conocerla.

**2. Por qué calcularDuracionConsulta() es abstracto**

La duración de una consulta depende del tipo de especialidad y no tiene un valor por defecto válido a nivel de `Odontologo`. Al declararlo abstracto, Java obliga a cada subclase a definir su propia implementación (Ortodoncista: 45 min, Endodoncista: 60 min). El método ya se invoca desde `Odontologo.toString()` de forma polimórfica, lo que demuestra el patrón Template Method implícito.

**3. Por qué iRepository\<T\> e iService\<T\> son genéricas**

La genericidad elimina la duplicación de firmas: una sola interfaz describe el contrato CRUD para Paciente, Odontologo y Turno. Sin generics habría tres interfaces paralelas idénticas. El tipo `T` es reemplazado en tiempo de compilación (`iService<Paciente>`, `iService<Odontologo>`), manteniendo type-safety sin sacrificar reutilización. Esto cumple directamente el principio OCP: agregar una cuarta entidad solo requiere una nueva implementación, no modificar la interfaz.

**4. Patrones GRASP aplicados por capa**

- **Controller (ClinicaController):** aplica el patrón *Controller* de GRASP — centraliza la recepción de eventos del usuario y delega a los objetos correctos sin mezclar lógica de negocio ni E/S.
- **Information Expert (Entity):** cada entidad responde a preguntas sobre sus propios datos. `Paciente.getNombreCompleto()` y `Turno.getDetalle()` son ejemplos: el objeto que tiene la información la procesa.
- **Creator (Controller):** el Controller crea `Domicilio`, `Paciente`, `Turno`, `OdontologoOrtodoncista` porque tiene los datos del `Dato` para hacerlo (principio Creator: quien tiene los datos de inicialización crea el objeto).

**5. Principios SOLID cumplidos**

- **SRP:** cada clase tiene una sola razón de cambio. `VistaClinica` solo maneja consola; `RepositorioPaciente` solo maneja la colección; `PacienteServiceImpl` solo aplica reglas de negocio de paciente. Las validaciones de unicidad (DNI, matrícula, horario) viven en el Service y no en el Repository.
- **OCP:** agregar una nueva especialidad de odontólogo (ej. `OdontologoPeriodonista`) requiere crear una subclase nueva sin modificar `Odontologo`, `RepositorioOdontologo`, ni `OdontologoServiceImpl`.
- **LSP:** `OdontologoOrtodoncista` y `OdontologoEndodoncista` pueden sustituir a `Odontologo` en cualquier contexto (el Controller declara `Odontologo odontologo` y asigna la subclase).
- **ISP / DIP:** las dependencias entre capas apuntan a interfaces (`iService<T>`, `iRepository<T>`) no a implementaciones concretas en el contrato de diseño.

**6. Manejo de errores sin excepciones**

Las validaciones de negocio no usan `throw`/`try-catch` como mecanismo de control de flujo. En cambio:
- Los servicios imprimen `[ERROR] mensaje` con `System.out.println` y retornan `false`
- `VistaClinica` atrapa internamente los errores de parseo (números, fechas) y retorna `null`
- El Controller chequea `null` y `boolean` con `if` simples

Esto mantiene el código legible antes de introducir el tema de excepciones formalmente.

---

## CONVENCIONES DEL PROYECTO

- **Paquetes:** `Entity`, `Repository`, `Service`, `Controller`, `View` — todos con inicial mayúscula
- **Interfaces:** inicial minúscula: `iRepository<T>`, `iService<T>`
- **Implementaciones de servicio:** sufijo `Impl` — `PacienteServiceImpl`, `OdontologoServiceImpl`, `TurnoServiceImpl`
- **Repositorios concretos:** prefijo `Repositorio` + nombre de entidad
- **Patrón constructor:** todas las entidades tienen constructor vacío `public Nombre() {}` y al menos un constructor completo con todos los campos de negocio
- **@Override:** aplicado en todos los métodos que sobreescriben: `calcularDuracionConsulta()`, `toString()`, los 5 métodos de `iService<T>` en cada ServiceImpl, y `run()` en el Controller
- **super():** `OdontologoOrtodoncista` y `OdontologoEndodoncista` llaman a `super()` en constructor vacío y `super(id, nombre, apellido, matricula, Especialidad.X)` en constructor completo
- **toString():** presente en todas las entidades (`Domicilio`, `Paciente`, `Odontologo`, `OdontologoOrtodoncista`, `OdontologoEndodoncista`, `Turno`, `HistorialClinico`, `Consultorio`)
- **Ids:** todos `long` en entidades; repositorios usan `long contadorId` auto-incremental iniciado en 1
- **Colecciones:** `ArrayList<T>` en todos los repositorios (no HashMap)
- **Acceso a campos:** todos `private` con getters/setters públicos
- **Java SE puro:** sin frameworks, sin anotaciones externas, sin dependencias

---

## ARCHIVOS ADICIONALES

- `diagrama_uml.mmd` — diagrama UML de clases en formato Mermaid. Puede visualizarse en mermaid.live, VS Code (extensión Mermaid Preview) o GitHub (dentro de un bloque de código mermaid en un archivo `.md`). Incluye herencia, implementación, composición y agregación.

---

## PENDIENTE — Entrega 3 (próximos pasos)

1. **Persistencia real** — reemplazar los `ArrayList` en memoria por archivos (serialización Java, CSV o JSON simple) para que los datos sobrevivan entre ejecuciones del programa
2. **Excepciones personalizadas** — cuando se vea el tema formalmente, reemplazar el manejo actual (boolean + System.out.println) por `PacienteNoEncontradoException`, `TurnoYaReservadoException`, `DniDuplicadoException`, etc., en los ServiceImpl
3. **Jerarquía de Turno** — posible `TurnoUrgente` y `TurnoControl` como subclases de `Turno` (análoga a la jerarquía de Odontologo ya implementada), con `Turno` volviéndose abstracta
4. **Tests unitarios** — introducción de JUnit para testear los ServiceImpl y la lógica de validación en aislamiento
5. **Ampliación de submenús** — gestión de `HistorialClinico` y `Consultorio` desde consola (actualmente existen las entidades pero no tienen CRUD en el menú)
