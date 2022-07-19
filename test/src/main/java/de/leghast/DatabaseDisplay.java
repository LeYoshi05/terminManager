package de.leghast;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.Collection;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.swing.*;
import java.awt.event.ActionListener;

public class DatabaseDisplay implements Initializable {

    private static Scene scene;

    @FXML
    MenuItem menuAccountDelete;
    @FXML
    MenuItem menuQuit;
    @FXML
    MenuItem programInfo;
    @FXML
    MenuItem menuContact;
    @FXML
    Menu optionsMenu;
    @FXML
    Menu menuHelp;
    @FXML
    TableView<Kunde> table;
    @FXML
    TableColumn<Kunde, String> tableFirstName;
    @FXML
    TableColumn<Kunde, String> tableLastName;
    @FXML
    TableColumn<Kunde, String> tablePhone;
    @FXML
    TableColumn<Kunde, String> tableMail;
    @FXML
    TableColumn<Kunde, String> tableDate;

    @FXML
    MenuBar menuBar;

    @FXML
    private void CreateContact(ActionEvent actionEvent) throws IOException {

    }

    private Connection connection;

    public void run(Stage stage) throws IOException {
        scene = new Scene(loadFXML("viewPane"));
        stage.setScene(scene);
        stage.setTitle("Kundenmanager");
        stage.show();
        connectToDatabase();
        getElements(scene, stage);
        runSQL();
    }

