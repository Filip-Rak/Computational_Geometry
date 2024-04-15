package GO2;

public class Visualization2D
{
    private final int size_const = 10;

    //Constructor
    Visualization2D(TreeNode<Double> head)
    {
        Visualization v = new Visualization(size_const, head, "MAIN");
        displayTree2D(head, "A");
    }

    private void displayTree2D(TreeNode<Double> local_head, String name)
    {
        if(local_head == null)
            return;

        //no drawing for leaves
        TreeNode<Double> assoc = local_head.getAssociate();
        if(assoc == null)
            return;

        Visualization v = new Visualization(size_const, assoc, name);

        displayTree2D(local_head.getLeft(), name + "->L");
        displayTree2D(local_head.getRight(), name + "->R");
    }
}
