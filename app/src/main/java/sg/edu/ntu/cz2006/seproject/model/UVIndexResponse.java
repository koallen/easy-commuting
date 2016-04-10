package sg.edu.ntu.cz2006.seproject.model;

import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

/**
 * A class for holding the response for UV index
 */
@Root(name = "uvindex", strict = false)
public class UVIndexResponse {
    @Text
    @Path("data/uv[1]")
    private int mUVIndexReading;

    public UVIndexResponse(int uvIndexReading) {
        mUVIndexReading = uvIndexReading;
    }

    /**
     * Returns the UV index reading
     * @return UV index reading
     */
    public int getUvIndexReading() {
        return mUVIndexReading;
    }
}
