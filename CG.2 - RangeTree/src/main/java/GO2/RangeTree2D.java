package GO2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class RangeTree2D
{
    //Attributes
    private TreeNode<Double> head = null;
    private TreeNode<Double> head2 = null;

    RangeTree2D(ArrayList<Point2D> p_arr)
    {
        //Copy nodes for two tress
        ArrayList<TreeNode<Double>> main = new ArrayList<>();
        ArrayList<TreeNode<Double>> associate = new ArrayList<>();

        //Link y as associate
        for(Point2D p : p_arr)
        {
            TreeNode<Double> y = new TreeNode<>(p.getY(), null, null);
            TreeNode<Double> x = new TreeNode<>(p.getX(), null, null, y);

            main.add(x);
            associate.add(y);
        }

        this.head = makeTree(this.head, main);
        TreeNode<Double> buffer = null;
        this.head.setAssociate(makeTree(buffer, associate));
    }

    private TreeNode<Double> makeTree(TreeNode<Double> local_head, ArrayList<TreeNode<Double>> inp)
    {
        //Sort the array
        sortNodes(inp);

        LinkedList<List<TreeNode<Double>>> pending = new LinkedList<>();
        pending.add(inp);

        while (!pending.isEmpty())
        {
            List<TreeNode<Double>> current = pending.poll();

            if(current.size() > 1)
            {
                //System.out.println("Current\n" + current);
                //split the list
                int splitPoint = splitPoint(current);
                List<TreeNode<Double>> left = current.subList(0, splitPoint);
                List<TreeNode<Double>> right = current.subList(splitPoint, current.size());

                //save results
                if (!left.isEmpty()) pending.add(left);
                if (!right.isEmpty()) pending.add(right);

                //if not new, insert it, otherwise copy it
                if(!exists(current.get(splitPoint -1), local_head))
                {
                    double v = current.get(splitPoint -1).getValue();
                    TreeNode<Double> l =  current.get(splitPoint -1).getLeft();
                    TreeNode<Double> r =  current.get(splitPoint -1).getRight();
                    TreeNode<Double> a =  current.get(splitPoint -1).getAssociate();

                    TreeNode<Double> insertion = new TreeNode<Double>(v, l ,r ,a);
                    local_head = insertInto(insertion, local_head);
                }
                else
                {
                    current.get(splitPoint -1).setLeft(null);
                    current.get(splitPoint -1).setRight(null);
                    local_head = insertInto(current.get(splitPoint -1), local_head);
                }

            }
            else if (current.size() == 1)
                local_head = insertInto(current.getFirst(), local_head);

        }

        return local_head;
    }

    private TreeNode<Double> insertInto(TreeNode<Double> in, TreeNode<Double> local_head)
    {
        if(local_head == null)
            return in;

        if(in.getValue() <= local_head.getValue())
            local_head.setLeft(insertInto(in, local_head.getLeft()));
        else
            local_head.setRight(insertInto(in, local_head.getRight()));

        return local_head;
    }

    private void sortNodes(ArrayList<TreeNode<Double>> inp)
    {
        for(int i = 0; i < inp.size(); i++)
        {
            int min = i;

            for(int j = i + 1; j < inp.size(); j++)
            {
                if(inp.get(j).getValue() < inp.get(min).getValue())
                    min = j;
            }

            TreeNode<Double> buffer = inp.get(i);
            inp.set(i, inp.get(min));
            inp.set(min, buffer);
        }
    }

    private int splitPoint(List<TreeNode<Double>> input)
    {
        //median with bias towards right
        double middle = input.size() / 2.0;
        middle = Math.ceil(middle);

        return (int)middle;
    }

    public boolean exists(TreeNode<Double> tgt, TreeNode<Double> local_head)
    {
        if(local_head == null)
            return false;

        if(local_head == tgt)
            return true;

        if(tgt.getValue() <= local_head.getValue())
            return exists(tgt, local_head.getLeft());
        else
            return exists(tgt, local_head.getRight());
    }

    //Getters
    TreeNode<Double> getHead() { return this.head; }
    TreeNode<Double> getHead2() { return this.head2; }
}
