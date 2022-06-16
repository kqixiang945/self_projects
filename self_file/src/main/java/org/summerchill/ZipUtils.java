package org.summerchill;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

	
	public static void zip(File input, File output) throws IOException {
	    FileOutputStream out = new FileOutputStream(output);
	    ZipOutputStream zOut = new ZipOutputStream(out);
	    try {
	      zipFile("", input, zOut);
	    } finally {
	      zOut.close();
	    }
	  }

	  public static void zipFolderContent(File folder, File output)
	      throws IOException {
	    FileOutputStream out = new FileOutputStream(output);
	    ZipOutputStream zOut = new ZipOutputStream(out);
	    try {
	      File[] files = folder.listFiles();
	      if (files != null) {
	        for (File f : files) {
	          zipFile("", f, zOut);
	        }
	      }
	    } finally {
	      zOut.close();
	    }
	  }

	  private static void zipFile(String path, File input, ZipOutputStream zOut)
	      throws IOException {
	    if (input.isDirectory()) {
	      File[] files = input.listFiles();
	      if (files != null) {
	        for (File f : files) {
	          String childPath =
	              path + input.getName() + (f.isDirectory() ? "/" : "");
	          zipFile(childPath, f, zOut);
	        }
	      }
	    } else {
	      String childPath =
	          path + (path.length() > 0 ? "/" : "") + input.getName();
	      ZipEntry entry = new ZipEntry(childPath);
	      zOut.putNextEntry(entry);
	      InputStream fileInputStream =
	          new BufferedInputStream(new FileInputStream(input));
	      try {
	        IOUtils.copy(fileInputStream, zOut);
	      } finally {
	        fileInputStream.close();
	      }
	    }
	  }

	  public static void unzip(ZipFile source, File dest) throws IOException {
	    Enumeration<?> entries = source.entries();
	    while (entries.hasMoreElements()) {
	      ZipEntry entry = (ZipEntry) entries.nextElement();
	      File newFile = new File(dest, entry.getName());
	      if (entry.isDirectory()) {
	        newFile.mkdirs();
	      } else {
	        newFile.getParentFile().mkdirs();
	        InputStream src = source.getInputStream(entry);
	        try {
	          OutputStream output =
	              new BufferedOutputStream(new FileOutputStream(newFile));
	          try {
	            IOUtils.copy(src, output);
	          } finally {
	            output.close();
	          }
	        } finally {
	          src.close();
	        }
	      }
	    }
	  }

	
}
