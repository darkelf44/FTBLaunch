package darkelf.minecraft.launcher.gui;

/**
 * Entry point of the Better FTB Launcher (TM). Launches the application and completes the wiring of the components.
 *
 * Created by evan on 10/25/14.
 */
public class Main {

    /**
     * Java main method.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        new LauncherFrame().setVisible(true);
    }
}
