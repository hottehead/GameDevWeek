package de.hochschuletrier.gdw.commons.tiled.tmx;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import de.hochschuletrier.gdw.commons.utils.Base64;

/**
 *
 * @author Santo Pfingsten
 */
public class TmxDataConverter implements Converter {

    @Override
    public boolean canConvert(Class type) {
        return TmxData.class == type;
    }

    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        final TmxData data = (TmxData) source;
        String encoding = data.getEncoding();
        writer.addAttribute("encoding", encoding);
        writer.addAttribute("compression", data.getCompression());

        if (encoding == null || encoding.isEmpty() || encoding.equals("xml")) {
            for (Integer id : data.getIds()) {
                writer.startNode("tile");
                writer.addAttribute("gid", id.toString());
                writer.endNode();
            }
        } else if (encoding.equals("csv")) {
            throw new RuntimeException("CSV writing not implemented yet");
//            ids = readIdsFromCSV(reader.getValue());
        } else if (encoding.equals("base64")) {
            throw new RuntimeException("Base64 writing not implemented yet");
//            ids = readIdsFromBase64(reader.getValue(), data.getCompression());
        } else {
            throw new RuntimeException("Unsupport encoding: " + encoding + ". We currently support base64, csv and none(xml)");
        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        TmxData data = new TmxData();
        String encoding = reader.getAttribute("encoding");
        data.setEncoding(encoding);
        data.setCompression(reader.getAttribute("compression"));

        try {
            if (encoding == null || encoding.isEmpty() || encoding.equals("xml")) {
                data.ids = readIdsFromXML(reader);
            } else if (encoding.equals("csv")) {
                data.ids = readIdsFromCSV(reader.getValue());
            } else if (encoding.equals("base64")) {
                data.ids = readIdsFromBase64(reader.getValue(), data.getCompression());
            } else {
                throw new Exception("Unsupport encoding: " + encoding + ". We currently support base64, csv and none(xml)");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return data;
    }

    private ArrayList<Integer> readIdsFromXML(HierarchicalStreamReader reader) throws NumberFormatException {
        ArrayList<Integer> ids = new ArrayList<Integer>();
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            String gid = reader.getAttribute("gid");
            ids.add(Integer.parseInt(gid));
            reader.moveUp();
        }
        return ids;
    }

    private ArrayList<Integer> readIdsFromCSV(String csv) throws NumberFormatException {
        ArrayList<Integer> ids = new ArrayList<Integer>();
        String[] values = csv.split(",");
        for (String value : values) {
            ids.add(Integer.parseInt(value.trim()));
        }
        return ids;
    }

    private ArrayList<Integer> readIdsFromBase64(String base64, String compression) throws IOException, Exception {
        InputStream stream;
        byte[] dec = Base64.decode(base64.trim());
        ByteArrayInputStream bis = new ByteArrayInputStream(dec);
        if (compression == null || compression.isEmpty()) {
            stream = bis;
        } else if (compression.equals("gzip")) {
            stream = new GZIPInputStream(bis);
        } else if (compression.equals("zlib")) {
            stream = new InflaterInputStream(bis);
        } else {
            throw new IOException("Unsupport compression: " + compression + ". Currently only uncompressed maps and gzip and zlib compressed maps are supported.");
        }

        ArrayList<Integer> ids = new ArrayList<Integer>();
        try {
            while (stream.available() > 0) {
                int id = 0;
                id |= stream.read();
                id |= stream.read() << 8;
                id |= stream.read() << 16;
                id |= stream.read() << 24;
                ids.add(id);
            }
        } catch (Exception e) {
        }
        return ids;
    }
}
