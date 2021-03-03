package rs.ac.bg.etf.pp1;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import rs.ac.bg.etf.pp1.CounterVisitor.FormParamCounter;
import rs.ac.bg.etf.pp1.CounterVisitor.VarCounter;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

import rs.ac.bg.etf.pp1.DesignatorVisitor;

public class CodeGenerator extends VisitorAdaptor {
	
	private int mainPc;
	private int condPc;
	private int stmtPc;
	
	private int nVars = 0;
	
	private ArrayList<Integer> andListOR = new ArrayList<>(); // lista za uslove
	private ArrayList<Integer> andListELSE = new ArrayList<>(); // lista za uslove
	private ArrayList<Integer> orList = new ArrayList<>(); // lista za uslove
	
	private ArrayList<ArrayList<Integer>> ListAndListOr = new ArrayList<>(); // lista za uslove ugnjezdavanje
	private ArrayList<ArrayList<Integer>> ListAndListELSE = new ArrayList<>();// lista za uslove ugnjezdavanje
	private ArrayList<ArrayList<Integer>> ListOrList = new ArrayList<>();// lista za uslove ugnjezdavanje
	private ArrayList<Integer> condPcList = new ArrayList<>();// lista za uslove ugnjezdavanje
	private ArrayList<Integer> stmtPcList = new ArrayList<>();// lista za uslove ugnjezdavanje
	private int numIf = 0, numFor = 0;
	
	private int condForPc = 0;
	private int statementForPc = 0;
	private int indexForPc = 0;
	private ArrayList<Integer> condForPcList = new ArrayList<>();
	private ArrayList<Integer> statementForPcList = new ArrayList<>();
	private ArrayList<Integer> indexForPCList = new ArrayList<>();
	
	private ArrayList<Integer> afterForPcList = new ArrayList<>();// lista za break-ove for
	private ArrayList<ArrayList<Integer>> afterForPcForEveryForList = new ArrayList<>();// lista za ugnjezdene for
	
	private int vratiSeNaCond = 1;
	private ArrayList<Integer> vratiSeNaCondList = new ArrayList<>();
	

	private Obj indexElem = null;
	private Obj desElem = null;
	private int startForEachBody  = 0;
	private int endForEachBody1 = 0;
	private int endForEachBody2 = 0;
	private int inc = 0;
	
	private ArrayList<Obj> indexElems = new ArrayList<>();
	private ArrayList<Obj> desElems = new ArrayList<>();
	private ArrayList<Integer> startForEachBodyList = new ArrayList<>();
	private ArrayList<Integer> endForEachBody1List = new ArrayList<>();
	private ArrayList<Integer> endForEachBody2List = new ArrayList<>();
	private int numForEach = 0;
	
	private ArrayList<Integer> inForEachList = new ArrayList<>();
	private int inForEach = 0;
	
	private ArrayList<Integer> afterForEachPcList = new ArrayList<>(); // lista za break-ove foreach
	private ArrayList<ArrayList<Integer>> afterForPcForEachEveryForList = new ArrayList<>(); // lista za ugnjezdene foreachove
	
	private ArrayList<Integer> continueForEach = new ArrayList<>();
	private ArrayList<ArrayList<Integer>> continueForEachList = new ArrayList<>();
	
	Obj designator = null;
	
	private int virtFuncTableAdr = 0; // ovo je kao pc u staticData
	
	private ArrayList<Obj> myClasses = new ArrayList<>();
	
	private int inClass = 0;
	
	public int getMainPc() {
		return mainPc;
	}
//	
//	
//	public void visit(PrintStmt printStmt) {
//		if(printStmt.getExpr().struct == Tab.intType) {
//			Code.loadConst(5);
//			Code.put(Code.print);
//		} else {
//			Code.loadConst(1);
//			Code.put(Code.bprint);
//		}
//	}
//	
//	public void visit(Const cnst) {
//		Obj con = Tab.insert(Obj.Con, "$", cnst.struct);
//		con.setLevel(0);
//		con.setAdr(cnst.getN1());
//		
//		Code.load(con);
//		//Code.loadConst(cnst.getN1());
//	}
//	
	

	
	private int countVirtTableOffset(Obj obj) { // racunanje velicine virtuelne tabele za svaku pojedinacnu klasu
		int num = 0 , ok = 0;
		HashSet<String> vidjenaImena = new HashSet<>();
		Struct klasa = obj.getType();
		
		while(klasa != null) {
			for (Iterator<Obj> iter = klasa.getMembers().iterator(); iter.hasNext();) {
				Obj elem = iter.next();
				
				if (elem.getKind() == Obj.Meth) {
					if (!(vidjenaImena.contains(elem.getName()))) {
						num = num + elem.getName().length() + 2; // ime, -1, adresa
						vidjenaImena.add(elem.getName());
					}
				}
			}
			
			klasa = klasa.getElemType();
		}
		

		return num;
	}
	
	
	public void visit(ClassType classType) {
		classType.obj.setAdr(virtFuncTableAdr); // kad load-ujem objekat cu je staviti kao prvi field
		myClasses.add(classType.obj);
		inClass = 1;
		
		
		// vrv bih ovde trebala da pamtim u staticData
	}
	
	public void visit(MyClassDecl myClassDecl) {
		ClassType classType = myClassDecl.getClassType();
		Obj obj = classType.obj; // objektni cvor klase
		
		int num = countVirtTableOffset(obj);
		num++; // zbog onog -2 u tabeli virtuelnih f-ja
		virtFuncTableAdr += num;
		
		inClass = 0;
	}
	
	public void visit(AbsClassType absClassType) {
		inClass = 1;
	}

	public void visit(MyAbstractClassDecl myAbstractClassDecl) {
		inClass = 0;
	}
	
	private void makeVirtualFunctionCallTable() {
		int startAddress = nVars;
		Obj klasa = null;
		Struct klasaStruct = null;
		int cnt = nVars;
		
		for (int i = 0 ; i < myClasses.size(); i++) {
			// popunjavanje tabele za klasu
			HashSet<String> vidjenaImena = new HashSet<>();
			klasa = myClasses.get(i);
			klasaStruct = klasa.getType(); // moja klasa (struct cvor)
			
			while (klasaStruct != null) {
				for (Iterator<Obj> iter = klasaStruct.getMembers().iterator(); iter.hasNext();) {
					Obj elem = iter.next();
					if (elem.getKind() == Obj.Meth) {
						if (!(vidjenaImena.contains(elem.getName()))) {
							String name = elem.getName();
							vidjenaImena.add(elem.getName());
							for(int j = 0; j < name.length(); j++) {
								Code.loadConst(name.charAt(j));
								Code.put(Code.putstatic);
								Code.put2(cnt++);
							}
							
							Code.loadConst(-1);
							Code.put(Code.putstatic);
							Code.put2(cnt++);
							
							Code.loadConst(elem.getAdr()); // vec obidjene metode, bice validna adresa u ovom momentu!!!
							Code.put(Code.putstatic);
							Code.put2(cnt++);
						}
					}
				}
				
				klasaStruct = klasaStruct.getElemType(); // sad za njenu nadklasu...
			}
			
			Code.loadConst(-2);
			Code.put(Code.putstatic);
			Code.put2(cnt++);
		}
	}
	
	public void visit(NoMethodTypeNameVoid methodTypeName) {
		
		if (methodTypeName.obj.getFpPos() == -10) {
			return; // abstract method
		}
		
		if("main".equalsIgnoreCase(methodTypeName.getMethodName())) {
			mainPc = Code.pc;
			makeVirtualFunctionCallTable();
		}
		methodTypeName.obj.setAdr(Code.pc);
		
		// Collect arguments and local variables
		SyntaxNode methodNode = methodTypeName.getParent();
		
		VarCounter varCounter = new VarCounter(); // lokalne promenljive funkcije
		methodNode.traverseTopDown(varCounter);
		 
		FormParamCounter formParamCounter = new FormParamCounter(); // fomarlni parametri funkcije
		methodNode.traverseTopDown(formParamCounter);
		
		// Geneate the entry
		Code.put(Code.enter);
		Code.put(methodTypeName.obj.getLevel());
		Code.put(methodTypeName.obj.getLevel() + varCounter.getCount());
		
	}
	
