package git.group
package View

import Builder.{Builder, BuilderGPS, BuilderInteger}
import List.{DoIt, TList}

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.event.ActionEvent
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.input.{KeyCode, KeyCodeCombination, KeyCombination}
import scalafx.scene.paint.Color
import scalafx.scene.paint.Color._

import java.io.{FileInputStream, FileOutputStream, ObjectInputStream, ObjectOutputStream}
import scala.collection.immutable._


object GUI_View extends JFXApp {


  val fullScreenX = 900
  val fullScreenY = 480
  var builder:Builder = new BuilderInteger
  var list:TList = new TList(builder)
  var auto_clear_TextField:Boolean = true


  stage = new JFXApp.PrimaryStage {
    title.value = "KiTPO GUI TList"
    width = fullScreenX
    height = fullScreenY
    centerOnScreen()
    resizable = false

    scene = new Scene {
      fill = LightBlue

      val menuBar = new MenuBar

      val menuFile = new Menu("Файл")
      val load_menu = new MenuItem("Загрузить")
      load_menu.accelerator = new KeyCodeCombination(KeyCode.I,KeyCombination.ControlDown)
      load_menu.onAction = (e:ActionEvent) => {
        try {
          var i:ObjectInputStream = new ObjectInputStream(new FileInputStream("list.bin"))
          var loaded:TList = i.readObject().asInstanceOf[TList]
          builder = loaded.getBuilder
          list = loaded
          if (builder.getName.equals("Integer"))
            integer_menuButton.selected = true
          else
            string_menuButton.selected = true

          println("Успешно загружен")
        }catch {
          case e:Exception => println("Ошибка! файл не загружен")
        }finally {
          updateList()
        }
      }

      val save_menu = new MenuItem("Сохранить")
      save_menu.accelerator = new KeyCodeCombination(KeyCode.O,KeyCombination.ControlDown)
      save_menu.onAction = (e:ActionEvent) => {
        try{
          var out:ObjectOutputStream = new ObjectOutputStream(new FileOutputStream("list.bin"))
          out.writeObject(list)
          println("Успешно сохранено")
        }catch {
          case e:Exception => println("Ошибка! файл не сохранен")
        }finally {
          updateList()
        }
      }
      val exit_menu = new MenuItem("Выйти")
      exit_menu.accelerator = new KeyCodeCombination(KeyCode.D,KeyCombination.ControlDown)
      exit_menu.onAction = (e:ActionEvent) => sys.exit(0)
      menuFile.items = List(load_menu,save_menu,new SeparatorMenuItem,exit_menu)

      val menuTList = new Menu("Список")
      val test = new MenuItem("тест")
      test.accelerator = new KeyCodeCombination(KeyCode.S,KeyCombination.ControlDown)
      test.onAction = (e:ActionEvent) => {
        try {
          var consol = new Test
          var flag: Boolean = false
          if (flag == false) {
            flag = consol.toBuilder("Integer")
            flag = consol.toBuilder("GPS")
          }
          consol.run()
        }catch {
          case e:Exception => e.printStackTrace()
        }
      }
      val sort_menu = new MenuItem("Сортировать список")
      sort_menu.accelerator = new KeyCodeCombination(KeyCode.S,KeyCombination.ControlDown)
      sort_menu.onAction = (e:ActionEvent) => {
        try {
          list.sort()
          updateList()
        }catch {
          case e:Exception => e.printStackTrace()
        }
      }
      val gen_front = new MenuItem("Сгенерировать число, вставить в начало")
      gen_front.accelerator = new KeyCodeCombination(KeyCode.F,KeyCombination.ControlDown)
      gen_front.onAction = (e:ActionEvent) => {
        try{
          list.pushFront(builder.createObject())
          updateList()
        }catch{
          case e:Exception => e.printStackTrace()
        }
      }
      val gen_back = new MenuItem("Сгенерировать число, вставить в конец")
      gen_back.accelerator = new KeyCodeCombination(KeyCode.B,KeyCombination.ControlDown)
      gen_back.onAction = (e:ActionEvent) => {
        try {
          list.pushEnd(builder.createObject())
          updateList()
        }catch {
          case e:Exception => e.printStackTrace()
        }
      }
      val clear_list_item = new MenuItem("Очистить")
      clear_list_item.accelerator = new KeyCodeCombination(KeyCode.L,KeyCombination.ControlDown)
      clear_list_item.onAction = (e:ActionEvent) => {
        try {
          list.clear()
          updateList()
        }catch {
          case e:Exception => e.printStackTrace()
        }
      }
      menuTList.items = List(test,sort_menu,new SeparatorMenuItem,gen_front,gen_back,new SeparatorMenuItem,clear_list_item)



      menuBar.menus = List(menuFile,menuTList)
      menuBar.prefWidth = (fullScreenX)

      val PossX = 10

      val insertLabel = new Label("Вставить")
      insertLabel.layoutX = PossX
      insertLabel.layoutY = 30


      val PossYInsert = 50

      val insertTextField = new TextField
      insertTextField.layoutX = PossX
      insertTextField.layoutY = PossYInsert
      insertTextField.prefWidth = 100

      val pushFront_button = new Button("В начало")
      pushFront_button.layoutX = PossX+110
      pushFront_button.layoutY = PossYInsert
      pushFront_button.prefWidth = 80
      pushFront_button.onAction = (e:ActionEvent) => {
        if(insertTextField.getText != "") {
          try {
            var tmp = insertTextField.getText()
            list.pushFront(builder.parseObject(tmp))
            updateList()
          } catch {
            case e: Exception => {
              var str = e.toString
              str = str.substring(0,32)
              println(str)
              if (str == "java.lang.NumberFormatException:"){
              }
              else {
                e.printStackTrace()
              }
            }
          }finally {
            if (auto_clear_TextField)
              insertTextField.clear()
          }
        }
      }



      val pushBack_button = new Button("В конец")
      pushBack_button.layoutX = PossX+110+90
      pushBack_button.layoutY = PossYInsert
      pushBack_button.prefWidth = 80
      pushBack_button.onAction = (e:ActionEvent) => {
        if (insertTextField.getText != ""){
          try {
            var tmp = insertTextField.getText()
            list.pushEnd(builder.parseObject(tmp))
            updateList()
          } catch {
            case e:Exception => {
              var str = e.toString
              str = str.substring(0,32)
              println(str)
              if (str == "java.lang.NumberFormatException:"){
              }
              else {
                e.printStackTrace()
              }
            }
          }finally {
            if (auto_clear_TextField)
              insertTextField.clear()
          }
        }
      }

      val insertToIndexLabel = new Label("Вставить по индексу")
      insertToIndexLabel.layoutX = PossX
      insertToIndexLabel.layoutY = 90

      val PossYInsertToIndex = 110

      val insertToIndexTextFieldTwo = new TextField
      insertToIndexTextFieldTwo.layoutX = PossX
      insertToIndexTextFieldTwo.layoutY = PossYInsertToIndex
      insertToIndexTextFieldTwo.prefWidth = 70
      insertToIndexTextFieldTwo.promptText = "Индекс:"

      val insertToIndexTextFieldOne = new TextField
      insertToIndexTextFieldOne.layoutX = PossX+80
      insertToIndexTextFieldOne.layoutY = PossYInsertToIndex
      insertToIndexTextFieldOne.prefWidth = 100
      insertToIndexTextFieldOne.promptText = "Элемент:"


      val push_button = new Button("Вставить")
      push_button.layoutX = PossX+190
      push_button.layoutY = PossYInsertToIndex
      push_button.prefWidth = 80
      push_button.onAction = (e:ActionEvent) => {
        if(insertToIndexTextFieldOne.getText != "" && insertToIndexTextFieldTwo.getText != "") {
          try{
            var tmp = insertToIndexTextFieldOne.getText()
            var tmpIndex = insertToIndexTextFieldTwo.getText().toInt
            list.add(builder.parseObject(tmp),tmpIndex)
            updateList()
          } catch {
            case e: Exception => {
              var str = e.toString
              str = str.substring(0,32)
              println(str)
              if (str == "java.lang.NumberFormatException:"){
              }
              else {
                e.printStackTrace()
              }
            }
          }finally {
            if (auto_clear_TextField) {
              insertToIndexTextFieldOne.clear()
              insertToIndexTextFieldTwo.clear()
            }
          }
        }
      }

      val deleteLabel = new Label("Удалить:")
      deleteLabel.layoutX = PossX
      deleteLabel.layoutY = 150

      val PossYDelete = 170
      val PossYDelete1 = 210


      val delFront = new Button("В начале")
      delFront.layoutX = PossX
      delFront.layoutY = PossYDelete
      delFront.prefWidth = 70
      delFront.onAction = (e:ActionEvent) => {
        try{
          list.delete(0)
          updateList()
        }catch {
          case e:Exception => {
            var str = e.toString
            str = str.substring(0,32)
            println(str)
            if (str == "java.lang.NumberFormatException:"){
            }
            else {
              e.printStackTrace()
            }
          }
        }
      }

      val delBack = new Button("В конце")
      delBack.layoutX = PossX+80
      delBack.layoutY = PossYDelete
      delBack.prefWidth = 70
      delBack.onAction = (e:ActionEvent) => {
        try{
          list.delete(list.getSize-1)
          updateList()
        }catch {
          case e:Exception => {
            var str = e.toString
            str = str.substring(0,32)
            println(str)
            if (str == "java.lang.NumberFormatException:"){
            }
            else {
              e.printStackTrace()
            }
          }
        }
      }

      val deleteTextFieldTwo = new TextField
      deleteTextFieldTwo.layoutX = PossX
      deleteTextFieldTwo.layoutY = PossYDelete1
      deleteTextFieldTwo.prefWidth = 60
      deleteTextFieldTwo.promptText = "Индекс:"

      val delete_button = new Button("Удалить")
      delete_button.layoutX = PossX +70
      delete_button.layoutY = PossYDelete1
      delete_button.prefWidth = 70
      delete_button.onAction = (e:ActionEvent) => {
        if (deleteTextFieldTwo.getText != ""){
          try{
            var tmpIndex = deleteTextFieldTwo.getText().toInt
            list.delete(tmpIndex)
            updateList()
          }catch {
            case e:Exception => {
              var str = e.toString
              str = str.substring(0,27)
              println(str)
              if (str == "java.lang.NumberFormatExce:"){
              }
              else if (str == "java.lang.NullPointerExcept"){
              }
              else{
                e.printStackTrace()
              }
            }
          }finally {
            if (auto_clear_TextField)
              deleteTextFieldTwo.clear()
          }
        }
      }



      val changeTypeLabel = new Label("Выберете тип данных")
      changeTypeLabel.layoutX = PossX
      changeTypeLabel.layoutY = 250

      val PossYChange = 270

      val change_menuButton = new MenuButton("Тип данных")
      change_menuButton.layoutX = PossX
      change_menuButton.layoutY = PossYChange

      val integer_menuButton = new RadioMenuItem("Integer")
      integer_menuButton.selected = true
      integer_menuButton.onAction = (e:ActionEvent) => {
        try{
          if (list.getSize == 0){
            builder = new BuilderInteger
            list = new TList(builder)
            updateList()
          }
          else {
            string_menuButton.selected = true
            println("Список пуст")
          }
        }catch {
          case e:Exception => e.printStackTrace()
        }finally {
          updateList()
        }
      }
      val string_menuButton = new RadioMenuItem("GPS")
      string_menuButton.onAction = (e:ActionEvent) => {
        try {
          if (list.getSize == 0){
            builder = new BuilderGPS
            list = new TList(builder)
            updateList()
          }
          else {
            println("Список пуст")
            integer_menuButton.selected = true
          }
        }catch {
          case e:Exception => e.printStackTrace()
        }
      }
      val group = new ToggleGroup

      group.toggles = List(integer_menuButton,string_menuButton)
      change_menuButton.items = List(integer_menuButton,string_menuButton)

      var typeName_label = Label("Текущий тип: " + builder.getName)
      typeName_label.layoutX = PossX+120
      typeName_label.layoutY = PossYChange+3

      val findLabel = new Label("Поиск:")
      findLabel.layoutX = PossX
      findLabel.layoutY = 320

      val PossYFindEl = 340
      val PossYFindIn = 370

      val findElTextField = new TextField
      findElTextField.layoutX = PossX
      findElTextField.layoutY = PossYFindEl
      findElTextField.prefWidth = 100
      findElTextField.promptText = "Элемент:"

      val findElem_button = new Button("Поиск")
      findElem_button.layoutX = PossX+110
      findElem_button.layoutY = PossYFindEl
      findElem_button.prefWidth = 60
      findElem_button.onAction = (e:ActionEvent) => {
        if (findElTextField.getText != ""){
          try{
            var tmp = findElTextField.getText()
            if (list.find_quantity(builder.parseObject(tmp)) == 1){
              findOutLabelOne.setText("Индекс: "+list.finds(builder.parseObject(tmp)))
            }
            else if (list.find_quantity(builder.parseObject(tmp)) == 0){
              findOutLabelOne.setText("Количество: 0")
            } else if (list.find_quantity(builder.parseObject(tmp)) == -1){
              findOutLabelOne.setText("Элемент не найден")
            }else {
              findOutLabelOne.setText("Количество: "+list.find_quantity(builder.parseObject(tmp)))
            }
          } catch {
            case e:ActionEvent => e.printStackTrace()
          } finally {
            findOutLabelOne.setTextFill(Color.Black)
          }
        }
        else{
          findOutLabelOne.setText("Элемент не найден")
          findOutLabelOne.setTextFill(Color.Red)
        }
      }

      val findOutLabelOne = new Label("-")
      findOutLabelOne.layoutX = PossX+110+70
      findOutLabelOne.layoutY = PossYFindEl

      val findInTextField = new TextField
      findInTextField.layoutX = PossX
      findInTextField.layoutY = PossYFindIn
      findInTextField.prefWidth = 100
      findInTextField.promptText = "Индекс:"

      val findIndex_button = new Button("Поиск")
      findIndex_button.layoutX = PossX+110
      findIndex_button.layoutY = PossYFindIn
      findIndex_button.prefWidth = 60
      findIndex_button.onAction = (e:ActionEvent) => {
        if (findInTextField.getText != ""){
          try{
            var tmpIndex = findInTextField.getText().toInt
            if (tmpIndex < list.getSize) {
              if(builder.getName == "GPS") {
                findOutLabelTwo.setText("Элемент: "+list.find(tmpIndex))
                val tmpText = list.find(tmpIndex)

              }
              if(builder.getName == "Integer") {
                findOutLabelTwo.setText("Элемент: "+list.find(tmpIndex))
                val tmpText = list.find(tmpIndex)

              }
            } else
              findOutLabelTwo.setText("Элемент не найден")
          } catch {
            case e:ActionEvent => e.printStackTrace()
          } finally {
            findOutLabelTwo.setTextFill(Color.Black)
          }
        }
        else{
          findOutLabelTwo.setText("Индекс не найден!")
          findOutLabelTwo.setTextFill(Color.Red)
        }
      }

      val findOutLabelTwo = new Label("-")
      findOutLabelTwo.layoutX = PossX+110+70
      findOutLabelTwo.layoutY = PossYFindIn


      val size_list_label = new Label("Размер списка = 0")
      size_list_label.layoutX = PossX
      size_list_label.layoutY = 420
      size_list_label.setTextFill(Color.Red)


      val textArea = new TextArea
      textArea.layoutX = fullScreenX/3 // <- динамическое расположения (при создании окна - НЕПРИИЗМЕНЕНИИРАЗМЕРА)
      textArea.layoutY = fullScreenY-(fullScreenY-30)
      textArea.prefHeight = (fullScreenY-75)
      textArea.prefWidth = fullScreenX-(fullScreenX/3 + 25)
      textArea.editable = false

      private def show_list() = {
        list.forEach(new DoIt{
          override def doIt(o: Any): Unit = {
            textArea.appendText(o.toString() + "\n")
          }
        })
      }

      private def updateList()={
        size_list_label.setText("Размер списка = "+ list.getSize)
        size_list_label.setTextFill(Color.Black)
        textArea.clear()
        show_list()
        typeName_label.setText("Текущий тип: " + builder.getName)
      }



      private val contentList = List(
        textArea,
        menuBar,
        insertLabel,
        insertTextField,
        pushFront_button,
        pushBack_button,
        insertToIndexLabel,
        insertToIndexTextFieldTwo,
        insertToIndexTextFieldOne,
        push_button,
        deleteLabel,
        delFront,
        delBack,
        deleteTextFieldTwo,
        delete_button,
        changeTypeLabel,
        change_menuButton,
        typeName_label,
        findLabel,
        findElTextField,
        findElem_button,
        findOutLabelOne,
        findInTextField,
        findIndex_button,
        findOutLabelTwo,
        size_list_label
      )

      content = contentList
    }
  }
}
