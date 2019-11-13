// Generated from C:/Users/Aram_/IdeaProjects/compilers/src\Language.g4 by ANTLR 4.7
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class LanguageParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, EQUALS=17, 
		PAR_OPEN=18, PAR_CLOSE=19, SEMICOLON=20, DATATYPE=21, BOOLEAN=22, ID=23, 
		INT=24, WHITESPACE=25;
	public static final int
		RULE_program = 0, RULE_statement = 1, RULE_variableDecl = 2, RULE_assignment = 3, 
		RULE_if_statement = 4, RULE_while_statement = 5, RULE_block = 6, RULE_print_statement = 7, 
		RULE_expression = 8;
	public static final String[] ruleNames = {
		"program", "statement", "variableDecl", "assignment", "if_statement", 
		"while_statement", "block", "print_statement", "expression"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'if'", "'{'", "'}'", "'else'", "'while'", "'print '", "'+'", "'-'", 
		"'*'", "'/'", "'<'", "'<='", "'=='", "'!='", "'>'", "'>='", "'='", "'('", 
		"')'", "';'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, "EQUALS", "PAR_OPEN", "PAR_CLOSE", "SEMICOLON", 
		"DATATYPE", "BOOLEAN", "ID", "INT", "WHITESPACE"
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
	public String getGrammarFileName() { return "Language.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public LanguageParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ProgramContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(LanguageParser.EOF, 0); }
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LanguageVisitor ) return ((LanguageVisitor<? extends T>)visitor).visitProgram(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(21);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__4) | (1L << T__5) | (1L << DATATYPE) | (1L << BOOLEAN) | (1L << ID) | (1L << INT))) != 0)) {
				{
				{
				setState(18);
				statement();
				}
				}
				setState(23);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(24);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StatementContext extends ParserRuleContext {
		public VariableDeclContext variableDecl() {
			return getRuleContext(VariableDeclContext.class,0);
		}
		public AssignmentContext assignment() {
			return getRuleContext(AssignmentContext.class,0);
		}
		public If_statementContext if_statement() {
			return getRuleContext(If_statementContext.class,0);
		}
		public While_statementContext while_statement() {
			return getRuleContext(While_statementContext.class,0);
		}
		public Print_statementContext print_statement() {
			return getRuleContext(Print_statementContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LanguageVisitor ) return ((LanguageVisitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_statement);
		try {
			setState(32);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(26);
				variableDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(27);
				assignment();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(28);
				if_statement();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(29);
				while_statement();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(30);
				print_statement();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(31);
				expression(0);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VariableDeclContext extends ParserRuleContext {
		public Token dataType;
		public ExpressionContext expr;
		public TerminalNode ID() { return getToken(LanguageParser.ID, 0); }
		public TerminalNode SEMICOLON() { return getToken(LanguageParser.SEMICOLON, 0); }
		public TerminalNode DATATYPE() { return getToken(LanguageParser.DATATYPE, 0); }
		public TerminalNode EQUALS() { return getToken(LanguageParser.EQUALS, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public VariableDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LanguageVisitor ) return ((LanguageVisitor<? extends T>)visitor).visitVariableDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableDeclContext variableDecl() throws RecognitionException {
		VariableDeclContext _localctx = new VariableDeclContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_variableDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(34);
			((VariableDeclContext)_localctx).dataType = match(DATATYPE);
			setState(35);
			match(ID);
			setState(38);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==EQUALS) {
				{
				setState(36);
				match(EQUALS);
				setState(37);
				((VariableDeclContext)_localctx).expr = expression(0);
				}
			}

			setState(40);
			match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AssignmentContext extends ParserRuleContext {
		public ExpressionContext expr;
		public TerminalNode ID() { return getToken(LanguageParser.ID, 0); }
		public TerminalNode EQUALS() { return getToken(LanguageParser.EQUALS, 0); }
		public TerminalNode SEMICOLON() { return getToken(LanguageParser.SEMICOLON, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public AssignmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignment; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LanguageVisitor ) return ((LanguageVisitor<? extends T>)visitor).visitAssignment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssignmentContext assignment() throws RecognitionException {
		AssignmentContext _localctx = new AssignmentContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_assignment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(42);
			match(ID);
			setState(43);
			match(EQUALS);
			setState(44);
			((AssignmentContext)_localctx).expr = expression(0);
			setState(45);
			match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class If_statementContext extends ParserRuleContext {
		public ExpressionContext expr;
		public BlockContext ifblock;
		public BlockContext elseblock;
		public TerminalNode PAR_OPEN() { return getToken(LanguageParser.PAR_OPEN, 0); }
		public TerminalNode PAR_CLOSE() { return getToken(LanguageParser.PAR_CLOSE, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<BlockContext> block() {
			return getRuleContexts(BlockContext.class);
		}
		public BlockContext block(int i) {
			return getRuleContext(BlockContext.class,i);
		}
		public If_statementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_if_statement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LanguageVisitor ) return ((LanguageVisitor<? extends T>)visitor).visitIf_statement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final If_statementContext if_statement() throws RecognitionException {
		If_statementContext _localctx = new If_statementContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_if_statement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(47);
			match(T__0);
			setState(48);
			match(PAR_OPEN);
			setState(49);
			((If_statementContext)_localctx).expr = expression(0);
			setState(50);
			match(PAR_CLOSE);
			setState(51);
			match(T__1);
			setState(52);
			((If_statementContext)_localctx).ifblock = block();
			setState(53);
			match(T__2);
			setState(59);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__3) {
				{
				setState(54);
				match(T__3);
				setState(55);
				match(T__1);
				setState(56);
				((If_statementContext)_localctx).elseblock = block();
				setState(57);
				match(T__2);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class While_statementContext extends ParserRuleContext {
		public TerminalNode PAR_OPEN() { return getToken(LanguageParser.PAR_OPEN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode PAR_CLOSE() { return getToken(LanguageParser.PAR_CLOSE, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public While_statementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_while_statement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LanguageVisitor ) return ((LanguageVisitor<? extends T>)visitor).visitWhile_statement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final While_statementContext while_statement() throws RecognitionException {
		While_statementContext _localctx = new While_statementContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_while_statement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(61);
			match(T__4);
			setState(62);
			match(PAR_OPEN);
			setState(63);
			expression(0);
			setState(64);
			match(PAR_CLOSE);
			setState(65);
			match(T__1);
			setState(66);
			block();
			setState(67);
			match(T__2);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BlockContext extends ParserRuleContext {
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LanguageVisitor ) return ((LanguageVisitor<? extends T>)visitor).visitBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(72);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__4) | (1L << T__5) | (1L << DATATYPE) | (1L << BOOLEAN) | (1L << ID) | (1L << INT))) != 0)) {
				{
				{
				setState(69);
				statement();
				}
				}
				setState(74);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Print_statementContext extends ParserRuleContext {
		public ExpressionContext expr;
		public TerminalNode SEMICOLON() { return getToken(LanguageParser.SEMICOLON, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public Print_statementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_print_statement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LanguageVisitor ) return ((LanguageVisitor<? extends T>)visitor).visitPrint_statement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Print_statementContext print_statement() throws RecognitionException {
		Print_statementContext _localctx = new Print_statementContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_print_statement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(75);
			match(T__5);
			setState(76);
			((Print_statementContext)_localctx).expr = expression(0);
			setState(77);
			match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionContext extends ParserRuleContext {
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	 
		public ExpressionContext() { }
		public void copyFrom(ExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ExLogicalBooleanContext extends ExpressionContext {
		public ExpressionContext leftexpr;
		public Token op;
		public ExpressionContext rightexpr;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode SEMICOLON() { return getToken(LanguageParser.SEMICOLON, 0); }
		public ExLogicalBooleanContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LanguageVisitor ) return ((LanguageVisitor<? extends T>)visitor).visitExLogicalBoolean(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExMulDivContext extends ExpressionContext {
		public ExpressionContext leftexpr;
		public Token op;
		public ExpressionContext rightexpr;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode SEMICOLON() { return getToken(LanguageParser.SEMICOLON, 0); }
		public ExMulDivContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LanguageVisitor ) return ((LanguageVisitor<? extends T>)visitor).visitExMulDiv(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExBooleanContext extends ExpressionContext {
		public TerminalNode BOOLEAN() { return getToken(LanguageParser.BOOLEAN, 0); }
		public ExBooleanContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LanguageVisitor ) return ((LanguageVisitor<? extends T>)visitor).visitExBoolean(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExAddOpContext extends ExpressionContext {
		public ExpressionContext leftexpr;
		public Token op;
		public ExpressionContext rightexpr;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode SEMICOLON() { return getToken(LanguageParser.SEMICOLON, 0); }
		public ExAddOpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LanguageVisitor ) return ((LanguageVisitor<? extends T>)visitor).visitExAddOp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExIntLiteralContext extends ExpressionContext {
		public TerminalNode INT() { return getToken(LanguageParser.INT, 0); }
		public ExIntLiteralContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LanguageVisitor ) return ((LanguageVisitor<? extends T>)visitor).visitExIntLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExIDContext extends ExpressionContext {
		public TerminalNode ID() { return getToken(LanguageParser.ID, 0); }
		public ExIDContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LanguageVisitor ) return ((LanguageVisitor<? extends T>)visitor).visitExID(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 16;
		enterRecursionRule(_localctx, 16, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(83);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INT:
				{
				_localctx = new ExIntLiteralContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(80);
				match(INT);
				}
				break;
			case BOOLEAN:
				{
				_localctx = new ExBooleanContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(81);
				match(BOOLEAN);
				}
				break;
			case ID:
				{
				_localctx = new ExIDContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(82);
				match(ID);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(105);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(103);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
					case 1:
						{
						_localctx = new ExAddOpContext(new ExpressionContext(_parentctx, _parentState));
						((ExAddOpContext)_localctx).leftexpr = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(85);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(86);
						((ExAddOpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__6 || _la==T__7) ) {
							((ExAddOpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(87);
						((ExAddOpContext)_localctx).rightexpr = expression(0);
						setState(89);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
						case 1:
							{
							setState(88);
							match(SEMICOLON);
							}
							break;
						}
						}
						break;
					case 2:
						{
						_localctx = new ExMulDivContext(new ExpressionContext(_parentctx, _parentState));
						((ExMulDivContext)_localctx).leftexpr = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(91);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(92);
						((ExMulDivContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__8 || _la==T__9) ) {
							((ExMulDivContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(93);
						((ExMulDivContext)_localctx).rightexpr = expression(0);
						setState(95);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
						case 1:
							{
							setState(94);
							match(SEMICOLON);
							}
							break;
						}
						}
						break;
					case 3:
						{
						_localctx = new ExLogicalBooleanContext(new ExpressionContext(_parentctx, _parentState));
						((ExLogicalBooleanContext)_localctx).leftexpr = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(97);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(98);
						((ExLogicalBooleanContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15))) != 0)) ) {
							((ExLogicalBooleanContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(99);
						((ExLogicalBooleanContext)_localctx).rightexpr = expression(0);
						setState(101);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
						case 1:
							{
							setState(100);
							match(SEMICOLON);
							}
							break;
						}
						}
						break;
					}
					} 
				}
				setState(107);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 8:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 6);
		case 1:
			return precpred(_ctx, 5);
		case 2:
			return precpred(_ctx, 4);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\33o\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\3\2\7\2\26"+
		"\n\2\f\2\16\2\31\13\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\5\3#\n\3\3\4\3\4"+
		"\3\4\3\4\5\4)\n\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\3\6\3\6\3\6\3\6\5\6>\n\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\b"+
		"\7\bI\n\b\f\b\16\bL\13\b\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\5\nV\n\n\3\n"+
		"\3\n\3\n\3\n\5\n\\\n\n\3\n\3\n\3\n\3\n\5\nb\n\n\3\n\3\n\3\n\3\n\5\nh\n"+
		"\n\7\nj\n\n\f\n\16\nm\13\n\3\n\2\3\22\13\2\4\6\b\n\f\16\20\22\2\5\3\2"+
		"\t\n\3\2\13\f\3\2\r\22\2v\2\27\3\2\2\2\4\"\3\2\2\2\6$\3\2\2\2\b,\3\2\2"+
		"\2\n\61\3\2\2\2\f?\3\2\2\2\16J\3\2\2\2\20M\3\2\2\2\22U\3\2\2\2\24\26\5"+
		"\4\3\2\25\24\3\2\2\2\26\31\3\2\2\2\27\25\3\2\2\2\27\30\3\2\2\2\30\32\3"+
		"\2\2\2\31\27\3\2\2\2\32\33\7\2\2\3\33\3\3\2\2\2\34#\5\6\4\2\35#\5\b\5"+
		"\2\36#\5\n\6\2\37#\5\f\7\2 #\5\20\t\2!#\5\22\n\2\"\34\3\2\2\2\"\35\3\2"+
		"\2\2\"\36\3\2\2\2\"\37\3\2\2\2\" \3\2\2\2\"!\3\2\2\2#\5\3\2\2\2$%\7\27"+
		"\2\2%(\7\31\2\2&\'\7\23\2\2\')\5\22\n\2(&\3\2\2\2()\3\2\2\2)*\3\2\2\2"+
		"*+\7\26\2\2+\7\3\2\2\2,-\7\31\2\2-.\7\23\2\2./\5\22\n\2/\60\7\26\2\2\60"+
		"\t\3\2\2\2\61\62\7\3\2\2\62\63\7\24\2\2\63\64\5\22\n\2\64\65\7\25\2\2"+
		"\65\66\7\4\2\2\66\67\5\16\b\2\67=\7\5\2\289\7\6\2\29:\7\4\2\2:;\5\16\b"+
		"\2;<\7\5\2\2<>\3\2\2\2=8\3\2\2\2=>\3\2\2\2>\13\3\2\2\2?@\7\7\2\2@A\7\24"+
		"\2\2AB\5\22\n\2BC\7\25\2\2CD\7\4\2\2DE\5\16\b\2EF\7\5\2\2F\r\3\2\2\2G"+
		"I\5\4\3\2HG\3\2\2\2IL\3\2\2\2JH\3\2\2\2JK\3\2\2\2K\17\3\2\2\2LJ\3\2\2"+
		"\2MN\7\b\2\2NO\5\22\n\2OP\7\26\2\2P\21\3\2\2\2QR\b\n\1\2RV\7\32\2\2SV"+
		"\7\30\2\2TV\7\31\2\2UQ\3\2\2\2US\3\2\2\2UT\3\2\2\2Vk\3\2\2\2WX\f\b\2\2"+
		"XY\t\2\2\2Y[\5\22\n\2Z\\\7\26\2\2[Z\3\2\2\2[\\\3\2\2\2\\j\3\2\2\2]^\f"+
		"\7\2\2^_\t\3\2\2_a\5\22\n\2`b\7\26\2\2a`\3\2\2\2ab\3\2\2\2bj\3\2\2\2c"+
		"d\f\6\2\2de\t\4\2\2eg\5\22\n\2fh\7\26\2\2gf\3\2\2\2gh\3\2\2\2hj\3\2\2"+
		"\2iW\3\2\2\2i]\3\2\2\2ic\3\2\2\2jm\3\2\2\2ki\3\2\2\2kl\3\2\2\2l\23\3\2"+
		"\2\2mk\3\2\2\2\r\27\"(=JU[agik";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}