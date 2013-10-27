package p2p.client;

public class Users implements UsersInteface{
	private String name;
	private String key;
	private String active;
	
	public Users(String names,String keys,String actives){
		this.name = names;
		this.key = keys;
		this.active = actives;
	}

	@Override
	public String getKey() {
		return this.key;
	}

	@Override
	public void setkey(String key) {
		this.key = key;
		
	}
	
	@Override
	public String getActive() {
		return this.active;
	}

	@Override
	public void setActive(String actives) {
		this.active = actives;
		
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
		
	}
	
}