	public void visit(MethodTypeNameRet methodTypeNameRet) {
//		
//		if("main".equalsIgnoreCase(methodTypeNameRet.getMethodName())) {
//			mainPc = Code.pc;
//		}
		
		if (methodTypeNameRet.obj.getFpPos() == -10) {
			return; // abstract method
		}
		methodTypeNameRet.obj.setAdr(Code.pc);
		
		// Collect arguments and local variables
		SyntaxNode methodNode = methodTypeNameRet.getParent();
		
		VarCounter varCounter = new VarCounter();
		methodNode.traverseTopDown(varCounter);
		
		FormParamCounter formParamCounter = new FormParamCounter();
		methodNode.traverseTopDown(formParamCounter);
		
		// Geneate the entry
		Code.put(Code.enter);
		Code.put(methodTypeNameRet.obj.getLevel()); // formCounterConuter
		Code.put(methodTypeNameRet.obj.getLevel() + varCounter.getCount()); // form + var
		
	}
	
	public void visit(MethodDecl methodDecl) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	
	public void visit(StatementReturnExpr statementReturnExpr) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public void visit(StatementNoReturnExpr statementNoReturnExpr) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public void visit(StatementRead statementRead) { // bread?
		Obj des = statementRead.getDesignator().obj;
		
		if(des.getType().compatibleWith(Tab.charType)) {
			Code.put(Code.bread);
		}
		else {
			Code.put(Code.read);
		}
		
		
		Code.store(des);
	}
	
	public void visit(StatementPrint statementPrint) { //bprint?
		
		if (statementPrint.getExpr().struct.compatibleWith(Tab.charType)) {
			Code.loadConst(5);
			Code.put(Code.bprint);
		}
		else {
			Code.loadConst(5);
			Code.put(Code.print);
		}
	}
	
	public void visit(StatementPrintNumber statementPrintNumber) { //bprint?
		Code.loadConst(statementPrintNumber.getNumConstant());
		if (statementPrintNumber.getExpr().struct.compatibleWith(Tab.charType)) {
			Code.put(Code.bprint);
		}
		else {
			Code.put(Code.print);
		}
	}
	

	
	public void visit(DesignatorStatementFuncCall designatorStatementFuncCall) {
		Obj des = designatorStatementFuncCall.getDesStmtFuncCallDesignator().getDesignator().obj;
		if (des.getName().equals("chr") || des.getName().equals("ord")){ // samo se posmatra izraz na steku na odgovarajuci nacin
			return;
		}
		if(des.getName().equals("len")) {
			Code.put(Code.arraylength);
			return;
		}
		
		Design design = (Design) designatorStatementFuncCall.getDesStmtFuncCallDesignator().getDesignator();
		
		if (design.getDesignatorElemList() instanceof MyDesignatorElemList) {
			// treba mi this da bih uzela pok na tab virt poziva
			DesignatorVisitor desVisitor = new DesignatorVisitor(); // zelim da stavim na stek sve od designatora nanize ---> a.b.c();
			desVisitor.setInClass(inClass);
			desVisitor.setMyClasses(myClasses);
			designatorStatementFuncCall.getDesStmtFuncCallDesignator().traverseBottomUp(desVisitor);
			// this na vrhu steka uzmi prvi field(tj pok na tab virt f-ja)
			Code.put(Code.getfield);
			Code.put2(0); // rBr fielda
			// stek : "this", parametri, pok na tabelu
			Code.put(Code.invokevirtual);
			String name = designatorStatementFuncCall.getDesStmtFuncCallDesignator().getDesignator().obj.getName();
			for (int i = 0; i < name.length(); i++) {
				Code.put4(name.charAt(i));
			}
			Code.put4(-1);
		}
		else if (design.getDesignatorElemList() instanceof MyNoDesignatorElemList && inClass == 1){ // implicitni this -> a(); po gramatici se iz klase ne mogu zvati globalne f-je!
			Code.put(Code.load_n + 0); // ovo ce biti moj this, jer sam u f-ji, na steku parametri funkcije; svi u aktivacionom zapisu ispod BP
			Code.put(Code.getfield);
			Code.put2(0);
			Code.put(Code.invokevirtual);
			String name = designatorStatementFuncCall.getDesStmtFuncCallDesignator().getDesignator().obj.getName();
			for (int i = 0; i < name.length(); i++) {
				Code.put4(name.charAt(i));
			}
			Code.put4(-1);
		}
		else {
			int offset = designatorStatementFuncCall.getDesStmtFuncCallDesignator().getDesignator().obj.getAdr() - Code.pc;
			Code.put(Code.call);
			Code.put2(offset);
		}
		
		if (designatorStatementFuncCall.getDesStmtFuncCallDesignator().getDesignator().obj.getType() != Tab.noType) {
			Code.put(Code.pop); // nema ko da ga pokupi
		}
		
	}

	
	public void visit(DesignatorStatementINC designatorStatementINC) { // a inc instrukcija?
		if (designatorStatementINC.getDesignator().obj.getKind() == Obj.Elem) { // niz, i
			Code.put(Code.dup2); // niz, i , niz, i
		}
		
		// npr field: a.b.c
		if (designatorStatementINC.getDesignator().obj.getKind() == Obj.Fld) { // a.b (ref  na klasu)
			Code.put(Code.dup); // a.b, a.b
		}
		
		Code.load(designatorStatementINC.getDesignator().obj);// a.b, a.b.c
		Code.loadConst(1);
		Code.put(Code.add);

		
		Code.store(designatorStatementINC.getDesignator().obj);
	}

	public void visit(DesignatorStatementDEC dec) { // a inc instrukcija?
		if (dec.getDesignator().obj.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
		}
		
		if (dec.getDesignator().obj.getKind() == Obj.Fld) {
			Code.put(Code.dup);
		}
		
		Code.load(dec.getDesignator().obj);
		Code.loadConst(-1);
		Code.put(Code.add);
		Code.store(dec.getDesignator().obj);
	}
	
	
	
