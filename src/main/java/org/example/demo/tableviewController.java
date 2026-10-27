package org.example.demo;

import jakarta.persistence.EntityManagerFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.demo.classes.MagicalCreature;
import org.example.demo.hibernate.GenericHibernate;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class tableviewController implements Initializable {
    public TableView<MagicalCreature> creatureWindow;
    public TableColumn<MagicalCreature, Integer> idCol;
    public TableColumn<MagicalCreature, String> nameCol;
    public TableColumn<MagicalCreature, String> founderCol;
    public TableColumn<MagicalCreature, LocalDate> dateCol;

    private GenericHibernate genericHibernate;

    public void setData(EntityManagerFactory entityManagerFactory){
        this.genericHibernate = new GenericHibernate(entityManagerFactory);
        creatureWindow.getItems().setAll(genericHibernate.getAll(MagicalCreature.class));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        creatureWindow.setEditable(true);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        founderCol.setCellValueFactory(new PropertyValueFactory<>("founderName"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateFound"));
    }
}
