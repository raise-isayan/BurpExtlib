package extend.view.base;

/**
 *
 * @author isayan
 */
public interface HttpStatusLine {

    public String getStatusLine();

    public String getProtocolVersion();

    public short getStatusCode();

    public String getReasonPhrase();

}