	public void visit(DesAssStmtm desAssStmtm) { //dodaj
		DesAssStmtCombOper oper = (DesAssStmtCombOper) desAssStmtm.getDesAssStmtCombOperators();
		if(oper.getAssignop() instanceof Assign) {
			Code.store(desAssStmtm.getDesignator().obj);
		}
		
		else {
			if(oper.getAssignop() instanceof AssignAddRight ) {
				AssignAddRight addRight = (AssignAddRight) oper.getAssignop();
				
				if (desAssStmtm.getDesignator().obj != null) { // ovaj uslov bi uvek trebao biti ispunjen!
					if (desAssStmtm.getDesignator().obj.getKind() == Obj.Elem) { // niz, i, niz[i] , expr
						
						if(addRight.getAddopRight() instanceof PlusRight) {
							Code.put(Code.add);
							Code.put(Code.dup_x2);
							Code.store(desAssStmtm.getDesignator().obj); // a += b += 3;
						}
						
						if(addRight.getAddopRight() instanceof MinusRight) {
							Code.put(Code.sub);
							Code.put(Code.dup_x2);
							Code.store(desAssStmtm.getDesignator().obj);
						}
					}
					else if (desAssStmtm.getDesignator().obj.getKind() == Obj.Fld){
						if(addRight.getAddopRight() instanceof PlusRight) {
							Code.put(Code.add);
							Code.put(Code.dup_x1); // da mi ostane za sled izrza ako treba...
							Code.store(desAssStmtm.getDesignator().obj);
						}
						
						if(addRight.getAddopRight() instanceof MinusRight) {
							Code.put(Code.sub);
							Code.put(Code.dup_x1);
							Code.store(desAssStmtm.getDesignator().obj);
						}
					}
					else {
						if(addRight.getAddopRight() instanceof PlusRight) {
							Code.put(Code.add);
							Code.put(Code.dup); // da mi ostane za sled izrza ako treba...
							Code.store(desAssStmtm.getDesignator().obj);
						}
						
						if(addRight.getAddopRight() instanceof MinusRight) {
							Code.put(Code.sub);
							Code.put(Code.dup);
							Code.store(desAssStmtm.getDesignator().obj);
						}
					}
				}
			}
			else {
				AssignMulRight mulRight = (AssignMulRight) oper.getAssignop();
				
				
				if (desAssStmtm.getDesignator().obj != null) {
					if (desAssStmtm.getDesignator().obj.getKind() == Obj.Elem) {
						if (mulRight.getMulopRight() instanceof MulR) {
							Code.put(Code.mul);
							Code.put(Code.dup_x2);
							Code.store(desAssStmtm.getDesignator().obj);
						}
						if (mulRight.getMulopRight() instanceof DivR) {
							Code.put(Code.div);
							Code.put(Code.dup_x2);
							Code.store(desAssStmtm.getDesignator().obj);
						}
						if (mulRight.getMulopRight() instanceof ModR) {
							Code.put(Code.rem);
							Code.put(Code.dup_x2);
							Code.store(desAssStmtm.getDesignator().obj);
						}
						
						
					}
					else if (desAssStmtm.getDesignator().obj.getKind() == Obj.Fld){
						if (mulRight.getMulopRight() instanceof MulR) {
							Code.put(Code.mul);
							Code.put(Code.dup_x1);
							Code.store(desAssStmtm.getDesignator().obj);
						}
						if (mulRight.getMulopRight() instanceof DivR) {
							Code.put(Code.div);
							Code.put(Code.dup_x1);
							Code.store(desAssStmtm.getDesignator().obj);
						}
						if (mulRight.getMulopRight() instanceof ModR) {
							Code.put(Code.rem);
							Code.put(Code.dup_x1);
							Code.store(desAssStmtm.getDesignator().obj);
						}
					}
					
					else {
						if (mulRight.getMulopRight() instanceof MulR) {
							Code.put(Code.mul);
							Code.put(Code.dup);
							Code.store(desAssStmtm.getDesignator().obj);
						}
						if (mulRight.getMulopRight() instanceof DivR) {
							Code.put(Code.div);
							Code.put(Code.dup);
							Code.store(desAssStmtm.getDesignator().obj);
						}
						if (mulRight.getMulopRight() instanceof ModR) {
							Code.put(Code.rem);
							Code.put(Code.dup);
							Code.store(desAssStmtm.getDesignator().obj);
						}
					}
				}
				
			}
			
			
			//Code.store(desAssStmtm.getDesignator().obj);
			Code.put(Code.pop);
		}
	}
	
	public void visit(DesAssStmtCombOper desAssStmtCombOper) {
		if(desAssStmtCombOper.getAssignop() instanceof AssignAddRight || desAssStmtCombOper.getAssignop() instanceof AssignMulRight ) {
			//if (designator != null) {
				if (designator.getKind() == Obj.Elem) {
					Code.put(Code.dup2); // niz, i , niz, i
				}
			//}
			if (designator.getKind() == Obj.Fld) {
				Code.put(Code.dup); // treba ovde da mu ostane jos jedno a zbog store... refNaKlasu, refNaKlasu
			}
			Code.load(designator); // 1. var, 2. niz, i , niz[1] ; 3. refNaKlasu, field ; 
		}
	}
	
	
	// *** JUN KOMBINOVANI OPERATORI:
	
	public void visit(DesListElem desListElem) {
		DesAssStmtCombOper oper = (DesAssStmtCombOper) desListElem.getDesAssStmtCombOperators();
		if(oper.getAssignop() instanceof Assign) {
			Code.store(desListElem.getDesVar().getDesignator().obj);
		}
		
		else {
			if(oper.getAssignop() instanceof AssignAddRight ) {
				AssignAddRight addRight = (AssignAddRight) oper.getAssignop();
				
				if (desListElem.getDesVar().getDesignator().obj != null) {
					if (desListElem.getDesVar().getDesignator().obj.getKind() == Obj.Elem) {
						
						if(addRight.getAddopRight() instanceof PlusRight) {
							Code.put(Code.add);
							Code.put(Code.dup_x2);
							Code.store(desListElem.getDesVar().getDesignator().obj);
						}
						
						if(addRight.getAddopRight() instanceof MinusRight) {
							Code.put(Code.sub);
							Code.put(Code.dup_x2);
							Code.store(desListElem.getDesVar().getDesignator().obj);
						}
					}
					else if (desListElem.getDesVar().getDesignator().obj.getKind() == Obj.Fld){
						if(addRight.getAddopRight() instanceof PlusRight) {
							Code.put(Code.add);
							Code.put(Code.dup_x1); // da mi ostane za sled izrza ako treba...
							Code.store(desListElem.getDesVar().getDesignator().obj);
						}
						
						if(addRight.getAddopRight() instanceof MinusRight) {
							Code.put(Code.sub);
							Code.put(Code.dup_x1);
							Code.store(desListElem.getDesVar().getDesignator().obj);
						}
					}
					else {
						if(addRight.getAddopRight() instanceof PlusRight) {
							Code.put(Code.add);
							Code.put(Code.dup); // da mi ostane za sled izrza ako treba...
							Code.store(desListElem.getDesVar().getDesignator().obj);
						}
						
						if(addRight.getAddopRight() instanceof MinusRight) {
							Code.put(Code.sub);
							Code.put(Code.dup);
							Code.store(desListElem.getDesVar().getDesignator().obj);
						}
					}
				}
			}
			else {
				AssignMulRight mulRight = (AssignMulRight) oper.getAssignop();
				
				
				if (desListElem.getDesVar().getDesignator().obj != null) {
					if (desListElem.getDesVar().getDesignator().obj.getKind() == Obj.Elem) {
						if (mulRight.getMulopRight() instanceof MulR) {
							Code.put(Code.mul);
							Code.put(Code.dup_x2);
							Code.store(desListElem.getDesVar().getDesignator().obj);
						}
						if (mulRight.getMulopRight() instanceof DivR) {
							Code.put(Code.div);
							Code.put(Code.dup_x2);
							Code.store(desListElem.getDesVar().getDesignator().obj);
						}
						if (mulRight.getMulopRight() instanceof ModR) {
							Code.put(Code.rem);
							Code.put(Code.dup_x2);
							Code.store(desListElem.getDesVar().getDesignator().obj);
						}
						
						
					}
					else if (desListElem.getDesVar().getDesignator().obj.getKind() == Obj.Fld){
						if (mulRight.getMulopRight() instanceof MulR) {
							Code.put(Code.mul);
							Code.put(Code.dup_x1);
							Code.store(desListElem.getDesVar().getDesignator().obj);
						}
						if (mulRight.getMulopRight() instanceof DivR) {
							Code.put(Code.div);
							Code.put(Code.dup_x1);
							Code.store(desListElem.getDesVar().getDesignator().obj);
						}
						if (mulRight.getMulopRight() instanceof ModR) {
							Code.put(Code.rem);
							Code.put(Code.dup_x1);
							Code.store(desListElem.getDesVar().getDesignator().obj);
						}
					}
					
					else {
						if (mulRight.getMulopRight() instanceof MulR) {
							Code.put(Code.mul);
							Code.put(Code.dup);
							Code.store(desListElem.getDesVar().getDesignator().obj);
						}
						if (mulRight.getMulopRight() instanceof DivR) {
							Code.put(Code.div);
							Code.put(Code.dup);
							Code.store(desListElem.getDesVar().getDesignator().obj);
						}
						if (mulRight.getMulopRight() instanceof ModR) {
							Code.put(Code.rem);
							Code.put(Code.dup);
							Code.store(desListElem.getDesVar().getDesignator().obj);
						}
					}
				}
				
			}
			
			//Code.store(desAssStmtm.getDesignator().obj);
			//Code.put(Code.pop);
		}
	}
	
	
	
	
	public void visit(StatementBreak statementBreak) {
		if (inForEach == 0) { //in for loop
			afterForPcList.add(Code.pc);
			Code.putJump(0);
		}
		else {
			afterForEachPcList.add(Code.pc);
			Code.putJump(0);
		}
	}
	
