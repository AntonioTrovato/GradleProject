package utente.personale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AmministratoreTest {

    @Test
    public void testGetName() {
        Amministratore amministratore = new Amministratore("John", "Doe", "HR");
        assertEquals("John", amministratore.getName(1));
    }

    @Test
    public void testGetDepartment() {
        Amministratore amministratore = new Amministratore("John", "Doe", "HR");
        assertEquals("HR", amministratore.getDepartment());
    }

    @Test
    public void testSetName() {
        Amministratore amministratore = new Amministratore("John", "Doe", "HR");
        amministratore.setName("Jane");
        assertEquals("Jane", amministratore.getName(1));
    }

    @Test
    public void testSetDepartment() {
        Amministratore amministratore = new Amministratore("John", "Doe", "HR");
        amministratore.setDepartment("IT");
        assertEquals("IT", amministratore.getDepartment());
    }

    @org.openjdk.jmh.annotations.State(org.openjdk.jmh.annotations.Scope.Thread)
    public static class _Benchmark extends se.chalmers.ju2jmh.api.JU2JmhBenchmark {

        @org.openjdk.jmh.annotations.Benchmark
        public void benchmark_testGetName() throws java.lang.Throwable {
            this.createImplementation();
            this.runBenchmark(this.implementation()::testGetName, this.description("testGetName"));
        }

        @org.openjdk.jmh.annotations.Benchmark
        public void benchmark_testGetDepartment() throws java.lang.Throwable {
            this.createImplementation();
            this.runBenchmark(this.implementation()::testGetDepartment, this.description("testGetDepartment"));
        }

        @org.openjdk.jmh.annotations.Benchmark
        public void benchmark_testSetName() throws java.lang.Throwable {
            this.createImplementation();
            this.runBenchmark(this.implementation()::testSetName, this.description("testSetName"));
        }

        @org.openjdk.jmh.annotations.Benchmark
        public void benchmark_testSetDepartment() throws java.lang.Throwable {
            this.createImplementation();
            this.runBenchmark(this.implementation()::testSetDepartment, this.description("testSetDepartment"));
        }

        private AmministratoreTest implementation;

        @java.lang.Override
        public void createImplementation() throws java.lang.Throwable {
            this.implementation = new AmministratoreTest();
        }

        @java.lang.Override
        public AmministratoreTest implementation() {
            return this.implementation;
        }
    }
}
