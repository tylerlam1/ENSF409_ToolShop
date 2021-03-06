package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import views.LoginView;
import views.MainView;
import views.OrderView;
import utils.DataCodes;
import utils.UserInformation;

/**
 * Controller for the login screen GUI that handles login screen actions
 * 
 * @author Navjot Brar, Jofred Cayabyab and Tyler Lam
 * @version 1.0.0
 * @since March 31, 2019
 */
public class LoginController implements DataCodes {

  /**
   * the loginView object that holds the GUI for the login screen
   */
  private LoginView loginView;

  /**
   * Communication object that allows for communication with server
   */
  private Communication communication;

  /**
   * the mainView object that holds the GUI used to opened upon login
   */
  private MainView mainView;

  /**
   * constructs the basic functionality of the loginController to handle the login
   * GUI, including showing the view, calling helper functions to create
   * listeners, etc.
   * 
   * @param view          the LoginView object that holds the login screen GUI
   * @param communication the communication object that allows for client-server
   *                      interaction
   */
  public LoginController(LoginView view, MainView mainView, Communication communication) {
    loginView = view;
    this.mainView = mainView;
    this.communication = communication;
    addLoginListeners();
  }

  /**
   * adds listeners to the login screen GUI buttons to enable responses to onClick
   * actions
   */
  private void addLoginListeners() {
    loginView.addLoginListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        String id = loginView.returnIDTextField();
        String password = loginView.returnPasswordTextField();
        UserInformation newUser = new UserInformation(id, password);
        Object response = communication.sendObject(SEND_USERDATA, newUser);
        if (response instanceof String && response.equals(SEND_ERROR)) {
          loginView.showErrorDialog("Please enter a valid username and password.", "Error Found");
          return;
        }
        newUser = (UserInformation) response;
        boolean isOwnerView = (boolean) communication.readObject();
        mainView.setIsOwnerView(isOwnerView);
        loginView.setVisible(false);
        mainView.showView();
      }
    });

    loginView.addCloseAppListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        loginView.setVisible(false);
      }
    });
  }

  /**
   * shows the login screen view
   */
  public void showView() {
    loginView.setVisible(true);
  }

  /**
   * The main function of the client. Since the login screen is shown first, main
   * is run through here as an entry point.
   * 
   * @param args input from the command line that will not be used
   */
  public static void main(String[] args) {
    LoginView loginView = new LoginView("Login");
    MainView mainView = new MainView("Main Window");
    OrderView orderView = new OrderView("Order View");
    Communication communication = new Communication("localhost", 3000);

    LoginController loginController = new LoginController(loginView, mainView, communication);
    MainController mainController = new MainController(mainView, loginView, orderView, communication);
    OrderController orderController = new OrderController(mainView, orderView, communication);
    loginController.showView();
  }
}