package darkelf.minecraft.launcher.gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * A Class that's a super-interface of most swing components. It's used to automatically set properties of various
 * properties among many swing objects.
 *
 * Created by evan on 10/27/14.
 */
public class GuiWrapper {

    public GuiWrapper(Component component) {
        this.component = component;

    }

    //--------------------------------------------------------------------------------
    // From AWT Component
    //--------------------------------------------------------------------------------

    public void setEnabled(boolean value) {
        component.setEnabled(value);
    }

    public void setVisible(boolean value) {
        component.setVisible(value);
    }

    public void setFocusable(boolean value) {
        component.setFocusable(value);
    }

    public void setBgColor(Color value) {
        component.setBackground(value);
    }

    public void setFgColor(Color value) {
        component.setForeground(value);
    }

    public void setBounds(Rectangle value) {
        if (component instanceof JFrame) {
            component.setLocation(value.getLocation());
            ((JFrame) component).getContentPane().setSize(value.getSize());
            ((JFrame) component).getContentPane().setPreferredSize(value.getSize());
        } else {
            component.setBounds(value);
            component.setPreferredSize(value.getSize());
        }
    }

    public void setPosition(Point value) {
        component.setLocation(value);
    }

    public void setSize(Dimension value) {
        component.setSize(value);
        component.setPreferredSize(value);
    }

    public void setMinSize(Dimension value) {
        component.setMinimumSize(value);
    }

    public void setMaxSize(Dimension value) {
        component.setMaximumSize(value);
    }

    public void setCursor(Cursor value) {
        component.setCursor(value);
    }

    public void setFont(Font value) {
        component.setFont(value);
    }

    //--------------------------------------------------------------------------------
    // From AWT Container
    //--------------------------------------------------------------------------------

    // TODO: support complex layouts
    public void setLayout(LayoutManager value) {
        if (component instanceof Container) {
            ((Container) component).setLayout(value);
        } else {
            throw new UnsupportedOperationException(getClass().getSimpleName());
        }
    }

    //--------------------------------------------------------------------------------
    // From Swing JComponent
    //--------------------------------------------------------------------------------

    public void setBorder(Border value) {
        if (component instanceof JComponent) {
            ((JComponent) component).setBorder(value);
        } else {
            throw new UnsupportedOperationException(getClass().getSimpleName());
        }
    }

    public void setToolTip(String value) {
        if (component instanceof JComponent) {
            ((JComponent) component).setToolTipText(value);
        } else {
            throw new UnsupportedOperationException(getClass().getSimpleName());
        }
    }

    //--------------------------------------------------------------------------------
    // From Swing JFrame
    //--------------------------------------------------------------------------------

    protected Component component;
}
