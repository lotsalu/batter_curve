package archermind.com.batter_curve;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by n910633 on 2015/8/20.
 */
public class CrushHandler implements Thread.UncaughtExceptionHandler {
    public static final String TAG = "CrushHandler";

    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

    }
}
