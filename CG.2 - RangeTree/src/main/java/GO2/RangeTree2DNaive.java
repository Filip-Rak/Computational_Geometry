package GO2;

import scala.collection.immutable.RedBlackTree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RangeTree2DNaive
{
    //Attributes
    private TreeNode<Double> head = null;

    //Constructor
    RangeTree2DNaive(ArrayList<Point2D> input)
    {
        initMain(input);
        initAssociate(head);
    }

    private void initAssociate(TreeNode<Double> local_head)
    {
        //ignore leaves
        if(local_head == null)
            return;

        //clear the temporary buffer
        if(local_head.getLeft() == null && local_head.getRight() == null)
        {
            local_head.setAssociate(null);
            return;
        }

        //find all the Ys, buffered in associate
        ArrayList<Double> Y = new ArrayList<>();
        fillYs(local_head, Y);

        //maka a 1D tree out of Ys
        RangeTree1D t = new RangeTree1D(Y);

        //assign head of the new tree to associate
        local_head.setAssociate(t.getHead());

        //repeat recursively
        initAssociate(local_head.getRight());
        initAssociate(local_head.getLeft());
    }

    private void fillYs(TreeNode<Double> local_head, ArrayList<Double> arr)
    {
        if(local_head == null)
            return;

        //only get vals from leaves
        if(local_head.getRight() == null && local_head.getLeft() == null)
            arr.add(local_head.getAssociate().getValue());

        fillYs(local_head.getLeft(), arr);
        fillYs(local_head.getRight(), arr);
    }

    private void initMain(ArrayList<Point2D> input)
    {
        //copy input and sort
        Point2D.sortByX(input);
        LinkedList<Point2D> x_arr = new LinkedList<>(input);

        LinkedList<List<Point2D>> pending = new LinkedList<>();
        pending.add(x_arr);

        while (!pending.isEmpty())
        {
            List<Point2D> current = pending.poll();

            if(current.size() > 1)
            {
                //split the list
                int splitPoint = splitPoint(current);
                List<Point2D> left = current.subList(0, splitPoint);
                List<Point2D> right = current.subList(splitPoint, current.size());

                //save results
                if (!left.isEmpty()) pending.add(left);
                if (!right.isEmpty()) pending.add(right);

                //add median to the tree
                insert(current.get(splitPoint - 1));
            }
            else if (current.size() == 1)
                insert(current.getFirst());
        }
    }

    private void insert(Point2D in)
    {
        head = insertInto(in, head);
    }

    private TreeNode<Double> insertInto(Point2D in, TreeNode<Double> node)
    {
        if(node == null)
        {
            //TreeNode.associate will serve as a temporary buffer
            TreeNode<Double> y = new TreeNode<>(in.getY(), null, null);
            return new TreeNode<>(in.getX(), null, null, y, in.getY());
        }
        else
        {
            if(in.getX() <= node.getValue())
                node.setLeft(insertInto(in, node.getLeft()));
            else
                node.setRight(insertInto(in, node.getRight()));

            return node;
        }
    }

    private int splitPoint(List<Point2D> input)
    {
        //median with bias towards right
        double middle = input.size() / 2.0;
        middle = Math.ceil(middle);

        return (int)middle;
    }

    private void rangeSearchRec(TreeNode<Double> node, double min, double max, LinkedList<TreeNode<Double>> results)
    {
        if (node == null)
            return;

        double value = node.getValue();

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

    public LinkedList<TreeNode<Double>> rangeSearch2D(double minX, double maxX, double minY, double maxY)
    {
        LinkedList<TreeNode<Double>> result = new LinkedList<>();
        rangeSearchRec(this.head, minX, maxX, result);

        //check if boundries are ok
        if(!result.isEmpty())
        {
            if(!inRange(result.getFirst().getValue(), minX, maxX ))
                result.removeFirst();

            if(!inRange(result.getLast().getValue(), minX, maxX ))
                result.removeLast();
        }

        //remove invalid Ys
        result.removeIf(d -> !inRange(d.getSecondary(), minY, maxY));

        return result;
    }


    //Getters / Setter
    TreeNode<Double> getHead() { return this.head; }
    void setHead(TreeNode<Double> head) { this.head = head; }
}