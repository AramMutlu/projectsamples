import model.CompilerException;
import model.Type;

/**
 * Created by Aram_ on 21-3-2018.
 */
 public class TypeChecker extends LanguageBaseVisitor<Type> {

   public Type visitExLogicalBoolean(LanguageParser.ExLogicalBooleanContext ctx){
       Type leftType = visit(ctx.leftexpr);
       Type rightType = visit(ctx.rightexpr);

       if (leftType == null)
           throw new CompilerException(ctx, "Expected input");
       if (rightType == null)
           throw new CompilerException(ctx, "Expected input");

       return Type.BOOLEAN;
   }

    public Type visitExMulDiv(LanguageParser.ExMulDivContext ctx){
        Type leftType = visit(ctx.leftexpr);
        Type rightType = visit(ctx.rightexpr);

        if (leftType != Type.INT || rightType != Type.INT)
            throw new CompilerException(ctx, "Expected int");
        return Type.INT;
    }

    public Type visitExAddOp(LanguageParser.ExAddOpContext ctx){
        Type leftType = visit(ctx.leftexpr);
        Type rightType = visit(ctx.rightexpr);

        if (leftType != Type.INT || rightType != Type.INT)
            throw new CompilerException(ctx, "Expected int");
        return Type.INT;
    }

    public Type visitVariableDecl(LanguageParser.VariableDeclContext ctx){
        Type data = visit(ctx.DATATYPE());
        Type expr = visit(ctx.expr);

        if (data == Type.INT){
            if (expr != Type.INT){
                throw new CompilerException(ctx, "Expected int");
            }
        } else if (data == Type.BOOLEAN){
            if (expr != Type.BOOLEAN){
                throw new CompilerException(ctx, "Expected boolean");
            }
        }
        return null;
    }

/*
    public Type visitExpression(LanguageParser.ExpressionContext ctx){
        Type expr = visit(ctx);

        if (expr != Type.BOOLEAN){
            throw new CompilerException(ctx, "Expected boolean expression (Condition)");
        }
        return  Type.BOOLEAN;
    }
*/

    public Type visitIf_statement(LanguageParser.If_statementContext ctx){
        Type expr = visit(ctx.expr);

        if (expr != Type.BOOLEAN){
             throw new CompilerException(ctx, "Expected boolean condition (if statement)");
        }
        return null;
    }

    public Type visitWhile_statement(LanguageParser.While_statementContext ctx){
        Type cond = visit(ctx.expression());

        if (cond != Type.BOOLEAN){
            throw new CompilerException(ctx, "Expected boolean condition (while statement)");
        }
        return null;
    }

    public Type visitExIntLiteral(LanguageParser.ExIntLiteralContext ctx){
        return Type.INT;
    }

    public Type visitExBoolean(LanguageParser.ExBooleanContext ctx){
        return Type.BOOLEAN;
    }

    public Type visitExID(LanguageParser.ExIDContext ctx){
        return Type.ID;
    }


    /*
    //TODO
    visitPrint_statement
     */

}
