package com.uottawa.cloud.util;

import com.uottawa.cloud.model.ActivityState;
import com.uottawa.cloud.model.PatientInfo;
import com.uottawa.cloud.model.StatisticalHistoryData;

import java.math.BigDecimal;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

public class ConnectToRedshiftCluster {
    //Redshift driver: "jdbc:redshift://x.y.us-west-2.redshift.amazonaws.com:5439/dev";
    static final String dbURL = "jdbc:redshift://redshift-cluster-1.c1utsnrzyxby.us-east-1.redshift.amazonaws.com:5439/dev";
    static final String MasterUsername = "awsuser";
    static final String MasterUserPassword = "!Cloudbasedproject2020";
    private static Properties prop = null;

    // TODO: thread-safe?
    public static void setProp(Properties _prop) {
        prop = _prop;
    }

    static Connection getClusterConnection() {
        Connection conn = null;

        try {
            //Dynamically load driver at runtime.
            //Redshift JDBC 4.1 driver: com.amazon.redshift.jdbc41.Driver
            //Redshift JDBC 4 driver: com.amazon.redshift.jdbc4.Driver
            Class.forName("com.amazon.redshift.jdbc.Driver");

            //Open a connection and define properties.
            System.out.println("Connecting to database...");
            Properties props = new Properties();

            //Uncomment the following line if using a keystore.
            //props.setProperty("ssl", "true");
            props.setProperty("user", MasterUsername);
            props.setProperty("password", MasterUserPassword);
            props.setProperty("tcpKeepAlive", "true");
            props.setProperty("TCPKeepAliveMinutes", "10");
            conn = DriverManager.getConnection(dbURL, props);
        } catch (Exception ex) {
            //For convenience, handle all errors here.
            ex.printStackTrace();
        }

        return conn;
    }

    public static void insertTableInRedshift(PatientInfo patientInfo) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
//        if (Boolean.parseBoolean(prop.getProperty("disableAws"))) {
//            System.out.println("AWS disabled, log for debug.");
//            return;
//        }

