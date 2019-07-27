package extend.util;

import extend.model.base.DefaultObjectTableModel;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author isayan
 */
public final class SwingUtil {

    private SwingUtil() {
    }

    private static Robot robot = null;

    public static synchronized Robot getRobot() {
        try {
            if (robot == null) {
                robot = new Robot();
            }
        } catch (AWTException ex) {
            Logger.getLogger(SwingUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return robot;
    }

    /**
     * 行の追加または更新
     *
     * @param srcTable 対象テーブル
     * @param items 追加オブジェクト
     * @param update 更新かどうか
     */
    public static void addOrUpdateItem(javax.swing.JTable srcTable, Object[] items, boolean update) {
        if (update) {
            updateItem(srcTable, items);
        } else {
            addItem(srcTable, items);
        }
    }

    public static void addItem(javax.swing.JTable srcTable, Object[] items) {
        TableModel modelSrc = srcTable.getModel();
        int lastIndex = modelSrc.getRowCount() - 1;
        if (modelSrc instanceof DefaultTableModel) {
            ((DefaultTableModel) modelSrc).addRow(items);
        }
        //        else if (modelSrc instanceof DefaultObjectTableModel) {
        //            ((DefaultObjectTableModel)modelSrc).addRow(items);
        //        }
        else {
            throw new java.lang.ClassCastException("class cast Excaption:" + modelSrc.getClass().getName());
        }
        srcTable.getSelectionModel().setSelectionInterval(lastIndex, lastIndex);
    }

    public static void insertItem(javax.swing.JTable srcTable, Object[] items) {
        TableModel modelSrc = srcTable.getModel();
        int index = srcTable.getSelectedRow();
        if (-1 < index && index < srcTable.getRowCount()) {
            int rowIndex = srcTable.convertRowIndexToModel(index);
            if (modelSrc instanceof DefaultTableModel) {
                ((DefaultTableModel) modelSrc).insertRow(rowIndex, items);
            }
//            else {
//                throw new java.lang.ClassCastException("class cast Excaption:" + modelSrc.getClass().getName());
//            }                
        } else {
            throw new java.lang.ClassCastException("class cast Excaption:" + modelSrc.getClass().getName());
        }
    }

    public static void updateItem(javax.swing.JTable srcTable, Object[] items, int viewIndex) {
        TableModel modelSrc = srcTable.getModel();
        int index = viewIndex;
        if (-1 < index && index < srcTable.getRowCount()) {
            int rowIndex = srcTable.convertRowIndexToModel(index);
            if (modelSrc instanceof DefaultTableModel) {
                ((DefaultTableModel) modelSrc).removeRow(rowIndex);
                ((DefaultTableModel) modelSrc).insertRow(rowIndex, items);
            }
            //            else if (modelSrc instanceof DefaultObjectTableModel) {
            //                ((DefaultObjectTableModel)modelSrc).removeRow(rowIndex);
            //                ((DefaultObjectTableModel)modelSrc).insertRow(rowIndex, items);                
            //            }            
            else {
                throw new java.lang.ClassCastException("class cast Excaption:" + modelSrc.getClass().getName());
            }
            srcTable.getSelectionModel().setSelectionInterval(rowIndex, rowIndex);
        }
    }

    public static void updateItem(javax.swing.JTable srcTable, Object[] items) {
        updateItem(srcTable, items, srcTable.getSelectedRow());
    }

    public static boolean removeItem(javax.swing.JTable srcTable) {
        TableModel modelSrc = srcTable.getModel();
        int index = srcTable.getSelectedRow();
        if (index > -1) {
            int rowIndex = srcTable.convertRowIndexToModel(index);
            if (modelSrc instanceof DefaultTableModel) {
                ((DefaultTableModel) modelSrc).removeRow(rowIndex);
            } else if (modelSrc instanceof DefaultObjectTableModel) {
                ((DefaultObjectTableModel) modelSrc).removeRow(rowIndex);
            } else {
                throw new java.lang.ClassCastException("class cast Excaption:" + modelSrc.getClass().getName());
            }
            return true;
        }
        return false;
    }

    public static Object[] editItem(javax.swing.JTable srcTable) {
        TableModel modelSrc = srcTable.getModel();
        int index = srcTable.getSelectedRow();
        Object[] editRows = null;
        if (index > -1) {
            int rowIndex = srcTable.convertRowIndexToModel(index);
            editRows = new Object[modelSrc.getColumnCount()];
            for (int i = 0; i < editRows.length; i++) {
                editRows[i] = modelSrc.getValueAt(rowIndex, i);
            }
        }
        return editRows;
    }

    public static void allNodesChanged(JTree tree) {
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        Enumeration e = root.preorderEnumeration();
        while (e.hasMoreElements()) {
            Object element = e.nextElement();
            if (element instanceof TreeNode) {
                TreeNode node = (TreeNode) element;
                model.nodeChanged(node);
            }
        }
    }

    public static void expandAll(JTree tree, TreePath path) {
        Object node = path.getLastPathComponent();
        TreeModel model = tree.getModel();
        if (model.isLeaf(node)) {
            return;
        }
        int num = model.getChildCount(node);
        for (int i = 0; i < num; i++) {
            expandAll(tree, path.pathByAddingChild(model.getChild(node, i)));
        }
        tree.expandPath(path);
    }

    public static void collapseAll(JTree tree, TreePath path) {
        Object node = path.getLastPathComponent();
        TreeModel model = tree.getModel();
        if (model.isLeaf(node)) {
            return;
        }
        int num = model.getChildCount(node);
        for (int i = 0; i < num; i++) {
            collapseAll(tree, path.pathByAddingChild(model.getChild(node, i)));
        }
        tree.collapsePath(path);
    }

    public static void setContainerEnable(Container container, boolean enabled) {
        Component[] list = container.getComponents();
        for (Component c : list) {
            if (c instanceof Container) {
                setContainerEnable((Container) c, enabled);
                c.setEnabled(enabled);
            } else {
                c.setEnabled(enabled);
            }
        }
    }

    /**
     * ファイル上書き確認ダイアログを表示する。
     *
     * @param file 上書き対象ファイル
     * @param message ダイヤログメッセージ
     * @param title ダイヤログタイトル
     * @return 上書きOKが指示されたらtrue
     */
    public static boolean isFileOverwriteConfirmed(File file, String message, String title) {
        if (!file.exists()) {
            return true;
        }
        int confirm = JOptionPane.showConfirmDialog(
                null, message, title,
                JOptionPane.WARNING_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION);
        return (confirm == JOptionPane.OK_OPTION);
    }

    public static ImageIcon createCloseIcon() {
        return new ImageIcon() {
            private final int width = 16;
            private final int height = 16;

            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                g.translate(x, y);
                g.setColor(Color.BLACK);
                g.drawLine(4, 4, 11, 11);
                g.drawLine(4, 5, 10, 11);
                g.drawLine(5, 4, 11, 10);
                g.drawLine(11, 4, 4, 11);
                g.drawLine(11, 5, 5, 11);
                g.drawLine(10, 4, 4, 10);
                g.translate(-x, -y);
            }

            @Override
            public int getIconWidth() {
                return width;
            }

            @Override
            public int getIconHeight() {
                return height;
            }
        };
    }

    public static ImageIcon createSquareIcon(final Color color, final int w, final int h) {
        return new ImageIcon() {
            @Override
            public int getIconWidth() {
                return w + 2;
            }

            @Override
            public int getIconHeight() {
                return h + 2;
            }

            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                g.setColor(color);
                g.fillRoundRect(x, y, w, h, 0, 0);
            }
        };
    }

