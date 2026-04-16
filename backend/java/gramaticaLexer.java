// $ANTLR 3.5.2 gramatica.g 2026-04-16 11:32:24

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class gramaticaLexer extends Lexer {
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
	// delegators
	public Lexer[] getDelegates() {
		return new Lexer[] {};
	}

	public gramaticaLexer() {} 
	public gramaticaLexer(CharStream input) {
		this(input, new RecognizerSharedState());
	}
	public gramaticaLexer(CharStream input, RecognizerSharedState state) {
		super(input,state);
	}
	@Override public String getGrammarFileName() { return "gramatica.g"; }

	// $ANTLR start "CREAR"
	public final void mCREAR() throws RecognitionException {
		try {
			int _type = CREAR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// gramatica.g:264:12: ( 'crear' )
			// gramatica.g:264:14: 'crear'
			{
			match("crear"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "CREAR"

	// $ANTLR start "USAR"
	public final void mUSAR() throws RecognitionException {
		try {
			int _type = USAR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// gramatica.g:265:12: ( 'usar' )
			// gramatica.g:265:14: 'usar'
			{
			match("usar"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "USAR"

	// $ANTLR start "TABLA"
	public final void mTABLA() throws RecognitionException {
		try {
			int _type = TABLA;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// gramatica.g:266:12: ( 'tabla' )
			// gramatica.g:266:14: 'tabla'
			{
			match("tabla"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "TABLA"

	// $ANTLR start "INICIO"
	public final void mINICIO() throws RecognitionException {
		try {
			int _type = INICIO;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// gramatica.g:267:12: ( 'inicio' )
			// gramatica.g:267:14: 'inicio'
			{
			match("inicio"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "INICIO"

	// $ANTLR start "FIN"
	public final void mFIN() throws RecognitionException {
		try {
			int _type = FIN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// gramatica.g:268:12: ( 'fin' )
			// gramatica.g:268:14: 'fin'
			{
			match("fin"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "FIN"

	// $ANTLR start "CERRAR"
	public final void mCERRAR() throws RecognitionException {
		try {
			int _type = CERRAR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// gramatica.g:269:12: ( 'cerrar' )
			// gramatica.g:269:14: 'cerrar'
			{
			match("cerrar"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "CERRAR"

	// $ANTLR start "DEPENDE_DE"
	public final void mDEPENDE_DE() throws RecognitionException {
		try {
			int _type = DEPENDE_DE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// gramatica.g:270:12: ( 'depende_de' )
			// gramatica.g:270:14: 'depende_de'
			{
			match("depende_de"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DEPENDE_DE"

	// $ANTLR start "CLAVE"
	public final void mCLAVE() throws RecognitionException {
		try {
			int _type = CLAVE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// gramatica.g:271:12: ( 'clave' )
			// gramatica.g:271:14: 'clave'
			{
			match("clave"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "CLAVE"

	// $ANTLR start "NUMEROS"
	public final void mNUMEROS() throws RecognitionException {
		try {
			int _type = NUMEROS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// gramatica.g:272:12: ( 'numeros' )
			// gramatica.g:272:14: 'numeros'
			{
			match("numeros"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NUMEROS"

	// $ANTLR start "LETRAS"
	public final void mLETRAS() throws RecognitionException {
		try {
			int _type = LETRAS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// gramatica.g:273:12: ( 'letras' )
			// gramatica.g:273:14: 'letras'
			{
			match("letras"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LETRAS"

	// $ANTLR start "FECHA"
	public final void mFECHA() throws RecognitionException {
		try {
			int _type = FECHA;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// gramatica.g:274:12: ( 'fecha' )
			// gramatica.g:274:14: 'fecha'
			{
			match("fecha"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "FECHA"

	// $ANTLR start "DECIMAL"
	public final void mDECIMAL() throws RecognitionException {
		try {
			int _type = DECIMAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// gramatica.g:275:12: ( 'decimal' )
			// gramatica.g:275:14: 'decimal'
			{
			match("decimal"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DECIMAL"

	// $ANTLR start "ID"
	public final void mID() throws RecognitionException {
		try {
			int _type = ID;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// gramatica.g:276:12: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
			// gramatica.g:276:14: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
			{
			if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			// gramatica.g:276:42: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( ((LA1_0 >= '0' && LA1_0 <= '9')||(LA1_0 >= 'A' && LA1_0 <= 'Z')||LA1_0=='_'||(LA1_0 >= 'a' && LA1_0 <= 'z')) ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// gramatica.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop1;
				}
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ID"

	// $ANTLR start "WS"
	public final void mWS() throws RecognitionException {
		try {
			int _type = WS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// gramatica.g:277:12: ( ( ' ' | '\\n' | '\\r' | '\\t' )+ )
			// gramatica.g:277:14: ( ' ' | '\\n' | '\\r' | '\\t' )+
			{
			// gramatica.g:277:14: ( ' ' | '\\n' | '\\r' | '\\t' )+
			int cnt2=0;
			loop2:
			while (true) {
				int alt2=2;
				int LA2_0 = input.LA(1);
				if ( ((LA2_0 >= '\t' && LA2_0 <= '\n')||LA2_0=='\r'||LA2_0==' ') ) {
					alt2=1;
				}

				switch (alt2) {
				case 1 :
					// gramatica.g:
					{
					if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt2 >= 1 ) break loop2;
					EarlyExitException eee = new EarlyExitException(2, input);
					throw eee;
				}
				cnt2++;
			}

			 _channel = HIDDEN; 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "WS"

	@Override
	public void mTokens() throws RecognitionException {
		// gramatica.g:1:8: ( CREAR | USAR | TABLA | INICIO | FIN | CERRAR | DEPENDE_DE | CLAVE | NUMEROS | LETRAS | FECHA | DECIMAL | ID | WS )
		int alt3=14;
		alt3 = dfa3.predict(input);
		switch (alt3) {
			case 1 :
				// gramatica.g:1:10: CREAR
				{
				mCREAR(); 

				}
				break;
			case 2 :
				// gramatica.g:1:16: USAR
				{
				mUSAR(); 

				}
				break;
			case 3 :
				// gramatica.g:1:21: TABLA
				{
				mTABLA(); 

				}
				break;
			case 4 :
				// gramatica.g:1:27: INICIO
				{
				mINICIO(); 

				}
				break;
			case 5 :
				// gramatica.g:1:34: FIN
				{
				mFIN(); 

				}
				break;
			case 6 :
				// gramatica.g:1:38: CERRAR
				{
				mCERRAR(); 

				}
				break;
			case 7 :
				// gramatica.g:1:45: DEPENDE_DE
				{
				mDEPENDE_DE(); 

				}
				break;
			case 8 :
				// gramatica.g:1:56: CLAVE
				{
				mCLAVE(); 

				}
				break;
			case 9 :
				// gramatica.g:1:62: NUMEROS
				{
				mNUMEROS(); 

				}
				break;
			case 10 :
				// gramatica.g:1:70: LETRAS
				{
				mLETRAS(); 

				}
				break;
			case 11 :
				// gramatica.g:1:77: FECHA
				{
				mFECHA(); 

				}
				break;
			case 12 :
				// gramatica.g:1:83: DECIMAL
				{
				mDECIMAL(); 

				}
				break;
			case 13 :
				// gramatica.g:1:91: ID
				{
				mID(); 

				}
				break;
			case 14 :
				// gramatica.g:1:94: WS
				{
				mWS(); 

				}
				break;

		}
	}


	protected DFA3 dfa3 = new DFA3(this);
	static final String DFA3_eotS =
		"\1\uffff\10\11\2\uffff\21\11\1\50\10\11\1\61\2\11\1\uffff\5\11\1\71\1"+
		"\11\1\73\1\uffff\1\74\1\11\1\76\4\11\1\uffff\1\103\2\uffff\1\104\1\uffff"+
		"\3\11\1\110\2\uffff\1\11\1\112\1\113\1\uffff\1\11\2\uffff\1\11\1\116\1"+
		"\uffff";
	static final String DFA3_eofS =
		"\117\uffff";
	static final String DFA3_minS =
		"\1\11\1\145\1\163\1\141\1\156\2\145\1\165\1\145\2\uffff\1\145\1\162\2"+
		"\141\1\142\1\151\1\156\2\143\1\155\1\164\1\141\1\162\1\166\1\162\1\154"+
		"\1\143\1\60\1\150\1\145\1\151\1\145\2\162\1\141\1\145\1\60\1\141\1\151"+
		"\1\uffff\1\141\1\156\1\155\1\162\1\141\1\60\1\162\1\60\1\uffff\1\60\1"+
		"\157\1\60\1\144\1\141\1\157\1\163\1\uffff\1\60\2\uffff\1\60\1\uffff\1"+
		"\145\1\154\1\163\1\60\2\uffff\1\137\2\60\1\uffff\1\144\2\uffff\1\145\1"+
		"\60\1\uffff";
	static final String DFA3_maxS =
		"\1\172\1\162\1\163\1\141\1\156\1\151\1\145\1\165\1\145\2\uffff\1\145\1"+
		"\162\2\141\1\142\1\151\1\156\1\143\1\160\1\155\1\164\1\141\1\162\1\166"+
		"\1\162\1\154\1\143\1\172\1\150\1\145\1\151\1\145\2\162\1\141\1\145\1\172"+
		"\1\141\1\151\1\uffff\1\141\1\156\1\155\1\162\1\141\1\172\1\162\1\172\1"+
		"\uffff\1\172\1\157\1\172\1\144\1\141\1\157\1\163\1\uffff\1\172\2\uffff"+
		"\1\172\1\uffff\1\145\1\154\1\163\1\172\2\uffff\1\137\2\172\1\uffff\1\144"+
		"\2\uffff\1\145\1\172\1\uffff";
	static final String DFA3_acceptS =
		"\11\uffff\1\15\1\16\35\uffff\1\5\10\uffff\1\2\7\uffff\1\1\1\uffff\1\10"+
		"\1\3\1\uffff\1\13\4\uffff\1\6\1\4\3\uffff\1\12\1\uffff\1\14\1\11\2\uffff"+
		"\1\7";
	static final String DFA3_specialS =
		"\117\uffff}>";
	static final String[] DFA3_transitionS = {
			"\2\12\2\uffff\1\12\22\uffff\1\12\40\uffff\32\11\4\uffff\1\11\1\uffff"+
			"\2\11\1\1\1\6\1\11\1\5\2\11\1\4\2\11\1\10\1\11\1\7\5\11\1\3\1\2\5\11",
			"\1\14\6\uffff\1\15\5\uffff\1\13",
			"\1\16",
			"\1\17",
			"\1\20",
			"\1\22\3\uffff\1\21",
			"\1\23",
			"\1\24",
			"\1\25",
			"",
			"",
			"\1\26",
			"\1\27",
			"\1\30",
			"\1\31",
			"\1\32",
			"\1\33",
			"\1\34",
			"\1\35",
			"\1\37\14\uffff\1\36",
			"\1\40",
			"\1\41",
			"\1\42",
			"\1\43",
			"\1\44",
			"\1\45",
			"\1\46",
			"\1\47",
			"\12\11\7\uffff\32\11\4\uffff\1\11\1\uffff\32\11",
			"\1\51",
			"\1\52",
			"\1\53",
			"\1\54",
			"\1\55",
			"\1\56",
			"\1\57",
			"\1\60",
			"\12\11\7\uffff\32\11\4\uffff\1\11\1\uffff\32\11",
			"\1\62",
			"\1\63",
			"",
			"\1\64",
			"\1\65",
			"\1\66",
			"\1\67",
			"\1\70",
			"\12\11\7\uffff\32\11\4\uffff\1\11\1\uffff\32\11",
			"\1\72",
			"\12\11\7\uffff\32\11\4\uffff\1\11\1\uffff\32\11",
			"",
			"\12\11\7\uffff\32\11\4\uffff\1\11\1\uffff\32\11",
			"\1\75",
			"\12\11\7\uffff\32\11\4\uffff\1\11\1\uffff\32\11",
			"\1\77",
			"\1\100",
			"\1\101",
			"\1\102",
			"",
			"\12\11\7\uffff\32\11\4\uffff\1\11\1\uffff\32\11",
			"",
			"",
			"\12\11\7\uffff\32\11\4\uffff\1\11\1\uffff\32\11",
			"",
			"\1\105",
			"\1\106",
			"\1\107",
			"\12\11\7\uffff\32\11\4\uffff\1\11\1\uffff\32\11",
			"",
			"",
			"\1\111",
			"\12\11\7\uffff\32\11\4\uffff\1\11\1\uffff\32\11",
			"\12\11\7\uffff\32\11\4\uffff\1\11\1\uffff\32\11",
			"",
			"\1\114",
			"",
			"",
			"\1\115",
			"\12\11\7\uffff\32\11\4\uffff\1\11\1\uffff\32\11",
			""
	};

	static final short[] DFA3_eot = DFA.unpackEncodedString(DFA3_eotS);
	static final short[] DFA3_eof = DFA.unpackEncodedString(DFA3_eofS);
	static final char[] DFA3_min = DFA.unpackEncodedStringToUnsignedChars(DFA3_minS);
	static final char[] DFA3_max = DFA.unpackEncodedStringToUnsignedChars(DFA3_maxS);
	static final short[] DFA3_accept = DFA.unpackEncodedString(DFA3_acceptS);
	static final short[] DFA3_special = DFA.unpackEncodedString(DFA3_specialS);
	static final short[][] DFA3_transition;

	static {
		int numStates = DFA3_transitionS.length;
		DFA3_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA3_transition[i] = DFA.unpackEncodedString(DFA3_transitionS[i]);
		}
	}

	protected class DFA3 extends DFA {

		public DFA3(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 3;
			this.eot = DFA3_eot;
			this.eof = DFA3_eof;
			this.min = DFA3_min;
			this.max = DFA3_max;
			this.accept = DFA3_accept;
			this.special = DFA3_special;
			this.transition = DFA3_transition;
		}
		@Override
		public String getDescription() {
			return "1:1: Tokens : ( CREAR | USAR | TABLA | INICIO | FIN | CERRAR | DEPENDE_DE | CLAVE | NUMEROS | LETRAS | FECHA | DECIMAL | ID | WS );";
		}
	}

}
