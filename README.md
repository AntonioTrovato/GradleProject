Using: java 17.0.10 e gradle 7.4.2

Build the ju2jmh tool (In the Tool Root):
gradle build

New Gradle Project using Kotling language
Make 2 subproject:
GradleProject
|--app (build.gradle.kts must have the right dependencies for junit 4.13.2)
|--src/main/java/(containing packages of classes)
|--src/test/java/(containing packages of test classes)
|--ju2jmh
|--src/jmh/java/(containing packages of benchmarks)
|--src/main/java/
|--src/test/java/

the ju2jmh/src/jmh/java and ju2jmh/src/jmh/resources folders must be created by hand, then run the build.gradle.kts (using id("me.champeau.jmh") version "0.6.6" as plugin and the needed dependencies)
packages must have the same name and be organized the same way for each module

build GradleProject (In the Target Project Root):
gradle build

remove existent benchmarks (In the Target Project Root):
rm -r ju2jmh/src/jmh/java/*

re-build GradleProject (In the Target Project Root):
gradle clean
gradle build

run generation
gradle converter:run --args="C:/Users/anton/Documents/IntellijProjects/GradleProject/app/src/test/java/ C:/Users/anton/Documents/IntellijProjects/GradleProject/app/build/classes/java/test/ C:/Users/anton/Documents/IntellijProjects/GradleProject/ju2jmh/src/jmh/java/ banca.ContoBancario"

build benchmarks (In the Target Project Root):
gradle jmhJar

list avaible benchmarks:
java -jar C:/Users/anton/Documents/IntellijProjects/GradleProject/ju2jmh/build/libs/ju2jmh-jmh.jar -l

run benchmarks (first way):
use the jmh task in the gradle menu under ju2jmh opening GradleProject with IntelliJ

run the benchmarks (second way):
java -jar C:/Users/anton/Documents/IntellijProjects/GradleProject/ju2jmh/build/libs/ju2jmh-jmh.jar -f 1 -wi 0 -i 1 -r 100ms -foe true