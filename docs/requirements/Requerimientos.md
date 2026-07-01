# DOSW-2026-POKEDEX-HernanDavidSanchez
Levantamiento requerimientos Pokédex

## 1. Descripción del proyecto

La Pokédex es una aplicación web inspirada en la Pokédex de Pokémon. El propósito es permitir que un entrenador cree su perfil, consulte la información de Pokémon, filtre el listado, guarde Pokémon de su interés y construya equipos para visualizar sus ventajas competitivas.

## 2. Clasificación de requerimientos

### 2.1 Requerimientos funcionales

| Código | Nombre | Descripción |
|--- | --- | --- |
| RF-01 | Crear perfil de usuario | Permitir que un usuario cree su perfil para acceder a la Pokédex. |
| RF-02 | Iniciar sesión mediante Gmail | Permitir que un usuario inicie sesión usando una cuenta vinculada a Gmail. |
| RF-03 | Registrar Pokémon | Permitir que el administrador registre un nuevo Pokémon en la Pokédex. |
| RF-04 | Actualizar Pokémon | Permitir que el administrador modifique la información de un Pokémon existente. |
| RF-05 | Eliminar Pokémon | Permitir que el administrador elimine o desactive un Pokémon registrado. |
| RF-06 | Mostrar listado de Pokémon | Permitir que el usuario consulte el listado de Pokémon disponibles. |
| RF-07 | Buscar Pokémon | Permitir que el usuario busque Pokémon por nombre o número de Pokédex. |
| RF-08 | Consultar detalle de Pokémon | Permitir que el usuario consulte la información completa de un Pokémon seleccionado. |
| RF-09 | Filtrar Pokémon | Permitir que el usuario filtre Pokémon por tipo, región, generación, estadística, habilidad, ataque aprendido o rol competitivo. |
| RF-10 | Ordenar listado de Pokémon | Permitir que el usuario ordene el listado por nombre, número o estadística. |
| RF-11 | Gestionar Pokémon favoritos | Permitir que el usuario agregue o retire Pokémon de su lista de favoritos. |
| RF-12 | Crear equipo Pokémon | Permitir que el usuario cree un equipo Pokémon con nombre propio. |
| RF-13 | Consultar equipos Pokémon | Permitir que el usuario consulte los equipos asociados a su perfil. |
| RF-14 | Editar equipo Pokémon | Permitir que el usuario modifique el nombre o la composición de un equipo, incluyendo agregar o retirar Pokémon. |
| RF-15 | Eliminar equipo Pokémon | Permitir que el usuario elimine un equipo asociado a su perfil. |
| RF-16 | Consultar estadísticas de Pokémon | Permitir que el usuario consulte tasa de elección en equipos y Pokémon populares. |
| RF-17 | Consultar métricas administrativas | Permitir que el administrador consulte métricas de uso, como cantidad de consultas por Pokémon. |
| RF-18 | Administrar perfiles de usuario | Permitir que el administrador consulte perfiles, actualice su estado y cambie roles según permisos. |

### 2.2 Requerimientos no funcionales

| Código | Nombre | Descripción |
|--- | --- | --- |
| RFN-01 | Rendimiento | Las búsquedas y consultas deben dar respuesta en menos de 2,5 segundos. |
| RFN-02 | Visual | La interfaz debe ser simple y fácil de usar. |
| RFN-03 | Multidispositivo | La aplicación debe funcionar correctamente tanto en computadores como en dispositivos móviles. |
| RFN-04 | Seguridad | La información de usuarios y administradores debe estar protegida y encriptada. |

## 3. Documento análisis de Requerimientos

### RF-01: Crear perfil de usuario

#### Funcionalidad

| Campo | Detalle |
| --- | --- |
| Código | RF-01 |
| Nombre | Crear perfil de usuario |
| Descripción | Permitir que un usuario cree su perfil para acceder a la Pokédex. |
| Cómo se ejecutará | Mediante un formulario de registro en la aplicación web. |
| Actor principal | Usuario visitante |
| Precondiciones | El usuario no debe tener una cuenta registrada con el mismo correo. |

#### Datos de Entrada

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
| --- | --- | --- | --- | --- |
| Nombre de usuario | Nombre visible del entrenador | Texto | Mínimo 3 caracteres | Sí |
| Correo electrónico | Correo asociado al perfil | Email | Debe tener formato válido | Sí |
| Contraseña | Clave de acceso | Password | Mínimo 8 caracteres | Sí |

#### Datos de Salida

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
| --- | --- | --- | --- | --- |
| Perfil creado | Datos iniciales del usuario | Objeto | Debe quedar almacenado en el sistema | Sí |
| Mensaje de registro | Confirmación del registro | Texto | Se muestra al finalizar el proceso | Sí |

#### Flujo Básico

| Paso | Actor | Descripción | Excepciones |
| --- | --- | --- | --- |
| 1 | Usuario | Ingresa a la opción de registro. | Página no disponible |
| 2 | Usuario | Diligencia nombre, correo y contraseña. | Campos vacíos |
| 3 | Sistema | Valida la información ingresada. | Correo inválido |
| 4 | Sistema | Crea el perfil del usuario. | Correo ya registrado |
| 5 | Sistema | Muestra confirmación de registro. | Error de almacenamiento |

