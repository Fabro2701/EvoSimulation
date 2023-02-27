package genome_editing.model.genome;

import java.util.ArrayList;
import java.util.List;

import simulator.Constants;


public class BaseSet extends ArrayList<Base>{
	public int position;
	public BaseSet(int pos) {
		super(Constants.PLOIDY);
		this.position = pos;
		for(int i=0;i<Constants.PLOIDY;i++) {
			add(new Base());
		}
	}
}
