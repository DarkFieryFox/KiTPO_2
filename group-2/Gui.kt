package git.group

import git.group.Builder.Builder
import git.group.List.TList
import git.group.List.DoIt
import git.group.Comparator.ComparatorGPS
import git.group.Builder.BuilderGPS
import git.group.Comparator.ComparatorInteger
import java.awt.Font
import java.awt.event.ActionListener
import java.awt.event.ActionEvent
import java.awt.FlowLayout
import git.group.Serialization
import java.lang.RuntimeException
import java.awt.BorderLayout
import kotlin.Throws
import kotlin.jvm.JvmStatic
import git.group.Gui
import git.group.Builder.BuilderInteger
import java.util.Arrays
import java.io.PrintWriter
import java.io.BufferedReader
import java.io.FileReader
import java.lang.Exception
import java.util.Objects
import javax.swing.*

class Gui internal constructor() : JFrame("Lab1") {
    private var builder: Builder? = null
    private var list: TList? = null

    init {
        defaultCloseOperation = DISPOSE_ON_CLOSE
        //окно вывода
        val out = JTextArea()
        out.isEditable = false
        out.font = Font("Time New Roman", Font.PLAIN, 20)
        val menu = JPanel()
        val box = Box.createVerticalBox()
        val builderType: JComboBox<*> = JComboBox<Any?>(Factory.getTypeNameList().toTypedArray())
        builderType.addActionListener { view: ActionEvent? ->
            println(builderType.selectedItem)
            val type = builderType.selectedItem
            val type1 = type.toString()
            try {
                builder = Factory.getBuilderByName(type1)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            list = TList(builder)
        }

        //loadsave
        val workFiles = JPanel(FlowLayout())
        workFiles.border = BorderFactory.createEmptyBorder(10, 0, 30, 0)
        val load = JButton("Загрузить из файла")
        load.addActionListener { v: ActionEvent? ->
            list = try {
                Serialization.loadFile("file.txt")
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
            val str = """
                ${out.text}
                Список был загружен!
                """.trimIndent()
            out.text = str
        }
        val save = JButton("Сохранить")
        save.addActionListener { v: ActionEvent? ->
            try {
                Serialization.saveToFile(list, "file.txt", builderType.selectedItem.toString())
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
            val str = """
                ${out.text}
                Список сохранен!
                """.trimIndent()
            out.text = str
        }
        workFiles.add(load)
        workFiles.add(save)

        //insert
        val insertIndexPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        val insertValuePanel = JPanel(FlowLayout(FlowLayout.LEFT))
        val insertIndexLabel = JLabel("Индекс: ")
        val insertIndexField = JTextField(4)
        val insertValueLabel = JLabel("Значение: ")
        val insertValueField = JTextField(10)
        insertValueField.toolTipText = "integer:int | GPS: double;double:int:int:int"
        insertIndexPanel.add(insertIndexLabel)
        insertIndexPanel.add(insertIndexField)
        insertValuePanel.add(insertValueLabel)
        insertValuePanel.add(insertValueField)
        val insert = JPanel(FlowLayout(FlowLayout.LEFT))
        val insertBtn = JButton("Вставка элемента в начало")
        insertBtn.addActionListener { view: ActionEvent? ->
            var obj: Builder? = null
            try {
                obj = Factory.getBuilderByName(builderType.selectedItem.toString())
                        ?.parseObject(insertValueField.text) as Builder
            } catch (e: Exception) {
                e.printStackTrace()
            }
            insertValueField.text = ""
            list!!.pushFront(obj)
            val str = """
                ${out.text}
                Вставка элемента в начало ${obj.toString()}
                """.trimIndent()
            out.text = str
        }
        val insertbyIndex = JPanel(FlowLayout(FlowLayout.LEFT))
        insertbyIndex.border = BorderFactory.createEmptyBorder(20, 0, 0, 0)
        val insertBtnbyIndex = JButton("Вставка по индексу")
        insertBtnbyIndex.addActionListener { view: ActionEvent? ->
            var obj: Builder? = null
            try {
                obj = Factory.getBuilderByName(builderType.selectedItem.toString())
                        ?.parseObject(insertValueField.text) as Builder
            } catch (e: Exception) {
                e.printStackTrace()
            }
            val index = insertIndexField.text.toInt()
            insertIndexField.text = ""
            insertValueField.text = ""
            list!!.add(obj, index)
            val str = """${out.text}
Вставка по индексу ${obj.toString()} в $index"""
            out.text = str
        }
        val insertBtnBack = JButton("Вставить в конец")
        insertBtnBack.addActionListener { view: ActionEvent? ->
            var obj: Builder? = null
            try {
                obj = Factory.getBuilderByName(builderType.selectedItem.toString())
                        ?.parseObject(insertValueField.text) as Builder
            } catch (e: Exception) {
                e.printStackTrace()
            }
            insertValueField.text = ""
            list!!.pushEnd(obj)
            val str = """
                ${out.text}
                Вставить в конец ${obj.toString()}
                """.trimIndent()
            out.text = str
        }
        val deleteBtn = JButton("Удалить элемент по индексу")
        deleteBtn.addActionListener { view: ActionEvent? ->
            val index = insertIndexField.text.toInt()
            insertIndexField.text = ""
            insertValueField.text = ""
            list!!.delete(index)
            val str = """${out.text}
Индекс: $index удален"""
            out.text = str
        }
        val search = JPanel(FlowLayout(FlowLayout.CENTER))
        search.border = BorderFactory.createEmptyBorder(20, 0, 0, 0)
        val searchBtn = JButton("Поиск элемента по индексу")
        searchBtn.addActionListener { view: ActionEvent? ->
            val index = insertIndexField.text.toInt()
            insertIndexField.text = ""
            val str = """
                ${out.text}
                Индекс $index: ${list!!.find(index)}
                """.trimIndent()
            out.text = str
        }
        val sortBtn = JButton("Сортировка")
        sortBtn.addActionListener { view: ActionEvent? ->
            list!!.sort(builder?.comparator)
            val str = """
                ${out.text}
                Список отсортирован! 
                """.trimIndent()
            out.text = str
        }
        val clrBtn = JButton("Очистить список")
        clrBtn.addActionListener { view: ActionEvent? ->
            list!!.clear()
            val str = """
                ${out.text}
                Список очищен! 
                """.trimIndent()
            out.text = str
        }
        val show = JPanel(FlowLayout(FlowLayout.LEFT))
        show.border = BorderFactory.createEmptyBorder(20, 0, 0, 0)
        val showBtn = JButton("Показать список")
        showBtn.addActionListener { view: ActionEvent? ->
            val str = """
     ${out.text}
     ${list.toString()}
     """.trimIndent()
            out.text = str
        }
        box.add(builderType)
        menu.add(box)
        builderType.addActionListener { e: ActionEvent? ->
            box.add(builderType)
            insert.add(insertBtn)
            insert.add(insertBtnBack)
            insertbyIndex.add(insertBtnbyIndex)
            insertbyIndex.add(deleteBtn)
            search.add(searchBtn)
            show.add(sortBtn)
            show.add(showBtn)
            show.add(clrBtn)
            box.add(builderType)
            box.add(workFiles)
            box.add(insertIndexPanel)
            box.add(insertValuePanel)
            box.add(insert)
            box.add(insertbyIndex)
            box.add(search)
            box.add(show)
            menu.add(box)
            revalidate()
            repaint()
        }
        val frame = JPanel()
        frame.layout = BorderLayout()
        frame.add(menu, BorderLayout.WEST)
        frame.add(JScrollPane(out), BorderLayout.CENTER)
        contentPane = frame
        setSize(1200, 600)
        isResizable = false
        isVisible = true
    }
}