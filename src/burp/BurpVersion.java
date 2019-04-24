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
