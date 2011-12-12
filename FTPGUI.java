/**
 * YE JIN
 * CSC 623: Data Communications and Networking
 * Date: 12/12/2011
 * Final Project: A Java based socket FTP implementation.
 *
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * FTP user interface.
 * Main Frame.
 */
public class FTPGUI extends javax.swing.JFrame {

	static final String USER_HOME = System.getProperty("user.home");
	private FTPClient client;
	private File currentpath;
	private File[] localfiles;
	private FileElement[] remotefiles;

	/**
	 * create new form FTPGUI
     */
	public FTPGUI() {
		currentpath = new File(USER_HOME);
		remotefiles = new FileElement[0];
		createLocalFileList();
		initComponents();
		localTextField.setText(currentpath.getAbsolutePath());
	}

	public void writeOutput(String text) {
		testoutput.append(text.trim());
		testoutput.append("\n");
	}

	private void createLocalFileList() {
		File[] files = currentpath.listFiles();
		LinkedList<File> filecollection = new LinkedList<File>();
		for (File f : files) {
			if (f.isDirectory()) {
				filecollection.add(0, f);
			} else {
				filecollection.add(f);
			}
		}
		filecollection.add(0, new File(".."));
		localfiles = new File[filecollection.size()];
		filecollection.toArray(localfiles);
	}

	@SuppressWarnings("unchecked")


