package com.ontour.excepcion;

// Excepcion propia del sistema para representar violaciones de reglas de negocio
// (por ejemplo, intentar pagar un viatico que ya estaba pagado).
// Extiende RuntimeException, asi que no obliga a declararla con "throws".
public class OnTourException extends RuntimeException {

    public OnTourException(String mensaje) {
        super(mensaje);
    }
}
