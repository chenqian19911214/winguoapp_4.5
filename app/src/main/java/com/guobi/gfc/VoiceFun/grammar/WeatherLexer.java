package com.guobi.gfc.VoiceFun.grammar;
// Generated from Weather.g4 by ANTLR 4.5

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
public class WeatherLexer extends Lexer {
    static {
        RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION);
    }

    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache =
            new PredictionContextCache();
    public static final int
            T__0 = 1, T__1 = 2, T__2 = 3, T__3 = 4, T__4 = 5, T__5 = 6, T__6 = 7, T__7 = 8, T__8 = 9,
            T__9 = 10, Z = 11, T = 12, A = 13;
    public static String[] modeNames = {
            "DEFAULT_MODE"
    };

    public static final String[] ruleNames = {
            "T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8",
            "T__9", "Z", "T", "A"
    };

    private static final String[] _LITERAL_NAMES = {
            null, "'天气'", "'的'", "'一'", "'二'", "'两'", "'三'", "'几'", "'今'", "'明'",
            "'后'"
    };
    private static final String[] _SYMBOLIC_NAMES = {
            null, null, null, null, null, null, null, null, null, null, null, "Z",
            "T", "A"
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


    public WeatherLexer(CharStream input) {
        super(input);
        _interp = new LexerATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    @Override
    public String getGrammarFileName() {
        return "Weather.g4";
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
            "\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\17G\b\1\4\2\t\2\4" +
                    "\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t" +
                    "\13\4\f\t\f\4\r\t\r\4\16\t\16\3\2\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6" +
                    "\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f" +
                    "\3\f\3\f\3\f\3\f\3\f\3\f\5\f?\n\f\3\r\3\r\3\16\6\16D\n\16\r\16\16\16E" +
                    "\3E\2\17\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17" +
                    "\3\2\3\4\2\u592b\u592b\u65e7\u65e7L\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2" +
                    "\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23" +
                    "\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\3\35\3\2" +
                    "\2\2\5 \3\2\2\2\7\"\3\2\2\2\t$\3\2\2\2\13&\3\2\2\2\r(\3\2\2\2\17*\3\2" +
                    "\2\2\21,\3\2\2\2\23.\3\2\2\2\25\60\3\2\2\2\27>\3\2\2\2\31@\3\2\2\2\33" +
                    "C\3\2\2\2\35\36\7\u592b\2\2\36\37\7\u6c16\2\2\37\4\3\2\2\2 !\7\u7686\2" +
                    "\2!\6\3\2\2\2\"#\7\u4e02\2\2#\b\3\2\2\2$%\7\u4e8e\2\2%\n\3\2\2\2&\'\7" +
                    "\u4e26\2\2\'\f\3\2\2\2()\7\u4e0b\2\2)\16\3\2\2\2*+\7\u51e2\2\2+\20\3\2" +
                    "\2\2,-\7\u4ecc\2\2-\22\3\2\2\2./\7\u6610\2\2/\24\3\2\2\2\60\61\7\u5410" +
                    "\2\2\61\26\3\2\2\2\62\63\7\u6702\2\2\63?\7\u8fd3\2\2\64\65\7\u672c\2\2" +
                    "\65?\7\u6767\2\2\66\67\7\u8fd3\2\2\67?\7\u6767\2\289\7\u5c08\2\29?\7\u6767" +
                    "\2\2:;\7\u4ee7\2\2;?\7\u5410\2\2<=\7\u5f82\2\2=?\7\u5410\2\2>\62\3\2\2" +
                    "\2>\64\3\2\2\2>\66\3\2\2\2>8\3\2\2\2>:\3\2\2\2><\3\2\2\2?\30\3\2\2\2@" +
                    "A\t\2\2\2A\32\3\2\2\2BD\13\2\2\2CB\3\2\2\2DE\3\2\2\2EF\3\2\2\2EC\3\2\2" +
                    "\2F\34\3\2\2\2\5\2>E\2";
    public static final ATN _ATN =
            new ATNDeserializer().deserialize(_serializedATN.toCharArray());

    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}