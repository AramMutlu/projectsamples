package model; /**
 * Created by Aram_ on 27-3-2018.
 */
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public class CompilerException extends RuntimeException {
    public CompilerException( ParserRuleContext ctx, String s ) {
        super(buildMessage(ctx, s));
    }

    private static String buildMessage( ParserRuleContext ctx, String msg ) {
        Token firstToken = ctx.getStart();
        int pos = firstToken.getStartIndex();

        return String.format("pos %d: %s", pos, msg);
    }
}
