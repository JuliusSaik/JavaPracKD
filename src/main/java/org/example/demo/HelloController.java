package org.example.demo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.demo.classes.Alignment;
import org.example.demo.classes.MagicalCreature;
import org.example.demo.hibernate.GenericHibernate;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("magicalCreatures");
    private final GenericHibernate genericHibernate = new GenericHibernate(entityManagerFactory);
    public ListView<MagicalCreature> magicalList;
    public TextField nameField;
    public TextField founderField;
    public DatePicker dateFoundPicker;
    public ComboBox<Alignment> alignmentBox;
    public Button filterButton;
    public Button createButton;
    public Button tableviewButton;


    public void closeDatabase() {
        genericHibernate.close();
        if (entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        alignmentBox.getItems().addAll(Alignment.values());
        List<MagicalCreature> magicalCreatureList = genericHibernate.getAll(MagicalCreature.class);
        if (magicalCreatureList.isEmpty()) {
            for (MagicalCreature creature : FileUtils.readFromFile("data.txt")) {
                genericHibernate.create(creature);
            }
            magicalCreatureList = genericHibernate.getAll(MagicalCreature.class);
        }
        magicalList.getItems().addAll(magicalCreatureList);
    }

    public void filterData() {
        List<MagicalCreature> filtered = genericHibernate.findCreatures((founderField.getText().split(" "))[0], nameField.getText(), alignmentBox.getValue(), dateFoundPicker.getValue());
        magicalList.getItems().clear();
        magicalList.getItems().addAll(filtered);
    }

    public void openCreationForm() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("creature-form.fxml"));
        Parent parent = fxmlLoader.load();
        CreatureController creatureController = fxmlLoader.getController();
        creatureController.setData(entityManagerFactory,
                () -> magicalList.getItems().setAll(genericHibernate.getAll(MagicalCreature.class)));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public void openTableview() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("tableview.fxml"));
        Parent parent = fxmlLoader.load();
        tableviewController tableviewController = fxmlLoader.getController();
        tableviewController.setData(entityManagerFactory);
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
