package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.sound.midi.Soundbank;

import org.apache.log4j.Logger;

import com.sun.org.apache.bcel.internal.generic.NEWARRAY;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.sun.swing.internal.plaf.basic.resources.basic;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;
import rs.etf.pp1.symboltable.structure.SymbolDataStructure;

public class SemanticPass extends VisitorAdaptor {
	
	Struct boolType = new Struct(Struct.Bool);

	int printCallCount = 0;
	int varDeclCount = 0;
	Obj currentMethod = null;
	Struct currentType = null;
	
	ArrayList<Obj> currMethodsObjects = new ArrayList<>();
	ArrayList<Integer> rBrActParams = new ArrayList<>();
	int currMethObjIndex = 0;
	int rBrActParam = 0;
	
	private int inFor = 0;
	
	
	
	int nFormPars = 0;
	
	
	boolean returnFound = false;
	boolean errorDetected = false;
	int nVars;
	
	Obj isDesignator = null;
	
	
	private ArrayList<Obj> indexElems = new ArrayList<>();
	
	

	private int inAbstractMethod = 0;
	private int regularClassExtendsAbstractClass = 0;
	private Struct currentClass = null;
	private Struct currentAbsClass = null;
	
	private Obj designatorObject = Tab.noObj;
	private int callFunctionClassField = 0;
	
	private int accessRight = 0; // 0 - podrazumevano ; 1 - public ; 2 - protected ; 3 - private;
	
