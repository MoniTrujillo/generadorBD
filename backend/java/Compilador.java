import org.antlr.runtime.*;
import java.io.IOException;

public class Compilador {
    public static void main(String[] args) throws Exception {
        try {
            // Leer desde stdin usando ANTLRInputStream
            ANTLRInputStream input = new ANTLRInputStream(System.in);
            gramaticaLexer lexer = new gramaticaLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            gramaticaParser parser = new gramaticaParser(tokens);

            parser.inicio();

            // JSON por defecto
            String json = "{";

            if (parser.getNumberOfSyntaxErrors() > 0 || parser.error) {
                // Hay errores
                json += "\"errores\": [";
                for (int i = 0; i < parser.errores.size(); i++) {
                    json += "\"" + escaparJSON(parser.errores.get(i)) + "\"";
                    if (i < parser.errores.size() - 1) json += ", ";
                }
                json += "], ";
                json += "\"resultado\": \"\", ";
                json += "\"estructura\": \"\"";
            } else {
                // Sin errores - agregar SQL y estructura
                json += "\"resultado\": \"" + escaparJSON(parser.sql) + "\", ";
                json += "\"estructura\": \"" + escaparJSON(parser.estructura) + "\"";
            }

            json += "}";
            System.out.println(json);

        } catch (Exception e) {
            // Error al procesar
            String json = "{\"error\": \"" + escaparJSON(e.getMessage()) + "\"}";
            System.out.println(json);
        }
    }

    // Función para escapar caracteres especiales en JSON
    private static String escaparJSON(String s) {
        if (s == null) return "";
        return s
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t");
    }
}
