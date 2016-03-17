package sg.edu.ntu.cz2006.seproject;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by koAllen on 17/3/16.
 */
@Root(name = "channel", strict = false)
public class PSIResponse {
    @Element(name = "item")
    private PSIData psiData;

    public PSIData getPsiData() {
        return psiData;
    }
}
