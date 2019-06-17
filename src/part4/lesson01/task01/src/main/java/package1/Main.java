package package1;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * покрыть код логированием
 * в основных блоках try покрыть уровнем INFO
 * с исключениях catch покрыть уровнем ERROR
 * настроить конфигурацию логера, что бы все логи записывались в БД, таблица LOGS,
 * колонки ID, DATE, LOG_LEVEL, MESSAGE, EXCEPTION
 *  *  *  *  *
 * 1)    Спроектировать базу
 * <p>
 * -      Таблица USER содержит поля id, name, birthday, login_ID, city, email, description
 * <p>
 * -      Таблица ROLE содержит поля id, name (принимает значения Administration, Clients, Billing), description
 * <p>
 * -      Таблица USER_ROLE содержит поля id, user_id, role_id
 * <p>
 * Типы полей на ваше усмотрению, возможно использование VARCHAR(255)
 * <p>
 * <p>
 * <p>
 * 2)      Через jdbc интерфейс сделать запись данных(INSERT) в таблицу
 * <p>
 * a)      Используя параметризированный запрос
 * <p>
 * b)      Используя batch процесс
 * <p>
 * 3)      Сделать параметризированную выборку по login_ID и name одновременно
 * <p>
 * 4)      Перевести connection в ручное управление транзакциями
 * <p>
 * a)      Выполнить 2-3 SQL операции на ваше усмотрение (например, Insert в 3 таблицы – USER, ROLE, USER_ROLE) между
 * sql операциями установить логическую точку сохранения(SAVEPOINT)
 * <p>
 * б)   Выполнить 2-3 SQL операции на ваше усмотрение (например, Insert в 3 таблицы – USER, ROLE, USER_ROLE) между sql
 * операциями установить точку сохранения (SAVEPOINT A), намеренно ввести некорректные данные на последней операции, что
 * бы транзакция откатилась к логической точке SAVEPOINT A
 */
public class Main {
    static Connection connection = null;
    static PreparedStatement ps = null;
    static ResourceBundle resource = ResourceBundle.getBundle("demo");
    static Savepoint savepointB = null, savepointA = null;
    static ResultSet rs = null;
    private static Logger logger = LogManager.getLogger(Main.class);
    /**
     * метод для подключения к БД. Также используется логгером.
     *
     * @return Connection
     */
  public static Connection getConnection() throws SQLException {
        String url = resource.getString("db.url");
        String user = resource.getString("db.user");
        String pass = resource.getString("db.password");
        return DriverManager.getConnection(url, user, pass);
    }

    /**
     * метод для установки параметров параметризованного запроса (insert в таблицу USER в подзадаче 2 )
     *
     * @param id
     * @param name
     * @param birthday
     * @param login_ID
     * @param city
     * @param email
     * @param description поля таблицы USER
     * @throws SQLException обрабатывается в main()
     */
    static void setT2PreparedStatementFields(long id, String name, LocalDate birthday, String login_ID, String city,
                                             String email, String description) throws SQLException {
        ps.setLong(1, id);
        ps.setString(2, name);
        ps.setObject(3, birthday);
        ps.setString(4, login_ID);
        ps.setString(5, city);
        ps.setString(6, email);
        ps.setString(7, description);
    }

    /**
     * подзадача 2а.  Через jdbc интерфейс сделать запись данных(INSERT) в таблицу
     * Используя параметризированный запрос
     *
     * @throws SQLException обрабатывается в main()
     */
    static void task2a() throws SQLException {
        setT2PreparedStatementFields(Long.parseLong(resource.getString("t2a.1")), resource.getString("t2a.2"),
                LocalDate.of(Integer.parseInt(resource.getString("t2a.3.y")),
                        Integer.parseInt(resource.getString("t2a.3.m")), Integer.parseInt(resource.getString("t2a.3.d"))),
                resource.getString("t2a.4"), resource.getString("t2a.5"), resource.getString("t2a.6"),
                resource.getString("t2a.7"));
        ps.executeUpdate();
        logger.info("Вставка с использованием параметризованного запроса");
    }

