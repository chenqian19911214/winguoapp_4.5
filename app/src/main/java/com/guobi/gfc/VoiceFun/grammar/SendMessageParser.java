package com.guobi.gfc.VoiceFun.grammar;

// Generated from SendMessage.g4 by ANTLR 4.5

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
public class SendMessageParser extends Parser {
    static {
        RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION);
    }

    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
    public static final int T__0 = 1, T__1 = 2, T__2 = 3, T__3 = 4, T__4 = 5, T__5 = 6, T__6 = 7, M = 8, L = 9, C = 10, A = 11;
    public static final int RULE_s = 0, RULE_u = 1, RULE_w = 2, RULE_f = 3, RULE_x = 4, RULE_y = 5, RULE_z = 6, RULE_b = 7, RULE_v = 8, RULE_a = 9;
    public static final String[] ruleNames = {"s", "u", "w", "f", "x", "y", "z", "b", "v", "a"};

    private static final String[] _LITERAL_NAMES = {null, "'告诉'", "'给'", "'发'", "'发送'", "'分享'", "'把'", "'将'"};
    private static final String[] _SYMBOLIC_NAMES = {null, null, null, null, null, null, null, null, "M", "L", "C", "A"};
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
    public ATN getATN() {
        return _ATN;
    }

    public SendMessageParser(TokenStream input) {
        super(input);
        _interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    public static class SContext extends ParserRuleContext {
        public UContext u() {
            return getRuleContext(UContext.class, 0);
        }

        public YContext y() {
            return getRuleContext(YContext.class, 0);
        }

        public TerminalNode C() {
            return getToken(SendMessageParser.C, 0);
        }

        public ZContext z() {
            return getRuleContext(ZContext.class, 0);
        }

        public WContext w() {
            return getRuleContext(WContext.class, 0);
        }

        public FContext f() {
            return getRuleContext(FContext.class, 0);
        }

        public TerminalNode M() {
            return getToken(SendMessageParser.M, 0);
        }

        public XContext x() {
            return getRuleContext(XContext.class, 0);
        }

        public VContext v() {
            return getRuleContext(VContext.class, 0);
        }

        public BContext b() {
            return getRuleContext(BContext.class, 0);
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
            setState(145);
            switch (getInterpreter().adaptivePredict(_input, 0, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(20);
                    u();
                    setState(21);
                    match(T__0);
                    setState(22);
                    y();
                    setState(23);
                    match(C);
                    setState(24);
                    z();
                }
                break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(26);
                    u();
                    setState(27);
                    match(T__0);
                    setState(28);
                    w();
                }
                break;
                case 3:
                    enterOuterAlt(_localctx, 3);
                {
                    setState(30);
                    u();
                    setState(31);
                    f();
                    setState(32);
                    match(M);
                    setState(33);
                    match(T__1);
                    setState(34);
                    y();
                    setState(35);
                    match(C);
                    setState(36);
                    z();
                }
                break;
                case 4:
                    enterOuterAlt(_localctx, 4);
                {
                    setState(38);
                    u();
                    setState(39);
                    f();
                    setState(40);
                    match(M);
                    setState(41);
                    match(T__1);
                    setState(42);
                    w();
                }
                break;
                case 5:
                    enterOuterAlt(_localctx, 5);
                {
                    setState(44);
                    u();
                    setState(45);
                    f();
                    setState(46);
                    x();
                    setState(47);
                    match(T__1);
                    setState(48);
                    y();
                    setState(49);
                    match(C);
                    setState(50);
                    z();
                }
                break;
                case 6:
                    enterOuterAlt(_localctx, 6);
                {
                    setState(52);
                    u();
                    setState(53);
                    f();
                    setState(54);
                    x();
                    setState(55);
                    match(T__1);
                    setState(56);
                    w();
                }
                break;
                case 7:
                    enterOuterAlt(_localctx, 7);
                {
                    setState(58);
                    u();
                    setState(59);
                    match(T__1);
                    setState(60);
                    y();
                    setState(61);
                    v();
                    setState(62);
                    match(M);
                    setState(63);
                    match(C);
                    setState(64);
                    z();
                }
                break;
                case 8:
                    enterOuterAlt(_localctx, 8);
                {
                    setState(66);
                    u();
                    setState(67);
                    match(T__1);
                    setState(68);
                    y();
                    setState(69);
                    v();
                    setState(70);
                    match(M);
                    setState(71);
                    x();
                }
                break;
                case 9:
                    enterOuterAlt(_localctx, 9);
                {
                    setState(73);
                    u();
                    setState(74);
                    match(T__1);
                    setState(75);
                    y();
                    setState(76);
                    f();
                    setState(77);
                    x();
                    setState(78);
                    match(C);
                    setState(79);
                    z();
                }
                break;
                case 10:
                    enterOuterAlt(_localctx, 10);
                {
                    setState(81);
                    u();
                    setState(82);
                    match(T__1);
                    setState(83);
                    y();
                    setState(84);
                    f();
                    setState(85);
                    x();
                }
                break;
                case 11:
                    enterOuterAlt(_localctx, 11);
                {
                    setState(87);
                    u();
                    setState(88);
                    b();
                    setState(89);
                    match(M);
                    setState(90);
                    v();
                    setState(91);
                    match(T__1);
                    setState(92);
                    y();
                    setState(93);
                    match(C);
                    setState(94);
                    z();
                }
                break;
                case 12:
                    enterOuterAlt(_localctx, 12);
                {
                    setState(96);
                    u();
                    setState(97);
                    b();
                    setState(98);
                    match(M);
                    setState(99);
                    v();
                    setState(100);
                    match(T__1);
                    setState(101);
                    w();
                }
                break;
                case 13:
                    enterOuterAlt(_localctx, 13);
                {
                    setState(103);
                    u();
                    setState(104);
                    b();
                    setState(105);
                    x();
                    setState(106);
                    v();
                    setState(107);
                    match(T__1);
                    setState(108);
                    y();
                    setState(109);
                    match(C);
                    setState(110);
                    z();
                }
                break;
                case 14:
                    enterOuterAlt(_localctx, 14);
                {
                    setState(112);
                    u();
                    setState(113);
                    b();
                    setState(114);
                    x();
                    setState(115);
                    v();
                    setState(116);
                    match(T__1);
                    setState(117);
                    w();
                }
                break;
                case 15:
                    enterOuterAlt(_localctx, 15);
                {
                    setState(119);
                    u();
                    setState(120);
                    match(M);
                    setState(121);
                    v();
                    setState(122);
                    match(T__1);
                    setState(123);
                    y();
                    setState(124);
                    match(C);
                    setState(125);
                    z();
                }
                break;
                case 16:
                    enterOuterAlt(_localctx, 16);
                {
                    setState(127);
                    u();
                    setState(128);
                    match(M);
                    setState(129);
                    v();
                    setState(130);
                    match(T__1);
                    setState(131);
                    w();
                }
                break;
                case 17:
                    enterOuterAlt(_localctx, 17);
                {
                    setState(133);
                    x();
                    setState(134);
                    v();
                    setState(135);
                    match(T__1);
                    setState(136);
                    y();
                    setState(137);
                    match(C);
                    setState(138);
                    z();
                }
                break;
                case 18:
                    enterOuterAlt(_localctx, 18);
                {
                    setState(140);
                    x();
                    setState(141);
                    v();
                    setState(142);
                    match(T__1);
                    setState(143);
                    w();
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
                setState(148);
                _la = _input.LA(1);
                if (_la == A) {
                    {
                        setState(147);
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

    public static class WContext extends ParserRuleContext {
        public AContext a() {
            return getRuleContext(AContext.class, 0);
        }

        public WContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_w;
        }
    }

    public final WContext w() throws RecognitionException {
        WContext _localctx = new WContext(_ctx, getState());
        enterRule(_localctx, 4, RULE_w);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(150);
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

    public static class FContext extends ParserRuleContext {
        public FContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_f;
        }
    }

    public final FContext f() throws RecognitionException {
        FContext _localctx = new FContext(_ctx, getState());
        enterRule(_localctx, 6, RULE_f);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(152);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__4))) != 0))) {
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
        enterRule(_localctx, 8, RULE_x);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(154);
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

    public static class YContext extends ParserRuleContext {
        public AContext a() {
            return getRuleContext(AContext.class, 0);
        }

        public YContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_y;
        }
    }

    public final YContext y() throws RecognitionException {
        YContext _localctx = new YContext(_ctx, getState());
        enterRule(_localctx, 10, RULE_y);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(156);
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

    public static class ZContext extends ParserRuleContext {
        public AContext a() {
            return getRuleContext(AContext.class, 0);
        }

        public ZContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_z;
        }
    }

    public final ZContext z() throws RecognitionException {
        ZContext _localctx = new ZContext(_ctx, getState());
        enterRule(_localctx, 12, RULE_z);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(159);
                _la = _input.LA(1);
                if (_la == A) {
                    {
                        setState(158);
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

    public static class BContext extends ParserRuleContext {
        public BContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_b;
        }
    }

    public final BContext b() throws RecognitionException {
        BContext _localctx = new BContext(_ctx, getState());
        enterRule(_localctx, 14, RULE_b);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(161);
                _la = _input.LA(1);
                if (!(_la == T__5 || _la == T__6)) {
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

    public static class VContext extends ParserRuleContext {
        public FContext f() {
            return getRuleContext(FContext.class, 0);
        }

        public VContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_v;
        }
    }

    public final VContext v() throws RecognitionException {
        VContext _localctx = new VContext(_ctx, getState());
        enterRule(_localctx, 16, RULE_v);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(164);
                _la = _input.LA(1);
                if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__4))) != 0)) {
                    {
                        setState(163);
                        f();
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

    public static class AContext extends ParserRuleContext {
        public TerminalNode A() {
            return getToken(SendMessageParser.A, 0);
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
        enterRule(_localctx, 18, RULE_a);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                {
                    setState(166);
                    match(A);
                    setState(170);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 4, _ctx);
                    while (_alt != 1 && _alt != ATN.INVALID_ALT_NUMBER) {
                        if (_alt == 1 + 1) {
                            {
                                {
                                    setState(167);
                                    matchWildcard();
                                }
                            }
                        }
                        setState(172);
                        _errHandler.sync(this);
                        _alt = getInterpreter().adaptivePredict(_input, 4, _ctx);
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

    public static final String _serializedATN = "\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\r\u00b0\4\2\t\2\4"
            + "\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"
            + "\13\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3"
            + "\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2"
            + "\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3"
            + "\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2"
            + "\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3"
            + "\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2"
            + "\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3"
            + "\2\3\2\3\2\3\2\5\2\u0094\n\2\3\3\5\3\u0097\n\3\3\4\3\4\3\5\3\5\3\6\3\6"
            + "\3\7\3\7\3\b\5\b\u00a2\n\b\3\t\3\t\3\n\5\n\u00a7\n\n\3\13\3\13\7\13\u00ab"
            + "\n\13\f\13\16\13\u00ae\13\13\3\13\3\u00ac\2\f\2\4\6\b\n\f\16\20\22\24"
            + "\2\4\3\2\5\7\3\2\b\t\u00ba\2\u0093\3\2\2\2\4\u0096\3\2\2\2\6\u0098\3\2"
            + "\2\2\b\u009a\3\2\2\2\n\u009c\3\2\2\2\f\u009e\3\2\2\2\16\u00a1\3\2\2\2"
            + "\20\u00a3\3\2\2\2\22\u00a6\3\2\2\2\24\u00a8\3\2\2\2\26\27\5\4\3\2\27\30"
            + "\7\3\2\2\30\31\5\f\7\2\31\32\7\f\2\2\32\33\5\16\b\2\33\u0094\3\2\2\2\34"
            + "\35\5\4\3\2\35\36\7\3\2\2\36\37\5\6\4\2\37\u0094\3\2\2\2 !\5\4\3\2!\""
            + "\5\b\5\2\"#\7\n\2\2#$\7\4\2\2$%\5\f\7\2%&\7\f\2\2&\'\5\16\b\2\'\u0094"
            + "\3\2\2\2()\5\4\3\2)*\5\b\5\2*+\7\n\2\2+,\7\4\2\2,-\5\6\4\2-\u0094\3\2"
            + "\2\2./\5\4\3\2/\60\5\b\5\2\60\61\5\n\6\2\61\62\7\4\2\2\62\63\5\f\7\2\63"
            + "\64\7\f\2\2\64\65\5\16\b\2\65\u0094\3\2\2\2\66\67\5\4\3\2\678\5\b\5\2"
            + "89\5\n\6\29:\7\4\2\2:;\5\6\4\2;\u0094\3\2\2\2<=\5\4\3\2=>\7\4\2\2>?\5"
            + "\f\7\2?@\5\22\n\2@A\7\n\2\2AB\7\f\2\2BC\5\16\b\2C\u0094\3\2\2\2DE\5\4"
            + "\3\2EF\7\4\2\2FG\5\f\7\2GH\5\22\n\2HI\7\n\2\2IJ\5\n\6\2J\u0094\3\2\2\2"
            + "KL\5\4\3\2LM\7\4\2\2MN\5\f\7\2NO\5\b\5\2OP\5\n\6\2PQ\7\f\2\2QR\5\16\b"
            + "\2R\u0094\3\2\2\2ST\5\4\3\2TU\7\4\2\2UV\5\f\7\2VW\5\b\5\2WX\5\n\6\2X\u0094"
            + "\3\2\2\2YZ\5\4\3\2Z[\5\20\t\2[\\\7\n\2\2\\]\5\22\n\2]^\7\4\2\2^_\5\f\7"
            + "\2_`\7\f\2\2`a\5\16\b\2a\u0094\3\2\2\2bc\5\4\3\2cd\5\20\t\2de\7\n\2\2"
            + "ef\5\22\n\2fg\7\4\2\2gh\5\6\4\2h\u0094\3\2\2\2ij\5\4\3\2jk\5\20\t\2kl"
            + "\5\n\6\2lm\5\22\n\2mn\7\4\2\2no\5\f\7\2op\7\f\2\2pq\5\16\b\2q\u0094\3"
            + "\2\2\2rs\5\4\3\2st\5\20\t\2tu\5\n\6\2uv\5\22\n\2vw\7\4\2\2wx\5\6\4\2x"
            + "\u0094\3\2\2\2yz\5\4\3\2z{\7\n\2\2{|\5\22\n\2|}\7\4\2\2}~\5\f\7\2~\177"
            + "\7\f\2\2\177\u0080\5\16\b\2\u0080\u0094\3\2\2\2\u0081\u0082\5\4\3\2\u0082"
            + "\u0083\7\n\2\2\u0083\u0084\5\22\n\2\u0084\u0085\7\4\2\2\u0085\u0086\5"
            + "\6\4\2\u0086\u0094\3\2\2\2\u0087\u0088\5\n\6\2\u0088\u0089\5\22\n\2\u0089"
            + "\u008a\7\4\2\2\u008a\u008b\5\f\7\2\u008b\u008c\7\f\2\2\u008c\u008d\5\16"
            + "\b\2\u008d\u0094\3\2\2\2\u008e\u008f\5\n\6\2\u008f\u0090\5\22\n\2\u0090"
            + "\u0091\7\4\2\2\u0091\u0092\5\6\4\2\u0092\u0094\3\2\2\2\u0093\26\3\2\2"
            + "\2\u0093\34\3\2\2\2\u0093 \3\2\2\2\u0093(\3\2\2\2\u0093.\3\2\2\2\u0093"
            + "\66\3\2\2\2\u0093<\3\2\2\2\u0093D\3\2\2\2\u0093K\3\2\2\2\u0093S\3\2\2"
            + "\2\u0093Y\3\2\2\2\u0093b\3\2\2\2\u0093i\3\2\2\2\u0093r\3\2\2\2\u0093y"
            + "\3\2\2\2\u0093\u0081\3\2\2\2\u0093\u0087\3\2\2\2\u0093\u008e\3\2\2\2\u0094"
            + "\3\3\2\2\2\u0095\u0097\5\24\13\2\u0096\u0095\3\2\2\2\u0096\u0097\3\2\2"
            + "\2\u0097\5\3\2\2\2\u0098\u0099\5\24\13\2\u0099\7\3\2\2\2\u009a\u009b\t"
            + "\2\2\2\u009b\t\3\2\2\2\u009c\u009d\5\24\13\2\u009d\13\3\2\2\2\u009e\u009f"
            + "\5\24\13\2\u009f\r\3\2\2\2\u00a0\u00a2\5\24\13\2\u00a1\u00a0\3\2\2\2\u00a1"
            + "\u00a2\3\2\2\2\u00a2\17\3\2\2\2\u00a3\u00a4\t\3\2\2\u00a4\21\3\2\2\2\u00a5"
            + "\u00a7\5\b\5\2\u00a6\u00a5\3\2\2\2\u00a6\u00a7\3\2\2\2\u00a7\23\3\2\2"
            + "\2\u00a8\u00ac\7\r\2\2\u00a9\u00ab\13\2\2\2\u00aa\u00a9\3\2\2\2\u00ab"
            + "\u00ae\3\2\2\2\u00ac\u00ad\3\2\2\2\u00ac\u00aa\3\2\2\2\u00ad\25\3\2\2" + "\2\u00ae\u00ac\3\2\2\2\7\u0093\u0096\u00a1\u00a6\u00ac";
    public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());

    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}