#### Reglas de Negocio

| No. | Descripción |
| --- | --- |
| RN-01 | No puede existir más de un perfil con el mismo correo electrónico. |
| RN-02 | Todo perfil creado inicia con rol de usuario normal. |

### RF-02: Iniciar sesión mediante Gmail

#### Funcionalidad

| Campo | Detalle |
| --- | --- |
| Código | RF-02 |
| Nombre | Iniciar sesión mediante Gmail |
| Descripción | Permitir que un usuario inicie sesión usando una cuenta vinculada a Gmail. |
| Cómo se ejecutará | Mediante autenticación con Google. |
| Actor principal | Usuario registrado |
| Precondiciones | El usuario debe tener una cuenta de Gmail válida. |

#### Datos de Entrada

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
| --- | --- | --- | --- | --- |
| Cuenta Gmail | Cuenta seleccionada por el usuario | Email | Debe ser una cuenta válida de Google | Sí |

#### Datos de Salida

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
| --- | --- | --- | --- | --- |
| Sesión activa | Acceso autorizado a la aplicación | Token / Sesión | Debe identificar al usuario | Sí |
| Mensaje de acceso | Confirmación de ingreso | Texto | Se muestra al iniciar sesión | Sí |

#### Flujo Básico

| Paso | Actor | Descripción | Excepciones |
| --- | --- | --- | --- |
| 1 | Usuario | Selecciona la opción de iniciar sesión con Gmail. | Servicio no disponible |
| 2 | Sistema | Redirige a la autenticación de Google. | Error de conexión |
| 3 | Usuario | Selecciona su cuenta Gmail. | Cuenta cerrada |
| 4 | Sistema | Valida la identidad del usuario. | Cuenta no autorizada |
| 5 | Sistema | Permite el acceso a la Pokédex. | Error de sesión |

#### Reglas de Negocio

| No. | Descripción |
| --- | --- |
| RN-03 | Solo se permite inicio de sesión con cuentas Gmail. |
| RN-04 | El rol del usuario define las funcionalidades disponibles. |

### RF-03: Registrar Pokémon

#### Funcionalidad

| Campo | Detalle |
| --- | --- |
| Código | RF-03 |
| Nombre | Registrar Pokémon |
| Descripción | Permitir que el administrador registre un nuevo Pokémon en la Pokédex. |
| Cómo se ejecutará | Desde un formulario de creación en el panel administrativo. |
| Actor principal | Administrador |
| Precondiciones | El actor debe estar autenticado y tener rol administrador. |

#### Datos de Entrada

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
| --- | --- | --- | --- | --- |
| Número Pokédex | Identificador del Pokémon | Número | Debe ser único | Sí |
| Nombre | Nombre del Pokémon | Texto | No puede estar vacío | Sí |
| Tipo | Tipo primario y secundario | Lista | Al menos un tipo primario | Sí |
| Estadísticas | Datos base del Pokémon | Número | Valores mayores o iguales a cero | Sí |
| Habilidades | Habilidades del Pokémon | Lista | Debe tener al menos una | Sí |

#### Datos de Salida

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
| --- | --- | --- | --- | --- |
| Pokémon registrado | Información almacenada | Objeto | Debe quedar disponible para consulta | Sí |
| Mensaje de creación | Confirmación del registro | Texto | Debe indicar que la operación fue exitosa | Sí |

#### Flujo Básico

| Paso | Actor | Descripción | Excepciones |
| --- | --- | --- | --- |
| 1 | Administrador | Ingresa al formulario de creación de Pokémon. | Permisos insuficientes |
| 2 | Administrador | Diligencia los datos requeridos. | Campos incompletos |
| 3 | Sistema | Valida el número de Pokédex y los campos obligatorios. | Número Pokédex duplicado |
| 4 | Sistema | Guarda el nuevo Pokémon. | Error de almacenamiento |
| 5 | Sistema | Muestra confirmación de creación. | Ninguna |

#### Reglas de Negocio

| No. | Descripción |
| --- | --- |
| RN-05 | Solo los administradores pueden registrar Pokémon. |
| RN-06 | El número de Pokédex no puede repetirse. |

### RF-04: Actualizar Pokémon

#### Funcionalidad

| Campo | Detalle |
| --- | --- |
| Código | RF-04 |
| Nombre | Actualizar Pokémon |
| Descripción | Permitir que el administrador modifique la información de un Pokémon existente. |
| Cómo se ejecutará | Desde un formulario de edición en el panel administrativo. |
| Actor principal | Administrador |
| Precondiciones | El Pokémon debe existir y el actor debe tener rol administrador. |

#### Datos de Entrada

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
| --- | --- | --- | --- | --- |
| Pokémon seleccionado | Pokémon que será editado | ID / Número | Debe existir en el sistema | Sí |
| Datos actualizados | Información que será modificada | Texto / Lista / Número | Debe cumplir las validaciones del sistema | Sí |

