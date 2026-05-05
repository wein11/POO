# Diseño de clases y relaciones

Este documento describe las clases del proyecto "Sistema de Gestión de Clínica Odontológica Sonrisa Feliz", sus atributos, métodos principales y relaciones. Está pensado como guía técnica para entender o reconstruir el sistema.

## Arquitectura general

El proyecto está organizado en capas:

```text
src/
├── Entity       -> Modelo de dominio
├── Repository   -> Persistencia en memoria
├── Service      -> Reglas de negocio y validaciones
├── Controller   -> Coordinación entre vista y servicios
├── View         -> Entrada/salida por consola
├── Main.java    -> Punto de entrada
└── ValidacionesTest.java -> Test manual de validaciones
```

Responsabilidades principales:

- `Entity`: representa conceptos del dominio: paciente, odontólogo, turno, domicilio, historial clínico.
- `Repository`: guarda entidades en memoria usando colecciones.
- `Service`: valida datos, crea entidades cuando corresponde y aplica reglas de negocio.
- `Controller`: comunica la vista con los servicios.
- `View`: muestra menús, lee datos del usuario y los empaqueta en clases `Dato`.

## 1. Entity

## 1.1 Domicilio

Clase concreta que representa el domicilio de un paciente.

### Atributos

```java
private long id;
private String calle;
private int numero;
private String localidad;
private String provincia;
private String codigoPostal;
private Integer piso;
private String especificaciones;
```

### Constructores

Simplificado:

```java
Domicilio()
Domicilio(id, calle, numero, localidad, provincia, codigoPostal, piso, especificaciones)
Domicilio(calle, numero, localidad, provincia, codigoPostal, piso, especificaciones)
Domicilio(calle, numero, localidad, provincia, codigoPostal, piso)
Domicilio(calle, numero, localidad, provincia, codigoPostal, especificaciones)
Domicilio(calle, numero, localidad, provincia, codigoPostal)
```

### Métodos

```java
getters/setters de todos los atributos
toString()
```

### Relaciones

- Composición con `Paciente`.
- Un `Paciente` tiene un `Domicilio`.
- En este sistema, el domicilio no se administra de forma independiente.

Tipo UML:

```text
Paciente 1 ◆── 1 Domicilio
```

## 1.2 Paciente

Clase concreta que representa a una persona atendida en la clínica.

### Atributos

```java
private long id;
private String nombre;
private String apellido;
private Integer dni;
private String email;
private LocalDate fechaIngreso;
private Domicilio domicilio;
private String obraSocial;
private HistorialClinico historialClinico;
```

### Constructores

Simplificado:

```java
Paciente()
Paciente(id, nombre, apellido, dni, email, fechaIngreso, domicilio)
Paciente(id, nombre, apellido, dni, email, fechaIngreso, domicilio, obraSocial, historialClinico)
```

### Métodos

```java
getters/setters de todos los atributos
getNombreCompleto()
toString()
```

### Relaciones

- Composición con `Domicilio`.
- Asociación bidireccional con `HistorialClinico`.
- Asociación indirecta con `Turno` a través de `HistorialClinico`.
- Asociación directa desde `Turno`, porque cada turno referencia un paciente.

Tipos UML:

```text
Paciente 1 ◆── 1 Domicilio
Paciente 1 ◄──► 1 HistorialClinico
Paciente 1 ── 0..* Turno
```

Notas:

- `Paciente` conoce su `HistorialClinico`.
- `HistorialClinico` también conoce el `Paciente`.
- Esa relación es bidireccional.

## 1.3 HistorialClinico

Clase concreta que representa el historial clínico de un paciente.

### Atributos

```java
private long id;
private Paciente paciente;
private List<Turno> turnos;
private String resumenPaciente;
```

### Constructores

Simplificado:

```java
HistorialClinico()
HistorialClinico(id, paciente, resumenPaciente)
HistorialClinico(id, paciente, turnos, resumenPaciente)
HistorialClinico(id, paciente)
```

### Métodos

```java
getters/setters de todos los atributos
agregarTurno(Turno turno)
eliminarTurnoPorId(long idTurno)
actualizarTurno(Turno turnoActualizado)
toString()
```

### Relaciones

- Asociación bidireccional con `Paciente`.
- Agregación con `Turno`.

Tipos UML:

