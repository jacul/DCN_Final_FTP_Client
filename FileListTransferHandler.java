/**
 * Xiangdong Zhu<br>
 * CSC 623: Data Communications and Networking<br>
 * Date: 12/4/2011<br>
 * Final Project: A Java based socket FTP implementation.
 * 
 */

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

/**
 * This class is to perform drag and drop actions on the list, to
 * upload/download files.
 * 
 * @author zxd
 */
public class FileListTransferHandler extends TransferHandler {

	private int type;
	private FTPGUI host;

	public FileListTransferHandler(FTPGUI host, int type) {
		this.host = host;
		this.type = type;
	}

	protected Transferable createTransferable(JComponent c) {
		JList list = ((JList) c);
		int select = list.getSelectedIndex();
		if (select == -1) {
			return null;
		}
		return new StringSelection(list.getModel().getElementAt(select).toString());
	}

	@Override
	public int getSourceActions(JComponent c) {
		return COPY;
	}

	public boolean importData(JComponent c, Transferable t) {

		return false;
	}

	protected void exportDone(JComponent c, Transferable data, int action) {
		if (type == 0) {// upload
			System.out.println("Upload");
			host.upload();
		} else if (type == 1) {// download
			host.download();
			System.out.println("Download");
		}
	}

	public boolean canImport(JComponent c, DataFlavor[] flavors) {
		if (c instanceof JList) {
			JList list = (JList) c;
			int select = list.getSelectedIndex();
			if (select == -1) {
				return false;
			}
			String filename = list.getModel().getElementAt(select).toString();
			return !filename.endsWith("/") && !filename.equals("..");
		} else {
			return false;
		}
	}
}
