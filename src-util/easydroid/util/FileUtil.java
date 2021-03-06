package easydroid.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;

import easydroid.util.StreamUtil.ExceptionHandler;


public class FileUtil {

	private final static String SLASH = "/";
	private final static String INVERSE_SLASH = "\\";
	private final static char DOT = '.';
	
	
	public static boolean deleteFileOrDirRecursive(File file) {
		if(file.isFile()){
			return file.delete();
		} else {
			return deleteDirRecursive(file);
		}

	}

	public static boolean deleteDirRecursive(String path) {
		return deleteDirRecursive(new File(path));
	}

	public static boolean deleteDirRecursive(File dir) {
		if (dir.exists()) {
			
			if( ! dir.isDirectory()){
				throw new IllegalArgumentException(""+dir+" is not directory");
			}
			
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirRecursive(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (dir.delete());
	}

	public static void copyFile(File sourceFile, File destFile)
			throws IOException {
		if (!destFile.exists()) {
			destFile.getParentFile().mkdirs();
			destFile.createNewFile();
		}

		FileChannel source = null;
		FileChannel destination = null;
		try {
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			destination.transferFrom(source, 0, source.size());
		} finally {
			if (source != null) {
				source.close();
			}
			if (destination != null) {
				destination.close();
			}
		}
	}
	
	public static String readFileUTF8(FileInputStream fis) throws IOException {
		return readFile(fis, "UTF8");
	}

	public static String readFileUTF8(File file) throws IOException {
		return readFile(file, "UTF8");
	}

	public static String readFile(File file, String charset) throws IOException {
		
		FileInputStream fis = new FileInputStream(file);
		return readFile(fis, charset);
	}

	public static String readFile(FileInputStream fis, String charset) throws UnsupportedEncodingException, IOException {
		InputStreamReader r = null;
		OutputStreamWriter w = null;

		try {
			r = new InputStreamReader(fis, charset);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			w = new OutputStreamWriter(out, charset);
			char[] buff = new char[1024 * 4];
			int i;
			while ((i = r.read(buff)) > 0) {
				w.write(buff, 0, i);
			}
			w.flush();
			return out.toString(charset);
		} finally {
			if (r != null)
				try {
					r.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

			if (w != null)
				try {
					w.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}

	public static void writeFileUTF8(File file, String text) throws IOException {
		writeFile(file, text, "UTF8");
	}
	
	public static void writeFileUTF8(FileOutputStream fos, String text) throws IOException {
		writeFile(fos, text, "UTF8");
	}

	public static void writeFile(File file, String text, String charset) throws IOException {
		
		FileOutputStream fos = new FileOutputStream(file, false);
		writeFile(fos, text, charset);
	}

	public static void writeFile(FileOutputStream fos, String text, String charset) throws UnsupportedEncodingException, IOException {
		InputStreamReader r = null;
		OutputStreamWriter w = null;

		try {
			r = new InputStreamReader(new ByteArrayInputStream(text.getBytes("UTF8")), charset);
			w = new OutputStreamWriter(fos, charset);
			char[] buff = new char[1024 * 4];
			int i;
			while ((i = r.read(buff)) > 0) {
				w.write(buff, 0, i);
			}
			w.flush();
		} finally {
			if (r != null)
				try {
					r.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

			if (w != null)
				try {
					w.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}

	/**
	 * получить имя файла из пути
	 * 
	 * @param path
	 *            путь до файла с прямыми, либо обратными слешами
	 * @return строка после последнего слеша в path, либо (если слешей нет) сам
	 *         path
	 */
	public static String getFileNameFromPath(String path) {
		String name = path;
		int index = Math.max(path.lastIndexOf(SLASH),
				path.lastIndexOf(INVERSE_SLASH));
		if (index > -1) {
			name = path.substring(index + 1);
		}
		return name;
	}

	public static String toUnixPath(String path) {
		return path.replace(INVERSE_SLASH, SLASH);
	}

	public static String getFileType(File file) {
		return getFileType(file.getName());
	}

	/**
	 * получить тип файла из пути
	 * 
	 * @param path
	 *            - "путь/имя.тип"
	 * @return "тип", либо null если не нашел точку в конце строки
	 */
	public static String getFileType(String path) {
		int lastDot = path.lastIndexOf(DOT);
		if (lastDot < 0)
			return null;
		else
			return path.substring(lastDot + 1);
	}

	/**
	 * получить тип файла из пути
	 * 
	 * @param id
	 *            - "имя.тип"
	 */
	public static String getFileNameWithoutType(String name) {
		int lastDot = name.lastIndexOf(DOT);
		if (lastDot < 0)
			return name;
		else
			return name.substring(0, lastDot);
	}

	public static BufferedReader getBufferedReaderUTF8(File file)
			throws IOException {
		return getBufferedReader(file, "utf-8");
	}

	public static BufferedReader getBufferedReader(File file, String charset)
			throws IOException {
		FileInputStream is = new FileInputStream(file);
		InputStreamReader fileReader = new InputStreamReader(is, charset);
		return new BufferedReader(fileReader);
	}

	public static BufferedOutputStream getBufferedOutputStream(File destFile)
			throws FileNotFoundException {
		return new BufferedOutputStream(new FileOutputStream(destFile));
	}

	public static void writeFile(Object obj, File file) throws Exception {

		FileOutputStream os = new FileOutputStream(file, false);
		ObjectOutputStream oos = new ObjectOutputStream(os);
		try {

			oos.writeObject(obj);

		} catch (Exception e) {
			file.delete();
			throw e;
		} finally {
			StreamUtil.close(oos);
		}
	}


	public static void writeFile(InputStream in, File file,
			ExceptionHandler exceptionHandler) throws IOException {
		try {

			OutputStream os = new FileOutputStream(file, false);
			StreamUtil.copy(in, os, StreamUtil.DEFAULT_BUFFER_SIZE, true,
					exceptionHandler);

		} catch (Exception e) {

			file.delete();

			if (e instanceof IOException) {
				throw (IOException) e;
			} else {
				throw ExceptionUtil.getRuntimeExceptionOrThrowError(e);
			}
		}
	}

}