    public void getElements(Scene scene, Stage stage) {
        menuBar = (MenuBar) scene.lookup("#menuBar");
        table = (TableView<Kunde>) scene.lookup("#table");
        tableFirstName = (TableColumn<Kunde, String>) table.getColumns().get(0);
        tableLastName = (TableColumn<Kunde, String>) table.getColumns().get(1);
        tablePhone = (TableColumn<Kunde, String>) table.getColumns().get(2);
        tableMail = (TableColumn<Kunde, String>) table.getColumns().get(3);
        tableDate = (TableColumn<Kunde, String>) table.getColumns().get(4);

        optionsMenu = menuBar.getMenus().get(0);
        menuHelp = menuBar.getMenus().get(1);

        menuAccountDelete = optionsMenu.getItems().get(0);
        menuQuit = optionsMenu.getItems().get(1);

        menuContact = menuHelp.getItems().get(1);
        programInfo = menuHelp.getItems().get(0);

        menuQuit.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                System.exit(0);
            }
        });

        menuAccountDelete.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                JFrame frame = new JFrame("Wirklich löschen?");
                JLabel label = new JLabel("Wollen Sie den Account wirklich löschen?");
                JButton button = new JButton("Bestätigen");
                frame.add(label);
                frame.add(button);
                frame.setLayout(null);
                frame.setSize(640, 480);

                label.setBounds(200, 120, 240, 60);
                button.setBounds(270, 400, 140, 40);

                frame.setVisible(true);
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent e) {
                        File f = new File("./test/src/main/resources/de/leghast/saves/databaseConn.42");
                        File g = new File("./test/src/main/resources/de/leghast/saves/firstRun.42");
                        if (f.exists())
                            f.delete();
                        if (g.exists())
                            g.delete();
                        System.exit(0);
                    }
                });

            }
        });
        menuContact.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try {
                    loadContact(scene, stage);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        });
        programInfo.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try {
                    loadInfo(scene, stage);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        tableFirstName.setCellValueFactory(new PropertyValueFactory<Kunde, String>("firstName"));
        tableLastName.setCellValueFactory(new PropertyValueFactory<Kunde, String>("lastName"));
        tableMail.setCellValueFactory(new PropertyValueFactory<Kunde, String>("email"));
        tablePhone.setCellValueFactory(new PropertyValueFactory<Kunde, String>("telefon"));
        tableDate.setCellValueFactory(new PropertyValueFactory<Kunde, String>("date"));

        /*
         * Kunde kunde = new Kunde("Max", "Musterfrau", "3@42.69", "34269420",
         * "16.73.6323; 92:23");
         * Kunde kunde2 = new Kunde("Marie", "Mustermann", "3420@42.69", "42369420",
         * "16.73.6923; 93:23");
         * table.getItems().add(kunde);
         * table.getItems().add(kunde2);
         */
    }

    private void loadInfo(Scene scene, Stage stage) throws IOException {
        scene = new Scene(loadFXML("info"));
        stage.setScene(scene);
        stage.setTitle("Programminfo");
        stage.show();

        Button backButton = (Button) scene.lookup("#button");
        backButton.addEventHandler(MouseEvent.MOUSE_CLICKED, EventHandler(stage));

    }

    private void loadContact(Scene scene, Stage stage) throws IOException {
        scene = new Scene(loadFXML("contact"));
        stage.setScene(scene);
        stage.setTitle("Kontakt");
        stage.show();

        Button backButton = (Button) scene.lookup("#button");
        backButton.addEventHandler(MouseEvent.MOUSE_CLICKED, EventHandler(stage));

    }

    public EventHandler<Event> EventHandler(Stage stage) {
        EventHandler<Event> handler = new EventHandler<Event>() {

            @Override
            public void handle(Event event) { // Wenn LOGIN-Knopf gedrückt: Passwort-Check!
                try {
                    run(stage);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        return handler;
    }

    public void connectToDatabase() {
        try {
            // Laden der Treiberklasse
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Verbindung herstellen
            BufferedReader b = new BufferedReader(
                    new FileReader(new File("./test/src/main/resources/de/leghast/saves/databaseConn.42")));

            String webaddress = b.readLine();
            String pUsername = b.readLine();
            String pPassword = b.readLine();

            b.close();

            connection = DriverManager.getConnection("jdbc:mysql://" + webaddress +
                    "?allowPublicKeyRetrieval=true",
                    pUsername,
                    pPassword);
            // connection =
            // DriverManager.getConnection("jdbc:mysql://leghast.lima-db.de:3306/db_416692_6",
            // "USER416692_dates", "!LGClient2");
        } catch (Exception e) {
            String message = e.getMessage();
            System.out.println(message);
        }
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub

    }

    public void runSQL() {

        String query = "select * from Kunden ORDER BY ID DESC";
        int len;
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            int id = 0;
            boolean first = true;
            while (rs.next()) {
                if (first) {
                    id = rs.getInt("ID");
                    String vorname = rs.getString("Vorname");
                    String nachname = rs.getString("Nachname");
                    String telefon = rs.getString("Telefon");
                    String mail = rs.getString("eMail");
                    String date = rs.getString("Datum");
                    first = false;
                } else {
                    int ids = rs.getInt("ID");
                    String vorname = rs.getString("Vorname");
                    String nachname = rs.getString("Nachname");
                    String telefon = rs.getString("Telefon");
                    String mail = rs.getString("eMail");
                    String date = rs.getString("Datum");
                }
            }
            if (id < 25) {
                len = 25;
            } else {
                len = id;
            }

            query = "select * from Kunden ORDER BY ID ASC";
            try (Statement stemt = connection.createStatement()) {
                ResultSet res = stemt.executeQuery(query);
                while (res.next()) {
                    if (len > 0) {
                        String vorname = res.getString("Vorname");
                        String nachname = res.getString("Nachname");
                        String telefon = res.getString("Telefon");
                        String mail = res.getString("eMail");
                        String date = res.getString("Datum");

                        Kunde k = new Kunde(vorname, nachname, mail, telefon, date);
                        table.getItems().add(k);
                        len--;
                    } else {
                        String vorname = res.getString("Vorname");
                        String nachname = res.getString("Nachname");
                        String telefon = res.getString("Telefon");
                        String mail = res.getString("eMail");
                        String date = res.getString("Datum");
                    }
                }

            } catch (SQLException e) {
                System.out.println(e.getSQLState());
            }
        } catch (SQLException e) {
            System.out.println(e.getSQLState());
        }

    }
}
