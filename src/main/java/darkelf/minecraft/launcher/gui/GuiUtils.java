package darkelf.minecraft.launcher.gui;

import com.google.gson.*;
import darkelf.util.ClassUtils;
import net.ftb.locale.I18N;
import net.ftb.util.OSUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;

/**
 * Created by evan on 10/26/14.
 */
public class GuiUtils {

    public static void addLink(JComponent component, final String url) {
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    OSUtils.browse(url);
                }
            }
        });
    }

    //--------------------------------------------------------------------------------
    //  Component initialization with JSON
    //--------------------------------------------------------------------------------

    public static JsonElement loadJson(String resource) {
        try {
            return new JsonParser().parse(new BufferedReader(new InputStreamReader(ClassUtils.getResource(resource).openStream())));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static Component initWithJson(Component obj, String config) {
        return initWithJson(obj, new JsonParser().parse(config).getAsJsonObject());
    }

    public static Component initWithJson(Component obj, JsonElement j) {
        GuiWrapper wrap = new GuiWrapper(obj);
        JsonObject config = j.getAsJsonObject();

        // iterate over properties
        if (config.has("enabled")) {
            wrap.setEnabled(config.get("enabled").getAsBoolean());
        }
        if (config.has("visible")) {
            wrap.setVisible(config.get("visible").getAsBoolean());
        }
        if (config.has("focusable")) {
            wrap.setFocusable(config.get("focusable").getAsBoolean());
        }
        if (config.has("bg-color")) {
            wrap.setBgColor(colorFromJson(config.get("bg-color")));
        }
        if (config.has("fg-color")) {
            wrap.setFgColor(colorFromJson(config.get("fg-color")));
        }
        if (config.has("bounds")) {
            wrap.setBounds(rectangleFromJson(config.get("bounds")));
        }
        if (config.has("position")) {
            wrap.setPosition(pointFromJson(config.get("position")));
        }
        if (config.has("size")) {
            wrap.setSize(dimensionFromJson(config.get("size")));
        }
        if (config.has("min-size")) {
            wrap.setMinSize(dimensionFromJson(config.get("min-size")));
        }
        if (config.has("max-size")) {
            wrap.setMaxSize(dimensionFromJson(config.get("max-size")));
        }
        if (config.has("cursor")) {
            wrap.setCursor(cursorFromJson(config.get("cursor")));
        }
        if (config.has("font")) {
            wrap.setFont(fontFromJson(config.get("font")));
        }
        if (config.has("border")) {
            // TODO: set border with Json
        }
        if (config.has("tooltip")) {
            wrap.setToolTip(i18nFromJson(config.get("tooltip")));
        }
        if (config.has("icon")) {
            wrap.setIcon(config.get("icon").getAsString());
        }
        if (config.has("resizable")) {
            wrap.setResizable(config.get("resizable").getAsBoolean());
        }
        if (config.has("title")) {
            wrap.setTitle(i18nFromJson(config.get("title")));
        }
        if (config.has("image")) {
            wrap.setImage(config.get("image").getAsString());
        }
        if (config.has("disabled-image")) {
            wrap.setDisabledImage(config.get("disabled-image").getAsString());
        }
        if (config.has("text")) {
            wrap.setText(i18nFromJson(config.get("text")));
        }

        // return object
        return obj;
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
        if (j.isJsonPrimitive() && j.getAsJsonPrimitive().isString()) {
            return Cursor.getPredefinedCursor(CursorIndex.valueOf(j.getAsString()).index);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public static Object instanceFromJson(JsonElement j) {
        // check format
        if (j.isJsonNull()) {
            return null;
        } else if (j.isJsonPrimitive() && j.getAsJsonPrimitive().isString()) {
            return ClassUtils.newInstance(j.getAsString());
        } else {
            throw new UnsupportedOperationException();
        }
    }

    // TODO: implements this
    public static  Font fontFromJson(JsonElement j) {
        if (j.isJsonNull()) {
            return null;
        } else if (j.isJsonArray() && j.getAsJsonArray().size() == 3) {
            return new Font(j.getAsJsonArray().get(0).getAsString(), FontStyle.valueOf(j.getAsJsonArray().get(1).getAsString()).style, j.getAsJsonArray().get(2).getAsInt());
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public static String i18nFromJson(JsonElement j) {
        // get string
        String text = j.getAsString();
        // strings starting with "@" are used for i18n
        if (text.startsWith("@")) {
            return I18N.getLocaleString(text.substring(1));
        } else {
            return text;
        }
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

    private static enum FontStyle {
        PLAIN(0),
        BOLD(1),
        ITALIC(2);

        FontStyle(int style) {
            this.style = style;
        }

        public int style;
    }
}
