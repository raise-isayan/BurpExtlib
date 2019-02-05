package extend.model.base;

import java.util.Enumeration;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author isayan
 */
@Deprecated
public class CustomTreeModel extends DefaultTreeModel  {
    
    public CustomTreeModel(TreeNode root) {
        super(root);
    }

    public void expandAll(JTree tree, TreePath path) {
        TreeNode node = (TreeNode) path.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            Enumeration enumeration = node.children();
            while (enumeration.hasMoreElements()) {
                TreeNode n = (TreeNode) enumeration.nextElement();
                TreePath p = path.pathByAddingChild(n);
                expandAll(tree, p);
            }
        }
        tree.expandPath(path);
    }

    public void collapseAll(JTree tree, TreePath path) {
        TreeNode node = (TreeNode) path.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            Enumeration enumeration = node.children();
            while (enumeration.hasMoreElements()) {
                TreeNode n = (TreeNode) enumeration.nextElement();
                TreePath p = path.pathByAddingChild(n);
                expandAll(tree, p);
            }
        }
        tree.collapsePath(path);        
    }
    
}
