package priv.dremma.game.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class IOHelper {

	public static void writeObject(String path, Object obj) {
		File file = new File(path);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream(file));
			objOut.writeObject(obj);
			objOut.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Object readObject(String path) {
		Object obj = null;

		File file = new File(path);
		try {
			ObjectInputStream objIn = new ObjectInputStream(new FileInputStream(file));
			try {
				obj = objIn.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				objIn.close();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return obj;
	}
}