```text
Paciente 1 ◄──► 1 HistorialClinico
HistorialClinico 1 ◇── 0..* Turno
```

Justificación:

- El historial pertenece a un paciente.
- El historial agrupa turnos del paciente.
- Los turnos pueden existir como entidades propias en `RepositorioTurno`, por eso con `Turno` conviene modelarlo como agregación y no como composición estricta.

## 1.4 Odontologo

Clase abstracta que representa un profesional odontológico.

### Atributos

```java
private long id;
private String nombre;
private String apellido;
private String matricula;
private Especialidad especialidad;
```

### Constructores

Simplificado:

```java
Odontologo()
Odontologo(id, nombre, apellido, matricula)
Odontologo(id, nombre, apellido, matricula, especialidad)
```

### Métodos

```java
getters/setters de todos los atributos
abstract int calcularDuracionConsulta()
getNombreCompleto()
getPresentacion()
toString()
```

### Relaciones

- Herencia con `OdontologoOrtodoncista`.
- Herencia con `OdontologoEndodoncista`.
- Asociación con `Turno`.

Tipos UML:

```text
Odontologo <|── OdontologoOrtodoncista
Odontologo <|── OdontologoEndodoncista
Odontologo 1 ── 0..* Turno
```

Notas:

- `Odontologo` es abstracta.
- Aplica polimorfismo mediante `calcularDuracionConsulta()`.

## 1.5 OdontologoOrtodoncista

Subclase concreta de `Odontologo`.

### Atributos

No declara atributos propios. Hereda los atributos de `Odontologo`.

### Constructores

Simplificado:

```java
OdontologoOrtodoncista()
OdontologoOrtodoncista(id, nombre, apellido, matricula)
```

### Métodos

```java
calcularDuracionConsulta() -> 45
toString()
```

### Relaciones

- Hereda de `Odontologo`.

Tipo UML:

```text
Odontologo <|── OdontologoOrtodoncista
```

## 1.6 OdontologoEndodoncista

Subclase concreta de `Odontologo`.

### Atributos

No declara atributos propios. Hereda los atributos de `Odontologo`.

### Constructores

Simplificado:

```java
OdontologoEndodoncista()
OdontologoEndodoncista(id, nombre, apellido, matricula)
```

### Métodos

```java
calcularDuracionConsulta() -> 60
toString()
```

### Relaciones

- Hereda de `Odontologo`.

Tipo UML:

```text
Odontologo <|── OdontologoEndodoncista
```

## 1.7 Turno

Clase concreta que representa una reserva de atención odontológica.

### Atributos

```java
private long id;
private Paciente paciente;
private Odontologo odontologo;
private LocalDate fecha;
private LocalTime hora;
private EstadoTurno estado;
private Consultorio consultorio;
```

### Constructores

Simplificado:

```java
Turno()
Turno(id, paciente, odontologo, fecha, hora, estado)
Turno(id, paciente, odontologo, fecha, hora, estado, consultorio)
```

### Métodos

```java
getters/setters de todos los atributos
getDetalle()
toString()
```

### Relaciones

- Asociación con `Paciente`.
- Asociación con `Odontologo`.
- Asociación con `EstadoTurno`.
- Asociación opcional con `Consultorio`.
- Agregación desde `HistorialClinico`.

Tipos UML:

```text
Turno 0..* ── 1 Paciente
Turno 0..* ── 1 Odontologo
Turno 1 ── 1 EstadoTurno
Turno 0..* ── 0..1 Consultorio
HistorialClinico 1 ◇── 0..* Turno
```

Notas:

- `Turno` guarda referencias directas al paciente y al odontólogo.
- `EstadoTurno` modela el ciclo de vida del turno.
- `Consultorio` es opcional actualmente.

## 1.8 Consultorio

Clase concreta que representa el lugar físico donde se atiende un turno.

### Atributos

```java
private long id;
private int numero;
private int piso;
```

### Constructores

Simplificado:

```java
Consultorio()
Consultorio(id, numero, piso)
Consultorio(id, numero)
```

### Métodos

```java
getters/setters de todos los atributos
toString()
```

### Relaciones

- Asociación opcional con `Turno`.

Tipo UML:

```text
Turno 0..* ── 0..1 Consultorio
```

## 1.9 Especialidad

Enum que representa especialidades odontológicas.

### Valores

