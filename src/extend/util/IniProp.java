package extend.util;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 *
 * @author isayan
 */
public class IniProp {

    private final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    private final TransformerFactory transFactory = TransformerFactory.newInstance();
    /// Properties セクション
    private Map<String, Object> section = null;

    /**
     * IniPropクラスの新しいインスタンスを作成します。
     */
    public IniProp() {
        this.section = new TreeMap<String, Object>();
    }

    /**
     * 指定したセクション-エントリーの読み込みを行う
     *
     * @param sectionName セクション名
     * @param entryName エントリー名
     * @return エントリ値
     */
    public String readEntry(
            String sectionName,
            String entryName) {
        return readEntry(sectionName, entryName, null);
    }

    /**
     * 指定したセクション-エントリーの読み込みを行う
     *
     * @param sectionName セクション名
     * @param entryName エントリー名
     * @param entryDefaultValue デフォルト値
     * @return エントリ値
     */
    public boolean readEntryBool(
            String sectionName,
            String entryName,
            boolean entryDefaultValue) {
        return Util.parseBooleanDefault(readEntry(sectionName, entryName), entryDefaultValue);
    }

    /**
     * 指定したセクション-エントリーの読み込みを行う
     *
     * @param sectionName セクション名
     * @param entryName エントリー名
     * @param entryDefaultValue デフォルト値
     * @return エントリ値
     */
    public int readEntryInt(
            String sectionName,
            String entryName,
            int entryDefaultValue) {
        return Util.parseIntDefault(readEntry(sectionName, entryName), entryDefaultValue);
    }

    /**
     * 指定したセクション-エントリーの読み込みを行う
     *
     * @param sectionName セクション名
     * @param entryName エントリー名
     * @param entryDefaultValue デフォルト値
     * @return エントリ値
     */
    public float readEntryFloat(
            String sectionName,
            String entryName,
            float entryDefaultValue) {
        return Util.parseFloatDefault(entryName, entryDefaultValue);
    }

    /**
     * 指定したセクション-エントリーリストの読み込みを行う
     *
     * @param sectionName セクション名
     * @param entryName エントリー名
     * @return エントリ値
     */
    public List readEntryList(
            String sectionName,
            String entryName) {
        return this.readEntryList(sectionName, entryName, new ArrayList());
    }

    /**
     * 指定したセクション-エントリーリストの読み込みを行う
     *
     * @param sectionName セクション名
     * @param entryName エントリー名
     * @param entryDefaultValue デフォルト値
     * @return エントリ値
     */
    public List readEntryList(
            String sectionName,
            String entryName,
            List entryDefaultValue) {
        Map<String, Object> entry = readSection(sectionName);
        List entryValue = entryDefaultValue;
        if (entry instanceof Map) {
            entryValue = (List) entry.get(entryName);
            if (entryValue == null) {
                entryValue = entryDefaultValue;
            }
        }
        return entryValue;
    }

    /**
     * エントリーの読み込みを行う 指定したセクション-エントリーの読み込みを行う
     *
     * @param sectionName セクション名
     * @param entryName エントリー名
     * @param entryDefaultValue デフォルト値
     * @return エントリ値
     */
    public String readEntry(
            String sectionName,
            String entryName,
            String entryDefaultValue) {
        Map<String, Object> entry = readSection(sectionName);
        Object entryValue = entry.get(entryName);
        if (entryValue == null) {
            entryValue = entryDefaultValue;
        }
        return String.valueOf(entryValue);
    }

    /**
     * 指定したセクション-エントリーの書き込みを行う
     *
     * @param sectionName セクション名
     * @param entryName エントリー名
     * @param entryValue エントリー値
     * @return 書き込み成功時true
     */
    public boolean writeEntry(
            String sectionName,
            String entryName,
            String entryValue) {
        Map<String, Object> entry = readSection(sectionName);
        entry.put(entryName, entryValue);
        this.section.put(sectionName, entry);
        return true;
    }

    /**
     * 指定したセクション-エントリーの書き込みを行う
     *
     * @param sectionName セクション名
     * @param entryName エントリー名
     * @param entryValue エントリー値
     * @return 書き込み成功時true
     */
    public boolean writeEntryBool(
            String sectionName,
            String entryName,
            boolean entryValue) {
        return writeEntry(sectionName, entryName, Boolean.toString(entryValue));
    }

    /**
     * 指定したセクション-エントリーの書き込みを行う
     *
     * @param sectionName セクション名
     * @param entryName エントリー名
     * @param entryValue エントリー値
     * @return 書き込み成功時true
     */
    public boolean writeEntryInt(
            String sectionName,
            String entryName,
            int entryValue) {
        return writeEntry(sectionName, entryName, Integer.toString(entryValue));
    }