#### Datos de Salida

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
| --- | --- | --- | --- | --- |
| Pokémon actualizado | Información modificada | Objeto | Debe reflejar los cambios realizados | Sí |
| Mensaje de actualización | Confirmación de edición | Texto | Debe indicar el resultado de la operación | Sí |

#### Flujo Básico

| Paso | Actor | Descripción | Excepciones |
| --- | --- | --- | --- |
| 1 | Administrador | Selecciona un Pokémon existente. | Pokémon no encontrado |
| 2 | Administrador | Modifica los campos permitidos. | Campos inválidos |
| 3 | Sistema | Valida los datos actualizados. | Número Pokédex duplicado |
| 4 | Sistema | Guarda los cambios. | Error de almacenamiento |
| 5 | Sistema | Muestra confirmación de actualización. | Ninguna |

#### Reglas de Negocio

| No. | Descripción |
| --- | --- |
| RN-07 | Solo los administradores pueden actualizar Pokémon. |
| RN-08 | La actualización no debe duplicar el número de Pokédex de otro Pokémon. |

### RF-05: Eliminar Pokémon

#### Funcionalidad

| Campo | Detalle |
| --- | --- |
| Código | RF-05 |
| Nombre | Eliminar Pokémon |
| Descripción | Permitir que el administrador elimine o desactive un Pokémon registrado. |
| Cómo se ejecutará | Desde una acción de eliminación en el panel administrativo. |
| Actor principal | Administrador |
| Precondiciones | El Pokémon debe existir y el actor debe tener rol administrador. |

#### Datos de Entrada

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
| --- | --- | --- | --- | --- |
| Pokémon seleccionado | Pokémon que será eliminado o desactivado | ID / Número | Debe existir en el sistema | Sí |
| Confirmación | Aprobación de la eliminación | Booleano | Debe ser aceptada por el administrador | Sí |

#### Datos de Salida

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
| --- | --- | --- | --- | --- |
| Estado del Pokémon | Registro eliminado o desactivado | Estado | No debe aparecer como disponible | Sí |
| Mensaje de eliminación | Confirmación de la acción | Texto | Debe indicar el resultado de la operación | Sí |

#### Flujo Básico

| Paso | Actor | Descripción | Excepciones |
| --- | --- | --- | --- |
| 1 | Administrador | Selecciona el Pokémon que desea eliminar. | Pokémon no encontrado |
| 2 | Sistema | Solicita confirmación de la acción. | Ninguna |
| 3 | Administrador | Confirma la eliminación. | Acción cancelada |
| 4 | Sistema | Elimina o desactiva el Pokémon. | Pokémon usado en equipos |
| 5 | Sistema | Muestra confirmación de eliminación. | Error de almacenamiento |

#### Reglas de Negocio

| No. | Descripción |
| --- | --- |
| RN-09 | Solo los administradores pueden eliminar Pokémon. |
| RN-10 | Si un Pokémon está asociado a equipos, el sistema debe desactivarlo o bloquear su eliminación. |

### RF-06: Mostrar listado de Pokémon

#### Funcionalidad

| Campo | Detalle |
| --- | --- |
| Código | RF-06 |
| Nombre | Mostrar listado de Pokémon |
| Descripción | Permitir que el usuario consulte el listado de Pokémon disponibles. |
| Cómo se ejecutará | Mediante una pantalla principal con tarjetas o tabla de Pokémon. |
| Actor principal | Usuario autenticado |
| Precondiciones | Debe existir información de Pokémon registrada. |

#### Datos de Entrada

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
| --- | --- | --- | --- | --- |
| Solicitud de listado | Acción de abrir el listado | Evento | Se ejecuta al entrar a la sección | Sí |

#### Datos de Salida

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
| --- | --- | --- | --- | --- |
| Listado Pokémon | Conjunto de Pokémon disponibles | Lista | Debe mostrar datos resumidos | Sí |

#### Flujo Básico

| Paso | Actor | Descripción | Excepciones |
| --- | --- | --- | --- |
| 1 | Usuario | Ingresa a la pantalla de Pokédex. | Usuario no autenticado |
| 2 | Sistema | Consulta los Pokémon activos. | Sin datos |
| 3 | Sistema | Muestra el listado con información resumida. | Error de carga |

#### Reglas de Negocio

| No. | Descripción |
| --- | --- |
| RN-11 | El listado debe mostrar únicamente Pokémon activos o disponibles. |

### RF-07: Buscar Pokémon

#### Funcionalidad

| Campo | Detalle |
| --- | --- |
| Código | RF-07 |
| Nombre | Buscar Pokémon |
| Descripción | Permitir que el usuario busque Pokémon por nombre total o parcial, o por número de Pokédex. |
| Cómo se ejecutará | Mediante campos de búsqueda en el listado de Pokémon. |
| Actor principal | Usuario autenticado |
| Precondiciones | Debe existir un listado de Pokémon disponible. |

