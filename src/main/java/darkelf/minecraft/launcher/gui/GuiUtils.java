package darkelf.minecraft.launcher.gui;

import com.google.gson.*;
import darkelf.util.ClassUtils;
import net.ftb.locale.I18N;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Map;

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
        if (config.has("bg-color")) {
            obj.setBackground(new Color(Integer.parseInt(config.get("bg-color").getAsString(), 16)));
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
            Rectangle rect = rectangleFromJson(config.get("bounds").getAsJsonArray());
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
            obj.setLocation(pointFromJson(config.get("position").getAsJsonArray()));
        }
        // set size (overrides bounds)
        if (config.has("size")) {
            Dimension size = dimensionFromJson(config.get("size").getAsJsonArray());
            // size without layout
            obj.setSize(size);
            // size with layout
            obj.setPreferredSize(size);
        }
        // set minimum size
        if (config.has("min-size")) {
            // overrides minimum size
            obj.setMinimumSize(dimensionFromJson(config.get("min-size").getAsJsonArray()));
        }
        // set maximum size
        if (config.has("max-size")) {
            // overrides maximum size
            obj.setMaximumSize(dimensionFromJson(config.get("max-size").getAsJsonArray()));
        }
    }

    /**
     * Create a @c Rectangle object from a JSON array. The array must contain exactly 4 integers, that will be used for
     * as the constructor parameters of the @c Rectangle object.
     *
     * @param array The @c JsonArray object to convert
     * @return The resulting @c Rectangle object
     */
    public static Rectangle rectangleFromJson(JsonArray array) {
        return new Rectangle(array.get(0).getAsInt(), array.get(1).getAsInt(), array.get(2).getAsInt(), array.get(3).getAsInt());
    }

    public static Point pointFromJson(JsonArray array) {
        return new Point(array.get(0).getAsInt(), array.get(1).getAsInt());
    }

    public static Dimension dimensionFromJson(JsonArray array) {
        return new Dimension(array.get(0).getAsInt(), array.get(1).getAsInt());
    }
}
