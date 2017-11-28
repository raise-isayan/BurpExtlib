/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package extend.view.base;

/**
 *
 * @author isayan
 */
public interface HttpRequestLine {

    public String getRequestLine();
        
    public String getMethod();

    public String getUri();

    public String getProtocolVersion();
    
}