    /**
     * подзадача 2b.  Через jdbc интерфейс сделать запись данных(INSERT) в таблицу
     * Используя batch процесс
     *
     * @throws SQLException обрабатывается в main()
     */
    static void task2b() throws SQLException {
        setT2PreparedStatementFields(Long.parseLong(resource.getString("t2b.1.1")), resource.getString("t2b.1.2"),
                LocalDate.of(Integer.parseInt(resource.getString("t2b.1.3.y")),
                        Integer.parseInt(resource.getString("t2b.1.3.m")), Integer.parseInt(resource.getString("t2b.1.3.d"))),
                resource.getString("t2b.1.4"), resource.getString("t2b.1.5"), resource.getString("t2b.1.6"),
                resource.getString("t2b.1.7"));
        ps.addBatch();
        setT2PreparedStatementFields(Long.parseLong(resource.getString("t2b.2.1")), resource.getString("t2b.2.2"),
                LocalDate.of(Integer.parseInt(resource.getString("t2b.2.3.y")),
                        Integer.parseInt(resource.getString("t2b.2.3.m")), Integer.parseInt(resource.getString("t2b.2.3.d"))),
                resource.getString("t2b.2.4"), resource.getString("t2b.2.5"), resource.getString("t2b.2.6"),
                resource.getString("t2b.2.7"));
        ps.addBatch();
        ps.executeBatch();
        logger.info("Вставка с использованием batch");
    }

    /**
     * подзадача 3.   Сделать параметризированную выборку по login_ID и name одновременно
     *
     * @throws SQLException обрабатывается в main()
     */
    static void task3() throws SQLException {
        ps = connection.prepareStatement(resource.getString("t3.ps"));
        ps.setString(1, resource.getString("t3.1"));
        ps.setString(2, resource.getString("t3.2"));
        rs = ps.executeQuery();
        System.out.println(resource.getString("t3.ptext"));
        while (rs.next()) {
            System.out.println(rs.getString(2) + " " + rs.getString(1));
        }
        logger.info("Выборка по login_ID и name");
    }

    /**
     * подзадача 4a.  Перевести connection в ручное управление транзакциями. Выполнить 2-3 SQL операции на ваше
     * усмотрение (например, Insert в 3 таблицы – USER, ROLE, USER_ROLE) между sql операциями установить логическую
     * точку сохранения(SAVEPOINT)
     *
     * @throws SQLException обрабатывается в main()
     */
    static void task4a() throws SQLException {
        connection.setAutoCommit(false);
        connection.prepareStatement(resource.getString("t4a.ps1")).executeUpdate();
        connection.prepareStatement(resource.getString("t4a.ps2")).executeUpdate();
        savepointB = connection.setSavepoint(resource.getString("t4a.sp"));
        connection.prepareStatement(resource.getString("t4a.ps3")).executeUpdate();
        logger.info("Операции \"(4a)\" проведены");
    }

    /**
     * подзадача 4б.   Выполнить 2-3 SQL операции на ваше усмотрение (например, Insert в 3 таблицы – USER, ROLE,
     * USER_ROLE) между sql операциями установить точку сохранения (SAVEPOINT A), намеренно ввести некорректные данные
     * на последней операции, что бы транзакция откатилась к логической точке SAVEPOINT A
     *
     * @throws SQLException обрабатывается в main()
     */
    static void task4b() throws SQLException {
        connection.prepareStatement(resource.getString("t4b.ps1")).executeUpdate();
        connection.prepareStatement(resource.getString("t4b.ps2")).executeUpdate();
        savepointA = connection.setSavepoint(resource.getString("t4b.sp"));
        connection.prepareStatement(resource.getString("t4b.ps3")).executeUpdate();
        logger.info("Операции \"(4б)\" проведены");
    }

    /**
     * подзадачи выполняются соответствующими методами, например task2a().
     * Исключения обрабатываются в main()
     *
     */
    public static void main(String[] args) {
        boolean flag = false;
        try {
            connection = getConnection();
            logger.info("Установлено соединение");
        } catch (SQLException e) {
            System.out.println(resource.getString("db.error"));
            flag = true;
            logger.error("Ошибка при соединении с БД",e);
        }
        if (!flag) {
            try {
                ps = connection.prepareStatement(resource.getString("t2.ps"));
                task2a();
                task2b();
                if (ps != null)
                    ps.close();
                task3();
                task4a();
                task4b();
                connection.commit();
            } catch (SQLException e) {
                try {
                    if ((savepointA != null)&&(!connection.getAutoCommit()))
                    {
                        logger.error("Ошибка при выполнении \"(4б)\"",e);
                        connection.rollback(savepointA);
                        connection.commit();
                        logger.info("Успешный rollback");
                        //e.printStackTrace();
                    }

                } catch (SQLException ex) {
                    logger.error("Ошибка при rollback",ex);
                    //ex.printStackTrace();
                }
            } finally {
                try {
                    if (rs != null)
                        rs.close();
                    if (ps != null)
                        ps.close();
                    connection.close();
                    logger.info("Закрыто соединение");
                } catch (SQLException e) {
                    logger.error("Ошибка при закрытии ресурсов",e);
                    //e.printStackTrace();
                }
            }
        }
    }
}