	public void visit(StatementContinue statementContinue) {
		if (inForEach == 0) { //in for loop
			Code.putJump(indexForPc);
		}
		else {
			continueForEach.add(Code.pc); // ovde ja sama na steku cuvam ind pa zato imam listu i za continue
			Code.putJump(0);
		}
	}
	
	// *** DODATAK ZA JUN ***
	
	public void visit(ForEachIndexName forEachIndexName) {
		if (numForEach > 0) {
			indexElems.add(indexElem);
			desElems.add(desElem);
			endForEachBody1List.add(endForEachBody1);
			endForEachBody2List.add(endForEachBody2);
			startForEachBodyList.add(startForEachBody);
		}
		
		if (numForEach > 0 && inForEach == 1) { // nije ispunjeno ako npr imam foerach{ for{ foreach{} } }
			afterForPcForEachEveryForList.add(afterForEachPcList); // cuvam listu breakova 
			continueForEachList.add(continueForEach);
		}
		
		if (numFor > 0 && inForEach == 0) {
			afterForPcForEveryForList.add(afterForPcList); // cuvam listu breakova , ako ima forova pre mene
		}
		
		if (numFor > 0 || numForEach > 0) {
			inForEachList.add(inForEach); // niz flegova
		}
		
		indexElem = forEachIndexName.obj;
		numForEach++;
		inc = 0;
		
		inForEach = 1;
		
		afterForEachPcList = new ArrayList<>();
		continueForEach = new ArrayList<>();
	}
	
	public void visit(ForEachDesignator forEachDesignator) {
		desElem = forEachDesignator.getDesignator().obj; // k.a
		
		
		Code.load(desElem);
		Code.loadConst(inc);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.dup);
		Code.put(Code.arraylength);
		Code.loadConst(inc); // ako niz  ima 0 elem...
		endForEachBody1 = Code.pc;
		Code.putFalseJump(Code.ne, 0); // na kraj fora, br elem niza = inc ---> prosao kroz sve elem niza...
		Code.loadConst(inc);
		
		Code.load(new Obj(Obj.Elem,"elem",desElem.getType().getElemType()));
		
