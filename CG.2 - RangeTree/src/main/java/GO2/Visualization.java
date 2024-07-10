package GO2;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

public class Visualization
{
    //Attributes
    private Graph graph;
    private double x_pos = 0, y_pos = 0;
    private double x_offset = 500, y_offset = 200;

    //Constructor
    Visualization(int size, TreeNode<Double> head, String name)
    {
        initWindow(name);
        initMath(size);
        displayTree1D(head);
    }

    //init methods
    private void initWindow(String name)
    {
        System.setProperty("org.graphstream.ui", "swing");
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        graph = new SingleGraph("Graph");
        graph.setAttribute("ui.title", name);

        //Add CSS
        graph.setAttribute("ui.stylesheet", "url('style.css')");

        Viewer viewer = graph.display();
        viewer.disableAutoLayout();
    }

    private void initMath(int size)
    {
        x_offset = 125 * size;
        y_offset = 20 * size;
    }

    private void makeTree(TreeNode<Double> treeNode, String id, double x, double y, double level)
    {
        if(treeNode == null) return;

        level += 1;
        level  = Math.pow(level, 1.25);

        try{ Thread.sleep(150); }
        catch (Exception ignore) {}

        double new_x_offset = x_offset / (level);
        //double new_x_offset = x_offset;
        double new_y = y - y_offset;

        if(treeNode.getLeft() != null)
        {
            //vars
            String new_id = id + "L";
            double new_x = x - new_x_offset;

            //visuals
            createNode(treeNode.getLeft().getValue(), new_id, new_x, new_y);
            graph.addEdge(id + new_id, id, new_id);

            //reiteration
            makeTree(treeNode.getLeft(), new_id, new_x, new_y, level);
        }

        if(treeNode.getRight() != null)
        {
            //vars
            String new_id = id + "R";
            double new_x = x + new_x_offset;

            //visuals
            createNode(treeNode.getRight().getValue(), new_id, new_x, new_y);
            graph.addEdge(id + new_id, id, new_id);

            //reiteration
            makeTree(treeNode.getRight(), new_id, new_x, new_y, level);
        }

    }

    private void createNode(double val, String id, double x, double y)
    {
        graph.addNode(id).setAttribute("ui.label", val);
        Node n = graph.getNode(id);
        n.setAttribute("xyz", x, y, 0.0);
    }

    private void displayTree1D(TreeNode<Double> head)
    {
        //create the head node
        createNode(head.getValue(), "H", x_pos, y_pos);

        //remiander of the nodes recursively
        makeTree(head, "H", x_pos, y_pos, 0);

    }
}
