package view;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import model.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.Optional;
/**
 * A class operating Manage Board Games Menu
 * @author Simona-Luana Draghici
 * @version 1.0
 */
public class ManageBoardGameController {
  @FXML
  private TextField titleAdd;
  @FXML
  private TextField numberAdd;
  @FXML
  private TextArea descriptionAdd;
  @FXML
  private TextField ownerSearchAdd;
  @FXML
  private ListView<Student> listViewAdd;
  @FXML
  private TextField firstNameAdd;
  @FXML
  private TextField lastNameAdd;
  @FXML
  private TextField VIAIDAdd;
  @FXML
  private Button addButton;

  @FXML
  private TextField titleEdit;
  @FXML
  private TextField numberEdit;
  @FXML
  private TextArea descriptionEdit;
  @FXML
  private TextField searchOwnerEdit;
  @FXML
  private ListView<Student> listViewOwnerEdit;
  @FXML
  private TextField searchGameEdit;
  @FXML
  private ListView<BoardGame> listViewGameEdit;
  @FXML
  private Button editButton;

  @FXML
  private TextField titleRemove;
  @FXML
  private TextField numberRemove;
  @FXML
  private TextArea descriptionRemove;
  @FXML
  private TextField ownerRemove;
  @FXML
  private TextField searchGameRemove;
  @FXML
  private ListView<BoardGame> listViewGameRemove;
  @FXML
  private Button removeButton;

  private ModelManager modelManager;
  private StudentList studentList;
  private BoardGamesList boardGamesList;
  private BoardGame boardGame;
  private Student student;
  private EventList eventList;
  private Scene scene;
  private ViewHandler viewHandler;

  @FXML
  private MenuBar menuBar;
  @FXML
  private Menu menuManage;
  @FXML
  private MenuItem exit;
  @FXML
  private MenuItem menuBoardGames;
  @FXML
  private MenuItem menuBorrowing;
  @FXML
  private MenuItem menuStudents;
  @FXML
  private MenuItem menuEvents;
  @FXML private MenuItem menuUpcomingGames;
  @FXML private TableView<BoardGame> tab;
  @FXML private TableColumn<BoardGame, String> titleTab;
  @FXML private TableColumn<BoardGame, String > numberTab;
  @FXML private TableColumn<BoardGame, String> descriptonTab;
  @FXML private TableColumn<BoardGame, String> ownerTab;

  /**
   * Initialize method called in the ViewHandler to initialize the view
   * @param viewHandler the ViewHandler object
   * @param scene the Scene object
   * @param modelManager the ModelManager object
   */
  public void init(ViewHandler viewHandler, Scene scene, ModelManager modelManager) {
    this.viewHandler = viewHandler;
    this.scene = scene;
    this.modelManager = modelManager;
  }

  /**
   * Reset method called in the ViewHandler that updates all ListViews when the tab is changed
   */
  public void reset()
  {
    updateStudentsList();
    updateBoardGamesList();
  }

  /**
   * Initialize method called once the application is run
   */
  public void initialize() {
    modelManager = new ModelManager("upcoming.bin", "games.bin", "students.bin", "events.bin", "website/xml/eventsWebsite.xml", "website/xml/boardGamesWebsite.xml", "website/xml/upcomingBoardGamesWebsite.xml");
    studentList = modelManager.getAllStudents();
    boardGamesList = modelManager.getAllBoardGames();
    eventList = modelManager.getAllEvents();
    if (listViewAdd != null && listViewOwnerEdit != null) {
      updateStudentsList();
    }
    if (listViewGameEdit != null && listViewGameRemove != null) {
      updateBoardGamesList();
      updateStudentsList();
    }
    if(tab != null){
      tab.getItems().clear();
      EventList eventList = modelManager.getAllEvents();
      titleTab.setCellValueFactory(new PropertyValueFactory<>("name"));
      numberTab.setCellValueFactory(new PropertyValueFactory<>("numberOfPlayers"));
      descriptonTab.setCellValueFactory(new PropertyValueFactory<>("description"));
      ownerTab.setCellValueFactory(new  PropertyValueFactory<>("owner"));
      for(int i = 0; i < boardGamesList.size(); i++){
        tab.getItems().add(boardGamesList.getBoardGameByIndex(i));
      }
    }

  }

