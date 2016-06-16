Patrón Estructural: Bridge
- Desacoplar  una  abstracción  de  su implementación, de manera que ambas puedan ser modificadas independientemente.
- Se desea evitar un enlace permanente entre la abstracción y su implementación. Esto debido a que la implementación debe ser seleccionada o cambiada en tiempo de ejecución.
- Tanto las abstracciones como sus implementaciones deben ser extensibles por medio de subclases. En este caso, el patrón  Bridge  permite  combinar  abstracciones  e implementaciones  diferentes  y  extenderlas independientemente.
- Cambios en la implementación de una abstracción no deben impactar en los clientes, es decir, su código no debe ser recompilado.
- Se desea compartir una implementación entre múltiples objetos, y este hecho debe ser escondido a los clientes.

BRIDGE
--

MediaCapture (Abstraction)
- saveMedia()
GalleryMediaCapture (RefinedAbstraction)
-
CloudMediaCapture (RefinedAbstraction)
-

Camera(Implementor)
Photocamera (ConcreteImplementor)
Videocamera (ConcreteImplementor)

--

CameraMode
CameraModeCreator

CameraPhotoMode
CameraPhotoModeCreator
CameraVideoMode
CameraVideoModeCreator

--

FileWriter

