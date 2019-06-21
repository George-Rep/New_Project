package package1;


import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * покрыть код логированием
 * в основных блоках try покрыть уровнем INFO
 * с исключениях catch покрыть уровнем ERROR
 * настроить конфигурацию логера, что бы все логи записывались в БД, таблица LOGS,
 * колонки ID, DATE, LOG_LEVEL, MESSAGE, EXCEPTION
 * *  *  *  *
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
   public static Connection connection = null;
    public static PreparedStatement ps2a = null, ps2b = null, ps3 = null, ps4as1 = null, ps4as2 = null, ps4as3 = null, ps4as4 = null,
            ps4as5 = null, ps4bs1 = null, ps4bs2 = null, ps4bs3 = null;
    public static ResourceBundle resource = ResourceBundle.getBundle("demo");
    public static Savepoint savepointB = null, savepointA = null;
    static ResultSet rs = null;

    /**
     * метод для подключения к БД.
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
     * метод для инициализации preparedStatements
     *
     * @throws SQLException
     */
    public static void initStatements() throws SQLException {
        ps2a = connection.prepareStatement(resource.getString("t2.ps"));
        ps2b = connection.prepareStatement(resource.getString("t2.ps"));
        setT2PreparedStatementFields(ps2a, Long.parseLong(resource.getString("t2a.1")), resource.getString("t2a.2"),
                LocalDate.of(Integer.parseInt(resource.getString("t2a.3.y")),
                        Integer.parseInt(resource.getString("t2a.3.m")), Integer.parseInt(resource.getString("t2a.3.d"))),
                resource.getString("t2a.4"), resource.getString("t2a.5"), resource.getString("t2a.6"),
                resource.getString("t2a.7"));
        setT2PreparedStatementFields(ps2b, Long.parseLong(resource.getString("t2b.1.1")), resource.getString("t2b.1.2"),
                LocalDate.of(Integer.parseInt(resource.getString("t2b.1.3.y")),
                        Integer.parseInt(resource.getString("t2b.1.3.m")), Integer.parseInt(resource.getString("t2b.1.3.d"))),
                resource.getString("t2b.1.4"), resource.getString("t2b.1.5"), resource.getString("t2b.1.6"),
                resource.getString("t2b.1.7"));
        ps2b.addBatch();
        setT2PreparedStatementFields(ps2b, Long.parseLong(resource.getString("t2b.2.1")), resource.getString("t2b.2.2"),
                LocalDate.of(Integer.parseInt(resource.getString("t2b.2.3.y")),
                        Integer.parseInt(resource.getString("t2b.2.3.m")), Integer.parseInt(resource.getString("t2b.2.3.d"))),
                resource.getString("t2b.2.4"), resource.getString("t2b.2.5"), resource.getString("t2b.2.6"),
                resource.getString("t2b.2.7"));
        ps2b.addBatch();
        ps3 = connection.prepareStatement(resource.getString("t3.ps"));
        ps3.setString(1, resource.getString("t3.1"));
        ps3.setString(2, resource.getString("t3.2"));
        ps4as1 = connection.prepareStatement(resource.getString("t4a.ps1"));
        ps4as2 = connection.prepareStatement(resource.getString("t4a.ps2"));
        ps4as3 = connection.prepareStatement(resource.getString("t4a.ps3"));
        ps4as4 = connection.prepareStatement(resource.getString("t4a.ps4"));
        ps4as5 = connection.prepareStatement(resource.getString("t4a.ps5"));
        ps4bs1 = connection.prepareStatement(resource.getString("t4b.ps1"));
        ps4bs2 = connection.prepareStatement(resource.getString("t4b.ps2"));
        ps4bs3 = connection.prepareStatement(resource.getString("t4b.ps3"));


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
    static void setT2PreparedStatementFields(PreparedStatement ps, long id, String name, LocalDate birthday, String login_ID, String city,
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
    public static void task2a() throws SQLException {
        ps2a.executeUpdate();

    }

    /**
     * подзадача 2b.  Через jdbc интерфейс сделать запись данных(INSERT) в таблицу
     * Используя batch процесс
     *
     * @throws SQLException обрабатывается в main()
     */
    public static void task2b() throws SQLException {
        ps2b.executeBatch();

    }

    /**
     * подзадача 3.   Сделать параметризированную выборку по login_ID и name одновременно
     * @return ResultSet с результатами
     * @throws SQLException обрабатывается в main()
     */
 public   static ResultSet task3() throws SQLException {
        return ps3.executeQuery();


    }

    /**
     * подзадача 4a.  Перевести connection в ручное управление транзакциями. Выполнить 2-3 SQL операции на ваше
     * усмотрение (например, Insert в 3 таблицы – USER, ROLE, USER_ROLE) между sql операциями установить логическую
     * точку сохранения(SAVEPOINT)
     *
     * @throws SQLException обрабатывается в main()
     */
  public  static void task4a() throws SQLException {
        connection.setAutoCommit(false);
        ps4as1.executeUpdate();
        ps4as2.executeUpdate();
        savepointB = connection.setSavepoint(resource.getString("t4a.sp"));
        ps4as3.executeUpdate();
        ps4as4.executeUpdate();
        ps4as5.executeUpdate();
        connection.commit();
    }

    /**
     * подзадача 4б.   Выполнить 2-3 SQL операции на ваше усмотрение (например, Insert в 3 таблицы – USER, ROLE,
     * USER_ROLE) между sql операциями установить точку сохранения (SAVEPOINT A), намеренно ввести некорректные данные
     * на последней операции, что бы транзакция откатилась к логической точке SAVEPOINT A
     *
     *
     */
  public  static void task4b() {

        try {
            connection.setAutoCommit(false);
            ps4bs1.executeUpdate();
            ps4bs2.executeUpdate();
            savepointA = connection.setSavepoint(resource.getString("t4b.sp"));
            ps4bs3.executeUpdate();
        } catch (SQLException e) {
            try {
                if ((savepointA != null) && (!connection.getAutoCommit())) {
                    connection.rollback(savepointA);
                    connection.commit();
                   // e.printStackTrace();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }

    /**
     * метод для закрытия PreparedStatement
     *
     * @param ps PreparedStatement для закрытия
     * @throws SQLException обрабатывается в main()
     */
  public  static void closeStatement(PreparedStatement ps) throws SQLException {
        if (ps != null)
            ps.close();
    }
    /**
     * метод для закрытия ресурсов до подзадачи "4 б"
     *
     * @throws SQLException обрабатывается в main()
     */
    public static void closeResources() throws SQLException {
        if (rs != null)
            rs.close();
        closeStatement(ps2a);
        closeStatement(ps2b);
        closeStatement(ps3);
        closeStatement(ps4as1);
        closeStatement(ps4as2);
        closeStatement(ps4as3);
        closeStatement(ps4as4);
        closeStatement(ps4as5);


    }

    /**
     * подзадачи выполняются соответствующими методами, например task2a().
     * Исключения обрабатываются в main()
     */
    public static void main(String[] args) {
        boolean flag = false;
        try {
            connection = getConnection();
        } catch (SQLException e) {
            System.out.println(resource.getString("db.error"));
            flag = true;

        }
        if (!flag) {
            try {
                initStatements();
                task2a();
                task2b();
                if (rs != null)
                    rs.close();
                task3();
                System.out.println(resource.getString("t3.ptext"));
                while (rs.next()) {
                    System.out.println(rs.getString(1) + " " + rs.getString(2));
                }
                task4a();


            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    closeResources();
                } catch (SQLException e) {
                    //e.printStackTrace();
                }
            }
            task4b();
                try {
                    closeResources();
                    closeStatement(ps4bs1);
                    closeStatement(ps4bs2);
                    closeStatement(ps4bs3);
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }



        }
    }
}