	Logger log = Logger.getLogger(getClass());
	

	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message); 
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.info(msg.toString());
	}
	
	public static Obj findInMyScope(String name) {
		Obj resultObj = null;
		Scope s = Tab.currentScope();
		if (s.getLocals() != null) {
			resultObj = s.getLocals().searchKey(name);
		}
		return (resultObj != null) ? resultObj : Tab.noObj;
	}
	
	public void visit(VarDecl varDecl) {
		currentType = null;
	}
	
	public void visit(PublicRight publicRight) {
		if ((currentClass == null && currentAbsClass == null)) {
			report_error("Semanticka greska na liniji : " + publicRight.getParent().getLine() + " Public pravo pristupa se moze dodeljivati jedino poljima i metodama klase! ", null);
		}
		
		else if (currentMethod != null) {
			report_error("Semanticka greska na liniji : " + publicRight.getParent().getLine() +" Public pravo pristupa se moze dodeljivati jedino poljima i metodama klase! ", null);
		}
		
		else {
			accessRight = 1;
		}
	}
	
	public void visit(ProtectedRight protectedRight) {
		if ((currentClass == null && currentAbsClass == null)) {
			report_error("Semanticka greska na liniji : " + protectedRight.getParent().getLine() +" Protected pravo pristupa se moze dodeljivati jedino poljima i metodama klase! ", null);
		}
		else if (currentMethod != null) {
			report_error("Semanticka greska na liniji : " + protectedRight.getParent().getLine() +" Protected pravo pristupa se moze dodeljivati jedino poljima i metodama klase! ", null);
		}
		
		else {
			accessRight = 2;
		}
	}
	
	public void visit(PrivateRight privateRight) {
		if ((currentClass == null && currentAbsClass == null) || currentMethod != null) {
			report_error("Semanticka greska na liniji : " + privateRight.getParent().getLine() + " Private pravo pristupa se moze dodeljivati jedino poljima i metodama klase! ", null);
		}
		
		else if (currentMethod != null) {
			report_error("Semanticka greska na liniji : " + privateRight.getParent().getLine() + " Private pravo pristupa se moze dodeljivati jedino poljima i metodama klase! ", null);
		}
		
		else {
			accessRight = 3;
		}
	}
	
	public void visit(NoRights noRights) {
		if ((currentClass != null || currentAbsClass != null) && currentMethod == null) {
			report_error("Semanticka greska na liniji : " + noRights.getParent().getLine() +" Uz polje ili metodu klase mora stajati pravo pristupa ! ", null);
		}
		
		else {
			accessRight = 0;
		}
	}

	public void visit(VarDeclElemNoBraces varDeclElemNoBraces){	
		Obj alreadyExists = findInMyScope(varDeclElemNoBraces.getVarName());
		if (alreadyExists == Tab.noObj) {
			varDeclCount++;
			
			if ((currentClass != null || currentAbsClass != null) && currentMethod == null) { // kakooo?
				report_info("Deklarisano polje klase  "+ varDeclElemNoBraces.getVarName(), varDeclElemNoBraces);
				Obj varNode = Tab.insert(Obj.Fld, varDeclElemNoBraces.getVarName(), currentType);
				varNode.setFpPos(accessRight); // 15.04.2020.
			}
			else {
				report_info("Deklarisana promenljiva "+ varDeclElemNoBraces.getVarName(), varDeclElemNoBraces);
				Obj varNode = Tab.insert(Obj.Var, varDeclElemNoBraces.getVarName(), currentType);
				//varNode.setFpPos(accessRight); // 15.04.2020.
			}
		}
		else {
			report_error ("Semanticka greska na liniji " + varDeclElemNoBraces.getLine() + ": Simbol se vec nalazi definisan u tabeli simbola!", null );
		}
	}
	
	public void visit(VarDeclElemBraces varDeclElemBraces){
		Obj alreadyExists = findInMyScope(varDeclElemBraces.getVarName());
		if (alreadyExists == Tab.noObj) {
			varDeclCount++;
			
			if ((currentClass != null || currentAbsClass != null) && currentMethod == null) {
				report_info("Deklarisan polje klase "+ varDeclElemBraces.getVarName(), varDeclElemBraces);
				Obj varNode = Tab.insert(Obj.Fld, varDeclElemBraces.getVarName(), new Struct(Struct.Array, currentType));
				varNode.setFpPos(accessRight); // 15.04.2020.
			}
			else {
				report_info("Deklarisana promenljiva "+ varDeclElemBraces.getVarName(), varDeclElemBraces);
				Obj varNode = Tab.insert(Obj.Var, varDeclElemBraces.getVarName(), new Struct(Struct.Array, currentType));
				//varNode.setFpPos(accessRight); // 15.04.2020.
			}
			
		}
		else {
			report_error ("Semanticka greska na liniji " + varDeclElemBraces.getLine() + ": Simbol se vec nalazi definisan u tabeli simbola!", null );
		}
	}
	
	
	public void visit(VarDeclType varDeclType) {
		if (currentType != null) {
			report_error ("Semanticka greska na liniji " + varDeclType.getLine() + ": Dupla definicija tipa podataka deklaracije promenjljivih", null );
		}
		else {
			currentType = varDeclType.getType().struct;
		}
	}
	
	public void visit(ConstDecl constDecl) {
		currentType = null;
	}
	
	public void visit(ConstDeclElem constDeclElem) {
		Obj alreadyExists = findInMyScope(constDeclElem.getVarName());
		if (alreadyExists == Tab.noObj) {
			if (constDeclElem.getConstType() instanceof NumConst && currentType.compatibleWith(Tab.intType)) {
				report_info("Deklarisana konstanta tipa int "+ constDeclElem.getVarName(), constDeclElem);
				Obj constNode = Tab.insert(Obj.Con, constDeclElem.getVarName(), currentType);
				int num =( (NumConst) constDeclElem.getConstType()).getN1();
				constNode.setAdr(num);
				return;
			}
			
			if (constDeclElem.getConstType() instanceof CharConst && currentType.compatibleWith(Tab.charType)) {
				report_info("Deklarisana konstanta tipa char "+ constDeclElem.getVarName(), constDeclElem);
				Obj constNode = Tab.insert(Obj.Con, constDeclElem.getVarName(), currentType);
				char c =( (CharConst) constDeclElem.getConstType()).getC1();
				constNode.setAdr(c);
				return;
			}
			
			if (constDeclElem.getConstType() instanceof BoolConst && currentType.compatibleWith(boolType)) {
				report_info("Deklarisana konstanta tipa bool "+ constDeclElem.getVarName(), constDeclElem);
				Obj constNode = Tab.insert(Obj.Con, constDeclElem.getVarName(), currentType);
				String  b =( (BoolConst) constDeclElem.getConstType()).getB1();
				if(b.equals("true")) {
					constNode.setAdr(1);
				} else if(b.equals("false")) {
					constNode.setAdr(0);
				}else {
					report_error ("Semanticka greska na liniji " + constDeclElem.getLine() + ": Promenljiva tipa boolean mora imati vrednost true/false!", null );
				}
				
				return;
			}
			
			report_error ("Semanticka greska na liniji " + constDeclElem.getLine() + ": Tip i vrednost konstante nisu kompatibilni!", null );
		}
		else {
			report_error ("Semanticka greska na liniji " + constDeclElem.getLine() + ": Simbol se vec nalazi definisan u tabeli simbola!", null );
		}
	}
	
	public void visit(ConstDeclType constDeclType) {
		if (currentType != null) {
			report_error ("Semanticka greska na liniji " + constDeclType.getLine() + ": Dupla definicija tipa podataka deklaracije promenjljivih", null );
		}
		else {
			currentType = constDeclType.getType().struct; // da li treba provera dal je ovo tip? ---> mislim to bi vec trebalo da je provereno kad
														 // je obradjivan cvor Type zar ne? - DA!
		}
	}
	
	// C nivo
	
	private HashMap<Struct, Obj> structObjMappingForClasses = new HashMap<>();
	
	public void visit(ClassType classType) {
		Obj alreadyExists = findInMyScope(classType.getClassName());
    	if(alreadyExists == Tab.noObj) {
    		currentClass = new Struct(Struct.Class);
    		currentClass.setElementType(null);
    		Obj tip = Tab.insert(Obj.Type, classType.getClassName(),currentClass);
    		
    		structObjMappingForClasses.put(currentClass, tip);
   		
//    		Obj pom = structObjMappingForClasses.get(currentClass);
//    		System.out.println();

    		classType.obj = tip;
    		Tab.openScope();
    		report_info("Obradjuje se unutrasnja klasa programa " + classType.getClassName(), classType);
    	}
    	else {
			report_error ("Semanticka greska na liniji " + classType.getLine() + ": Simbol se vec nalazi definisan u tabeli simbola!", null );
    	}
	}
	
	public void visit(MyExtendsClass myExtendsClass) {
		if (myExtendsClass.getType().struct != Tab.noType) { // vec ispisuje gresku ako je noType...
			if (myExtendsClass.getType().struct.getKind() != Struct.Class && myExtendsClass.getType().struct.getKind()!= Struct.Interface) {
				report_error ("Semanticka greska na liniji " + myExtendsClass.getLine() + ": tip mora predstavljati unutrasnju klasu programa!", null );
			}
			else {
				if (currentClass != null && myExtendsClass.getType().struct.getKind() == Struct.Interface) {
					regularClassExtendsAbstractClass = 1;
				}
			}
			
			if (currentClass != null) {
				currentClass.setElementType(myExtendsClass.getType().struct);
			}
			
			if (currentAbsClass != null) {
				currentAbsClass.setElementType(myExtendsClass.getType().struct);
			}
		}
	}
	
	// o1 je implementirana, o2 je apstraktna metoda
	// ali ovo sluzi i za parametre apstraktnih metoda
	public boolean mojeEquals(Obj o1, Obj o2) {
		//System.out.println(o1.getName() + " " + o2.getName());
		boolean sameRight = true; // 21.06.2020. provera prava pristupa
		boolean sameName = true;	// 22.06.2020. ako nisu metode ne moraju da imaju isto ime
		boolean sameArrayType = true; //22.6.2020 ako je parametar niz mora isti tip niza da bude
		
		if (o1.getKind() == Obj.Meth && o2.getKind() == Obj.Meth) {
			sameRight = o1.getFpPos() == o2.getFpPos()+10; 
			sameName = o1.getName().equals(o2.getName());
		}
		else {
			if (o1.getType().getKind() == Struct.Array && o2.getType().getKind() == Struct.Array) {
				if (!(o1.getType().getElemType() == o2.getType().getElemType())) { // ZA KLASE MOZDA TREBA equals
					sameArrayType = false;
				}
			}
		}
		
		// 16.08.2020.
//		if (o1.getType().getKind() == Struct.Class || o1.getType().getKind() == Struct.Interface)
//			return true;
		
		
		return o1.getKind() == o2.getKind() 
				&& sameName
				&& o1.getType().equals(o2.getType())
				&& /*adr == other.adr*/  o1.getLevel() == o2.getLevel()
				&& equalsCompleteHash(o1.getLocalSymbols(), o2.getLocalSymbols())
				&& sameRight // 21.06.2020. provera prava pristupa
				&& sameArrayType; //22.6.2020 ako je parametar niz mora isti tip niza da bude
	}
	
	public boolean equalsCompleteHash(Collection<Obj> first, Collection<Obj> second) {
		
		if (first == second) 
			return true;
		
		if (first == null || second == null)
			return false;
		
		if (first.size() >= second.size()) {
			Iterator<Obj> itH1 = first.iterator(), itH2 = second.iterator();
			itH1.next();//preskoci "this"
			itH2.next();// preskoci "this"
			
			while (itH1.hasNext() && itH2.hasNext()) {
				if (!mojeEquals(itH1.next(), itH2.next()))
					return false;
			}
			return true;
		}
		else 
			return false;
		
	}
	
	public void visit(MyClassDecl myClassDecl) {
		// treba i za apstraktnu klasu provera...
		//smao provera dal je gore u listi definisana sa istim imenoma razl parametrima ili pov tipom ---> ne treba ako ima sakrivanja imena
		if (regularClassExtendsAbstractClass == 1) { 			
			ArrayList<Struct> nadklase = new ArrayList<>();
			
//			MyExtendsClass extClass = (MyExtendsClass) myClassDecl.getExtendsClass();
//			Struct parentClassNode = extClass.getType().struct;
			
			Struct parentClassNode = currentClass.getElemType();
			
			while (parentClassNode != null && parentClassNode.getKind() == Struct.Interface ) { 
				nadklase.add(0, parentClassNode);
				parentClassNode = parentClassNode.getElemType();
			}
			
			
			ArrayList<Obj> finalMemebrs = new ArrayList<>();
			if (nadklase.size() > 0) {
				Collection<Obj> membersAbsClass = nadklase.get(0).getMembers();
			
			
				for (Iterator<Obj> iter = membersAbsClass.iterator();iter.hasNext();) {
					Obj elem = iter.next();
					if (elem.getKind() == Obj.Meth && elem.getFpPos() < 0) { // only abstract methods
						finalMemebrs.add(elem);
					}	
				}
			
			}
			
			for (int i = 1; i < nadklase.size(); i++) {
				Collection<Obj> membersSubClass = nadklase.get(i).getMembers();
				for(Iterator<Obj> iter = membersSubClass.iterator(); iter.hasNext();) {
					Obj elem = iter.next();
					if (elem.getKind() == Obj.Meth && elem.getFpPos() < 0 ) { // abs method
						finalMemebrs.add(elem); // da li treba da proveravam ako ga vec ima u nizu da ga ne ubacujem, mada mi sustinski ne smeta ako se ponovi?
					}
					else if(elem.getKind() == Obj.Meth && elem.getFpPos() >= 0) { // ne abs method
						for(int j  = 0;j <finalMemebrs.size() ; j++) {
							if (mojeEquals(elem, finalMemebrs.get(j))) {
								finalMemebrs.remove(j);
								j--;
							}
						}
					}
				}
			}
			
			
			
			//22.6.2020 ako klasa nema ni jedno polje preskoci
			if (Tab.currentScope.getLocals() != null) {
			
				Collection<Obj> myMembers = Tab.currentScope.getLocals().symbols();
				
				int allImplemented = 1;
				for (int i = 0 ; i < finalMemebrs.size();i++) {
					Obj elem = finalMemebrs.get(i);
					if (elem.getKind() == Obj.Meth && elem.getFpPos() < 0) {//abstract method
						int nasao  = 0;
						for(Iterator<Obj> iterator2 = myMembers.iterator();iterator2.hasNext();) {
							Obj meth = iterator2.next();
							if (mojeEquals(meth, elem)) {
								nasao = 1;
								break;
							}
						}
						
						if (nasao == 0) {
							allImplemented = 0;
							break;
						}
					}
					
				}
				
				if (allImplemented == 0) {
					report_error ("Semanticka greska na liniji " + myClassDecl.getLine() + ": klasa izvedena iz apstraktne klase mora implementirati sve njene apstraktne metode!", null );
				}
			}
			//22.6.2020 ako klasa nema ni jedno polje a ima abstraktnih metoda koje treba da implementira greska
			else {
				if (finalMemebrs.size()>0) {
					report_error ("Semanticka greska na liniji " + myClassDecl.getLine() +
							": klasa izvedena iz apstraktne klase mora implementirati sve njene apstraktne metode!", null );
				}
			}
		}
	

		Struct pred = currentClass.getElemType();
		int predNum = 0;
		if (pred != null) {
			
			while(pred != null) {
				predNum += pred.getNumberOfFields();
				pred = pred.getElemType();
			}
			
			// 22.6.2020 ako je klasa prazna preskoci
			if (Tab.currentScope.getLocals() != null) {
				for(Iterator<Obj> iter = Tab.currentScope().getLocals().symbols().iterator();iter.hasNext();) {
					Obj elem = iter.next();
					elem.setAdr(elem.getAdr() + predNum);
				}
			}
		}
		
		// 22.6.2020 ako je klasa prazna preskoci
		if (Tab.currentScope.getLocals() != null) {
			for(Iterator<Obj> iter = Tab.currentScope().getLocals().symbols().iterator();iter.hasNext();) {
				Obj elem = iter.next();
				elem.setAdr(elem.getAdr() + 1); //  sacuvaj mesto za pok. na tabelu virtuelnih f-ja
			}
		}
		
		Tab.chainLocalSymbols(currentClass);
    	Tab.closeScope();
    	currentClass = null;
	}

	public void visit(AbsMethodSignifications absMethodSignifications) {
		if (currentClass != null) {
			report_error ("Semanticka greska na liniji " + absMethodSignifications.getLine() + ": klasa koja nije apstraktna ne sme sadrzati abstraktne metode!", null );
		}else {
			if (currentAbsClass == null) {
				report_error ("Semanticka greska na liniji " + absMethodSignifications.getLine() + ": apstraktna metoda se ne moze deklarisati izvan tela apstraktne klase!", null );
			}
			else {
				inAbstractMethod = 1; //  da li ovo treba svakako ili smao u ovom slucaju?
			}
		}
	}
	
	public void visit(AbsClassType absClassType) {
		Obj alreadyExists = findInMyScope(absClassType.getAbsClassName());
    	if(alreadyExists == Tab.noObj) {
    		currentAbsClass = new Struct(Struct.Interface); // abstraktna klasa
    		currentAbsClass.setElementType(null);
    		Obj tip = Tab.insert(Obj.Type, absClassType.getAbsClassName(),currentAbsClass);
    		
    		structObjMappingForClasses.put(currentAbsClass, tip);
    		
    		absClassType.obj = tip;
    		Tab.openScope();
    		report_info("Obradjuje se unutrasnja apstraktna klasa programa " + absClassType.getAbsClassName(), absClassType);
    	}
    	else {
			report_error ("Semanticka greska na liniji " + absClassType.getLine() + ": Simbol se vec nalazi definisan u tabeli simbola!", null );
    	}
	}
	
	public void visit(MyAbsMethodDecl myAbsMethodDecl) {
		// mil trebalo bi cisto ko za obicnu fju close scope, chainsymbols i ostalo
		Tab.chainLocalSymbols(currentMethod);
    	currentMethod.setLevel(nFormPars);
    	Tab.closeScope();
    	returnFound = false;
    	currentMethod = null;
    	nFormPars = 0;
		inAbstractMethod = 0;
	}
	
	public void visit(MyAbstractClassDecl abstractClassDecl) {
		Struct pred = currentAbsClass.getElemType();
		int predNum = 0;
		if (pred != null) {
			
			while(pred != null) {
				predNum += pred.getNumberOfFields();
				pred = pred.getElemType();
			}
			
			// 22.6.2020 ako je klasa prazna preskoci
			if (Tab.currentScope.getLocals() != null) {
				for(Iterator<Obj> iter = Tab.currentScope().getLocals().symbols().iterator();iter.hasNext();) {
					Obj elem = iter.next();
					elem.setAdr(elem.getAdr() + predNum);
				}
			}
		}
		
		// 22.6.2020 ako je klasa prazna preskoci
		if (Tab.currentScope.getLocals() != null) {
			for(Iterator<Obj> iter = Tab.currentScope().getLocals().symbols().iterator();iter.hasNext();) {
				Obj elem = iter.next();
				elem.setAdr(elem.getAdr() + 1); //  sacuvaj mesto za pok. na tabelu virtuelnih f-ja
			}
		}

		Tab.chainLocalSymbols(currentAbsClass);
    	Tab.closeScope();
    	currentAbsClass = null;
	}
	
	// Kraj dela za C nivo
    
    public void visit(ProgName progName) {
    	progName.obj = Tab.insert(Obj.Prog, progName.getProgName(), Tab.noType);
    	Tab.insert(Obj.Type, "bool", boolType);
    	Tab.openScope();
    	
    	
    }
    
    public void visit(Program program) {
    	Obj mainFunc = Tab.find("main");
    	if (mainFunc == Tab.noObj) {
    		report_error("Nije definisana main f-ja!", null);
    	}
    	else {
    		if(mainFunc.getKind() != Obj.Meth) {
    			report_error("Nije definisana main f-ja!", null);
    		}
    		
    		if (mainFunc.getType() != Tab.noType) {
    			report_error("Povratna vrednost main funkcije mora biti void!", null);
    		}
    	}
    	nVars = Tab.currentScope.getnVars();
    	Tab.chainLocalSymbols(program.getProgName().obj);
    	Tab.closeScope();
    }
    
    
    
    public void visit(Type type) {
    	Obj typeNode = Tab.find(type.getTypeName());
    	if(typeNode == Tab.noObj){
    		report_error("Nije pronadjen tip " + type.getTypeName() + " u tabeli simbola! ", null);
    		type.struct = Tab.noType;
    	}else{
    		if(Obj.Type == typeNode.getKind()){
    			type.struct = typeNode.getType();
    		}else{
    			report_error("Greska: Ime " + type.getTypeName() + " ne predstavlja tip!", type);
    			type.struct = Tab.noType;
    		}
    	}
    }
    
    
    public void visit(MethodTypeNameRet methodTypeNameRet) {
    	Obj alreadyExists = findInMyScope(methodTypeNameRet.getMethodName());
    	if(alreadyExists == Tab.noObj) {
    		currentMethod = Tab.insert(Obj.Meth, methodTypeNameRet.getMethodName(), methodTypeNameRet.getType().struct);
    		if (inAbstractMethod == 1) {
    			currentMethod.setFpPos(-10);
    		}
    		
    		currentMethod.setFpPos(currentMethod.getFpPos() + accessRight); // 15.04.2020.
    		
    		methodTypeNameRet.obj = currentMethod;
    		Tab.openScope();
    		
    		if (currentClass != null || currentAbsClass != null) {
    			Obj implThis = null;
    			if (currentClass != null) {
    				implThis = Tab.insert(Obj.Var, "this", currentClass);
    			}
    			else {
    				implThis = Tab.insert(Obj.Var, "this", currentAbsClass);
    			}
    			nFormPars++;
    		
    		}
    		
    		if (inAbstractMethod == 1) {
    			report_info("Obradjuje se deklaracija apstraktne funkcije sa return iskazom " + methodTypeNameRet.getMethodName(), methodTypeNameRet);
    		}else {
    			report_info("Obradjuje se funkcija sa return iskazom " + methodTypeNameRet.getMethodName(), methodTypeNameRet);
    		}
    	}
    	else {
			report_error ("Semanticka greska na liniji " + methodTypeNameRet.getLine() + ": Simbol se vec nalazi definisan u tabeli simbola!", null );
    	}
    }
    
    public void visit(NoMethodTypeNameVoid methodTypeNameVoid) {
    	Obj alreadyExists = findInMyScope(methodTypeNameVoid.getMethodName());
    	if(alreadyExists == Tab.noObj) {
    		currentMethod = Tab.insert(Obj.Meth, methodTypeNameVoid.getMethodName(), Tab.noType);
    		Tab.openScope();
    		
    		
    		if (currentClass != null || currentAbsClass != null) {
    			Obj implThis = null;
    			if (currentClass != null) {
    				implThis = Tab.insert(Obj.Var, "this", currentClass);
    			}
    			else {
    				implThis = Tab.insert(Obj.Var, "this", currentAbsClass);
    			}
    			nFormPars++;
    		}
    		
    		if (inAbstractMethod == 1) {
    			currentMethod.setFpPos(-10);
    		}
    		
    		currentMethod.setFpPos(currentMethod.getFpPos() + accessRight); // 15.04.2020.
    		
    		methodTypeNameVoid.obj = currentMethod;
    		if (inAbstractMethod == 1) {
    			report_info("Obradjuje se deklaracija apstraktne funkcije bez return iskaza " + methodTypeNameVoid.getMethodName(), methodTypeNameVoid);
    		}else {
    			report_info("Obradjuje se funkcija bez return iskaza " + methodTypeNameVoid.getMethodName(), methodTypeNameVoid);
    		}
    	}
    	else {
			report_error ("Semanticka greska na liniji " + methodTypeNameVoid.getLine() + ": Simbol se vec nalazi definisan u tabeli simbola!", null );
    	}
    }
    
    
    public void visit(MethodDecl methodDecl) {
    	if(!returnFound && currentMethod.getType() != Tab.noType) {
			report_error("Semanticka greska na liniji " + methodDecl.getLine() + ": funkcija " + currentMethod.getName() + " nema return iskaz!", null);
    	}
    	Tab.chainLocalSymbols(currentMethod);
    	currentMethod.setLevel(nFormPars);
    	Tab.closeScope();
    	returnFound = false;
    	currentMethod = null;
    	nFormPars = 0;
    }
    
    public void visit(FormalParamDeclBraces formalParamDeclBraces) {
    	if (currentMethod == null) { // ne bi smelo da stoji npr int a random u programu...; nije potrebna provera i za FormPars jer je vec provereno na nizem nivou...?
    		report_error("Semanticka greska na liniji " + formalParamDeclBraces.getLine() + " Definicija parametara f-je samo unutar deklaracije metode!", null);
    	}
    	
    	Obj alreadyExists = findInMyScope(formalParamDeclBraces.getVarName());
		if (alreadyExists == Tab.noObj) {
			//varDeclCount++; ---> dal se i form par. ubraja?
			nFormPars++;
			report_info("Deklarisan formalni parametar "+ formalParamDeclBraces.getVarName() + " funkcije:" + currentMethod.getName(), formalParamDeclBraces);
			Obj varNode = Tab.insert(Obj.Var, formalParamDeclBraces.getVarName(), new Struct(Struct.Array,formalParamDeclBraces.getType().struct));
		}
		else {
			report_error ("Semanticka greska na liniji " + formalParamDeclBraces.getLine() + ": Simbol se vec nalazi definisan u tabeli simbola!", null );
		}
    }
	
    public void visit(FormalParamDeclNoBraces formalParamDeclNoBraces) {
    	if (currentMethod == null) {
    		report_error("Semanticka greska na liniji " + formalParamDeclNoBraces.getLine() + " Definicija parametara f-je samo unutar deklaracije metode!", null);
    	}
    	
    	Obj alreadyExists = findInMyScope(formalParamDeclNoBraces.getVarName());
		if (alreadyExists == Tab.noObj) {
			//varDeclCount++; ---> dal se i form par. ubraja?
			nFormPars++;
			report_info("Deklarisan formalni parametar "+ formalParamDeclNoBraces.getVarName() + " funkcije:" + currentMethod.getName(), formalParamDeclNoBraces);
			Obj varNode = Tab.insert(Obj.Var, formalParamDeclNoBraces.getVarName(), formalParamDeclNoBraces.getType().struct);
		}
		else {
			report_error ("Semanticka greska na liniji " + formalParamDeclNoBraces.getLine() + ": Simbol se vec nalazi definisan u tabeli simbola!", null );
		}
    }
    
    public void visit(StatementReturnExpr statementReturnExpr) {
    	returnFound = true;
    	if (currentMethod == null) {
			report_error("Greska na liniji " + statementReturnExpr.getLine() + " : " + " RETURN iskaz ne sme postojati izvan tela globalnih f-ja!" + currentMethod.getName(), null);
    	}
    	else {
    		Struct currMethType = currentMethod.getType();
    		
    		if (currMethType.getKind() == Struct.Class || currMethType.getKind() == Struct.Interface) { // posebna provera ako je ref na klasu
    			if(!(checkCompatibleForClasses(currMethType, statementReturnExpr.getExpr().struct))) {
    				report_error("Greska na liniji " + statementReturnExpr.getLine()+ " tip reference na klasu mora biti osnovna klasa za tip objekta koji mu se dodeljuje!", null);
    			}
    		}
    		else {
	        	if(!(statementReturnExpr.getExpr().struct.assignableTo(currMethType))){
	    			report_error("Greska na liniji " + statementReturnExpr.getLine() + " : " + "tip izraza u return naredbi ne slaze se sa tipom povratne vrednosti funkcije " + currentMethod.getName(), null);
	        	}
    		}
    	}
    }
    
    public void visit(StatementNoReturnExpr statementNoReturnExpr) {
    	//returnFound = true;---> NE BI SMELO DA BUDE TRUE AKO METODA NIJE VOID!!!
    	if (currentMethod == null) {
			report_error("Greska na liniji " + statementNoReturnExpr.getLine() + " : " + " RETURN iskaz ne sme postojati izvan tela globalnih f-ja!" + currentMethod.getName(), null);
    	}
    	else { 
	    	Struct currMethType = currentMethod.getType();
	    	if(!currMethType.compatibleWith(Tab.noType)){
				report_error("Greska na liniji " + statementNoReturnExpr.getLine() + " : " + " Tekuca metoda nije deklarisana kao VOID! " + currentMethod.getName(), null);
	    	}
    	}
    }
    
    public void visit(StatementRead statementRead) {
    	Obj des = statementRead.getDesignator().obj;
    	if (indexElems.size() > 0) { // znaci da sam u nekom levelu forEach-a!
    		for (int i = 0; i < indexElems.size() ; i++) {
	    		if (indexElems.get(i).equals(des)) {
	    			report_error("Greska na liniji " + statementRead.getLine()+ " Ne sme se vrsiti upis u promenljivu " + indexElems.get(i).getName(), null);
	    			return; // da li ovde ostaviti return ili ga pustiti nek izvrsi i ove dole provere...?
	    		}
    		}
    	}
    	if (des.getKind() != Obj.Var && des.getKind() != Obj.Elem && des.getKind() != Obj.Fld) {
    		report_error("Greska na liniji " + statementRead.getLine()+ " Designator mora oznacavati promenljivu, element niza ili polje unutar objekta", null);
    	}
    	
    	if(des.getType() != Tab.intType && des.getType() != Tab.charType && des.getType() != boolType) {
    		report_error ("Semanticka greska na liniji " + statementRead.getLine() + ": Operand instrukcije READ mora biti char ili int tipa", null );
    	}
    	
    }
	
    public void visit(StatementPrint print) {
    	if(print.getExpr().struct != Tab.intType && print.getExpr().struct!= Tab.charType && print.getExpr().struct != boolType)
    		report_error ("Semanticka greska na liniji " + print.getLine() + ": Operand instrukcije PRINT mora biti char , int ili bool tipa", null );
		printCallCount++;
	}
    
    public void visit(StatementPrintNumber print) {
    	if(print.getExpr().struct != Tab.intType && print.getExpr().struct!= Tab.charType && print.getExpr().struct != boolType)
    		report_error ("Semanticka greska na liniji " + print.getLine() + ": Prvi operand instrukcije PRINT mora biti char , int ili bool tipa", null );
		printCallCount++;
    }
    
    public void visit(StatementBreak statementBreak) {
    	if (inFor == 0 && indexElems.size() == 0) {
    		report_error ("Semanticka greska na liniji " + statementBreak.getLine() + " iskaz break se moze naci samo unutar for i foreach petlje!", null );
    	}
    }
    
    public void visit(StatementContinue statementContinue) {
    	if (inFor == 0 && indexElems.size() == 0) {
    		report_error ("Semanticka greska na liniji " + statementContinue.getLine() + " iskaz continue se moze naci samo unutar for i foreach petlje!", null );
    	}
    }
    
    public void visit(MyDesignatorForBegin myDesignatorForBegin) {
    	inFor++;
    }
    
    public void visit(MyNoDesignatorForBegin myNoDesignatorForBegin) {
    	inFor++;
    }
    
    public void visit(StatementFor statementFor) {
    	inFor--;
    }
   
    
    public void visit(ForEachIndexName index) {
    	Obj obj = Tab.find(index.getIndexName());
    	if (obj == Tab.noObj) {
    		report_error ("Semanticka greska na liniji " + index.getLine() + " Promenljiva " + index.getIndexName() + " koja se koristi u foreach petlji nije deklarisana!", null );
    	}
    	
    	index.obj = obj;
    	indexElems.add(obj);
    }
    
    public void visit(StatementForeach statementForeach) {
    	Obj designator = statementForeach.getForEachDesignator().getDesignator().obj;
    	Obj index = statementForeach.getForEachIndexName().obj;
    	
    	if (designator.getType().getKind() != Struct.Array) {
    		report_error ("Semanticka greska na liniji " + statementForeach.getLine() + " Designator " + designator.getName() + " mora oznacavati niz proizvoljnog tipa!", null );
    	}
    	else {
    		if (index == Tab.noObj) {
    			report_error ("Semanticka greska na liniji " + statementForeach.getLine() + " Indeksna promenljiva " + index.getName() + " mora biti deklarisana lokalna ili globalna promenljiva!", null );
    		}
    		else {
    			if (!(designator.getType().getElemType()==(index.getType()))) {
    				report_error ("Semanticka greska na liniji " + statementForeach.getLine() + " Indeksna promenljiva " + index.getName() + " mora biti istog tipa kao i elementi niza koji se u nju smestaju!", null );
    			}
    		}
    	}
    	
    	indexElems.remove(indexElems.size() - 1);
    }
    
    public void visit(DesignatorStatementFuncCall designatorStatementFuncCall) {
    	Obj des = designatorStatementFuncCall.getDesStmtFuncCallDesignator().getDesignator().obj;
    	if (des.getKind() != Obj.Meth) {
    		report_error("Greska na liniji " + designatorStatementFuncCall.getLine()+ " Designator mora oznacavati nestaticku metodu unutrasnje klase ili  globalnu funkciju glavnog programa", null);
    	}
    	
    	// plus dodatak za proveru ActPars...
    	
    	Obj meth = currMethodsObjects.remove(currMethObjIndex-1);
    	
    	if (meth.getLevel() != rBrActParam) {
    		report_error("Greska na liniji " + designatorStatementFuncCall.getLine()+ " Broj formalnih parametara funkcije i broj agrumenata poziva se ne podudaraju!", null);
    	}
    	
    	currMethObjIndex--;
    	if (currMethObjIndex>0) {
    		rBrActParam = rBrActParams.remove(rBrActParams.size()-1);
    	}
    	else {
    		rBrActParam = 0;
    		//rBrActParam = 1;
    	}
    }
    
    public void visit(DesStmtFuncCallDesignator desStmtFuncCallDesignator) {
    	currMethodsObjects.add(desStmtFuncCallDesignator.getDesignator().obj);
    	if(currMethObjIndex>0) {
    		rBrActParams.add(rBrActParam);
    	}
    	currMethObjIndex++;
    	if (callFunctionClassField == 1) {
    		rBrActParam = 1;
    	}
    	else {
    		rBrActParam = 0;
    	}
    }
  
    
    public void visit(ActualParam actualParam) {
    	Obj meth = currMethodsObjects.get(currMethodsObjects.size()-1);
    	Collection<Obj> pars = meth.getLocalSymbols();
    	
    	if (callFunctionClassField == 1 && rBrActParam == 0) {
    		rBrActParam++;
    		return;
    	}
    	
    	Obj elem = null;
    	int i = 0;
    	for(Iterator<Obj> elems = pars.iterator(); elems.hasNext();) {
    		elem = elems.next();
    		if (i == rBrActParam) {
    			break;
    		}
    		i++;
    	}

    	if (i == pars.size() || elem == null) { // if elem == null
    		return;
    	}
    	
    	if (elem.getType().getKind() == Struct.Class || elem.getType().getKind() == Struct.Interface) { // posebna provera ako je ref na klasu
			if(!(checkCompatibleForClasses(elem.getType(), actualParam.getExpr().struct))) {
				report_error("Greska na liniji " + actualParam.getLine()+ " tip reference na klasu mora biti osnovna klasa za tip objekta koji mu se dodeljuje!", null);
			}
		}
    	else {
	    	if(!(actualParam.getExpr().struct.assignableTo(elem.getType()))) {
	    		report_error("Greska na liniji " + actualParam.getLine()+ " tip argumenta i formalnog parametra se ne podudaraju!", null);
	    	}
    	}
    	
    
    	rBrActParam++;
    	
    }
    
    public void visit(ActualParams actualParams) {
    	Obj meth  = currMethodsObjects.get(currMethodsObjects.size()-1);
    	Collection<Obj> pars = meth.getLocalSymbols();
    	
    	if (callFunctionClassField == 1 && rBrActParam == 0) {
    		rBrActParam++;
    		return;
    	}
    	
    	Obj elem = null;
    	int i = 0;
    	for(Iterator<Obj> elems = pars.iterator(); elems.hasNext();) {
    		elem = elems.next();
    		if (i == rBrActParam) {
    			break;
    		}
    		i++;
    	}
    	
    	if (i == pars.size()) {
    		return;
    	}
    	
    	if (elem.getType().getKind() == Struct.Class || elem.getType().getKind() == Struct.Interface) { // posebna provera ako je ref na klasu
			if(!(checkCompatibleForClasses(elem.getType(), actualParams.getExpr().struct))) {
				report_error("Greska na liniji " + actualParams.getLine()+ " tip reference na klasu mora biti osnovna klasa za tip objekta koji mu se dodeljuje!", null);
			}
		}
    	else {
	    	if(!(actualParams.getExpr().struct.assignableTo(elem.getType()))) {
	    		report_error("Greska na liniji " + actualParams.getLine()+ " tip argumenta i formalnog parametra se ne podudaraju!", null);
	    	}
    	}
    	rBrActParam++;
    }
    
    public void visit(DesignatorStatementINC designatorStatementINC) {
    	Obj des = designatorStatementINC.getDesignator().obj;
    	if (indexElems.size() > 0) { // znaci da sam u nekom levelu forEach-a!
    		for (int i = 0; i < indexElems.size() ; i++) {
	    		if (indexElems.get(i).equals(des)) {
	    			report_error("Greska na liniji " + designatorStatementINC.getLine()+ " Ne sme se vrsiti upis u promenljivu " + indexElems.get(i).getName(), null);
	    			return; // da li ovde ostaviti return ili ga pustiti nek izvrsi i ove dole provere...?
	    		}
    		}
    	}
    	if (des.getKind() != Obj.Var && des.getKind() != Obj.Elem && des.getKind() != Obj.Fld) {
    		report_error("Greska na liniji " + designatorStatementINC.getLine()+ " Designator mora oznacavati promenljivu, element niza ili polje unutar objekta", null);
    	}
    	else {
    		if (!(des.getType().compatibleWith(Tab.intType))) {
    			report_error("Greska na liniji " + designatorStatementINC.getLine()+ " Tip designatora mora biti int!", null);
    		}
    	}
    }

    
    public void visit(DesignatorStatementDEC designatorStatementDEC) {
    	Obj des = designatorStatementDEC.getDesignator().obj;
    	if (indexElems.size() > 0) { // znaci da sam u nekom levelu forEach-a!
    		for (int i = 0; i < indexElems.size() ; i++) {
	    		if (indexElems.get(i).equals(des)) {
	    			report_error("Greska na liniji " + designatorStatementDEC.getLine()+ " Ne sme se vrsiti upis u promenljivu " + indexElems.get(i).getName(), null);
	    			return; // da li ovde ostaviti return ili ga pustiti nek izvrsi i ove dole provere...?
	    		}
    		}
    	}
    	if (des.getKind() != Obj.Var && des.getKind() != Obj.Elem && des.getKind() != Obj.Fld) {
    		report_error("Greska na liniji " + designatorStatementDEC.getLine()+ " Designator mora oznacavati promenljivu, element niza ili polje unutar objekta", null);
    	}
    	else {
    		if (!(des.getType().compatibleWith(Tab.intType))) {
    			report_error("Greska na liniji " + designatorStatementDEC.getLine()+ " Tip designatora mora biti int!", null);
    		}
    	}
    }
    
    private boolean checkCompatibleForClasses(Struct des, Struct src) {
    	
    	//19.08.2020
    	if (des.getKind() == Struct.Array && src == Tab.nullType) { // ako je dst array, a src null to je ok
    		return true;
    	}
    	
    	if (des == null || src == null) {
    		return false;
    	}
    	
//    	if (src == Tab.nullType) {
//    		return true;
//    	}
    	
    	if (des.getKind() == Struct.Interface && src == Tab.nullType) {
    		return true;
    	}
    	
    	if (src.getKind() == Struct.Interface && des == Tab.nullType) {
    		return true;
    	}
    	
    	// 19.08.2020
    	if (des.getKind() == Struct.Class && src == Tab.nullType) {
    		return true;
    	}
    	
    	if (src.getKind() == Struct.Class && des == Tab.nullType) {
    		return true;
    	}
    	
    	if (src == Tab.nullType && des == Tab.nullType) {
    		return true;
    	}
    		
    	if (src==des) {
    		return true;
    	}
    	
    	
    	Struct basicClassStruct = src.getElemType();
    	
    	while(basicClassStruct != null) {
    		if (basicClassStruct == des){ // ili po referencama jednak...razmisli
    			return true;
    		}
    		basicClassStruct = basicClassStruct.getElemType();
    	}
    	
    	return false;
    }
    
    	//PROBAAAAAAAAAAAAAAAAAAA
