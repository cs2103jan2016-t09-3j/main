package Parser;
import java.util.ArrayList;
import java.text.ParseException;


public class TNotesParser {
	
	public interface Parser {
		public Object parse(String input);
	}
	
	public static ArrayList<String> timeList = new ArrayList<String>();
	TNotesParserAdd add;
	TNotesParserChange change;
	TNotesParserDelete delete;
	TNotesParserSet set;
	TNotesParserSort sort;
	TNotesParserSearch search;
	TNotesParserTime time;
	TNotesParserDate date;
	TNotesParserView view;
	TNotesParserEdit edit;
	
	public TNotesParser(){
		add = new TNotesParserAdd();
		change = new TNotesParserChange();
		delete = new TNotesParserDelete();
		set = new TNotesParserSet();
		sort = new TNotesParserSort();
		search = new TNotesParserSearch();
		time = new TNotesParserTime();
		date = new TNotesParserDate();
		view = new TNotesParserView();
		edit = new TNotesParserEdit();
	}
	//private ArrayList<String> list = new ArrayList<String>();
	public static void main(String[] args) throws ParseException{
		TNotesParser parser = new TNotesParser();
		parser.execute();
		//System.out.println("haha");
		
	}
	public void execute() throws ParseException{
		//Month month = Month.august;
		String output = new String();
		String input = new String();  
		input = "edit call from to due at  mom details buy apple";
		for (int i = 0; i < checkCommand(input).size(); i++){
			output = checkCommand(input).get(i);
			System.out.println(output);
		}
	}
	
	
	
	public ArrayList<String> checkCommand(String inputString) throws ParseException {
		ArrayList<String> list = new ArrayList<String>();
		
		String arr[] = inputString.split(" ");
		String firstWord = arr[0].toLowerCase();
		
		switch(firstWord){
			case "add" :

				list.add(firstWord);
				list.addAll(add.addCommand(arr));
				//System.out.println(list);
				
				return list;
			case "view" :
				
				list.add(firstWord);
				list.addAll(view.viewCommand(arr));
				
				return list;
			case "edit" :
				
				list.add(firstWord);
				list.addAll(edit.editCommand(arr));
				
				return list;	
			case "delete" :
				
				list.addAll(delete.deleteCommand(arr));
				
				return list;
			case "search" :
				
				list.add(firstWord);
				list.addAll(search.searchCommand(arr));
				
				return list;
			case "sort" :
				
				list.add(firstWord);
				list.addAll(sort.sortCommand(arr));
				
				//return list;
				break;
			case "help" :
				
				list.add(firstWord);
				
				return list;
			case "exit" :
				
				list.add(firstWord);
				
				return list;
			case "set" :
				
				list.add(firstWord);
				list.addAll(set.setCommand(arr));
				
				return list;
			case "change" :
				
				list.addAll(change.changeCommand(arr));
				
				//return list;
				break;
				
			//default :
				// throw new ParseException("Invald Command!");
		}
		return list;

		
	}

}
