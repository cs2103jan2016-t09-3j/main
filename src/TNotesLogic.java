import java.util.ArrayList;

public class TNotesLogic {

	
	TNotesParser parser = new TNotesParser();
	
	ArrayList<String> list = new ArrayList<String>();
	
	
	public boolean addTask(String whatever){
		list = parser.checkCommand(whatever);
		TNotesStorage storage = new TNotesStorage(list);
		if(storage.addThisEvent()){
			return true;
		}
		else{
			return false;
		}
	}
	
	
}
