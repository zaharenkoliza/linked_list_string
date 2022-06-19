public class Main {
    public static void main (String [] args){
        //ListString a = new ListString("yepyepyepyepyepydbjhjndfdfvdfdffghgd");
        ListString b = new ListString("Iloveyouforeeverandyouneverleavemeadscsdc");
        System.out.println(b.substring(1, 4));
        try{
            b.insert(-2, new ListString("arrayerge"));
        }
        catch (IndexException e){
            System.out.println(e.getMessage());
        }
        System.out.println(b);
        //System.out.println(b.length());
    }
}
