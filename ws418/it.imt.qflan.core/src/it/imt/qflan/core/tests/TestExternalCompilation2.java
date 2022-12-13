package it.imt.qflan.core.tests;

//import javax.tools.JavaCompiler;
//import javax.tools.ToolProvider;
//
//import it.imt.qflan.core.model.IQFlanModelBuilder;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.OutputStream;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLClassLoader;

public class TestExternalCompilation2 {

	public static void main(String[] args) /*throws FileNotFoundException*/ {
		/*
		File sourceFile = null;
		JavaCompiler compiler = null;
		int res=0;
		//String source=null;
		URLClassLoader classLoader;
		File root;
		
		root = new File("models"); // On Windows running on C:\, this is C:\java.
		sourceFile = new File(root, "BikesIDE.java");
		if(sourceFile.exists()){
			System.out.println("Exists!");
		}
		else{
			System.out.println("Do not exist!");
		}
		
		File f = new File("BikesIDE.log");
		OutputStream stream = new FileOutputStream(f);
		
		compiler = ToolProvider.getSystemJavaCompiler();
		res = compiler.run(null, null, stream, sourceFile.getPath());
		System.out.println(res);

		// Load and instantiate compiled class.
		try {
			classLoader = URLClassLoader.newInstance(new URL[] { root.toURI().toURL() });
			Class<?> cls = Class.forName("BikesIDE", true, classLoader); // Should print "hello".
			//Object instance = cls.newInstance(); // Should print "world".
			//System.out.println(instance); // Should print "test.Test@hashcode".
			IQFlanModelBuilder modelBuilder = (IQFlanModelBuilder)cls.newInstance();
			System.out.println(modelBuilder);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		
		
		
		
		
		
		/*
		source = "public class Test2 { static { System.out.println(\"hello\"); } public Test2() { System.out.println(\"world\"); } }";
		root = new File("models"); // On Windows running on C:\, this is C:\java.
		sourceFile=new File(root,"Test2.java");
		//sourceFile.getParentFile().mkdirs();
		
		try {
			Files.write(sourceFile.toPath(),source.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Compile source file.
		compiler = ToolProvider.getSystemJavaCompiler();
		res = compiler.run(null, null, null, sourceFile.getPath());

		// Load and instantiate compiled class.
		try {
			classLoader = URLClassLoader.newInstance(new URL[] { root.toURI().toURL() });
			Class<?> cls = Class.forName("Test2", true, classLoader); // Should print "hello".
			Object instance = cls.newInstance(); // Should print "world".
			System.out.println(instance); // Should print "test.Test@hashcode".
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		
		
		
		/*
		// Prepare source somehow.
		source = "package test; public class Test { static { System.out.println(\"hello\"); } public Test() { System.out.println(\"world\"); } }";
		// Save source in .java file.
		root = new File("models"); // On Windows running on C:\, this is C:\java.
		sourceFile = new File(root, "test/Test.java");
		//sourceFile.getParentFile().mkdirs();
		//Files.write(source, sourceFile, StandardCharsets.UTF_8);
		try {
			Files.write(sourceFile.toPath(),source.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Compile source file.
		compiler = ToolProvider.getSystemJavaCompiler();
		res = compiler.run(null, null, null, sourceFile.getPath());
		
		System.out.println(res);
		
		// Load and instantiate compiled class.
		try {
			classLoader = URLClassLoader.newInstance(new URL[] { root.toURI().toURL() });
			Class<?> cls = Class.forName("test.Test", true, classLoader); // Should print "hello".
			Object instance = cls.newInstance(); // Should print "world".
			System.out.println(instance); // Should print "test.Test@hashcode".
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		

	}

}
