package Kursovoi;

import java.util.Scanner;

import java.util.InputMismatchException;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        AVLTree AVLTree = new AVLTree(); // жаңа AVL ағашын жасаймыз
        System.out.println("The total number of the elements of the AVL tree:");
        int t = scan.nextInt(); // ағаш элементтерінің жалпы санын орнатамыз
        int[] arr;
        arr = new int[t];
        System.out.println("Enter values for branches of the AVL tree:");

        try {// try catch
            for (int i = 0; i < arr.length; i++) {
                arr[i] = scan.nextInt();
            }
        } catch (NumberFormatException e) { // егер ұяшық пішімі яғни int сақталмаса
            System.out.println("Detected Numberformatexception...");
        } catch (InputMismatchException r) { // егер int түрі сақталмаса (адам сандардың орнына жолды енгізсе)
            System.out.println("Detected InputMismatchException...");
        } catch (NegativeArraySizeException a) { // егер int түрі сақталмаса (адам сандардың орнына жолды енгізсе)
            System.out.println("NegativeArraySizeException...");
        }


        for (int i = 0; i < arr.length; i++) { // біздің АВЛ ағашын толтырамыз
            AVLTree.insert(arr[i]);
        }

        AVLTree.traverseInOrder(); // ұяшықты жоймас бұрын, егер ол керек болса

        System.out.print("Select the number you want to delete : ");
        int del = scan.nextInt();
        System.out.println("Tree after deleting a number: ");
        int k = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == del)
                k++;
        }
        if (k != 0) {
            AVLTree.delete(del);// біз 6 санын немесе кез келген басқа санды жек көреміз делік
            } else {
            System.out.println("In Tree not this number !!!\n");
            AVLTree.printTree();
        }
    }
}
