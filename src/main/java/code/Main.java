package code;

import code.db.SettingsJDBCTemplate;
import code.ui.MainScreenController;
import code.utils.LoggerManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.flywaydb.core.Flyway;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.File;
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/main_screen.fxml"));
        Pane pane = fxmlLoader.load();
        MainScreenController mainScreenController = fxmlLoader.getController();
        mainScreenController.setParentStage(primaryStage);
        Scene scene = new Scene(pane);
        primaryStage.setTitle(Defines.APP_FULL_NAME);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        processArguments(args);
        checkMigration();
        launch(args);
    }

    private static void checkMigration() {
        Flyway flyway = new Flyway();
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(Defines.BEANS_CONFIG);
        SettingsJDBCTemplate settingsJDBCTemplate = (SettingsJDBCTemplate) applicationContext.getBean("settingsJDBCTemplateId");
        DataSource dataSource = settingsJDBCTemplate.getDataSource();
        flyway.setDataSource(dataSource);
        flyway.migrate();
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
