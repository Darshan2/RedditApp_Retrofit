package darshan.com.redditdemoapp.util;

import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Darshan B.S on 06-07-2018.
 */

/*
    what is CData?
    ex : <a href = "https://some link">Link name</a>
    here href = "https://some link" is CData(i.e data enclosed within tag declaration)

    We can easily get data: Link name(not a CData)
    However to get data: href = "https://some link"(CData). we need to employ some logic
 */
public class ExtractXML_CData {
    private static final String TAG = "ExtractXML_CData";

    public ExtractXML_CData() {
    }


    /**
     * Split CData in
     * @param xmlData
     * from
     * @param startTag
     * to
     * @param endTag
     * @return all the Strings followed by startTag up to endTag.
     */
    public ArrayList<String> extractXMLFor(String xmlData, String startTag, @Nullable String endTag) {
        ArrayList<String> result = new ArrayList<>();

        if(endTag == null) {
            String[] splitXMLArray = xmlData.split(startTag + "\"");
            for (String s : splitXMLArray) {
                if (s.contains("\"")) {
                    int position = s.indexOf("\"");
                    String link = s.substring(0, position);
                    result.add(link);
                }
            }

        } else {
            String[] splitXMLArray = xmlData.split(startTag);
            for (String s : splitXMLArray) {
                if (s.contains(endTag)) {
                    int position = s.indexOf(endTag);
                    String link = s.substring(0, position);
                    result.add(link);
                }
            }
        }

        return result;

    }
}
