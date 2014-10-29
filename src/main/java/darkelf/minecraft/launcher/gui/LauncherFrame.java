package darkelf.minecraft.launcher.gui;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.swing.*;
import java.awt.*;

/**
 * JFrame class for the launcher window. Replaces the @c net.ftb.gui.LaunchFrame class in functionality.
 *
 * Created by evan on 10/25/14.
 */
public class LauncherFrame extends JFrame {

    public final JsonObject config; // = GuiUtils.loadJson("gui/launcher.json").getAsJsonObject();

    public LauncherFrame(JsonElement cfg) {
        // save configuration
        this.config = cfg.getAsJsonObject();

        // initialize components
        bottomPanel  = (JPanel) GuiUtils.initWithJson(new JPanel(), config.get("bottom-panel"));
        supportPanel = (JPanel) GuiUtils.initWithJson(new JPanel(), config.get("support-panel"));
        controlPanel = (JPanel) GuiUtils.initWithJson(new JPanel(), config.get("control-panel"));
        editButton   = (JButton) GuiUtils.initWithJson(new JButton(), config.get("edit-button"));
        launchButton = (JButton) GuiUtils.initWithJson(new JButton(), config.get("launch-button"));
        profileSelectBox = (JComboBox) GuiUtils.initWithJson(new JComboBox(), config.get("profile-select-box"));

        tabbedView = (JTabbedPane) GuiUtils.initWithJson(new JTabbedPane(), config.get("tabbed-view"));
        newsTab = new JPanel();
        optionsTab = new JPanel();
        modpacksTab = new JPanel();
        thirdPartyTab = new JPanel();
        mapsAndTexturesTab = new JPanel();



        // call init
        init();
    }

    private void init() {
        // initialize frame
        GuiUtils.initWithJson(this, config.get("frame"));
        GuiUtils.initWithJson(getContentPane(), config.get("content-pane"));
        pack();

        // enable exit on close
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // initialize the support panel
        initSupportPanel();

        // wire the layout together
        setLayout(new BorderLayout());
        add(tabbedView, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        tabbedView.add(newsTab);
        tabbedView.add(optionsTab);
        tabbedView.add(modpacksTab);
        tabbedView.add(thirdPartyTab);
        tabbedView.add(mapsAndTexturesTab);
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(supportPanel, BorderLayout.CENTER);
        bottomPanel.add(controlPanel, BorderLayout.EAST);
        controlPanel.setLayout(null);
        controlPanel.add(editButton);
        controlPanel.add(launchButton);
        controlPanel.add(profileSelectBox);
    }


    private void initSupportPanel() {
        // get configuration for support panel
        JsonObject panelConfig = config.getAsJsonObject("support-panel");
        // set support panel layout
        supportPanel.setLayout(new FlowLayout(FlowLayout.LEFT,
                panelConfig.get("layout-h-gap").getAsInt(),
                panelConfig.get("layout-v-gap").getAsInt()));
        // add links to support panel
        for (JsonElement j : panelConfig.getAsJsonArray("links")) {
            // element
            JsonObject labelConfig = config.getAsJsonObject(j.getAsString());
            // create label
            JLabel label = (JLabel) GuiUtils.initWithJson(new JLabel(), labelConfig);
            // add link
            GuiUtils.addLink(label, labelConfig.get("link").getAsString());
            // add label to support panel
            supportPanel.add(label);
        }
    }


    //--------------------------------------------------------------------------------
    // UI elements - ?
    //--------------------------------------------------------------------------------

    // UI elements
    private final JPanel bottomPanel;
    private final JPanel supportPanel;
    private final JPanel controlPanel;
    private final JButton editButton;
    private final JButton launchButton;
    private final JComboBox profileSelectBox;
    private final JTabbedPane tabbedView;
    private final JPanel newsTab;
    private final JPanel optionsTab;
    private final JPanel modpacksTab;
    private final JPanel thirdPartyTab;
    private final JPanel mapsAndTexturesTab;

}
