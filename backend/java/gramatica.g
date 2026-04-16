grammar gramatica;

@header {
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.HashMap;
import org.antlr.runtime.*;
}

@members {
    String sql = "";
    String estructura = "";

    boolean error = false;
    List<String> errores = new ArrayList<String>();

    // ORDEN CORRECTO
    Set<String> tablas = new LinkedHashSet<String>();

    Map<String, Set<String>> camposPorTabla = new HashMap<String, Set<String>>();

    String tablaActual = null;
    boolean tienePK = false;

    boolean existeCrear = false;
    boolean existeUsar = false;
    boolean existeCerrar = false;

    int estado = 0;

    String nombreBD = "";

    Map<String, Map<String, String>> tiposPorTabla = new HashMap<>();

    Map<String, String> llavesPrimarias = new HashMap<>();

    Map<String, List<String>> llavesForaneas = new HashMap<>();

    List<String> acciones = new ArrayList<>();

    private void generarEstructura() {
        estructura += "========================================\n";
        estructura += "      RESUMEN DE LA BASE DE DATOS\n";
        estructura += "========================================\n\n";
        estructura += "Base de datos creada: " + nombreBD + "\n\n";
        estructura += "----------------------------------------\n";
        estructura += "TABLAS CREADAS\n";
        estructura += "----------------------------------------\n\n";

        for (String tabla : tablas) {
            estructura += "Tabla: " + tabla + "\n\n";
            estructura += "Atributos:\n";

            Set<String> campos = camposPorTabla.get(tabla);
            Map<String, String> tipos = tiposPorTabla.get(tabla);

            if (campos != null) {
                for (String campo : campos) {
                    String tipo = (tipos != null && tipos.containsKey(campo))
                            ? tipos.get(campo)
                            : "desconocido";

                    boolean esPK = llavesPrimarias.containsKey(tabla)
                            && llavesPrimarias.get(tabla).equals(campo);

                    estructura += " - " + campo + " (" + tipo + ")";

                    if (esPK) {
                        estructura += "  CLAVE PRINCIPAL";
                    }

                    estructura += "\n";
                }
            }

            estructura += "\nRelaciones:\n";

            List<String> relaciones = llavesForaneas.get(tabla);

            if (relaciones == null || relaciones.isEmpty()) {
                estructura += " - No depende de ninguna tabla\n";
            } else {
                for (String fk : relaciones) {
                    String[] partes = fk.split(" -> ");
                    if (partes.length == 2) {
                        estructura += " - " + partes[0] + " depende de " + partes[1] + "\n";
                    } else {
                        estructura += " - " + fk + "\n";
                    }
                }
            }

            estructura += "\n----------------------------------------\n\n";
        }

        estructura += "========================================\n";
    }

    @Override
    public void displayRecognitionError(String[] tokenNames, RecognitionException e) {
        String mensaje = "Error en la línea " + e.line + ": error de sintaxis.";
        errores.add(mensaje);
        error = true;
    }
}

// -------------------- INICIO --------------------
inicio 
    : crear usar instrucciones cerrar EOF
    {
        if(!error) {
            generarEstructura();
        }
    }
;

// -------------------- BASE DE DATOS --------------------
crear  
    : CREAR id1=ID 
    { 
        if(estado > 0){
            errores.add("Error en línea " + $id1.line + ": 'crear' debe ir antes de 'usar'.");
            error = true;
        }

        estado = 1;
        existeCrear = true;

        nombreBD = $id1.text;
        acciones.add("Se creó la base de datos '" + nombreBD + "'");

        sql += "CREATE DATABASE " + $id1.text + ";\n"; 
    }
;

usar   
    : USAR id1=ID 
    { 
        if(!$id1.text.equals(nombreBD)){
            errores.add("Error en línea " + $id1.line + ": el nombre de la base de datos en 'usar' (" + $id1.text + ") no coincide con 'crear' (" + nombreBD + ").");
            error = true;
        }

        estado = 2;
        existeUsar = true;

        acciones.add("Se seleccionó la base de datos '" + $id1.text + "'");

        sql += "USE " + $id1.text + ";\n"; 
    }
;

