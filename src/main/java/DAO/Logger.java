package DAO;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class Logger
{

    private static final String logPath = "login_activity.txt";

    public static void auditLogin(String userName, Boolean successBool) throws IOException {

            BufferedWriter logger = new BufferedWriter( new FileWriter(logPath, true));
            logger.append(ZonedDateTime.now(ZoneOffset.UTC).toString()).append(" UTC- LOGIN ATTEMPT-USERNAME ").append(userName).append("LOGIN SUCCESSFUL ").append(successBool.toString()).append("\n");
            logger.flush();
            logger.flush();
            logger.close();
    }
}
