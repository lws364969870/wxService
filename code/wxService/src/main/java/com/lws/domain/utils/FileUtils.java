package com.lws.domain.utils;

import java.io.File;

public class FileUtils {
	public boolean delete(String path) {
		File file = new File(path);
		if ((file.exists()) && (file.isFile())) {
			return file.delete();
		}
		return true;
	}
}