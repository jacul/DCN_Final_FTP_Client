/**
 * This interface defines most often used FTP commands.<br>
 * All commands and brief explanations here are from<br>
 * <a href="http://www.nsftools.com/tips/RawFTP.htm"
 * >http://www.nsftools.com/tips/RawFTP.htm</a>
 * 
 * 
 * @author zxd
 * 
 */
public interface FTPCommands {
	/**
	 * send username
	 */
	static final String USER = "USER";
	/**
	 * send password
	 */
	static final String PASS = "PASS";
	/**
	 * print working directory
	 */
	static final String PWD = "PWD";
	/**
	 * set transfer type
	 */
	static final String TYPE = "TYPE";
	/**
	 * enter passive mode
	 */
	static final String PASV = "PASV";
	/**
	 * return the size of a file
	 */
	static final String SIZE = "SIZE";
	/**
	 * list remote files
	 */
	static final String LIST = "LIST";
	/**
	 * abort a file transfer
	 */
	static final String ABOR = "ABOR";
	/**
	 * change working directory
	 */
	static final String CWD = "CWD";
	/**
	 * delete a remote file
	 */
	static final String DELE = "DELE";
	/**
	 * return the modification time of a file
	 */
	static final String MDTM = "MDTM";
	/**
	 * make a remote directory
	 */
	static final String MKD = "MKD";
	/**
	 * name list of remote directory
	 */
	static final String NLST = "NLST";
	/**
	 * open a data port
	 */
	static final String PORT = "PORT";
	/**
	 * terminate the connection
	 */
	static final String QUIT = "QUIT";
	/**
	 * retrieve a remote file
	 */
	static final String RETR = "RETR";
	/**
	 * remove a remote directory
	 */
	static final String RMD = "RMD";
	/**
	 * rename from
	 */
	static final String RNFR = "RNFR";
	/**
	 * rename to
	 */
	static final String RNTO = "RNTO";
	/**
	 * site-specific commands
	 */
	static final String SITE = "SITE";
	/**
	 * store a file on the remote host
	 */
	static final String STOR = "STOR";
	/**
	 * send account information. <br>
	 * Not implemented in a number of FTP servers.
	 */
	static final String ACCT = "ACCT";
	/**
	 * append to a remote file
	 */
	static final String APPE = "APPE";
	/**
	 * CWD to the parent of the current directory
	 */
	static final String CDUP = "CDUP";
	/**
	 * return help on using the server
	 */
	static final String HELP = "HELP";
	/**
	 * set transfer mode
	 */
	static final String MODE = "MODE";
	/**
	 * do nothing
	 */
	static final String NOOP = "NOOP";
	/**
	 * reinitialize the connection. <br>
	 * Not implemented in a number of FTP servers.
	 */
	static final String REIN = "REIN";
	/**
	 * return server status
	 */
	static final String STAT = "STAT";
	/**
	 * store a file uniquely
	 */
	static final String STOU = "STOU";
	/**
	 * set file transfer structure
	 */
	static final String STRU = "STRU";
	/**
	 * return system type
	 */
	static final String SYST = "SYST";
	/**
	 * list all features of FTP server.
	 */
	static final String FEAT = "FEAT";
	
	static final String JIAMI = "JIAMI";
}
