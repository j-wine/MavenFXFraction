/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.ggnet.mavenfxfraction.fractionfx;

import eu.ggnet.mavenfxfraction.fraction.Log;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author jacob.weinhold
 * Custom ListCell displaying an image and the id(+1) of list element
 */
public class FractionLogCell extends ListCell<Log> {

    @FXML
    private ImageView logImage;

    @FXML
    private Label logLabel;

    @FXML
    private AnchorPane anchorPane;

    private FXMLLoader mLLoader;

    public FractionLogCell()
    {
        mLLoader = new FXMLLoader(getClass().getResource("FractionLogCell.fxml"));
        mLLoader.setController(this);
        try
        {
            mLLoader.load();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(Log item, boolean empty)
    {
        super.updateItem(item, empty);
        if (empty || item == null)
        {
            setGraphic(null);
            return;
        }

        File file = new File("C:\\Users\\jacob.weinhold\\Desktop\\Java Sammlung\\FractionFXGUI\\FXGUI\\ressources\\logIcon.png");
        Image image = new Image(file.toURI().toString());
        logImage.setImage(image);
        
        logLabel.setText("Log Nr: " + (getIndex() +1));
        setGraphic(anchorPane);

    }

}