```java
ORTODONCIA
ENDODONCIA
PERIODONCIA
PROTESIS
CIRUGIA
```

### Relaciones

- Asociación con `Odontologo`.

Tipo UML:

```text
Odontologo 1 ── 1 Especialidad
```

## 1.10 EstadoTurno

Enum que representa el estado del turno.

### Valores

```java
PENDIENTE
CONFIRMADO
CANCELADO
COMPLETADO
```

### Relaciones

- Asociación con `Turno`.

Tipo UML:

```text
Turno 1 ── 1 EstadoTurno
```

### Flujo lógico de estados

```text
PENDIENTE -> CONFIRMADO
PENDIENTE -> CANCELADO
CONFIRMADO -> CANCELADO
CONFIRMADO -> COMPLETADO
```

Estados cerrados:

```text
CANCELADO
COMPLETADO
```

Desde `CANCELADO` o `COMPLETADO` no se permite avanzar a otro estado.

## 2. Repository

## 2.1 iRepository<T>

Interfaz genérica para repositorios CRUD.

### Métodos

```java
void guardar(T t)
T buscarPorId(long id)
void eliminarPorId(long id)
void actualizar(T t)
List<T> listarTodos()
```

### Relaciones

- Contrato genérico de repositorio.
- Los repositorios concretos tienen la misma forma de métodos.

Tipo UML:

```text
iRepository<T> <|.. RepositorioPaciente       // implementación esperada conceptualmente
iRepository<T> <|.. RepositorioOdontologo     // implementación esperada conceptualmente
iRepository<T> <|.. RepositorioTurno          // implementación esperada conceptualmente
```

Nota:

- En el código actual los repositorios concretos no declaran `implements iRepository<T>`.
- Conceptualmente, deberían implementar esta interfaz para cumplir mejor con OCP y programación contra interfaces.

## 2.2 RepositorioPaciente

Repositorio en memoria para pacientes.

### Atributos

```java
private List<Paciente> pacientes;
private long contadorId;
```

### Constructores

```java
RepositorioPaciente()
```

### Métodos

```java
guardar(Paciente paciente)
buscarPorId(long id)
eliminarPorId(long id)
actualizar(Paciente paciente)
listarTodos()
```

### Relaciones

- Agregación con `Paciente`.

Tipo UML:

```text
RepositorioPaciente 1 ◇── 0..* Paciente
```

## 2.3 RepositorioOdontologo

Repositorio en memoria para odontólogos.

### Atributos

```java
private List<Odontologo> odontologos;
private long contadorId;
```

### Constructores

```java
RepositorioOdontologo()
```

### Métodos

```java
guardar(Odontologo odontologo)
buscarPorId(long id)
eliminarPorId(long id)
actualizar(Odontologo odontologo)
listarTodos()
```

### Relaciones

- Agregación con `Odontologo`.

Tipo UML:

```text
RepositorioOdontologo 1 ◇── 0..* Odontologo
```

## 2.4 RepositorioTurno

Repositorio en memoria para turnos.

### Atributos

```java
private List<Turno> turnos;
private long contadorId;
```

### Constructores

```java
RepositorioTurno()
```

### Métodos

```java
guardar(Turno turno)
buscarPorId(long id)
eliminarPorId(long id)
actualizar(Turno turno)
listarTodos()
```

### Relaciones

- Agregación con `Turno`.

Tipo UML:

```text
RepositorioTurno 1 ◇── 0..* Turno
```

## 3. Service

## 3.1 iService<T>

Interfaz genérica para servicios CRUD.

### Métodos

```java
boolean guardar(T t)
T buscarPorId(long id)
boolean eliminarPorId(long id)
boolean actualizar(T t)
List<T> listarTodos()
```

### Relaciones

- Implementada por servicios concretos.

Tipo UML:

```text
iService<Paciente> <|.. PacienteServiceImpl
iService<Odontologo> <|.. OdontologoServiceImpl
iService<Turno> <|.. TurnoServiceImpl
```

## 3.2 PacienteServiceImpl

Servicio que gestiona pacientes, validaciones, creación de domicilio e historial clínico.

### Atributos

```java
private static final String SOLO_LETRAS;
private static final String EMAIL_VALIDO;
private RepositorioPaciente repositorio;
```

### Constructores

```java
PacienteServiceImpl()
```

### Métodos CRUD

