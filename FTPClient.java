/**
 * Xiangdong Zhu<br>
 * CSC 623: Data Communications and Networking<br>
 * Date: 12/4/2011<br>
 * Final Project: A Java based socket FTP implementation.
 * 
 */
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * This class represents the ftp client, user can setup the connection to a
 * server, then perform varies actions.<br>
 * Also, caller of this class may retrieve the output(commands sent and response
 * from server) by {@link #getOutputs()}.
 * 
 * @author zxd
 * 
 */
public class FTPClient implements FTPCommands {

	/**
	 * Algorithm used in encryption.
	 */
	static final String encryptionAlgorithm = "AES";
	/**
	 * Current working directory.
	 */
	private String currentPath;
	/**
	 * Url of server.
	 */
	private String serverurl;
	/**
	 * Port of the server this client connects to.
	 */
	private int port;
	/**
	 * This listener is used in active mode.
	 */
	private ServerSocket activelistener;
	/**
	 * Listening port of active mode.
	 */
	private int activeport = 10001;
	/**
	 * Socket connection to send commands and receive response.
	 */
	private Socket cmdSocket;
	/**
	 * To send commands.
	 */
	private PrintWriter cmdWriter;
	/**
	 * To receive response.
	 */
	private BufferedReader cmdReader;
	/**
	 * Socket connection to send/receive data.
	 */
	private Socket dataSocket;
	/**
	 * Send data to server.
	 */
	private OutputStream dataWriter;
	/**
	 * Read data from server.
	 */
	private InputStream dataReader;
	/**
	 * Login information.
	 */
	private String username;
	/**
	 * Authorization information.
	 */
	private String password;
	/**
	 * true if the data connection is established with passive mode. Otherwise
	 * use active mode.
	 */
	private boolean usePassive = true;
	/**
	 * These three are current secured states.
	 */
	static final int PLAIN = 0;
	static final int TOSECURE = 1;
	static final int SECURED = 2;
	/**
	 * 0 if the connection is not secured. 1 if the connection should be secured
	 * but not performed yet. 2 means the connection is secured now.
	 */
	private int isSecured = PLAIN;
	/**
	 * This stores all the output this client yields. Caller may retrieve the
	 * output.
	 */
	private StringBuilder outputs = new StringBuilder();

	/**
	 * Constructor with server only. User will be anonymous, port number will by
	 * default be 21.
	 * 
	 * @param server
	 *            Server's url.
	 */
	public FTPClient(String server) {
		this(server, 21, "anonymous", "");
	}

	/**
	 * Constructor with server name, port number, user name and password.
	 * 
	 * @param server
	 *            Server's url
	 * @param port
	 *            port to establish the connection
	 * @param user
	 *            Login user name
	 * @param pass
	 *            Password
	 */
	public FTPClient(String server, int port, String user, String pass) {
		serverurl = server;
		this.port = port;
		setLoginInfo(user, pass);
	}

	/**
	 * Set the user name and password for login.
	 * 
	 * @param user
	 *            Username
	 * @param pass
	 *            Password
	 */
	public void setLoginInfo(String user, String pass) {
		if (user == null || user.length() == 0)
			user = "anonymous";
		username = user;
		password = pass;
	}

	/**
	 * Set the data connection mode to passive or not.
	 * 
	 * @param ispassive
	 *            true to make the connection established with passive mode.
	 *            Otherwise use active mode.
	 */
	public void setPassiveMode(boolean ispassive) {
		usePassive = ispassive;
	}

	/**
	 * Set the command and data connection to be secured or not.
	 * 
	 * @param isSecured
	 *            true to set all connection secured; false to be not secured.
	 */
	public void setSecureMode(boolean isSecured) {
		this.isSecured = isSecured ? TOSECURE : PLAIN;
	}

	/**
	 * Establish the connection to the server. Remote server should be specified
	 * in the constructor or {@link #setLoginInfo(String, String)}.
	 * 
	 * @return true if login is successful; otherwise false.
	 * @throws IOException
	 * @throws UnknownHostException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws InvalidKeyException
	 */
	public boolean openConnection() throws UnknownHostException, IOException, InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
		cmdSocket = new Socket(serverurl, port);

		cmdSocket.setSendBufferSize(0x100000); // 1Mb
		cmdSocket.setReceiveBufferSize(0x100000); // 1Mb
		cmdSocket.setKeepAlive(true);
		cmdSocket.setReuseAddress(true);
		cmdSocket.setSoLinger(false, 0);
		cmdSocket.setTcpNoDelay(false);

		cmdReader = new BufferedReader(new InputStreamReader(cmdSocket.getInputStream()));
		cmdWriter = new PrintWriter(cmdSocket.getOutputStream());
		read(false);

		if (isSecured == TOSECURE) {
			send(JIAMI);
			read();
			this.enterEncryptionMode();
		}
		send(USER + " " + username);
		if (!read().startsWith("331")) {// 331 Password required
			return false;
		}
		send(PASS + " " + password);
		return read().startsWith("230");// 230 User logged in.
	}

	private Cipher encipher;
	private Cipher decipher;

	/**
	 * To create encrypted data input/ output streams to send/ receive commands
	 * and data.
	 * 
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws IOException
	 */
	private void enterEncryptionMode() throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException,
			NoSuchAlgorithmException, NoSuchPaddingException, IOException {

		// Read key data from file.
		FileInputStream fis = new FileInputStream("key");
		String plainkey = new BufferedReader(new InputStreamReader(fis)).readLine();
		byte[] raw = Base64.decode(plainkey.getBytes(), Base64.DEFAULT);

		SecretKeySpec skeySpec = new SecretKeySpec(raw, encryptionAlgorithm);

		encipher = Cipher.getInstance(encryptionAlgorithm);
		decipher = Cipher.getInstance(encryptionAlgorithm);

		encipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		decipher.init(Cipher.DECRYPT_MODE, skeySpec);

		isSecured = SECURED;
	}

	public String getRemoteWorkingDir() {
		return currentPath;
	}

	private static void generateKey() {
		// *** Get the KeyGenerator
		KeyGenerator kgen;
		try {
			kgen = KeyGenerator.getInstance(encryptionAlgorithm);
			kgen.init(128);
			// *** Generate the secret key specs.
			SecretKey skey = kgen.generateKey();
			byte[] raw = skey.getEncoded();
			System.out.println(raw.length);
			System.out.println(Base64.encodeToString(raw, Base64.DEFAULT));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * When sending or getting a file, we need to open a data link. Here we do
	 * this in passive mode, thus the server open a port and we connect it.
	 * 
	 * @throws IOException
	 */
	private void establishDataLink() throws IOException {
		if (usePassive) {
			connectPassive();
		} else {
			connectActive();
		}
	}

	/**
	 * Establish data connection to the server in passive mode.
	 * 
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	private void connectPassive() throws UnknownHostException, IOException {
		send(PASV);
		String response = read();
		String[] address_port = getPassiveAddressAndPort(response);
		dataSocket = new Socket(address_port[0], Integer.parseInt(address_port[1]));
		if (isSecured == SECURED) {
			dataReader = new CipherInputStream(new Base64InputStream(dataSocket.getInputStream(), Base64.DEFAULT), decipher);
			dataWriter = new CipherOutputStream(new Base64OutputStream(dataSocket.getOutputStream(), Base64.DEFAULT), encipher);
		} else {
			dataReader = dataSocket.getInputStream();
			dataWriter = dataSocket.getOutputStream();
		}
	}

	/**
	 * Establish data connection to the server in active mode.
	 * 
	 * @throws IOException
	 */
	private void connectActive() throws IOException {
		// Open the listening port first.
		if (activelistener == null) {
			activeport = 10001 + new Random().nextInt(30000);
			activelistener = new ServerSocket(activeport, 6);
		}
		Thread thread = new Thread() {

			public void run() {
				try {
					dataSocket = activelistener.accept();
					dataReader = dataSocket.getInputStream();
					dataWriter = dataSocket.getOutputStream();
				} catch (IOException e) {
					System.err.println(e.getClass().getName() + e.getMessage());
				}
			}
		};
		thread.start();

		// send the PORT command.
		String localaddress = cmdSocket.getLocalAddress().getHostAddress();
		int p1 = activeport >> 8;
		int p2 = activeport & 0xff;

		send(PORT + " " + localaddress.replace('.', ',') + "," + p1 + "," + p2);
		this.read();
	}

	/**
	 * In passive mode, we need to resolve the data connection address and port
	 * number opened by server.
	 * 
	 * @param serverResponse
	 *            Full response message from the server.
	 * @return Resolved port number.
	 */
	private String[] getPassiveAddressAndPort(String serverResponse) {
		String port = serverResponse.substring(serverResponse.indexOf('(') + 1, serverResponse.indexOf(')'));
		String[] split = port.split(",");
		String address = split[0] + "." + split[1] + "." + split[2] + "." + split[3];
		int p = (Integer.parseInt(split[4]) << 8) + (Integer.parseInt(split[5]));
		return new String[] { address, String.valueOf(p) };
	}

	/**
	 * When we set up a data link in extensive passive mode, we have different
	 * response message. So resolve it in another way.
	 * 
	 * @param serverResponse
	 *            Full response message from the server.
	 * @return Port number
	 */
	private int getExPassivePort(String serverResponse) {
		int port = Integer.parseInt(serverResponse.substring(serverResponse.indexOf('|') + 3, serverResponse.lastIndexOf('|')));
		return port;
	}

	/**
	 * Get the current working directory.
	 * 
	 * @return Full path of current directory.
	 * @throws IOException
	 */
	public String printWorkingDir() throws IOException {
		send(PWD);
		String response = read();
		currentPath = response.substring(response.indexOf('\"') + 1, response.lastIndexOf('\"'));
		return response;
	}

	/**
	 * List all the sub files of the current directory. This includes the
	 * folders.
	 * 
	 * @return A collection of files we retrieved. or null if something is
	 *         wrong.
	 * @throws IOException
	 */
	public FileElement[] list() throws IOException {
		establishDataLink();
		send(LIST);
		// While we are waiting for this respond, server is connecting our port.
		read();
		ArrayList<FileElement> files = new ArrayList<FileElement>();
		String line = null;
		int len;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		while (-1 != (len = dataReader.read(b, 0, b.length))) {
			baos.write(b, 0, len);
		}
		baos.flush();

		BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(baos.toByteArray())));
		while (null != (line = reader.readLine())) {
			System.out.println(line);
			String[] split = line.split(" |\t");
			if (split.length < 9) {
				continue;
			} else {
				int counter = 0;
				int i;
				StringBuffer filename = new StringBuffer();
				for (i = 0; i < split.length; i++) {
					if (split[i].length() > 0)
						counter++;
					if (counter > 8)
						filename.append(split[i]).append(' ');
				}
				filename.deleteCharAt(filename.length() - 1);

				FileElement file = new FileElement(filename.toString(), line.startsWith("d"));
				if (file.isDirectory()) {// Directory should be added from the
					// beginning.
					files.add(0, file);
				} else {
					files.add(file);
				}

				outputs.append(" >" + file.getFileName() + "\n");
			}
		}
		// After transmission, we should close the data link.
		if (activelistener != null) {
			activelistener.close();
			activelistener = null;
		}
		dataSocket.close();
		dataSocket = null;
		read();
		if (!currentPath.equals("/")) {
			files.add(0, new FileElement("..", true));
		}
		if (files.isEmpty()) {
			return null;
		} else {
			FileElement[] filearray = new FileElement[files.size()];
			files.toArray(filearray);
			return filearray;
		}
	}

	/**
	 * Change to another directory.
	 * 
	 * @param dir
	 *            name of the directory to switch to. Must be the sub directory
	 *            of the current working directory or "." "..".
	 * @return true if this command is successful, otherwise false.
	 * @throws IOException
	 */
	public boolean changeDir(String dir) throws IOException {
		send(CWD + " " + dir);
		if (dir.equals("..") && !currentPath.equals('/')) {
			currentPath = currentPath.substring(0, currentPath.lastIndexOf('/'));
		} else if (!dir.equals("..")) {
			currentPath += "/" + dir;
		}
		return read().startsWith("250");
	}

	/**
	 * Go to parent directory.
	 * 
	 * @throws IOException
	 */
	public boolean changeDirUp() throws IOException {
		send(CDUP);
		return read().startsWith("250");
	}

	/**
	 * Download a file from the server.
	 * 
	 * @param filename
	 *            Name of the file only. It must be under the current working
	 *            directory.
	 * @param localDir
	 *            URL of local file system directory, where to save the file.
	 * @return true if this file is downloaded successfully; otherwise false.
	 * @throws IOException
	 */
	public boolean retrieve(String filename, String localDir) throws IOException {
		establishDataLink();
		// set transfer mode to binary
		send(TYPE + " I");
		read();
		// send retrieve command
		send(RETR + " " + filename);

		if (!read().startsWith("150")) {
			return false;
		}

		byte[] b = new byte[1024];
		int len = -1;
		// Here we may overwrite existing file.
		File file = new File(localDir + "/" + filename);
		if (!file.exists()) {// This file does not exist
			if (!file.createNewFile()) {// And create it unsuccessfully
				// means this file can't be downloaded here
				return false;
			}
		}
		FileOutputStream fos = new FileOutputStream(file, false);
		while (-1 != (len = dataReader.read(b, 0, b.length))) {
			fos.write(b, 0, len);
		}
		fos.flush();
		fos.close();

		if (activelistener != null) {
			activelistener.close();
			activelistener = null;
		}
		dataSocket.close();
		dataSocket = null;
		return read().startsWith("");
	}

	/**
	 * Upload a filepath to remote server. Must be called after the connection
	 * is established. The filepath is uploaded to current working directory.
	 * 
	 * @param filepath
	 *            Full path of the local filepath to upload.
	 * @throws IOException
	 */
	public boolean store(String filepath) throws IOException {
		// If local file does not exist, return false.
		File file = new File(filepath);
		if (!file.exists() || !file.isFile()) {
			return false;
		}

		establishDataLink();

		// set transfer mode to binary
		send(TYPE + " I");
		read();

		send(STOR + " " + file.getName());
		if (!read().startsWith("150")) {
			return false;
		}

		FileInputStream fis = new FileInputStream(file);
		byte[] b = new byte[1024];
		int len = -1;
		while (-1 != (len = fis.read(b, 0, b.length))) {
			dataWriter.write(b, 0, len);
		}

		if (activelistener != null) {
			activelistener.close();
			activelistener = null;
		}
		dataWriter.flush();
		dataWriter.close();
		dataSocket.close();

		fis.close();
		return read().startsWith("226");
	}

	/**
	 * Rename a file.
	 * 
	 * @param srcfile
	 *            Original file name.
	 * @param dstfile
	 *            The file name to rename to.
	 * @return true if this command is successful; otherwise false.
	 * @throws IOException
	 */
	public boolean rename(String srcfile, String dstfile) throws IOException {
		send(RNFR + " " + srcfile);
		if (!read().startsWith("350")) {
			return false;
		}
		send(RNTO + " " + dstfile);
		return (read().startsWith("250"));
	}

	/**
	 * Delete a file on the server.
	 * 
	 * @param filename
	 *            Name of the file to delete.
	 * @return True if the file is deleted successfully; otherwise false.
	 * @throws IOException
	 */
	public boolean delete(String filename) throws IOException {
		send(DELE + " " + filename);
		return read().startsWith("250 ");
	}

	/**
	 * Make a directory under current working directory.
	 * 
	 * @param dirname
	 *            Name of the directory to create.
	 * @return true if this directory is created successfully; otherwise false.
	 * @throws IOException
	 */
	public boolean makeDirectory(String dirname) throws IOException {
		send(MKD + " " + dirname);
		return read().startsWith("257");
	}

	/**
	 * Remove a directory on the server.
	 * 
	 * @param dirname
	 * @return true if the directory is deleted successfully; otherwise false.
	 * @throws IOException
	 */
	public boolean removeDir(String dirname) throws IOException {
		send(RMD + " " + dirname);
		return read().startsWith("257");
	}

	/**
	 * Request help from server.
	 * 
	 * @throws IOException
	 */
	public void help() throws IOException {
		send(HELP);
		read(false);
	}

	/**
	 * Do nothing.
	 * 
	 * @throws IOException
	 * 
	 */
	public void noop() throws IOException {
		send(NOOP);
		read();
	}

	/**
	 * list all features.
	 * 
	 * @throws IOException
	 */
	public void features() throws IOException {
		send(FEAT);
		read(false);
	}

	/**
	 * Close the connection to the server. This includes sending command and
	 * close the socket connection.
	 * 
	 * @throws IOException
	 */
	public void quit() throws IOException {
		send(QUIT);
		read(false);
		cmdSocket.close();
		if (dataSocket != null) {
			dataSocket.close();
		}
		if (activelistener != null) {
			activelistener.close();
		}
		cmdSocket = null;
		dataSocket = null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String server = "polaris";
		int port = 35088;
		String user = "ftp";
		String pass = "ftp";

		if (args != null) {
			if (args.length >= 1) {
				server = args[0];
			}
			if (args.length >= 2) {
				try {
					port = Integer.parseInt(args[1]);
				} catch (Exception e) {
				}
			}

			if (args.length >= 3) {
				user = args[2];
			}
			if (args.length >= 4) {
				pass = args[3];
			}
		}
		FTPClient client = new FTPClient(server, port, user, pass);
		try {
			// client.setPassiveMode(false);
			// client.setSecureMode(true);
			client.openConnection();
			// client.noop();
			// client.help();
			client.list();
			// client.printWorkingDir();
			// client.list();
			// client.changeDirUp();
			// test();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + e.getMessage());
		} finally {
			try {
				client.quit();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Send a command and flush the input stream.
	 * 
	 * @param msg
	 *            Command message to send.
	 */
	private void send(String msg) {
		if (msg.startsWith("PASS")) {
			outputs.append("PASS ******\n");
		} else
			outputs.append(msg + "\n");
		if (isSecured == SECURED) {
			try {
				msg = new String(Base64.encode(encipher.doFinal(msg.getBytes()), Base64.NO_WRAP));
				outputs.append("Encrypted: " + msg + "\n");
			} catch (IllegalBlockSizeException e) {
			} catch (BadPaddingException e) {
			}
		}
		cmdWriter.println(msg);
		cmdWriter.flush();
	}

	/**
	 * Read only one line of server's response.
	 * 
	 * @return Response message.
	 * @throws IOException
	 */
	private String read() throws IOException {
		return read(true);
	}

	/**
	 * Sometimes the server responses multiple lines of messages. We'd like to
	 * read them all.
	 * 
	 * @param oneline
	 *            read only one line or multiple lines.
	 * @return Response messages, delimited by CRLF.
	 * @throws IOException
	 */
	private String read(boolean oneline) throws IOException {
		StringBuffer sb = new StringBuffer();
		cmdSocket.setSoTimeout(120000);
		String read = cmdReader.readLine();
		outputs.append('>' + read + "\n");

		sb.append(read).append("\r\n");
		if (!oneline) {
			cmdSocket.setSoTimeout(1500);
			try {
				String tmp;
				while (null != (tmp = cmdReader.readLine())) {
					sb.append(tmp).append("\r\n");
					outputs.append('>' + tmp + "\n");
				}
			} catch (SocketTimeoutException e) {
				// Reading timeout, means no more message to read.
			}
		}
		String replies = sb.toString().trim();
		if (isSecured == SECURED) {
			try {
				replies = new String(decipher.doFinal(Base64.decode(replies, Base64.NO_WRAP)));
				outputs.append(">" + replies + "\n");
			} catch (Exception e) {
				System.err.println(e.getClass().getName() + e.getMessage());
			}
		}
		return replies;
	}

	/**
	 * Get all the output currently in the cache. After this, the cache is
	 * emptied.
	 * 
	 * @return
	 */
	public String getOutputs() {
		String temp = outputs.toString();
		outputs = new StringBuilder();
		return temp;
	}
}
