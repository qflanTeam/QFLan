package elevator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;

public class ParametricElevator {

	public static void main(String[] args) throws IOException {
//		int lastFloor=4;
//		int capacity=8;
//		int maxLoad=4;
		int lastFloor=49;
		int capacity=8;
		int maxLoad=4;
		if(args.length==3){
			lastFloor=Integer.valueOf(args[0]);
			capacity=Integer.valueOf(args[1]);
			maxLoad=Integer.valueOf(args[2]);
		}
		
		ParametricElevator b= new ParametricElevator();
		b.createElevator(lastFloor,capacity,maxLoad);
	}
	
	private void createElevator(int lastFloor,int capacity,int maxLoad) throws IOException {
		//Floors are from 0 to lastFloor
		//Executive is lastFloor, parking is 0
		
		String fileName="ParametricElevator"+lastFloor+"_"+capacity+"_"+maxLoad+".qflan";
		
		System.out.println("Writing elevator with lastFloor "+lastFloor+", capacity "+capacity+", and maxLoad "+maxLoad+" in file "+fileName);
		BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
		String nl="\n";
		bw.write("begin model ParametricElevator"+lastFloor+"_"+capacity+"_"+maxLoad+nl);
		bw.write("begin variables"+nl);		 
		bw.write("\t// system"+nl);
		bw.write("\ttop = "+lastFloor+"			// top floor"+nl);
		bw.write("\tbottom = 0		// bottom floor"+nl);
		bw.write("\tparkingFloor = 0 "+nl);
		bw.write("\texecutiveFloor = "+lastFloor+nl);
		bw.write("\t// elevator"+nl);
		bw.write("\tfloor = 0		// current floor"+nl);
		bw.write("\tdoor = 0		// door status (open = 1, closed =0)"+nl);
		bw.write("\tcapacity = "+capacity+" 	// number of persons that can enter the cabin"+nl);
		bw.write("\tmaxLoad = "+maxLoad+"		// max persons that should enter the cabin"+nl);
		bw.write("\tload = 0 		// current load (in number of persons)"+nl);
		bw.write("\tdirection = 0	// +1 is up, -1 is down"+nl);
		bw.write("\tgoal = 0		// floor to be reached"+nl);
		bw.write("\t// buttons inside the lift (L) and at the floors (F)"+nl);
		bw.write("\t// 0 means not-pushed, 1 means pushed"+nl);
		for(int i=0;i<=lastFloor;i++){
			bw.write("\tbuttonL"+i+" = 0"+nl);
		}
		for(int i=0;i<=lastFloor;i++){
			bw.write("\tbuttonF"+i+" = 0"+nl);
		}
		bw.write("end variables\n\n");
		
		writeFixedCode(bw, nl,"fromBeginAbstractFeaturesToEndActions.txt");
		
		
		bw.write("begin action constraints"+nl);
		bw.write("\t// "+lastFloor+" is executive"+nl);
		bw.write("\t// 0 is parking"+nl);
		


		bw.write("\tdo(open)  -> ({ door == 0 } and ("+nl);
		bw.write("\t\t// top & executive"+nl);
		bw.write("\t\t(  { floor == executiveFloor }"+nl);
		bw.write("\t\tand"+nl);
		bw.write("\t\t("+nl);
		bw.write("\t\t(has(Executive) and ({ buttonL"+lastFloor+" == 1} or { buttonF"+lastFloor+" == 1}))"+nl);
		bw.write("\t\t//"+nl);
		bw.write("\t\tor"+nl); 
		bw.write("\t\t(({ buttonL"+lastFloor+" == 1} or ({ buttonF"+lastFloor+" == 1} and (has(AlmostFull) implies {load < 0.66*maxLoad})  )))"+nl);
		bw.write("\t\t)"+nl);
		bw.write("\t\t)"+nl);
		bw.write("\t\t"+nl);
		bw.write("\t\t// bottom & parking"+nl);
		//
		bw.write("\t\tor  (  { floor == parkingFloor }"+nl);
		bw.write("\t\tand"+nl);
		bw.write("\t\t("+nl);
		bw.write("\t\t(has(Parking)"+nl);
		for(int i=0;i<=lastFloor;i++){
			bw.write("\t\t and { buttonL"+i+" == 0 }"+nl);
			bw.write("\t\t and { buttonF"+i+" == 0 }"+nl);
		}								
		bw.write("\t\t)"+nl);
		bw.write("\t\t//"+nl);
		bw.write("\t\tor"+nl); 
		bw.write("\t\t("+nl);
		bw.write("\t\thas(Executive) implies ({ buttonF"+lastFloor+" == 0} and { buttonL"+lastFloor+" == 0})"+nl);
		bw.write("\t\tand"+nl);
		bw.write("\t\t(({ buttonL0 == 1} or ({ buttonF0 == 1} and (has(AlmostFull) implies {load < 0.66*maxLoad})  )))"+nl);
		bw.write("\t\t)"+nl);
		bw.write("\t\t)"+nl);
		bw.write("\t\t)"+nl);
		bw.write("\t\t// Non-exec/non-park floor"+nl);
		bw.write("\t\tor ("+nl);
		bw.write("\t\t// Elevator is neither at the exec nor at the park floor"+nl);
		bw.write("\t\t{ floor != executiveFloor } and { floor != parkingFloor}"+nl);
		bw.write("\t\t// There is no request at the executive floor"+nl);
		bw.write("\t\tand (has(Executive) implies ({ buttonF"+lastFloor+" == 0} and { buttonL"+lastFloor+" == 0}) )"+nl);
		bw.write("\t\t// Elevator is at floor i"+nl);
		bw.write("\t\tand ("+nl);
		for(int i=1;i<lastFloor;i++){
			bw.write("\t\t( { floor == "+i+" }"+nl);
			bw.write("\t\t// There is an internal request at floor i or an external one (if it has feature AlmostFull check if load)"+nl);
			bw.write("\t\tand ({ buttonL"+i+" == 1} or ({ buttonF"+i+" == 1} and (has(AlmostFull) implies {load < 0.66*maxLoad})  )))"+nl);
			if(i<lastFloor-1){
				bw.write("\t\tor"+nl);
			}
		}
				
		bw.write("\t\t)");
		bw.write("\t\t)");
		bw.write("\t\t)");
		bw.write("\t)");
		
		bw.write(nl);
		bw.write("\tdo(stop) -> (! has(Shuttle)"+nl);
		for(int i=0;i<=lastFloor;i++){
			bw.write("\t\tand { buttonL"+i+" == 0 }"+nl);
			bw.write("\t\tand { buttonF"+i+" == 0 }"+nl);
		}
		bw.write("\t)"+nl);
		
		bw.write(nl);
		
		bw.write("\tdo(wakeup) -> ("+nl);
		bw.write("\t\t { buttonL0 == 1 }"+nl);
		for(int i=0;i<=lastFloor;i++){
			if(i>0){
				bw.write("\t\tor { buttonL"+i+" == 1 }"+nl);
			}
			bw.write("\t\tor { buttonF"+i+" == 1 }"+nl);
		}
		bw.write("\t)"+nl);
		
		bw.write(nl);
		
		bw.write("\tdo(park) -> ("+nl);
		bw.write("\t\t has(Parking) and { floor == parkingFloor  }"+nl);
		for(int i=0;i<=lastFloor;i++){
			bw.write("\t\tand { buttonL"+i+" == 0 }"+nl);
			bw.write("\t\tand { buttonF"+i+" == 0 }"+nl);
		}
		bw.write("\t)"+nl);
   	
		
   		writeFixedCode(bw, nl,"fixedActionConstraints.txt");
		bw.write("end action constraints"+nl+nl);
		
//		bw.write("begin processes"+nl);
//		writeFixedCode(bw, nl,"processes.txt");
//		bw.write("end processes"+nl+nl);

		bw.write("begin processes diagram"+nl);

		bw.write("\tbegin process LiftProc"+nl);
		bw.write("\tstates = Lift,LiftTurnButtonDown"+nl);
		bw.write("\ttransitions="+nl);
		bw.write("\tLift -(open    , 1 , {door  = 1 } )-> LiftTurnButtonDown,"+nl);
		bw.write("\tLift -(close   , 1 , {door  = 0 } )-> Lift,"+nl);
		bw.write("\tLift -(up      , 1 , {floor = floor + 1} )-> Lift,"+nl);
		bw.write("\tLift -(down    , 1 , {floor = floor - 1} )-> Lift,"+nl);
		bw.write("\tLift -(clean	   , 100 , { "+nl);
		for(int i=0;i<lastFloor;i++){
			bw.write("\t\tbuttonL"+i+" = 0 ,"+nl);
		}
		bw.write("\t\tbuttonL"+lastFloor+" = 0 })-> Lift,"+nl+nl);
		for(int i=0;i<lastFloor;i++){
			bw.write("\tLiftTurnButtonDown -(ask({floor=="+i+"}) , 100 , { buttonL"+i+" = 0 , buttonF"+i+" = 0 })-> Lift,"+nl);
		}
		bw.write("\tLiftTurnButtonDown -(ask({floor=="+lastFloor+"}) , 100 , { buttonL"+lastFloor+" = 0 , buttonF"+lastFloor+" = 0 })-> Lift"+nl);
		bw.write("\tend process"+nl+nl);

		
		//TODO: write ControllerProc
		bw.write("\tbegin process ControllerProc"+nl);
		bw.write("\tstates=Controller, ChooseGoal, ChooseGoalExec, ChooseDirection, ChooseGoalNonExec, ChooseGoalInside, ChooseGoalInAndOut"+nl);
		bw.write("\ttransitions="+nl);
		bw.write("\tController -(ask(true)    , 1 )-> ChooseGoal,"+nl+nl);

		bw.write("\tChooseGoal -(ask(has(Parking) and "+nl);
		for(int i=0;i<lastFloor;i++){
			bw.write("\t	       {buttonL"+i+"==0} and {buttonF"+i+"==0} and"+nl);
		}
		bw.write("\t	       {buttonL"+lastFloor+"==0} and {buttonF"+lastFloor+"==0}"+nl);
		bw.write("\t	       ) , 100 , {goal = 0} )->ChooseDirection,"+nl);

		bw.write("\tChooseGoal -(ask(!(has(Parking) and "+nl);
		for(int i=0;i<lastFloor;i++){
			bw.write("\t	       {buttonL"+i+"==0} and {buttonF"+i+"==0} and"+nl);
		}
		bw.write("\t	       {buttonL"+lastFloor+"==0} and {buttonF"+lastFloor+"==0}"+nl);
		bw.write("\t	       )) , 100 )->ChooseGoalExec,"+nl+nl);

		bw.write("\tChooseGoalExec -(ask(has(Executive) and"+nl);
		bw.write("\t      	  ({buttonL"+lastFloor+"==1} or {buttonF"+lastFloor+"==1})) , 100 , {goal = "+lastFloor+"})-> ChooseDirection,"+nl);
		bw.write("\tChooseGoalExec -(ask( ! (has(Executive) and"+nl);
		bw.write("\t      	  ({buttonL"+lastFloor+"==1} or {buttonF"+lastFloor+"==1}))) , 100)-> ChooseGoalNonExec,"+nl+nl);

		bw.write("\tChooseGoalNonExec-(ask(has(AlmostFull) and"+nl);
		bw.write("\t      	(   { buttonL0 == 1 } "+nl);
		for(int i=1;i<=lastFloor;i++){
			bw.write("\t      	 or { buttonL"+i+" == 1 }"+nl);
		}
		bw.write("\t      	 ))"+nl);
		bw.write("\t      	 , 100"+nl);
		bw.write("\t      )-> ChooseGoalInside,"+nl);

		bw.write("\tChooseGoalNonExec-(ask(!(has(AlmostFull) and"+nl);
		bw.write("\t      	(   { buttonL0 == 1 } "+nl);
		for(int i=1;i<=lastFloor;i++){
			bw.write("\t      	 or { buttonL"+i+" == 1 }"+nl);
		}
		bw.write("\t      	 )))"+nl);
		bw.write("\t      	 , 100"+nl);
		bw.write("\t      )-> ChooseGoalInAndOut,"+nl+nl);
		
		for(int i=lastFloor;i>=1;i--){
			bw.write("\tChooseGoalInside -(ask({floor<"+i+"} and {direction>=0} and ({buttonL"+i+"==1})) , 100 , {goal = "+i+"})-> ChooseDirection,"+nl);
		}
		for(int i=lastFloor-1;i>=0;i--){
			bw.write("\tChooseGoalInside -(ask({floor>"+i+"} and {direction<=0} and ({buttonL"+i+"==1})) , 100 , {goal = "+i+"})-> ChooseDirection,"+nl);
		}
		bw.write(nl);

		
		for(int i=lastFloor;i>=1;i--){
			bw.write("\tChooseGoalInAndOut -(ask({floor<"+i+"} and {direction>=0} and ({buttonL"+i+"==1} or {buttonF"+i+"==1})) , 100 , {goal = "+i+"})-> ChooseDirection,"+nl);
		}
		for(int i=lastFloor-1;i>=0;i--){
			bw.write("\tChooseGoalInAndOut -(ask({floor>"+i+"} and {direction<=0} and ({buttonL"+i+"==1} or {buttonF"+i+"==1})) , 100 , {goal = "+i+"})-> ChooseDirection,"+nl);
		}
		bw.write(nl);

		bw.write("\tChooseDirection -(stop      , 100 , {direction = 0 } )             -> Controller,"+nl);
		bw.write("\tChooseDirection -(park      , 100 , {direction = 0 , door = 1} )   -> Controller,"+nl);
		bw.write("\tChooseDirection -(startUp   , 100 , {direction =  1})              -> Controller,"+nl);
		bw.write("\tChooseDirection -(startDown , 100 , {direction = -1})              -> Controller,"+nl);
		bw.write("\tChooseDirection -(keepDir   , 100 )                                -> Controller,"+nl);
		bw.write("\tChooseDirection -(reverse   , 100 , {direction = -direction})      -> Controller"+nl);     
		bw.write("\tend process"+nl+nl);

		
		
		bw.write("\tbegin process ButtonsProc"+nl);
		bw.write("\tstates=Buttons"+nl);
		bw.write("\ttransitions="+nl);
		for(int i=0;i<=lastFloor;i++){
			bw.write("\tButtons -(pressL , 1 , { buttonL"+i+" = 1 })-> Buttons,"+nl);
			bw.write("\tButtons -(pressF , 1 , { buttonF"+i+" = 1 })-> Buttons,"+nl);
		}						
		bw.write("\t// Unpressing (AntiPrank feature"+nl);
		for(int i=0;i<lastFloor;i++){
			bw.write("\tButtons -(unpressL , 1 , { buttonL"+i+" = 0 })-> Buttons,"+nl);
			bw.write("\tButtons -(unpressF , 1 , { buttonF"+i+" = 0 })-> Buttons,"+nl);
		}						
		bw.write("\tButtons -(unpressL , 1 , { buttonL"+lastFloor+" = 0 })-> Buttons,"+nl);
		bw.write("\tButtons -(unpressF , 1 , { buttonF"+lastFloor+" = 0 })-> Buttons"+nl);
		bw.write("\tend process"+nl+nl);

		bw.write("\tbegin process PeopleProc"+nl);
		bw.write("\tstates = People"+nl);
		bw.write("\ttransitions ="+nl);
		bw.write("\tPeople-(enter , 10 , { load = load + 1 }) -> People,"+nl);
		bw.write("\tPeople-(leave , 10 , { load = load - 1 }) -> People"+nl);
		bw.write("\tend process"+nl+nl);
		
		bw.write("end processes diagram"+nl+nl);
		
		writeFixedCode(bw, nl,"fromBeginInitToEndModel.txt");

		System.out.println("Completed");
		
		bw.close();
	}