#### Datos de Entrada

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
| --- | --- | --- | --- | --- |
| Nombre buscado | Nombre total o parcial del Pokémon | Texto | Debe tener al menos un carácter si se usa este criterio | No |
| Número Pokédex | Número del Pokémon buscado | Número | Debe ser mayor que cero si se usa este criterio | No |

#### Datos de Salida

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
| --- | --- | --- | --- | --- |
| Resultados de búsqueda | Pokémon que coinciden con el criterio ingresado | Lista / Objeto | Debe coincidir parcial o totalmente por nombre, o exactamente por número | Sí |

#### Flujo Básico

| Paso | Actor | Descripción | Excepciones |
| --- | --- | --- | --- |
| 1 | Usuario | Ingresa un nombre o número de Pokédex. | Campo vacío |
| 2 | Sistema | Valida el criterio ingresado. | Número inválido |
| 3 | Sistema | Busca coincidencias según el criterio. | Error de consulta |
| 4 | Sistema | Muestra los resultados encontrados. | Sin resultados |

#### Reglas de Negocio

| No. | Descripción |
| --- | --- |
| RN-12 | La búsqueda por nombre debe aceptar coincidencias parciales. |
| RN-13 | La búsqueda por número de Pokédex debe usar coincidencia exacta. |

### RF-08: Consultar detalle de Pokémon

#### Funcionalidad

| Campo | Detalle |
| --- | --- |
| Código | RF-08 |
| Nombre | Consultar detalle de Pokémon |
| Descripción | Permitir que el usuario consulte la información completa de un Pokémon seleccionado. |
| Cómo se ejecutará | Al seleccionar un Pokémon desde el listado o resultado de búsqueda. |
| Actor principal | Usuario autenticado |
| Precondiciones | El Pokémon debe existir en la Pokédex. |

#### Datos de Entrada

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
| --- | --- | --- | --- | --- |
| Pokémon seleccionado | Identificador del Pokémon | Número / ID | Debe existir en el sistema | Sí |

#### Datos de Salida

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
| --- | --- | --- | --- | --- |
| Detalle Pokémon | Información completa del Pokémon | Objeto | Debe incluir tipos, estadísticas, habilidades y evolución | Sí |

#### Flujo Básico

| Paso | Actor | Descripción | Excepciones |
| --- | --- | --- | --- |
| 1 | Usuario | Selecciona un Pokémon. | Pokémon no disponible |
| 2 | Sistema | Consulta la información asociada. | Error de consulta |
| 3 | Sistema | Muestra el detalle completo. | Información incompleta |

#### Reglas de Negocio

| No. | Descripción |
| --- | --- |
| RN-14 | El detalle debe mostrar información asociada al Pokémon seleccionado. |

### RF-09: Filtrar Pokémon

#### Funcionalidad

| Campo | Detalle |
| --- | --- |
| Código | RF-09 |
| Nombre | Filtrar Pokémon |
| Descripción | Permitir que el usuario filtre Pokémon por tipo, región, generación, estadística, habilidad, ataque aprendido o rol competitivo. |
| Cómo se ejecutará | Mediante controles de filtrado disponibles en el listado de Pokémon. |
| Actor principal | Usuario autenticado |
| Precondiciones | Debe existir un listado de Pokémon cargado con la información necesaria para aplicar los criterios de filtrado. |

#### Datos de Entrada

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
| --- | --- | --- | --- | --- |
| Tipo seleccionado | Tipo usado para filtrar | Lista | Debe corresponder a un tipo válido | No |
| Región seleccionada | Región usada para filtrar | Lista | Debe corresponder a una región válida | No |
| Generación seleccionada | Generación usada para filtrar | Lista / Número | Debe corresponder a una generación válida | No |
| Estadística seleccionada | Estadística usada para filtrar | Lista | Puede ser vida, ataque, defensa, ataque especial, defensa especial o velocidad | No |
| Valor mínimo | Umbral de comparación para la estadística | Número | Debe ser mayor o igual a cero | No |
| Habilidad seleccionada | Habilidad usada para filtrar | Texto / Lista | Debe existir en el sistema | No |
| Ataque seleccionado | Ataque usado para filtrar | Texto / Lista | Debe existir en el sistema | No |
| Rol competitivo | Rol usado para filtrar | Lista | Puede ser atacante, defensor, soporte o mixto | No |

#### Datos de Salida

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
| --- | --- | --- | --- | --- |
| Pokémon filtrados | Resultado ajustado a los criterios seleccionados | Lista | Debe incluir únicamente Pokémon que cumplan los filtros aplicados | Sí |

#### Flujo Básico

| Paso | Actor | Descripción | Excepciones |
| --- | --- | --- | --- |
| 1 | Usuario | Selecciona uno o más criterios de filtrado. | Criterio no disponible |
| 2 | Sistema | Valida los criterios seleccionados. | Valor inválido |
| 3 | Sistema | Aplica los filtros al listado de Pokémon. | Error de consulta |
| 4 | Sistema | Muestra los Pokémon filtrados. | Sin resultados |