        try {
            //Try a simple query.
            System.out.println("Listing system tables...");
            conn = getClusterConnection();
            String sql = "insert into " + prop.getProperty("awsTable") + " (deviceID, epoch, datainterval, range, breathRate, fallDetected, detected)"
                    + " values (?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, patientInfo.getDeviceID());
            preparedStatement.setBigDecimal(2, BigDecimal.valueOf(patientInfo.getEpoch()));
            preparedStatement.setBigDecimal(3, BigDecimal.valueOf(patientInfo.getInterval()));
            preparedStatement.setDouble(4, patientInfo.getHeartRate());
            preparedStatement.setDouble(5, patientInfo.getBreathRate());
            preparedStatement.setBoolean(6, patientInfo.getFallDetected());
            preparedStatement.setInt(7, patientInfo.getDetected());
            preparedStatement.executeUpdate();
            sql = "update " + prop.getProperty("awsTable") +
                    " set ltime = (timestamp 'epoch' + epoch * interval '1 second')" +
                    " where epoch = " +
                    patientInfo.getEpoch();
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.executeUpdate();
            conn.close();
        } catch (Exception ex) {
            //For convenience, handle all errors here.
            ex.printStackTrace();
        } finally {
            //Finally block to close resources.
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } catch (Exception ex) {
            }// nothing we can do
            //Finally block to close resources.
            try {
                if (conn != null)
                    conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void createTableInRedshift() {
//        if (Boolean.parseBoolean(prop.getProperty("disableAws"))) {
//            System.out.println("AWS disabled, log for debug.");
//            return;
//        }
        Statement stmt = null;
        Connection conn = null;

        String sql = "create table if not exists " +
                prop.getProperty("awsTable") +
                " (id bigint identity(1,1)," +
                "deviceID varchar(1000) not null," +
                "epoch bigint not null sortkey," +
                "ltime timestamp," +
                "datainterval bigint not null," +
                "range DOUBLE PRECISION," + // heart rate
                "breathRate DOUBLE PRECISION," +
                "fallDetected BOOLEAN," +
                "detected Integer," +
                "primary key(id)" +
                ")";
        try {
            //Try a simple query.
            System.out.println("Listing system tables...");
            conn = getClusterConnection();

            stmt = conn.createStatement();
            stmt.execute(sql);
            conn.close();
        } catch (Exception ex) {
            //For convenience, handle all errors here.
            ex.printStackTrace();
        } finally {
            //Finally block to close resources.
            try {
                if (stmt != null)
                    stmt.close();
            } catch (Exception ex) {
            }// nothing we can do
            //Finally block to close resources.
            try {
                if (conn != null)
                    conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static String convertDateToString(Date date) {
        String pattern = "yyyy-MM-dd"; // HH:mm:ss
        DateFormat df = new SimpleDateFormat(pattern);
        String dateAsString = df.format(date);
        System.out.println("convert Date to string, which is: " + dateAsString);
        return dateAsString;
    }

    public static List<StatisticalHistoryData> execQueryDaily(String deviceID, String startdate, String enddate) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        List<StatisticalHistoryData> list = new ArrayList<>();
        try {
            //Try a simple query.
            System.out.println("Start queries...");
            conn = getClusterConnection();
            String sql = "select cast(ltime as date) AS date, avg(breathRate) AS avgrate, STDDEV_SAMP(breathRate) AS stddevrate from " + prop.getProperty("awsTable") +
                    " where detected = 1 and deviceID = ? and cast(ltime as date) between ? and ?" +
                    " group by 1 order by 1 ASC;";
            System.out.print(deviceID + "," + startdate + "," + enddate);
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, deviceID);
            preparedStatement.setString(2, startdate);
            preparedStatement.setString(3, enddate);
            System.out.println(preparedStatement.toString());
            ResultSet rs = preparedStatement.executeQuery();
            //Get the data from the result set.
            while (rs.next()) {
                //Retrieve two columns.
                list.add(new StatisticalHistoryData(deviceID, convertDateToString(rs.getDate("date")),
                        rs.getDouble("avgrate"), rs.getDouble("stddevrate")));
                System.out.println(list.get(list.size() - 1).toString());
            }
            conn.close();
        } catch (Exception ex) {
            //For convenience, handle all errors here.
            ex.printStackTrace();
        } finally {
            //Finally block to close resources.
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } catch (Exception ex) {
            }// nothing we can do
            //Finally block to close resources.
            try {
                if (conn != null)
                    conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return list;
    }

    public static List<StatisticalHistoryData> execQueryHourly(String deviceID, String startdate, String enddate) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        List<StatisticalHistoryData> list = new ArrayList<>();
        try {
            //Try a simple query.
            System.out.println("Start queries...");
            conn = getClusterConnection();
            String sql = "select to_char(ltime, 'YYYY-MM-DD HH24') AS dateandhour, avg(breathRate) AS avgrate, STDDEV_SAMP(breathRate) AS stddevrate from " + prop.getProperty("awsTable") +
                    " where detected = 1 and deviceID = ? and cast(ltime AS date) between ? and ?" +
                    " group by 1 order by 1 ASC;";
            System.out.print(prop.getProperty("awsTable") + "," + deviceID + "," + startdate + "," + enddate);
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, deviceID);
            preparedStatement.setString(2, startdate);
            preparedStatement.setString(3, enddate);
            System.out.println(preparedStatement.toString());
            ResultSet rs = preparedStatement.executeQuery();
            //Get the data from the result set.
            while (rs.next()) {
                //Retrieve two columns.
                list.add(new StatisticalHistoryData(deviceID, rs.getString("dateandhour"),
                        rs.getDouble("avgrate"), rs.getDouble("stddevrate")));
                System.out.println(list.get(list.size() - 1).toString());
            }
            conn.close();
        } catch (Exception ex) {
            //For convenience, handle all errors here.
            ex.printStackTrace();
        } finally {
            //Finally block to close resources.
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } catch (Exception ex) {
            }// nothing we can do
            //Finally block to close resources.
            try {
                if (conn != null)
                    conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return list;
    }

    public static ActivityState execQueryActivityState(String deviceID, String startdate, String enddate) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ActivityState activestate = null;
        try {
            //Try a simple query.
            System.out.println("Start queries...");
            conn = getClusterConnection();
            String sql = "SELECT sum(case when detected = 1 then 1 end) as activecount, count(*) as totalcount " +
                    "FROM " + prop.getProperty("awsTable") +
                    " where deviceid = ? and cast(ltime as date) between ? and ?;";
            System.out.print(prop.getProperty("awsTable") + "," + deviceID + "," + startdate + "," + enddate);
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, deviceID);
            preparedStatement.setString(2, startdate);
            preparedStatement.setString(3, enddate);
            ResultSet rs = preparedStatement.executeQuery();
            //Get the data from the result set.
            while (rs.next()) {
                //Retrieve two columns.
                activestate = new ActivityState(deviceID, startdate, enddate,
                        rs.getInt("activecount"), rs.getInt("totalcount"),
                        calcPercentage(rs.getInt("activecount"), rs.getInt("totalcount")));
                System.out.println(activestate.toString());
            }
            conn.close();
        } catch (Exception ex) {
            //For convenience, handle all errors here.
            ex.printStackTrace();
        } finally {
            //Finally block to close resources.
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } catch (Exception ex) {
            }// nothing we can do
            //Finally block to close resources.
            try {
                if (conn != null)
                    conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return activestate;
    }

    static public double calcPercentage(int activenum, int totalnum) {
        if(totalnum == 0) {// devided by 0
            return -1;
        } else {
            return (double)activenum / totalnum;
        }
    }

}
