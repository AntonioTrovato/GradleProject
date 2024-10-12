import java.util.List;

public class GenericClass {

    public GenericClass(String string1,String string2,int int1,int int2) {
        this.string1 = string1;
        this.string2 = string2;
        this.int1 = int1;
        this.int2 = int2;
    }

    public String a(int a, int c) {
        return "";
    }

    public String a(float a, int d) {
        return "";
    }

    private static <X> List<X> print(List<X> list) {
        return list;
    }

    public String ciao() {
        if (this.string1 == null) {
            this.string1 = "";
        }
        return "ciao3";
    }

    public String getString1(){
        return string1;
    }

    public int getInt1() {
        return int1;
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
        }while (this.int1 == this.int2) {
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

    private String string1;
    private String string2;
    private int int1;
    private int int2;

}
