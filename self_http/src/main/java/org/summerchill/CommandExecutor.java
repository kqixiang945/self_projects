package org.summerchill;

import com.google.common.base.Preconditions;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CommandExecutor {
    private final static Logger LOG = Logger.getLogger(CommandExecutor.class);
    private final static int CMD_EXEC_TIMEOUT_MS = 60000;
    private final static Map<String, String> DEFAULT_ENV = new HashMap<String, String>();

    static {
        DEFAULT_ENV.put("PATH", "/usr/bin:/bin:/usr/sbin:/sbin:/usr/local/bin");
    }

    public int runCmd(String cmd,String projectDir) throws ExecuteException, IOException, Exception {
        Preconditions.checkNotNull(cmd);

        DefaultExecutor executor = new DefaultExecutor();
        ExecuteWatchdog watchdog = new ExecuteWatchdog(CMD_EXEC_TIMEOUT_MS);

        executor.setWorkingDirectory(new File(projectDir));
        executor.setWatchdog(watchdog);
        LOG.info("cmd:" + cmd);
        CommandLine projectCmd = CommandLine.parse(cmd);

        int exitValue =-1;

        exitValue = executor.execute(projectCmd, DEFAULT_ENV);

        return exitValue;
    }

}