// -------------------- INSTRUCCIONES --------------------
instrucciones  
    : (tabla)+ 
;

// -------------------- TABLAS --------------------
tabla  
    : TABLA id1=ID INICIO
    {
        tablaActual = $id1.text;
        tienePK = false;

        tablas.add(tablaActual);

        camposPorTabla.put(tablaActual, new LinkedHashSet<String>());
        tiposPorTabla.put(tablaActual, new HashMap<String, String>());
        llavesForaneas.put(tablaActual, new ArrayList<String>());

        acciones.add("Se creó la tabla '" + tablaActual + "'");

        sql += "CREATE TABLE " + tablaActual + " (\n";
    }
    campos
    FIN
    {
        if(!tienePK){
            errores.add("Error: la tabla '" + tablaActual + "' no tiene clave.");
            error = true;
        }

        
        if (sql.endsWith(",\n")) {
            sql = sql.substring(0, sql.length() - 2) + "\n";
        }

        tablaActual = null;
        sql += ");\n";
    }
;

// -------------------- CAMPOS --------------------
campos  : (campo)+ ;

campo
    : id1=ID tipo CLAVE
    {
        if(tienePK){
            errores.add("Error: ya existe clave en la tabla.");
            error = true;
        } else {
            tienePK = true;

            camposPorTabla.get(tablaActual).add($id1.text);
            tiposPorTabla.get(tablaActual).put($id1.text, $tipo.text);

            llavesPrimarias.put(tablaActual, $id1.text);

            acciones.add("Se definió clave '" + $id1.text + "'");

            sql += "  " + $id1.text + " INT PRIMARY KEY,\n";
        }
    }

    | id1=ID tipo
    {
        camposPorTabla.get(tablaActual).add($id1.text);
        tiposPorTabla.get(tablaActual).put($id1.text, $tipo.text);

        if($tipo.text.equals("numeros"))
            sql += "  " + $id1.text + " INT,\n";
        else if($tipo.text.equals("letras"))
            sql += "  " + $id1.text + " VARCHAR(255),\n";
        else if($tipo.text.equals("fecha"))
            sql += "  " + $id1.text + " DATE,\n";
        else if($tipo.text.equals("decimal"))
            sql += "  " + $id1.text + " DOUBLE,\n";
    }

    | id1=ID DEPENDE_DE id2=ID
    {
        camposPorTabla.get(tablaActual).add($id1.text);
        tiposPorTabla.get(tablaActual).put($id1.text, "numeros");

        llavesForaneas.get(tablaActual).add($id1.text + " -> " + $id2.text);

        acciones.add("Relación: " + $id1.text + " depende de " + $id2.text);

        // Buscar el nombre real del primary key de la tabla referenciada
        String pkColumna = llavesPrimarias.get($id2.text);
        if (pkColumna == null) {
            errores.add("Error: la tabla '" + $id2.text + "' no existe o no fue definida antes.");
            error = true;
            pkColumna = "id"; // fallback
        }

        sql += "  " + $id1.text + " INT,\n";
        sql += "  FOREIGN KEY (" + $id1.text + ") REFERENCES " + $id2.text + "(" + pkColumna + "),\n";
    }
;

// -------------------- CIERRE --------------------
cerrar : CERRAR { existeCerrar = true; };

// -------------------- TIPO --------------------
tipo : NUMEROS | LETRAS | FECHA | DECIMAL ;

// -------------------- TOKENS --------------------
CREAR      : 'crear' ;
USAR       : 'usar' ;
TABLA      : 'tabla' ;
INICIO     : 'inicio' ;
FIN        : 'fin' ;
CERRAR     : 'cerrar' ;
DEPENDE_DE : 'depende_de' ;
CLAVE      : 'clave' ;
NUMEROS    : 'numeros' ;
LETRAS     : 'letras' ;
FECHA      : 'fecha' ;
DECIMAL    : 'decimal' ;
ID         : ('a'..'z' | 'A'..'Z' | '_') ('a'..'z' | 'A'..'Z' | '0'..'9' | '_')* ;
WS         : (' ' | '\n' | '\r' | '\t')+ { $channel = HIDDEN; };