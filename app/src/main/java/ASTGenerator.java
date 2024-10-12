import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ASTGenerator {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java ASTGenerator <file_with_modified_classes>");
            return;
        }

        String filePath = args[0];

        try {
            // Leggi il file e ottieni la lista di classi modificate
            List<String> modifiedClasses = Files.readAllLines(Paths.get(filePath));
            JavaParser javaParser = new JavaParser(); // Crea un'istanza di JavaParser

            for (String className : modifiedClasses) {
                // Costruisci il percorso del file .java corrispondente
                String filePathJava = className.replace('.', '/') + ".java";

                // Supponendo che i file .java siano nel modulo app in main/java/
                String fullPath = "./app/src/main/java/" + filePathJava;

                // Crea l'AST
                File file = new File(fullPath);
                if (file.exists()) {
                    CompilationUnit cu = javaParser.parse(file).getResult().orElse(null); // Usa l'istanza per chiamare parse
                    if (cu != null) {
                        // Fai qualcosa con l'AST (ad esempio, stampalo)
                        System.out.println("AST for class: " + className);
                        System.out.println(cu.toString());
                    } else {
                        System.out.println("Could not parse the file: " + fullPath);
                    }
                } else {
                    System.out.println("File not found: " + fullPath);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
