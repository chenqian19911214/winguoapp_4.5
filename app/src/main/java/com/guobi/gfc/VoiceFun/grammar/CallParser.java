package com.guobi.gfc.VoiceFun.grammar;
// Generated from Call.g4 by ANTLR 4.5

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
public class CallParser extends Parser {
    static {
        RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION);
    }

    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache =
            new PredictionContextCache();
    public static final int
            T__0 = 1, T__1 = 2, T__2 = 3, T__3 = 4, T__4 = 5, T__5 = 6, T__6 = 7, T__7 = 8, T__8 = 9,
            T__9 = 10, T__10 = 11, T__11 = 12, T__12 = 13, T__13 = 14, T__14 = 15, A = 16;
    public static final int
            RULE_s = 0, RULE_u = 1, RULE_d = 2, RULE_n = 3, RULE_j = 4, RULE_h = 5,
            RULE_l = 6, RULE_x = 7, RULE_a = 8;
    public static final String[] ruleNames = {
            "s", "u", "d", "n", "j", "h", "l", "x", "a"
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
    public ATN getATN() {
        return _ATN;
    }

    public CallParser(TokenStream input) {
        super(input);
        _interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    public static class SContext extends ParserRuleContext {
        public UContext u() {
            return getRuleContext(UContext.class, 0);
        }

        public DContext d() {
            return getRuleContext(DContext.class, 0);
        }

        public NContext n() {
            return getRuleContext(NContext.class, 0);
        }

        public XContext x() {
            return getRuleContext(XContext.class, 0);
        }

        public JContext j() {
            return getRuleContext(JContext.class, 0);
        }

        public HContext h() {
            return getRuleContext(HContext.class, 0);
        }

        public LContext l() {
            return getRuleContext(LContext.class, 0);
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
            setState(43);
            switch (getInterpreter().adaptivePredict(_input, 0, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(18);
                    u();
                    setState(19);
                    d();
                    setState(20);
                    n();
                    setState(21);
                    match(T__0);
                    setState(22);
                    x();
                }
                break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(24);
                    u();
                    setState(25);
                    d();
                    setState(26);
                    match(T__0);
                    setState(27);
                    x();
                }
                break;
                case 3:
                    enterOuterAlt(_localctx, 3);
                {
                    setState(29);
                    u();
                    setState(30);
                    match(T__0);
                    setState(31);
                    x();
                    setState(32);
                    d();
                    setState(33);
                    n();
                }
                break;
                case 4:
                    enterOuterAlt(_localctx, 4);
                {
                    setState(35);
                    j();
                    setState(36);
                    x();
                }
                break;
                case 5:
                    enterOuterAlt(_localctx, 5);
                {
                    setState(38);
                    u();
                    setState(39);
                    h();
                    setState(40);
                    x();
                    setState(41);
                    l();
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
                setState(46);
                _la = _input.LA(1);
                if (_la == A) {
                    {
                        setState(45);
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

    public static class DContext extends ParserRuleContext {
        public DContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_d;
        }
    }

    public final DContext d() throws RecognitionException {
        DContext _localctx = new DContext(_ctx, getState());
        enterRule(_localctx, 4, RULE_d);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(48);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
        enterRule(_localctx, 6, RULE_n);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(50);
                _la = _input.LA(1);
                if (!(_la == T__4 || _la == T__5)) {
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
        enterRule(_localctx, 8, RULE_j);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(52);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__6) | (1L << T__7) | (1L << T__8))) != 0))) {
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

    public static class HContext extends ParserRuleContext {
        public HContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_h;
        }
    }

    public final HContext h() throws RecognitionException {
        HContext _localctx = new HContext(_ctx, getState());
        enterRule(_localctx, 10, RULE_h);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(54);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__9) | (1L << T__10) | (1L << T__11))) != 0))) {
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

    public static class LContext extends ParserRuleContext {
        public LContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_l;
        }
    }

    public final LContext l() throws RecognitionException {
        LContext _localctx = new LContext(_ctx, getState());
        enterRule(_localctx, 12, RULE_l);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(56);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__8) | (1L << T__12) | (1L << T__13) | (1L << T__14))) != 0))) {
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
        enterRule(_localctx, 14, RULE_x);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(58);
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
            return getToken(CallParser.A, 0);
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
        enterRule(_localctx, 16, RULE_a);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                {
                    setState(60);
                    match(A);
                    setState(64);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 2, _ctx);
                    while (_alt != 1 && _alt != ATN.INVALID_ALT_NUMBER) {
                        if (_alt == 1 + 1) {
                            {
                                {
                                    setState(61);
                                    matchWildcard();
                                }
                            }
                        }
                        setState(66);
                        _errHandler.sync(this);
                        _alt = getInterpreter().adaptivePredict(_input, 2, _ctx);
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
            "\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\22F\4\2\t\2\4\3\t" +
                    "\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\3\2\3\2\3\2" +
                    "\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3" +
                    "\2\3\2\3\2\3\2\3\2\5\2.\n\2\3\3\5\3\61\n\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7" +
                    "\3\7\3\b\3\b\3\t\3\t\3\n\3\n\7\nA\n\n\f\n\16\nD\13\n\3\n\3B\2\13\2\4\6" +
                    "\b\n\f\16\20\22\2\7\3\2\4\6\3\2\7\b\4\2\5\6\t\13\3\2\f\16\4\2\13\13\17" +
                    "\21B\2-\3\2\2\2\4\60\3\2\2\2\6\62\3\2\2\2\b\64\3\2\2\2\n\66\3\2\2\2\f" +
                    "8\3\2\2\2\16:\3\2\2\2\20<\3\2\2\2\22>\3\2\2\2\24\25\5\4\3\2\25\26\5\6" +
                    "\4\2\26\27\5\b\5\2\27\30\7\3\2\2\30\31\5\20\t\2\31.\3\2\2\2\32\33\5\4" +
                    "\3\2\33\34\5\6\4\2\34\35\7\3\2\2\35\36\5\20\t\2\36.\3\2\2\2\37 \5\4\3" +
                    "\2 !\7\3\2\2!\"\5\20\t\2\"#\5\6\4\2#$\5\b\5\2$.\3\2\2\2%&\5\n\6\2&\'\5" +
                    "\20\t\2\'.\3\2\2\2()\5\4\3\2)*\5\f\7\2*+\5\20\t\2+,\5\16\b\2,.\3\2\2\2" +
                    "-\24\3\2\2\2-\32\3\2\2\2-\37\3\2\2\2-%\3\2\2\2-(\3\2\2\2.\3\3\2\2\2/\61" +
                    "\5\22\n\2\60/\3\2\2\2\60\61\3\2\2\2\61\5\3\2\2\2\62\63\t\2\2\2\63\7\3" +
                    "\2\2\2\64\65\t\3\2\2\65\t\3\2\2\2\66\67\t\4\2\2\67\13\3\2\2\289\t\5\2" +
                    "\29\r\3\2\2\2:;\t\6\2\2;\17\3\2\2\2<=\5\22\n\2=\21\3\2\2\2>B\7\22\2\2" +
                    "?A\13\2\2\2@?\3\2\2\2AD\3\2\2\2BC\3\2\2\2B@\3\2\2\2C\23\3\2\2\2DB\3\2" +
                    "\2\2\5-\60B";
    public static final ATN _ATN =
            new ATNDeserializer().deserialize(_serializedATN.toCharArray());

    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}
