package testdata;

public class TestData02 {

	String username;
	private String password;
	public int sex;
	private String[] habits;


	public String[] getHabits() {
		return habits;
	}

	public void setHabits(String[] habits) {
		this.habits= habits;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

}
