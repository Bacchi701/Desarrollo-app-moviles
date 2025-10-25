Aplicación movil para pequeño emprendimiento de confeccion y venta de ropa escolar desarrollado por el alumno Bryan Ignacio Bacchi Aceituno.

Este proyecto se desarrolló con la finalidad de cumplir con lo solicitado en las Instrucciones y pauta de evaluacion para
la entrega N°2 del ramo Desarrollo de aplicaciones moviles seccion 009D.

Para el correcto funcionamiento de las interfaces de Login, Register y Menu principal se requere revisar el archivo
build.grace.kts (:app), el cual debe de contener las siguientes dependencias dentro de "dependencies {}"

dependencies {
    // Compose Foundation (HorizontalPager para el carrusel)
    implementation("androidx.compose.foundation:foundation")
    // Navegación en Compose
    implementation("androidx.navigation:navigation-compose:2.7.7")
    // Iconos Material (email, lock, person, etc.)
    implementation("androidx.compose.material:material-icons-extended")
    // DataStore (persistencia local de preferencias)
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    
    ...dependencias que ya vienen por defecto.
}

Despues de añadir las dependencias, sincronizar el Gradle project para su correcto funcionamiento.
