import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            ArrayList<String> all_modified_methods = new ArrayList<>();

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
                    all_modified_methods.addAll(compareMethods(className,currentMethods, previousMethods));
                } else {
                    System.out.println("No previous version found for class: " + className);
                }
            }

            System.out.println("All the modified methods: ");
            System.out.println(all_modified_methods);

            // Scrivi i metodi modificati in un file
            String outputPath = "modified_methods.txt";
            try {
                // Sovrascrivi il file se esiste già
                Files.write(Paths.get(outputPath), all_modified_methods);
                System.out.println("Modified methods have been written to " + outputPath);
            } catch (IOException e) {
                e.printStackTrace();
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
    private static ArrayList<String> compareMethods(String className, List<MethodDeclaration> currentMethods, List<MethodDeclaration> previousMethods) {
        Set<String> currentMethodSignatures = new HashSet<>();
        Set<String> previousMethodSignatures = new HashSet<>();

        // Aggiungi i metodi della versione attuale
        for (MethodDeclaration method : currentMethods) {
            currentMethodSignatures.add(getMethodSignature(method));
        }

        System.out.println("Current method signatures: " + currentMethodSignatures);

        // Aggiungi i metodi della versione precedente
        for (MethodDeclaration method : previousMethods) {
            previousMethodSignatures.add(getMethodSignature(method));
        }

        System.out.println("Previous method signatures: " + previousMethodSignatures);

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

        ArrayList<String> modified_methods = new ArrayList<>();
        for (String new_method : newMethods) {
            String new_method_fully_qualified_name = className + "." + extractMethodNameAndParameters(new_method);
            if (!modified_methods.contains(new_method_fully_qualified_name)) {
                modified_methods.add(new_method_fully_qualified_name);
            }
        }
        for (String modified_method : modifiedMethods) {
            String modified_method_fully_qualified_name = className + "." + extractMethodNameAndParameters(modified_method);
            if (!modified_methods.contains(modified_method_fully_qualified_name)) {
                modified_methods.add(modified_method_fully_qualified_name);
            }
        }

        System.out.println("Modified Methods: " + modified_methods);
        return modified_methods;
    }

    // Metodo per ottenere la firma del metodo (nome e parametri)
    private static String getMethodSignature(MethodDeclaration method) {
        StringBuilder signature = new StringBuilder();

        // Aggiungi modificatori di accesso e tipo di ritorno
        method.getModifiers().forEach(modifier -> signature.append(modifier.getKeyword().asString()).append(" "));
        signature.append(getTypeAsString(method.getType())).append(" ");
        signature.append(method.getNameAsString()).append("(");

        method.getParameters().forEach(param -> signature.append(getTypeAsString(param.getType())).append(", "));
        if (method.getParameters().size() > 0) {
            signature.setLength(signature.length() - 2); // Rimuovi l'ultima virgola e spazio
        }
        signature.append(")");

        return signature.toString();
    }

    // Metodo per ottenere la rappresentazione del tipo (gestendo tipi generici)
    private static String getTypeAsString(com.github.javaparser.ast.type.Type type) {
        StringBuilder typeString = new StringBuilder();

        if (type.isClassOrInterfaceType()) {
            ClassOrInterfaceType classOrInterfaceType = type.asClassOrInterfaceType();
            typeString.append(classOrInterfaceType.getNameAsString());

            // Controlla se ci sono argomenti di tipo
            classOrInterfaceType.getTypeArguments().ifPresent(typeArgs -> {
                typeString.append("<");
                typeArgs.forEach(arg -> typeString.append(getTypeAsString(arg)).append(", "));
                if (typeArgs.size() > 0) {
                    typeString.setLength(typeString.length() - 2); // Rimuovi l'ultima virgola e spazio
                }
                typeString.append(">");
            });
        } else {
            typeString.append(type.asString());
        }

        return typeString.toString();
    }

    // Metodo per estrarre nome metodo e parametri
    private static String extractMethodNameAndParameters(String methodSignature) {
        // Definisci la regex
        String regex = "(\\w+)\\s*\\((.*?)\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(methodSignature);

        if (matcher.find()) {
            String methodName = matcher.group(1); // Nome del metodo
            String parameters = matcher.group(2); // Parametri del metodo
            return methodName + "(" + parameters + ")";
        }

        return null; // Restituisci null se non viene trovata alcuna corrispondenza
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
