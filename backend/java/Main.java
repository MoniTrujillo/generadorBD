import org.antlr.runtime.*;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {

        System.out.println("Escribe tu programa (CTRL+Z para terminar):");

        ANTLRInputStream input = new ANTLRInputStream(System.in);
        gramaticaLexer lexer = new gramaticaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        gramaticaParser parser = new gramaticaParser(tokens);

        parser.inicio();

        if (parser.getNumberOfSyntaxErrors() > 0 || parser.error) {

            System.out.println("\nSe encontraron los siguientes errores:\n");

            for (String err : parser.errores) {
                System.out.println(err);
            }

            System.out.println("\nNo se generaron los archivos por los errores.");

        } else {

            System.out.println("\nSQL generado correctamente:\n");
            System.out.println(parser.sql);

            try {
                FileWriter sqlFile = new FileWriter("salida_sql.sql");
                sqlFile.write(parser.sql);
                sqlFile.close();

                FileWriter estructuraFile = new FileWriter("estructura_bd.txt");
                estructuraFile.write(parser.estructura);
                estructuraFile.close();

                System.out.println("\nArchivos generados correctamente:");
                System.out.println("✔ salida_sql.sql");
                System.out.println("✔ estructura_bd.txt");

            } catch (IOException e) {
                System.out.println("Error al generar los archivos.");
            }
        }
    }
}