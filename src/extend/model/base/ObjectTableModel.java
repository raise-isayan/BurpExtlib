package extend.model.base;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.swing.table.TableModel;

/**
 *
 * @author isayan
 * @param <T>
 */
@Deprecated
public abstract class ObjectTableModel<T> extends CustomTableModel {

    private List<T> data = null;

    public ObjectTableModel(TableModel model) {
        this(model, Collections.synchronizedList(new LinkedList<T>()));
    }

    public ObjectTableModel(TableModel model, List<T> d) {
        super(model);
        this.data = d;
    }

    public void addRow(T value) {
        this.data.add(value);
        this.fireTableRowsInserted(this.data.size() - 1, this.data.size() - 1);
    }

    @Override
    public void removeRow(int index) {
        this.data.remove(index);
        fireTableRowsDeleted(index, index);
    }

    /**
     * 全て削除する
     */
    @Override
    public void removeAll() {
        int lastIndex = this.data.size() - 1;
        this.data.clear();
        if (lastIndex >= 0) {
            fireTableRowsDeleted(0, lastIndex);
        }
    }

    @Override
    public int getRowCount() {
        return this.data == null ? 0 : this.data.size();
    }

    @Override
    public abstract Object getValueAt(int row, int col);

    @Override
    public abstract void setValueAt(Object value, int row, int col);
    
    @Override
    public abstract Object[] getRows(int row);

    public T getData(int row) {
        return this.data.get(row);
    }

    private static int gcd(int i, int j) {
        return (j == 0) ? i : gcd(j, i % j);
    }

    @Override
    public void moveRow(int start, int end, int to) {
        moveRow(this.data, start, end, to);
    }

    @SuppressWarnings("unchecked")
    public void moveRow(List data, int start, int end, int to) {
        int shift = to - start;
        int first, last;
        if (shift < 0) {
            first = to;
            last = end;
        } else {
            first = start;
            last = to + end - start;
        }
        int size = end - start;
        int r = size - to;
        int g = gcd(size, r);
        for (int i = 0; i < g; i++) {
            int to2 = i;
            Object tmp = data.get(start + to2);
            for (int from = (to2 + r) % size; from != i; from = (to2 + r) % size) {
                data.set(start + to2, data.get(start + from));
                to2 = from;
            }
            data.set(start + to2, tmp);
        }
        fireTableRowsUpdated(first, last);
    }    
}