```java
guardar(Paciente paciente)
buscarPorId(long id)
eliminarPorId(long id)
actualizar(Paciente paciente)
listarTodos()
```

### Métodos de caso de uso

```java
registrarPaciente(nombre, apellido, dni, email, fechaIngreso, calle, numeroCalle, localidad, provincia)
actualizarPaciente(id, nombre, apellido, dni, email, fechaIngreso, calle, numeroCalle, localidad, provincia)
obtenerHistorialClinico(long idPaciente)
```

### Métodos privados

```java
validarDatos(Paciente paciente)
crearDomicilio(calle, numeroCalle, localidad, provincia)
existeDniEnOtroPaciente(dni, idPaciente)
asegurarHistorialClinico(Paciente paciente)
```

### Relaciones

- Implementación de `iService<Paciente>`.
- Asociación/dependencia con `RepositorioPaciente`.
- Crea `Domicilio`.
- Crea o asegura `HistorialClinico`.
- Usa `Paciente`.

Tipos UML:

```text
iService<Paciente> <|.. PacienteServiceImpl
PacienteServiceImpl --> RepositorioPaciente
PacienteServiceImpl ..> Paciente
PacienteServiceImpl ..> Domicilio
PacienteServiceImpl ..> HistorialClinico
```

## 3.3 OdontologoServiceImpl

Servicio que gestiona odontólogos, validaciones y creación de subclases concretas.

### Atributos

```java
private static final String SOLO_LETRAS;
private static final String MATRICULA_VALIDA;
private RepositorioOdontologo repositorio;
```

### Constructores

```java
OdontologoServiceImpl()
```

### Métodos CRUD

```java
guardar(Odontologo odontologo)
buscarPorId(long id)
eliminarPorId(long id)
actualizar(Odontologo odontologo)
listarTodos()
```

### Métodos de caso de uso

```java
registrarOdontologo(nombre, apellido, matricula, tipoEspecialista)
actualizarOdontologo(id, nombre, apellido, matricula, tipoEspecialista)
```

### Métodos privados

```java
validarDatos(Odontologo odontologo)
crearOdontologo(id, nombre, apellido, matricula, tipoEspecialista)
existeMatriculaEnOtroOdontologo(matricula, idOdontologo)
```

### Relaciones

- Implementación de `iService<Odontologo>`.
- Asociación/dependencia con `RepositorioOdontologo`.
- Crea `OdontologoOrtodoncista`.
- Crea `OdontologoEndodoncista`.
- Usa polimorfismo con `Odontologo`.

Tipos UML:

```text
iService<Odontologo> <|.. OdontologoServiceImpl
OdontologoServiceImpl --> RepositorioOdontologo
OdontologoServiceImpl ..> Odontologo
OdontologoServiceImpl ..> OdontologoOrtodoncista
OdontologoServiceImpl ..> OdontologoEndodoncista
```

## 3.4 TurnoServiceImpl

Servicio que gestiona turnos, validaciones, disponibilidad, historial clínico y estados.

### Atributos

```java
private static final LocalTime HORA_APERTURA;
private static final LocalTime HORA_CIERRE;
private RepositorioTurno repositorio;
private PacienteServiceImpl servicioPaciente;
private OdontologoServiceImpl servicioOdontologo;
```

### Constructores

```java
TurnoServiceImpl(PacienteServiceImpl servicioPaciente, OdontologoServiceImpl servicioOdontologo)
```

### Métodos CRUD

```java
guardar(Turno turno)
buscarPorId(long id)
eliminarPorId(long id)
actualizar(Turno turno)
listarTodos()
```

### Métodos de caso de uso

```java
registrarTurno(idPaciente, idOdontologo, fecha, hora)
actualizarTurno(id, idPaciente, idOdontologo, fecha, hora)
confirmarTurno(long id)
cancelarTurno(long id)
completarTurno(long id)
```

### Métodos privados

```java
validarDatos(Turno turno)
crearTurno(id, idPaciente, idOdontologo, fecha, hora, estado)
odontologoOcupado(Turno turno, long idTurno)
cambiarEstadoTurno(long id, EstadoTurno nuevoEstado)
estadoPermitido(EstadoTurno estadoActual, EstadoTurno nuevoEstado)
agregarTurnoAlHistorial(Turno turno)
quitarTurnoDelHistorial(Turno turno)
actualizarTurnoEnHistorial(Turno turno)
```