		Code.store(indexElem);
		startForEachBody = Code.pc;
	}
	
	public void visit(ForEachBody forEachBody) {
	
		for(int i = 0 ; i <continueForEach.size(); i++) {
			Code.fixup(continueForEach.get(i) + 1);
		}
		continueForEach.clear();
		
		Code.loadConst(1);
		Code.put(Code.add);
		Code.put(Code.dup);
		DesignatorVisitor designatorVisitor = new DesignatorVisitor();
		designatorVisitor.setInClass(inClass);
		designatorVisitor.setMyClasses(myClasses);
		
		((StatementForeach)(forEachBody.getParent())).getForEachDesignator().traverseBottomUp(designatorVisitor);
		Code.load(desElem);
		Code.put(Code.dup_x1);
		Code.put(Code.arraylength);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		
		endForEachBody2 = Code.pc;
		Code.putFalseJump(Code.ne, 0); // na kraj fora, br elem niza = inc ---> prosao kroz sve elem niza...
		
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.dup_x1);
		
		Code.load(new Obj(Obj.Elem,"elem",desElem.getType().getElemType()));
		
		Code.store(indexElem);
		Code.putJump(startForEachBody);
	}
	
	public void visit(StatementForeach statementForeach) {
		Code.fixup(endForEachBody1 + 1); // ako nema nijedan elem niz...
		Code.fixup(endForEachBody2 + 1);
		
		Code.put(Code.pop);
		for(int i = 0 ; i <afterForEachPcList.size(); i++) {
			Code.fixup(afterForEachPcList.get(i) + 1);
		}
		afterForEachPcList.clear();
		Code.put(Code.pop);
	
		numForEach--;
		
		if (numForEach > 0) {
			indexElem = indexElems.remove(indexElems.size() - 1);
			desElem = desElems.remove(desElems.size() - 1);
			endForEachBody1 = endForEachBody1List.remove(endForEachBody1List.size() - 1);
			endForEachBody2 = endForEachBody2List.remove(endForEachBody2List.size() - 1);
			startForEachBody = startForEachBodyList.remove(startForEachBodyList.size() - 1);
			//afterForEachPcList = afterForPcForEachEveryForList.remove(afterForPcForEachEveryForList.size() - 1);
		}
		
		
		
		if (numForEach > 0 || numFor > 0) {
			inForEach = inForEachList.remove(inForEachList.size() - 1);
		}
		
		if (numForEach > 0 && inForEach == 1) {
			afterForEachPcList = afterForPcForEachEveryForList.remove(afterForPcForEachEveryForList.size() - 1);
			continueForEach = continueForEachList.remove(continueForEachList.size() - 1);
		}
		
		if (numFor > 0 && inForEach == 0) {
			afterForPcList = afterForPcForEveryForList.remove(afterForPcForEveryForList.size() - 1);
		}
	}
	
	// *** KRAJ DODATKA ZA JUN ***
	
	/*
	for (int i = 0; i< 10; i++) {
		
	}
	*/
	
	public void visit(MyDesignatorForBegin beginFor) {
		if (numIf>0 || numFor>0) {
			condPcList.add(condPc);
			stmtPcList.add(stmtPc);
			ListAndListELSE.add(andListELSE);
			ListAndListOr.add(andListOR);
			ListOrList.add(orList);
		}
		if (numFor>0) {
			condForPcList.add(condForPc);
			statementForPcList.add(statementForPc);
			indexForPCList.add(indexForPc);
			
			
			vratiSeNaCondList.add(vratiSeNaCond);
		}
		
		if (numForEach > 0 && inForEach == 1) {
			afterForPcForEachEveryForList.add(afterForEachPcList);
			continueForEachList.add(continueForEach);
		}
		
		if (numFor > 0 && inForEach == 0) {
			afterForPcForEveryForList.add(afterForPcList);
		}
		
		
		if (numFor > 0 || numForEach > 0) {
			inForEachList.add(inForEach);
		}
		
		
		
		condPc = 0;
		stmtPc = 0;
		andListELSE = new ArrayList<>();
		andListOR = new ArrayList<>();
		orList = new ArrayList<>();
		numIf++;
		numFor++;
		
		
		condForPc = Code.pc;
		statementForPc = 0;
		indexForPc = 0;
		afterForPcList = new ArrayList<>();
		
		vratiSeNaCond = 1;
		
		inForEach = 0;
	}
	
	public void visit(MyNoDesignatorForBegin beginFor) {
		if (numIf>0 || numFor > 0) {
			condPcList.add(condPc);
			stmtPcList.add(stmtPc);
			ListAndListELSE.add(andListELSE);
			ListAndListOr.add(andListOR);
			ListOrList.add(orList);
		}
		
		if (numFor>0) {
			condForPcList.add(condForPc);
			statementForPcList.add(statementForPc);
			indexForPCList.add(indexForPc);
			
		
			
			vratiSeNaCondList.add(vratiSeNaCond);
		}
		
		if (numForEach > 0 && inForEach == 1) {
			afterForPcForEachEveryForList.add(afterForEachPcList);
			continueForEachList.add(continueForEach);
		}
		
		if (numFor > 0 && inForEach == 0) {
			afterForPcForEveryForList.add(afterForPcList);
		}
		
		if (numFor > 0 || numForEach > 0) {
			inForEachList.add(inForEach);
		}
		
		condPc = 0;
		stmtPc = 0;
		andListELSE = new ArrayList<>();
		andListOR = new ArrayList<>();
		orList = new ArrayList<>();
		numIf++;
		numFor++;
		
		condForPc = Code.pc;
		statementForPc = 0;
		indexForPc = 0;
		afterForPcList = new ArrayList<>();
		
		vratiSeNaCond = 1;
		
		inForEach = 0;
	}
	

	public void visit(MyConditionFor myConditionFor) {
		condPc = Code.pc;
		Code.putJump(0); // iskoci iz for...
		indexForPc = Code.pc;
	}
	

	public void visit(MyNoConditionFor myNoConditionFor) {
		statementForPc = Code.pc;
		Code.putJump(0);
		vratiSeNaCond = 0;
		indexForPc = Code.pc;
	}
	
	public void visit(ForBody forBody) {
		Code.putJump(indexForPc);
	}
	
	public void visit(MyDesignatorStatementFor myDesignatorStatementFor) {
		if (vratiSeNaCond == 1) {
			Code.putJump(condForPc);
		}
		else {
			Code.fixup(statementForPc + 1);
		}

		for (int i=0 ; i<orList.size() ; i++) {
			Code.fixup(orList.get(i)+1);
		}
		
		orList.clear();
	}
	
	public void visit(MyNoDesignatorStatementFor myNoDesignatorStatementFor) {
		if (vratiSeNaCond == 1) {
			Code.putJump(condForPc);
		}
		else {
			Code.fixup(statementForPc + 1);
		}
		
		for (int i=0 ; i<orList.size() ; i++) {
			Code.fixup(orList.get(i)+1);
		}
		
		orList.clear();
	}
	
	
	public void visit(StatementFor statementFor) {
		for(int i = 0; i <afterForPcList.size();i++) {
			Code.fixup(afterForPcList.get(i) + 1); // za break
		}
		afterForPcList.clear();
		if (vratiSeNaCond == 1 ) {
			Code.fixup(condPc + 1); // iskoci iz for-a
		}
		
		numIf--;
		numFor--;
		if (numIf>0 || numFor > 0) {
			condPc = condPcList.remove(condPcList.size() - 1);
			stmtPc = stmtPcList.remove(stmtPcList.size() - 1);
			andListELSE = ListAndListELSE.remove(ListAndListELSE.size() - 1);
			andListOR = ListAndListOr.remove(ListAndListOr.size() - 1);
			orList = ListOrList.remove(ListOrList.size() - 1);
		}
		
		if (numFor > 0) {
			condForPc = condForPcList.remove(condForPcList.size() - 1);
			statementForPc = statementForPcList.remove(statementForPcList.size() - 1);
			indexForPc = indexForPCList.remove(indexForPCList.size() - 1);
			
			
			
			vratiSeNaCond = vratiSeNaCondList.remove(vratiSeNaCondList.size() - 1);
		}
		
		if (numForEach > 0 || numFor > 0) {
			inForEach = inForEachList.remove(inForEachList.size() - 1);
		}
		
		if (numForEach > 0 && inForEach == 1) {
			afterForEachPcList = afterForPcForEachEveryForList.remove(afterForPcForEachEveryForList.size() - 1);
			continueForEach = continueForEachList.remove(continueForEachList.size() - 1);
		}
		
		if (numFor > 0 && inForEach == 0) {
			afterForPcList = afterForPcForEveryForList.remove(afterForPcForEveryForList.size() - 1);
		}
	}

	public void visit(BeginingIf beginingIf) {
		if (numIf>0 || numFor > 0) {
			condPcList.add(condPc);
			stmtPcList.add(stmtPc);
			ListAndListELSE.add(andListELSE);
			ListAndListOr.add(andListOR);
			ListOrList.add(orList);
		}
		
		condPc = 0;
		stmtPc = 0;
		andListELSE = new ArrayList<>();
		andListOR = new ArrayList<>();
		orList = new ArrayList<>();
		numIf++;
	}
	
	public void visit(StatementIf statementIf) {		
		Code.fixup(stmtPc+1);
		numIf--;
		if (numIf>0 || numFor > 0) {
			condPc = condPcList.remove(condPcList.size() - 1);
			stmtPc = stmtPcList.remove(stmtPcList.size() - 1);
			andListELSE = ListAndListELSE.remove(ListAndListELSE.size() - 1);
			andListOR = ListAndListOr.remove(ListAndListOr.size() - 1);
			orList = ListOrList.remove(ListOrList.size() - 1);
		}
	}
	
	public void visit(StatementIfElse statementIfElse) {	
		Code.fixup(stmtPc+1);
		numIf--;
		if (numIf>0 || numFor > 0) {
			condPc = condPcList.remove(condPcList.size() - 1);
			stmtPc = stmtPcList.remove(stmtPcList.size() - 1);
			andListELSE = ListAndListELSE.remove(ListAndListELSE.size() - 1); 
			andListOR = ListAndListOr.remove(ListAndListOr.size() - 1); 
			orList = ListOrList.remove(ListOrList.size() - 1);
		}
	}
	
	public void visit(StmtIf StmtIf) {
		stmtPc = Code.pc;
		Code.putJump(0);//preskoci sve stmt za else
		Code.fixup(condPc + 1);
		
		//ovde bi else trebao da krece...---> da ovde skacem na else ako postoji, ako ne samo preskacem if
		for(int i=0; i < andListELSE.size() ;i ++) {
			Code.fixup(andListELSE.get(i) + 1);
		}
		andListELSE.clear();
	}
	
	
	public void visit(CondIf condIf) {
		condPc = Code.pc;
		Code.putJump(0); // preskoci sve statamente za if... dosao si do kraja uslova, nijedan OR nije true
		
		for (int i=0 ; i<orList.size() ; i++) {
			Code.fixup(orList.get(i)+1);
		}
		
		orList.clear();
	}
	

	public void visit(CondTerms condTerms) {
		for(int i = 0 ; i< andListOR.size() ; i++) {
			Code.fixup(andListOR.get(i) + 1);
		}
		andListOR.clear();
	}
	

	
	public void visit(CondFactNoRelop cond) {
		if (cond.getParent() instanceof CondTerms) {
			CondTerms parent = (CondTerms) cond.getParent();
			if (parent.getCondTermList() instanceof MyNoCondTermList) {
				//ako imam samo x>y == true
				Code.loadConst(1);
				orList.add(Code.pc);
				Code.putFalseJump(Code.ne,0);
				return;
			}
		}
		
		if (cond.getParent() instanceof MyCondTermList) {
			MyCondTermList par = (MyCondTermList) cond.getParent();
			if (par.getParent() instanceof CondTerms) {
				//ako sam poslednji u and
				Code.loadConst(1);
				orList.add(Code.pc);
				Code.putFalseJump(Code.ne, 0);
				return;
			}
			
		}
		
		if (cond.getParent() instanceof CondTerms) {
			CondTerms parent = (CondTerms) cond.getParent();
			if (parent.getCondTermList() instanceof MyCondTermList) {
				if (parent.getParent() instanceof Condition) {
					Condition par2 = (Condition) parent.getParent();
					if(par2.getConditionList() instanceof MyNoConditionList) {
						andListELSE.add(Code.pc + 1);
					}
					else {
						andListOR.add(Code.pc + 1);
					}
				}
				else {
					if (parent.getParent() instanceof MyConditionList) {
						MyConditionList par2 = (MyConditionList) parent.getParent();
						if (par2.getParent() instanceof Condition) {
							andListELSE.add(Code.pc + 1);
						}
						else {
							andListOR.add(Code.pc + 1);
						}
					}
				}
				Code.loadConst(0);
				Code.putFalseJump(Code.ne,0);
				return;
			}
		}
		
		if (cond.getParent() instanceof MyCondTermList) {
			MyCondTermList list = (MyCondTermList) cond.getParent();
			while(list.getParent() instanceof MyCondTermList) {
				list = (MyCondTermList) list.getParent();
			}
			
			CondTerms parent = (CondTerms) list.getParent();
			
			if (parent.getCondTermList() instanceof MyCondTermList) {
				if (parent.getParent() instanceof Condition) {
					Condition par2 = (Condition) parent.getParent();
					if(par2.getConditionList() instanceof MyNoConditionList) {
						andListELSE.add(Code.pc + 1);
					}
					else {
						andListOR.add(Code.pc + 1);
					}
				}
				else {
					if (parent.getParent() instanceof MyConditionList) {
						MyConditionList par2 = (MyConditionList) parent.getParent();
						if (par2.getParent() instanceof Condition) {
							andListELSE.add(Code.pc + 1);
						}
						else {
							andListOR.add(Code.pc + 1);
						}
					}
				}
				Code.loadConst(0);
				Code.putFalseJump(Code.ne,0);
				return;
			}
		}
		
		
		Code.put(Code.pop);
	}
	
	public void visit(CondFactRelop cond) {
    	Relop op = cond.getRelop();
    	int myLen = 3;
    	
    	if (op instanceof Same) {
    		Code.putFalseJump(Code.eq, Code.pc + myLen + 4);
    	}
    	
    	if (op instanceof Different) {
    		Code.putFalseJump(Code.ne, Code.pc + myLen + 4);
    	}
    	
    	if (op instanceof Greater) {
    		Code.putFalseJump(Code.gt, Code.pc + myLen + 4);
    	}
    	

    	if (op instanceof GreaterOrEqual) {
    		Code.putFalseJump(Code.ge, Code.pc + myLen + 4);
    	}
    	
    	if (op instanceof Smaller) {
    		Code.putFalseJump(Code.lt, Code.pc + myLen + 4);
    	}
    	
    	if (op instanceof SmallerOrEqual) {
    		Code.putFalseJump(Code.le, Code.pc + myLen + 4);
    	}
    	
    	Code.loadConst(1);
		Code.putJump(Code.pc+ myLen +1);
		Code.loadConst(0);	
		
		// stavila rez Relop na stek
		if (cond.getParent() instanceof CondTerms) {
			CondTerms parent = (CondTerms) cond.getParent();
			if (parent.getCondTermList() instanceof MyNoCondTermList) {
				//ako imam samo x>y == true
				Code.loadConst(1);
				orList.add(Code.pc);
				Code.putFalseJump(Code.ne,0);
				return;
			}
		}
		
		if (cond.getParent() instanceof MyCondTermList) {
			MyCondTermList par = (MyCondTermList) cond.getParent();
			if (par.getParent() instanceof CondTerms) {
				//ako sam poslednji u and
				Code.loadConst(1);
				orList.add(Code.pc);
				Code.putFalseJump(Code.ne, 0);
				return;
			}
			
		}
		
		if (cond.getParent() instanceof CondTerms) {
			CondTerms parent = (CondTerms) cond.getParent();
			if (parent.getCondTermList() instanceof MyCondTermList) {
				if (parent.getParent() instanceof Condition) {
					Condition par2 = (Condition) parent.getParent();
					if(par2.getConditionList() instanceof MyNoConditionList) {
						andListELSE.add(Code.pc + 1);
					}
					else {
						andListOR.add(Code.pc + 1);
					}
				}
				else {
					if (parent.getParent() instanceof MyConditionList) {
						MyConditionList par2 = (MyConditionList) parent.getParent();
						if (par2.getParent() instanceof Condition) {
							andListELSE.add(Code.pc + 1);
						}
						else {
							andListOR.add(Code.pc + 1);
						}
					}
				}
				Code.loadConst(0);
				Code.putFalseJump(Code.ne,0);
				return;
			}
		}
		
		if (cond.getParent() instanceof MyCondTermList) {
			MyCondTermList list = (MyCondTermList) cond.getParent();
			while(list.getParent() instanceof MyCondTermList) {
				list = (MyCondTermList) list.getParent();
			}
			
			CondTerms parent = (CondTerms) list.getParent();
			
			if (parent.getCondTermList() instanceof MyCondTermList) {
				if (parent.getParent() instanceof Condition) {
					Condition par2 = (Condition) parent.getParent();
					if(par2.getConditionList() instanceof MyNoConditionList) {
						andListELSE.add(Code.pc + 1);
					}
					else {
						andListOR.add(Code.pc + 1);
					}
				}
				else {
					if (parent.getParent() instanceof MyConditionList) {
						MyConditionList par2 = (MyConditionList) parent.getParent();
						if (par2.getParent() instanceof Condition) {
							andListELSE.add(Code.pc + 1);
						}
						else {
							andListOR.add(Code.pc + 1);
						}
					}
				}
				Code.loadConst(0);
				Code.putFalseJump(Code.ne,0);
				return;
			}
		}
		
		
		Code.put(Code.pop);
	}
	
	public void visit(MinusExpr minusExpr) {		
//		Code.loadConst(0);
//		Code.put(Code.dup_x1);
//		Code.put(Code.pop);
//		Code.put(Code.sub);
	}
	
	public void visit(MinusTerm minusTerm) {
		Code.loadConst(0);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.sub);
	}
	
	public void visit(ExprListElems exprListElems) {
		ListAdd listAdd = (ListAdd) exprListElems.getListAddop();
		
		//if(listAdd.getAddop() instanceof AddLeft) {
			AddopLeft addLeft  = (AddopLeft) listAdd.getAddopLeft();
			
			if(addLeft instanceof Plus) {
				Code.put(Code.add);
			}
			
			if(addLeft instanceof Minus) {
				Code.put(Code.sub);
			}
			
//		}
//		else {
//			AddRight addRight = (AddRight) listAdd.getAddop();
//			
//			if (listAdd.obj != null) {
//				if (listAdd.obj.getKind() == Obj.Elem) {	
//					if(addRight.getAddopRight() instanceof PlusRight) {
//						Code.put(Code.add);
//						Code.put(Code.dup_x2);
//						Code.store(listAdd.obj);
//					}
//					
//					if(addRight.getAddopRight() instanceof MinusRight) {
//						Code.put(Code.sub);
//						Code.put(Code.dup_x2);
//						Code.store(listAdd.obj);
//					}
//				}
//				else if (listAdd.obj.getKind() == Obj.Fld){
//					if(addRight.getAddopRight() instanceof PlusRight) {
//						Code.put(Code.add);
//						Code.put(Code.dup_x1);
//						Code.store(listAdd.obj);
//					}
//					
//					if(addRight.getAddopRight() instanceof MinusRight) {
//						Code.put(Code.sub);
//						Code.put(Code.dup_x1);
//						Code.store(listAdd.obj);
//					}
//				}
//				
//				else {
//					if(addRight.getAddopRight() instanceof PlusRight) {
//						Code.put(Code.add);
//						Code.put(Code.dup);
//						Code.store(listAdd.obj);
//					}
//					
//					if(addRight.getAddopRight() instanceof MinusRight) {
//						Code.put(Code.sub);
//						Code.put(Code.dup);
//						Code.store(listAdd.obj);
//					}
//				}
//			}
//		}
		
	}

	public void visit(TermListElems termListElems) {
		ListMul listMulop = (ListMul) termListElems.getListMulop();
		
		//if(listMulop.getMulop() instanceof MulLeft) {

		MulopLeft operator = (MulopLeft) listMulop.getMulopLeft();
			
		if (operator instanceof Mul) {
			Code.put(Code.mul);
		}
		
		if (operator instanceof Div) {
			Code.put(Code.div);
		}
		
		if (operator instanceof Mod) {
			Code.put(Code.rem);
		}
		//}
//		else {
//			
//			MulRight operator = (MulRight) listMulop.getMulop();
//			
//			if (listMulop.obj != null) {
//				if (listMulop.obj.getKind() == Obj.Elem) {
//					
//					if (operator.getMulopRight() instanceof MulR) {
//						Code.put(Code.mul);
//						Code.put(Code.dup_x2);
//						Code.store(listMulop.obj);
//					}
//					if (operator.getMulopRight() instanceof DivR) {
//						Code.put(Code.div);
//						Code.put(Code.dup_x2);
//						Code.store(listMulop.obj);
//					}
//					if (operator.getMulopRight() instanceof ModR) {
//						Code.put(Code.rem);
//						Code.put(Code.dup_x2);
//						Code.store(listMulop.obj);
//					}
//
//				}
//				
//				else if (listMulop.obj.getKind() == Obj.Fld) {
//					if (operator.getMulopRight() instanceof MulR) {
//						Code.put(Code.mul);
//						Code.put(Code.dup_x1);
//						Code.store(listMulop.obj);
//					}
//					if (operator.getMulopRight() instanceof DivR) {
//						Code.put(Code.div);
//						Code.put(Code.dup_x1);
//						Code.store(listMulop.obj);
//					}
//					if (operator.getMulopRight() instanceof ModR) {
//						Code.put(Code.rem);
//						Code.put(Code.dup_x1);
//						Code.store(listMulop.obj);
//					}
//				}
//				else {
//					if (operator.getMulopRight() instanceof MulR) {
//						Code.put(Code.mul);
//						Code.put(Code.dup);
//						Code.store(listMulop.obj);
//					}
//					if (operator.getMulopRight() instanceof DivR) {
//						Code.put(Code.div);
//						Code.put(Code.dup);
//						Code.store(listMulop.obj);
//					}
//					if (operator.getMulopRight() instanceof ModR) {
//						Code.put(Code.rem);
//						Code.put(Code.dup);
//						Code.store(listMulop.obj);
//					}
//				}
//			}
//			
//		}
		
	}
	
	
	public void visit(NumberFactor numberFactor) {
		Code.loadConst(numberFactor.getValNum());
		designator = null;
	}
	
	public void visit(CharFactor charFactor) {
		Code.loadConst(charFactor.getValChar());
		designator = null;
	}
	
	public void visit(BoolFactor boolFactor) { // true - 1 ; else - 0;
		if (boolFactor.getVarBool().equals("true")) {
			Code.loadConst(1);
		}
		else {
			Code.loadConst(0);
		}
		designator = null;
	}
	
	public void visit(ExprFactor exprFactor) {
		designator = null;
	}
	
	
	public void visit(Var var) {
		//checkAssignOp(var);
		Code.load(var.getDesignator().obj);
		designator = var.getDesignator().obj;
	}
	
	public void visit(DesVar var) {
		//checkAssignOp(var);
		//Code.load(var.getDesignator().obj);
		designator = var.getDesignator().obj;
	}
	
	private void checkAssignOp(DesVar var) {
		if (var.getDesignator().obj.getKind() != Obj.Elem && var.getDesignator().obj.getKind() != Obj.Fld) {
			return;
		}
		
		if (var.getDesignator().obj.getKind() == Obj.Elem ) {
			Code.put(Code.dup2);
		}
		else { // Obj.Fld
			Code.put(Code.dup);
		}
		
//		if (var.getParent() instanceof TermListElems) { //TermListElems
//			TermListElems par = (TermListElems) var.getParent();
//			
//			//MyTermList
//			MyTermList secPar = (MyTermList) par.getParent();
//			if (secPar.getTermList() instanceof MyNoTermList) {
//				return;
//			}
//			MyTermList rightSub = (MyTermList) secPar.getTermList();
//			TermListElems elem = (TermListElems) rightSub.getTermListElem();
//			
//			ListMul mulOp = (ListMul) elem.getListMulop();
//			
//			if(mulOp.getMulop() instanceof MulRight) {
//				if (var.getDesignator().obj.getKind() == Obj.Elem ) {
//					Code.put(Code.dup2);
//				}
//				else { // Obj.Fld
//					Code.put(Code.dup);
//				}
//			}
//			
//		}
//		else { // Terms
//			Terms par = (Terms) var.getParent();
//			
//			if (par.getTermList() instanceof MyTermList) {
//				MyTermList list = (MyTermList) par.getTermList();
//
//				TermListElems elem = (TermListElems) list.getTermListElem();
//				
//				ListMul mulOp = (ListMul) elem.getListMulop();
//				
//				if(mulOp.getMulop() instanceof MulRight) {
//					if (var.getDesignator().obj.getKind() == Obj.Elem ) {
//						Code.put(Code.dup2);
//					}
//					else { // Obj.Fld
//						Code.put(Code.dup);
//					}
//				}
//			}
//			
//			else {
//				if (par.getParent() instanceof ExprListElems) {
//					ExprListElems secPar = (ExprListElems) par.getParent();
//					
//					if (secPar.getParent() instanceof MyExprList) {
//						MyExprList lista = (MyExprList) secPar.getParent();
//						if (lista.getExprList() instanceof MyNoExprList) {
//							return; // dodato 15.04.2020.
//						}
//						if (lista.getExprList() instanceof MyExprList) { // dodato 15.04.2020.
//							MyExprList RightSub = (MyExprList) lista.getExprList();
//							if (RightSub.getExprList() instanceof MyNoExprList) {
//								return;
//							}
//							ExprListElems elem = (ExprListElems) RightSub.getExprListElem();
//							
//							ListAdd addOp = (ListAdd) elem.getListAddop();
//							
//							if (addOp.getAddop() instanceof AddRight) {
//								if (var.getDesignator().obj.getKind() == Obj.Elem ) {
//									Code.put(Code.dup2);
//								}
//								else { // Obj.FLd
//									Code.put(Code.dup);
//								}
//							}
//						}
//					}
//				}
//				
//				if (par.getParent() instanceof MinusTerm) {
//					MinusExpr minusExp = (MinusExpr) par.getParent().getParent();
//	
//					if (minusExp.getExprList() instanceof MyExprList) {
//						ExprList listaSled = minusExp.getExprList();
//						MyExprList lista = (MyExprList) listaSled;
//						ExprListElems elem = (ExprListElems) lista.getExprListElem();
//						ListAdd addOp = (ListAdd) elem.getListAddop();
//						if (addOp.getAddop() instanceof AddRight) {
//							if (var.getDesignator().obj.getKind() == Obj.Elem ) {
//								Code.put(Code.dup2);
//							}
//							else { //Obj.Fld
//								Code.put(Code.dup);
//							}
//						}
//					}
//				}
//				
//				if (par.getParent() instanceof NoMinusExpr) {
//	
//					NoMinusExpr noMinusExpr = (NoMinusExpr) par.getParent();
//					
//					if (noMinusExpr.getExprList() instanceof MyExprList) {
//						ExprList listaSled = noMinusExpr.getExprList();
//						MyExprList lista = (MyExprList) listaSled;
//						ExprListElems elem = (ExprListElems) lista.getExprListElem();
//						ListAdd addOp = (ListAdd) elem.getListAddop();
//						if (addOp.getAddop() instanceof AddRight) {
//							if (var.getDesignator().obj.getKind() == Obj.Elem ) {
//								Code.put(Code.dup2);
//							}
//							else { // Obj.Fld
//								Code.put(Code.dup);
//							}
//						}
//					}
//				}
//			}
//		}
		
		
	}
	
	
	public void visit(NewFactor newFactor) { // provera da li je tip class ili array...
		Code.put(Code.newarray);
		if(newFactor.getType().struct.compatibleWith(Tab.charType)) {
			Code.put(0); // array of char's in Bytes
		}
		else {
			Code.put(1); // array of words (32b)
		}
		designator = null;
	}
	
	
	public void visit(FuncCallFactorDesignator funcCallFactorDesignator) {
//		Design des = (Design) funcCallFactorDesignator.getDesignator();
//		
//		if (des.getDesignatorElemList() instanceof MyDesignatorElemList) {
//			Code.load(designator);
//		}
	}
	
	public void visit(FuncCallFactor factor) {
		Obj des = factor.getFuncCallFactorDesignator().getDesignator().obj;
		if (des.getName().equals("chr") || des.getName().equals("ord")){
			return;
		}
		if(des.getName().equals("len")) { // npr ako je fpPos == 0 uradi ovo, u universe opsegu je, inace je moja f-ja
			Code.put(Code.arraylength);
			return;
		}
		
		Design design = (Design) factor.getFuncCallFactorDesignator().getDesignator();
		
		if (design.getDesignatorElemList() instanceof MyDesignatorElemList) {
			// treba mi this :(
			DesignatorVisitor desVisitor = new DesignatorVisitor();
			desVisitor.setInClass(inClass);
			desVisitor.setMyClasses(myClasses);
			//factor.getFuncCallFactorDesignator().traverseTopDown(desVisitor);
			design.traverseBottomUp(desVisitor);
			// valjda je tu
			Code.put(Code.getfield);
			Code.put2(0);
			Code.put(Code.invokevirtual);
			String name = factor.getFuncCallFactorDesignator().getDesignator().obj.getName();
			for (int i = 0; i < name.length(); i++) {
				Code.put4(name.charAt(i));
			}
			Code.put4(-1);
		}
		else if (design.getDesignatorElemList() instanceof MyNoDesignatorElemList && inClass == 1){
			Code.put(Code.load_n + 0); // ovo ce biti moj this, jer sam u f-ji
			Code.put(Code.getfield);
			Code.put2(0);
			Code.put(Code.invokevirtual);
			String name = factor.getFuncCallFactorDesignator().getDesignator().obj.getName();
			for (int i = 0; i < name.length(); i++) {
				Code.put4(name.charAt(i));
			}
			Code.put4(-1);
			
		}else {
			int offset = factor.getFuncCallFactorDesignator().getDesignator().obj.getAdr() - Code.pc;
			Code.put(Code.call);
			Code.put2(offset);
		}
		
		designator = null;
	}
	
	public void visit (NewClass newClass) {
		Struct mojaKlasa = newClass.getType().struct;
		Struct predKlasa = mojaKlasa.getElemType();
		
		int numFields = mojaKlasa.getNumberOfFields();
		
		while (predKlasa != null) {
			numFields = numFields + predKlasa.getNumberOfFields();
			predKlasa = predKlasa.getElemType();
		}
		
		int num = numFields * 4;
		num = num + 4; // additional space for pointer for table virtual functions
		
		Code.put(Code.new_);
		Code.put2(num); 
		// alocira se obj na heap-u
		
		int adr = -1;
		
		for (int i = 0; i < myClasses.size(); i++) {
			if (myClasses.get(i).getType() == mojaKlasa) {
				adr = myClasses.get(i).getAdr();
				break;
			}
		}
		
		Code.put(Code.dup); // adresa alociranog obj na heap-u, adresa alociranog obj na heap-u
		Code.loadConst(adr);
		Code.put(Code.putfield);
		Code.put2(0);
		
		// adresa alociranog obj na heap-u
	}
	
	

	public void visit(DesignatorIdent designatorIdent) {
		if (designatorIdent.obj.getKind() == Obj.Fld || designatorIdent.obj.getKind() == Obj.Meth && inClass == 1) {
			if (designatorIdent.getName().equals("chr") || designatorIdent.getName().equals("ord") || designatorIdent.getName().equals("len")) {
				// nemoj da loadujes this; globalne f-je u universe scope-u, jedine definisane pre klasa
			}
			else {
				Code.put(Code.load_n + 0); // ubaci "this" ! // a ne moze se pristupati polju klase van metode? ---> ne moze sa this(nema objekta)
			}
		}
		
		// 18.06.2020.
		
		//

			
		if (designatorIdent.getParent() instanceof Design) {
			Design design = (Design) designatorIdent.getParent();
			if (design.getDesignatorElemList() instanceof MyNoDesignatorElemList) {
				// nista ne radi ---> ti si jedini(poslednji u listi designatora)
			} 
			else {
				Code.load(designatorIdent.obj); // ako je this isto ce ici load0
			}
		}
		
	}
	
	public void visit(Design design) {
		Obj des = design.obj;		
		designator = des;
	}
	
	public void visit(ArrayElem arrayElem) {
		MyDesignatorElemList parent = (MyDesignatorElemList) arrayElem.getParent();
		
		if (parent.getDesignatorElemList() instanceof MyDesignatorElemList){
			Code.load(arrayElem.obj);
		}

	}
	
	public void visit(ClassField classField) {
		MyDesignatorElemList parent = (MyDesignatorElemList) classField.getParent();
		
		if (parent.getDesignatorElemList() instanceof MyDesignatorElemList){
			Code.load(classField.obj);
		}
		
	}
	
	// PROVERA!!!
	
	public void visit(ListAdd listAdd) {
		//Code.load(designator);
		listAdd.obj = designator;
	}
	
	public void visit(ListMul listMul) {
		//Code.load(designator);
		listMul.obj = designator;
	}

		
