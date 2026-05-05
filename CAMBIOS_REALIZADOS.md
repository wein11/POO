# Cambios realizados en el proyecto

Este documento resume los cambios aplicados al sistema de gestión de la clínica odontológica "Sonrisa Feliz" y explica por qué se hicieron.

## Objetivo general de los cambios

El objetivo principal fue corregir la separación de responsabilidades entre capas, especialmente en `ClinicaController`, y hacer que clases ya existentes como `HistorialClinico`, `Turno` y `EstadoTurno` tengan un uso real dentro del sistema.

El proyecto sigue trabajando con consola y mensajes de texto para errores porque corresponde a Entrega 2. No se agregaron excepciones personalizadas, ya que eso pertenece a la Entrega 3.

## 1. Refactor de ClinicaController

### Qué se cambió

Se simplificó `ClinicaController` para que deje de construir entidades directamente.

Antes, el controller creaba objetos como:

```java
Domicilio domicilio = new Domicilio(...);
Paciente paciente = new Paciente(...);
Turno turno = new Turno(...);
```

También decidía qué subclase de odontólogo crear:

```java
if (dato.getTipoEspecialista() == 1) {
    odontologo = new OdontologoOrtodoncista(...);
} else {
    odontologo = new OdontologoEndodoncista(...);
}
```

Ahora el controller solamente:

- pide datos a la vista;
- valida si la vista devolvió `null` por error de formato;
- llama al service correspondiente;
- muestra un mensaje de éxito si el service devuelve `true`;
- pausa la consola.

### Por qué se cambió

El profesor indicó que el controller estaba haciendo cosas que no correspondían. En una arquitectura en capas, el controller no debe contener lógica de negocio ni decidir cómo se construyen las entidades.

Su responsabilidad debe ser coordinar la comunicación entre vista y servicios.

Con este cambio, `ClinicaController` queda alineado con el patrón GRASP Controller: recibe la acción del usuario y delega el trabajo al servicio correspondiente.

## 2. Nuevos métodos de alto nivel en PacienteServiceImpl

### Qué se cambió

Se agregaron métodos específicos para registrar y actualizar pacientes:

```java
registrarPaciente(...)
actualizarPaciente(...)
```

Estos métodos reciben los datos simples que vienen desde el controller, crean internamente el `Domicilio` y el `Paciente`, y luego usan los métodos existentes `guardar` o `actualizar`.

También se agregó una validación de DNI duplicado al actualizar.

### Por qué se cambió

La creación de `Paciente` y `Domicilio` corresponde a la lógica de la capa de servicio, no al controller.

Además, validar DNI duplicado solo al registrar dejaba un problema: se podía actualizar un paciente y ponerle el DNI de otro paciente existente. Ahora esa regla se controla tanto en alta como en modificación.

## 3. Nuevos métodos de alto nivel en OdontologoServiceImpl

### Qué se cambió

Se agregaron métodos:

```java
registrarOdontologo(...)
actualizarOdontologo(...)
```

También se movió al service la decisión de qué tipo concreto de odontólogo crear:

- `OdontologoOrtodoncista` si la opción es `1`;
- `OdontologoEndodoncista` si la opción es `2`.

Además, se agregó validación de matrícula duplicada al actualizar.

### Por qué se cambió

El controller no debería conocer ni decidir qué subclase concreta corresponde crear. Esa decisión forma parte de la lógica del caso de uso.

También era necesario validar matrícula duplicada en actualizaciones para evitar inconsistencias.

## 4. Nuevos métodos de alto nivel en TurnoServiceImpl

### Qué se cambió

Se agregaron métodos:

```java
registrarTurno(...)
actualizarTurno(...)
```

Ahora `TurnoServiceImpl` se encarga de:

- buscar el paciente por ID;
- buscar el odontólogo por ID;
- crear el objeto `Turno`;
- asignar el estado inicial `PENDIENTE`;
- conservar el estado existente cuando se actualiza un turno;
- validar disponibilidad del odontólogo.

### Por qué se cambió

Antes el controller buscaba paciente y odontólogo, creaba el turno y decidía qué estado conservar. Eso era lógica de negocio.

La existencia de paciente, existencia de odontólogo, disponibilidad horaria y estado del turno son reglas propias del servicio de turnos.

## 5. Uso real de EstadoTurno

### Qué se cambió

Se agregaron operaciones para cambiar el estado de un turno:

```java
confirmarTurno(long id)
cancelarTurno(long id)
completarTurno(long id)
```

También se agregó un flujo válido de estados:

- `PENDIENTE -> CONFIRMADO`
- `PENDIENTE -> CANCELADO`
- `CONFIRMADO -> CANCELADO`
- `CONFIRMADO -> COMPLETADO`

Los estados `CANCELADO` y `COMPLETADO` quedan cerrados. Desde esos estados no se permite avanzar a otro.

