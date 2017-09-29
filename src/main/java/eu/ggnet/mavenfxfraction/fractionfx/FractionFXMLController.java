/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.ggnet.mavenfxfraction.fractionfx;

import eu.ggnet.mavenfxfraction.fraction.ArithmeticData;
import eu.ggnet.mavenfxfraction.fraction.Fraction;
import eu.ggnet.mavenfxfraction.fraction.Log;
import java.io.*;

import java.net.URL;
import java.util.*;
import java.util.concurrent.*;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FXML Controller class
 *
 * @author jacob.weinhold
 */
public class FractionFXMLController implements Initializable {

    private final static Logger Log = LoggerFactory.getLogger(FractionFXMLController.class);
    private ArithmeticData arithmeticData = null;
    @FXML
    private Label bruch1Label;
    @FXML
    private TextField zaehlerEins;
    @FXML
    private TextField nennerEins;
    @FXML
    private Label bruch2Label;
    @FXML
    private TextField zaehlerZwei;
    @FXML
    private TextField nennerZwei;
    @FXML
    private Label resultLabel;
    @FXML
    private TextField resultZaehler;
    @FXML
    private TextField resultNenner;
    @FXML
    private TextArea commentArea;
    @FXML
    private ListView<Log> logList;
    @FXML
    private TextArea logFixOutput;

    @FXML
    private Button saveLogButton;
    @FXML
    private Button useLogButton;
    @FXML
    private Button addButton;
    @FXML
    private Button subButton;
    @FXML
    private Button multiplyButton;
    @FXML
    private Button divideButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button clearButton;
    @FXML
    private MenuItem editLogMenuButton;

    private ExecutorService pool = Executors.newWorkStealingPool();
    @FXML
    private MenuItem deleteLogMenuItem;
    @FXML
    private MenuItem exporttoCSVMenuItem;
    @FXML
    private AnchorPane anchorPane;
    private ProgressBar progressBar;
    @FXML
    private MenuItem importCSVMenuItem;

