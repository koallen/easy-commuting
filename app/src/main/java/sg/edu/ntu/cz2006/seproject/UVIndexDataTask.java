package sg.edu.ntu.cz2006.seproject;

import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by koAllen on 16/3/16.
 */
public class UVIndexDataTask extends AsyncTask<Void, Void, String> {

    // callback interface
    public interface UVIndexDataTaskListener {
        public void onDataFetched(String apiData);
    }

    private UVIndexDataTaskListener listener;
    private OkHttpClient client;
    private static final String url = "http://www.nea.gov.sg/api/WebAPI/?dataset=uvi&keyref=781CF461BB6606AD62B1E1CAA87ECA612A87DF33A3ECDC11";

    public UVIndexDataTask(UVIndexDataTaskListener listener) {
        this.listener = listener;
        client = new OkHttpClient();
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            return run(url);
        } catch (IOException e) {
            return "Request Failed";
        }
    }

    @Override
    protected void onPostExecute(String apiData) {
        listener.onDataFetched(apiData);
    }

    private String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