#### Reglas de Negocio

| No. | Descripción |
| --- | --- |
| RN-15 | El filtro por tipo debe considerar tipo primario y secundario. |
| RN-16 | El filtro por región debe usar la región asociada al Pokémon. |
| RN-17 | El filtro por generación debe usar la generación de aparición del Pokémon. |
| RN-18 | Los valores de estadísticas deben ser mayores o iguales a cero. |
| RN-19 | El filtro debe considerar habilidades normales y ocultas si están registradas. |
| RN-20 | El filtro debe usar los ataques registrados para cada Pokémon. |
| RN-21 | El rol competitivo se clasifica como atacante, defensor, soporte o mixto. |

### RF-10: Ordenar listado de Pokémon

#### Funcionalidad

| Campo | Detalle |
| --- | --- |
| Código | RF-10 |
| Nombre | Ordenar listado de Pokémon |
| Descripción | Permitir que el usuario ordene el listado por nombre, número o estadística. |
| Cómo se ejecutará | Mediante controles de ordenamiento en el listado. |
| Actor principal | Usuario autenticado |
| Precondiciones | Debe existir un listado de Pokémon cargado. |

#### Datos de Entrada

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
| --- | --- | --- | --- | --- |
| Criterio de ordenamiento | Campo usado para ordenar | Lista | Puede ser nombre, número o estadística | Sí |
| Dirección de ordenamiento | Sentido del orden | Lista | Ascendente o descendente | Sí |

#### Datos de Salida

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
| --- | --- | --- | --- | --- |
| Listado ordenado | Pokémon organizados según el criterio | Lista | Debe mantener los filtros aplicados | Sí |

#### Flujo Básico

| Paso | Actor | Descripción | Excepciones |
| --- | --- | --- | --- |
| 1 | Usuario | Selecciona un criterio de ordenamiento. | Criterio no disponible |
| 2 | Usuario | Selecciona la dirección del orden. | Dirección inválida |
| 3 | Sistema | Ordena el listado mostrado. | Error de consulta |

#### Reglas de Negocio

| No. | Descripción |
| --- | --- |
| RN-22 | El ordenamiento debe respetar los filtros aplicados previamente. |

### RF-11: Gestionar Pokémon favoritos

#### Funcionalidad

| Campo | Detalle |
| --- | --- |
| Código | RF-11 |
| Nombre | Gestionar Pokémon favoritos |
| Descripción | Permitir que el usuario agregue o retire Pokémon de su lista de favoritos. |
| Cómo se ejecutará | Mediante acciones de agregar o quitar favorito en el listado, detalle o sección de favoritos. |
| Actor principal | Usuario autenticado |
| Precondiciones | El Pokémon debe existir y el usuario debe estar autenticado. |

#### Datos de Entrada

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
| --- | --- | --- | --- | --- |
| Pokémon seleccionado | Pokémon que se desea agregar o retirar de favoritos | ID | Debe existir en el sistema | Sí |
| Acción de favorito | Acción que se desea realizar | Lista | Puede ser agregar o quitar | Sí |

#### Datos de Salida

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
| --- | --- | --- | --- | --- |
| Lista de favoritos actualizada | Favoritos del usuario después de la acción | Lista / Registro | Debe reflejar si el Pokémon quedó asociado o retirado del perfil | Sí |

#### Flujo Básico

| Paso | Actor | Descripción | Excepciones |
| --- | --- | --- | --- |
| 1 | Usuario | Selecciona un Pokémon y elige agregar o quitar de favoritos. | Pokémon no disponible |
| 2 | Sistema | Valida el estado actual del favorito. | Favorito duplicado o no encontrado |
| 3 | Sistema | Agrega o elimina la relación de favorito. | Error de almacenamiento |
| 4 | Sistema | Muestra confirmación visual y actualiza la lista. | Ninguna |

#### Reglas de Negocio

| No. | Descripción |
| --- | --- |
| RN-23 | Un mismo Pokémon no debe duplicarse en la lista de favoritos del mismo usuario. |
| RN-24 | Solo el usuario dueño de la lista puede quitar sus favoritos. |

### RF-12: Crear equipo Pokémon

#### Funcionalidad

| Campo | Detalle |
| --- | --- |
| Código | RF-12 |
| Nombre | Crear equipo Pokémon |
| Descripción | Permitir que el usuario cree un equipo Pokémon con nombre propio. |
| Cómo se ejecutará | Mediante una pantalla de creación de equipo. |
| Actor principal | Usuario autenticado |
| Precondiciones | El usuario debe tener sesión activa. |

#### Datos de Entrada

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
| --- | --- | --- | --- | --- |
| Nombre del equipo | Identificación del equipo | Texto | No puede estar vacío | Sí |

#### Datos de Salida

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
| --- | --- | --- | --- | --- |
| Equipo creado | Equipo asociado al usuario | Objeto | Debe iniciar vacío o con selección posterior | Sí |

#### Flujo Básico

