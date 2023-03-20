package diagram.code_style;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;



public class CodeStylizer {
	List<UnidadLexica>uds;
	public CodeStylizer() {
		uds = new ArrayList<>();
	}
	public static void main(String[] args) throws IOException {
		Reader input = new InputStreamReader(new FileInputStream("src/main/java/diagram/test.txt"));
	     Yylex al = new Yylex(input);

	     while(al.yylex()) {
	    	 ;
	     }
	    /* do {
	       al.yylex();
	     }
	     while (unidad.clase() != ClaseLexica.EOF);
	    }   */
	}

}