### Relaciones

- Implementación de `iService<Turno>`.
- Asociación/dependencia con `RepositorioTurno`.
- Asociación/dependencia con `PacienteServiceImpl`.
- Asociación/dependencia con `OdontologoServiceImpl`.
- Crea `Turno`.
- Usa `EstadoTurno`.
- Actualiza `HistorialClinico`.

Tipos UML:

```text
iService<Turno> <|.. TurnoServiceImpl
TurnoServiceImpl --> RepositorioTurno
TurnoServiceImpl --> PacienteServiceImpl
TurnoServiceImpl --> OdontologoServiceImpl
TurnoServiceImpl ..> Turno
TurnoServiceImpl ..> EstadoTurno
TurnoServiceImpl ..> HistorialClinico
```

## 4. View

## 4.1 Dato

Clase abstracta base para datos ingresados desde la vista.

### Atributos

```java
protected int tipo;
```

### Métodos

```java
getTipo()
```

### Relaciones

- Herencia con `DatoPaciente`.
- Herencia con `DatoOdontologo`.
- Herencia con `DatoTurno`.

Tipo UML:

```text
Dato <|── DatoPaciente
Dato <|── DatoOdontologo
Dato <|── DatoTurno
```

## 4.2 DatoPaciente

Clase que transporta los datos ingresados para crear o actualizar pacientes.

### Atributos

```java
private String nombre;
private String apellido;
private int dni;
private String email;
private LocalDate fechaIngreso;
private String calle;
private int numeroCalle;
private String localidad;
private String provincia;
```

### Constructores

```java
DatoPaciente()
```

### Métodos

```java
getters/setters de todos los atributos
```

### Relaciones

- Hereda de `Dato`.
- Usada por `VistaClinica`.
- Usada por `ClinicaController`.

Tipo UML:

```text
Dato <|── DatoPaciente
VistaClinica ..> DatoPaciente
ClinicaController ..> DatoPaciente
```

## 4.3 DatoOdontologo

Clase que transporta los datos ingresados para crear o actualizar odontólogos.

### Atributos

```java
private String nombre;
private String apellido;
private String matricula;
private int tipoEspecialista;
```

### Constructores

```java
DatoOdontologo()
```

### Métodos

```java
getters/setters de todos los atributos
```

### Relaciones

- Hereda de `Dato`.
- Usada por `VistaClinica`.
- Usada por `ClinicaController`.

Tipo UML:

```text
Dato <|── DatoOdontologo
VistaClinica ..> DatoOdontologo
ClinicaController ..> DatoOdontologo
```

## 4.4 DatoTurno

Clase que transporta los datos ingresados para crear o actualizar turnos.

### Atributos

```java
private long idPaciente;
private long idOdontologo;
private LocalDate fecha;
private LocalTime hora;
```

### Constructores

```java
DatoTurno()
```

### Métodos

```java
getters/setters de todos los atributos
```

### Relaciones

- Hereda de `Dato`.
- Usada por `VistaClinica`.
- Usada por `ClinicaController`.

Tipo UML:

```text
Dato <|── DatoTurno
VistaClinica ..> DatoTurno
ClinicaController ..> DatoTurno
```

## 4.5 VistaClinica

Clase responsable de la interacción por consola.

### Atributos

```java
private Scanner scanner;
```

### Constructores

```java
VistaClinica()
```

### Métodos de menú

```java
mostrarMenuInicial()
mostrarMenuPacientes()
mostrarMenuOdontologos()
mostrarMenuTurnos()
```

### Métodos de entrada

```java
pedirOpcion()
pedirId()
pedirConfirmacion(String mensaje)
pedirDatosPaciente()
pedirDatosOdontologo()
pedirDatosTurno()
```

### Métodos de salida

```java
mostrarMensaje(String mensaje)
mostrarError(String error)
pausar()
cerrar()
```

### Relaciones

- Crea `DatoPaciente`.
- Crea `DatoOdontologo`.
- Crea `DatoTurno`.
- Usa `Scanner`.
- Es usada por `ClinicaController`.

Tipos UML:

```text
VistaClinica ..> DatoPaciente
VistaClinica ..> DatoOdontologo
VistaClinica ..> DatoTurno
ClinicaController --> VistaClinica
```

## 5. Controller

## 5.1 ClinicaController

Clase que coordina la vista con los servicios.

