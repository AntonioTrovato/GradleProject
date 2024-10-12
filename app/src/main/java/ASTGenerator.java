import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ASTGenerator {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java ASTGenerator <file_with_modified_classes>");
            return;
        }

        String filePath = args[0];

        try {
            List<String> modifiedClasses = Files.readAllLines(Paths.get(filePath));
            JavaParser javaParser = new JavaParser();

            for (String className : modifiedClasses) {
                String filePathJava = className.replace('.', '/') + ".java";
                String currentFullPath = "./app/src/main/java/" + filePathJava;

                // Crea l'AST della versione attuale
                List<MethodDeclaration> currentMethods = createASTFromFile(javaParser, currentFullPath, "Current", className);

                // Crea l'AST della versione al commit precedente
                String previousContent = getPreviousCommitContent(filePathJava);
                if (previousContent != null) {
                    List<MethodDeclaration> previousMethods = createASTFromContent(javaParser, previousContent, "Previous", className);
                    // Confronta i metodi
                    compareMethods(currentMethods, previousMethods);
                } else {
                    System.out.println("No previous version found for class: " + className);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Metodo per creare l'AST da un file attuale e restituire i metodi
    private static List<MethodDeclaration> createASTFromFile(JavaParser javaParser, String filePath, String version, String className) {
        File file = new File(filePath);
        List<MethodDeclaration> methods = new ArrayList<>();
        if (file.exists()) {
            try {
                CompilationUnit cu = javaParser.parse(file).getResult().orElse(null);
                if (cu != null) {
                    System.out.println("AST for " + version + " version of class: " + className);
                    System.out.println(cu.toString());
                    methods.addAll(cu.findAll(MethodDeclaration.class));
                } else {
                    System.out.println("Could not parse the " + version + " version file: " + filePath);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(version + " version file not found: " + filePath);
        }
        return methods;
    }

    // Metodo per creare l'AST dal contenuto di un commit precedente e restituire i metodi
    private static List<MethodDeclaration> createASTFromContent(JavaParser javaParser, String content, String version, String className) {
        List<MethodDeclaration> methods = new ArrayList<>();
        CompilationUnit cu = javaParser.parse(content).getResult().orElse(null);
        if (cu != null) {
            System.out.println("AST for " + version + " version of class: " + className);
            System.out.println(cu.toString());
            methods.addAll(cu.findAll(MethodDeclaration.class));
        } else {
            System.out.println("Could not parse the " + version + " version content for class: " + className);
        }
        return methods;
    }

    // Metodo per confrontare i metodi tra le due versioni
    private static void compareMethods(List<MethodDeclaration> currentMethods, List<MethodDeclaration> previousMethods) {
        Set<String> currentMethodSignatures = new HashSet<>();
        Set<String> previousMethodSignatures = new HashSet<>();

        // Aggiungi i metodi della versione attuale
        for (MethodDeclaration method : currentMethods) {
            currentMethodSignatures.add(getMethodSignature(method));
        }

        // Aggiungi i metodi della versione precedente
        for (MethodDeclaration method : previousMethods) {
            previousMethodSignatures.add(getMethodSignature(method));
        }

        // Trova metodi nuovi
        Set<String> newMethods = new HashSet<>(currentMethodSignatures);
        newMethods.removeAll(previousMethodSignatures);

        // Trova metodi modificati
        Set<String> modifiedMethods = new HashSet<>();
        for (MethodDeclaration method : currentMethods) {
            String signature = getMethodSignature(method);
            if (previousMethodSignatures.contains(signature)) {
                // Se il metodo esiste nella versione precedente, verifica se ci sono modifiche
                if (hasMethodChanged(method, previousMethods)) {
                    modifiedMethods.add(signature);
                }
            }
        }

        // Stampa i risultati
        System.out.println("New Methods: " + newMethods);
        System.out.println("Modified Methods: " + modifiedMethods);
    }

    // Metodo per ottenere la firma del metodo (nome e parametri)
    private static String getMethodSignature(MethodDeclaration method) {
        StringBuilder signature = new StringBuilder();
        signature.append(method.getNameAsString()).append("(");
        method.getParameters().forEach(param -> signature.append(param.getType().asString()).append(", "));
        if (method.getParameters().size() > 0) {
            signature.setLength(signature.length() - 2); // Rimuovi l'ultima virgola e spazio
        }
        signature.append(")");
        return signature.toString();
    }

    // Metodo per verificare se un metodo è stato modificato
    private static boolean hasMethodChanged(MethodDeclaration currentMethod, List<MethodDeclaration> previousMethods) {
        String currentSignature = getMethodSignature(currentMethod);
        for (MethodDeclaration previousMethod : previousMethods) {
            if (currentSignature.equals(getMethodSignature(previousMethod))) {
                // Confronta la logica del metodo, puoi aggiungere altre condizioni per il confronto
                return !currentMethod.getBody().equals(previousMethod.getBody());
            }
        }
        return false; // Se non troviamo corrispondenze, consideriamo non modificato
    }

    // Metodo per ottenere il contenuto della versione precedente di un file da Git
    private static String getPreviousCommitContent(String filePathJava) {
        try {
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
