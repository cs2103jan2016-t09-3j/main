package Storage;

import java.lang.reflect.Type;
import java.util.Calendar;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class CalendarSerializer implements JsonSerializer<Calendar>, JsonDeserializer<Calendar> {


	@Override
	public Calendar deserialize(JsonElement cal, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonElement serialize(Calendar cal, Type typeOfT, JsonSerializationContext conten) {
		// TODO Auto-generated method stub
		return null;
	}
}