### Atributos

```java
private VistaClinica vista;
private PacienteServiceImpl servicioPaciente;
private OdontologoServiceImpl servicioOdontologo;
private TurnoServiceImpl servicioTurno;
private boolean ejecutando;
```

### Constructores

```java
ClinicaController(VistaClinica vista)
```

### Métodos principales

```java
run()
salir()
```

### Métodos de pacientes

```java
menuPacientes()
registrarPaciente()
buscarPaciente()
eliminarPaciente()
actualizarPaciente()
listarPacientes()
mostrarHistorialClinico()
```

### Métodos de odontólogos

```java
menuOdontologos()
registrarOdontologo()
buscarOdontologo()
eliminarOdontologo()
actualizarOdontologo()
listarOdontologos()
```

### Métodos de turnos

```java
menuTurnos()
registrarTurno()
buscarTurno()
eliminarTurno()
actualizarTurno()
listarTurnos()
confirmarTurno()
cancelarTurno()
completarTurno()
```

### Relaciones

- Implementa `Runnable`.
- Asociación con `VistaClinica`.
- Asociación/dependencia con `PacienteServiceImpl`.
- Asociación/dependencia con `OdontologoServiceImpl`.
- Asociación/dependencia con `TurnoServiceImpl`.
- Usa `DatoPaciente`, `DatoOdontologo`, `DatoTurno`.
- Usa entidades solo para mostrar resultados: `Paciente`, `Odontologo`, `Turno`, `HistorialClinico`.

Tipos UML:

```text
Runnable <|.. ClinicaController
ClinicaController --> VistaClinica
ClinicaController --> PacienteServiceImpl
ClinicaController --> OdontologoServiceImpl
ClinicaController --> TurnoServiceImpl
ClinicaController ..> DatoPaciente
ClinicaController ..> DatoOdontologo
ClinicaController ..> DatoTurno
```

Nota importante:

- `ClinicaController` no debe crear entidades de dominio.
- No debe validar reglas de negocio.
- No debe decidir estados de turno.
- Solo debe comunicar vista y servicios.

## 6. Main

## 6.1 Main

Punto de entrada del programa.

### Métodos

```java
public static void main(String[] args)
```

### Relaciones

- Crea `VistaClinica`.
- Crea `ClinicaController`.
- Ejecuta `controller.run()`.

Tipo UML:

```text
Main ..> VistaClinica
Main ..> ClinicaController
```

## 7. ValidacionesTest

Clase de prueba manual sin JUnit ni dependencias externas.

### Atributos

```java
private static int pruebasEjecutadas;
private static int pruebasFallidas;
```

### Métodos

```java
main(String[] args)
probarValidacionesPaciente()
probarValidacionesOdontologo()
probarValidacionesTurno()
probarHistorialYEstados()
esperarVerdadero(String nombrePrueba, boolean resultado)
esperarFalso(String nombrePrueba, boolean resultado)
```

### Relaciones

- Usa `PacienteServiceImpl`.
- Usa `OdontologoServiceImpl`.
- Usa `TurnoServiceImpl`.
- Usa `Paciente`, `Turno`, `EstadoTurno`.

Tipo UML:

```text
ValidacionesTest ..> PacienteServiceImpl
ValidacionesTest ..> OdontologoServiceImpl
ValidacionesTest ..> TurnoServiceImpl
ValidacionesTest ..> Paciente
ValidacionesTest ..> Turno
ValidacionesTest ..> EstadoTurno
```

## 8. Resumen de relaciones principales del dominio

```text
Paciente 1 ◆── 1 Domicilio
Paciente 1 ◄──► 1 HistorialClinico
HistorialClinico 1 ◇── 0..* Turno
Turno 0..* ── 1 Paciente
Turno 0..* ── 1 Odontologo
Turno 1 ── 1 EstadoTurno
Turno 0..* ── 0..1 Consultorio
Odontologo 1 ── 1 Especialidad
Odontologo <|── OdontologoOrtodoncista
Odontologo <|── OdontologoEndodoncista
```

## 9. Resumen de relaciones de capas

```text
Main -> ClinicaController -> VistaClinica
Main -> ClinicaController -> Services
Services -> Repositories
Services -> Entities
Repositories -> Entities
View -> Dato*
Controller -> Dato*
Controller -> Services
```

## 10. Tipos de relación usados

