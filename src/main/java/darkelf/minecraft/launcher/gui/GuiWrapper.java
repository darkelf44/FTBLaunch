package darkelf.minecraft.launcher.gui;

import darkelf.util.ClassUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A Class that's a super-interface of most swing components. It's used to automatically set properties of various
 * swing objects. It only contains properties that are well suited for being automatically set by a config file.
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
        if (! (component instanceof JFrame)) {
            component.setVisible(value);
        }
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
            component.setBounds(value);
            component.setPreferredSize(value.getSize());
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

    public void setIcon(String value) {
        if (component instanceof Frame) {
            ((Frame) component).setIconImage(Toolkit.getDefaultToolkit().createImage(ClassUtils.getResource(value)));
        } else {
            throw new UnsupportedOperationException(getClass().getSimpleName());
        }
    }

    public void setResizable(boolean value) {
        if (component instanceof Frame) {
            ((Frame) component).setResizable(value);
        } else {
            throw new UnsupportedOperationException(getClass().getSimpleName());
        }
    }

    public void setTitle(String value) {
        if (component instanceof Frame) {
            ((Frame) component).setTitle(value);
        } else {
            throw new UnsupportedOperationException(getClass().getSimpleName());
        }
    }

    //--------------------------------------------------------------------------------
    // From JLabel and JButton
    //--------------------------------------------------------------------------------

    public void setImage(String image) {
        if (component instanceof JLabel) {
            ((JLabel) component).setIcon(new ImageIcon(ClassUtils.getResource(image)));
        } else if (component instanceof AbstractButton) {
            ((AbstractButton) component).setIcon(new ImageIcon(ClassUtils.getResource(image)));
        } else {
            throw new UnsupportedOperationException(getClass().getSimpleName());
        }
    }

    public void setDisabledImage(String image) {
        if (component instanceof JLabel) {
            ((JLabel) component).setDisabledIcon(new ImageIcon(ClassUtils.getResource(image)));
        } else if (component instanceof AbstractButton) {
            ((AbstractButton) component).setDisabledIcon(new ImageIcon(ClassUtils.getResource(image)));
        } else {
            throw new UnsupportedOperationException(getClass().getSimpleName());
        }
    }

    public void setText(String text) {
        if (component instanceof  JLabel) {
            ((JLabel) component).setText(text);
        } else if (component instanceof AbstractButton) {
            ((AbstractButton) component).setText(text);
        } else {
            throw new UnsupportedOperationException(getClass().getSimpleName());
        }
    }


    protected Component component;
}
