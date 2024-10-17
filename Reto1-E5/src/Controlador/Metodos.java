package Controlador;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Metodos {

	public boolean contraComprobar(String contraseña, String repContraseña) {
		boolean resultado = true;
		if(repContraseña.intern() != contraseña.intern()) {
			resultado = false;
		}
		return resultado;
	}

	public boolean comprobarFecha(String fNac, String dateFormat) {
		SimpleDateFormat formato = new SimpleDateFormat(dateFormat);
		formato.setLenient(false);
		  try {
		    formato.parse(fNac);
		  } catch (ParseException e) {
		    return false;
		  }
		  return true;
	}

	public Boolean validarEmail (String email) {
		Pattern pattern = Pattern.compile("^([0-9a-zA-Z]+[-._+&])*[0-9a-zA-Z]+@([-0-9a-zA-Z]+[.])+[a-zA-Z]{2,6}$");
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
	
}
