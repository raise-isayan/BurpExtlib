package extend.model.base;

import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;

/**
 *
 * @author isayan
 */
public class CustomListModel extends DefaultListModel {

    /**
     * 上へ移動
     *
     * @param index インデックス
     * @return 移動後の index
     */
    @SuppressWarnings("unchecked")
    public synchronized int moveUp(int index) {
        if (0 < index) {
            Object prevItem = this.getElementAt(index);
            Object curItem = this.set(index - 1, prevItem);
            this.setElementAt(curItem, index);
            index--;
        }
        return index;
    }

    /**
     * 下へ移動
     *
     * @param index インデックス
     * @return 移動後の index
     */
    @SuppressWarnings("unchecked")
    public synchronized int moveDown(int index) {
        if (-1 < index && index < this.size() - 1) {
            Object prevItem = this.getElementAt(index);
            Object curItem = this.set(index + 1, prevItem);
            this.setElementAt(curItem, index);
            index++;
        }
        return index;
    }

    public List toList() {
        List<Object> list = new ArrayList<>();
        for (int index = 0; index < this.size(); index++) {
            list.add(this.getElementAt(index));
        }
        return list;
    }
}
