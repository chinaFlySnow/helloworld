package ppd.entity;

public class Profile {
  private double profile;

public double getProfile() {
	return profile;
}

public void setProfile(double profile) {
	this.profile = profile;
}

@Override
public String toString() {
	return "Profile [profile=" + profile + "]";
}

}
