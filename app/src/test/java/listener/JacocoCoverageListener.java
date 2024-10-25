package listener;

import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.IMethodCoverage;
import org.jacoco.core.data.ExecutionData;
import org.jacoco.core.data.ExecutionDataReader;
import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.data.SessionInfoStore;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.lang.management.ManagementFactory;
import java.util.HashSet;
import java.util.Set;

public class JacocoCoverageListener extends TestWatcher {

    private static final String JACOCO_MBEAN_NAME = "org.jacoco:type=Runtime";

    @Override
    protected void succeeded(Description description) {
        printCoveredMethods(description);
    }

    @Override
    protected void failed(Throwable e, Description description) {
        printCoveredMethods(description);
    }

    private void printCoveredMethods(Description description) {
        try {
            // Connect to the platform MBean server
            MBeanServerConnection mbsc = ManagementFactory.getPlatformMBeanServer();
            ObjectName objectName = new ObjectName(JACOCO_MBEAN_NAME);

            // Invoke the dump command with no reset (you can set to true if you want to reset coverage after each dump)
            byte[] executionData = (byte[]) mbsc.invoke(objectName, "getExecutionData", new Object[]{true}, new String[]{"boolean"});

            // Use JaCoCo's ExecutionDataReader to parse the data
            ExecutionDataStore executionDataStore = new ExecutionDataStore();
            SessionInfoStore sessionInfoStore = new SessionInfoStore();
            ExecutionDataReader reader = new ExecutionDataReader(new ByteArrayInputStream(executionData));
            reader.setExecutionDataVisitor(executionDataStore);
            reader.setSessionInfoVisitor(sessionInfoStore);
            reader.read();

            System.out.println("Covered methods for test: " + description.getMethodName());

            // Analyze the covered classes to determine methods
            CoverageBuilder coverageBuilder = new CoverageBuilder();
            Analyzer analyzer = new Analyzer(executionDataStore, coverageBuilder);

            // Specify the directory where your compiled classes are located
            File classesDir = new File("build/classes/java/main"); // Adjust the path as needed

            // Analyze each class file to extract covered methods
            for (ExecutionData data : executionDataStore.getContents()) {
                if (data.hasHits()) {
                    String className = data.getName().replace("/", ".");
                    System.out.println("Class: " + className);

                    // Analyze the corresponding .class file
                    File classFile = new File(classesDir, data.getName() + ".class");
                    if (classFile.exists()) {
                        try (FileInputStream classStream = new FileInputStream(classFile)) {
                            analyzer.analyzeClass(classStream, data.getName());
                        }
                    }

                    // Print the covered method names
                    Set<String> coveredMethods = getCoveredMethods(coverageBuilder, className);
                    for (String method : coveredMethods) {
                        System.out.println("Method covered: " + method);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Set<String> getCoveredMethods(CoverageBuilder coverageBuilder, String className) {
        Set<String> coveredMethods = new HashSet<>();
        className = className.replace(".", "/");
        for (IClassCoverage classCoverage : coverageBuilder.getClasses()) {
            if (classCoverage.getName().equals(className)) {
                for (IMethodCoverage methodCoverage : classCoverage.getMethods()) {
                    if (methodCoverage.getInstructionCounter().getCoveredCount() > 0) {
                        String methodName = methodCoverage.getName(); // Get method name
                        /*String methodDescriptor = methodCoverage.getDesc(); // Get method descriptor

                        // Extract parameter types
                        String paramTypes = extractParameterTypes(methodDescriptor);
                        coveredMethods.add(methodName + "(" + paramTypes + ")"); // Add formatted method name to the set*/

                        coveredMethods.add(methodName);
                    }
                }
            }
        }
        return coveredMethods;
    }

    // Method to extract parameter types from the method descriptor
    private String extractParameterTypes(String descriptor) {
        StringBuilder paramTypes = new StringBuilder();

        // The descriptor starts with '(' and ends with ')'
        if (descriptor.startsWith("(") && descriptor.contains(")")) {
            // Extract the substring between '(' and ')'
            String params = descriptor.substring(descriptor.indexOf('(') + 1, descriptor.indexOf(')'));
            String[] paramArray = params.split(","); // Split by ',' to get individual parameter types

            for (String param : paramArray) {
                // Clean up the parameter type and add it to the StringBuilder
                param = param.replaceAll("^L", "").replaceAll(";$", ""); // Remove 'L' prefix and ';' suffix
                paramTypes.append(param).append(", ");
            }

            // Remove trailing comma and space if there are any parameters
            if (paramTypes.length() > 0) {
                paramTypes.setLength(paramTypes.length() - 2); // Remove last ", "
            }
        }

        return paramTypes.toString();
    }
}

