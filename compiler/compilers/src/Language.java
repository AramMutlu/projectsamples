import model.Type;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Created by Aram_ on 4-4-2018.
 */
public class Language {

    private String defaultCode  = "a=5 ;  b = 4 ; a = 34 + 34 + b / 4 ;\n"+
            "print a;" +
            "print 34 + 34 - 8 /4;";

    private static String startProg = ".class public {{name}}\n" +
            ".super java/lang/Object\n" +
            "\n" +
            ";\n" +
            "; standard initializer (calls java.lang.Object's initializer)\n" +
            ";\n" +
            ".method public <init>()V\n" +
            "\taload_0\n" +
            "\tinvokenonvirtual java/lang/Object/<init>()V\n" +
            "\treturn\n" +
            ".end method\n" +
            "\n" +
            ";\n" +
            "; main() method\n" +
            ";\n" +
            ".method public static main([Ljava/lang/String;)V\n"+
            ".limit stack " + 99 + " \n" +
            ".limit locals " + 99 + "\n";

    private static String endProg = "\n\nreturn\n\n.end method";

    public void compile(String[] args){
        String compileString = "";
        String name = "";

        if (args.length == 0){
            compileString = defaultCode;
            name = "DefaultCode";
        } else {
            File f = new File(args[0]);
            if (!f.exists()){
                System.out.println("File doesnt exist");
                return;
            }
            try {
                compileString = new String(Files.readAllBytes(Paths.get(args[0])));
            } catch (IOException ex){
                ex.printStackTrace();
                return;
            }
            name = f.getName();
            if (name.lastIndexOf(".") > -1){
                name = name.substring(0, name.lastIndexOf("."));
            }
        }

        CharStream charStream = CharStreams.fromString(compileString);
        LanguageLexer lexer = new LanguageLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        LanguageParser parser = new LanguageParser(tokens);
        ParseTree parseTree = parser.program();

        TypeChecker typeChecker = new TypeChecker();
        typeChecker.visit(parseTree);

        LangVisitor visitor = new LangVisitor(name);
        ArrayList<String> prog = visitor.visit(parseTree);

        // Output fixed part of the Jasmin file (except for the name)
        System.out.println(startProg.replaceAll("\\{\\{name\\}\\}",name));
        // Output compiled part of the jasmin file
        System.out.println(prog.stream().collect(Collectors.joining("\n")));
        // Output footer of jasmin file
        System.out.println(endProg);

    }

    public static void main(String[] args) {
        Language language = new Language();
        language.compile(args);
    }
}
