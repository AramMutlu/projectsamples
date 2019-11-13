import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Aram_ on 2-4-2018.
 */
public class LangVisitor extends LanguageBaseVisitor<ArrayList<String>> {

    private int nrIfStatement = 0;
    private int nrWhileStatement = 0;

    private final String name;
    private HashMap<String, Integer> variables = new HashMap<>();

    public LangVisitor(String name){
        this.name = name;
    }


    public ArrayList<String> visitProgram(LanguageParser.ProgramContext ctx){
        ArrayList<String> code = new ArrayList<>();
        for (LanguageParser.StatementContext s:ctx.statement()){
            ArrayList<String> statementCode = visit(s);
            code.addAll(statementCode);
        }
        return code;
    }

    public ArrayList<String> visitExMulDiv(LanguageParser.ExMulDivContext ctx){
        ArrayList<String> code = new ArrayList<>();
        code.addAll(visit(ctx.leftexpr));
        code.addAll(visit(ctx.rightexpr));
        switch (ctx.op.getText()){
            case "*":
                code.add("imul");
                break;
            case "/":
                code.add("idiv");
                break;
            default:
                throw new IllegalStateException("Error ar visitExMulDiv");
        }

        return code;
    }

    public ArrayList<String> visitExAddOp(LanguageParser.ExAddOpContext ctx){
        ArrayList<String> code = new ArrayList<>();
        code.addAll(visit(ctx.leftexpr));
        code.addAll(visit(ctx.rightexpr));
        switch (ctx.op.getText()){
            case "+":
                code.add("iadd");
                break;
            case "-":
                code.add("isub");
                break;
            default:
                throw new IllegalStateException("Error at visitExAddOp");
        }

        return code;
    }

    public ArrayList<String> visitExLogicalBoolean(LanguageParser.ExLogicalBooleanContext ctx){
        ArrayList<String> code = new ArrayList<>();
        code.addAll(visit(ctx.leftexpr));
        code.addAll(visit(ctx.rightexpr));
        code.add("if_icmp" + returnOperator(ctx.op) + " then" + nrIfStatement);

        return code;
    }

    public ArrayList<String> visitIf_statement(LanguageParser.If_statementContext ctx){
        ArrayList<String> code = visit(ctx.expr);
        int nr = nrIfStatement++;
        if (ctx.elseblock != null){
            code.add("else" + nr + ":");
            code.addAll(visit(ctx.elseblock));
        }
        code.add("goto endif_" + nr);
        code.add("then" +nr+ ":");
        if (ctx.ifblock != null){
            String endif = "endif_" + nr;
            //code.add("ifeq " + endif);
            code.addAll(visit(ctx.ifblock));
            code.add(endif + ":");
        }


        return code;
    }

    public ArrayList<String> visitVariableDecl (LanguageParser.VariableDeclContext ctx ){
        ArrayList<String> code = new ArrayList<>();

        String varName = ctx.ID().getText();
        Integer index = variables.get(varName);
        if (index == null){
            index = variables.size() + 1;
            variables.put(varName, index);
        }
        code.addAll(visit(ctx.expression()));
        code.add("istore " + index);


        return code;
    }

    public ArrayList<String> visitExBoolean(LanguageParser.ExBooleanContext ctx){
        ArrayList<String> code = new ArrayList<>();

        if (ctx.BOOLEAN().getText().equals("true")){
            code.add("iconst_1");
        } else {
            code.add("iconst_0");
        }

        return code;
    }

    public ArrayList<String> visitAssignment(LanguageParser.AssignmentContext ctx){

        String varName = ctx.ID().getText();
        Integer index = variables.get(varName);
        if (index == null){
            index = variables.size() + 1;
            variables.put(varName, index);
        }

        ArrayList<String> code = new ArrayList<>();
        code.addAll(visit(ctx.expression()));
        code.add("istore " + index);

        return code;
    }

    public ArrayList<String> visitExID (LanguageParser.ExIDContext ctx){
        String varName = ctx.ID().getText();
        Integer index = variables.get(varName);

        if (index == null){
            throw new IllegalArgumentException("fout op regel " + ctx.getStart().getLine());
        }

        ArrayList<String> code = new ArrayList<>();
        code.add("iload " + index);

        return code;
    }

    public ArrayList<String> visitPrint_statement(LanguageParser.Print_statementContext ctx){
        ArrayList<String> code = new ArrayList<>();
        code.add("getstatic java/lang/System/out Ljava/io/PrintStream;");
        code.addAll(visit(ctx.expr));
        code.add("invokevirtual java/io/PrintStream/println(I)V");

        return code;
    }

    public ArrayList<String> visitExIntLiteral (LanguageParser.ExIntLiteralContext ctx){
        ArrayList<String> code = new ArrayList<>();
        code.add("ldc " + ctx.getText());
        return code;
    }

    //TODO
    /*
    public ArrayList<String> (LanguageParser. ctx){
        ArrayList<String> code = new ArrayList<>();

        return code;
    }

    visitStatement
    visitWhile_statement
     */

    private String returnOperator(Token token) {

        switch (token.getText()) {
            case "<":
                return "lt";
            case "<=":
                return "le";
            case ">":
                return "gt";
            case ">=":
                return "ge";
            case "==":
                return "eq";
            case "!=":
                return "ne";
            default:
                return null;
        }

    }

}
