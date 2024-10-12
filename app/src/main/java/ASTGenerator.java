import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
            JavaParser javaParser = new JavaParser();

            for (String className : modifiedClasses) {
                // Costruisci il percorso del file .java corrispondente
                String filePathJava = className.replace('.', '/') + ".java";
                String currentFullPath = "./app/src/main/java/" + filePathJava;

                // Crea l'AST della versione attuale
                createASTFromFile(javaParser, currentFullPath, "Current", className);

                // Crea l'AST della versione al commit precedente
                String previousContent = getPreviousCommitContent(filePathJava);
                if (previousContent != null) {
                    createASTFromContent(javaParser, previousContent, "Previous", className);
                } else {
                    System.out.println("No previous version found for class: " + className);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Metodo per creare l'AST da un file attuale
    private static void createASTFromFile(JavaParser javaParser, String filePath, String version, String className) {
        File file = new File(filePath);
        if (file.exists()) {
            try {
                CompilationUnit cu = javaParser.parse(file).getResult().orElse(null);
                if (cu != null) {
                    System.out.println("AST for " + version + " version of class: " + className);
                    System.out.println(cu.toString());
                } else {
                    System.out.println("Could not parse the " + version + " version file: " + filePath);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(version + " version file not found: " + filePath);
        }
    }

    // Metodo per creare l'AST dal contenuto di un commit precedente
    private static void createASTFromContent(JavaParser javaParser, String content, String version, String className) {
        CompilationUnit cu = javaParser.parse(content).getResult().orElse(null);
        if (cu != null) {
            System.out.println("AST for " + version + " version of class: " + className);
            System.out.println(cu.toString());
        } else {
            System.out.println("Could not parse the " + version + " version content for class: " + className);
        }
    }

    // Metodo per ottenere il contenuto della versione precedente di un file da Git
    private static String getPreviousCommitContent(String filePathJava) {
        try {
            // Usa 'git show' per ottenere il contenuto della versione precedente del file
            Process process = new ProcessBuilder("git", "show", "HEAD^:" + "app/src/main/java/" + filePathJava).start();
            process.waitFor();
            if (process.exitValue() == 0) {
                return new String(process.getInputStream().readAllBytes());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