//    public void visit(DesListElem desListElem) {
//    	if (!(desListElem.getDesVar().struct ==  Tab.intType) || !(desListElem.getDesignatorList().struct == Tab.intType)) {
//    		report_error("Greska na liniji " + desListElem.getLine()+ " operandi kombinovanog operatora moraju biti tipa int! ", null);
//    	}
//    	if(((DesAssStmtCombOper)desListElem.getDesAssStmtCombOperators()).getAssignop() instanceof Assign) {
//    		report_error("Greska na liniji " + desListElem.getLine()+ " operator ne sme biti =", null);
//    	}
//    	desListElem.struct = desListElem.getDesVar().struct;
//    }
    
//    public void visit(DesListEnd desListEnd) {
//    	desListEnd.struct = desListEnd.getExpr().struct;
//    }
    
    public void visit(DesListElem desListElem) {
    	if (!(desListElem.getDesVar().struct ==  Tab.intType) || !(desListElem.getExpr().struct == Tab.intType) 
    			|| (desListElem.getDesVar().getDesignator().obj.getKind() == Obj.Meth)) {
    		report_error("Greska na liniji " + desListElem.getLine()+ " operandi kombinovanog operatora moraju biti tipa int! ", null);
    	}
    	if(((DesAssStmtCombOper)desListElem.getDesAssStmtCombOperators()).getAssignop() instanceof Assign) {
    		report_error("Greska na liniji " + desListElem.getLine()+ " operator ne sme biti =", null);
    	}
    	desListElem.struct = desListElem.getDesVar().struct;
    }
   
    
    public void visit(DesAssStmtm desAssStmtm) {
    	Obj des = desAssStmtm.getDesignator().obj;
    	if (indexElems.size() > 0) { // znaci da sam u nekom levelu forEach-a!
    		for (int i = 0; i < indexElems.size() ; i++) {
	    		if (indexElems.get(i).equals(des)) {
	    			report_error("Greska na liniji " + desAssStmtm.getLine()+ " Ne sme se vrsiti upis u promenljivu " + indexElems.get(i).getName(), null);
	    			return; // da li ovde ostaviti return ili ga pustiti nek izvrsi i ove dole provere...?
	    		}
    		}
    	}
    	if (des.getKind() != Obj.Var && des.getKind() != Obj.Elem && des.getKind() != Obj.Fld) {
    		report_error("Greska na liniji " + desAssStmtm.getLine()+ " Designator mora oznacavati promenljivu, element niza ili polje unutar objekta", null);
    	}
    	else {
    		if (des.getType().getKind() == Struct.Class || des.getType().getKind() == Struct.Interface) { // posebna provera ako je ref na klasu
    			if(!(checkCompatibleForClasses(des.getType(), desAssStmtm.getExpr().struct))) {
    				report_error("Greska na liniji " + desAssStmtm.getLine()+ " tip reference na klasu mora biti osnovna klasa za tip objekta koji mu se dodeljuje!", null);
    			}
    		}
    		else if (!(desAssStmtm.getExpr().struct.assignableTo(des.getType()))) {
    			report_error("Greska na liniji " + desAssStmtm.getLine()+ " tip designatora kome se dodeljuje vrednost mora biti kompatibilan sa tipom vrednosti koja mu se dodeljuje!", null);
    		}
    	}
    	
    	DesAssStmtCombOper oper = (DesAssStmtCombOper) desAssStmtm.getDesAssStmtCombOperators();
    	
    	if (!(oper.getAssignop() instanceof Assign) && desAssStmtm.getDesignator().obj.getType() != Tab.intType) {
    		report_error("Greska na liniji " + desAssStmtm.getLine()+ " tip designatora u operaciji sa kombinovanim operatorima mora biti tipa int", null);
    	}
    }

    public void visit(CondFactRelop condFactRelop) {
    	Expr f = condFactRelop.getExpr();
    	Expr s = condFactRelop.getExpr1();
    	
    	if (f.struct.getKind() == Struct.Class || s.struct.getKind() == Struct.Class 
    			|| f.struct.getKind() == Struct.Interface || s.struct.getKind() == Struct.Interface) { // posebna provera ako je ref na klasu
			if((!(checkCompatibleForClasses(f.struct, s.struct))) && (!(checkCompatibleForClasses(s.struct, f.struct)))) {
				report_error("Greska na liniji " + condFactRelop.getLine()+ " tip reference na klasu mora biti osnovna klasa za tip objekta koji mu se dodeljuje!", null);
			}
		}
    	else {
	    	if (!(f.struct.compatibleWith(s.struct))) {
	    		report_error("Greska na liniji " + condFactRelop.getLine()+ " Tipovi oba izraza u uslovu moraju biti kompatibilni!", null);
	    	}
    	}
    	
    	if (f.struct.getKind() == Struct.Array || s.struct.getKind() == Struct.Array) {
    		if (!(condFactRelop.getRelop() instanceof Same || condFactRelop.getRelop() instanceof Different)) {
    			report_error("Greska na liniji " + condFactRelop.getLine()+ " Uz promenljive tipa niza smeju stajati samo operatori != ili ==", null);
    		}
    	}
    	
    	if (f.struct.getKind() == Struct.Class || s.struct.getKind() == Struct.Class || f.struct.getKind() == Struct.Interface || s.struct.getKind() == Struct.Interface) {
    		if (!(condFactRelop.getRelop() instanceof Same || condFactRelop.getRelop() instanceof Different)) {
    			report_error("Greska na liniji " + condFactRelop.getLine()+ " Uz promenljive tipa niza smeju stajati samo operatori != ili ==", null);
    		}
    	}
    	
    }
    
    public void visit(CondFactNoRelop cond) {
    	if (!(cond.getExpr().struct.compatibleWith(boolType))) {
    		report_error("Greska na liniji " + cond.getLine()+ " Tipovi izraza u uslovu mora biti tipa bool!", null);
    	}
    }
    
    public void visit(NoMinusExpr minusExpr) {
    	minusExpr.struct = minusExpr.getTerm().struct;
    }
    
    public void visit(MinusExpr minusExpr) {
//    	if (minusExpr. != Tab.intType) { 
//    		report_error("Greska na liniji " + minusExpr.getLine()+ " Term mora biti tipa int!", null);
//    	}
//    	
//    	Provera za condFact...
//    	Terms t = (Terms) minusExpr.getTerm();
//    	Factor f= t.getFactor();
//    	if (f instanceof Var) {
//    		//proveri tip za Var...
//    	}
    	
    	minusExpr.struct = minusExpr.getMinusTerm().struct;
    }
    
    public void visit(MinusTerm minusTerm) {
    	if (minusTerm.getTerm().struct != Tab.intType) {
    		report_error("Greska na liniji " + minusTerm.getLine()+ " Term mora biti tipa int!", null);
    	}
    	minusTerm.struct = minusTerm.getTerm().struct;
    }
    
    public void visit(ExprListElems exprListElems) {
    	if (exprListElems.getTerm().struct != Tab.intType) {
    		report_error("Greska na liniji " + exprListElems.getLine()+ " Term mora biti tipa int!", null);
    	}
    }
    
    public void visit(Terms terms) {
    	terms.struct = terms.getFactor().struct;
    }
    
    public void visit(TermListElems termListElems) {
    	if (termListElems.getFactor().struct != Tab.intType) {
    		report_error("Greska na liniji " + termListElems.getLine()+ " Factor mora biti tipa int!", null);
    	}
    }
    
    // ***DODATAK ZA JUN***
    public void visit (ListAdd listAdd) {
//    	if (listAdd.getAddop() instanceof AddRight) {
//    		if (isDesignator == null || isDesignator == Tab.noObj) {
//	    		report_error("Greska na liniji " + listAdd.getLine()+ " Uz kombinovani operator mora stajani neki Designator!", null);
//	    	}
//    		else {
//    			if (indexElems.size() > 0) { // znaci da sam u nekom levelu forEach-a!
//    	    		for (int i = 0; i < indexElems.size() ; i++) {
//    		    		if (indexElems.get(i).equals(isDesignator)) {
//    		    			report_error("Greska na liniji " + listAdd.getLine()+ " Ne sme se vrsiti upis u promenljivu " + indexElems.get(i).getName(), null);
//    		    			return; // da li ovde ostaviti return ili ga pustiti nek izvrsi i ove dole provere...?
//    		    		}
//    	    		}
//    	    	}
//    		}
//    	}
    }
    
    public void visit(ListMul listMul) {
//    	if (listMul.getMulop() instanceof MulRight) {
//	    	if (isDesignator == null || isDesignator == Tab.noObj) {
//	    		report_error("Greska na liniji " + listMul.getLine()+ " Uz kombinovani operator mora stajani neki Designator!", null);
//	    	}
//	    	else {
//	    		if (indexElems.size() > 0) { // znaci da sam u nekom levelu forEach-a!
//    	    		for (int i = 0; i < indexElems.size() ; i++) {
//    		    		if (indexElems.get(i).equals(isDesignator)) {
//    		    			report_error("Greska na liniji " + listMul.getLine()+ " Ne sme se vrsiti upis u promenljivu " + indexElems.get(i).getName(), null);
//    		    			return; // da li ovde ostaviti return ili ga pustiti nek izvrsi i ove dole provere...?
//    		    		}
//    	    		}
//    	    	}
//	    	}
//    	}
    }
    
    // ***KRAJ DODATKA ZA JUN***
    
    public void visit(NumberFactor numberFactor) {
    	numberFactor.struct = Tab.intType;
    	isDesignator = null;
    }
    
    public void visit(CharFactor charFactor) {
    	charFactor.struct = Tab.charType;
    	isDesignator = null;
    }
    
    public void visit(BoolFactor boolFactor) {
    	boolFactor.struct = boolType;
    	isDesignator = null;
    }
    
    public void visit(Var var) {
    	if (var.getDesignator().obj.getKind() == Obj.Meth) {
    		report_error("Greska na liniji " + var.getLine()+ " promenljiva je tipa funkcije " +var.getDesignator().obj.getName(), null);
    	}
    	var.struct = var.getDesignator().obj.getType();
    	isDesignator = var.getDesignator().obj;
    }
    
    public void visit(DesVar desVar) {
    	desVar.struct = desVar.getDesignator().obj.getType();
    	isDesignator = desVar.getDesignator().obj;
    	
    	
    	if (indexElems.size() > 0) { // znaci da sam u nekom levelu forEach-a!
    		for (int i = 0; i < indexElems.size() ; i++) {
	    		if (indexElems.get(i).equals(isDesignator)) {
	    			report_error("Greska na liniji " + desVar.getLine()+ " Ne sme se vrsiti upis u promenljivu " + indexElems.get(i).getName(), null);
	    			return; // da li ovde ostaviti return ili ga pustiti nek izvrsi i ove dole provere...?
	    		}
    		}
    	}
    }
    
    public void visit(ExprFactor exprFactor) {
    	exprFactor.struct = exprFactor.getExpr().struct;
    	isDesignator = null;
    }
    
    public void visit(FuncCallFactor funcCallFactor) { // bez provere za Act Parametre!
    	funcCallFactor.struct = funcCallFactor.getFuncCallFactorDesignator().getDesignator().obj.getType();
    	if (funcCallFactor.getFuncCallFactorDesignator().getDesignator().obj.getKind() != Obj.Meth) {
    		report_error("Greska na liniji " + funcCallFactor.getLine()+ " Designator mora oznacavati nestaticku metodu unutrasnje klase ili  globalnu funkciju glavnog programa!", null);
    	}
    	if(funcCallFactor.getFuncCallFactorDesignator().getDesignator().obj.getType().compatibleWith(Tab.noType)) {
    		report_error("Greska na liniji " + funcCallFactor.getLine()+ " Funkcija ne sme biti VOID!", null);
    	}
    	// dodati proveru da li su tipovi ActPars ekv. sa tipovima FormPars...
    	Obj meth = currMethodsObjects.remove(currMethObjIndex-1);
    	
    	if (meth.getLevel() > rBrActParam) {
    		report_error("Greska na liniji " + funcCallFactor.getLine()+ " Broj formalnih parametara funkcije i broj agrumenata poziva se ne podudaraju!", null);
    	}
    	
    	currMethObjIndex--;
    	if (rBrActParams.size()>0) {
    		rBrActParam = rBrActParams.remove(rBrActParams.size()-1);
    	}
    	else {
    		rBrActParam = 0;
    	}
    	
    	isDesignator = null;
    	
    }
    
    
    public void visit(FuncCallFactorDesignator des) {
    	currMethodsObjects.add(des.getDesignator().obj);
    	if(currMethObjIndex>0) {
    		rBrActParams.add(rBrActParam);
    	}
    	currMethObjIndex++;
    	if (callFunctionClassField == 1) {
    		rBrActParam = 1;
    	}
    	else {
    		rBrActParam = 0;
    	}
    }
    
    
    public void visit(NewFactor newFactor) { // vrv na nekom mestu treba dodati proveru dal se ovo nizu pripisuje!
    	newFactor.struct = new Struct(Struct.Array, newFactor.getType().struct);
    	if(newFactor.getExpr().struct != Tab.intType) {
    		report_error("Greska na liniji " + newFactor.getLine()+ " Izraz izmedju [] pri definisanju niza mora biti tipa int (izraz predstavlja broj elemenata niza)!", null);
    	}
    	
    	isDesignator = null;
    }
    
    public void visit(NewClass newClass) {
    	if (newClass.getType().struct.getKind() != Struct.Class) {
    		report_error("Greska na liniji " + newClass.getLine()+ " tip objekta koji se pravi mora da oznacava unutrasnju klasu!", null);
    	}
    	newClass.struct = newClass.getType().struct;
    }
    
    
    
    public void visit(DesignatorIdent designatorIdent) {
    	Obj obj = null;
    	if (currentClass != null || currentAbsClass != null) {
    		obj = myFindForClass(designatorIdent.getName(), designatorIdent);
    	}
    	else {
    		obj = Tab.find(designatorIdent.getName());
    		if (obj.getKind() == Obj.Meth) {
        		report_info("Detektovan poziv  globalne f-je:   " + designatorIdent.getName() + " na liniji " + designatorIdent.getLine(), designatorIdent);
    			callFunctionClassField = 0;
        	}
    	}
    	designatorIdent.obj = obj;
    	designatorObject = obj; // potrebno je !
    }
    

    
    private Obj myFindForClass(String name, DesignatorIdent desIdent) {
		// TODO Auto-generated method stub
    	Obj rez = findInMyScope(name); // provera da li je lokalna promenljiva trenutne funkcije
    	if(rez!= Tab.noObj) {
    		return rez;
    	}
    	
    	Struct classStruct = null;
    	if(currentClass != null) {
    		classStruct = currentClass;
    	}
    	else {
    		classStruct = currentAbsClass;
    	}
    	
    	Obj resultObj = null;
    	SymbolDataStructure members = Tab.currentScope().getOuter().getLocals(); // nisu dodati u classStruct, ali se nalaze u scope, vec su sig svi navedeni
    	
    	while (classStruct != null) { // u sebi ga trazis ...ako si ga nasao pristupas preko implicitnog this!
    		if (members != null) {
    			resultObj = members.searchKey(name);
    			if (resultObj != null) break;
    		}
    		
    		classStruct = classStruct.getElemType();
    		if (classStruct != null) {
    			members = classStruct.getMembersTable();
    		}
    	}
    	
    	if (resultObj != null) {
    		Struct trenutna = null;
    		if (currentClass != null)
    			trenutna = currentClass;
    		if (currentAbsClass != null)
    			trenutna = currentAbsClass;
//    		if (trenutna == null) // dal je moguce? ---> jeste u main-u npr ---> ovde nece moci jel sig zovem iz klase...
//    			return;
    		// znaci polje sam klase "klasa" ; zovem se sa . nesto...sig sam polje ili metoda klase
    		if (resultObj.getKind() ==  Obj.Fld || resultObj.getKind() == Obj.Meth) { 
    			if (resultObj.getFpPos() == 1 || resultObj.getFpPos() == -9) { // public
    				if (resultObj.getKind() == Obj.Meth) {
    	    			report_info("Detektovan poziv  metoda unutrasnje klase:   " + desIdent.getName() + " na liniji " + desIdent.getLine(), desIdent);
    	    			callFunctionClassField = 1;
    	    		}
    				return resultObj;
    			}
    			
    			if (resultObj.getFpPos() == 2 || resultObj.getFpPos() == -8) { // protected
    				if (resultObj.getKind() == Obj.Meth) {
    	    			report_info("Detektovan poziv  metoda unutrasnje klase:   " + desIdent.getName() + " na liniji " + desIdent.getLine(), desIdent);
    	    			callFunctionClassField = 1;
    	    		}
    				return resultObj;
    			}
    			
    			if (resultObj.getFpPos() == 3 || resultObj.getFpPos() == -7) { // private
    				if (trenutna != classStruct) {
    					report_error("Greska na liniji " + desIdent.getLine()+ " : polju "+desIdent.getName()+" se ne sme pristupati na ovom mestu, private je!", null);
    				}
    				else {
    					if (resultObj.getKind() == Obj.Meth) {
    		    			report_info("Detektovan poziv  metoda unutrasnje klase:   " + desIdent.getName() + " na liniji " + desIdent.getLine(), desIdent);
    		    			callFunctionClassField = 1;
    		    			return resultObj;
    		    		}
    				}
    			}
    		}
    		
//    		if (resultObj.getKind() == Obj.Meth) {
//    			report_info("Detektovan poziv  metoda unutrasnje klase:   " + desIdent.getName() + " na liniji " + desIdent.getLine(), desIdent);
//    			callFunctionClassField = 1;
//    		}
    		return resultObj;
    	}
    	
    	Obj meth = Tab.find(name); // ako je global...
    	if (meth.getKind() == Obj.Meth) {
    		report_info("Detektovan poziv  globalne f-je:   " + desIdent.getName() + " na liniji " + desIdent.getLine(), desIdent);
			callFunctionClassField = 0;
    	}
    	
    	return meth;
	}


    
	public void visit(Design designator) { // smena predstavlja koriscenje simbola!
    	Obj obj = null;
		
    	obj = designatorObject;
    	
    	if (obj == null) {
    		obj = Tab.noObj;
    	}
		
    	if (obj == Tab.noObj) {
			report_error("Greska na liniji " + designator.getLine()+ " : ime "+obj.getName()+" nije deklarisano! ", null);
			designator.obj = obj;
			return;
    	}
    	
    	
    	// A nivo
    	if(obj.getKind() == Obj.Con) {
			report_info("Detektovan pristup simbolickoj konstanti:   " + designator.getDesignatorIdent().getName() + " na liniji " + designator.getLine(), designator);
		}
		
		if(obj.getKind() == Obj.Var && obj.getLevel() == 0) {
			report_info("Detektovan pristup globalnoj promenljivoj:   " + designator.getDesignatorIdent().getName() + " na liniji " + designator.getLine(), designator);
		}
		
		if (obj.getKind() == Obj.Elem) {
			report_info("Detektovan pristup elementu niza:   " + obj.getName() + " na liniji " + designator.getLine(), designator);
		}
		
		// B nivo
		
//		if (obj.getKind() == Obj.Meth && designator.getDesignatorElemList() instanceof MyNoDesignatorElemList) { 
//			report_info("Detektovan poziv  globalne f-je:   " + designator.getDesignatorIdent().getName() + " na liniji " + designator.getLine(), designator);
//			callFunctionClassField = 0;
//		}
		
		if (obj.getKind() == Obj.Var  && obj.getLevel() != 0 && currentMethod != null && currentMethod.getLevel() > obj.getAdr()) {
			report_info("Detektovan pristup formalnom parametru f-je:   " + obj.getName() + " na liniji " + designator.getLine(), designator);
		}
	
		// C nivo 
	
	
		if(obj.getKind() == Obj.Fld) {
			report_info("Detektovan pristup polju klase:   " + obj.getName() + " na liniji " + designator.getLine(), designator);
		}
		
		if (obj.getKind() == Obj.Meth && !(designator.getDesignatorElemList() instanceof MyNoDesignatorElemList)) { 
			report_info("Detektovan poziv  metoda unutrasnje klase:   " + obj.getName() + " na liniji " + designator.getLine(), designator);
			callFunctionClassField = 1;
		}

    	designator.obj = obj;
    }
    
    private Obj checkForArray(ArrayElem arrayElem, Obj obj) { // mora da proveri da li je ovaj obj tipa niz
    	if (obj == Tab.noObj) {
			report_error("Greska na liniji " + arrayElem.getLine()+ " : ime "+obj.getName()+" nije deklarisano! ", null);
    	}
    	
    	if (obj.getType().getKind() == Struct.Array) {
    		String name = obj.getName();
    		int adr = obj.getAdr();
    		int lev = obj.getLevel();
    		obj = new Obj(Obj.Elem,name,obj.getType().getElemType());
    		obj.setAdr(adr); // sta ce mi ovo?
    		obj.setLevel(lev); // sta ce mi ovo?
    		
    		//report_info("Detektovan pristup elementu niza:  " + obj.getName() + " na liniji " + arrayElem.getLine(), arrayElem);
    		
    		return obj;
    	}else if (obj.getType().getKind() != Struct.Array ) {
    		report_error("Greska na liniji " + arrayElem.getLine()+ " navedeno ime nije definisamo kao ime niza", null);
    	}
    	
    	return Tab.noObj;
    }
    
    
    public void visit(ArrayElem arrayElem) {
    	
    	if (!(arrayElem.getExpr().struct.compatibleWith(Tab.intType))) {
    		report_error("Greska na liniji " + arrayElem.getLine()+ " izraz u [] mora biti tipa int!", null);
		}
    	
   
    	if (arrayElem.getParent() instanceof MyDesignatorElemList) { // ako nisam poslednji elem u listi
    		MyDesignatorElemList parent = (MyDesignatorElemList) arrayElem.getParent();
    		if(parent.getParent() instanceof Design) {
    			Design des = (Design) parent.getParent();
    			Obj obj = checkForArray(arrayElem, des.getDesignatorIdent().obj);
    			arrayElem.obj = obj;
    		}
    		else if(parent.getParent() instanceof MyDesignatorElemList) {
    			MyDesignatorElemList myDesList = (MyDesignatorElemList) parent.getParent();
    			if (myDesList.getDesignatorElem() instanceof ArrayElem) {
    				arrayElem.obj = null;
    				report_error("Greska na liniji " + arrayElem.getLine()+ " nema definisanja visedimenzionih nizova!", null);
    			}
    			else if(myDesList.getDesignatorElem() instanceof ClassField) {
    				ClassField fld = (ClassField) myDesList.getDesignatorElem();
    				Obj obj = checkForArray(arrayElem, fld.obj);
    				arrayElem.obj = obj;
    			}
    		}
    		
    		if (parent.getDesignatorElemList() instanceof MyNoDesignatorElemList) { // poslednji sam u stablu...
				designatorObject = arrayElem.obj;
			}
    	}	
    }
 
 
    private void checkForCLassField(ClassField classField, Obj obj) { // obj mora biti klasa ---> a.field
    	if (obj == Tab.noObj) {
			report_error("Greska na liniji " + classField.getLine()+ " : ime "+obj.getName()+" nije deklarisano! ", null);
    	}
    
    	
    	if (obj.getType().getKind() != Struct.Class && obj.getType().getKind() != Struct.Interface) {
    		report_error("Greska na liniji " + classField.getLine()+ " : ime "+ obj.getName() +" mora oznacavati objekat unutrasnje klase!", null);
    	}
    	
    	Collection<Obj> lokals = null;
    	
    	if (obj.getName().equals("this")) { // ja ovo zovem iz funkcije klase, zaro uzimam scope klase; moze samo prvi put (this.a)
    		lokals = Tab.currentScope().getOuter().getLocals().symbols();
    	}
    	else {
    		lokals = obj.getType().getMembers();
    	}
    
    	Struct klasa = obj.getType();
    	Obj elem = null;
    	int ok = 0 ;
    	
    	while (klasa != null) {
	    	elem = null;
	    	for(Iterator<Obj> iterator = lokals.iterator();iterator.hasNext();) {
	    		elem = iterator.next();
	    		if (elem.getName().equals(classField.getFieldName())) {
	    			ok = 1;
	    			break;
	    		}
	    	}
	    	
	    	if (ok == 1)
	    		break;
	    	klasa = klasa.getElemType();
	    	if (klasa != null) {
	    		lokals = klasa.getMembers();
	    	}
    	}
    	
    	classField.obj = elem;
    	
    	if (ok == 0) {
    		classField.obj = Tab.noObj;
    		report_error("Greska na liniji " + classField.getLine()+ " : ime "+classField.getFieldName()+" mora biti polje ili metoda navedene unutrasnje klase!", null);
    	}
    	else {
    		Struct trenutna = null;
    		if (currentClass != null)
    			trenutna = currentClass;
    		if (currentAbsClass != null)
    			trenutna = currentAbsClass;
//    		if (trenutna == null) // dal je moguce? ---> jeste u main-u npr
//    			return;
    		// znaci polje sam klase "klasa" ; zovem se sa . nesto...sig sam polje ili metoda klase
    		if (elem.getKind() ==  Obj.Fld || elem.getKind() == Obj.Meth) { 
    			if (elem.getFpPos() == 1 || elem.getFpPos() == -9) { // public
    				return;
    			}
    			
    			if (elem.getFpPos() == 2 || elem.getFpPos() == -8) { // protected
    				if (trenutna == null) {
    					report_error("Greska na liniji " + classField.getLine()+ " : polju "+classField.getFieldName()+" se ne sme pristupati na ovom mestu, protected je!", null);
    				}
    				else {
    					if (!(checkCompatibleForClasses(klasa, trenutna))) { //  ako je klasa u kojoj se nalazim NIJE izvedena iz klase iz koje potice field
    						report_error("Greska na liniji " + classField.getLine()+ " : polju "+classField.getFieldName()+" se ne sme pristupati na ovom mestu, protected je!", null);
    					}
    				}
    			}
    			
    			if (elem.getFpPos() == 3 || elem.getFpPos() == -7) { // private
    				if (trenutna != klasa) {
    					report_error("Greska na liniji " + classField.getLine()+ " : polju "+classField.getFieldName()+" se ne sme pristupati na ovom mestu, private je!", null);
    				}
    			}
    		}
    		
//    		if (elem.getKind() == Obj.Meth) {
//    			if (elem.getFpPos() == 1 || elem.getFpPos() == -9) { // nikad ne bi smeo ni da zove aps ... [-9,-8,-7] 
//    			//ONDA OSTAJE ISTI USLOV ZA METH I FLD	
//    			}
//    		}
    	}
    	
    }
    
    public void visit(ClassField classField) {
    	
    	if (classField.getParent() instanceof MyDesignatorElemList) {
    		MyDesignatorElemList parent = (MyDesignatorElemList) classField.getParent();
    		if(parent.getParent() instanceof Design) {
    			Design des = (Design) parent.getParent();
    			checkForCLassField(classField, des.getDesignatorIdent().obj);
    		}
    		else if(parent.getParent() instanceof MyDesignatorElemList) {
    			MyDesignatorElemList myDesList = (MyDesignatorElemList) parent.getParent();
    			if (myDesList.getDesignatorElem() instanceof ArrayElem) {
    				ArrayElem arrayElem = (ArrayElem) myDesList.getDesignatorElem();
    				checkForCLassField(classField, arrayElem.obj);
    				
    			}
    			else if(myDesList.getDesignatorElem() instanceof ClassField) {
    				ClassField fld = (ClassField) myDesList.getDesignatorElem();
    				checkForCLassField(classField, fld.obj);
    			}
    		}
    		if (parent.getDesignatorElemList() instanceof MyNoDesignatorElemList) { // poslednji element u stablu...
				designatorObject = classField.obj;
			}
    	}	
    	report_info("Detektovan pristup polju klase:  " + classField.getFieldName() + " na liniji " + classField.getLine(), classField);
    	
    }
    