    /**
     * Initializes the controller class. initialize and set ProgressBar for
     * calculation task and ChangeListener for Fraction textfield inputs
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {

        //set custom listViewCell
        logList.setCellFactory(customCell -> new FractionLogCell());
        logList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        //initialize ProgressBar manually so we spare the FXML Doc from having it blocking the view on other elements
        //location is set on between @resultZaehler and @resultNenner fields
        progressBar = new ProgressBar();
        progressBar.setVisible(false);
        anchorPane.getChildren().add(progressBar);
        progressBar.setMinWidth(149);
        progressBar.setMinHeight(25);
        progressBar.setLayoutX(14);
        progressBar.setLayoutY(160);

        /**
         * Construct ChangeListener for textfields zaehlerEins, zaehlerZwei,
         * nennerEins, nennerZwei Show Alert if input is NaN, not an int or 0
         * for nennerEins and nennerZwei
         */
        ChangeListener<String> textListener = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                    String oldValue, String newValue)
            {
                StringProperty textProperty = (StringProperty) observable;
                TextField textField = (TextField) textProperty.getBean();

                if (!newValue.isEmpty() && (!(newValue.startsWith("-") && newValue.length() == 1)))
                {

                    try
                    {
                        if (Long.valueOf(newValue) > Integer.MAX_VALUE || Long.valueOf(newValue) < Integer.MIN_VALUE)
                        {
                            throw new NumberFormatException();
                        }

                        if ((textField == nennerEins || textField == nennerZwei) && Integer.valueOf(newValue) == 0)
                        {
                            Alert alert = new Alert(AlertType.ERROR, "ERROR: 0 IN DENOMINATOR!", ButtonType.OK);
                            alert.setTitle("Fraction Input Error");
                            alert.setHeaderText("User Input Error");

                            alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response ->
                            {
                                textField.clear();
                            }
                            );
                        }

                    } catch (NumberFormatException e)
                    {
                        Alert alert = new Alert(AlertType.ERROR, "ERROR: NOT A INTEGER INPUT", ButtonType.OK);
                        alert.setTitle("Fraction Input Error");
                        alert.setHeaderText("User Input Error");
                        alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response ->
                        {

                            textField.clear();
                        }
                        );
                    }
                }

            }

        };

        zaehlerEins.textProperty().addListener(textListener);
        zaehlerZwei.textProperty().addListener(textListener);
        nennerEins.textProperty().addListener(textListener);
        nennerZwei.textProperty().addListener(textListener);
    }

    /**
     * display selected logList element on click
     *
     * @param event click on listView @logList element
     */
    @FXML
    private void handleLogListClickAction(MouseEvent event)
    {
        Log log = logList.getSelectionModel().getSelectedItem();
        try
        {
            logFixOutput.setText(log.toFixOutput());
            commentArea.setText(log.toCommentString());
        } catch (NullPointerException e)
        {
            Log.info("handleLogListClickAction() NullPointerException");
        }
    }

    /**
     * Initialize Log.class object with last calculation and user input in @commentArea, add into @logList,
     * select and display it
     * @param event click on "Save Log" Button
     */
    @FXML
    private void handleSaveLogButtonAction(ActionEvent event)
    {

        try
        {

            Log log = new Log(arithmeticData.getFractionOne(), arithmeticData.getFractionTwo(), arithmeticData.getResult(),
                    arithmeticData.getOperator(), commentArea.getText());

            logList.getItems().add(log);
            logList.getSelectionModel().clearSelection();
            logList.getSelectionModel().select(log);
            int index = logList.getSelectionModel().selectedIndexProperty().get();

            logFixOutput.setText(log.toFixOutput());
            commentArea.setText(log.toCommentString());
            logList.getSelectionModel().select(log);

        } catch (NullPointerException e)
        {
            Log.info("handleSaveLogButtonAction() NullPointerException");
        }
    }

    /**
     *
     * @param event mouse click on "Use Log" button load data of selected Log
     * into Fraction textfields clear result
     */
    @FXML
    private void handleUseLogButtonAction(ActionEvent event)
    {
        int index = logList.getSelectionModel().getSelectedIndex();
        try
        {

            Log data = logList.getSelectionModel().getSelectedItem();

            setFractions(data.getFractionOne(), data.getFractionTwo());
            resultZaehler.clear();
            resultNenner.clear();

        } catch (NullPointerException e)

        {
            Log.info("handleUseLogButtonAction() NullPointerException");
        }

    }

    @FXML
    private void handleAddButtonAction(ActionEvent event)
    {

        calculate('+');

    }

    @FXML
    private void handleSubtractButtonAction(ActionEvent event)
    {
        calculate('-');
    }

    @FXML
    private void handleMultiplyButtonAction(ActionEvent event)
    {
        calculate('*');
    }

    /**
     * Method is called in EventHandlers fired by clicking on calculator
     * buttons. FractionTask.class and Fraction.class contain arithmetic logic.
     * Start FractionTask, bind ProgressBar on it, and disable the pressed
     * button until finished Set Fraction return of FractionTask into
     * resultZaehler and resultNenner textfields. Rewind everything (if failed).
     *
     * @param operator '+', '-', '/', '*'
     */
    private void calculate(char operator)
    {

        FractionCalculationTask task = new FractionCalculationTask(getFractions(), operator);

        task.setOnRunning((evt) ->
        {

            resultZaehler.setVisible(false);
            resultNenner.setVisible(false);
            progressBar.progressProperty().bind(task.progressProperty());
            progressBar.setVisible(true);

            switch (operator)
            {
                case '+':
                    addButton.disableProperty().set(true);
                    break;
                case '-':
                    subButton.disableProperty().set(true);
                    break;
                case '*':
                    multiplyButton.disableProperty().set(true);
                    break;
                case '/':
                    divideButton.disableProperty().set(true);
                    break;

            }

        });
        task.setOnSucceeded((evt) ->
        {
            
            resultZaehler.setVisible(true);
            resultNenner.setVisible(true);
            progressBar.progressProperty().unbind();
            progressBar.setVisible(false);

            switch (operator)
            {
                case '+':
                    addButton.disableProperty().set(false);
                    break;
                case '-':
                    subButton.disableProperty().set(false);
                    break;
                case '*':
                    multiplyButton.disableProperty().set(false);
                    break;
                case '/':
                    divideButton.disableProperty().set(false);
                    break;

            }

            Fraction result = task.getValue();
            setResult(result);
            arithmeticData = new ArithmeticData(getFractions().get(0), getFractions().get(1), result, operator);
        });

        task.setOnFailed((evt) ->
        {
            Log.info("FractionTask failed");
            resultZaehler.setVisible(true);
            resultNenner.setVisible(true);
            progressBar.progressProperty().unbind();
            progressBar.setVisible(false);

            switch (operator)
            {
                case '+':
                    addButton.disableProperty().set(false);
                    break;
                case '-':
                    subButton.disableProperty().set(false);
                    break;
                case '*':
                    multiplyButton.disableProperty().set(false);
                    break;
                case '/':
                    divideButton.disableProperty().set(false);
                    break;

            }
        });
        pool.execute(task);
    }

    @FXML
    private void handleDivideButtonAction(ActionEvent event)
    {
        calculate('/');
    }

    /**
     * Parse all possible textfield
     * representations of Fraction objects, invoke cancel() if possible and
     * override textfields
     * @param event mouse click on "Cancel" Button parse 
     */
    @FXML
    private void handleCancelButtonAction(ActionEvent event)
    {

        if (getFractionOne() != null)
        {
            zaehlerEins.setText(getFractionOne().cancel().getZaehler() + "");
            nennerEins.setText(getFractionOne().cancel().getNenner() + "");
        }

        if (getFractionTwo() != null)
        {
            zaehlerZwei.setText(getFractionTwo().cancel().getZaehler() + "");
            nennerZwei.setText(getFractionTwo().cancel().getNenner() + "");
        }

        if (getResult() != null)
        {
            setResult(getResult().cancel());
        }

    }

    @FXML
    private void handleClearButtonAction(ActionEvent event)
    {
        clearAllFields();

    }

    private void clearInputFields()
    {

        zaehlerEins.clear();
        zaehlerZwei.clear();
        nennerEins.clear();
        nennerZwei.clear();
    }

    private void clearAllFields()
    {
        zaehlerEins.clear();
        zaehlerZwei.clear();
        nennerEins.clear();
        nennerZwei.clear();
        resultZaehler.clear();
        resultNenner.clear();
    }
