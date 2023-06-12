import java.util.*;
import java.io.*;
public class wordle{
	public static void main(String [] args){
		game g=new game();
	}
}
class game{
	private String[] word;
	private ArrayList<String> words;
	public game(){	
		String ii="";
		String o="";
		String []no=new String[5];
		for (int i=0;i<5;i++){
			no[i]="";
		}
		while(true){
		
		int [][] am=new int[26][5];
		words=new ArrayList<String>();
		sortDict();
		Scanner in=new Scanner(System.in);
		System.out.print("revealed word so far(put dash where there is no letters)  ");
		String s=in.next();
		System.out.print("what did you type in ");
		String guess=in.next();
		for (int i=0; i<5; i++){
			if (s.charAt(i)==guess.charAt(i)){
				guess=guess.substring(0,i)+"-"+guess.substring(i+1);
			}
		}
		for (int i=0; i<5; i++){
			if (guess.charAt(i)!='-')
				no[i]+=""+guess.charAt(i);
		}
		System.out.print("letters with unknown spaces  ");
		String inp=in.next();
		if (!inp.equals("-"))
			ii+=inp;
		System.out.print("letters that dont exist in the word   ");
		o+=in.next();
		char[] inWord=ii.toCharArray();
		char[] outWord=o.toCharArray();
		if (!ii.equals("-"))	
		for (String w:word){
			if (w!=null)
			{
			int loop=0;
			int time=0;
			
			char []ppp=w.toCharArray();
			char []p=removeRepeat(ppp);
			for (char c:inWord){
			
				for (char v:p){
				if (c==v)
					time++;	
				}
			loop++;
			}
			if (time==loop)
				words.add(w);
		}
		}
		else
			for (String vs:word)
				words.add(vs);
		for (int i=0; i<words.size();i++){
			for (char c:words.get(i).toCharArray()){
				boolean run=true;
				for (char u:outWord){
					if (u==c)
						run=false;
					if (!run)
						break;
				}
				if (!run){
					words.remove(i);
					i--;
					break;
				}
			}
		}
		char[]finalCheck=s.toCharArray();
		for (int y=0; y<words.size(); y++){
			for (int i=0; i<5; i++){
				if (finalCheck[i]!='-'){
					if (finalCheck[i]!=words.get(y).toCharArray()[i]){
						words.remove(y);
						y--;
						break;
					}
				}
			}
		}
		

		for (int i=0; i<words.size();i++){
			char []ch=words.get(i).toCharArray();
			for (int q=0;q<ch.length;q++){
				boolean run=true;
				for (char c:no[q].toCharArray()){
					if (i==-1)
						i=0;
					if (c==ch[q]){
						words.remove(i);
						i--;
						run=false;
						break;
					}
					if (!run)
						break;
				}
			}
			
		}
		System.out.println(words); 
		System.out.println(words.size());
		for (String st:words){
			char []ch=st.toCharArray();
			for (int q=0;q<ch.length;q++){
				int num=(int)ch[q]-97;
				am[num][q]++;	
			}
		}
		int maxRow=0;
		char maxRowChar=0;
		char maxColRow=0;
		int maxCol=0;
		int maxColChar=0;
		for (int i=0; i<am.length;i++){
			int total=0;
			if(isLockedLetter((char)(i+97),s)){	
			for (int q=0; q<am[i].length;q++){
							
				if (maxCol<am[i][q]){
					maxColRow=(char)(i+97);
					maxColChar=q;
					maxCol=am[i][q];
				}
				total+=am[i][q];
				
			}
			if (total>maxRow){
				maxRowChar=(char)(i+97);
				maxRow=total;
			}
			}
		}
		System.out.println("most common letter "+maxRowChar+" "+maxRow);
		System.out.println("most common letter placement "+maxColRow+" space "+(maxColChar+1)+" times "+ maxCol);
		System.out.print("did you guess it? (y/n) ");
		String input=in.next();
		if (input.equals("y")){	
			ii="";
			o="";
			no=new String[5];
			for (int i=0;i<5;i++){
			no[i]="";
		}
		}
		}
	}
	public  void sortDict(){
		try{
			Scanner dict=new Scanner(new File("f.dat"));
			String []dic=new String[14855];
			int z=0;
			while(dict.hasNextLine()){
				z++;
				dict.nextLine();
			}
			dict=new Scanner(new File("order.dat"));
			for (int i=0; i<z; i++){
				dic[i]=dict.nextLine();
			}
			System.out.println(dict);
			word=dic;
		}
		catch(Exception e){
			System.out.println(e+"error");
		}
	}
	public  boolean checkWord(String s){
		int save=0;
		boolean use=false;
		for (int i=0; i<14855; i++){
			if (s.compareTo(word[i])>0&&!use){
				save=i;
				i+=100;
			}			
			else{
				if (!use)
					i=save;
				if (i>save+100)
					return false;
				use=true;
				if (s.equals(word[i]))
					return true;
			}
		}
		return false;
	}
	public char[] removeRepeat(char[] c){
		char[] unique=new char[c.length];
		int spot=0;
		for (int i=0;i<c.length;i++){
			boolean add=true;
			for (int x=0; x<i&&add; x++)
				if (unique[x]==c[i])
					add=false;
			if (add){
				unique[spot]=c[i];
				spot++;
			}
		}
		int x=0;
		while (x!=c.length&&unique[x]!=' '){
			x++;
		}
		char[] fin=new char[x];
		for (int i=0;i<x; i++){
			fin[i]=unique[i];
		}
		return fin;
	}
	public boolean isLockedLetter(char c,String s){
		for (char e:s.toCharArray()){
			if (e==c)
				return false;
		}
		return true;
	}
}
