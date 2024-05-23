package utente.personale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TecnicoTest {

    @Test
    public void testGetName() {
        Tecnico tecnico = new Tecnico("John", "Doe", "Engineer");
        assertEquals("John", tecnico.getName());
    }

    @Test
    public void testGetSurname() {
        Tecnico tecnico = new Tecnico("John", "Doe", "Engineer");
        assertEquals("Doe", tecnico.getSurname());
    }

    @Test
    public void testGetProfession() {
        Tecnico tecnico = new Tecnico("John", "Doe", "Engineer");
        assertEquals("Engineer", tecnico.getProfession());
    }

    @Test
    public void testSetName() {
        Tecnico tecnico = new Tecnico("John", "Doe", "Engineer");
        tecnico.setName("Jane");
        assertEquals("Jane", tecnico.getName());
    }

    @Test
    public void testSetSurname() {
        Tecnico tecnico = new Tecnico("John", "Doe", "Engineer");
        tecnico.setSurname("Smith");
        assertEquals("Smith", tecnico.getSurname());
    }

    @Test
    public void testSetProfession() {
        Tecnico tecnico = new Tecnico("John", "Doe", "Engineer");
        tecnico.setProfessione("Technician");
        assertEquals("Technician", tecnico.getProfession());
    }

    @org.openjdk.jmh.annotations.State(org.openjdk.jmh.annotations.Scope.Thread)
    public static class _Benchmark extends se.chalmers.ju2jmh.api.JU2JmhBenchmark {

        @org.openjdk.jmh.annotations.Benchmark
        public void benchmark_testGetName() throws java.lang.Throwable {
            this.createImplementation();
            this.runBenchmark(this.implementation()::testGetName, this.description("testGetName"));
        }

        @org.openjdk.jmh.annotations.Benchmark
        public void benchmark_testGetSurname() throws java.lang.Throwable {
            this.createImplementation();
            this.runBenchmark(this.implementation()::testGetSurname, this.description("testGetSurname"));
        }

        @org.openjdk.jmh.annotations.Benchmark
        public void benchmark_testGetProfession() throws java.lang.Throwable {
            this.createImplementation();
            this.runBenchmark(this.implementation()::testGetProfession, this.description("testGetProfession"));
        }

        @org.openjdk.jmh.annotations.Benchmark
        public void benchmark_testSetName() throws java.lang.Throwable {
            this.createImplementation();
            this.runBenchmark(this.implementation()::testSetName, this.description("testSetName"));
        }

        @org.openjdk.jmh.annotations.Benchmark
        public void benchmark_testSetSurname() throws java.lang.Throwable {
            this.createImplementation();
            this.runBenchmark(this.implementation()::testSetSurname, this.description("testSetSurname"));
        }

        @org.openjdk.jmh.annotations.Benchmark
        public void benchmark_testSetProfession() throws java.lang.Throwable {
            this.createImplementation();
            this.runBenchmark(this.implementation()::testSetProfession, this.description("testSetProfession"));
        }

        private TecnicoTest implementation;

        @java.lang.Override
        public void createImplementation() throws java.lang.Throwable {
            this.implementation = new TecnicoTest();
        }

        @java.lang.Override
        public TecnicoTest implementation() {
            return this.implementation;
        }
    }
}
