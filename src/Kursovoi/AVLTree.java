package Kursovoi;

import java.util.Stack;

public class AVLTree {
    private Node root = null; // жеке жолдық null логикалық кілт ретінде қызмет етеді

    public void insert(int data) {// санды кірістіру
        insert(root, data); // сол атаудағы кірістіру әдісін шақырады
    }

    private int height(Node node) {
        return node == null ? -1 : node.height; // биіктігін есептейміз
    }

    private void reHeight(Node node) {
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        // биіктікті қайта есептеу
        // бұл әдіс кейбір ұяшықтарды жойғаннан кейін немесе
        // аударылғаннан кейін қажет болады
    }

    private void insert(Node node, int value) {
        // ағашқа кірістіру
        if (root == null) {
            root = new Node(value, null);
            // мұнда жіберілген әр санды ағашқа саламыз
            return;
            // жабу
        }

        if (value < node.getValue()) { // Егер қосылған сан басқасынан аз болса
            if (node.left != null) { // егер сол жақ ішкі ағаш бос болмаса
                insert(node.left, value); // онда оның орнына қоямыз (рекурсия)
            } else {// или оно пустое
                node.left = new Node(value, node); // содан кейін бос орынға саламыз
            }

            if (height(node.left) - height(node.right) == 2) { // сол жағы ұзағырақ
                if (value < node.left.getValue()) { // егер сан сол ұрпақтан аз болса
                    rotateRight(node); // оңға бұру
                } else {
                    rotateLeftThenRight(node); // немесе солға, содан кейін оңға бұрамыз, яғни
                    // біріншісі қалады, екіншісі сол жақта, ал үшіншісі оң жақта болады
                }
            }
        } else if (value > node.getValue()) { // егер мән басқасынан үлкен болса, яғни алдыңғысы
            if (node.right != null) { // онда санның бар-жоғын есептейміз, яғни босқа тең емес пе екенін
                insert(node.right, value); // оның орнына жаңасын саламыз
            } else {
                node.right = new Node(value, node);// немесе ата-бабасы бар ұяшық ретінде орнатамыз
            }

            if (height(node.right) - height(node.left) == 2) { // оң жағы ұзағырақ
                if (value > node.right.getValue()) // егер сан оң жақ ішкі ағаштың мәнінен үлкен болса
                    rotateLeft(node); // солға бұру
                else {
                    rotateRightThenLeft(node); // немесе оңға, содан кейін солға бұрамыз, біріншісі қалады,
                    // екіншісі оң ұрпақ, ал үшіншісі сол жақ ұрпақ болады
                }
            }
        }

        reHeight(node); // түйіннің биіктігін қайта анықтау
    }

    private void rotateRight(Node turn) { // оңға бұру әдісі
        Node parent = turn.parent;
        Node leftChild = turn.left;
        Node rightChildOfLeftChild = leftChild.right;
        turn.setLeftChild(rightChildOfLeftChild);
        leftChild.setRightChild(turn);
        if (parent == null) {
            this.root = leftChild;
            leftChild.parent = null;
            return;
        }

        if (parent.left == turn) {
            parent.setLeftChild(leftChild);
        } else {
            parent.setRightChild(leftChild);
        }

        reHeight(turn);
        reHeight(leftChild);
    }

    private void rotateLeft(Node turn) { // солға бұрылудың дәл сондай әдісі
        Node parent = turn.parent;
        Node rightChild = turn.right;
        Node leftChildOfRightChild = rightChild.left;
        turn.setRightChild(leftChildOfRightChild);
        rightChild.setLeftChild(turn);
        if (parent == null) {
            this.root = rightChild;
            rightChild.parent = null;
            return;
        }

        if (parent.left == turn) {
            parent.setLeftChild(rightChild);
        } else {
            parent.setRightChild(rightChild);
        }

        reHeight(turn);
        reHeight(rightChild);
    }

    private void rotateLeftThenRight(Node node) { // бұл оңға, содан кейін солға бұрылу үшін
        rotateLeft(node.left);
        rotateRight(node);
    }

    private void rotateRightThenLeft(Node node) { // бұл тура сондай әдіс, бірақ керісінше
        rotateRight(node.right);
        rotateLeft(node);
    }

    public boolean delete(int key) { // ұяшықты жою әдісі
        Node target = search(key);
        if (target == null) {
            // printTree();
            return false; // егер таргет болмаса, онда фолз қайтарамыз
        }
        target = deleteNode(target); // егер сан бар болса, онда бір нәрсе жояды
        balanceTree(target.parent); // ағаштың тепе-теңдігін тексереміз
        printTree();
        return true;

    }