	private void initComponents() {
		localPopupMenu = new javax.swing.JPopupMenu();
		remotePopupMenu = new javax.swing.JPopupMenu();
		headPanel = new javax.swing.JPanel();
		connectPanel = new javax.swing.JPanel();
		hostLabel = new javax.swing.JLabel();
		hostTextField = new javax.swing.JTextField();
		usernameLabel = new javax.swing.JLabel();
		usernameTextField = new javax.swing.JTextField();
		passwordLabel = new javax.swing.JLabel();
		portLabel = new javax.swing.JLabel();
		portTextField = new javax.swing.JTextField();
		connectButton = new javax.swing.JButton();
		PasswordField = new javax.swing.JPasswordField();
		disconnectButton = new javax.swing.JButton();
		jScrollPane1 = new javax.swing.JScrollPane();
		testoutput = new javax.swing.JTextArea();
		localPanel = new javax.swing.JPanel();
		localSeparator = new javax.swing.JSeparator();
		localLabel = new javax.swing.JLabel();
		localTextField = new javax.swing.JTextField();
		localScrollPane = new javax.swing.JScrollPane();
		localList = new javax.swing.JList();
		remotePanel = new javax.swing.JPanel();
		remoteSeparator = new javax.swing.JSeparator();
		remoteLabel = new javax.swing.JLabel();
		remoteTextField = new javax.swing.JTextField();
		remoteScrollPane = new javax.swing.JScrollPane();
		remoteList = new javax.swing.JList();
		menuBar = new javax.swing.JMenuBar();
		fileMenu = new javax.swing.JMenu();
		smMenuItem = new javax.swing.JMenuItem();
		exportMenuItem = new javax.swing.JMenuItem();
		importMenuItem = new javax.swing.JMenuItem();
		exitMenuItem = new javax.swing.JMenuItem();
		editMenu = new javax.swing.JMenu();
		pasvMenuItem = new javax.swing.JCheckBoxMenuItem();
		securedMenuItem = new javax.swing.JCheckBoxMenuItem();
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

 		hostLabel.setText("Host:");//host label

        /**
         * add event handling to host
         */
		hostTextField.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				hostTextFieldActionPerformed(evt);
			}
		});

		usernameLabel.setText("Username:");//username label

        /**
         * add event handling to username
         */
		usernameTextField.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				usernameTextFieldActionPerformed(evt);
			}
		});

		passwordLabel.setText("Password:");//password label

		portLabel.setText("Port:");//port label

        /**
         * add event handling to port
         */
		portTextField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyTyped(java.awt.event.KeyEvent evt) {
				portTextFieldKeyTyped(evt);
			}
		});

		connectButton.setText("Connect");//connect button

        /**
         * add event handling to connect button
         */
		connectButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				connectButtonActionPerformed(evt);
			}
		});

		PasswordField.setText("");//password textfield

		disconnectButton.setText("Disconnect");//disconnect button

	    /**
         * add event handling to disconnect button
         */
		disconnectButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				disconnectButtonActionPerformed(evt);
			}
		});

		testoutput.setColumns(20);
		testoutput.setRows(5);
		jScrollPane1.setViewportView(testoutput);

        /**
         * connect pane layout
         */
		javax.swing.GroupLayout connectPanelLayout = new javax.swing.GroupLayout(connectPanel);
		connectPanel.setLayout(connectPanelLayout);
		connectPanelLayout
				.setHorizontalGroup(connectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
						javax.swing.GroupLayout.Alignment.TRAILING,
						connectPanelLayout.createSequentialGroup().addGroup(
								connectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(
										jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 898, Short.MAX_VALUE).addGroup(
										connectPanelLayout.createSequentialGroup().addContainerGap().addComponent(hostLabel)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
														hostTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 114,
														javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(
														javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(usernameLabel)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
														usernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 102,
														javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(
														javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(passwordLabel)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
														PasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, 102,
														javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(
														javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(portLabel)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
														portTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 98,
														javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(
														javax.swing.LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
												.addComponent(connectButton).addGap(18, 18, 18).addComponent(disconnectButton)))
								.addContainerGap()));
		connectPanelLayout.setVerticalGroup(connectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				connectPanelLayout.createSequentialGroup().addGroup(
						connectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(hostTextField,
								javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(hostLabel).addComponent(usernameLabel).addComponent(
								usernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(passwordLabel).addComponent(PasswordField,
								javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(portTextField, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(portLabel)
								.addComponent(disconnectButton).addComponent(connectButton)).addPreferredGap(
						javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jScrollPane1,
						javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE).addContainerGap()));

		javax.swing.GroupLayout headPanelLayout = new javax.swing.GroupLayout(headPanel);
		headPanel.setLayout(headPanelLayout);
		headPanelLayout.setHorizontalGroup(headPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				headPanelLayout.createSequentialGroup().addContainerGap().addComponent(connectPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
						javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		headPanelLayout.setVerticalGroup(headPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				headPanelLayout.createSequentialGroup().addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(connectPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE)));

		localLabel.setText("Local Site:");//local site label

		localList.setModel(new javax.swing.AbstractListModel() {
			public int getSize() {
				return localfiles.length;
			}

			public Object getElementAt(int i) {
				return localfiles[i].getName() + (localfiles[i].isDirectory() && !localfiles[i].getName().equals("..") ? "/" : "");
			}
		});

	    /**
         * add event handling to locallist
         */
		localList.addMouseListener(new java.awt.event.MouseAdapter() {
			/**
			 * add event handling to locallist when mouse released
             */
			public void mouseReleased(java.awt.event.MouseEvent evt) {
				localListMouseReleased(evt);
			}
			/**
			 * add event handling to locallist when mouse clicked
             */
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				localListMouseClicked(evt);
			}
			/**
			 * add event handling to locallist when mouse pressed
             */
			public void mousePressed(java.awt.event.MouseEvent evt) {
				localListMousePressed(evt);
			}
		});
		localList.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
			/**
			 * add event handling to locallist when mouse dragged
             */
			public void mouseDragged(java.awt.event.MouseEvent evt) {
				localListMouseDragged(evt);
			}
		});
		localScrollPane.setViewportView(localList);
	    /**
	     * add locallist right click menu
         */
		localList.add(localPopupMenu);

		JMenuItem menu = new JMenuItem("Upload");//right click menu item-upload
		menu.addActionListener(localMenuListener);
		localPopupMenu.add(menu);

		menu = new JMenuItem("Enter directory");//right click menu item-enter dirctory
		menu.addActionListener(localMenuListener);
		localPopupMenu.add(menu);

		menu = new JMenuItem("Create directory");//right click menu item-create directory
		menu.addActionListener(localMenuListener);
		localPopupMenu.add(menu);

		menu = new JMenuItem("Refresh");//right click menu item-refresh
		menu.addActionListener(localMenuListener);
		localPopupMenu.add(menu);

		menu = new JMenuItem("Delete");//right click menu item-delete
		menu.addActionListener(localMenuListener);
		localPopupMenu.add(menu);

		menu = new JMenuItem("Rename");//right click menu item-rename
		menu.addActionListener(localMenuListener);
		localPopupMenu.add(menu);

		localList.setDragEnabled(true);
		localList.setTransferHandler(new FileListTransferHandler(this, 0));

        /**
         * local pane layout
         */
		javax.swing.GroupLayout localPanelLayout = new javax.swing.GroupLayout(localPanel);
		localPanel.setLayout(localPanelLayout);
		localPanelLayout.setHorizontalGroup(localPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				localSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 436, Short.MAX_VALUE).addGroup(
				localPanelLayout.createSequentialGroup().addContainerGap().addComponent(localLabel).addPreferredGap(
						javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(localTextField,
						javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE).addContainerGap()).addComponent(localScrollPane,
				javax.swing.GroupLayout.DEFAULT_SIZE, 436, Short.MAX_VALUE));
		localPanelLayout.setVerticalGroup(localPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				localPanelLayout.createSequentialGroup().addComponent(localSeparator, javax.swing.GroupLayout.PREFERRED_SIZE,
						javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(
						javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
						localPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(localLabel)
								.addComponent(localTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
						javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(localScrollPane,
						javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE).addContainerGap()));

		remoteLabel.setText("Remote Site:");//remote site label

		remoteList.setModel(new javax.swing.AbstractListModel() {

			public int getSize() {
				return remotefiles.length;
			}

			public Object getElementAt(int i) {
				return remotefiles[i].toString();
			}
		});
		/**
		 * add event handling to remotelist
         */
		remoteList.addMouseListener(new java.awt.event.MouseAdapter() {
			/**
			 * add event handling to remotelist when mouse released
             */
			public void mouseReleased(java.awt.event.MouseEvent evt) {
				remoteListMouseReleased(evt);
			}
			/**
			 * add event handling to remotelist when mouse clicked
             */
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				remoteListMouseClicked(evt);
			}
			/**
			 * add event handling to remotelist when mouse pressed
             */
			public void mousePressed(java.awt.event.MouseEvent evt) {
				remoteListMousePressed(evt);
			}
		});
		remoteList.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
			/**
			 * add event handling to remotelist when mouse dragged
             */
			public void mouseDragged(java.awt.event.MouseEvent evt) {
				remoteListMouseDragged(evt);
			}
		});
		remoteScrollPane.setViewportView(remoteList);
	    /**
	     * add remotelist right click menu
         */
		remoteList.add(localPopupMenu);

		menu = new JMenuItem("Download");//right click menu item-download
		menu.addActionListener(remoteMenuListener);
		remotePopupMenu.add(menu);

		menu = new JMenuItem("Enter directory");//right click menu item-enter directory
		menu.addActionListener(remoteMenuListener);
		remotePopupMenu.add(menu);

		menu = new JMenuItem("Create directory");//right click menu item-create directory
		menu.addActionListener(remoteMenuListener);
		remotePopupMenu.add(menu);

		menu = new JMenuItem("Refresh");//right click menu item-refresh
		menu.addActionListener(remoteMenuListener);
		remotePopupMenu.add(menu);

		menu = new JMenuItem("Delete");//right click menu item-delete
		menu.addActionListener(remoteMenuListener);
		remotePopupMenu.add(menu);

		menu = new JMenuItem("Rename");//right click menu item-rename
		menu.addActionListener(remoteMenuListener);
		remotePopupMenu.add(menu);

		remoteList.setDragEnabled(true);
		remoteList.setTransferHandler(new FileListTransferHandler(this, 1));

        /**
         * remote pane layout
         */
		javax.swing.GroupLayout remotePanelLayout = new javax.swing.GroupLayout(remotePanel);
		remotePanel.setLayout(remotePanelLayout);
		remotePanelLayout.setHorizontalGroup(remotePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				remoteSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 451, Short.MAX_VALUE).addGroup(
				remotePanelLayout.createSequentialGroup().addContainerGap().addComponent(remoteLabel).addPreferredGap(
						javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(remoteTextField,
						javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE).addContainerGap()).addGroup(
				remotePanelLayout.createSequentialGroup().addComponent(remoteScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 441,
						Short.MAX_VALUE).addContainerGap()));
		remotePanelLayout.setVerticalGroup(remotePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				remotePanelLayout.createSequentialGroup().addComponent(remoteSeparator, javax.swing.GroupLayout.PREFERRED_SIZE,
						javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(
						javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
						remotePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(remoteLabel)
								.addComponent(remoteTextField, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
						javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(remoteScrollPane,
						javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(
						javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		fileMenu.setMnemonic('f');
		fileMenu.setText("File");

		smMenuItem.setMnemonic('m');
		smMenuItem.setText("Site Manager");
		fileMenu.add(smMenuItem);

		exportMenuItem.setMnemonic('e');
		exportMenuItem.setText("Export");
		fileMenu.add(exportMenuItem);

		importMenuItem.setMnemonic('i');
		importMenuItem.setText("Import");
		fileMenu.add(importMenuItem);

		exitMenuItem.setMnemonic('x');
		exitMenuItem.setText("Exit");
		exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				exitMenuItemActionPerformed(evt);
			}
		});
		fileMenu.add(exitMenuItem);

		menuBar.add(fileMenu);

		editMenu.setMnemonic('e');
		editMenu.setText("Edit");

		pasvMenuItem.setSelected(true);
		pasvMenuItem.setText("Passive mode");
		editMenu.add(pasvMenuItem);

		securedMenuItem.setText("Secure mode");
		editMenu.add(securedMenuItem);

		menuBar.add(editMenu);

		setJMenuBar(menuBar);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup().addComponent(localPanel, javax.swing.GroupLayout.PREFERRED_SIZE,
						javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(31, 31, 31).addComponent(
						remotePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addComponent(headPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup().addComponent(headPanel, javax.swing.GroupLayout.PREFERRED_SIZE,
						javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(
						javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
						layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(remotePanel,
								javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(localPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));

		pack();
	}//initComponents

	/**
	 * when close the window
	 */
	private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		System.exit(0);
	}//event_exitMenuItemActionPerformed

	private void connectButtonActionPerformed(java.awt.event.ActionEvent evt) {
		String host = hostTextField.getText();
		String username = usernameTextField.getText();
		char[] password = PasswordField.getPassword();
		String port = portTextField.getText();
		int p;
		try {
			p = Integer.parseInt(port);
		} catch (Exception e) {
			p = 21;
		}
		client = new FTPClient(host, p, username, new String(password));
		client.setPassiveMode(pasvMenuItem.isSelected());
		client.setSecureMode(securedMenuItem.isSelected());
		try {
			if (client.openConnection()) {
				client.printWorkingDir();
				this.writeOutput(client.getOutputs());
				remoteTextField.setText(client.getRemoteWorkingDir());
				refreshRemote();
			} else {
				this.writeOutput("Can't open connection to server, check your input.");
				//error message
			}
		} catch (Exception e) {
			if (e instanceof java.net.UnknownHostException) {
				this.writeOutput("Host unknown: " + e.getMessage());
				client = null;
			}
		}
	}//event_connectButtonActionPerformed

	/**
	 * when double click directory in localList
	 */
	private void localListMouseClicked(java.awt.event.MouseEvent evt) {
		if (evt.getClickCount() == 2) {
			enterDir();
		}
	}//event_localListMouseClicked

	private ActionListener localMenuListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			String menu = e.getActionCommand();
			if (menu.equals("Upload")) {
				upload();
			} else if (menu.equals("Enter directory")) {
				enterDir();
			} else if (menu.equals("Create directory")) {
				createDir();
			} else if (menu.equals("Refresh")) {
				refresh();
			} else if (menu.equals("Delete")) {
				delete();
			} else if (menu.equals("Rename")) {
				rename();
			}
		}
	};
	private ActionListener remoteMenuListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			String menu = e.getActionCommand();
			if (menu.equals("Download")) {
				download();
			} else if (menu.equals("Enter directory")) {
				enterRemoteDir();
			} else if (menu.equals("Create directory")) {
				createRemoteDir();
			} else if (menu.equals("Refresh")) {
				refreshRemote();
			} else if (menu.equals("Delete")) {
				deleteRemote();
			} else if (menu.equals("Rename")) {
				renameRemote();
			}
		}
	};

	/**
	 * upload files from localList
	 */
	public void upload() {
		int select = localList.getSelectedIndex();
		if (select < 0 || select >= localfiles.length) {
			return;
		}
		if (client != null && localfiles[select].isFile()) {
			try {
				if (client.store(localfiles[select].getCanonicalPath())) {
					// The file is uploaded successfully
					refreshRemote();
					this.writeOutput(client.getOutputs());
				}
			} catch (IOException ex) {
				Logger.getLogger(FTPGUI.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

	}

	/**
	 * when enter directory in localList
	 */
	private void enterDir() {
		int select = localList.getSelectedIndex();
		if (select < 0 || select >= localfiles.length) {
			return;
		}
		File selectFile = localfiles[select];
		if (selectFile.isDirectory()) {
			if (selectFile.getName().equals("..")) {
				currentpath = currentpath.getParentFile();
			} else {
				currentpath = selectFile;
			}
			localTextField.setText(currentpath.getAbsolutePath());
			refresh();
		}
	}

	/**
	 * when create directory in localList
	 */
	private void createDir() {
		int select = localList.getSelectedIndex();
		if (select < 0 || select >= localfiles.length) {
			return;
		}
		showDialog("New folder", new KeyListener() {

			@Override
			public void keyPressed(KeyEvent evt) {
				if (evt.getKeyCode() == 27) {
					dialog.setVisible(false);
				}
			}

			@Override
			public void keyReleased(KeyEvent evt) {
			}

			@Override
			public void keyTyped(KeyEvent evt) {
				if (evt.getKeyChar() == '\n') {
					String foldername = tf.getText();
					File newfolder = new File(currentpath.getAbsolutePath() + "/" + foldername);
					if (!newfolder.exists()) {
						newfolder.mkdir();
						refresh();
					}
					dialog.setVisible(false);
				}
			}
		});
	}

	private void showDialog(String eventname, KeyListener listener) {
		tf = new JTextField(eventname, 20);
		dialog = new JDialog();
		JPanel panel = new JPanel();
		panel.add(tf);
		dialog.setLocation(100, 250);
		dialog.setSize(230, 50);
		dialog.setContentPane(panel);
		tf.addKeyListener(listener);
		dialog.setVisible(true);
	}

	private JTextField tf;//create and rename directory textfield
	JDialog dialog;//create and rename directory dialog

	/**
	 * when refresh directory in localList
	 */
	private void refresh() {
		createLocalFileList();
		localList.setSelectedIndex(0);
		localList.scrollRectToVisible(localList.getCellBounds(0, 0));
		localList.invalidate();
		localList.repaint();
	}

	/**
	 * when delete directory in localList
	 */
	private void delete() {
		int select = localList.getSelectedIndex();
		if (select < 0 || select >= localfiles.length) {
			return;
		}
		localfiles[select].delete();
		refresh();
	}

	/**
	 * when rename directory in localList
	 */
	private void rename() {
		final int select = localList.getSelectedIndex();
		if (select < 0 || select >= localfiles.length) {
			return;
		}
		showDialog(localfiles[select].getName(), new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 27) {
					dialog.setVisible(false);
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == '\n') {
					String foldername = tf.getText();
					File oldfile = localfiles[select];
					if (!oldfile.getName().equals(foldername)) {
						File newfile = new File(oldfile.getParent() + "/" + foldername);
						oldfile.renameTo(newfile);
						refresh();
					}
					dialog.setVisible(false);
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
			}

		});
	}

	/**
	 * download files from remoteList
	 */
	public void download() {
		int select = remoteList.getSelectedIndex();
		if (select < 0 || select >= remotefiles.length) {
			return;
		}
		// testoutput.append(currentpath.getAbsolutePath());
		try {
			boolean result = client.retrieve(remotefiles[select].getFileName(), currentpath.getAbsolutePath());
			if (result) {
				this.writeOutput(client.getOutputs());
				refresh();
			}
		} catch (Exception ex) {
			Logger.getLogger(FTPGUI.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * when enter directory in remoteList
	 */
	private void enterRemoteDir() {
		int select = remoteList.getSelectedIndex();
		if (select != -1 && client != null) {
			try {
				client.changeDir(remotefiles[select].getFileName());
				remoteTextField.setText(client.getRemoteWorkingDir());
				this.writeOutput(client.getOutputs());
				refreshRemote();
			} catch (IOException ex) {
				Logger.getLogger(FTPGUI.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

 	/**
	 * when create directory in remoteList
	 */
	private void createRemoteDir() {
		showDialog("New Folder", new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 27)
					dialog.setVisible(false);
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == '\n') {
					String foldername = tf.getText();
					try {
						if (client != null && client.makeDirectory(foldername))
							refreshRemote();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					dialog.setVisible(false);
				}
			}
		});
	}

	/**
	 * when delete directory in remoteLsit
	 */
	private void deleteRemote() {
		int select = remoteList.getSelectedIndex();
		if (select < 0 || select > remotefiles.length || client == null) {
			return;
		}
		try {
			if (client.delete(remotefiles[select].getFileName())) {
				this.writeOutput(client.getOutputs());
				refreshRemote();
			}
		} catch (IOException ex) {
			Logger.getLogger(FTPGUI.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * when rename directory in remoteList
	 */
	private void renameRemote() {
		final int select = remoteList.getSelectedIndex();
		if (select < 0 || select > remotefiles.length || client == null) {
			return;
		}
		showDialog(remotefiles[select].getFileName(), new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 27)
					dialog.setVisible(false);
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == '\n') {
					String oldname = remotefiles[select].getFileName();
					String foldername = tf.getText();
					try {
						if (client != null && !oldname.equals(foldername) && client.rename(oldname, foldername))
							refreshRemote();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					dialog.setVisible(false);
				}
			}
		});
	}

	/**
	 * when refresh directory in remoteList
	 */
	private void refreshRemote() {
		try {
			remotefiles = client.list();
			remoteList.setListData(remotefiles);
			remoteList.invalidate();
			remoteList.repaint();
			this.writeOutput(client.getOutputs());
		} catch (IOException ex) {
			Logger.getLogger(FTPGUI.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * when right click directory in localList
	 */
	private void localListMousePressed(java.awt.event.MouseEvent evt) {
		if (evt.isPopupTrigger() && localList.getSelectedIndex() != -1) {
			localPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
		}
	}//event_localListMousePressed

	/**
	 * when right click directory in localList
	 */
	private void localListMouseReleased(java.awt.event.MouseEvent evt) {
		if (evt.isPopupTrigger() && localList.getSelectedIndex() != -1) {
			localPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
		}
	}//event_localListMouseReleased

	/**
	 * when double click directory in remoteList
	 */
	private void remoteListMouseClicked(java.awt.event.MouseEvent evt) {
		if (evt.getClickCount() == 2) {
			enterRemoteDir();
		}
	}//event_remoteListMouseClicked

	/**
	 * when right click directory in remoteList
	 */
	private void remoteListMousePressed(java.awt.event.MouseEvent evt) {
		if (evt.isPopupTrigger() && remoteList.getSelectedIndex() != -1) {
			remotePopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
		}
	}//event_remoteListMousePressed

	/**
	 * when right click directory in remoteList
	 */
	private void remoteListMouseReleased(java.awt.event.MouseEvent evt) {
		if (evt.isPopupTrigger() && remoteList.getSelectedIndex() != -1) {
			remotePopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
		}
	}//event_remoteListMouseReleased

	private void localListMouseDragged(java.awt.event.MouseEvent evt) {
	}//event_localListMouseDragged

	/**
	 * when drag directory in remoteList
	 */
	private void remoteListMouseDragged(java.awt.event.MouseEvent evt) {
		if ((evt.getModifiers() & MouseEvent.MOUSE_RELEASED) == MouseEvent.MOUSE_RELEASED) {
			System.out.println("Drag");
		}
	}//event_remoteListMouseDragged

	private void hostTextFieldActionPerformed(java.awt.event.ActionEvent evt) {
	}//event_hostTextFieldActionPerformed

	private void usernameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {
	}//event_usernameTextFieldActionPerformed

	private void disconnectButtonActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			client.quit();
			this.writeOutput(client.getOutputs());
			client = null;
			remotefiles = null;
			remoteList.setListData(new Object[0]);
			remoteTextField.setText("");
		} catch (Exception e) {
		}
	}//event_disconnectButtonActionPerformed

	private void portTextFieldKeyTyped(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_portTextFieldKeyTyped
		Object o = evt.getSource();
		if (o instanceof JTextField) {
			char keyCh = evt.getKeyChar();
			if ((keyCh < '0') || (keyCh > '9')) {
				if (keyCh != '\n')
				{
					evt.setKeyChar('\0');
				}
			}
		}

	}//event_portTextFieldKeyTyped

	/**
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(FTPGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(FTPGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(FTPGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(FTPGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new FTPGUI().setVisible(true);
			}
		});
	}

	// Variables declaration
	private javax.swing.JPasswordField PasswordField;//password
	private javax.swing.JButton connectButton;// connect button
	private javax.swing.JPanel connectPanel; //connect panel
	private javax.swing.JButton disconnectButton;// disconnect button
	private javax.swing.JMenu editMenu; // edit menu
	private javax.swing.JMenuItem exitMenuItem;//exit, file drop dwon menu item
	private javax.swing.JMenuItem exportMenuItem;//export. file drop down menu item
	private javax.swing.JMenu fileMenu;//file menu
	private javax.swing.JPanel headPanel;//head panel
	private javax.swing.JLabel hostLabel;//host label
	private javax.swing.JTextField hostTextField;// host textfield
	private javax.swing.JMenuItem importMenuItem;//import, file drop down menu item
	private javax.swing.JScrollPane jScrollPane1;//scrollpane
	private javax.swing.JLabel localLabel;//local label
	private javax.swing.JList localList;//local list
	private javax.swing.JPanel localPanel;//local panel
	private javax.swing.JPopupMenu localPopupMenu;//right click menu
	private javax.swing.JScrollPane localScrollPane;//local scrollpane
	private javax.swing.JSeparator localSeparator;//separator
	private javax.swing.JTextField localTextField;//local textfield
	private javax.swing.JMenuBar menuBar;//menu bar
	private javax.swing.JLabel passwordLabel; //password label
	private javax.swing.JCheckBoxMenuItem pasvMenuItem;//passive mode check box, edit drop down menu item
	private javax.swing.JLabel portLabel;//port label
	private javax.swing.JTextField portTextField;//port textfield
	private javax.swing.JLabel remoteLabel;//remote label
	private javax.swing.JList remoteList;//remote list
	private javax.swing.JPanel remotePanel;//remote panel
	private javax.swing.JPopupMenu remotePopupMenu;//remote right click menu
	private javax.swing.JScrollPane remoteScrollPane;//remote scrollpane
	private javax.swing.JSeparator remoteSeparator;//separator
	private javax.swing.JTextField remoteTextField;//remote textfield
	private javax.swing.JCheckBoxMenuItem securedMenuItem;//secure mode check box, edit drop down menu item
	private javax.swing.JMenuItem smMenuItem;//menu item
	private javax.swing.JTextArea testoutput;//upload status text area
	private javax.swing.JLabel usernameLabel;//username label
	private javax.swing.JTextField usernameTextField;//username textfield
	// End of variables declaration
}