/**
 * 
 * @return Fraction initialized with resultZaehler and resultNenner textfield contents
 */
    private Fraction getResult()
    {
        try
        {
            return new Fraction(Integer.valueOf(resultZaehler.getText()), Integer.valueOf(resultNenner.getText()));
        } catch (NumberFormatException e)
        {
            Log.info("getResult() NumberFormatException");
        }

        return null;

    }

    /**
     * Overwrite textfields resultZaehler and resultNenner.
     *
     * @param result desired Fraction to display in result textfields
     */
    private void setResult(Fraction result)
    {
        resultZaehler.setText(result.getZaehler() + "");
        resultNenner.setText(result.getNenner() + "");

    }

    /**
     * Parse fields @zaehlerEins and @nennerEins to initialize and return the
     * represented Fraction instance.
     *
     * @return Fraction or null if parsing went wrong
     */
    private Fraction getFractionOne()
    {
        try
        {
            Fraction one = new Fraction(Integer.valueOf(zaehlerEins.getText()), Integer.valueOf(nennerEins.getText()));

            return one;
        } catch (NumberFormatException e)
        {
            Log.info("getFractionOne() NumberFormatException");
        }
        Log.info("getFractionOne() returns null");
        return null;
    }

    /**
     * Parse fields @zaehlerZwei and @nennerZwei to initialize and return the
     * represented Fraction instance.
     *
     * @return two Fraction objects or null if parsing went wrong
     */
    private Fraction getFractionTwo()
    {
        try
        {
            Fraction two = new Fraction(Integer.valueOf(zaehlerZwei.getText()), Integer.valueOf(nennerZwei.getText()));

            return two;
        } catch (NumberFormatException e)
        {
            Log.info("getFractionTwo() NumberFormatException");
        }
        Log.info("getFractigetFractionTwoons() returns null");
        return null;
    }

    /**
     * Parse fields @zaehlerEins, @nennerEins and @zaehlerZwei,@nennerZwei to
     * initialize and return two Fraction instantiations.
     *
     * @return two Fraction objects or null if parsing went wrong
     */
    private List<Fraction> getFractions()
    {

        try
        {
            Fraction first = new Fraction(Integer.valueOf(zaehlerEins.getText()), Integer.valueOf(nennerEins.getText()));
            Fraction second = new Fraction(Integer.valueOf(zaehlerZwei.getText()), Integer.valueOf(nennerZwei.getText()));
            List<Fraction> fractionObjects = new LinkedList();
            fractionObjects.add(first);
            fractionObjects.add(second);

            return fractionObjects;
        } catch (NumberFormatException e)
        {
            Log.info("getFractions() NumberFormatException");
        }
        Log.info("getFractions() returns null");
        return null;
    }

    /**
     * Overwrites user input textfields representing Fraction Objects.
     *
     * @param first Fraction fields overwrite textfields zaehlerEins and
     * nennerEins
     * @param second Fraction fields overwrite textfields zaehlerZwei and
     * nennerZwei
     */
    private void setFractions(Fraction first, Fraction second)
    {
        zaehlerEins.setText(first.getZaehler() + "");
        zaehlerZwei.setText(second.getZaehler() + "");
        nennerEins.setText(first.getNenner() + "");
        nennerZwei.setText(second.getNenner() + "");

    }

    /**
     * Overwrite all textfields representing Fraction objects.
     *
     * @param first Fraction fields overwrite textfields zaehlerEins and
     * nennerEins
     * @param second Fraction fields overwrite textfields zaehlerZwei and
     * nennerZwei
     * @param result Fraction fields overwrite textfields resultZaehler and
     * resultNenner
     */
    private void setFractions(Fraction first, Fraction second, Fraction result)
    {
        zaehlerEins.setText(first.getZaehler() + "");
        zaehlerZwei.setText(second.getZaehler() + "");
        nennerEins.setText(first.getNenner() + "");
        nennerZwei.setText(second.getNenner() + "");
        resultZaehler.setText(result.getZaehler() + "");
        resultNenner.setText(result.getNenner() + "");

    }

    /**
     * If there is a listView element selected, remove it, else show alert. Only
     * delete one element at a time.
     *
     * @param event click on "Delete Log" MenuItem in "Edit" Menu in MenuBar
     */
    @FXML
    private void handleDeleteLogAction(ActionEvent event)
    {

        int index = logList.getSelectionModel().getSelectedIndex();
        if (index == -1)
        {
            Alert alert = new Alert(AlertType.ERROR, "ERROR: NO LOG SELECTED", ButtonType.OK);
            alert.setTitle("");
            alert.setHeaderText("No Log Selected Error");
            alert.show();
            return;
        }

        logList.getItems().remove(index);

        logList.getSelectionModel().select(--index);

    }

    /**
     * Write selected elements of @logList into .csv file and prompt user to
     * save the file. See CSVExportTask.class for further information as well as
     * the format specifications.
     *
     * @param event click on "Save Log as CSV" MenuItem in "File" Menu in
     * MenuBar
     */
    @FXML
    private void handleExporttoCSVMenuItemAction(ActionEvent event) throws Exception
    {

        Writer writer = null;

        if (logList.getSelectionModel().getSelectedItem() == null)
        {

            Alert alert = new Alert(Alert.AlertType.ERROR, "ERROR: NO LOG SELECTED", ButtonType.OK);
            alert.setTitle("File not found Error");
            alert.setHeaderText("No Log Selected Error");
            alert.show();

        } else
        {

            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("csv files (*.csv)", "*.csv");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showSaveDialog(anchorPane.getScene().getWindow());

            writer = new BufferedWriter(new FileWriter(file));
            List list = logList.getSelectionModel().getSelectedItems();
            CSVExportTask task = new CSVExportTask(list, writer);
            pool.execute(task);

            task.setOnFailed((evt) ->
            {
                Log.info("CSVExportTask failed");
                Alert alert = new Alert(AlertType.ERROR, "ERROR: EXPORT TO CSV FAIL ", ButtonType.OK);
                alert.setTitle("FAILED TO EXPORT CSV");
                alert.setHeaderText("FILE ERROR");
                alert.setContentText("ERROR");
                alert.showAndWait();

            });

            task.setOnSucceeded((evt) ->
            {
                Alert alert = new Alert(AlertType.INFORMATION, ".csv Export succesfull!", ButtonType.OK);
                alert.setTitle("Succesfull CSV Export");
                alert.setContentText("");
                alert.setHeaderText("Export of .csv file succesfull!");
                alert.show();

            });

        }

    }

    /**
     * Prompt user to select .csv file containing Log Objects, parse it and if
     * succesfull add into listView @logList. See CSVImportTask.class for
     * further information as well as the format specifications.
     *
     * @param event click on "import .csv" menuItem in "File" Menu in MenuBar
     */
    @FXML
    private void handleimportCSVMenuItemAction(ActionEvent event)
    {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("csv files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(anchorPane.getScene().getWindow());

        CSVImportTask task = new CSVImportTask(file);

        pool.execute(task);
        task.setOnSucceeded((evt) ->
        {
            Log.info("Task succeeded");
            logList.getItems().addAll(task.getValue());
        });

    }

    /**
     * @param event click on "Edit Log" MenuItem in "Edit" Menu in MenuBar if
     * there is a logList element selected, overwrite its comment field with
     * input currently in the commentArea textfield else show alert
     */
    @FXML
    private void handleEditLogMenuButton(ActionEvent event)
    {
        int index = logList.getSelectionModel().getSelectedIndex();
        if (!(index < 0))
        {
            logList.getSelectionModel().getSelectedItem().setComment(commentArea.getText());

        } else
        {
            Alert alert = new Alert(AlertType.ERROR, "ERROR: NO LOG SELECTED", ButtonType.OK);
            alert.setTitle("");
            alert.setHeaderText("No Log Selected Error");
            alert.show();

        }
    }

}
