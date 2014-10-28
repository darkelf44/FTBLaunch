package darkelf.minecraft.launcher.gui;

import com.google.gson.*;
import darkelf.util.ClassUtils;
import net.ftb.locale.I18N;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by evan on 10/26/14.
 */
public class GuiUtils {

    public static JsonElement loadJsonFromResource(String resource) {
        try {
            return new JsonParser().parse(new BufferedReader(new InputStreamReader(ClassUtils.getResource(resource).openStream())));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static JFrame initFrameWithJson(JFrame frame, JsonObject config) {
        // set layout (should be set before bounds)
        setLayoutWithJson(frame, config);
        // set up bounds of the content pane
        setBoundsWithJson(frame, config);
        // set icon (frame only)
        if (config.has("icon")) {
            frame.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassUtils.getResource(config.get("icon").getAsString())));
        }
        // set title (frame only)
        if (config.has("title")) {
            frame.setTitle(I18N.getLocaleString(config.get("title").getAsString()));
        }
        // return modified frame
        return frame;
    }

    public static Component initWithJson(Component obj, String config) {
        return initWithJson(obj, new JsonParser().parse(config).getAsJsonObject());
    }

    public static Component initWithJson(Component obj, JsonObject config) {
        // set up bounds
        setBoundsWithJson(obj, config);

        // set enabled
        if (config.has("enabled")) {
            obj.setEnabled(config.get("enabled").getAsBoolean());
        }

        // set background
        if (config.has("background")) {
            obj.setBackground(new Color(Integer.parseInt(config.get("background").getAsString(), 16)));
        }

        // set container properties
        if (obj instanceof Container) {
            // set layout
            setLayoutWithJson((Container) obj, config);
        }

        // set window properties
        if (obj instanceof Window) {
            // set icon
            if (config.has("icon")) {
                ((Window) obj).setIconImage(Toolkit.getDefaultToolkit().getImage(ClassUtils.getResource(config.get("icon").getAsString())));
            }

            // set always on top
            if (config.has("always-on-top")) {
                ((Window) obj).setAlwaysOnTop(config.get("always-on-top").getAsBoolean());
            }
        }

        // set frame properties
        if (obj instanceof Frame) {
            // set title
            if (config.has("title")) {
                ((Frame) obj).setTitle(config.get("title").getAsString());
            }

            // set title - i18n version
            if (config.has("title-i18n")) {
                ((Frame) obj).setTitle(I18N.getLocaleString(config.get("title").getAsString()));
            }

            // set resizeable
            if (config.has("resizable")) {
                ((Frame) obj).setResizable(config.get("resizable").getAsBoolean());
            }
        }

        // set JFrame properties
        if (obj instanceof JFrame) {
            // set exit on close
            if (config.has("exit-on-close")) {
                if (config.get("exit-on-close").getAsBoolean()) {
                    ((JFrame) obj).setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                } else {
                    ((JFrame) obj).setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
                }
            }
        }

        // return modified component
        return obj;
    }

    private static void setLayoutWithJson(Container obj, JsonObject config) {
        // set layout TODO: use object to describe layout
        if (config.has("layout")) {
            // get layout element
            JsonElement layout = config.get("layout");
            // set layout depending on the node
            if (layout.isJsonNull()) {
                // set layout to null
                obj.setLayout(null);
            } else {
                // set layout to a new instance of the class
                obj.setLayout((LayoutManager) ClassUtils.newInstance(layout.getAsString()));
            }
        }
    }

    private static void setBoundsWithJson(Component obj, JsonObject config) {
        // set bounds
        if (config.has("bounds")) {
            // read bounds
            Rectangle rect = rectangleFromJson(config.get("bounds"));
            // handle JFrames internal bounds
            if (obj instanceof JFrame) {
                // position
                obj.setLocation(rect.getLocation());
                // size without layout
                ((JFrame) obj).getContentPane().setSize(rect.getSize());
                // size with layout
                ((JFrame) obj).getContentPane().setPreferredSize(rect.getSize());
            } else {
                // size without layout
                obj.setBounds(rect);
                // size with layout
                obj.setPreferredSize(obj.getSize());
            }
        }
        // set location (overrides bounds)
        if (config.has("position")) {
            obj.setLocation(pointFromJson(config.get("position")));
        }
        // set size (overrides bounds)
        if (config.has("size")) {
            Dimension size = dimensionFromJson(config.get("size"));
            // size without layout
            obj.setSize(size);
            // size with layout
            obj.setPreferredSize(size);
        }
        // set minimum size
        if (config.has("min-size")) {
            // overrides minimum size
            obj.setMinimumSize(dimensionFromJson(config.get("min-size")));
        }
        // set maximum size
        if (config.has("max-size")) {
            // overrides maximum size
            obj.setMaximumSize(dimensionFromJson(config.get("max-size")));
        }
    }

    //--------------------------------------------------------------------------------
    //  JSON to structure conversions
    //--------------------------------------------------------------------------------

    public static Rectangle rectangleFromJson(JsonElement j) {
        // check array format
        if (j.getAsJsonArray().size() == 4) {
            return new Rectangle(
                    j.getAsJsonArray().get(0).getAsInt(),
                    j.getAsJsonArray().get(1).getAsInt(),
                    j.getAsJsonArray().get(2).getAsInt(),
                    j.getAsJsonArray().get(3).getAsInt());
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public static Point pointFromJson(JsonElement j) {
        // check array format
        if (j.getAsJsonArray().size() == 2) {
            return new Point(
                    j.getAsJsonArray().get(0).getAsInt(),
                    j.getAsJsonArray().get(1).getAsInt());
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public static Dimension dimensionFromJson(JsonElement j) {
        // check array format
        if (j.getAsJsonArray().size() == 2) {
            return new Dimension(
                    j.getAsJsonArray().get(0).getAsInt(),
                    j.getAsJsonArray().get(1).getAsInt());
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public static Color colorFromJson(JsonElement j) {
        // select conversion method
        if (j.isJsonArray() && j.getAsJsonArray().size() == 3) {
            // convert an array with exactly 3 integers
            return new Color(
                    j.getAsJsonArray().get(0).getAsByte(),
                    j.getAsJsonArray().get(1).getAsByte(),
                    j.getAsJsonArray().get(2).getAsByte());
        } else if (j.isJsonPrimitive() && j.getAsJsonPrimitive().isString() && j.getAsString().startsWith("#")) {
            // convert a string in "#0000000" format
            return new Color(Integer.parseInt(j.getAsString().substring(1), 16));
        } else {
            // invalid formats
            throw new UnsupportedOperationException();
        }
    }

    public static Cursor cursorFromJson(JsonElement j) {
        // check format
        if (j.getAsJsonPrimitive().isString()) {
            return Cursor.getPredefinedCursor(CursorIndex.valueOf(j.getAsString()).index);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public static  Font fontFromJson(JsonElement j) {
        return null;
    }

    private static enum CursorIndex {

        DEFAULT_CURSOR(0),
        CROSSHAIR_CURSOR(1),
        TEXT_CURSOR(2),
        WAIT_CURSOR(3),
        SW_RESIZE_CURSOR(4),
        SE_RESIZE_CURSOR(5),
        NW_RESIZE_CURSOR(6),
        NE_RESIZE_CURSOR(7),
        N_RESIZE_CURSOR(8),
        S_RESIZE_CURSOR(9),
        W_RESIZE_CURSOR(10),
        E_RESIZE_CURSOR(11),
        HAND_CURSOR(12),
        MOVE_CURSOR(13);

        CursorIndex(int index) {
            this.index = index;
        }

        public int index;
    }
}
