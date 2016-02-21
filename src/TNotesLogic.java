import java.util.ArrayList;

public class TNotesLogic {
<<<<<<< HEAD

	public static void main(String[] args) {
		
				
	}
	
	public TaskFile createEvent(String secondPart){
		TNotesParser parser = new TNotesParser();
		ArrayList<String> list = new ArrayList<String>();		
		list = parser.checkCommand(// inputhere);
		// pass to adam, create file, return false/true, get error details	
				
			
		return TaskFile;		
	}
	
	public boolean deleteEvent(String eventName){
	// pass to adam to delete? or i delete ?
		
	return flag;	
	}
=======
>>>>>>> 1186c82ceb40180dd481cdc7d99a90cced1b0a62
	
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
