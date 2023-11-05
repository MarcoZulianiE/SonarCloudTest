package co.edu.uniandes.dse.museoartemoderno.exceptions;

public final class ErrorMessage {
	//ERRORES RELACIONADOS CON ARTISTA_SERVICE
	public static final String ARTISTA_NOT_FOUND = "El *artista* con el id dado no fue encontrado";
	public static final String FECHA_FALLECIMIENTO_INVALIDA = "Fecha Fallecimiento is not valid";
	public static final String FECHA_NACIMIENTO_INVALIDA = "Fecha Nacimiento is not valid";
	
	//ERRORES RELACIONADOS CON CIUDAD_SERVICE
	public static final String CIUDAD_NOT_FOUND = "La *ciudad* con el id dado no fue encontrado";
	
	//ERRORES RELACIONADOS CON MOVIMIENTO_ARTISTICO_SERVICE
	public static final String MOVIMIENTO_ARTISTICO_NOT_FOUND = "El *movimiento artistico* con el id dado no fue encontrado";
	
	//ERRORES RELACIONADOS CON MUSEO_SERVICE
	public static final String MUSEO_NOT_FOUND = "El *museo* con el id dado no fue encontrado";
	
	//ERRORES RELACIONADOS CON OBRA_SERVICE
	public static final String OBRA_NOT_FOUND = "La *obra* con el id dado no fue encontrado";
	
	//ERRORES RELACIONADOS CON PAIS_SERVICE
	public static final String PAIS_NOT_FOUND = "El *pais* con el id dado no fue encontrado";
	

	private ErrorMessage() {
		throw new IllegalStateException("Utility class");
	}
}