| Paso | Actor | Descripción | Excepciones |
| --- | --- | --- | --- |
| 1 | Usuario | Ingresa a la sección de equipos. | Usuario no autenticado |
| 2 | Usuario | Asigna nombre al equipo. | Nombre vacío |
| 3 | Sistema | Crea el equipo asociado al usuario. | Error de almacenamiento |
| 4 | Sistema | Muestra el equipo creado. | Ninguna |

#### Reglas de Negocio

| No. | Descripción |
| --- | --- |
| RN-25 | Un equipo debe pertenecer a un usuario registrado. |

### RF-13: Consultar equipos Pokémon

#### Funcionalidad

| Campo | Detalle |
| --- | --- |
| Código | RF-13 |
| Nombre | Consultar equipos Pokémon |
| Descripción | Permitir que el usuario consulte los equipos asociados a su perfil. |
| Cómo se ejecutará | Desde la sección Mis equipos. |
| Actor principal | Usuario autenticado |
| Precondiciones | El usuario debe tener un perfil activo. |

#### Datos de Entrada

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
| --- | --- | --- | --- | --- |
| Solicitud de equipos | Acción de abrir Mis equipos | Evento | Debe ejecutarse con sesión activa | Sí |

#### Datos de Salida

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
| --- | --- | --- | --- | --- |
| Lista de equipos | Equipos asociados al usuario | Lista | Debe mostrar equipos disponibles | Sí |

#### Flujo Básico

| Paso | Actor | Descripción | Excepciones |
| --- | --- | --- | --- |
| 1 | Usuario | Ingresa a la sección Mis equipos. | Usuario no autenticado |
| 2 | Sistema | Consulta los equipos del usuario. | Sin equipos |
| 3 | Sistema | Muestra los equipos encontrados. | Error de consulta |

#### Reglas de Negocio

| No. | Descripción |
| --- | --- |
| RN-29 | Cada usuario solo puede consultar los equipos asociados a su perfil. |

### RF-14: Editar equipo Pokémon

#### Funcionalidad

| Campo | Detalle |
| --- | --- |
| Código | RF-14 |
| Nombre | Editar equipo Pokémon |
| Descripción | Permitir que el usuario modifique el nombre o la composición de un equipo, incluyendo agregar o retirar Pokémon. |
| Cómo se ejecutará | Desde la opción de edición en el detalle del equipo. |
| Actor principal | Usuario autenticado |
| Precondiciones | El equipo debe existir y pertenecer al usuario. |

#### Datos de Entrada

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
| --- | --- | --- | --- | --- |
| Equipo seleccionado | Equipo que será editado | ID | Debe pertenecer al usuario | Sí |
| Datos actualizados | Nombre, Pokémon agregados o Pokémon retirados del equipo | Texto / Lista | Debe cumplir las reglas de equipo | Sí |

#### Datos de Salida

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
| --- | --- | --- | --- | --- |
| Equipo actualizado | Equipo con nombre o composición modificada | Objeto | Debe reflejar la información modificada | Sí |

#### Flujo Básico

| Paso | Actor | Descripción | Excepciones |
| --- | --- | --- | --- |
| 1 | Usuario | Selecciona un equipo propio. | Equipo no encontrado |
| 2 | Usuario | Modifica el nombre, agrega Pokémon o retira Pokémon. | Datos inválidos |
| 3 | Sistema | Valida las reglas del equipo. | Límite superado |
| 4 | Sistema | Guarda los cambios. | Error de almacenamiento |
| 5 | Sistema | Muestra el equipo actualizado. | Ninguna |

#### Reglas de Negocio

| No. | Descripción |
| --- | --- |
| RN-26 | Un equipo Pokémon no puede tener más de 6 Pokémon. |
| RN-27 | Un usuario solo puede modificar sus propios equipos. |
| RN-28 | Solo el dueño del equipo puede retirar Pokémon de ese equipo. |
| RN-30 | Un usuario solo puede editar sus propios equipos. |
| RN-31 | La composición editada no puede superar 6 Pokémon. |

### RF-15: Eliminar equipo Pokémon

#### Funcionalidad

| Campo | Detalle |
| --- | --- |
| Código | RF-15 |
| Nombre | Eliminar equipo Pokémon |
| Descripción | Permitir que el usuario elimine un equipo asociado a su perfil. |
| Cómo se ejecutará | Desde una opción de eliminación en Mis equipos. |
| Actor principal | Usuario autenticado |
| Precondiciones | El equipo debe existir y pertenecer al usuario. |

#### Datos de Entrada

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
| --- | --- | --- | --- | --- |
| Equipo seleccionado | Equipo que será eliminado | ID | Debe pertenecer al usuario | Sí |
| Confirmación | Aprobación de la eliminación | Booleano | Debe ser aceptada por el usuario | Sí |

#### Datos de Salida

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
| --- | --- | --- | --- | --- |
| Equipo eliminado | Registro removido del perfil | Estado | No debe aparecer en Mis equipos | Sí |

#### Flujo Básico

