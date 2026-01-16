# Dragolandia

## Introducción
Dragolandia es un juego de batalla medieval orientado a enfrentamientos entre seres antropomorfos, magos, monstruos y dragones. El diseño del código se basa en programación orientada a objetos: cada entidad del juego se representa mediante una clase con atributos y métodos que modelan su comportamiento.

Clases y métodos principales (introducción al juego):
- **`SeresAntropomorfos`**: clase base para entidades vivas.
	- Atributos: `vida`, `nombre`.
	- Métodos: `recibirDaño()` — aplica daño a la entidad; `estaVivo()` — comprueba si `vida > 0`.
- **`Monstruo`**: hereda de `SeresAntropomorfos`.
	- Atributos: `id`, `TipoMonstruo tipo`, `fuerza`.
	- Método: `atacar()` — lógica de ataque cuerpo a cuerpo o automática para monstruos.
- **`Mago`**: hereda de `SeresAntropomorfos`.
	- Atributos: `id`, `nivelMagia`, `conjuros` (colección de `Hechizo`).
	- Método: `lanzarHechizo()` — selecciona y aplica un `Hechizo` sobre un objetivo.
- **`Bosque`**:
	- Atributos: `id`, `nombre`, `Monstruo monstruo` (jefe o habitante), `peligro`.
	- Métodos: `mostrarJefe()`, `cambiarJefe()` — gestión de monstruos/jefes del bosque.
- **`Dragon`**:
	- Atributos: `nombre`, `resistencia`, `intensidadFuego`.
	- Método: `exalar()` — ataque de fuego con daño según `intensidadFuego`.
- **`Hechizo`**:
	- Atributos: `nombre`, `efecto` (valor o tipo de efecto sobre objetivo).
	- Método: `aplicar()` — ejecuta la lógica del hechizo (daño, curación, efectos secundarios).

Estas clases cooperan para modelar combates por turnos o secuenciales: los `Mago` usan `Hechizo`, los `Monstruo` atacan, y el `Dragon` actúa como jefe del `Bosque`.

## Análisis
- Lenguaje y herramientas: el proyecto está implementado en Java y gestionado con Maven (`pom.xml` presente). Se utiliza OOP (herencia y polimorfismo) para compartir comportamiento entre entidades.
- Arquitectura y patrones: la presencia de clases como `Controller` y `Vista` sugiere una separación tipo MVC para manejar la lógica de juego y la interacción con el usuario.
- Persistencia/configuración: existe `hibernate.cfg.xml` en `resources`, por lo que se ha preparado/considerado el soporte de persistencia con Hibernate (mapear entidades y guardar estados si se requiere).
- Colecciones y relaciones: los `Mago` mantienen colecciones de `Hechizo`; los `Bosque` contienen colecciones o referencias a `Monstruo` y un `Dragon` como jefe.
- Consideraciones de diseño: las interacciones (ataque, hechizos, recibir daño) están encapsuladas en métodos concretos para facilitar pruebas unitarias y futura extensión (nuevos hechizos, tipos de monstruo, niveles de magia).

## Diagrama de clases
https://www.mermaidchart.com/d/aa83de10-9148-498b-bfeb-ccb5556aa792

## PDF Capturas
https://drive.google.com/file/d/1dXsblFsMwtsUbJ5RTOp2DqkEBrfSvv8Z/view?usp=sharing

## Diseño del juego
- Mecánica: combates por turnos donde cada entidad puede realizar acciones (atacar, lanzar hechizo, usar habilidad). Los hechizos aplican efectos directos (daño/curación) o estados temporales.
- Progresión: los `Mago` tienen `nivelMagia` que potencia los `Hechizo` o permite nuevos conjurios.
- Entornos: los `Bosque` tienen peligro variable; cada bosque puede tener varios `Monstruo` y un `Dragon` jefe.
- Balance: parámetros como `fuerza`, `resistencia`, `intensidadFuego` y `efecto` de `Hechizo` deben ajustarse para equilibrar retos y recompensas.

## Modelo Entidad-Relación
- Entidades principales: `Mago`, `Hechizo`, `Dragon`, `Bosque`, `Monstruo`.
- Relaciones clave:
	- `Mago` 1 — * `Hechizo` : un mago conoce varios hechizos.
	- `Bosque` 1 — * `Monstruo` : varios monstruos habitan un bosque.
	- `Bosque` 1 — 1 `Dragon` : cada bosque tiene un dragon jefe (un dragon pertenece a un único bosque).
- Claves y FK (sugerido):
	- `Mago(id)` — `Hechizo(mago_id)` (o tabla intermedia si se modela muchos-a-muchos)
	- `Bosque(id)` — `Monstruo(bosque_id)`
	- `Bosque(id)` — `Dragon(bosque_id)`

