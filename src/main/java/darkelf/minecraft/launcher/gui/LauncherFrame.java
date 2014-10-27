package darkelf.minecraft.launcher.gui;

import com.google.gson.JsonObject;
import darkelf.util.ClassUtils;
import net.ftb.data.LauncherStyle;
import net.ftb.download.Locations;
import net.ftb.locale.I18N;
import net.ftb.util.OSUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * JFrame class for the launcher window. Replaces the @c net.ftb.gui.LaunchFrame class in functionality.
 *
 * Created by evan on 10/25/14.
 */
public class LauncherFrame extends JFrame {

    public static final JsonObject config = GuiUtils.loadJsonFromResource("gui/launcher.json").getAsJsonObject();

    public LauncherFrame() {

        // initialize the underlying JFrame
        init();

        // initialize the internal layout of the JFrame
//        initLayout();
    }

    private void init() {
        // Initialize frame
        GuiUtils.initWithJson(this, config.get("launcher-frame").getAsJsonObject());
        pack();

        // set title
        setTitle("Better FTB Launcher - Version 0.1 (FTB: 1.4.4)");

        // set panel color TODO: add to json
        bottomPanel.setBackground(LauncherStyle.getCurrentStyle().footerColor);

        // wire the layout together
        add(bottomPanel);
        add(tabbedView);
        bottomPanel.add(supportPanel);
        bottomPanel.add(controlPanel);
        controlPanel.add(editButton);
        controlPanel.add(launchButton);
        controlPanel.add(profileSelectBox);
    }

    // build up the layout of the frame, and save all components into the corresponding instance variables
    private void initLayout() {
/*
        // build tabViewPanel
        tabViewPanel.setLayout(null);
        tabViewPanel.setBounds(Config.Launcher.TabView.BOUNDS);

        // build footerPanel
        footerPanel.setLayout(null);
        footerPanel.setBounds(Config.Launcher.Footer.BOUNDS);
        footerPanel.setBackground(LauncherStyle.getCurrentStyle().footerColor);

        // build footerPanel links
        footerLinkPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
        footerLinkPanel.setBounds(Config.Launcher.FooterLink.BOUNDS);
        footerLinkPanel.setBackground(LauncherStyle.getCurrentStyle().footerColor);
        footerLinkPanel.add(makeImageLink(Locations.FTBLOGO.substring(1), Locations.FTBSITE)); // FIXME: remove leading slash
        footerLinkPanel.add(makeImageLink(Locations.CHLOGO.substring(1), "http://billing.creeperhost.net/link.php?id=2"));
        footerLinkPanel.add(makeImageLink(Locations.TUGLOGO.substring(1), "http://feed-the-beast.com/tug"));

        // make donate button
        donateButton.setText(I18N.getLocaleString("DONATE_BUTTON"));
        donateButton.setBounds(Config.Launcher.Donate.BOUNDS);
        donateButton.setEnabled(false);
        donateButton.setToolTipText("Coming soon ..."); // TODO: Use i18n for the tooltip

        // make edit button
        editButton.setText(I18N.getLocaleString("EDIT_BUTTON"));
        editButton.setBounds(Config.Launcher.Edit.BOUNDS);

        // make launch button
        launchButton.setText(I18N.getLocaleString("LAUNCH_BUTTON"));
        launchButton.setBounds(Config.Launcher.Launch.BOUNDS);

        // make profile combo box
        profileComboBox.setBounds(Config.Launcher.Profile.BOUNDS);

        // wire up layout
        footerPanel.add(footerLinkPanel);
        footerPanel.add(donateButton);
        footerPanel.add(editButton);
        footerPanel.add(launchButton);
        footerPanel.add(profileComboBox);
        add(tabViewPanel);
        add(footerPanel);*/
    }

    private static JLabel makeImageLink(String image, final String link) {
        // create label
        JLabel label = new JLabel(new ImageIcon(ClassUtils.getResource(image)));
        // fix dimensions
        Dimension iconSize = new Dimension(label.getIcon().getIconWidth(), label.getIcon().getIconHeight());
        // disable resizing the JLabel by LayoutManagers
        label.setMinimumSize(iconSize);
        label.setMaximumSize(iconSize);
        label.setPreferredSize(iconSize);
        // change cursor
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // add mouse listener
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    OSUtils.browse(link);
                }
            }
        });
        // return the label
        return label;
    }

    //--------------------------------------------------------------------------------
    // UI elements - ?
    //--------------------------------------------------------------------------------

    // UI elements
    private final JPanel bottomPanel
            = (JPanel) GuiUtils.initWithJson(new JPanel(), config.get("bottom-panel").getAsJsonObject());
    private final JPanel supportPanel
            = (JPanel) GuiUtils.initWithJson(new JPanel(), config.get("support-panel").getAsJsonObject());
    private final JPanel controlPanel
            = (JPanel) GuiUtils.initWithJson(new JPanel(), config.get("control-panel").getAsJsonObject());
    private final JButton editButton
            = (JButton) GuiUtils.initWithJson(new JButton(), config.get("edit-button").getAsJsonObject());
    private final JButton launchButton
            = (JButton) GuiUtils.initWithJson(new JButton(), config.get("launch-button").getAsJsonObject());
    private final JComboBox profileSelectBox =
            (JComboBox) GuiUtils.initWithJson(new JComboBox(), config.get("profile-select-box").getAsJsonObject());
    private final JTabbedPane tabbedView
            = (JTabbedPane) GuiUtils.initWithJson(new JTabbedPane(), config.get("tabbed-view").getAsJsonObject());

    // UI controls

}