    /**
     * 指定したセクション-エントリーの書き込みを行う
     *
     * @param sectionName セクション名
     * @param entryName エントリー名
     * @param entryValue エントリー値
     * @return 書き込み成功時true
     */
    public boolean writeEntryFloat(
            String sectionName,
            String entryName,
            float entryValue) {
        return writeEntry(sectionName, entryName, String.valueOf(entryValue));
    }

    /**
     * 指定したセクション-エントリーリストの書き込みを行う
     *
     * @param sectionName セクション名
     * @param entryName エントリー名
     * @param entryList エントリー値
     * @return 書き込み成功時true
     */
    public boolean writeEntryList(
            String sectionName,
            String entryName,
            List entryList) {
        Map<String, Object> entry = readSection(sectionName);
        entry.put(entryName, entryList);
        this.section.put(sectionName, entry);
        return true;
    }

    /**
     * セクションの読込を行う
     *
     * @param sectionName セクション名
     * @return セクションハッシュ
     */
    protected Map<String, Object> readSection(String sectionName) {
        return readSection(this.section, sectionName);
    }

    /**
     * セクションリストの読込を行う
     *
     * @param sectionName セクション名
     * @return セクションリスト
     */
    @SuppressWarnings("unchecked")
    public List readSectionKeys(String sectionName) {
        Map<String, Object> entry = readSection(this.section, sectionName);
        return new ArrayList(Util.toList(entry.keySet().iterator()));
    }

    /**
     * セクションの読込を行う
     *
     * @param section セクションハッシュ
     * @param sectionName セクション名
     * @return セクションハッシュ
     */
    @SuppressWarnings("unchecked")
    protected static Map<String, Object> readSection(
            Map<String, Object> section,
            String sectionName) {
        Map<String, Object> entry = null;
        Object o = section.get(sectionName);
        if (o instanceof Map) {
            entry = (Map<String, Object>) o;
        } else {
            // ない場合は新規作成する
            entry = new TreeMap<String, Object>();
        }
        return entry;
    }

    /**
     * 指定されたセクションをクリア
     *
     * @param sectionName セクションハッシュ
     */
    public void remove(String sectionName) {
        this.section.remove(sectionName);
    }

    /**
     * プロパティクリア
     */
    public void clear() {
        this.section.clear();
    }

    /**
     * XMLファイルの読み込み処理
     *
     * @param file
     * @throws FileNotFoundException
     */
    public void loadFromXML(File file) throws IOException {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(file);
            loadFromXML(stream);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * XMLファイルの読み込み処理
     *
     * @throws FileNotFoundException
     */
    public void loadFromXML(String content) throws IOException {
        this.loadFromXML(content, StandardCharsets.ISO_8859_1);
    }

    /**
     * XMLファイルの読み込み処理
     *
     * @throws FileNotFoundException
     */
    public void loadFromXML(String content, Charset charset) throws IOException {
        ByteArrayInputStream bin = new ByteArrayInputStream(content.getBytes(charset));
        loadFromXML(bin);
    }
    
    /**
     * プロパティファイルの読み込みを行う
     *
     * @param instm XMLファイルストリーム
     * @throws java.io.IOException
     */
    protected void loadFromXML(InputStream instm) throws IOException {
        try {
            this.clear(); // クリアする
            DocumentBuilder builder = factory.newDocumentBuilder();
            factory.setIgnoringElementContentWhitespace(true);
            factory.setIgnoringComments(true);
            factory.setValidating(false);
            Document document = builder.parse(instm);
            readXMLItems(document);
        } catch (SAXException ex) {
            throw new IOException(ex);
        } catch (ParserConfigurationException ex) {
            throw new IOException(ex);
        }
    }

    /**
     * プロパティファイル項目の読み込みを行う
     *
     * @param document XMLドキュメント
     */
    protected void readXMLItems(Document document) {
        Element docElement = document.getDocumentElement();
        NodeList nodeSelectionList = docElement.getElementsByTagName("section");
        for (int i = 0; i < nodeSelectionList.getLength(); i++) {
            Element nodeSection = (Element) nodeSelectionList.item(i);
            // セクション名
            String sectionName = nodeSection.getAttribute("key");
            NodeList nodeEntryList = nodeSection.getElementsByTagName("entry");
            for (int j = 0; j < nodeEntryList.getLength(); j++) {
                Element nodeEntry = (Element) nodeEntryList.item(j);
                String entryName = nodeEntry.getAttribute("key");
                Object entryValue = readXMLItems(nodeEntry);
                if (entryValue instanceof List) {
                    writeEntryList(sectionName, entryName, (List) entryValue);
                } else {
                    writeEntry(sectionName, entryName, String.valueOf(entryValue));
                }
            }
        }
    }

    /**
     * プロパティファイル項目の読み込みを行う
     *
     * @param nodeEntry XMLノード
     * @return エントリー値
     */
    @SuppressWarnings("unchecked")
    protected Object readXMLItems(Element nodeEntry) {
        Object entryValue = getNodeText(nodeEntry);
        String typeValue = nodeEntry.getAttribute("type");
        if ("List".equals(typeValue)) {
            List list = new ArrayList();
            NodeList nodeList = nodeEntry.getElementsByTagName("list");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element nodeElement = (Element) nodeList.item(i);
                NodeList nodeItemList = nodeElement.getElementsByTagName("item");
                for (int j = 0; j < nodeItemList.getLength(); j++) {
                    Element nodeItem = (Element) nodeItemList.item(j);
                    list.add(this.getNodeText(nodeItem));
                }
            }
            entryValue = list;
        }
        return entryValue;
    }

