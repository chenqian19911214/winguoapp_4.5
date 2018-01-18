// Generated from Launch.g4 by ANTLR 4.5.1
package com.guobi.gfc.VoiceFun.grammar;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.TerminalNode;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class LaunchParser extends Parser {
    static {
        RuntimeMetaData.checkVersion("4.5.1", RuntimeMetaData.VERSION);
    }

    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache =
            new PredictionContextCache();
    public static final int
            T__0 = 1, T__1 = 2, T__2 = 3, A = 4;
    public static final int
            RULE_s = 0, RULE_j = 1, RULE_x = 2, RULE_a = 3;
    public static final String[] ruleNames = {
            "s", "j", "x", "a"
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
    public ATN getATN() {
        return _ATN;
    }

    public LaunchParser(TokenStream input) {
        super(input);
        _interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    public static class SContext extends ParserRuleContext {
        public JContext j() {
            return getRuleContext(JContext.class, 0);
        }

        public XContext x() {
            return getRuleContext(XContext.class, 0);
        }

        public SContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_s;
        }
    }

    public final SContext s() throws RecognitionException {
        SContext _localctx = new SContext(_ctx, getState());
        enterRule(_localctx, 0, RULE_s);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(8);
                j();
                setState(9);
                x();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class JContext extends ParserRuleContext {
        public JContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_j;
        }
    }

    public final JContext j() throws RecognitionException {
        JContext _localctx = new JContext(_ctx, getState());
        enterRule(_localctx, 2, RULE_j);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(11);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2))) != 0))) {
                    _errHandler.recoverInline(this);
                } else {
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class XContext extends ParserRuleContext {
        public AContext a() {
            return getRuleContext(AContext.class, 0);
        }

        public XContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_x;
        }
    }

    public final XContext x() throws RecognitionException {
        XContext _localctx = new XContext(_ctx, getState());
        enterRule(_localctx, 4, RULE_x);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(13);
                a();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class AContext extends ParserRuleContext {
        public TerminalNode A() {
            return getToken(LaunchParser.A, 0);
        }

        public AContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_a;
        }
    }

    public final AContext a() throws RecognitionException {
        AContext _localctx = new AContext(_ctx, getState());
        enterRule(_localctx, 6, RULE_a);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                {
                    setState(15);
                    match(A);
                    setState(19);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 0, _ctx);
                    while (_alt != 1 && _alt != ATN.INVALID_ALT_NUMBER) {
                        if (_alt == 1 + 1) {
                            {
                                {
                                    setState(16);
                                    matchWildcard();
                                }
                            }
                        }
                        setState(21);
                        _errHandler.sync(this);
                        _alt = getInterpreter().adaptivePredict(_input, 0, _ctx);
                    }
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static final String _serializedATN =
            "\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\6\31\4\2\t\2\4\3" +
                    "\t\3\4\4\t\4\4\5\t\5\3\2\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\7\5\24\n\5\f" +
                    "\5\16\5\27\13\5\3\5\3\25\2\6\2\4\6\b\2\3\3\2\3\5\25\2\n\3\2\2\2\4\r\3" +
                    "\2\2\2\6\17\3\2\2\2\b\21\3\2\2\2\n\13\5\4\3\2\13\f\5\6\4\2\f\3\3\2\2\2" +
                    "\r\16\t\2\2\2\16\5\3\2\2\2\17\20\5\b\5\2\20\7\3\2\2\2\21\25\7\6\2\2\22" +
                    "\24\13\2\2\2\23\22\3\2\2\2\24\27\3\2\2\2\25\26\3\2\2\2\25\23\3\2\2\2\26" +
                    "\t\3\2\2\2\27\25\3\2\2\2\3\25";
    public static final ATN _ATN =
            new ATNDeserializer().deserialize(_serializedATN.toCharArray());

    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}