    public static Icon createEmptyIcon() {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                /* Empty icon */ }

            @Override
            public int getIconWidth() {
                return 2;
            }

            @Override
            public int getIconHeight() {
                return 0;
            }
        };
    }

    public static class IntegerDocument extends PlainDocument {

        int currentValue = 0;

        public IntegerDocument() {
            super();
        }

        public int getValue() {
            return currentValue;
        }

        @Override
        public void insertString(int offset, String str, AttributeSet attributes) throws BadLocationException {
            if (str == null) {
            } else {
                String newValue;
                int length = getLength();
                if (length == 0) {
                    newValue = str;
                } else {
                    String currentContent = getText(0, length);
                    StringBuilder currentBuffer = new StringBuilder(currentContent);
                    currentBuffer.insert(offset, str);
                    newValue = currentBuffer.toString();
                }
                currentValue = checkInput(newValue, offset);
                super.insertString(offset, str, attributes);
            }
        }

        @Override
        public void remove(int offset, int length) throws BadLocationException {
            int currentLength = getLength();
            String currentContent = getText(0, currentLength);
            String before = currentContent.substring(0, offset);
            String after = currentContent.substring(length + offset, currentLength);
            String newValue = before + after;
            currentValue = checkInput(newValue, offset);
            super.remove(offset, length);
        }

        private int checkInput(String proposedValue, int offset) throws BadLocationException {
            if (proposedValue.length() > 0) {
                try {
                    int newValue = Integer.parseInt(proposedValue);
                    return newValue;
                } catch (NumberFormatException e) {
                    throw new BadLocationException(proposedValue, offset);
                }
            } else {
                return 0;
            }
        }
    }

    public static class ReadOnlyDocument extends PlainDocument {

        public ReadOnlyDocument() {
            super();
        }

        @Override
        public void insertString(int offset, String str, AttributeSet attributes) throws BadLocationException {
        }

        @Override
        public void remove(int offset, int length) throws BadLocationException {
        }

    }

    private static final Toolkit TOOLKIT = Toolkit.getDefaultToolkit();

    public static String systemSelection() {

        String selection = null;
        try {
            Clipboard cb = TOOLKIT.getSystemSelection();
            if (cb != null) {
                Transferable t = cb.getContents(null);
                selection = (String) t.getTransferData(DataFlavor.stringFlavor);
            }
        } catch (UnsupportedFlavorException ex) {
            Logger.getLogger(SwingUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SwingUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return selection;
    }

    /**
     * クリップボードにコピー
     *
     * @param s 文字列
     */
    public static void systemClipboardCopy(String s) {
        StringSelection ss = new StringSelection(s);
        Clipboard systemClipbord = TOOLKIT.getSystemClipboard();
        systemClipbord.setContents(ss, null);
    }

    /**
     * クリップボードからペースト
     *
     * @return s 文字列
     */
    public static String systemClipboardPaste() {
        Clipboard systemClipbord = TOOLKIT.getSystemClipboard();
        Transferable t = systemClipbord.getContents(null);
        String s = "";
        try {
            s = (String) t.getTransferData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException e) {
        } catch (IOException e) {
        }
        return s;
    }

    public static Popup createToolTip(Component owner, String tip, int x, int y) {
        JToolTip toolTip = new JToolTip();
        toolTip.setTipText(tip);
        PopupFactory popupFactory = PopupFactory.getSharedInstance();
        return popupFactory.getPopup(owner, toolTip, x, y);
    }

    public static void showToolTip(Component owner, String tip, int x, int y, long millis) {
        Popup popTip = createToolTip(owner, tip, x, y);
        Runnable thread = new Runnable() {
            @Override
            public void run() {
                try {
                    popTip.show();
                    Thread.sleep(millis);
                    popTip.hide();
                } catch (InterruptedException ex) {
                }
            }
        };
        (new Thread(thread)).start();
    }

    public static String getKeyText(java.awt.event.KeyEvent evt) {
        StringBuilder keyIdent = new StringBuilder();
        keyIdent.append(java.awt.event.KeyEvent.getKeyModifiersText(evt.getModifiers()));
        if (evt.getKeyCode() != java.awt.event.KeyEvent.CHAR_UNDEFINED
                && evt.getKeyCode() != java.awt.event.KeyEvent.VK_CONTROL
                && evt.getKeyCode() != java.awt.event.KeyEvent.VK_SHIFT
                && evt.getKeyCode() != java.awt.event.KeyEvent.VK_ALT
                && evt.getKeyCode() != java.awt.event.KeyEvent.VK_META) {
            if (keyIdent.length() > 0) {
                keyIdent.append("+");
            }
            keyIdent.append(java.awt.event.KeyEvent.getKeyText(evt.getKeyCode()));
        }
        return keyIdent.toString();
    }

    public static void sendKeys(int key) {
        getRobot().keyPress(key);
        getRobot().keyRelease(key);
    }

    public static void sendMouse(int buttons) {
        getRobot().mousePress(buttons);
        getRobot().mouseRelease(buttons);
    }

}