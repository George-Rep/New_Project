
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;



import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static package1.Main.*;

/**
 * Взять за основу ДЗ_13. Используя Spy и Mockito создать заглушки для интерфейса JDBC
 */
public class Tests {

    static ResultSet rs;
    static long t4a1, t4a2, t4a3, t4b1, t4b2;
    static PreparedStatement ps = Mockito.mock(PreparedStatement.class),
    pst1 = null, pst2 = null, pst3 = null;

    /**
     * получение соединения с БД.
     * инициализация preparedStatement и получение необходимых ID из properties.
     * preparedStatement - запрос 3 в подзадаче "4 б" превращается в мок.
     */
    @BeforeAll
    public static void init() throws SQLException {
        connection = Mockito.spy(getConnection());

        doReturn(ps).when(connection).prepareStatement(resource.getString("t4b.ps3"));

        initStatements();
        pst1 = connection.prepareStatement(resource.getString("test.pst1"));
        pst2 = connection.prepareStatement(resource.getString("test.pst2"));
        pst3 = connection.prepareStatement(resource.getString("test.pst3"));
        t4a1 = Long.parseLong(resource.getString("test.4a.1"));
        t4a2 = Long.parseLong(resource.getString("test.4a.2"));
        t4a3 = Long.parseLong(resource.getString("test.4a.3"));
        t4b1 = Long.parseLong(resource.getString("test.4b.1"));
        t4b2 = Long.parseLong(resource.getString("test.4b.2"));

    }
    /**
     * метод для проверки сушествования записей с заданным ID в таблице.
     * (Каждой таблице соответствует PreparedStatement)
     * @param ps PreparedStatement, select по ID из необходимой таблицы,
     * @param id , ID записи в таблице
     * @return boolean , true если записи с данным ID есть
     * @throws SQLException
     */
    static boolean testSelect(PreparedStatement ps, Long id) throws SQLException {
        ps.setLong(1, id);
        rs = ps.executeQuery();
        return rs.next();
    }
    /**
     * тест задачи 2 а
     * "Через jdbc интерфейс сделать запись данных(INSERT) в таблицу
     * Используя параметризированный запрос"
     *
     * @throws SQLException
     */
    @Test
    void task2aTest() throws SQLException {
        task2a();
        assertTrue(testSelect(pst1, Long.parseLong(resource.getString("t2a.1"))));
        rs.close();
    }
    /**
     * тест задачи 2 б
     * "Через jdbc интерфейс сделать запись данных(INSERT) в таблицу
     * Используя batch процесс"
     *
     * @throws SQLException
     */
    @Test
    void task2bTest() throws SQLException {
        task2b();
        assertTrue(testSelect(pst1, Long.parseLong(resource.getString("t2b.1.1"))));
        assertTrue(testSelect(pst1, Long.parseLong(resource.getString("t2b.2.1"))));
        rs.close();
    }
    /**
     * тест задачи 3
     * "ЧСделать параметризированную выборку по login_ID и name одновременно"
     *
     * @throws SQLException
     */
    @Test
    void task3aTest() throws SQLException {
        rs = task3();
        rs.next();
        assertEquals(resource.getString("t3.1"), rs.getString(1));
        assertEquals(resource.getString("t3.2"), rs.getString(2));
        rs.close();
    }
    /**
     * тест задачи 4 а
     * "Выполнить 2-3 SQL операции на ваше усмотрение (например, Insert в 3 таблицы – USER, ROLE, USER_ROLE) между sql
     * операциями установить логическую точку сохранения(SAVEPOINT)"
     *
     * @throws SQLException
     */
    @Test
    void task4aTest() throws SQLException {
        task4a();
        assertFalse(testSelect(pst1, Long.parseLong(resource.getString("t2b.2.1"))));
        assertFalse(testSelect(pst1, Long.parseLong(resource.getString("t2a.1"))));
        rs.close();
    }
    /**
     * тест задачи 4 б
     * "Выполнить 2-3 SQL операции на ваше усмотрение (например, Insert в 3 таблицы – USER, ROLE, USER_ROLE) между sql
     * операциями установить точку сохранения (SAVEPOINT A), намеренно ввести некорректные данные на последней операции,
     * что бы транзакция откатилась к логической точке SAVEPOINT A"
     *
     * при запуске запроса 3 (подмененного на мок) выводится сообщение.
     *
     * @throws SQLException
     */
    @Test
    void task4bTest() throws SQLException {

        doAnswer(x -> {
            System.out.println(resource.getString("test.msg"));
            return Integer.parseInt(resource.getString("test.answerReturn"));
        })
                .when(ps).executeUpdate();

        task4b();
        verify(ps4bs3).executeUpdate();
        assertTrue(testSelect(pst1, t4b1));
        assertTrue(testSelect(pst2, t4b2));
        rs.close();
    }

    /**
     * закрытие ресурсов
     *
     * @throws SQLException
     */
    @AfterAll
    public static void close() throws SQLException {

        closeResources();
        closeStatement(ps4bs1);
        closeStatement(ps4bs2);
        closeStatement(ps4bs3);
        closeStatement(pst1);
        closeStatement(pst2);
        closeStatement(pst3);
        closeStatement(ps);
        connection.close();
    }
}
