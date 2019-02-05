/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extend.model.base;

/**
 *
 * @author isayan
 */
public interface ObjectTableColumn {

    public String getColumnName(int column);

    public Class<?> getColumnClass(int columnIndex);
    
    public int getColumnCount();
    
}
