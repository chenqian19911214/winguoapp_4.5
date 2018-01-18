package com.guobi.gfc.VoiceFun.grammar;
// Generated from SendMessage.g4 by ANTLR 4.5

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SendMessageLexer extends Lexer {
    static {
        RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION);
    }

    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache =
            new PredictionContextCache();
    public static final int
            T__0 = 1, T__1 = 2, T__2 = 3, T__3 = 4, T__4 = 5, T__5 = 6, T__6 = 7, M = 8, L = 9, C = 10,
            A = 11;
    public static String[] modeNames = {
            "DEFAULT_MODE"
    };

    public static final String[] ruleNames = {
            "T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "M", "L", "D",
            "N", "G", "C", "J", "K", "H", "A"
    };

    private static final String[] _LITERAL_NAMES = {
            null, "'告诉'", "'给'", "'发'", "'发送'", "'分享'", "'把'", "'将'"
    };
    private static final String[] _SYMBOLIC_NAMES = {
            null, null, null, null, null, null, null, null, "M", "L", "C", "A"
    };
    public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

    /**
     * @deprecated Use {@link #VOCABULARY} instead.
     */
    @Deprecated
    public static final String[] tokenNames;

    static {
        tokenNames = new String[_SYMBOLIC_NAMES.length];
        for (int i = 0; i < tokenNames.length; i++) {
            tokenNames[i] = VOCABULARY.getLiteralName(i);
            if (tokenNames[i] == null) {
                tokenNames[i] = VOCABULARY.getSymbolicName(i);
            }

            if (tokenNames[i] == null) {
                tokenNames[i] = "<INVALID>";
            }
        }
    }

    @Override
    @Deprecated
    public String[] getTokenNames() {
        return tokenNames;
    }

    @Override

    public Vocabulary getVocabulary() {
        return VOCABULARY;
    }


    public SendMessageLexer(CharStream input) {
        super(input);
        _interp = new LexerATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    @Override
    public String getGrammarFileName() {
        return "SendMessage.g4";
    }

    @Override
    public String[] getRuleNames() {
        return ruleNames;
    }

    @Override
    public String getSerializedATN() {
        return _serializedATN;
    }

    @Override
    public String[] getModeNames() {
        return modeNames;
    }

    @Override
    public ATN getATN() {
        return _ATN;
    }

    public static final String _serializedATN =
            "\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\ru\b\1\4\2\t\2\4" +
                    "\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t" +
                    "\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22" +
                    "\3\2\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\5\3\6\3\6\3\6\3\7\3\7\3\b\3\b\3" +
                    "\t\3\t\3\t\3\t\5\t;\n\t\3\n\3\n\3\n\3\n\5\nA\n\n\3\13\3\13\3\f\3\f\3\r" +
                    "\3\r\3\r\3\r\3\r\3\r\3\r\5\rN\n\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3" +
                    "\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\5\16b\n\16\3\17" +
                    "\3\17\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21\5\21o\n\21\3\22\6\22" +
                    "r\n\22\r\22\16\22s\3s\2\23\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25" +
                    "\2\27\2\31\2\33\f\35\2\37\2!\2#\r\3\2\6\4\2\u4e2c\u4e2c\u6763\u6763\5" +
                    "\2\u548e\u548e\u5bfb\u5bfb\u8de1\u8de1\4\2\u8bb4\u8bb4\u8bf6\u8bf6\5\2" +
                    "\u4ed8\u4ed8\u597b\u597b\u5b85\u5b85{\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2" +
                    "\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2" +
                    "\23\3\2\2\2\2\33\3\2\2\2\2#\3\2\2\2\3%\3\2\2\2\5(\3\2\2\2\7*\3\2\2\2\t" +
                    ",\3\2\2\2\13/\3\2\2\2\r\62\3\2\2\2\17\64\3\2\2\2\21:\3\2\2\2\23@\3\2\2" +
                    "\2\25B\3\2\2\2\27D\3\2\2\2\31M\3\2\2\2\33a\3\2\2\2\35c\3\2\2\2\37e\3\2" +
                    "\2\2!n\3\2\2\2#q\3\2\2\2%&\7\u544c\2\2&\'\7\u8bcb\2\2\'\4\3\2\2\2()\7" +
                    "\u7edb\2\2)\6\3\2\2\2*+\7\u53d3\2\2+\b\3\2\2\2,-\7\u53d3\2\2-.\7\u9003" +
                    "\2\2.\n\3\2\2\2/\60\7\u5208\2\2\60\61\7\u4ead\2\2\61\f\3\2\2\2\62\63\7" +
                    "\u628c\2\2\63\16\3\2\2\2\64\65\7\u5c08\2\2\65\20\3\2\2\2\66;\5\31\r\2" +
                    "\678\5\23\n\289\5\31\r\29;\3\2\2\2:\66\3\2\2\2:\67\3\2\2\2;\22\3\2\2\2" +
                    "<A\5\25\13\2=>\5\27\f\2>?\5\25\13\2?A\3\2\2\2@<\3\2\2\2@=\3\2\2\2A\24" +
                    "\3\2\2\2BC\t\2\2\2C\26\3\2\2\2DE\7\u4e02\2\2E\30\3\2\2\2FG\7\u77ef\2\2" +
                    "GN\7\u4fe3\2\2HI\7\u4fe3\2\2IN\7\u6071\2\2JK\7\u6d8a\2\2KN\7\u6071\2\2" +
                    "LN\7\u4fe3\2\2MF\3\2\2\2MH\3\2\2\2MJ\3\2\2\2ML\3\2\2\2N\32\3\2\2\2OP\5" +
                    "\35\17\2PQ\5!\21\2QR\5\37\20\2Rb\3\2\2\2ST\7\u544c\2\2TU\7\u8bcb\2\2U" +
                    "V\3\2\2\2Vb\5!\21\2WX\7\u544c\2\2XY\7\u8bcb\2\2YZ\3\2\2\2Z[\5!\21\2[\\" +
                    "\5\37\20\2\\b\3\2\2\2]b\5\37\20\2^_\7\u5187\2\2_`\7\u5bbb\2\2`b\7\u6631" +
                    "\2\2aO\3\2\2\2aS\3\2\2\2aW\3\2\2\2a]\3\2\2\2a^\3\2\2\2b\34\3\2\2\2cd\t" +
                    "\3\2\2d\36\3\2\2\2ef\t\4\2\2f \3\2\2\2go\t\5\2\2hi\7\u4ed8\2\2io\7\u4eee" +
                    "\2\2jk\7\u5b85\2\2ko\7\u4eee\2\2lm\7\u597b\2\2mo\7\u4eee\2\2ng\3\2\2\2" +
                    "nh\3\2\2\2nj\3\2\2\2nl\3\2\2\2o\"\3\2\2\2pr\13\2\2\2qp\3\2\2\2rs\3\2\2" +
                    "\2st\3\2\2\2sq\3\2\2\2t$\3\2\2\2\t\2:@Mans\2";
    public static final ATN _ATN =
            new ATNDeserializer().deserialize(_serializedATN.toCharArray());

    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}