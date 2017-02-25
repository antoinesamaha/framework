package com.foc.web.server.xmlViewDictionary.xmlViewKeyGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ClassFinder {

	private String classShortName     = null;

	public ClassFinder(String classShortName) {
		if(classShortName != null && classShortName.contains(".java")){
			classShortName = classShortName.replace(".java", ".class");
		}
		this.classShortName = classShortName;
	}

	public Class<?> locateImplementation() {
		Class<?> classe = null;
		String[] cp = System.getProperty("java.class.path").split(File.pathSeparator);

		for(int i=0; (classe == null) && (i < cp.length); i++) {
			File file = new File(cp[i]);
			if(file.exists() && file.canRead()) {
				if (isJar(file)) {
					try{
						classe = searchJar(new FileInputStream(file));
					}catch (Throwable t) {
						// Nothing to worry about
					}
				}else {
					classe = searchFile(file);
				}
			}
		}
		return classe;
	}

	private boolean isClass(String path) {
		return path.matches(".+\\.class$") && !path.contains("$");
	}

	private Class<?> searchFile(File f) {
		return searchFile(f, f.getPath());
	}

	private Class<?> searchFile(File file, String root) {
		Class<?> implementation = null;

		if(file.isDirectory()) {
			File[] files = file.listFiles();
			for(int i=0; i<files.length; i++) {
				implementation = searchFile(files[i], root);
				if(implementation != null) {
					break;
				}
			}
		}else if (isClass(file.getPath())) {
			String path = file.getPath().substring(root.length() + 1);
			if(file.getName().equals(getClassShortName())){
				Class<?> classe = getClass(path);
				if((classe != null) && !classe.isInterface()) {
					implementation = classe;
				}
			}
		}
		return implementation;
	}

	private Class<?> getClass(String name) {
		Class<?> c;
		String className = name.replaceAll("[/\\\\]", ".").replaceFirst("^\\.", "").replace(".class", "");
		try {
			c = Class.forName(className);
		} catch (Throwable e) {
			c = null;
		}

		return c;
	}

	private Class<?> searchJar(InputStream in) throws Exception {
		ZipInputStream zin = new ZipInputStream(in);
		Class<?> implementation = null;

		ZipEntry ze;
		while ((implementation == null) && ((ze = zin.getNextEntry()) != null)) {
			String name = ze.getName();
			if (name.endsWith("class") && name.matches("^com.xxx.+") && !name.contains("$")) {
				try {
					Class<?> c = getClass(name);
					if ((c != null) && !c.isInterface()) {
						implementation = c;
					}
				} catch (Throwable t) {
					// Nothing to worry about
				}
			}
		}

		return implementation;
	}

	private boolean isJar(File f) {
		return f.getPath().endsWith(".jar");
	}
	
	private String getClassShortName(){
		return classShortName;
	}

}
