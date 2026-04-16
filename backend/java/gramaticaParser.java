// $ANTLR 3.5.2 gramatica.g 2026-04-16 11:32:23

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.HashMap;
import org.antlr.runtime.*;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class gramaticaParser extends Parser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "CERRAR", "CLAVE", "CREAR", "DECIMAL", 
		"DEPENDE_DE", "FECHA", "FIN", "ID", "INICIO", "LETRAS", "NUMEROS", "TABLA", 
		"USAR", "WS"
	};
	public static final int EOF=-1;
	public static final int CERRAR=4;
	public static final int CLAVE=5;
	public static final int CREAR=6;
	public static final int DECIMAL=7;
	public static final int DEPENDE_DE=8;
	public static final int FECHA=9;
	public static final int FIN=10;
	public static final int ID=11;
	public static final int INICIO=12;
	public static final int LETRAS=13;
	public static final int NUMEROS=14;
	public static final int TABLA=15;
	public static final int USAR=16;
	public static final int WS=17;

	// delegates
	public Parser[] getDelegates() {
		return new Parser[] {};
	}

	// delegators


	public gramaticaParser(TokenStream input) {
		this(input, new RecognizerSharedState());
	}
	public gramaticaParser(TokenStream input, RecognizerSharedState state) {
		super(input, state);
	}

	@Override public String[] getTokenNames() { return gramaticaParser.tokenNames; }
	@Override public String getGrammarFileName() { return "gramatica.g"; }


	    String sql = "";
	    String estructura = "";

	    boolean error = false;
	    List<String> errores = new ArrayList<String>();

	    // 🔥 ORDEN CORRECTO
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



	// $ANTLR start "inicio"
	// gramatica.g:112:1: inicio : crear usar instrucciones cerrar EOF ;
	public final void inicio() throws RecognitionException {
		try {
			// gramatica.g:113:5: ( crear usar instrucciones cerrar EOF )
			// gramatica.g:113:7: crear usar instrucciones cerrar EOF
			{
			pushFollow(FOLLOW_crear_in_inicio28);
			crear();
			state._fsp--;

			pushFollow(FOLLOW_usar_in_inicio30);
			usar();
			state._fsp--;

			pushFollow(FOLLOW_instrucciones_in_inicio32);
			instrucciones();
			state._fsp--;

			pushFollow(FOLLOW_cerrar_in_inicio34);
			cerrar();
			state._fsp--;

			match(input,EOF,FOLLOW_EOF_in_inicio36); 

			        if(!error) {
			            generarEstructura();
			        }
			    
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "inicio"



	// $ANTLR start "crear"
	// gramatica.g:122:1: crear : CREAR id1= ID ;
	public final void crear() throws RecognitionException {
		Token id1=null;

		try {
			// gramatica.g:123:5: ( CREAR id1= ID )
			// gramatica.g:123:7: CREAR id1= ID
			{
			match(input,CREAR,FOLLOW_CREAR_in_crear58); 
			id1=(Token)match(input,ID,FOLLOW_ID_in_crear62); 
			 
			        if(estado > 0){
			            errores.add("Error en línea " + (id1!=null?id1.getLine():0) + ": 'crear' debe ir antes de 'usar'.");
			            error = true;
			        }

			        estado = 1;
			        existeCrear = true;

			        nombreBD = (id1!=null?id1.getText():null);
			        acciones.add("Se creó la base de datos '" + nombreBD + "'");

			        sql += "CREATE DATABASE " + (id1!=null?id1.getText():null) + ";\n"; 
			    
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "crear"



	// $ANTLR start "usar"
	// gramatica.g:140:1: usar : USAR id1= ID ;
	public final void usar() throws RecognitionException {
		Token id1=null;

		try {
			// gramatica.g:141:5: ( USAR id1= ID )
			// gramatica.g:141:7: USAR id1= ID
			{
			match(input,USAR,FOLLOW_USAR_in_usar85); 
			id1=(Token)match(input,ID,FOLLOW_ID_in_usar89); 
			 
			        if(!(id1!=null?id1.getText():null).equals(nombreBD)){
			            errores.add("Error en línea " + (id1!=null?id1.getLine():0) + ": el nombre de la base de datos en 'usar' (" + (id1!=null?id1.getText():null) + ") no coincide con 'crear' (" + nombreBD + ").");
			            error = true;
			        }

			        estado = 2;
			        existeUsar = true;

			        acciones.add("Se seleccionó la base de datos '" + (id1!=null?id1.getText():null) + "'");

			        sql += "USE " + (id1!=null?id1.getText():null) + ";\n"; 
			    
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "usar"



	// $ANTLR start "instrucciones"
	// gramatica.g:158:1: instrucciones : ( tabla )+ ;
	public final void instrucciones() throws RecognitionException {
		try {
			// gramatica.g:159:5: ( ( tabla )+ )
			// gramatica.g:159:7: ( tabla )+
			{
			// gramatica.g:159:7: ( tabla )+
			int cnt1=0;
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( (LA1_0==TABLA) ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// gramatica.g:159:8: tabla
					{
					pushFollow(FOLLOW_tabla_in_instrucciones113);
					tabla();
					state._fsp--;

					}
					break;

				default :
					if ( cnt1 >= 1 ) break loop1;
					EarlyExitException eee = new EarlyExitException(1, input);
					throw eee;
				}
				cnt1++;
			}

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "instrucciones"



	// $ANTLR start "tabla"
	// gramatica.g:163:1: tabla : TABLA id1= ID INICIO campos FIN ;
	public final void tabla() throws RecognitionException {
		Token id1=null;

		try {
			// gramatica.g:164:5: ( TABLA id1= ID INICIO campos FIN )
			// gramatica.g:164:7: TABLA id1= ID INICIO campos FIN
			{
			match(input,TABLA,FOLLOW_TABLA_in_tabla132); 
			id1=(Token)match(input,ID,FOLLOW_ID_in_tabla136); 
			match(input,INICIO,FOLLOW_INICIO_in_tabla138); 

			        tablaActual = (id1!=null?id1.getText():null);
			        tienePK = false;

			        tablas.add(tablaActual);

			        camposPorTabla.put(tablaActual, new LinkedHashSet<String>());
			        tiposPorTabla.put(tablaActual, new HashMap<String, String>());
			        llavesForaneas.put(tablaActual, new ArrayList<String>());

			        acciones.add("Se creó la tabla '" + tablaActual + "'");

			        sql += "CREATE TABLE " + tablaActual + " (\n";
			    
			pushFollow(FOLLOW_campos_in_tabla150);
			campos();
			state._fsp--;

			match(input,FIN,FOLLOW_FIN_in_tabla156); 

			        if(!tienePK){
			            errores.add("Error: la tabla '" + tablaActual + "' no tiene clave.");
			            error = true;
			        }

			        // Remover última coma y newline antes de cerrar
			        if (sql.endsWith(",\n")) {
			            sql = sql.substring(0, sql.length() - 2) + "\n";
			        }

			        tablaActual = null;
			        sql += ");\n";
			    
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "tabla"



	// $ANTLR start "campos"
	// gramatica.g:198:1: campos : ( campo )+ ;
	public final void campos() throws RecognitionException {
		try {
			// gramatica.g:198:9: ( ( campo )+ )
			// gramatica.g:198:11: ( campo )+
			{
			// gramatica.g:198:11: ( campo )+
			int cnt2=0;
			loop2:
			while (true) {
				int alt2=2;
				int LA2_0 = input.LA(1);
				if ( (LA2_0==ID) ) {
					alt2=1;
				}

				switch (alt2) {
				case 1 :
					// gramatica.g:198:12: campo
					{
					pushFollow(FOLLOW_campo_in_campos174);
					campo();
					state._fsp--;

					}
					break;

				default :
					if ( cnt2 >= 1 ) break loop2;
					EarlyExitException eee = new EarlyExitException(2, input);
					throw eee;
				}
				cnt2++;
			}

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "campos"



	// $ANTLR start "campo"
	// gramatica.g:200:1: campo : (id1= ID tipo CLAVE |id1= ID tipo |id1= ID DEPENDE_DE id2= ID );
	public final void campo() throws RecognitionException {
		Token id1=null;
		Token id2=null;
		ParserRuleReturnScope tipo1 =null;
		ParserRuleReturnScope tipo2 =null;

		try {
			// gramatica.g:201:5: (id1= ID tipo CLAVE |id1= ID tipo |id1= ID DEPENDE_DE id2= ID )
			int alt3=3;
			int LA3_0 = input.LA(1);
			if ( (LA3_0==ID) ) {
				int LA3_1 = input.LA(2);
				if ( (LA3_1==DEPENDE_DE) ) {
					alt3=3;
				}
				else if ( (LA3_1==DECIMAL||LA3_1==FECHA||(LA3_1 >= LETRAS && LA3_1 <= NUMEROS)) ) {
					int LA3_3 = input.LA(3);
					if ( (LA3_3==CLAVE) ) {
						alt3=1;
					}
					else if ( ((LA3_3 >= FIN && LA3_3 <= ID)) ) {
						alt3=2;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 3, 3, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 3, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 3, 0, input);
				throw nvae;
			}

			switch (alt3) {
				case 1 :
					// gramatica.g:201:7: id1= ID tipo CLAVE
					{
					id1=(Token)match(input,ID,FOLLOW_ID_in_campo191); 
					pushFollow(FOLLOW_tipo_in_campo193);
					tipo1=tipo();
					state._fsp--;

					match(input,CLAVE,FOLLOW_CLAVE_in_campo195); 

					        if(tienePK){
					            errores.add("Error: ya existe clave en la tabla.");
					            error = true;
					        } else {
					            tienePK = true;

					            camposPorTabla.get(tablaActual).add((id1!=null?id1.getText():null));
					            tiposPorTabla.get(tablaActual).put((id1!=null?id1.getText():null), (tipo1!=null?input.toString(tipo1.start,tipo1.stop):null));

					            llavesPrimarias.put(tablaActual, (id1!=null?id1.getText():null));

					            acciones.add("Se definió clave '" + (id1!=null?id1.getText():null) + "'");

					            sql += "  " + (id1!=null?id1.getText():null) + " INT PRIMARY KEY,\n";
					        }
					    
					}
					break;
				case 2 :
					// gramatica.g:220:7: id1= ID tipo
					{
					id1=(Token)match(input,ID,FOLLOW_ID_in_campo212); 
					pushFollow(FOLLOW_tipo_in_campo214);
					tipo2=tipo();
					state._fsp--;


					        camposPorTabla.get(tablaActual).add((id1!=null?id1.getText():null));
					        tiposPorTabla.get(tablaActual).put((id1!=null?id1.getText():null), (tipo2!=null?input.toString(tipo2.start,tipo2.stop):null));

					        if((tipo2!=null?input.toString(tipo2.start,tipo2.stop):null).equals("numeros"))
					            sql += "  " + (id1!=null?id1.getText():null) + " INT,\n";
					        else if((tipo2!=null?input.toString(tipo2.start,tipo2.stop):null).equals("letras"))
					            sql += "  " + (id1!=null?id1.getText():null) + " VARCHAR(255),\n";
					        else if((tipo2!=null?input.toString(tipo2.start,tipo2.stop):null).equals("fecha"))
					            sql += "  " + (id1!=null?id1.getText():null) + " DATE,\n";
					        else if((tipo2!=null?input.toString(tipo2.start,tipo2.stop):null).equals("decimal"))
					            sql += "  " + (id1!=null?id1.getText():null) + " DOUBLE,\n";
					    
					}
					break;
				case 3 :
					// gramatica.g:235:7: id1= ID DEPENDE_DE id2= ID
					{
					id1=(Token)match(input,ID,FOLLOW_ID_in_campo231); 
					match(input,DEPENDE_DE,FOLLOW_DEPENDE_DE_in_campo233); 
					id2=(Token)match(input,ID,FOLLOW_ID_in_campo237); 

					        camposPorTabla.get(tablaActual).add((id1!=null?id1.getText():null));
					        tiposPorTabla.get(tablaActual).put((id1!=null?id1.getText():null), "numeros");

					        llavesForaneas.get(tablaActual).add((id1!=null?id1.getText():null) + " -> " + (id2!=null?id2.getText():null));

					        acciones.add("Relación: " + (id1!=null?id1.getText():null) + " depende de " + (id2!=null?id2.getText():null));

					        // Buscar el nombre real del primary key de la tabla referenciada
					        String pkColumna = llavesPrimarias.get((id2!=null?id2.getText():null));
					        if (pkColumna == null) {
					            errores.add("Error: la tabla '" + (id2!=null?id2.getText():null) + "' no existe o no fue definida antes.");
					            error = true;
					            pkColumna = "id"; // fallback
					        }

					        sql += "  " + (id1!=null?id1.getText():null) + " INT,\n";
					        sql += "  FOREIGN KEY (" + (id1!=null?id1.getText():null) + ") REFERENCES " + (id2!=null?id2.getText():null) + "(" + pkColumna + "),\n";
					    
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "campo"



	// $ANTLR start "cerrar"
	// gramatica.g:258:1: cerrar : CERRAR ;
	public final void cerrar() throws RecognitionException {
		try {
			// gramatica.g:258:8: ( CERRAR )
			// gramatica.g:258:10: CERRAR
			{
			match(input,CERRAR,FOLLOW_CERRAR_in_cerrar253); 
			 existeCerrar = true; 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "cerrar"


	public static class tipo_return extends ParserRuleReturnScope {
	};


	// $ANTLR start "tipo"
	// gramatica.g:261:1: tipo : ( NUMEROS | LETRAS | FECHA | DECIMAL );
	public final gramaticaParser.tipo_return tipo() throws RecognitionException {
		gramaticaParser.tipo_return retval = new gramaticaParser.tipo_return();
		retval.start = input.LT(1);

		try {
			// gramatica.g:261:6: ( NUMEROS | LETRAS | FECHA | DECIMAL )
			// gramatica.g:
			{
			if ( input.LA(1)==DECIMAL||input.LA(1)==FECHA||(input.LA(1) >= LETRAS && input.LA(1) <= NUMEROS) ) {
				input.consume();
				state.errorRecovery=false;
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			}

			retval.stop = input.LT(-1);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "tipo"

	// Delegated rules



	public static final BitSet FOLLOW_crear_in_inicio28 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_usar_in_inicio30 = new BitSet(new long[]{0x0000000000008000L});
	public static final BitSet FOLLOW_instrucciones_in_inicio32 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_cerrar_in_inicio34 = new BitSet(new long[]{0x0000000000000000L});
	public static final BitSet FOLLOW_EOF_in_inicio36 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_CREAR_in_crear58 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_ID_in_crear62 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_USAR_in_usar85 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_ID_in_usar89 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_tabla_in_instrucciones113 = new BitSet(new long[]{0x0000000000008002L});
	public static final BitSet FOLLOW_TABLA_in_tabla132 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_ID_in_tabla136 = new BitSet(new long[]{0x0000000000001000L});
	public static final BitSet FOLLOW_INICIO_in_tabla138 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_campos_in_tabla150 = new BitSet(new long[]{0x0000000000000400L});
	public static final BitSet FOLLOW_FIN_in_tabla156 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_campo_in_campos174 = new BitSet(new long[]{0x0000000000000802L});
	public static final BitSet FOLLOW_ID_in_campo191 = new BitSet(new long[]{0x0000000000006280L});
	public static final BitSet FOLLOW_tipo_in_campo193 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_CLAVE_in_campo195 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_campo212 = new BitSet(new long[]{0x0000000000006280L});
	public static final BitSet FOLLOW_tipo_in_campo214 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_campo231 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_DEPENDE_DE_in_campo233 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_ID_in_campo237 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_CERRAR_in_cerrar253 = new BitSet(new long[]{0x0000000000000002L});
}
