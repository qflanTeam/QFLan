package it.imt.qflan.core.tests;

//import java.io.File;
//
//import javax.tools.JavaCompiler;
//import javax.tools.ToolProvider;
//
//import com.microsoft.z3.Z3Exception;
//
//import it.imt.qflan.core.model.IQFlanModelBuilder;
//import it.imt.qflan.core.model.QFlanModel;


public class TestExternalCompilation {

	public static void main(String[] args) {
		/*
		File f = new File("BikesIDE.java");
		System.out.println(f.getPath());
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		int res = compiler.run(null, null, null, f.getPath());
		System.out.println("res:"+res);
		
		IQFlanModelBuilder modelBuilder=null;
		System.out.println("Before loading the model");
		//modelBuilder = (IQFlanModelBuilder)ClassLoader.getSystemClassLoader().loadClass("it.imt.qflan.core.models.BikesSPLC").newInstance();
		try {
			//modelBuilder = (IQFlanModelBuilder)ClassLoader.getSystemClassLoader().loadClass("BikesIDEBuiltin").newInstance();
			ClassLoader classLoader = ClassLoader.getSystemClassLoader();
			Class<?> loadedClass = classLoader.loadClass("BikesIDE");
			modelBuilder = (IQFlanModelBuilder)loadedClass.newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("After loading the model: " + modelBuilder);
		//IQFlanModelBuilder bikesSPLCBuilder = new BikesSPLC();
		//IQFlanModelBuilder bikesSPLCBuilder = new BikesIDE();
		try {
			QFlanModel loadedModel = modelBuilder.createModel();
			loadedModel.resetToInitialState();
		} catch (Z3Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/

	}

}
