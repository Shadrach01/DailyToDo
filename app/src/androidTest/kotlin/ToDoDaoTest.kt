import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.dailytodo.data.DailyToDoDataBase
import com.example.dailytodo.data.ToDo
import com.example.dailytodo.data.ToDoDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.jvm.Throws

@RunWith(AndroidJUnit4::class)
class ToDoDaoTest {
    private lateinit var toDoDao: ToDoDao
    private lateinit var toDoDataBase: DailyToDoDataBase

    private var todo1 = ToDo(1, "Wakeup", "12:00")
    private var todo2 = ToDo(2, "Sleep", "01:00")

    /**
     * Function to create the database
     * The @Before annotation is so that is can run before every test
     */
    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()

        /**
         * Using an in-memory database because the information stored here disappears
         * when the process is killed
         */
        toDoDataBase = Room.inMemoryDatabaseBuilder(context, DailyToDoDataBase::class.java)
            //Allowing the main thread queries, just for testing
            .allowMainThreadQueries()
            .build()
        toDoDao = toDoDataBase.todoDao()
    }

    //Close the database
    @After
    @Throws(IOException::class)
    fun closeDb() {
        toDoDataBase.close()
    }

    private suspend fun addOneTodoToDb() {
        toDoDao.insert(todo1)
    }

    private suspend fun addTwoTodosToDb() {
        toDoDao.insert(todo1)
        toDoDao.insert(todo2)
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsItemIntoDB() = runBlocking {
        addOneTodoToDb()
        val allItems = toDoDao.getAllToDo().first()
        assertEquals(allItems[0], todo1)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllItems_returnsAllItemsFromDb() = runBlocking {
        addTwoTodosToDb()
        val allItems = toDoDao.getAllToDo().first()
        assertEquals(allItems[0], todo1)
        assertEquals(allItems[1], todo2)
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdateItems_updatesItemsInDB() = runBlocking {
        addTwoTodosToDb()
        toDoDao.update(ToDo(1, "knack", "05:00"))
        toDoDao.update(ToDo(2, "Come", "07:00"))

        val allItems = toDoDao.getAllToDo().first()
        assertEquals(allItems[0], ToDo(1, "knack", "05:00"))
        assertEquals(allItems[1], ToDo(2, "Come", "07:00"))
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteItems_deletesAllItemsFromDB() = runBlocking {
        addTwoTodosToDb()
        toDoDao.delete(todo1)
        toDoDao.delete(todo2)
        val allItems = toDoDao.getAllToDo().first()
        assertTrue(allItems.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun daoGetItem_returnsItemFromDB() = runBlocking {
        addOneTodoToDb()
        val item = toDoDao.getToDO(1)
        assertEquals(item.first(), todo1)
    }


}