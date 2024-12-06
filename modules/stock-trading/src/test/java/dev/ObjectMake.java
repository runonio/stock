package dev;

import io.runon.commons.config.Config;
import io.runon.jdbc.connection.ApplicationConnectionPool;
import io.runon.jdbc.objects.JdbcObjects;

/**
 * @author macle
 */
public class ObjectMake {

    public static void main(String[] args) {

        Config.getConfig("");
        //noinspection ResultOfMethodCallIgnored
        ApplicationConnectionPool.getInstance();

        String tableName = "event_calendar";
        System.out.println("class make info");
        System.out.println(JdbcObjects.makeObjectValue(tableName, false));

    }


}