	private void writeFixedCode(BufferedWriter bw, String nl,String resource) throws IOException {
		InputStream is = getClass().getResourceAsStream(resource);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line=br.readLine();
		while(line!=null){
			//line=line.trim();
			bw.write(line);
			bw.write(nl);
			line=br.readLine();
		}
		is.close();
	}


	public void addMin(String fileName) throws IOException{
		//bike733NoMinTrunc2NBB._ode
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(fileName+"replaced"));
		
		InputStream is = getClass().getResourceAsStream(fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line=br.readLine();
		boolean speciesInit=false;
		boolean speciesCompleted=false;
		HashSet<String> BSpecies=new HashSet<String>(); 
		HashSet<String> SSpecies=new HashSet<String>();
		boolean stop=false;
		while(line!=null&&!stop){
			line=line.trim();
			if(!speciesInit){
				if(line.startsWith("begin init")){
					speciesInit=true;
				}
				else{
					
				}
			}
			else if(!speciesCompleted){
				if(line.startsWith("end init")){
					speciesCompleted=true;
				}
				else{
					int space = line.indexOf(' ');
					String name=line;
					if(space>0){
						name=line.substring(0, line.indexOf(' '));
					}
					if(name.startsWith("B")){
						BSpecies.add(name);
					}
					else if(name.startsWith("S")){
						SSpecies.add(name);
					}
				}
			} else{
				if(line.startsWith("begin reactions")){
					//line=br.readLine();
					stop=true;
				}
			}
			
			/*for(int i=1;i<=nVars;i++){
				line=line.replace("dy("+i+")", "y"+i+"'");
				line=line.replace("y("+i+")", "y"+i);
				line=line.replace(";", "");
			}
			System.out.println(line);
			System.out.println();*/
			bw.write(line);
			bw.write("\n");
			line=br.readLine();
		}
		
		boolean ignore=false;
		while(line!=null){
			line=line.trim();
			if(line.startsWith("end reactions")){
				ignore=true;
			}
			else if(!ignore){
				for (String species : SSpecies) {
					line=line.replace("*"+species,"*min("+species+",1)");
				}
				for (String species : BSpecies) {
					line=line.replace("*"+species,"*min("+species+",1)");
				}
			}
			bw.write(line);
			bw.write("\n");
			line=br.readLine();
		}
		br.close();
		bw.close();
	}
}