//	public void visit(Assignment assignment) {
//		Code.store(assignment.getDesignator().obj);
//	}
//
//	public void visit(Designator designator) {
//		SyntaxNode parent =  designator.getParent();
//		
//		if(Assignment.class != parent.getClass() && FuncCall.class != parent.getClass()) {
//			Code.load(designator.obj);
//		}
//	}
//	
//	public void visit(FuncCall funcCall) {
//		Obj functionObj = funcCall.getDesignator().obj;
//		int offset = functionObj.getAdr() - Code.pc; // pc-rel vr;
//		Code.put(Code.call);
//		Code.put2(offset);
//	}
//	
//	
//	public void visit(ProcCall procCall) {
//		Obj functionObj = procCall.getDesignator().obj;
//		int offset = functionObj.getAdr() - Code.pc; // pc-rel vr;
//		Code.put(Code.call);
//		Code.put2(offset);
//		if(procCall.getDesignator().obj.getType() != Tab.noType) {
//			Code.put(Code.pop);
//		}
//	}
//	
//	public void visit(ReturnExpr returnExpr) {
//		Code.put(Code.exit);
//		Code.put(Code.return_);
//	}
//	
//	public void visit(ReturnNoExpr returnNoExpr) {
//		Code.put(Code.exit);
//		Code.put(Code.return_);
//	}
//	
//	public void visit(AddExpr addExpr) {
//		Code.put(Code.add);
//	}
	
	public int getnVars() {
		return nVars;
	}


	public void setnVars(int nVars) {
		this.nVars = nVars;
		virtFuncTableAdr = nVars;
	}



	public int getVirtFuncTableAdr() {
		return virtFuncTableAdr;
	}



	public void setVirtFuncTableAdr(int virtFuncTableAdr) {
		this.virtFuncTableAdr = virtFuncTableAdr;
	}
	
	
}