    /**
     * テキストノード取得
     *
     * @param element Elementノード
     * @return テキスト
     */
    protected String getNodeText(Element element) {
        StringBuilder buff = new StringBuilder();
        NodeList nodeList = element.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node instanceof Text) {
                buff.append(node.getNodeValue());
            }
        }
        return buff.toString();
    }

    /**
     * XMLファイルの書き込み処理
     *
     * @param file
     * @param comment コメント
     * @param encoding エンコーディング
     * @throws FileNotFoundException
     */
    public void storeToXML(File file, String comment, String encoding) throws IOException {
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
            storeToXML(stream, comment, encoding);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ex1) {
                }
            }
        }
    }

    /**
     * XMLファイルの書き込み処理
     * @param comment
     * @param encoding
     * @return XML
     * @throws IOException 
     */
    public String storeToXML(String comment, String encoding) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        storeToXML(bout, comment, encoding);
        return new String(bout.toByteArray(), encoding);
    }
    
    /**
     * XMLファイルの書き込み処理
     *
     * @param filename ファイル名
     * @param comment コメント
     * @param encoding エンコーディング
     * @throws FileNotFoundException
     */
    public void storeToXML(String filename, String comment, String encoding) throws IOException {
        this.storeToXML(new File(filename), comment, encoding);
    }

    
    /**
     * プロパティファイルの書き込み処理
     *
     * @param ostm XML 出力ストリーム
     * @param comment コメント
     * @param encoding エンコーディング
     * @throws java.io.IOException
     */
    protected void storeToXML(OutputStream ostm, String comment, String encoding) throws IOException {
        try {
            Transformer transformer = transFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            StreamResult result = new StreamResult(ostm);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            DOMSource source = new DOMSource(document);
            document.appendChild(document.createComment(comment));
            Element element = document.createElement("properties");
            writeXMLItems(element);
            document.appendChild(element);
            transformer.transform(source, result);
        } catch (ParserConfigurationException ex) {
            throw new IOException(ex);
        } catch (TransformerException ex) {
            throw new IOException(ex);
        }
    }

    /**
     * XML項目の出力を行う
     *
     * @param element XMLライター
     */
    protected void writeXMLItems(Element element) {
        Document document = element.getOwnerDocument();
        // セクションの作成
        Iterator emu = this.section.keySet().iterator();
        while (emu.hasNext()) {
            String sectionName = (String) emu.next();
            Element sectionElement = document.createElement("section");
            sectionElement.setAttribute("key", sectionName);
            Map<String, Object> entry = readSection(this.section, sectionName);
            Iterator emu2 = entry.keySet().iterator();
            while (emu2.hasNext()) {
                String entryName = (String) emu2.next();
                Object entryValue = entry.get(entryName);
                Element entryElement = document.createElement("entry");
                entryElement.setAttribute("key", entryName);
                writeXMLItems(entryElement, entryValue);
                sectionElement.appendChild(entryElement);
            }
            element.appendChild(sectionElement);
        }
    }

    /**
     * XML項目の出力を行う
     *
     * @param element XMLライター
     * @param entryValue エントリー値
     */
    protected void writeXMLItems(Element element, Object entryValue) {
        Document document = element.getOwnerDocument();
        // 配列
        if (entryValue instanceof List) {
            element.setAttribute("type", "List");
            List list = (List) entryValue;
            Element elementList = document.createElement("list");
            for (Object item : list) {
                Element elementItem = document.createElement("item");
                elementItem.setAttribute("type", "string");
                elementItem.appendChild(document.createTextNode(String.valueOf(item)));
                elementList.appendChild(elementItem);
            }
            element.appendChild(elementList);
        } else {
            element.setAttribute("type", "string");
            element.appendChild(document.createTextNode(String.valueOf(entryValue)));
        }
    }
}