  /**
   * Returns the Scene object
   * @return the Scene object
   */
  public Scene getScene() {
    return scene;
  }

  /**
   * Handles all user interactions
   * @param e the ActionEvent object that tells the application user interactions
   */
  public void handleActions(ActionEvent e) {
    if (e.getSource() == menuBoardGames) {
      viewHandler.openView("ManageBoardGames");
    }
    if (e.getSource() == menuBorrowing) {
      viewHandler.openView("ManageBorrowing");
    }
    if (e.getSource() == menuStudents) {
      viewHandler.openView("ManageStudents");
    }
    if (e.getSource() == menuEvents) {
      viewHandler.openView("ManageEvents");
    }
    if(e.getSource() == menuUpcomingGames){
      viewHandler.openView("ManageUpcomingBoardGames");
    }
    if (e.getSource() == exit) {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to exit?", ButtonType.YES, ButtonType.CANCEL);
      alert.setHeaderText(null);
      Optional<ButtonType> result = alert.showAndWait();
      if (result.get() == ButtonType.YES) {
        System.exit(0);
      }
    }
    if (e.getSource() == addButton) {
      if (!titleAdd.getText().equals("") && !numberAdd.getText().equals("") && !descriptionAdd.getText().equals(""))
      {
        String title = titleAdd.getText();
        String numberOfPlayers = numberAdd.getText();
        String description = descriptionAdd.getText();
        student = listViewAdd.getSelectionModel().getSelectedItem();
        if (student != null)
        {
          BoardGame boardGame = new BoardGame(title, numberOfPlayers, description);
          boardGame.setOwner(student);
          modelManager.addBoardGame(boardGame);
          titleAdd.clear();
          numberAdd.clear();
          descriptionAdd.clear();
          Alert alert = new Alert(Alert.AlertType.INFORMATION);
          alert.setHeaderText(null);
          alert.setContentText("The game was added to the system.");
          alert.show();
        }
        else if (!firstNameAdd.getText().equals("") && !lastNameAdd.getText().equals("") && !VIAIDAdd.getText().equals(""))
        {
          String firstName = firstNameAdd.getText();
          String lastName = lastNameAdd.getText();
          int VIAID = 0;
          try
          {
            VIAID = Integer.parseInt(VIAIDAdd.getText());
          }
          catch (IllegalArgumentException exception)
          {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Entered VIA ID should contain only digits.");
            alert.show();
            return;
          }
          Student guest = null;
          try
          {
            guest = new Student(firstName, lastName, VIAID);

          }
          catch (IllegalArgumentException exception)
          {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Entered VIA ID should contain 6 digits.");
            alert.show();
            return;
          }
          StudentList allStudents = modelManager.getAllStudents();
            if (allStudents.sameID(guest)){
              Alert alert = new Alert(Alert.AlertType.INFORMATION);
              alert.setHeaderText(null);
              alert.setContentText("Student is already in the system.");
              alert.show();
              return;
            }
            else
            {
              BoardGame boardGame = new BoardGame(title, numberOfPlayers, description);
              boardGame.setOwner(guest);
              modelManager.addGuest(guest);
              modelManager.addBoardGame(boardGame);
              titleAdd.clear();
              numberAdd.clear();
              descriptionAdd.clear();
              Alert alert = new Alert(Alert.AlertType.INFORMATION);
              alert.setHeaderText(null);
              alert.setContentText("The game was added to the system.");
              alert.show();
            }
        }
        else
        {
          Alert alert = new Alert(Alert.AlertType.INFORMATION);
          alert.setHeaderText(null);
          alert.setContentText("Student data is missing.");
          alert.show();
        }
        updateStudentsList();
        updateBoardGamesList();
        updateWebsite();

      }
      else
      {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("No data entered.");
        alert.show();
      }
    } else if (e.getSource() == editButton) {
      if (boardGame != null && titleEdit.getText() != "" && numberEdit.getText() != "" && descriptionEdit.getText() != "") {
        String title = titleEdit.getText();
        String number = numberEdit.getText();
        String description = descriptionEdit.getText();
        boardGame.setName(title);
        boardGame.setNumberOfPlayers(number);
        boardGame.setDescription(description);
        Student temp = boardGame.getOwner();
        if(student != null){
          boardGame.setOwner(student);
        }
        if(temp.isAMember()==false  && !boardGamesList.isAnOwner(temp)  && !eventList.isAParticipant(temp))
        {
          studentList.removeGuest(temp);
        }
        searchGameEdit.clear();
        titleEdit.clear();
        numberEdit.clear();
        descriptionEdit.clear();
        searchOwnerEdit.clear();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("The game has been edited.");
        alert.show();
      }
      else if (boardGame == null)
      {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("Game is not selected.");
        alert.show();
      }
      else
      {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("No data entered.");
        alert.show();
      }
      modelManager.saveAllStudents(studentList);
      modelManager.saveAllGames(boardGamesList);
      updateStudentsList();
      updateBoardGamesList();
      updateWebsite();
      titleEdit.clear();
      numberEdit.clear();
      descriptionEdit.clear();
      searchOwnerEdit.clear();
    }
    else if (e.getSource() == removeButton) {
      if (boardGame != null) {
        Student borrower = boardGame.getBorrower();
        Student owner = boardGame.getOwner();
        Student[] students = boardGame.getAllReservants();
        boardGamesList.removeGame(boardGame);
        if (owner != null && !owner.isAMember() && !boardGamesList.isABorrower(owner) && !eventList.isAParticipant(owner) && !boardGamesList.isAnOwner(owner)) {
          studentList.removeGuest(owner);
        }
        if (borrower != null && !borrower.isAMember() && !boardGamesList.isABorrower(borrower) && !boardGamesList.isAnOwner(borrower) && !eventList.isAParticipant(borrower)) {
          studentList.removeGuest(borrower);
        }
        for (int i = 0; i < students.length; i++) {
          Student temp = students[i];
          if (!temp.isAMember() && !boardGamesList.isAnOwner(temp) && !boardGamesList.isABorrower(temp) && !eventList.isAParticipant(temp)) {
            studentList.removeGuest(temp);
          }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("The game has been removed.");
        alert.show();
        modelManager.saveAllGames(boardGamesList);
        modelManager.saveAllStudents(studentList);
        updateBoardGamesList();
        updateStudentsList();
        updateWebsite();
        searchGameRemove.clear();
        titleRemove.clear();
        numberRemove.clear();
        descriptionRemove.clear();
        ownerRemove.clear();
        boardGame = null;
        student = null;
      } else {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("Game is not selected.");
        alert.show();
      }
    }
  }

  /**
   * Listener that allows the user to search through the lists
   * @param keyEvent the KeyEvent object that tells the application the keyboard interactions
   */
  public void textChangeListener(KeyEvent keyEvent) {
    if (keyEvent.getSource() == ownerSearchAdd) {
      ownerSearchAdd.textProperty().addListener(new ChangeListener() {
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
          listViewAdd.getItems().clear();

          StudentList students = modelManager.containStudents(ownerSearchAdd.getText());
          for (int i = 0; i < students.size(); i++) {
            listViewAdd.getItems().add(students.getStudentByIndex(i));
          }
        }
      });
    }
    if (keyEvent.getSource() == searchGameEdit) {
      searchGameEdit.textProperty().addListener(new ChangeListener() {
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
          listViewGameEdit.getItems().clear();

          BoardGamesList boardGames = modelManager.containGame(searchGameEdit.getText());
          for (int i = 0; i < boardGames.size(); i++) {
            listViewGameEdit.getItems().add(boardGames.getBoardGameByIndex(i));
          }
        }
      });
    }
    if (keyEvent.getSource() == searchOwnerEdit) {
      searchOwnerEdit.textProperty().addListener(new ChangeListener() {
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
          listViewOwnerEdit.getItems().clear();

          StudentList students = modelManager.containStudents(searchOwnerEdit.getText());
          for (int i = 0; i < students.size(); i++) {
            listViewOwnerEdit.getItems().add(students.getStudentByIndex(i));
          }
        }
      });
    }
    if (keyEvent.getSource() == searchGameRemove) {
      searchGameRemove.textProperty().addListener(new ChangeListener() {
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
          listViewGameRemove.getItems().clear();

          BoardGamesList boardGames = modelManager.containGame(searchGameRemove.getText());
          for (int i = 0; i < boardGames.size(); i++) {
            listViewGameRemove.getItems().add(boardGames.getBoardGameByIndex(i));
          }
        }
      });
    }
  }

  /**
   * Listener that listens to user interactions in the ListView objects
   * @param mouseEvent the MouseEvent object that tells the application about mouse interactions
   */
  public void listViewChangeListener(MouseEvent mouseEvent) {
    if(mouseEvent.getSource() == listViewAdd){
      this.student = (Student) listViewAdd.getSelectionModel().getSelectedItem();
    }
    if (mouseEvent.getSource() == listViewGameEdit) {
      this.boardGame = (BoardGame) listViewGameEdit.getSelectionModel().getSelectedItem();
      if (boardGame != null) {
        titleEdit.setText(boardGame.getName());
        numberEdit.setText(boardGame.getNumberOfPlayers());
        descriptionEdit.setText(boardGame.getDescription());
        Student student = boardGame.getOwner();
        for (int i = 0; i < studentList.size(); i++) {
          if (studentList.getStudentByIndex(i).equals(student)) {
            listViewOwnerEdit.getSelectionModel().select(i);
            searchOwnerEdit.setText(student.getFirstName() + " " + student.getLastName() + " [" + student.getVIAID() + "]");
          }
        }
      }
    }
    if (mouseEvent.getSource() == listViewOwnerEdit) {
      student = (Student) listViewOwnerEdit.getSelectionModel().getSelectedItem();
      if (student != null) {
        searchOwnerEdit.setText(student.getFirstName() + " " + student.getLastName() + " [" + student.getVIAID() + "]");
      }
    }
    if (mouseEvent.getSource() == listViewGameRemove) {
      boardGame = (BoardGame) listViewGameRemove.getSelectionModel().getSelectedItem();
      if (boardGame != null) {
        titleRemove.setText(boardGame.getName());
        numberRemove.setText(boardGame.getNumberOfPlayers());
        descriptionRemove.setText(boardGame.getDescription());
        if (boardGame.getOwner() != null) {
          student = boardGame.getOwner();
          ownerRemove.setText(student.toString());

        }
      }
    }
  }

  /**
   * Updates ViewLists containing Student objects
   */
    private void updateStudentsList ()
    {
      StudentList students = modelManager.getAllStudents();
      listViewAdd.getItems().clear();
      listViewOwnerEdit.getItems().clear();
      for (int i = 0; i < students.size(); i++) {
        listViewAdd.getItems().add(students.getStudentByIndex(i));
        listViewOwnerEdit.getItems().add(students.getStudentByIndex(i));
      }
    }

  /**
   * Updates ViewLists containing BoardGame objects
   */
  private void updateBoardGamesList()
    {
      listViewGameEdit.getItems().clear();
      listViewGameRemove.getItems().clear();
      for (int i = 0; i < boardGamesList.size(); i++) {
        listViewGameEdit.getItems().add(boardGamesList.getBoardGameByIndex(i));
        listViewGameRemove.getItems().add(boardGamesList.getBoardGameByIndex(i));
      }
    }

  /**
   * Updates XML files
   */
  private void updateWebsite()
  {
    modelManager.saveAllBoardGamesXML(modelManager.getAllBoardGames());
  }

}