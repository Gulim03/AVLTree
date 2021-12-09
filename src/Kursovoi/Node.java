package Kursovoi;

public class Node implements Nodeable { // түйін класын жасаймыз
    Node right; // ұрпақтардың жолы
    Node left;
    Node parent; // ата-ана жолы
    private int value; // берілген сан бойынша, біз AVL ағашын жасаймыз
    int height = 0; // Ағаштың биіктігі

    public Node(int data, Node parent) { // түйін құрастырушысы
        this.value = data; // САНДЫ АТА-АНАНЫҢ А САНЫНЫҢ АТА-АНАСЫНА ТЕҢЕСТІРЕМІЗ
        this.parent = parent;
    }

    public void setRightChild(Node child) {
        // оң ұрпағын орнатамыз
        if (child != null) { // егер ұрпақ null тең болмаса, яғни ағаштың астында болса
            child.parent = this; // қиылыстың басталуы осы ішкі ағаштың ата-анасы болады
        }
        this.right = child; // оң ұрпағын орнатамыз
    }

    public void setLeftChild(Node child) {
        // сол жақ ұрпақты немесе сол жақ ішкі бұтақты орнатамыз
        if (child != null) { // егер ұрпақ null тең болмаса, яғни ағаштың астында болса
            child.parent = this; // қиылыстың басталуы осы ішкі ағаштың ата-анасы болады
        }
        this.left = child; // біз сол ұрпақты орнатамыз
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}