    private Node deleteNode(Node target) {// удаление какого нибудь объекта
        if (IsDiversion(target)) { // есть ли отвлетвление у удаляемого объекта отвлетвление
            if (isLeftChild(target)) {
                target.parent.left = null;
            } else {
                target.parent.right = null;
            }
        } else if (target.left == null ^ target.right == null) { // если один потомок
            Node nonNullChild = target.left == null ? target.right : target.left;
            if (isLeftChild(target)) {
                target.parent.setLeftChild(nonNullChild);
            } else {
                target.parent.setRightChild(nonNullChild);
            }
        } else {// если 2 потомка
            Node immediatePuttingInOrder = immediatePuttingInOrder(target);
            target.setValue(immediatePuttingInOrder.getValue());
            target = deleteNode(immediatePuttingInOrder);
        }

        reHeight(target.parent);
        return target;
    }

    private Node immediatePuttingInOrder(Node node) {// приведение в порядок
        Node current = node.left;
        while (current.right != null) {
            current = current.right;
        }

        return current;
    }

    private boolean isLeftChild(Node child) {// задать как левый потомок
        return (child.parent.left == child);
    }

    private boolean IsDiversion(Node node) {// проверка наличии листвы т,е проверка чтобы проверить есть ли отвлетвление
        return node.left == null && node.right == null;
    }

    private int calculateDifference(Node node) {// метод вычитания разницы
        int rightHeight = height(node.right);
        int leftHeight = height(node.left);
        return rightHeight - leftHeight;
    }

    private void balanceTree(Node node) {// балансировка дерева
        int difference = calculateDifference(node);// задаем разницу
        Node parent = node.parent;
        if (difference == -2) {// если разница высоты равна -2
            if (height(node.left.left) >= height(node.left.right)) {
                rotateRight(node);// вращаем вправо
            } else {
                rotateLeftThenRight(node);// все правильно так что делаем поворот влево и вправо чтобы вернулось на
                // круги своя

            }
        } else if (difference == 2) {// если разница высоты равна 2
            if (height(node.right.right) >= height(node.right.left)) {
                rotateLeft(node);// вращаем влево
            } else {
                rotateRightThenLeft(node);// все правильно так что делаем поворот вправо и влево чтобы вернулось на
                // круги своя

            }
        }

        if (parent != null) {// если нет родителя то может быть что балансировка дерева пошатнется
            balanceTree(parent);// поэтому ссылаем его к балансировщику дерева
        }

        reHeight(node);// перевысить узел
    }

    public Node search(int key) {// поиск т.е метод чтобы направить на двоичный поиск
        return binarySearch(root, key);
    }

    private Node binarySearch(Node node, int key) {
        // метод бинарный поиск чтобы найти ->
        // -> конкретные числа чтобы построить дерево и чтобы в дальнейшем обозначить с
        // правой ли стороны будет число или с левой
        if (node == null)
            return null;

        if (key == node.getValue()) {
            return node;
        }

        if (key < node.getValue() && node.left != null) {
            return binarySearch(node.left, key);
        }

        if (key > node.getValue() && node.right != null) {
            return binarySearch(node.right, key);
        }
        return null;
    }

    public void traverseInOrder() {// вывести на экран корень дерева(ағаштың ұшы) и начать поочередное печатывание
        System.out.println("THE ROOT OF THE TREE: \n");
        printTree();
        // InOrder(root);
        System.out.println();
    }

    public void printTree() { // ағашты консольге шығару әдісі
        Stack globalStack = new Stack(); // ағаш мәндеріне арналған жалпы стек
        globalStack.push(root);
        int gaps = 32; // элементтер арасындағы қашықтықтың бастапқы мәні
        boolean isRowEmpty = false;
        String separator = "-----------------------------------------------------------------";
        System.out.println(separator);// жаңа ағаштың басталуын көрсететін сызық
        while (isRowEmpty == false) {
            Stack localStack = new Stack(); // элемент ұрпақтарын анықтауға арналған жергілікті(локальдық) стек
            isRowEmpty = true;
            for (int j = 0; j < gaps; j++)
                System.out.print(' ');
            while (globalStack.isEmpty() == false) { // жалпы жинақта элементтер бар
                Node temp = (Node) globalStack.pop(); // келесі біреуін алып, оны стектен алып тастаймыз
                if (temp != null) {
                    System.out.print(temp.getValue()); // оның мәнін консольде көрсетеміз
                    localStack.push(temp.left); // ағымдағы элементтің мұрагерлерін жергілікті(локальді) стекке сақтаймыз
                    localStack.push(temp.right);
                    if (temp.left != null || temp.right != null)
                        isRowEmpty = false;
                } else {
                    System.out.print("__");// - егер элемент бос болса
                    localStack.push(null);
                    localStack.push(null);
                }
                for (int j = 0; j < gaps * 2 - 2; j++)
                    System.out.print(' ');
            }
            System.out.println();
            gaps /= 2;// келесі деңгейге өткен сайын элементтер арасындағы
            // қашықтық азаяды
            while (localStack.isEmpty() == false)
                globalStack.push(localStack.pop()); // барлық элементтерді жергілікті стектен глобалға жылжытамыз
        }
        System.out.println(separator);// сызық сызамыз
    }
    // күрделілік массиві :О(1)
    // АВЛ ағашының күрделілігі: О(log(n))
    // Бинарлық іздеудің күрделілігі: О(log(n))
}
