package sample;


import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import process.CreateMessage;
import process.ExcelReader;
import process.PropertyUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

public class Controller {
    public Button button;
    public Pane pane;
    public TextField from;
    public TextField to;
    public CheckBox chck;
//    public GridPane gridpane;
    public AnchorPane anchorPane;
    public Label lfrom;
    public Label filename;
    public Task task;
    public List<String> wagonlist;
    public ProgressBar pbar;
    public Label plabel;
    public ListView listid;

    File selectedFile;

    public void handleCheck(){
        if (chck.isSelected()){
            System.out.println("checked");
            from.setVisible(false);
            to.setVisible(false);
        } else{
            System.out.println("unchecked");
            from.setVisible(true);
            to.setVisible(true);
        }
    }

    public void handleClick(ActionEvent event) throws IOException, InterruptedException {
        PropertyUtil putil=new PropertyUtil();
        Properties properties=putil.getProperties("conf.properties");
        if(!properties.getProperty("excel-file-path").isEmpty()){
            selectedFile=new File(Paths.get(properties.getProperty("excel-file-path")).toString());
        } else{
            selectedFile=null;
        }

        if(selectedFile!=null){
            button.setDisable(true);
            plabel.setText("Обработка...");

            pbar.progressProperty().unbind();
            pbar.setProgress(0.0);
            task=createWorker();

            pbar.progressProperty().bind(task.progressProperty());
            task.messageProperty().addListener(new ChangeListener<String>() {
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    System.out.println(newValue);
                    plabel.setText(newValue);
                }
            });
            new Thread(task).start();
        } else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("");
            alert.setContentText("Выберите файл");

            alert.showAndWait();
        }

    }

    public void fileChooseClick(ActionEvent event) {
        PropertyUtil putil=new PropertyUtil();
        Properties properties=putil.getProperties("conf.properties");
        FileChooser fileChooser = new FileChooser();
        Stage stage=(Stage)anchorPane.getScene().getWindow();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);
        selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile!=null){

            filename.setText("Файл: "+selectedFile.toPath().toString());
            try {
                putil.updateValue("excel-file-path",selectedFile.toPath().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Task createWorker() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                ExcelReader er=new ExcelReader();
                wagonlist=er.numwag(Integer.parseInt(from.getText()),Integer.parseInt(to.getText()),selectedFile.toPath().toString());
                for (int i = 0; i < wagonlist.size(); i++) {
                    String wag=wagonlist.get(i);
                    if (CreateMessage.createFile(wag,"140520","300520")) {
                        Thread.sleep(5000);
                        updateProgress(i + 1, wagonlist.size());
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                listid.getItems().add("Сообщение для вагона № "+wag+" успешно создано");
                            }
                        });

                    }

                }
                Platform.runLater(() -> {
                    button.setDisable(false);
                    plabel.setText("Завершено!");

                });
                return true;
            }
        };
    }
}
