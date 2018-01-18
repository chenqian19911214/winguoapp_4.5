package com.guobi.gfc.VoiceFun.grammar;
// Generated from Weather.g4 by ANTLR 4.5

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

import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class WeatherParser extends Parser {
    static {
        RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION);
    }

    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache =
            new PredictionContextCache();
    public static final int
            T__0 = 1, T__1 = 2, T__2 = 3, T__3 = 4, T__4 = 5, T__5 = 6, T__6 = 7, T__7 = 8, T__8 = 9,
            T__9 = 10, Z = 11, T = 12, A = 13;
    public static final int
            RULE_s = 0, RULE_u = 1, RULE_c = 2, RULE_j = 3, RULE_r = 4, RULE_n = 5,
            RULE_g = 6, RULE_a = 7;
    public static final String[] ruleNames = {
            "s", "u", "c", "j", "r", "n", "g", "a"
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
    public ATN getATN() {
        return _ATN;
    }

    public WeatherParser(TokenStream input) {
        super(input);
        _interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    public static class SContext extends ParserRuleContext {
        public List<UContext> u() {
            return getRuleContexts(UContext.class);
        }

        public UContext u(int i) {
            return getRuleContext(UContext.class, i);
        }

        public RContext r() {
            return getRuleContext(RContext.class, 0);
        }

        public List<JContext> j() {
            return getRuleContexts(JContext.class);
        }

        public JContext j(int i) {
            return getRuleContext(JContext.class, i);
        }

        public CContext c() {
            return getRuleContext(CContext.class, 0);
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
            setState(36);
            switch (getInterpreter().adaptivePredict(_input, 0, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(16);
                    u();
                    setState(17);
                    r();
                    setState(18);
                    j();
                    setState(19);
                    c();
                    setState(20);
                    j();
                    setState(21);
                    match(T__0);
                    setState(22);
                    u();
                }
                break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(24);
                    c();
                    setState(25);
                    j();
                    setState(26);
                    r();
                    setState(27);
                    j();
                    setState(28);
                    match(T__0);
                    setState(29);
                    u();
                }
                break;
                case 3:
                    enterOuterAlt(_localctx, 3);
                {
                    setState(31);
                    c();
                    setState(32);
                    j();
                    setState(33);
                    match(T__0);
                    setState(34);
                    u();
                }
                break;
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

    public static class UContext extends ParserRuleContext {
        public AContext a() {
            return getRuleContext(AContext.class, 0);
        }

        public UContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_u;
        }
    }

    public final UContext u() throws RecognitionException {
        UContext _localctx = new UContext(_ctx, getState());
        enterRule(_localctx, 2, RULE_u);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(39);
                _la = _input.LA(1);
                if (_la == A) {
                    {
                        setState(38);
                        a();
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

    public static class CContext extends ParserRuleContext {
        public AContext a() {
            return getRuleContext(AContext.class, 0);
        }

        public CContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_c;
        }
    }

    public final CContext c() throws RecognitionException {
        CContext _localctx = new CContext(_ctx, getState());
        enterRule(_localctx, 4, RULE_c);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(42);
                _la = _input.LA(1);
                if (_la == A) {
                    {
                        setState(41);
                        a();
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
        enterRule(_localctx, 6, RULE_j);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(45);
                switch (getInterpreter().adaptivePredict(_input, 3, _ctx)) {
                    case 1: {
                        setState(44);
                        match(T__1);
                    }
                    break;
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

    public static class RContext extends ParserRuleContext {
        public TerminalNode Z() {
            return getToken(WeatherParser.Z, 0);
        }

        public NContext n() {
            return getRuleContext(NContext.class, 0);
        }

        public TerminalNode T() {
            return getToken(WeatherParser.T, 0);
        }

        public GContext g() {
            return getRuleContext(GContext.class, 0);
        }

        public RContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_r;
        }
    }

    public final RContext r() throws RecognitionException {
        RContext _localctx = new RContext(_ctx, getState());
        enterRule(_localctx, 8, RULE_r);
        try {
            setState(55);
            switch (getInterpreter().adaptivePredict(_input, 4, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(47);
                    match(Z);
                }
                break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(48);
                    match(Z);
                    setState(49);
                    n();
                    setState(50);
                    match(T);
                }
                break;
                case 3:
                    enterOuterAlt(_localctx, 3);
                {
                    setState(52);
                    g();
                    setState(53);
                    match(T);
                }
                break;
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

    public static class NContext extends ParserRuleContext {
        public NContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_n;
        }
    }

    public final NContext n() throws RecognitionException {
        NContext _localctx = new NContext(_ctx, getState());
        enterRule(_localctx, 10, RULE_n);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(57);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__6))) != 0))) {
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

    public static class GContext extends ParserRuleContext {
        public GContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_g;
        }
    }

    public final GContext g() throws RecognitionException {
        GContext _localctx = new GContext(_ctx, getState());
        enterRule(_localctx, 12, RULE_g);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(59);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__7) | (1L << T__8) | (1L << T__9))) != 0))) {
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

    public static class AContext extends ParserRuleContext {
        public TerminalNode A() {
            return getToken(WeatherParser.A, 0);
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
        enterRule(_localctx, 14, RULE_a);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                {
                    setState(61);
                    match(A);
                    setState(65);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 5, _ctx);
                    while (_alt != 1 && _alt != ATN.INVALID_ALT_NUMBER) {
                        if (_alt == 1 + 1) {
                            {
                                {
                                    setState(62);
                                    matchWildcard();
                                }
                            }
                        }
                        setState(67);
                        _errHandler.sync(this);
                        _alt = getInterpreter().adaptivePredict(_input, 5, _ctx);
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
            "\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\17G\4\2\t\2\4\3\t" +
                    "\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\3\2\3\2\3\2\3\2\3\2" +
                    "\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\5\2\'\n\2" +
                    "\3\3\5\3*\n\3\3\4\5\4-\n\4\3\5\5\5\60\n\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6" +
                    "\3\6\5\6:\n\6\3\7\3\7\3\b\3\b\3\t\3\t\7\tB\n\t\f\t\16\tE\13\t\3\t\3C\2" +
                    "\n\2\4\6\b\n\f\16\20\2\4\3\2\5\t\3\2\n\fF\2&\3\2\2\2\4)\3\2\2\2\6,\3\2" +
                    "\2\2\b/\3\2\2\2\n9\3\2\2\2\f;\3\2\2\2\16=\3\2\2\2\20?\3\2\2\2\22\23\5" +
                    "\4\3\2\23\24\5\n\6\2\24\25\5\b\5\2\25\26\5\6\4\2\26\27\5\b\5\2\27\30\7" +
                    "\3\2\2\30\31\5\4\3\2\31\'\3\2\2\2\32\33\5\6\4\2\33\34\5\b\5\2\34\35\5" +
                    "\n\6\2\35\36\5\b\5\2\36\37\7\3\2\2\37 \5\4\3\2 \'\3\2\2\2!\"\5\6\4\2\"" +
                    "#\5\b\5\2#$\7\3\2\2$%\5\4\3\2%\'\3\2\2\2&\22\3\2\2\2&\32\3\2\2\2&!\3\2" +
                    "\2\2\'\3\3\2\2\2(*\5\20\t\2)(\3\2\2\2)*\3\2\2\2*\5\3\2\2\2+-\5\20\t\2" +
                    ",+\3\2\2\2,-\3\2\2\2-\7\3\2\2\2.\60\7\4\2\2/.\3\2\2\2/\60\3\2\2\2\60\t" +
                    "\3\2\2\2\61:\7\r\2\2\62\63\7\r\2\2\63\64\5\f\7\2\64\65\7\16\2\2\65:\3" +
                    "\2\2\2\66\67\5\16\b\2\678\7\16\2\28:\3\2\2\29\61\3\2\2\29\62\3\2\2\29" +
                    "\66\3\2\2\2:\13\3\2\2\2;<\t\2\2\2<\r\3\2\2\2=>\t\3\2\2>\17\3\2\2\2?C\7" +
                    "\17\2\2@B\13\2\2\2A@\3\2\2\2BE\3\2\2\2CD\3\2\2\2CA\3\2\2\2D\21\3\2\2\2" +
                    "EC\3\2\2\2\b&),/9C";
    public static final ATN _ATN =
            new ATNDeserializer().deserialize(_serializedATN.toCharArray());

    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}
