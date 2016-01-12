package com.codeest.postory.presentation;

import com.codeest.postory.dao.PosProfileDAO;
import com.codeest.postory.pojo.PosProfile;
import com.codeest.postory.presentation.control.IconicButton;
import com.codeest.postory.presentation.control.LeftMenuToolBar;
import com.codeest.postory.presentation.control.SlideBar;
import com.codeest.postory.presentation.control.TopMenuBox;
import com.codeest.postory.presentation.controller.BluetoothController;
import com.codeest.postory.presentation.controller.NewBarcodeInvoicePaneController;
import com.codeest.postory.presentation.controller.NewInvoicePaneController;
import com.codeest.postory.presentation.controller.PostoryModalWindow;
import com.codeest.postory.presentation.font.FlatIcons;
import com.codeest.postory.presentation.font.Iconics;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Prashant
 */
public class AppMain extends Application {

    SlideBar bar;
    TopMenuBox tmb;

    @Override
    public void start(Stage primaryStage) throws Exception {
        StackPane root = new StackPane();
        bar = new SlideBar();
        Label content = new Label("DASHBOARD----TBI");
        content.setStyle("-fx-background-color: #F2F2F2; -fx-border-color: black;");
        content.setAlignment(Pos.CENTER);
        content.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        //try {
        // Parent rootCenter = FXMLLoader.load(getClass().getResource("test.fxml"));
        //  bar.setContent(rootCenter);
        //} catch (IOException ex) {
        //    Logger.getLogger(AppMain.class.getName()).log(Level.SEVERE, null, ex);
        // }
        bar.setContent(content);
        // loadItemPaneInCOntent();
        LeftMenuToolBar lmtb = new LeftMenuToolBar();
        lmtb.setGlobaActionCallBack((String source) -> {
            selectMenuAndHideMenuBar(source);
        });
        bar.setLeft(lmtb);

        //topLabelR.setStyle("-fx-background-color: rgba(0,255,0,.25);");
        IconicButton testButton = new IconicButton();
        testButton.getStyleClass().setAll("");
        Iconics.setFlatIcon(testButton, FlatIcons.Icons.ICON_MENU.toString(), "2em", "white", "Menu");
         testButton.setOnAction(e -> {
            if (bar.isShowing()) {
                bar.setShowing(Boolean.FALSE);
                tmb.setShowing(Boolean.FALSE);
            } else {
                bar.setShowing(Boolean.TRUE);
                tmb.setShowing(Boolean.TRUE);
            }
        });

        HBox topRightBox = new HBox();
        topRightBox.setAlignment(Pos.CENTER_RIGHT);
        IconicButton newInvoiceButton = new IconicButton();
        newInvoiceButton.getStyleClass().setAll("");
        Iconics.setFlatIcon(newInvoiceButton, FlatIcons.Icons.ICON_INVOICE_NEW.toString(), "2em", "white", "Add New Invoice");
        newInvoiceButton.setOnAction(e -> {
            openNewInvoicePane(e);
        });
        
        IconicButton newBarcodeInvoice = new IconicButton();
        newBarcodeInvoice.getStyleClass().setAll("");
        Iconics.setFlatIcon(newBarcodeInvoice, FlatIcons.Icons.ICON_BARCODE_SCAN.toString(), "2em", "white", "New Barcode Invoice");
        newBarcodeInvoice.setOnAction(e -> {
            openNewBarcodeInvoicePane(e);
        });
        
        VBox posProfileBox = new VBox(2);
        posProfileBox.setFillWidth(true);
        posProfileBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(posProfileBox, Priority.ALWAYS);
        PosProfileDAO posProfileDAO = new PosProfileDAO();
        PosProfile profile = posProfileDAO.getPosProfile();
        Text posNameText = new Text(profile.getName());
        Text posAdressText = new Text(profile.getAddress());
        Text posCityText  = new Text(profile.getCity()+","+profile.getPincode());
        posProfileBox.getChildren().addAll(posNameText,posAdressText,posCityText);
        topRightBox.getChildren().addAll(posProfileBox,newInvoiceButton, newBarcodeInvoice);
        tmb = new TopMenuBox(testButton, topRightBox);
        SetHoverEffect(testButton);
        SetHoverEffect(newInvoiceButton);
        SetHoverEffect(newBarcodeInvoice);
       

        bar.setTop(tmb);
        root.getChildren().addAll(bar);

        Scene scene = new Scene(root, 300, 250);
        scene.getStylesheets().add(AppMain.class.getResource("fxt-default.css").toExternalForm());

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void selectMenuAndHideMenuBar(String source) {
        if (!StringUtils.isEmpty(source)) {
            try {
                Parent rootCenter = FXMLLoader.load(getClass().getResource("fxml/" + source + ".fxml"));
                bar.setContent(rootCenter);
            } catch (IOException ex) {
                Logger.getLogger(AppMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (bar.isShowing()) {
            bar.setShowing(Boolean.FALSE);
            tmb.setShowing(Boolean.FALSE);
        }
    }

    private void loadItemPaneInCOntent() {
        try {
            Parent rootCenter = FXMLLoader.load(getClass().getResource("fxml/itempane.fxml"));
            bar.setContent(rootCenter);
        } catch (IOException ex) {
            Logger.getLogger(AppMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void openNewInvoicePane(ActionEvent ae) {
        PostoryModalWindow modalWindow = new PostoryModalWindow();
        modalWindow.openNewModalPaneWithParent("New Invoice", "fxml/newinvoicepane.fxml", ae);
        NewInvoicePaneController controller = (NewInvoicePaneController) modalWindow.getDswFromController();
        controller.getWindowStage().showAndWait();
    }

    private void openNewBarcodeInvoicePane(ActionEvent ae) {
        PostoryModalWindow modalWindow = new PostoryModalWindow();
        modalWindow.openNewModalPaneWithParent("New Invoice", "fxml/newbarcodeinvoicepane.fxml", ae);
        //NewInvoicePaneController controller = (NewInvoicePaneController) modalWindow.getDswFromController();
        //++controller.getWindowStage().showAndWait();
        NewBarcodeInvoicePaneController controller = (NewBarcodeInvoicePaneController) modalWindow.getDswFromController();
       //controller.setup();
        controller.getWindowStage().showAndWait();
        BluetoothController bc = controller.getBc();
        if(bc != null){
            System.err.println("CANCELLING");
            bc.cancel(true);
            bc.cancelandExit();
        }
        
    }
    private void SetHoverEffect(IconicButton ib){
        ib.setOnMouseEntered(e -> {
            ib.setScaleX(1.1);
            ib.setScaleY(1.1);
        });
        ib.setOnMouseExited(e -> {
            ib.setScaleX(1.0);
            ib.setScaleY(1.0);
        });
    }
}