### Por qué se cambió

Antes `EstadoTurno` existía, pero casi no tenía comportamiento real. Los turnos se creaban como `PENDIENTE`, pero no había forma de confirmarlos, cancelarlos o completarlos desde el sistema.

Con este cambio, el enum pasa a representar el ciclo de vida real de un turno.

## 6. Cambios en el menú de turnos

### Qué se cambió

En `VistaClinica`, el menú de turnos ahora incluye:

```text
6. Confirmar turno
7. Cancelar turno
8. Completar turno
```

En `ClinicaController`, se agregaron métodos para estas opciones:

```java
confirmarTurno()
cancelarTurno()
completarTurno()
```

### Por qué se cambió

Para que el usuario pueda usar realmente el ciclo de estados desde la interfaz por consola, no solo desde el código interno.

El controller sigue sin decidir si el cambio de estado es válido. Solamente pide el ID, llama al service y muestra el resultado.

## 7. Uso real de HistorialClinico

### Qué se cambió

Se reforzó la clase `HistorialClinico` para que:

- inicialice siempre la lista de turnos;
- no falle si la lista llega como `null`;
- permita agregar turnos;
- permita eliminar turnos por ID;
- permita actualizar un turno existente dentro del historial;
- muestre el historial en un formato más legible.

Se agregaron estos métodos:

```java
eliminarTurnoPorId(long idTurno)
actualizarTurno(Turno turnoActualizado)
```

### Por qué se cambió

La clase existía, pero no estaba conectada al flujo real del sistema. Además, podía fallar si se llamaba a `agregarTurno` cuando la lista estaba sin inicializar.

Ahora el historial clínico funciona como una entidad útil del dominio y refleja los turnos asociados al paciente.

## 8. Creación automática del historial clínico del paciente

### Qué se cambió

En `PacienteServiceImpl`, cuando se guarda o consulta un paciente, se asegura que tenga un `HistorialClinico`.

Se agregó:

```java
obtenerHistorialClinico(long idPaciente)
```

Este método busca el paciente y devuelve su historial.

### Por qué se cambió

Para que no haya pacientes sin historial clínico y para poder mostrarlo desde el sistema.

Esto mantiene la responsabilidad en la capa de servicio: el controller no crea ni inicializa historiales.

## 9. Sincronización entre Turno e HistorialClinico

### Qué se cambió

En `TurnoServiceImpl`, cuando se registra un turno, se agrega automáticamente al historial clínico del paciente.

Cuando se actualiza un turno, se actualiza también en el historial.

Cuando se elimina un turno, se elimina también del historial.

Cuando cambia el estado de un turno, el historial queda actualizado con el nuevo estado.

### Por qué se cambió

Si el historial clínico existe, debe reflejar lo que sucede con los turnos del paciente. Si no se sincroniza, el sistema puede mostrar información desactualizada o contradictoria.

Esta lógica pertenece al service porque coordina entidades del dominio y aplica reglas de negocio.

## 10. Cambios en el menú de pacientes

### Qué se cambió

En `VistaClinica`, el menú de pacientes ahora incluye:

```text
6. Mostrar historial clinico
```

En `ClinicaController`, se agregó:

```java
mostrarHistorialClinico()
```

### Por qué se cambió

Para que el usuario pueda consultar el historial clínico desde la consola, usando una operación visible del sistema.

El controller solamente pide el ID y delega la búsqueda al service.

## 11. Validación de disponibilidad considerando turnos cancelados

### Qué se cambió

La validación de disponibilidad del odontólogo ahora ignora turnos con estado `CANCELADO`.

### Por qué se cambió

Un turno cancelado no debería bloquear la agenda del odontólogo. Si un turno fue cancelado, ese horario puede volver a utilizarse.

## 12. Verificación realizada

Se compiló el proyecto con:

```bash
javac 'POO/Entrega1 Codigo/src/'*/*.java 'POO/Entrega1 Codigo/src/Main.java'
```

La compilación terminó sin errores.

También se probó un flujo real por consola:

1. Registrar paciente.
2. Registrar odontólogo.
3. Registrar turno.
4. Confirmar turno.
5. Mostrar historial clínico del paciente.

El historial mostró correctamente el turno asociado con estado `CONFIRMADO`.

Después de compilar y probar, se eliminaron los archivos `.class` generados para no ensuciar el proyecto.

## Resumen final

Los cambios dejan el proyecto mejor organizado para Entrega 2:

- `Controller` queda como comunicador entre vista y servicios.
- `Service` concentra reglas de negocio, creación de entidades y validaciones.
- `EstadoTurno` se usa para manejar el ciclo de vida del turno.
- `HistorialClinico` se conecta al sistema y refleja los turnos del paciente.
- No se agregaron excepciones porque todavía no corresponden a esta entrega.