| Paso | Actor | Descripción | Excepciones |
| --- | --- | --- | --- |
| 1 | Usuario | Selecciona un equipo propio. | Equipo no encontrado |
| 2 | Sistema | Solicita confirmación de eliminación. | Ninguna |
| 3 | Usuario | Confirma la eliminación. | Acción cancelada |
| 4 | Sistema | Elimina el equipo. | Error de almacenamiento |
| 5 | Sistema | Muestra confirmación. | Ninguna |

#### Reglas de Negocio

| No. | Descripción |
| --- | --- |
| RN-32 | Solo el dueño del equipo puede eliminarlo. |

### RF-16: Consultar estadísticas de Pokémon

#### Funcionalidad

| Campo | Detalle |
| --- | --- |
| Código | RF-16 |
| Nombre | Consultar estadísticas de Pokémon |
| Descripción | Permitir que el usuario consulte estadísticas como tasa de elección en equipos y Pokémon populares. |
| Cómo se ejecutará | Mediante una sección de estadísticas con rankings, tablas o gráficos. |
| Actor principal | Usuario autenticado |
| Precondiciones | Deben existir equipos, favoritos o consultas registradas para calcular estadísticas. |

#### Datos de Entrada

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
| --- | --- | --- | --- | --- |
| Solicitud de estadísticas | Acción de abrir la sección de estadísticas | Evento | Se ejecuta desde estadísticas | Sí |

#### Datos de Salida

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
| --- | --- | --- | --- | --- |
| Estadísticas de Pokémon | Tasa de elección y ranking de popularidad | Gráfico / Tabla / Lista | Debe mostrar los Pokémon más relevantes primero | Sí |

#### Flujo Básico

| Paso | Actor | Descripción | Excepciones |
| --- | --- | --- | --- |
| 1 | Usuario | Ingresa a la sección de estadísticas. | Usuario no autenticado |
| 2 | Sistema | Calcula la tasa de elección y popularidad de los Pokémon. | Datos insuficientes |
| 3 | Sistema | Muestra rankings, tablas o gráficos. | Error de consulta |

#### Reglas de Negocio

| No. | Descripción |
| --- | --- |
| RN-33 | La tasa de elección debe actualizarse con base en los equipos creados. |
| RN-35 | La popularidad puede calcularse con base en favoritos, consultas y selección en equipos. |

### RF-17: Consultar métricas administrativas

#### Funcionalidad

| Campo | Detalle |
| --- | --- |
| Código | RF-17 |
| Nombre | Consultar métricas administrativas |
| Descripción | Permitir que el administrador consulte métricas de uso, como la cantidad de consultas realizadas por Pokémon o periodo. |
| Cómo se ejecutará | Mediante una sección administrativa de estadísticas. |
| Actor principal | Administrador |
| Precondiciones | Deben existir registros de consultas realizadas. |

#### Datos de Entrada

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
| --- | --- | --- | --- | --- |
| Solicitud de métricas | Acción de abrir las métricas administrativas | Evento | Se ejecuta desde estadísticas administrativas | Sí |

#### Datos de Salida

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
| --- | --- | --- | --- | --- |
| Métricas administrativas | Número de consultas por Pokémon o periodo | Gráfico / Tabla | Debe basarse en registros del sistema | Sí |

#### Flujo Básico

| Paso | Actor | Descripción | Excepciones |
| --- | --- | --- | --- |
| 1 | Administrador | Ingresa a estadísticas administrativas. | Permisos insuficientes |
| 2 | Sistema | Calcula las métricas de uso disponibles. | Datos insuficientes |
| 3 | Sistema | Muestra los resultados. | Error de consulta |

#### Reglas de Negocio

| No. | Descripción |
| --- | --- |
| RN-34 | Solo los administradores pueden consultar métricas administrativas de uso. |

### RF-18: Administrar perfiles de usuario

#### Funcionalidad

| Campo | Detalle |
| --- | --- |
| Código | RF-18 |
| Nombre | Administrar perfiles de usuario |
| Descripción | Permitir que el administrador consulte perfiles registrados, actualice su estado y cambie roles según los permisos definidos. |
| Cómo se ejecutará | Desde un panel administrativo de usuarios. |
| Actor principal | Administrador |
| Precondiciones | El actor debe estar autenticado como administrador. |

#### Datos de Entrada

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
| --- | --- | --- | --- | --- |
| Solicitud de perfiles | Acción de abrir el panel de usuarios | Evento | Debe ejecutarse con permisos administrativos | Sí |
| Usuario seleccionado | Perfil que será actualizado | ID | Debe existir en el sistema para acciones de cambio | No |
| Estado seleccionado | Nuevo estado del perfil | Lista | Puede ser activo o inactivo | No |
| Rol seleccionado | Nuevo rol del usuario | Lista | Debe ser un rol válido del sistema | No |

#### Datos de Salida

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
| --- | --- | --- | --- | --- |
| Gestión de perfiles | Lista de usuarios y cambios aplicados | Lista / Objeto | Debe mostrar perfiles y reflejar actualizaciones de estado o rol | Sí |

#### Flujo Básico

