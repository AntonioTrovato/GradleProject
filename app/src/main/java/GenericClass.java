public class GenericClass {

    public GenericClass(String string1,String string2,int int1,int int2) {
        this.string1 = string1;
        this.string2 = string2;
        this.int1 = int1;
        this.int2 = int2;
    }

    public String getString1()
    {
        return string1;
    }

    public String getString2() {

        return string2;
    }

    private final int getInt1(int x) {
        return int1;
    }

    public int getInt2() {
        if (this.int1>this.int2) {
            this.int2 = this.int1;
        }
        return int2;
    }

    public void setString1(String string1) {
        this.string1 = string1;
    }

    public void setString2(String string2) {
        this.string2 = string2;
    }

    public void setInt1(int int1) {
        this.int1 = int1;
    }

    public void setInt2(int int2) {
        this.int2 = int2;
    }

    private int sum() {
        if (this.int1 > this.int2) {
            return this.int1 - this.int2;
        }while (this.int1 > this.int2) {
            this.int1--;
        }

        while (this.int1 != 0) { if (this.int2 != 0) { this.int1--; }
            System.out.println(this.string1);

            if (this.string1.equals(this.string2)) {
                System.out.println(this.string2);

            }
        }

        return 0;
    }

    public static void performOperations() {
        int a = 10;
        int b = 5;

        // Somma
        int sum = a + b;
        System.out.println("Somma: " + sum);

        // Sottrazione
        int diff = a - b;
        System.out.println("Sottrazione: " + diff);

        // Moltiplicazione
        int product = a * b;
        System.out.println("Moltiplicazione: " + product);

        // Divisione
        if (b != 0) {
            int quotient = a / b;
            System.out.println("Divisione: " + quotient);
        } else {
            System.out.println("Impossibile dividere per zero");
        }

        // Modulo
        int remainder = a % b;
        System.out.println("Resto: " + remainder);

        // Incremento
        a++;
        System.out.println("Incremento di a: " + a);

        // Decremento
        b--;
        System.out.println("Decremento di b: " + b);

        // Operazioni con float
        float x = 2.5f;
        float y = 1.5f;

        // Somma
        float floatSum = x + y;
        System.out.println("Somma float: " + floatSum);

        // Sottrazione
        float floatDiff = x - y;
        System.out.println("Sottrazione float: " + floatDiff);

        // Verifica booleani
        boolean isGreater = a > b;
        System.out.println("a è maggiore di b: " + isGreater);

        System.out.println("hey");

        boolean isEqual = a == b;
        System.out.println("a è uguale a b: " + isEqual);
    }

    private String string1;
    private String string2;
    private int int1;
    private int int2;

}
