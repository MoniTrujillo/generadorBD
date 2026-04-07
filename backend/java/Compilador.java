import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Compilador {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder builder = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            builder.append(line).append("\n");
        }

        String texto = builder.toString().trim();
        if (texto.isEmpty()) {
            System.out.println("No se recibió ningún texto.");
            return;
        }

        String sqlGenerado = generarSql(texto);
        System.out.println(sqlGenerado);
    }

    private static String generarSql(String input) {
        String escaped = input.replace("\"", "\\\"");
        return "-- SQL generado a partir del texto de entrada:\n" + input + "\n\n" +
               "SELECT * FROM ejemplo WHERE descripcion LIKE \"%" + escaped + "%\";";
    }
}