### Herencia

```text
Odontologo <|── OdontologoOrtodoncista
Odontologo <|── OdontologoEndodoncista
Dato <|── DatoPaciente
Dato <|── DatoOdontologo
Dato <|── DatoTurno
```

### Implementación

```text
iService<Paciente> <|.. PacienteServiceImpl
iService<Odontologo> <|.. OdontologoServiceImpl
iService<Turno> <|.. TurnoServiceImpl
Runnable <|.. ClinicaController
```

Implementación conceptual recomendada:

```text
iRepository<Paciente> <|.. RepositorioPaciente
iRepository<Odontologo> <|.. RepositorioOdontologo
iRepository<Turno> <|.. RepositorioTurno
```

### Composición

```text
Paciente 1 ◆── 1 Domicilio
```

### Agregación

```text
HistorialClinico 1 ◇── 0..* Turno
RepositorioPaciente 1 ◇── 0..* Paciente
RepositorioOdontologo 1 ◇── 0..* Odontologo
RepositorioTurno 1 ◇── 0..* Turno
```

### Asociación normal

```text
Turno ── Paciente
Turno ── Odontologo
Turno ── EstadoTurno
Turno ── Consultorio
Odontologo ── Especialidad
ClinicaController ── VistaClinica
ClinicaController ── Services
Services ── Repositories
```

### Asociación bidireccional

```text
Paciente ◄──► HistorialClinico
```

## 11. Reglas de negocio relevantes

### Paciente

- Nombre obligatorio.
- Nombre solo con letras.
- Apellido obligatorio.
- Apellido solo con letras.
- DNI obligatorio entre 7 y 8 dígitos.
- DNI no duplicado.
- Email con formato válido.
- Fecha de ingreso no futura.
- Domicilio obligatorio.
- Número de calle mayor a 0.
- Localidad y provincia solo con letras.

### Odontólogo

- Nombre obligatorio.
- Nombre solo con letras.
- Apellido obligatorio.
- Apellido solo con letras.
- Matrícula obligatoria.
- Matrícula no duplicada.
- Matrícula alfanumérica o con guiones, entre 3 y 20 caracteres.
- Tipo de especialista válido: `1` ortodoncista, `2` endodoncista.

### Turno

- Paciente existente.
- Odontólogo existente.
- Fecha no pasada.
- Hora obligatoria.
- Horario entre `08:00` y antes de `20:00`.
- Turnos cada 30 minutos.
- No se permite superponer turnos del mismo odontólogo en misma fecha y hora.
- Los turnos cancelados no bloquean disponibilidad.

### EstadoTurno

- Todo turno nuevo inicia como `PENDIENTE`.
- Un turno pendiente puede confirmarse o cancelarse.
- Un turno confirmado puede cancelarse o completarse.
- Un turno cancelado no puede cambiar de estado.
- Un turno completado no puede cambiar de estado.

### HistorialClinico

- Todo paciente debe tener historial clínico.
- Al registrar un turno, se agrega al historial.
- Al actualizar un turno, se actualiza en el historial.
- Al eliminar un turno, se elimina del historial.
- Al cambiar el estado de un turno, el historial refleja el nuevo estado.

## 12. Diagrama textual general

```text
Main
  └── ClinicaController implements Runnable
        ├── VistaClinica
        │     ├── DatoPaciente extends Dato
        │     ├── DatoOdontologo extends Dato
        │     └── DatoTurno extends Dato
        ├── PacienteServiceImpl implements iService<Paciente>
        │     ├── RepositorioPaciente
        │     │     └── List<Paciente>
        │     ├── Paciente
        │     │     ├── Domicilio
        │     │     └── HistorialClinico
        │     │           └── List<Turno>
        │     └── Domicilio
        ├── OdontologoServiceImpl implements iService<Odontologo>
        │     ├── RepositorioOdontologo
        │     │     └── List<Odontologo>
        │     └── Odontologo abstract
        │           ├── OdontologoOrtodoncista
        │           └── OdontologoEndodoncista
        └── TurnoServiceImpl implements iService<Turno>
              ├── RepositorioTurno
              │     └── List<Turno>
              ├── PacienteServiceImpl
              ├── OdontologoServiceImpl
              └── Turno
                    ├── Paciente
                    ├── Odontologo
                    ├── EstadoTurno
                    └── Consultorio opcional
```
