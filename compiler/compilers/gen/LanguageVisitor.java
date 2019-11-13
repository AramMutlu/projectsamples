// Generated from C:/Users/Aram_/IdeaProjects/compilers/src\Language.g4 by ANTLR 4.7
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link LanguageParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface LanguageVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link LanguageParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(LanguageParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link LanguageParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(LanguageParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link LanguageParser#variableDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDecl(LanguageParser.VariableDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link LanguageParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment(LanguageParser.AssignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link LanguageParser#if_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_statement(LanguageParser.If_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link LanguageParser#while_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhile_statement(LanguageParser.While_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link LanguageParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(LanguageParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link LanguageParser#print_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrint_statement(LanguageParser.Print_statementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExLogicalBoolean}
	 * labeled alternative in {@link LanguageParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExLogicalBoolean(LanguageParser.ExLogicalBooleanContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExMulDiv}
	 * labeled alternative in {@link LanguageParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExMulDiv(LanguageParser.ExMulDivContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExBoolean}
	 * labeled alternative in {@link LanguageParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExBoolean(LanguageParser.ExBooleanContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExAddOp}
	 * labeled alternative in {@link LanguageParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExAddOp(LanguageParser.ExAddOpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExIntLiteral}
	 * labeled alternative in {@link LanguageParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExIntLiteral(LanguageParser.ExIntLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExID}
	 * labeled alternative in {@link LanguageParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExID(LanguageParser.ExIDContext ctx);
}