//    public void visit(FuncCall funcCall) {
//    	Obj func = funcCall.getDesignator().obj;
//    	if(Obj.Meth == func.getKind()){
//    		if(Tab.noType == func.getType()) {
//    			report_error("Semanticka greska " + func.getName()  + " ne moze se koristiti u izrazima jer nema povratnu vrednost ", null);
//    		}else {
//    			report_info("Pronadjen poziv funkcije " + func.getName() + " na liniji " + funcCall.getLine(), null);
//    			funcCall.struct = func.getType();
//    		}
//    	}else{
//			report_error("Greska na liniji " + funcCall.getLine()+" : ime " + func.getName() + " nije funkcija!", null);
//			funcCall.struct = Tab.noType;
//    	}
//    }
//    
//    public void visit(ReturnExpr returnExpr) {
//    	returnFound = true;
//    	Struct currMethType = currentMethod.getType();
//    	if(!currMethType.compatibleWith(returnExpr.getExpr().struct)){
//			report_error("Greska na liniji " + returnExpr.getLine() + " : " + "tip izraza u return naredbi ne slaze se sa tipom povratne vrednosti funkcije " + currentMethod.getName(), null);
//    	}
//    }
//    
//    public void visit(AddExpr addExpr) {
//    	Struct first = addExpr.getExpr().struct;
//    	Struct second = addExpr.getTerm().struct;
//
//    	if(first.equals(second) && first == Tab.intType) {
//    		addExpr.struct = first;
//    	}
//    	else {
//    		report_error("Greska na liniji "+ addExpr.getLine()+" : nekompatibilni tipovi u izrazu za sabiranje.", null);
//			addExpr.struct = Tab.noType;
//    	}
//    	
//    }
//
//    public void visit(TermExpr termExpr) {
//    	termExpr.struct = termExpr.getTerm().struct;
//    }
//    
//    public void visit(Term term) {
//    	term.struct = term.getFactor().struct;
//    }
//    
//    public void visit(Const c) {
//    	c.struct = Tab.intType;
//    }
//    
//    public void visit(Var v) {
//    	Obj obj = v.getDesignator().obj;
//    	if(Obj.Var == obj.getKind()) {
//    		v.struct = obj.getType();	
//    	}
//    	else {
//    		v.struct = Tab.noType;
//    	}
//    }
//    
//    public void visit(Assignment assignment) {
//    	if(!assignment.getExpr().struct.assignableTo(assignment.getDesignator().obj.getType()))
//    		report_error("Greska na liniji " + assignment.getLine() + " : " + "nekompatibilni tipovi u dodeli vrednosti! ", null);
//    }

    public boolean passed() {
    	return !errorDetected;
    }

	public int getnVars() {
		return nVars;
	}

	public void setnVars(int nVars) {
		this.nVars = nVars;
	}
    

	private Obj findInClassScope(String className) {
		Obj klasa = Tab.find(className);
		
		Struct classStruct = klasa.getType();
    	
    	
    	Obj resultObj = null;
    	
		SymbolDataStructure members = classStruct.getMembersTable(); 
	

		if (members != null) {
			resultObj = members.searchKey(className);
		}
    	
    	return resultObj;
	}
}