| Paso | Actor | Descripción | Excepciones |
| --- | --- | --- | --- |
| 1 | Administrador | Ingresa al panel de usuarios. | Permisos insuficientes |
| 2 | Sistema | Consulta los perfiles registrados. | Sin usuarios |
| 3 | Sistema | Muestra la lista de perfiles. | Error de consulta |
| 4 | Administrador | Selecciona un usuario y elige actualizar estado o cambiar rol. | Usuario no encontrado |
| 5 | Sistema | Valida que la acción sea permitida. | Acción no permitida |
| 6 | Sistema | Guarda el cambio y muestra confirmación. | Error de almacenamiento |

#### Reglas de Negocio

| No. | Descripción |
| --- | --- |
| RN-36 | Solo un administrador puede consultar perfiles de otros usuarios. |
| RN-37 | Solo un administrador puede cambiar el estado de perfiles de usuario. |
| RN-38 | No se debe permitir desactivar al único administrador activo. |
| RN-39 | Solo un administrador puede cambiar roles de usuario. |
| RN-40 | No se debe permitir que el único administrador activo pierda sus permisos. |

## 4. Reglas de uso de elementos de marca

Las reglas de uso de marca definen cómo deben aplicarse los elementos visuales de la Pokédex para mantener una identidad coherente, reconocible y fácil de usar dentro de la aplicación.

### 4.1 Usos correctos

| Elemento de marca | Regla de uso correcto |
| --- | --- |
| Logo | Usar el logo completo de la Pokédex en pantallas principales, inicio de sesión, encabezados y documentos oficiales del proyecto. |
| Área de seguridad del logo | Mantener un espacio libre alrededor del logo para evitar que textos, botones u otros elementos lo invadan. |
| Proporción del logo | Escalar el logo de forma proporcional, sin alterar su ancho o alto de manera independiente. |
| Colores principales | Utilizar los colores definidos para la marca, como rojo, blanco, negro y tonos complementarios asociados al universo Pokémon. |
| Contraste | Aplicar el logo y los textos sobre fondos que permitan una lectura clara. |
| Tipografía | Usar una tipografía legible y consistente en títulos, botones, menús y descripciones de Pokémon. |
| Íconos | Usar íconos relacionados con búsqueda, favoritos, equipos, estadísticas y perfil de forma clara y coherente. |
| Botones | Mantener un estilo uniforme en botones primarios, secundarios y acciones administrativas. |
| Imágenes de Pokémon | Mostrar las imágenes sin deformarlas y respetando su proporción original. |
| Tono visual | Mantener una apariencia dinámica, amigable y relacionada con una herramienta para entrenadores Pokémon. |
| Fondos | Usar fondos simples que no compitan visualmente con la información del Pokémon. |
| Aplicación móvil y web | Adaptar los elementos visuales al tamaño de pantalla sin perder legibilidad ni jerarquía. |

### 4.2 Usos incorrectos

| Elemento de marca | Uso incorrecto |
| --- | --- |
| Logo | No usar versiones borrosas, pixeladas o incompletas del logo. |
| Proporción del logo | No estirar, comprimir, rotar o inclinar el logo. |
| Color del logo | No cambiar los colores del logo por tonos que no pertenezcan a la identidad visual definida. |
| Fondos | No ubicar el logo sobre fondos con bajo contraste, imágenes saturadas o patrones que dificulten su lectura. |
| Área de seguridad | No colocar textos, íconos, bordes o botones pegados al logo. |
| Tipografía | No mezclar demasiadas fuentes ni usar tipografías difíciles de leer. |
| Colores | No usar combinaciones de colores que afecten la accesibilidad o confundan acciones importantes. |
| Íconos | No usar íconos con significados ambiguos o que no representen la acción correspondiente. |
| Imágenes de Pokémon | No deformar, recortar de forma agresiva o aplicar efectos que alteren la identificación del Pokémon. |
| Botones | No cambiar el estilo de los botones de forma inconsistente entre pantallas. |
| Tono visual | No usar elementos visuales que parezcan ajenos al concepto de Pokédex o entrenador Pokémon. |
| Jerarquía visual | No saturar las pantallas con demasiados colores, sombras, efectos o animaciones innecesarias. |

### 4.3 Reglas generales de aplicación

| No. | Regla |
| --- | --- |
| RM-01 | Todo elemento visual debe reforzar la identidad de la Pokédex y facilitar la consulta de información. |
| RM-02 | Los colores de acción deben ser consistentes: una misma acción debe representarse siempre con el mismo color o estilo. |
| RM-03 | Los elementos interactivos deben ser reconocibles como botones, enlaces, filtros o tarjetas. |
| RM-04 | La marca debe conservarse en todas las pantallas: registro, inicio de sesión, listado, detalle, favoritos, equipos, estadísticas y administración. |
| RM-05 | La interfaz debe priorizar la legibilidad de los datos del Pokémon por encima de efectos decorativos. |
| RM-06 | Las adaptaciones visuales para dispositivos móviles deben mantener el logo, colores, tipografía e íconos de forma coherente. |