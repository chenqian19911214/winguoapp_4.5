// Generated from Launch.g4 by ANTLR 4.5.1
package com.guobi.gfc.VoiceFun.grammar;

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
public class LaunchLexer extends Lexer {
    static {
        RuntimeMetaData.checkVersion("4.5.1", RuntimeMetaData.VERSION);
    }

    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache =
            new PredictionContextCache();
    public static final int
            T__0 = 1, T__1 = 2, T__2 = 3, A = 4;
    public static String[] modeNames = {
            "DEFAULT_MODE"
    };

    public static final String[] ruleNames = {
            "T__0", "T__1", "T__2", "A"
    };

    private static final String[] _LITERAL_NAMES = {
            null, "'打开'", "'启动'", "'运行'"
    };
    private static final String[] _SYMBOLIC_NAMES = {
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


    public LaunchLexer(CharStream input) {
        super(input);
        _interp = new LexerATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    @Override
    public String getGrammarFileName() {
        return "Launch.g4";
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
            "\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\6\31\b\1\4\2\t\2" +
                    "\4\3\t\3\4\4\t\4\4\5\t\5\3\2\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3\4\3\5\6\5\26" +
                    "\n\5\r\5\16\5\27\3\27\2\6\3\3\5\4\7\5\t\6\3\2\2\31\2\3\3\2\2\2\2\5\3\2" +
                    "\2\2\2\7\3\2\2\2\2\t\3\2\2\2\3\13\3\2\2\2\5\16\3\2\2\2\7\21\3\2\2\2\t" +
                    "\25\3\2\2\2\13\f\7\u6255\2\2\f\r\7\u5f02\2\2\r\4\3\2\2\2\16\17\7\u5431" +
                    "\2\2\17\20\7\u52aa\2\2\20\6\3\2\2\2\21\22\7\u8fd2\2\2\22\23\7\u884e\2" +
                    "\2\23\b\3\2\2\2\24\26\13\2\2\2\25\24\3\2\2\2\26\27\3\2\2\2\27\30\3\2\2" +
                    "\2\27\25\3\2\2\2\30\n\3\2\2\2\4\2\27\2";
    public static final ATN _ATN =
            new ATNDeserializer().deserialize(_serializedATN.toCharArray());

    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}