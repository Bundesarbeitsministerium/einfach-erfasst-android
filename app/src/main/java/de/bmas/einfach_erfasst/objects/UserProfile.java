package de.bmas.einfach_erfasst.objects;

import io.realm.RealmObject;

public class UserProfile extends RealmObject
{
    private String firstName;
    private String lastName;
    private String email;
    private boolean accepted;

    public String getFirstName () { return this.firstName; }
    public void setFirstName (String firstName) { this.firstName = firstName; }
    public String getLastName () { return this.lastName; }
    public void setLastName (String lastName) { this.lastName = lastName; }
    public String getEmail () { return this.email; }
    public void setEmail (String email) { this.email = email; }
    public boolean isAccepted () { return this.accepted; }
    public void setAccepted (boolean accepted) { this.accepted = accepted; }
}
