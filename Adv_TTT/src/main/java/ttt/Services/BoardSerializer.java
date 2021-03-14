package ttt.Services;

import org.springframework.stereotype.Service;

@Service
public class BoardSerializer {
	public String serializeBoard(String[] serializeMe) {
		StringBuilder sb = new StringBuilder();
		for (String s : serializeMe) {
			sb.append(s + ",");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
}