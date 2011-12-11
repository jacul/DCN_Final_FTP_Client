/**
 * Xiangdong Zhu<br>
 * CSC 623: Data Communications and Networking<br>
 * Date: 12/4/2011<br>
 * Final Project: A Java based socket FTP implementation.
 * 
 * 
 * This class stands for the file elements returned by the server. It could be a
 * file, or a directory.
 * 
 * @author zxd
 * 
 */
public class FileElement {

	/**
	 * Attribute meaning that this file is a directory or a file.
	 */
	private boolean isDir = false;
	/**
	 * Name of this file element.
	 */
	private String filename;

	/**
	 * Constructor with file name only.
	 * 
	 * @param name
	 *            Name of this file element.
	 */
	public FileElement(String name) {
		filename = name;
	}

	/**
	 * Constructor with file name and file type.
	 * 
	 * @param name
	 *            name of this file element.
	 * @param isDir
	 *            type of this file element, a file or a directory.
	 */
	public FileElement(String name, boolean isDir) {
		this(name);
		this.setType(isDir);
	}

	/**
	 * Set the type of this file element, to be a file or a directory.
	 * 
	 * @param isDir
	 *            true to set as a directory, false to be a file.
	 */
	public void setType(boolean isDir) {
		this.isDir = isDir;
	}

	/**
	 * Return the type of this file, is a directory or not
	 * 
	 * @return true if this is a directory, otherwise false.
	 */
	public boolean isDirectory() {
		return isDir;
	}

	/**
	 * Get the file name of this file.
	 * 
	 * @return name of the file.
	 */
	public String getFileName() {
		return filename;
	}

	public String toString() {
		return filename + (isDir && !filename.equals("..") ? "/" : "");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((filename == null) ? 0 : filename.hashCode());
		result = prime * result + (isDir ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FileElement other = (FileElement) obj;
		if (filename == null) {
			if (other.filename != null) {
				return false;
			}
		} else if (!filename.equals(other.filename)) {
			return false;
		}
		if (isDir != other.isDir) {
			return false;
		}
		return true;
	}
}
