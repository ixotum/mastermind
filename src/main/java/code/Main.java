package code;

import code.utils.LoggerManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class Main extends Application {

    private static final Logger logger = LoggerManager.getLoggerInstance();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/main_screen.fxml"));
        primaryStage.setTitle(Defines.APP_FULL_NAME);
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        processArguments(args);
        launch(args);
    }

    private static void processArguments(String[] args) {
        Defines.ApplicationMode appMode = getAppMode(args);

        switch (appMode) {
            case VERSION:
                writeAppFullNameToFile();
                System.exit(0);
                break;
        }
    }

    private static void writeAppFullNameToFile() {
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(Defines.VERSION_FILE_NAME);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        printWriter.write(Defines.APP_FULL_NAME);
        printWriter.close();

        logger.info("App full name saved to file: " + Defines.APP_FULL_NAME);
    }

    private static Defines.ApplicationMode getAppMode(String[] args) {
        List<String> argsList = new ArrayList<>();
        Collections.addAll(argsList, args);
        if (argsList.size() == 1 && argsList.get(0).equals("-version")) {
            return Defines.ApplicationMode.VERSION;
        }

        return Defines.ApplicationMode.GENERAL;
    }
}
