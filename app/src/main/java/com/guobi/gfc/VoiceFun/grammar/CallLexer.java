package com.guobi.gfc.VoiceFun.grammar;
// Generated from Call.g4 by ANTLR 4.5

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
public class CallLexer extends Lexer {
    static {
        RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION);
    }

    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache =
            new PredictionContextCache();
    public static final int
            T__0 = 1, T__1 = 2, T__2 = 3, T__3 = 4, T__4 = 5, T__5 = 6, T__6 = 7, T__7 = 8, T__8 = 9,
            T__9 = 10, T__10 = 11, T__11 = 12, T__12 = 13, T__13 = 14, T__14 = 15, A = 16;
    public static String[] modeNames = {
            "DEFAULT_MODE"
    };

    public static final String[] ruleNames = {
            "T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8",
            "T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "A"
    };

    private static final String[] _LITERAL_NAMES = {
            null, "'给'", "'打'", "'拨'", "'拨打'", "'电话'", "'号'", "'接'", "'接通'", "'联系'",
            "'和'", "'对'", "'跟'", "'通话'", "'对话'", "'说话'"
    };
    private static final String[] _SYMBOLIC_NAMES = {
            null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, "A"
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


    public CallLexer(CharStream input) {
        super(input);
        _interp = new LexerATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    @Override
    public String getGrammarFileName() {
        return "Call.g4";
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
            "\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\22M\b\1\4\2\t\2\4" +
                    "\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t" +
                    "\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\3\2\3\2\3" +
                    "\3\3\3\3\4\3\4\3\5\3\5\3\5\3\6\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\t\3\n" +
                    "\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\16\3\17\3\17\3\17\3\20" +
                    "\3\20\3\20\3\21\6\21J\n\21\r\21\16\21K\3K\2\22\3\3\5\4\7\5\t\6\13\7\r" +
                    "\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22\3\2\2M\2\3\3" +
                    "\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2" +
                    "\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3" +
                    "\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\3#\3\2\2\2\5" +
                    "%\3\2\2\2\7\'\3\2\2\2\t)\3\2\2\2\13,\3\2\2\2\r/\3\2\2\2\17\61\3\2\2\2" +
                    "\21\63\3\2\2\2\23\66\3\2\2\2\259\3\2\2\2\27;\3\2\2\2\31=\3\2\2\2\33?\3" +
                    "\2\2\2\35B\3\2\2\2\37E\3\2\2\2!I\3\2\2\2#$\7\u7edb\2\2$\4\3\2\2\2%&\7" +
                    "\u6255\2\2&\6\3\2\2\2\'(\7\u62ea\2\2(\b\3\2\2\2)*\7\u62ea\2\2*+\7\u6255" +
                    "\2\2+\n\3\2\2\2,-\7\u7537\2\2-.\7\u8bdf\2\2.\f\3\2\2\2/\60\7\u53f9\2\2" +
                    "\60\16\3\2\2\2\61\62\7\u63a7\2\2\62\20\3\2\2\2\63\64\7\u63a7\2\2\64\65" +
                    "\7\u901c\2\2\65\22\3\2\2\2\66\67\7\u8056\2\2\678\7\u7cfd\2\28\24\3\2\2" +
                    "\29:\7\u548e\2\2:\26\3\2\2\2;<\7\u5bfb\2\2<\30\3\2\2\2=>\7\u8de1\2\2>" +
                    "\32\3\2\2\2?@\7\u901c\2\2@A\7\u8bdf\2\2A\34\3\2\2\2BC\7\u5bfb\2\2CD\7" +
                    "\u8bdf\2\2D\36\3\2\2\2EF\7\u8bf6\2\2FG\7\u8bdf\2\2G \3\2\2\2HJ\13\2\2" +
                    "\2IH\3\2\2\2JK\3\2\2\2KL\3\2\2\2KI\3\2\2\2L\"\3\2\2\2\4\2K\2";
    public static final ATN _ATN =
            new ATNDeserializer().deserialize(_serializedATN.toCharArray());

    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}
