/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package burp;

/**
 *
 * @author isayan
 */
public interface BurpVersion {

    public String getProductName();

    public String getMajor();

    public String getMinor();

    public int getMajorVersion();

    public int getMinorVersion();

    public boolean isFreeVersion();

    public boolean isExtendSupport();

    public boolean isProfessional();
    
}
