package sg.edu.ntu.cz2006.seproject.model;

import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

/**
 * Created by koAllen on 17/3/16.
 */
@Root(name = "uvindex", strict = false)
public class UVIndexResponse {
    @Text
    @Path("data/uv[1]")
    private int mUVIndexReading;

    public int getUvIndexReading() {
        return mUVIndexReading;
    }

    public String toString() {
        return "" + mUVIndexReading;
    }
}
