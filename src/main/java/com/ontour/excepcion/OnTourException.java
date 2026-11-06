package com.ontour.excepcion;

// Excepcion propia del sistema. Se usa para violaciones de reglas de negocio
// (por ejemplo, pagar un viatico que ya estaba pagado, o un soundcheck despues del show)
// y para errores de configuracion, como que falte el archivo db.properties.
// Extiende RuntimeException, asi que no obliga a declararla con "throws".
public class OnTourException extends RuntimeException {

    public OnTourException(String mensaje) {
        super(mensaje);
    }
}
