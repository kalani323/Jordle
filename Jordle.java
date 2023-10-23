import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
/**
 * HW08 : Jordle.java .
 * @author kalanidissanayake
 * @version 1
 */
public class Jordle extends Application {
    private String word;
    /**
     * genrates a random word using Words.list .
     * @return a a random word
     */
    public String generateWord() {
        int index = (int) (Math.random() * Words.list.size());
        return Words.list.get(index);
    }
    /**
     * starts and runs the program .
     * @param s : the initial stage
     */
    public void start(Stage s) {
        word = generateWord();
        BorderPane border = new BorderPane(); Label title = new Label("Jordle");
        title.setFont(new Font("Arial", 40));
        border.setTop(title); border.setAlignment(title, Pos.CENTER);
        StackPane[][] sp = new StackPane[5][6]; Rectangle[][] rs = new Rectangle[5][6]; Text[][] txt = new Text[5][6];
        GridPane grid = new GridPane();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                sp[i][j] = new StackPane();
                txt[i][j] = new Text();
                rs[i][j] = new Rectangle(50, 50);
                rs[i][j].setFill(Color.WHITE);
                rs[i][j].setStroke(Color.BLACK);
                sp[i][j].getChildren().add(rs[i][j]);
                sp[i][j].getChildren().add(txt[i][j]);
            }
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                grid.add(sp[i][j], i, j);
            }
        }
        grid.setPadding(new Insets(50, 50, 50, 50));
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(15);
        grid.setVgap(15);
        border.setCenter(grid);
        GridPane gridbottom = new GridPane();
        Button restartbutton = new Button("Restart");
        Text txtMessage = new Text();
        txtMessage.setText("Try guessing?");
        Button instructionsbutton = new Button("Instructions");
        StackPane spbottom1 = new StackPane();
        StackPane spbottom2 = new StackPane();
        spbottom1.getChildren().add(instructionsbutton);
        spbottom2.getChildren().add(restartbutton);
        gridbottom.add(txtMessage, 0, 0);
        gridbottom.add(spbottom1, 1, 0);
        gridbottom.add(spbottom2, 2, 0);
        gridbottom.setPadding(new Insets(50, 50, 50, 50));
        gridbottom.setAlignment(Pos.CENTER);
        gridbottom.setHgap(15);
        gridbottom.setVgap(15);
        border.setBottom(gridbottom);
        instructionsbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Label secondLabel = new Label("How do I play? Wordle is simple:\nYou have six chances to guess"
                    + " the day's secret five-letter word.\n"
                    + "Type in a word as a guess, and the game tells you which letters are or aren't in the word."
                    + "\nThe aim is to figure out the secret word with the fewest guesses."
                    + "\nIf the correct letter is in the correct place, the cell will turn green"
                    + "\nIf the correct letter is in the wrong place, the cell will turn yellow"
                    + "\nIf the wrong letter is in the wrong place, the cell will turn grey");
                StackPane secondaryLayout = new StackPane();
                secondaryLayout.getChildren().add(secondLabel);
                Scene secondScene = new Scene(secondaryLayout, 500, 300);
                Stage newWindow = new Stage();
                newWindow.setTitle("Instructions");
                newWindow.setScene(secondScene);
                newWindow.setX(s.getX() + 200); newWindow.setY(s.getY() + 100);
                newWindow.show();
                grid.requestFocus();
            }
        });
        Scene scene = new Scene(border, 700, 700);
        s.setScene(scene);
        s.setTitle("Jordle");
        s.show();
        int[] index = {0, 0}; // 0th value in index = col index &  1st value in index = row index
        grid.setOnKeyPressed(e -> {
            boolean tryToGuess = true;
            boolean lost = false;
            boolean won = false;
            if (e.getCode() == KeyCode.ENTER) {
                if (tryToGuess) {
                    if (index[0] < 5) {
                        Alert alert = new Alert(AlertType.WARNING);
                        alert.setTitle("Warning");
                        alert.setHeaderText("The word must be 5 characters long!");
                        alert.setContentText("Add more letters.");
                        alert.showAndWait();
                    } else {
                        for (int i = 0; i < 5; i++) {
                            if (word.toUpperCase().charAt(i) == txt[i][index[1]].getText().charAt(0)) {
                                rs[i][index[1]].setFill(Color.GREEN);
                            } else if ((word.toUpperCase()).contains(txt[i][index[1]].getText())) {
                                rs[i][index[1]].setFill(Color.YELLOW);
                            } else {
                                rs[i][index[1]].setFill(Color.GREY);
                            }
                        }
                        if (rs[0][index[1]].getFill() == Color.GREEN && rs[1][index[1]].getFill() == Color.GREEN
                            && rs[2][index[1]].getFill() == Color.GREEN && rs[3][index[1]].getFill() == Color.GREEN
                            && rs[4][index[1]].getFill() == Color.GREEN) {
                            won = true; tryToGuess = false;
                        } else if (index[1] == 5 && !won) {
                            lost = true; tryToGuess = false;
                        } else {
                            index[1]++; index[0] = 0;
                        }
                    }
                }
            } else if (e.getCode() == KeyCode.BACK_SPACE) {
                if (index[0] != 0 && tryToGuess) {
                    index[0]--; txt[index[0]][index[1]].setText(" ");
                }
            } else {
                if (tryToGuess) {
                    if (index[0] < 5 && index[1] < 6) {
                        tryToGuess = true; String input = e.getCode().toString();
                        if (Character.isLetter(input.charAt(0)) && input.length() == 1) {
                            txt[index[0]][index[1]].setText(input); index[0]++;
                        }
                    }
                }
            } // end else
            if (tryToGuess) {
                txtMessage.setText("Try guessing?");
            } else if (lost) {
                txtMessage.setText("Game Over. The Word was " + word);
            } else if (won) {
                txtMessage.setText("Congratulations! You've guessed the word");
            }
        });
        restartbutton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                String newWord = generateWord();
                word = newWord;
                index[0] = 0;
                index[1] = 0;
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 6; j++) {
                        rs[i][j].setFill(Color.WHITE); rs[i][j].setStroke(Color.BLACK); txt[i][j].setText(" ");
                    }
                }
                grid.requestFocus();
            }
        });
        grid.requestFocus();
    }
}
