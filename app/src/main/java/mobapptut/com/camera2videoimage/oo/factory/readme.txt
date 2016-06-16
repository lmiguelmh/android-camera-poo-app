Patrón Creacional: Factory Method
- Define una interfaz para crear un objeto pero deja que las subclases decidan qué clase instanciar.
- Delega la función de crear objetos a las subclases.
- La clase Aplicación no sabe qué tipo de documento específico debe crear, sólo cuándo debe hacerlo.

FACTORY METHOD

Media
MediaCreator
- create
FileMediaCreator
VideoMediaCreator

***

CameraDevice
CameraDeviceCreator




Filter
FilterCreator
BlackWhiteFilter
BlackWhiteFilterCreator

---

CameraType - CameraConfiguration
CameraTypeCreator - CameraConfigurationCreator

SimpleCameraType - RotatedCameraConfiguration
VideoCameraType
Camera1MpType
Camera2MpType

---

FileWriterMode ?

---

Media
MediaCreator

PhotoFileMedia

CameraFileMedia

---

CameraDevice
