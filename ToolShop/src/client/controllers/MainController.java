package client.controllers;

import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import client.views.ItemDialogView;
import client.views.MainView;
import utils.DataCodes;

/**
 * Controller for the MainView GUI
 * 
 * @author Navjot Brar, Jofred Cayabyab and Tyler Lam
 * @version 1.0.0
 * @since March 31, 2019
 */
public class MainController implements DataCodes {

  /**
   * the MainView object used which will be used to control the main GUI screen
   */
  MainView mainView;

  /**
   * Communication object that allows for communication with server
   */
  Communication communication;

  /**
   * Constructs the main controller by setting the MainView of the Controller as
   * well as the communication
   * 
   * @param view          the MainView object
   * @param communication the communication object
   */
  public MainController(MainView view, Communication communication) {
    mainView = view;
    this.communication = communication;
    addMainListeners();
  }

  /**
   * adds listeners for the mainView buttons that allows for responses for buttons
   * being clicked
   */
  public void addMainListeners() {

    mainView.addCreateOrderListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ItemDialogView itemPrompt = new ItemDialogView("Create Item");
        itemPrompt.pack();
        itemPrompt.setVisible(true);

        itemPrompt.addCreateItemListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            String description = itemPrompt.getDescription();
            String supplierId = itemPrompt.getSupplierId();
            String quantity = itemPrompt.getQuantity();
            String price = itemPrompt.getPrice();

            boolean hasEmptyField = description.length() == 0 || supplierId.length() == 0 || quantity.length() == 0
                || price.length() == 0;

            if (hasEmptyField) {
              itemPrompt.setLabel("Please fill out all the fields.");
            } else {
              communication.sendItemInfo(description, quantity, price, supplierId);
              itemPrompt.setVisible(false);
            }
          }
        });
      }
    });

    mainView.addDeleteItemListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // int row = mainView.getTextArea().getSelectedRow();
        // TODO: Potentially pass the row to the server side to correctly retrieve the
        // item to delete
      }
    });

    mainView.addQuitListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mainView.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // TODO: Potentially open the login window again?
      }
    });
  }
}