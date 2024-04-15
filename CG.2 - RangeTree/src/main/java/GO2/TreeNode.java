package GO2;

import java.util.LinkedList;

public class TreeNode<T>
{
    //Attributes
    private T value;
    private TreeNode<T> left, right;
    private TreeNode<T> associate;

    //tylko dla rangesearch2d, nie wiem jak inaczej
    private T secondary_value;

    //Constructors
    TreeNode(T value, TreeNode<T> left, TreeNode<T> right, TreeNode<T> associate)
    {
        initialize(value, left, right, associate, null);
    }

    TreeNode(T value, TreeNode<T> left, TreeNode<T> right)
    {
        initialize(value, left, right, null, null);
    }

    TreeNode(T value, TreeNode<T> left, TreeNode<T> right, TreeNode<T> associate, T secondary)
    {
        initialize(value, left, right, associate, secondary);
    }

    private void initialize(T value, TreeNode<T> left, TreeNode<T> right, TreeNode<T> associate, T secondary)
    {
        this.value = value;
        this.left = left;
        this.right = right;
        this.associate = associate;
        this.secondary_value = secondary;
    }

    public static LinkedList<TreeNode<Double>> get_all_nodes(TreeNode<Double> head)
    {
        LinkedList<TreeNode<Double>> output = new LinkedList<>();
        get_all_nodes(head, output);

        return output;
    }

    private static void get_all_nodes(TreeNode<Double> head, LinkedList<TreeNode<Double>> outList)
    {
        if(head == null)
            return;

        outList.add(head);

        get_all_nodes(head.getAssociate());
        get_all_nodes(head.getLeft());
        get_all_nodes(head.getRight());
    }

    //Getters
    public T getValue() { return value; }
    public TreeNode<T> getLeft() { return left; }
    public TreeNode<T> getRight() { return right; }
    public TreeNode<T> getAssociate() { return associate; }
    public T getSecondary() {return this.secondary_value; }

    //Setters
    public void setValue(T val) { value = val; }
    public void setLeft(TreeNode<T> left) { this.left = left; }
    public void setRight(TreeNode<T> right) { this.right = right; }
    public void setAssociate(TreeNode<T> associate) { this.associate = associate; }
    public void setSecondary_value(T secondary_value) { this.secondary_value= secondary_value; }
}
