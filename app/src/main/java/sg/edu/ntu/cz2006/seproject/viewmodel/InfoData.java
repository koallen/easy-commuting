package sg.edu.ntu.cz2006.seproject.viewmodel;

/**
 * Created by koAllen on 17/3/16.
 */
public class InfoData {
    private String data;
    private String suggestion;

    public InfoData(String data, String suggestion) {
        this.data = data;
        this.suggestion = suggestion;
    }

    public String getData() {
        return data;
    }

    public String getSuggestion() {
        return suggestion;
    }
}
