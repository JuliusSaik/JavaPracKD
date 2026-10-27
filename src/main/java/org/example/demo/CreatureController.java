package org.example.demo;

import jakarta.persistence.EntityManagerFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.demo.classes.Alignment;
import org.example.demo.classes.MagicalCreature;
import org.example.demo.hibernate.GenericHibernate;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class CreatureController implements Initializable {

    public TextField nameField;
    public TextField founderField;
    public ComboBox<Alignment> alignmentBox;
    public DatePicker dateFoundField;
    public Button createButton;
    public CheckBox endangeredCheck;
    public TextArea itemsField;

    private GenericHibernate genericHibernate;
    private Runnable onSaved;

    public void setData(EntityManagerFactory entityManagerFactory, Runnable onSaved){
        this.genericHibernate = new GenericHibernate(entityManagerFactory);
        this.onSaved = onSaved;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        alignmentBox.getItems().addAll(Alignment.values());
    }

    public void createCreature() {
        String[] founderSplit = founderField.getText().split(" ");
        String founderName = founderSplit[0].trim();
        String founderSurname = founderSplit[1].trim();
        MagicalCreature magicalCreature = new MagicalCreature(nameField.getText(), alignmentBox.getValue(), itemsField.getText(), dateFoundField.getValue(), founderName, founderSurname, endangeredCheck.isSelected());
        try {
            genericHibernate.create(magicalCreature);
        } catch (RuntimeException e) {
            new Alert(Alert.AlertType.ERROR, "Įrašo išsaugoti nepavyko: " + e.getMessage()).showAndWait();
            return;
        }
        onSaved.run();
        ((Stage) createButton.getScene().getWindow()).close();

        new Thread(()->{
            List<MagicalCreature> magicalCreatureList = genericHibernate.getAll(MagicalCreature.class);
            Collections.sort(magicalCreatureList);
            try {
                FileUtils.writeToBinaryFile(magicalCreatureList, "dataFile");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            for(MagicalCreature m : magicalCreatureList){
                System.out.println(m);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }).start();
    }
}
