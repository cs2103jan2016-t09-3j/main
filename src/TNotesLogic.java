import java.util.ArrayList;

public class TNotesLogic {

	
	TNotesParser parser = new TNotesParser();
	TNotesStorage storage = new TNotesStorage();
	ArrayList<String> list = new ArrayList<String>();
	
	
	public boolean addTask(String whatever){
		list = parser.checkCommand(whatever);
		
		if(storage.addEvent(list)){
			return true;
		}
		else{
			return false;
		}
	}
	
	
}
