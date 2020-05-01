
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Iterator;
import java.util.Map;


public class Wallpaper {

    public static void main(String[] args) {
        try {
            readJson();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        String wallpaper_file = "C:/Users/Public/Downloads/Todays_Wallpaper.jpg";
        changeBackground(wallpaper_file);

    }
    public static interface User32 extends Library{
        User32 INSTANCE = Native.loadLibrary("user32",User32.class, W32APIOptions.DEFAULT_OPTIONS);
        boolean SystemParametersInfo(int one,int two,String s,int three);
    }
    public static void changeBackground(String filePath){
        User32.INSTANCE.SystemParametersInfo(20,0,filePath,0);
    }
    public static void readJson() throws IOException, ParseException{
        URL url = new URL("http://www.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&mkt=en-US");
        URLConnection request= url.openConnection();
        request.connect();
        JSONParser jp = new JSONParser();
        JSONObject skr = (JSONObject) jp.parse(new InputStreamReader((InputStream)request.getContent()));
        JSONArray array = (JSONArray) skr.get("images");
        Iterator itr2 = array.iterator();

        while (itr2.hasNext())
        {
            Iterator itr1 = ((Map) itr2.next()).entrySet().iterator();
            while (itr1.hasNext()) {
                Map.Entry pair = (Map.Entry) itr1.next();
                if(pair.getKey().equals("url")){
                    downloadusingstream("http://www.bing.com"+pair.getValue());}
            }
        }


    }


    public static void downloadusingstream(String urlstr) throws IOException{
        URL url = new URL(urlstr);
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream("C:/Users/Public/Downloads/Todays_Wallpaper.jpg");
        fos.getChannel().transferFrom(rbc,0,Long.MAX_VALUE);
        fos.close();
    }
}
