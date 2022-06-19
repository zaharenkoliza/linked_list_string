public class ListString {
    private static class StringItem {
        private final static int SIZE = 16;
        private char[] symbols;
        private byte size;
        private StringItem next;
        private StringItem (char [] symbols, byte size, StringItem next) {
            this.symbols = symbols;
            this.size = size;
            this.next = next;
        }
        private StringItem(StringItem  a){
            symbols = new char [16];
            for (int i=0; i<a.size; i++){
                this.symbols[i]=a.symbols[i];
            }
            this.size=(byte) a.size;
            this.next = null;
        }
    }
    //сабстринг, копиаррей
    private void copyarray(char[] s1, char[] s2, int startin, int startfrom, int count){ //метод копирует каунт элементов массива 2 со старта 2 в первый массив
        // s1 - массив, в который копируем, s2 - массив, из которого копируем, startin - индекс элемента в массиве s1, с которого мы начинаем копировать, count - количество копируемых элементов
        for (int i = startin; i < startin+count; i++){
            s1[startin]=s2[startfrom];
            startfrom++;
        }
    }
    private StringItem head;
    public ListString () {
        head = null;
    }
    public ListString (ListString a){ //копирующий конструктор
        this.head = new StringItem(a.head);
        StringItem h = a.head.next;
        StringItem h1 = head;
        while (h!=null){
            h1.next=new StringItem(h);
            h1=h1.next;
            h=h.next;
        }
    }
    public ListString(String string) { //конструктор для создания листстринг из стринг
        int j=0;
        if (string.length()>=16){ //голова для строки, которая займёт больше одного блока
            head = new StringItem(string.substring(j, j+16).toCharArray(), (byte) 16, null);
            j+=16;
        }
        else{ //голова для строки, которая займёт один блок
            char [] temp = new char [16];
            char [] again = string.toCharArray();
            copyarray(temp, string.toCharArray(), 0, 0, again.length);
            head = new StringItem(temp, (byte) again.length, null);
        }
        StringItem h = head;
        int m = string.length()/16-1;
        //добавляем полные блоки
        for (int i = 0; i<m; i++){
            h.next = new StringItem(string.substring(j, j+16).toCharArray(), (byte) 16, null);
            h=h.next;
            j+=16;
        }
        if (string.length()%16!=0 & string.length()>16){
            char [] temp = new char [16]; //добавляем последний блок
            char[] again = string.substring(j).toCharArray();
            copyarray(temp, again, 0, 0, again.length);
            h.next = new StringItem(temp, (byte) again.length, null);
        }
    }
    public StringItem last(StringItem a){ //возвращает последний блок строки, которая начинается с a
        StringItem h = a, p = null;
        while (h!=null){
            p = h;
            h=h.next;
        }
        return p;
    }
    private static class Pair{ //внутренний класс для того чтобы вернуть два значения
        StringItem block;
        int index;
        private Pair(StringItem a, int in){
            block=a;
            index=in;
        }
    }
    private Pair index(int index) { //метод ищет местонахождение по индексу
        StringItem h;
        if (index<16){
            h = null;
        }
        else{
            h = head;
            int counter = index/16-1;
            for (int i=0; i<counter; i++){
                h = h.next;
                if (h==null){
                    return new Pair(h, -1);
                }
            }
        }
        return new Pair(h, index%16-1);
    }
    public int length(){
        StringItem h = head;
        int l = 0;
        while (h.next!=null){
            int size1=h.size;
            int size2=h.next.size;
            if (size1+size2<=16){ //проверяем можем ли объединить блоки
                copyarray(h.symbols, h.next.symbols, h.size, 0, size2);
                h.size+=size2;
                h.next.size=0;
                h.next=h.next.next; //выкидываем пустой блок
                if (h.next==null){
                    break;
                }
            }
            l+=h.size; //суммируем заполненность каждого блока
            h=h.next;
        }
        l+=h.size; //не забываем прибавить последний блок
        return l;
    }
    public char charAt(int index) throws IndexException {
        StringItem temp = index(index).block; //находим необходимый блок
        int in = index(index).index;
        if (temp == null & in <= -1){
            throw new IndexException("Ошибка!");
        }
        else if (temp == null){
            return head.symbols[in];
        }
        else {
            return temp.next.symbols[in]; // находим местонахождение символа в массиве блока
        }
    }
    public void setcharAt (int index, char ch){
        StringItem temp = index(index).block; //находим необходимый блок
        int in = index(index).index; // находим местонахождение символа в массиве блока
        if (temp == null & in <= -1){
            throw new IndexException("Ошибка!");
        }
        else if (temp == null){
            head.symbols[in] = ch;
        }
        else {
            temp.next.symbols[in] = ch;
        }
    }
    public ListString substring(int start, int end) throws IndexException{
        StringItem temp = index(start).block; //находим необходимый блок
        int in = index(start).index;
        if (temp == null & in <= -1){
            throw new IndexException("Ошибка!");
        }
        else if (temp == null){
            temp = head;
        }
        else{
            temp = temp.next;
        }
        ListString answer = new ListString();
        char [] arr = new char [16];
        copyarray(arr, temp.symbols, 0, in, temp.size-in);
        head = new StringItem(arr, (byte) (temp.size-in), null);
        StringItem templast = index(end).block; //находим необходимый блок
        int inlast = index(end).index;
        if (templast != null & in > -1){
            templast = templast.next;
            char [] arrlast = new char [16];
            copyarray(arrlast, templast.symbols, 0, inlast, templast.size-inlast);
            head.next = new StringItem(arrlast, (byte) (templast.size-in), null);
        }
    }
    void append(char ch){
        StringItem h=last(head);
        if (h.size<16){ //вставляем в конец неполного блока
            char [] temp = new char [1];
            temp[0]=ch;
            copyarray(h.symbols, temp, h.size, 0, 1);
            h.size++;
        }
        else{ //создаём новый блок
            char [] temp = new char [16];
            temp[0]=ch;
            h.next = new StringItem(temp, (byte) 1, null);
        }
    }
    public void append(ListString string){
        StringItem hcopystring = new ListString(string).head; //создаём копию
    }
    public void append(String string){
        ListString news = new ListString(string); //преобразуем строку в строку в виде связного списка
        StringItem h=last(head); //нахожим последний блок
        h.next = news.head; //соединяем два листстринга
    }
    private void inserthelper(int index, ListString string){
        StringItem hcopystring = string.head;
        StringItem l = last(hcopystring); //находим последний блок
        StringItem temp = index(index).block;
        int in = index(index).index;
        if (temp == null & in <= -1){
            throw new IndexException("Ошибка!");
        }
        if (index%16==1) { //вставляем без разделения блоков
            StringItem h = temp.next;
            StringItem previoush = temp;
            l.next = h;
            if (temp == null){
                head = hcopystring;
            }
            else{
                previoush.next = hcopystring;
            }
        }
        else {//разделим блок по индексу
            if (temp == null){
                temp = head;
            }
            else{
                temp=temp.next;
            }
            StringItem ns = new StringItem(new char [16], (byte) 0, temp.next); //новый пустой блок
            temp.next = hcopystring; //вставляем новый блок в наш листстринг
            l.next=ns;
            copyarray(ns.symbols, temp.symbols, 0, index%16-1, 17-index%16); //разделяем блоки
            ns.size = (byte) (17-index%16);
            temp.size -= (byte) (17-index%16);
            copyarray(temp.symbols, hcopystring.symbols, temp.size, 0, 16-temp.size); //осуществляем копирование
            if (hcopystring.size < 16-temp.size){
                temp.size+=hcopystring.size;
                hcopystring.size=0;
            }
            else{
                hcopystring.size = (byte) (16-temp.size);
                temp.size=16;
            }
        }
    }
    public void insert(int index, ListString string){
        ListString hcopystring = new ListString(string); //создаём копию
        inserthelper(index, hcopystring);
    }
    public void insert(int index, String string){
        ListString news = new ListString(string); //преобразуем строку в строку в виде связного списка
        inserthelper(index, news);
    }
    public String toString(){
        StringItem h = head;
        char[] r = new char [length()]; //новый массив длиной нашей строки
        int i = 0;
        while (h!=null){
            copyarray(r, h.symbols, i, 0, h.size); //копируем не пустые ячейки в новый массив
            i+=h.size;
            h=h.next;
        }
        return new String(r);
    }
}
