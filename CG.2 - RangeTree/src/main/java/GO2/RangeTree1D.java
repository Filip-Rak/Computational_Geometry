package GO2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class RangeTree1D
{
    //Attributes
    private TreeNode<Double> head;
    //int i = 0;

    //Constructor / creates the tree structure
    RangeTree1D(ArrayList<Double> inp)
    {
        //Copy and sort the array
        LinkedList<Double> dataSet = new LinkedList<>(inp);
        Collections.sort(dataSet);

        LinkedList<List<Double>> pending = new LinkedList<>();
        pending.add(dataSet);

       while (!pending.isEmpty())
       {
           List<Double> current = pending.poll();

           if(current.size() > 1)
           {
               //System.out.println("Current\n" + current);

               //split the list
               int splitPoint = splitPoint(current);
               List<Double> left = current.subList(0, splitPoint);
               List<Double> right = current.subList(splitPoint, current.size());

               //save results
               if (!left.isEmpty()) pending.add(left);
               if (!right.isEmpty()) pending.add(right);

               //add median to the tree
               insert(current.get(splitPoint - 1));

               //System.out.println("Selected: " + current.get(splitPoint - 1));
           }
           else if (current.size() == 1)
               insert(current.getFirst());
       }
    }

    public void insert(Double in) { head = insertInto(in, head); }

    private TreeNode<Double> insertInto(Double in, TreeNode<Double> node)
    {
        if(node == null)
            return new TreeNode<>(in, null, null, null);
        else
        {
            if(in <= node.getValue())
                node.setLeft(insertInto(in, node.getLeft()));
            else
                node.setRight(insertInto(in, node.getRight()));

            return node;
        }
    }

    private int splitPoint(List<Double> input)
    {
        //median with bias towards right
        double middle = input.size() / 2.0;
        middle = Math.ceil(middle);

        return (int)middle;
    }

    public static void print(TreeNode<Double> head)
    {
        if(head == null)
            return;

        System.out.println(head.getValue());

        print(head.getLeft());
        print(head.getRight());
    }

    public LinkedList<TreeNode<Double>> rangeSearch(double min, double max)
    {
        LinkedList<TreeNode<Double>> result = new LinkedList<>();
        rangeSearchRec(this.head, min, max, result);

        //check if boundries are ok
        if(!result.isEmpty())
        {
            if(!inRange(result.getFirst().getValue(), min, max ))
                result.removeFirst();

            if(result.size() > 1 && !inRange(result.getLast().getValue(), min, max ))
                result.removeLast();
        }

        return result;
    }

    private void rangeSearchRec(TreeNode<Double> node, double min, double max, LinkedList<TreeNode<Double>> results)
    {
        if (node == null)
            return;

        double value = node.getValue();

        //System.out.println(++i);

        //possibly in range values on the left
        if (value >= min)
            rangeSearchRec(node.getLeft(), min, max, results);

        //no left child - a leaf
        if (node.getLeft() == null)
            results.add(node);

        //value is lower than max, right can be too
        if (value < max)
            rangeSearchRec(node.getRight(), min, max, results);
    }

    private boolean inRange(double val, double min, double max)
    {
        return val <= max && val >= min;
    }

    //Getters
    TreeNode<Double> getHead() { return this.head; }
}

// Zlozonosc Obliczeniowa

// Najlepszy przypadek: O(log n + k), gdzie n to liczba wezłow, a k to liczba wynikow.
// Wynik mozliwy tylko dla, drzewa przypadkowo zbalansowanego i ograniczonego zakresu zapytania.

// Najgorszy przypade: O(n), gdy drzewo staje sie lista
// lub zakres zapytania obejmuje większość lub wszystkie wartości w drzewie.