package org.jclouds.conferenceapp

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.stage.Stage
import scalafx.scene.Scene
import scalafx.scene.image.{ImageView, Image}
import scalafx.scene.shape.Rectangle
import scalafx.scene.layout.{HBox, StackPane, VBox}
import scalafx.scene.paint.{Stops, LinearGradient}
import scalafx.scene.paint.Color._
import scalafx.geometry.Insets
import scalafx.scene.control._
import scalafx.scene.control.TextField
import javafx.geometry.Pos
import scalafx.collections.ObservableBuffer
import javafx.application.Platform
import scalafx.event.ActionEvent
import ru.circumflex.{orm => ruco}
import scalafx.util.StringConverter

object ConferenceUI extends JFXApp {
  val model = ConferenceModel
  stage = new Stage {
    width = 625
    height = 700
    scene = new Scene(new StackPane()) {
      fill = "#fcfcfc"
      children = Seq(
        new VBox {
          children = Seq(
            new ImageView {
              image = new Image(getClass().getResourceAsStream("JavaOneLogo.png"))
            },
            new Rectangle {
              width = 625
              height = 50
              fill = new LinearGradient(
                endX = 0,
                stops = Stops(WHITE, "#d0cbc8")
              )
            }
          )
        },
        new VBox {
          padding = Insets(100, 20, 20, 20)
          spacing = 30
          children = Seq(
            new HBox {
              val filter = new TextField();
              val items = new ChoiceBox[ruco.TextField[Speaker]]() {
                items = ObservableBuffer(Speaker.firstName, Speaker.lastName, Speaker.jobTitle, Speaker.company)
                converter = StringConverter.toStringConverter({s:ruco.TextField[Speaker] => s.name})
              }
              alignment = Pos.BASELINE_LEFT
              spacing = 15
              children = Seq(
                items,
                filter,
                new Button("Filter") {
                  onAction = { e:ActionEvent =>
                    model.filter(items.selectionModel().getSelectedItem(), filter.text())
                  }
                },
                new Button("Clear") {
                  onAction = { e:ActionEvent =>
                    filter.text = ""
                    model.clear()
                  }
                },
                new Button("Reload") {
                  onAction = { e:ActionEvent =>
                    filter.text = ""
                    model.load()
                  }
                }
              )
              items.selectionModel().selectFirst()
            },
            new TableView[Speaker](model.filteredSpeakers) {
              columns = Seq(
                new TableColumn[Speaker, String] {
                  text = "First Name"
                  converter = {_.firstName()}
                },
                new TableColumn[Speaker, String] {
                  text = "Last Name"
                  converter = {_.lastName()}
                },
                new TableColumn[Speaker, String] {
                  text = "Job Title"
                  converter = {_.jobTitle()}
                  prefWidth = 200
                },
                new TableColumn[Speaker, String] {
                  text = "Company"
                  converter = {_.company()}
                  prefWidth = 212
                }
              )
              prefHeight = 1000
            }
          )
        }
      )
    }
    onCloseRequest = {_:Any => Platform.exit}
  }
}
